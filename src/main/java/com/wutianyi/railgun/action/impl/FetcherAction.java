/**
 * @(#)FetcherAction.java
 * 2013-11-20-上午11:39:25
 */
package com.wutianyi.railgun.action.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.wutianyi.railgun.TemplateManager;
import com.wutianyi.railgun.action.Action;
import com.wutianyi.railgun.action.bean.CheckResult;
import com.wutianyi.railgun.context.Context;
import com.wutianyi.railgun.flow.configs.ActionConfigs;

/**
 * 抓取动作
 * 
 * @author hanjiewu
 * 
 */
public class FetcherAction implements Action {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qq.railgun.action.Action#process(com.qq.railgun.flow.configs.
	 * ActionConfigs)
	 */
	@Override
	public void process(ActionConfigs config, Context context) {
		HttpInfo httpInfo = new HttpInfo();
		getHttpInfo(config, httpInfo);

		HttpClient httpClient = context.getHttpClient();

		String content = fetcher(httpClient, config, httpInfo, context);
		context.putLastProcessResult(content);
	}

	/**
	 * @param httpclient
	 * @param config
	 * @param httpInfo
	 * @param context
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private String fetcher(HttpClient httpClient, ActionConfigs config,
			HttpInfo httpInfo, Context context) {
		String url = TemplateManager.merge(config.getUrl(), context);
		HttpGet httpGet = new HttpGet(url);
		addHeader(httpGet, httpInfo);
		String content = StringUtils.EMPTY;
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
			Header encoding = response.getEntity().getContentEncoding();
			String charset = null != encoding ? encoding.getValue() : "utf-8";
			content = EntityUtils.toString(response.getEntity(), charset);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpGet.releaseConnection();
		}
		return content;
	}

	private void addHeader(HttpGet httpGet, HttpInfo httpInfo) {
		if (httpInfo.headers.size() > 0) {
			for (Entry<String, String> header : httpInfo.headers.entrySet()) {
				httpGet.addHeader(header.getKey(), header.getValue());
			}
		}
	}

	private void getHttpInfo(ActionConfigs config, HttpInfo httpInfo) {
		if (null != config) {
			if (null != config.getParentActionConfigs()) {
				getHttpInfo(config.getParentActionConfigs(), httpInfo);
			} else {
				if (StringUtils.isNotBlank(config.getProxy())) {
					httpInfo.proxy = config.getProxy();
					httpInfo.proxyPort = config.getProxyPort();
				}
				if (null != config.getHeaders()) {
					httpInfo.headers.putAll(config.getHeaders());
				}
			}
		}
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
		if (StringUtils.isNotBlank(config.getUrl())) {
			try {
				new URL(config.getUrl());
			} catch (MalformedURLException e) {
				return new CheckResult(false, "无效url");
			}
		}
		return CheckResult.SUCCESS;
	}

	private class HttpInfo {
		String proxy;

		int proxyPort;

		Map<String, String> headers = new HashMap<String, String>();

		@Override
		public String toString() {
			return "HttpInfo [proxy=" + proxy + ", proxyPort=" + proxyPort
					+ ", headers=" + headers + "]";
		}

	}
}
