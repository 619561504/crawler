package com.qq.crawler.webcrawler;

import org.apache.http.client.HttpClient;

import com.qq.crawler.configs.CrawlerCustomData;
import com.qq.railgun.RailGun;
import com.qq.railgun.hc.HttpClientUtils;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;

/**
 * 根据传入的url和请求参数,抓取页面内容
 * 
 * @author bobpeng
 * 
 */
public class PageFetcher {

	private boolean isFinish = false;
	private CrawlerCustomData customData;
	private HttpClient httpClient;
	private CrawlConfig crawlConfig;

	public PageFetcher(CrawlerCustomData customData) {
		this.customData = customData;
		init();
	}

	public void init() {
		String confName = customData.getName();
		crawlConfig = customData.getCrawlConfig();
		httpClient = HttpClientUtils.getHttpClient(
				crawlConfig.getConnectionTimeout(),
				crawlConfig.getSocketTimeout(), crawlConfig.getProxyHost(),
				crawlConfig.getProxyPort(), crawlConfig.getUserAgentString());
	}

	public void start() {
		isFinish = false;
		(new Thread(new WorkerThread())).start();
	}

	public boolean isFinish() {
		return isFinish;
	}

	public void shutdown() {
		isFinish = true;
	}

	class WorkerThread implements Runnable {

		@Override
		public void run() {
			for (String seed : customData.getSeeds()) {
				if (isFinish) {
					break;
				}
				try {
					String content = HttpClientUtils.get(httpClient, seed);
					RailGun railGun = new RailGun(
							customData.getRailgunConfigs(), true);
					railGun.fire(content);
					String result = seed + "\t" + railGun.getResultMapString();
					Thread.sleep(crawlConfig.getPolitenessDelay());
				} catch (Exception e) {

				}
			}
			isFinish = true;
		}
	}
}
