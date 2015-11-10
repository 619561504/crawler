/**
 * @(#)TemplateManager.java
 * 2013-11-20-下午5:29:51
 */
package com.qq.railgun;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.qq.railgun.bean.Node;
import com.qq.railgun.context.Context;

/**
 * @author hanjiewu
 * 
 */
public class TemplateManager {
	private static Pattern pattern = Pattern.compile("\\$\\{([^{$}]*)}");

	/**
	 * 用于替换url中的占位符
	 * 
	 * @param template
	 * @param context
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String merge(String template, Context context) {
		if (StringUtils.isBlank(template)) {
			return template;
		}
		StringBuilder builder = new StringBuilder();
		Node node = context.getNode();

		Matcher matcher = pattern.matcher(template);

		int spos = 0;

		while (matcher.find()) {
			String token = matcher.group(1);
			String value = StringUtils.EMPTY;
			if (null != node) {
				List<String> values = node.get(token);
				value = null != values && values.size() > 0 ? values.get(0)
						: value;
			}
			if (StringUtils.isBlank(value)) {
				value = context.getString(token);
			}

			builder.append(template.substring(spos, matcher.start()));
			if (StringUtils.isNotBlank(value)) {
				try {
					builder.append(URLEncoder.encode(value, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
				}
			} else {
				builder.append("${");
				builder.append(token);
				builder.append('}');
			}
			spos = matcher.end();
		}
		builder.append(template.substring(spos));

		return builder.toString();
	}

	/**
	 * 用于替换url中的占位符
	 * 
	 * @param template
	 * @param context
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String merge(String template, Map<String, String> params) {
		if (StringUtils.isBlank(template)) {
			return template;
		}
		StringBuilder builder = new StringBuilder();
		Matcher matcher = pattern.matcher(template);
		int spos = 0;

		while (matcher.find()) {
			String token = matcher.group(1);
			String value = params.get(token);

			builder.append(template.substring(spos, matcher.start()));
			if (StringUtils.isNotBlank(value)) {
				try {
					builder.append(URLEncoder.encode(value, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
				}
			} else {
				builder.append("${");
				builder.append(token);
				builder.append('}');
			}
			spos = matcher.end();
		}
		builder.append(template.substring(spos));
		return builder.toString();
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		Context context = new Context();
		context.put("phone", "12211221");
		context.put("tt", "wuhanjie");
		System.out
				.println(merge(
						"http://www.baidu.com/s?ie=utf-8&bs=&bs1=&f=3&rsv_bp=1&rsv_spt=3&wd=01059209519&rsv_sug3=3&rsv_sug4=204&rsv_sug1=2&oq=010&rsp=3&rsv_sug5=0&rsv_sug=0&rsv_sug2=0&inputT=3151",
						context));
	}
}
