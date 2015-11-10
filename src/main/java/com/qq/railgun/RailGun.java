/**
 * @(#)RailGun.java
 * 2013-11-20-上午10:58:10
 */
package com.qq.railgun;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

import com.qq.railgun.action.Action;
import com.qq.railgun.action.bean.CheckResult;
import com.qq.railgun.action.enums.Actions;
import com.qq.railgun.bean.Node;
import com.qq.railgun.context.Context;
import com.qq.railgun.flow.configs.ActionConfigs;
import com.qq.railgun.flow.configs.JsonConfigParser;
import com.qq.railgun.hc.HttpClientUtils;
import com.qq.railgun.utils.RailGunUtils;

/**
 * @author hanjiewu
 * 
 */
public class RailGun {
	static {
		// 清楚slf4j的logger
		Logger logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		if (logger instanceof ch.qos.logback.classic.Logger) {
			ch.qos.logback.classic.Logger l = (ch.qos.logback.classic.Logger) logger;
			l.setLevel(Level.OFF);
		} else if (logger instanceof org.apache.log4j.Logger) {
			org.apache.log4j.Logger l = (org.apache.log4j.Logger) logger;
			l.setLevel(org.apache.log4j.Level.OFF);
		}
	}
	/**
	 * 配置流
	 */
	private ActionConfigs actionConfigs;

	/**
	 * 上下文环境
	 */
	private Context context;

	/**
	 * 结果数据集
	 */
	private List<Node> nodes;

	private String proxyHost;
	private int proxyPort;

	public RailGun(String railgunConfigs, boolean string)
			throws JsonParseException, JsonMappingException, IOException {
		if (StringUtils.isNotBlank(railgunConfigs)) {
			actionConfigs = JsonConfigParser.parserContents(railgunConfigs);
		}
		context = new Context();
		if (StringUtils.isNotEmpty(actionConfigs.getProxy())) {
			this.proxyHost = actionConfigs.getProxy();
			this.proxyPort = actionConfigs.getProxyPort();
		} else {
			// this.proxyHost = CrawlerUtils.PROXYHOST;
			// this.proxyPort = CrawlerUtils.PROXYPORT;
		}
		HttpClient httpClient = HttpClientUtils.getHttpClient(this.proxyHost,
				this.proxyPort);
		context.setHttpClient(httpClient);
		check();
		nodes = new ArrayList<Node>();
	}

	public RailGun(String path) throws JsonParseException,
			JsonMappingException, IOException {
		actionConfigs = JsonConfigParser.parser(path);
		context = new Context();
		HttpClient httpClient = HttpClientUtils.getHttpClient(
				actionConfigs.getProxy(), actionConfigs.getProxyPort());
		context.setHttpClient(httpClient);
		check();
		nodes = new ArrayList<Node>();
	}

	/**
	 * 将已经拉取的结果放入到上下文环境中
	 * 
	 * @param path
	 * @param contents
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public RailGun(String path, String contents) throws JsonParseException,
			JsonMappingException, IOException {
		this(path);
		context.putLastProcessResult(contents);
	}

	/**
	 * 检查配置是否有错误
	 * 
	 * @return
	 */
	public void check() {
		check(actionConfigs);
	}

	/**
	 * 检查配置是否正确
	 * 
	 * @param actionConfigs
	 * @return
	 * @throws CheckResultException
	 */
	public void check(ActionConfigs actionConfigs) {
		if (null != actionConfigs) {
			Action action = Actions.getAction(actionConfigs.getAction());
			if (null == action) {
				// throw new CheckResultException("无效的action！");
			}
			CheckResult cr = action.check(actionConfigs, context);
			if (cr != CheckResult.SUCCESS) {
				// throw new CheckResultException(cr.getInfo());
			}

			if (null != actionConfigs.getSubActionConfigs()
					&& actionConfigs.getSubActionConfigs().length > 0) {
				for (ActionConfigs subActionConfigs : actionConfigs
						.getSubActionConfigs()) {
					check(subActionConfigs);
				}
			}
		}
	}

	public void fire(String content) {
		reset();
		context.putLastProcessResult(content);
		fire(actionConfigs);
	}

