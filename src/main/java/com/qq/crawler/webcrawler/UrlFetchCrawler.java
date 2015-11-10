/**
 * 上午10:54:20
 */
package com.qq.crawler.webcrawler;

import java.util.regex.Pattern;

import com.qq.crawler.utils.CrawlerUtils;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author hanjiewu
 * 
 */
@Deprecated
public class UrlFetchCrawler extends RailgunCrawler {
	/*
	 * @see
	 * com.qq.crawler.webcrawler.RailgunCrawler#shouldVisit(edu.uci.ics.crawler4j
	 * .url.WebURL)
	 */
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		if (urlPattern.matcher(href).matches()) {
			return false;
		}
		if (null != shouldUnVisitPatterns) {
			for (Pattern pattern : shouldUnVisitPatterns) {
				if (pattern.matcher(href).matches()) {
					return false;
				}
			}
		}

		boolean shouldVisit = false;

		if (null != shouldVisitPatterns) {
			for (Pattern pattern : shouldVisitPatterns) {
				if (pattern.matcher(href).matches()) {
					shouldVisit = true;
					break;
				}
			}
		} else {
			shouldVisit = true;
		}

		if (!shouldVisit) {
			return false;
		}
		if (!CrawlerUtils.FILTERS.matcher(href).matches()) {
			for (String host : hosts) {
				if (href.startsWith(host)) {
					// System.out.println(href + ":  fetch");
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * @see
	 * edu.uci.ics.crawler4j.crawler.WebCrawler#visit(edu.uci.ics.crawler4j.
	 * crawler.Page)
	 */
	@Override
	public void visit(Page page) {
	}
}
