/**
 * @(#)RGContext.java
 * 2013-11-20-上午11:41:13
 */
package com.qq.railgun.context;

import java.io.Writer;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;

//import com.qq.crawler.utils.InfoAccumulator;
import com.qq.railgun.bean.Node;

/**
 * @author hanjiewu
 * 
 */
public class Context {
	private final String LAST_PROCESS_RESULT_KEY = "#@%$last_process_result_key";

	/**
	 * 用于全局
	 */
	private Map<String, Object> context = new HashMap<String, Object>();

	private Map<String, Pattern> regexContext = new HashMap<String, Pattern>();

	private Map<String, Method> methodContext = new HashMap<String, Method>();
	// //收集页面抓取运行信息
	// private InfoAccumulator infoAccumulator;

	/**
	 * 需要填充值
	 */
	private Node node = null;

	private HttpClient httpClient;

	/**
	 * 设置上一步操作的结果
	 * 
	 * @param result
	 */
	public void putLastProcessResult(Object result) {
		context.put(LAST_PROCESS_RESULT_KEY, result);
	}

	/**
	 * 获取上一步的结果
	 * 
	 * @return
	 */
	public Object getLastProcessResult() {
		return context.get(LAST_PROCESS_RESULT_KEY);
	}

	// public Node getLastProcessResultNode()
	// {
	// Object obj = getLastProcessResult();
	// if (null != obj && obj instanceof Node)
	// {
	// return (Node) obj;
	// }
	// return null;
	// }

	public Node getNode() {
		return node;
	}

	public Object get(String key) {
		return context.get(key);
	}

	public String getString(String key) {
		Object obj = get(key);
		if (null != obj && obj instanceof String) {
			return (String) obj;
		}
		return StringUtils.EMPTY;
	}

	public void put(String key, Object value) {
		context.put(key, value);
	}

	public void putRegex(String regex, Pattern pattern) {
		regexContext.put(regex, pattern);
	}

	public Pattern getRegex(String regex) {
		return regexContext.get(regex);
	}

	public void putMethod(String name, Method method) {
		methodContext.put(name, method);
	}

	public Method getMethod(String name) {
		return methodContext.get(name);
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public void reset() {
		node = null;
		context.clear();
	}

}
