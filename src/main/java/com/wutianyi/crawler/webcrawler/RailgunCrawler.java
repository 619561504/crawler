/**
 * 上午10:43:57
 */
package com.wutianyi.crawler.webcrawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.codehaus.jackson.map.ObjectMapper;

import com.wutianyi.crawler.configs.CrawlerCustomData;
import com.wutianyi.crawler.utils.CrawlerUtils;
import com.wutianyi.railgun.RailGun;
import com.wutianyi.railgun.bean.Node;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author hanjiewu
 * 
 */
public class RailgunCrawler extends WebCrawler {

	RailGun railGun;

	String[] hosts;

	Pattern urlPattern;

	Pattern[] shouldVisitPatterns;

	Pattern[] shouldUnVisitPatterns;

	private String name;

	/*
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#onStart()
	 */
	@Override
	public void onStart() {
		Object customData = myController.getCustomData();
		if (null == customData || !(customData instanceof CrawlerCustomData)) {
			throw new RuntimeException(
					"RailGunComData must not null or not instance of RailGunCustomData");
		}
		CrawlerCustomData railGunCustomData = (CrawlerCustomData) customData;
		try {
			name = railGunCustomData.getName();
			railGun = new RailGun(railGunCustomData.getRailgunConfigs(), true);
			hosts = CrawlerUtils.getHosts(railGunCustomData.getSeeds());
			urlPattern = Pattern.compile(railGunCustomData.getUrlPattern());

			String[] ps = railGunCustomData.getShouldVisitPatterns();
			if (null != ps && ps.length > 0) {
				shouldVisitPatterns = new Pattern[ps.length];
				for (int i = 0; i < shouldVisitPatterns.length; i++) {
					shouldVisitPatterns[i] = Pattern.compile(ps[i]);
				}
			}
			ps = railGunCustomData.getShouldUnVisitPatterns();
			if (null != ps && ps.length > 0) {
				shouldUnVisitPatterns = new Pattern[ps.length];
				for (int i = 0; i < shouldUnVisitPatterns.length; i++) {
					shouldUnVisitPatterns[i] = Pattern.compile(ps[i]);
				}
			}
		} catch (Exception e) {
		}
	}

	/*
	 * @see
	 * edu.uci.ics.crawler4j.crawler.WebCrawler#shouldVisit(edu.uci.ics.crawler4j
	 * .url.WebURL)
	 */
	@Override
	public boolean shouldVisit(WebURL url) {
		boolean shouldVisit = doShouldVisit(url);
		String href = url.getURL().toLowerCase();
		String shouldVisitInfo = "shouldVisit:url=" + href + ",parent="
				+ url.getParentUrl() + ",shouldVisit=" + shouldVisit
				+ ",depth=" + url.getDepth();
		return shouldVisit;
	}

	public boolean doShouldVisit(WebURL url) {

		String href = url.getURL().toLowerCase();
		if (null != shouldUnVisitPatterns) {
			for (Pattern pattern : shouldUnVisitPatterns) {
				if (pattern.matcher(href).matches()) {
					return false;
				}
			}
		}

		boolean shouldVisit = false;

		if (!urlPattern.matcher(href).matches() && null != shouldVisitPatterns) {
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
		String url = page.getWebURL().getURL();
		String visitInfo = "visit:" + url + ",matches="
				+ urlPattern.matcher(url).matches();
		if (urlPattern.matcher(url).matches()
				&& page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			railGun.fire(htmlParseData.getHtml());
			try {
				if (railGun.getShell().size() == 0) {
					return;
				}
				logger.info(url + "\t" + railGun.getResultMapString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) throws MalformedURLException {
		URL url = new URL(
				"http://www.fpsjk.com/searchList_Detail.aspx?date=2014-03-18");
		System.out.print(url.getProtocol());
		System.out.print(url.getHost());
		WebURL webURL = new WebURL();
		webURL.setURL("http://www.fpsjk.com/searchList_Detail.aspx?date=2014-03-18");
		System.out.println(webURL.getDomain());
	}
}
