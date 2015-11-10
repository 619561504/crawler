/**
 * @(#)ParserAction.java
 * 2013-11-20-上午11:40:13
 */
package com.qq.railgun.action.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.qq.railgun.action.Action;
import com.qq.railgun.action.bean.CheckResult;
import com.qq.railgun.bean.Node;
import com.qq.railgun.context.Context;
import com.qq.railgun.flow.configs.ActionConfigs;

/**
 * @author hanjiewu
 * 
 */
public class ParserAction implements Action {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qq.railgun.action.Action#process(com.qq.railgun.flow.configs.
	 * ActionConfigs)
	 */
	@Override
	public void process(ActionConfigs config, Context context) {
		Object lastProcessResult = context.getLastProcessResult();
		if (lastProcessResult instanceof String) {
			String content = (String) lastProcessResult;
			context.putLastProcessResult(parser(content, config, context));
		} else if (lastProcessResult instanceof List) {
			List<String> results = new ArrayList<String>();
			List<String> values = (List<String>) lastProcessResult;
			for (String value : values) {
				List<String> tt = parser(value, config, context);
				if (null != tt) {
					results.addAll(tt);
				}
			}
			context.putLastProcessResult(results);
		}
	}

	/**
	 * 解析值
	 * 
	 * @param content
	 * @param config
	 * @param context
	 * @return
	 */
	private List<String> parser(String content, ActionConfigs config,
			Context context) {
		List<String> rs = new ArrayList<String>();
		if (StringUtils.isNotBlank(config.getRule())) {
			Elements elements = Jsoup.parse(content).select(config.getRule());
			if (!elements.isEmpty()) {
				for (Element element : elements) {

					String text = getText(element, config);
					String value = regex(text, config, context);

					setValue(value, config, context);
					rs.add(value);
				}
			}
		} else {
			String value = regex(content, config, context);
			setValue(value, config, context);
			rs.add(content);
		}
		return rs;
	}

	/**
	 * 提取匹配到的元素的文本
	 * 
	 * @param element
	 * @param config
	 * @return
	 */
	private String getText(Element element, ActionConfigs config) {
		String text = StringUtils.EMPTY;

		if (StringUtils.isNotBlank(config.getAttr())) {
			text = element.attr(config.getAttr());
		} else {
			if (config.isText()) {
				text = element.text();
			} else {
				text = element.outerHtml();
			}
		}

		return text;
	}

	private void setValue(String value, ActionConfigs config, Context context) {
		if (config.isStrip() && null != value) {
			value = value.trim();
			value = trim(value);
		}

		if (StringUtils.isNotBlank(config.getSetField())
				&& null != context.getNode()) {
			Node node = context.getNode();
			List<String> values = node.get(config.getSetField());
			if (null == values) {
				values = new ArrayList<String>();
				node.put(config.getSetField(), values);
			}
			values.add(value);
		}
	}

	private String trim(String value) {
		int index = 0;
		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) == 160) {
				index++;
			} else {
				break;
			}
		}
		int end = value.length();
		for (int i = value.length() - 1; i >= 0; i--) {
			if (value.charAt(i) == 160) {
				end--;
			} else {
				break;
			}
		}

		if (end < index) {
			end = value.length();
		}
		return value.substring(index, end);
	}

	private String regex(String content, ActionConfigs config, Context context) {
		if (StringUtils.isNotBlank(config.getRegex())) {
			Pattern pattern = context.getRegex(config.getRegex());
			Matcher matcher = pattern.matcher(content);

			StringBuilder builder = new StringBuilder();
			while (matcher.find()) {
				if (matcher.groupCount() > 0) {
					for (int i = 1; i <= matcher.groupCount(); i++) {
						builder.append(matcher.group(i));
					}
				} else {
					builder.append(matcher.group());
				}
				builder.append(",");
			}
			if (builder.length() > 0) {
				return builder.substring(0, builder.length() - 1);
			}
			return builder.toString();
		}
		return content;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.qq.railgun.action.Action#check(com.qq.railgun.flow.configs.ActionConfigs
	 * )
	 */
	@Override
	public CheckResult check(ActionConfigs config, Context context) {
		// 检查正则是否有效
		if (StringUtils.isNotBlank(config.getRegex())) {
			try {
				Pattern pattern = Pattern.compile(config.getRegex());
				context.putRegex(config.getRegex(), pattern);
			} catch (PatternSyntaxException e) {
				return new CheckResult(false, "无效正则表达式！");
			}
		}
		// if (StringUtils.isNotBlank(config.getResultAction()))
		// {
		// if (null == ResultActions.getResultAction(config.getResultAction()))
		// {
		// return new CheckResult(false, "无效的结果处理动作");
		// }
		// }
		// 检查是否有改方法
		if (StringUtils.isNotBlank(config.getJsoupSelectAction())) {
			try {
				Method method = Elements.class.getDeclaredMethod(config
						.getJsoupSelectAction());
				context.putMethod(config.getJsoupSelectAction(), method);
			} catch (Exception e) {
				return new CheckResult(false, "Jsoup Elements 没有改方法");
			}
		}

		return CheckResult.SUCCESS;
	}
}
