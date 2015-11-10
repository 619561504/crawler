package com.qq.crawler.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

public class CrawlerUtils {

	public final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	public static String[] getHosts(String[] seeds)
			throws MalformedURLException {
		String[] hosts = new String[seeds.length];
		for (int i = 0; i < seeds.length; i++) {
			URL url = new URL(seeds[i]);
			hosts[i] = url.getProtocol() + "://" + url.getHost();
		}
		return hosts;
	}
}