	/**
	 * 触发
	 */
	public void fire() {
		fire(actionConfigs);
	}

	public void fire(ActionConfigs actionConfigs) {
		if (null != actionConfigs) {
			Action action = Actions.getAction(actionConfigs.getAction());
			action.process(actionConfigs, context);
			Object lastProcessResult = context.getLastProcessResult();

			if (null != actionConfigs.getSubActionConfigs()
					&& actionConfigs.getSubActionConfigs().length > 0) {
				if (StringUtils.equals(Actions.SHELL.getName(),
						actionConfigs.getAction())) {
					fireShellAction(actionConfigs.getSubActionConfigs(),
							lastProcessResult, context);
				} else {
					for (ActionConfigs subActionConfigs : actionConfigs
							.getSubActionConfigs()) {
						subActionConfigs.setParentActionConfigs(actionConfigs);
						context.putLastProcessResult(lastProcessResult);
						fire(subActionConfigs);
					}
				}

			}

		}
	}

	/**
	 * shell主要是用于数据的提取
	 * 
	 * @param subActionConfigs
	 * @param lastProcessResult
	 */
	@SuppressWarnings("rawtypes")
	private void fireShellAction(ActionConfigs[] subActionConfigs,
			Object lastProcessResult, Context context) {
		if (lastProcessResult instanceof String) {
			Node node = new Node();
			context.setNode(node);
			for (ActionConfigs subActionConfig : subActionConfigs) {
				context.putLastProcessResult(lastProcessResult);
				fire(subActionConfig);
			}
			nodes.add(node);
		} else if (lastProcessResult instanceof List) {
			List lists = (List) lastProcessResult;
			for (int i = 0; i < lists.size(); i++) {
				Node node = new Node();
				context.setNode(node);
				for (ActionConfigs subActionConfig : subActionConfigs) {
					context.putLastProcessResult(lists.get(i));
					fire(subActionConfig);
				}
				nodes.add(node);
			}
		} else {
			for (ActionConfigs subActionConfig : subActionConfigs) {
				context.putLastProcessResult(lastProcessResult);
				fire(subActionConfig);
			}
		}
	}

	public List<Node> getShell() {
		return nodes;
	}

	private static ObjectMapper objectMappler = new ObjectMapper();

	public String getResultMapString() throws JsonGenerationException,
			JsonMappingException, IOException {
		List<Map<String, List<String>>> maps = new ArrayList<Map<String, List<String>>>();
		for (Node node : this.getShell()) {
			maps.add(node.getDatas());
		}
		return objectMappler.writeValueAsString(maps);
	}

	public <T> List<T> getResults(Class<T> clz) throws InstantiationException,
			IllegalAccessException, InvocationTargetException {
		List<T> results = new ArrayList<T>();
		for (Node node : nodes) {
			results.add(RailGunUtils.shellToObject(clz, node));
		}
		return results;
	}

	public void setData(String key, String value) {
		context.put(key, value);
	}

	public void reset() {
		nodes = new ArrayList<Node>();
		context.reset();
	}

	public static void main(String[] args) throws JsonParseException,
			JsonMappingException, IOException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		ObjectMapper objectMapper = new ObjectMapper();
		RailGun railGun = new RailGun(RailGun.class.getResource(
				"/resources/crawler/baidu.json").getFile());
		String[] phones = new String[] { "04765872777", "02161136666" };
		for (String phone : phones) {
			railGun.reset();
			railGun.setData("phone", phone);
			railGun.fire();
			List<Node> nodes = railGun.getShell();
			for (Node node : nodes) {
				Map<String, List<String>> datas = node.getDatas();
				for (Entry<String, List<String>> data : datas.entrySet()) {
					System.out.print(data.getKey());
					for (String value : data.getValue()) {
						System.out.print("\t" + value);
					}
					System.out.println();
				}
			}
			// List<TagedPhoneCompetitorTbDO> results =
			// railGun.getResults(TagedPhoneCompetitorTbDO.class);
			// for (TagedPhoneCompetitorTbDO r : results)
			// {
			// System.out.println(objectMapper.writeValueAsString(r));
			// }
		}

	}
}
