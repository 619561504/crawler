package com.qq.railgun.hc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

public class HttpClientUtils {

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
	private static final int CONNECTION_TIMEOUT = 10000;
	private static final int SO_TIMEOUT = 10000;

	/**
	 * @param connectionTimeout
	 * @param soTimeout
	 *            socket 的超时
	 * @param userAgent
	 * @param proxy
	 * @param proxyPort
	 * @return
	 */
	public static HttpClient getHttpClient(int connectionTimeout,
			int soTimeout, String proxy, int proxyPort, String userAgent) {

		DefaultHttpClient httpClient = new DefaultHttpClient();

		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeout);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				soTimeout);
		httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
				userAgent);
		if (StringUtils.isNotBlank(proxy)) {
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					new HttpHost(proxy, proxyPort));
		}
		try {
			httpClient.getConnectionManager().getSchemeRegistry()
					.unregister("https");
			// httpClient
			// .getConnectionManager()
			// .getSchemeRegistry()
			// .register(
			// new Scheme("https", 443, new MockSSLSocketFactory()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return httpClient;
	}

	// /**
	// * @return 获取默认配置的httpclient
	// */
	// public static HttpClient getHttpClient() {
	//
	// return getHttpClient(CONNECTION_TIMEOUT, SO_TIMEOUT,
	// CrawlerUtils.PROXYHOST, CrawlerUtils.PROXYPORT, UserAgents.IE9);
	// }

	public static HttpClient getHttpClient(String proxyHost, int proxyPort) {
		return getHttpClient(CONNECTION_TIMEOUT, SO_TIMEOUT, proxyHost,
				proxyPort, UserAgents.IE9);
	}

	/**
	 * 使用HttpClient 的get方法请求获取url内容数据
	 * 
	 * @param client
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String get(HttpClient client, String url) throws Exception {
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			Header encoding = response.getEntity().getContentEncoding();
			String charset = null != encoding ? encoding.getValue() : "utf-8";
			String content = EntityUtils
					.toString(response.getEntity(), charset);
			return content;
		} finally {
			get.releaseConnection();
		}
	}

	/**
	 * 使用HttpClient 的get方法请求获取url内容数据,返回二进制字节流
	 * 
	 * @param client
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static byte[] getBytes(HttpClient client, String url)
			throws Exception {
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			InputStream inputStream = response.getEntity().getContent();
			ByteArrayOutputStream out = new ByteArrayOutputStream(
					(int) response.getEntity().getContentLength());
			byte[] read = new byte[1024];
			int readBytes = 0;
			while ((readBytes = inputStream.read(read)) != -1) {
				out.write(read, 0, readBytes);
			}
			byte[] content = out.toByteArray();
			inputStream.close();
			out.close();
			return content;
		} finally {
			get.releaseConnection();
		}
	}

	public static void main(String[] args) throws ClientProtocolException,
			IOException {
		final HttpClient httpClient = getHttpClient("web-proxy.oa.com", 8080);
		HttpGet get = new HttpGet("http://www.sogou.com/web?query=04765872777");
		HttpResponse response = httpClient.execute(get);

		Elements els = Jsoup.parse(EntityUtils.toString(response.getEntity()))
				.select("#phonenumberdetail");

		for (Element el : els) {
			System.out.println(el.html());
		}

		//
		// Thread[] threads = new Thread[2];
		// for (int i = 0; i < 2; i++)
		// {
		// threads[i] = new Thread(new Runnable()
		// {
		//
		// @Override
		// public void run()
		// {
		// HttpGet get = new HttpGet("http://www.soso.com");
		// try
		// {
		// HttpResponse response = httpClient.execute(get);
		// System.out.println(EntityUtils.toString(response.getEntity()));
		// Thread.sleep(5000);
		// get.releaseConnection();
		// }
		// catch (ClientProtocolException e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// catch (IOException e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// catch (InterruptedException e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// });
		// threads[i].start();
		// }
	}
}
