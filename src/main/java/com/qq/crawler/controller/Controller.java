/**
 * 下午12:00:53
 */
package com.qq.crawler.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.scheme.Scheme;

import com.qq.crawler.exception.NonSeedException;
import com.qq.crawler.utils.MockSSLSocketFactory;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.util.IO;

/**
 * @author hanjiewu
 * 
 */
public class Controller {
	CrawlController crawlController;
	int numberOfCrawlers = 1;
	CrawlConfig crawlConfig;
	List<String> seeds;

	public Controller(String crawlStorageFolder, String proxyHost,
			int proxyPort, int numberOfCrawlers, List<String> seeds)
			throws Exception {
		crawlConfig = new CrawlConfig();
		crawlConfig.setIncludeHttpsPages(true);
		crawlConfig.setCrawlStorageFolder(crawlStorageFolder);
		crawlConfig.setProxyHost(proxyHost);
		crawlConfig.setProxyPort(proxyPort);
		this.seeds = seeds;
		this.numberOfCrawlers = numberOfCrawlers;
		initCrawlerController();
	}

	public Controller(CrawlConfig crawlConfig, int numberOfCrawlers)
			throws Exception {
		this.crawlConfig = crawlConfig;
		this.numberOfCrawlers = numberOfCrawlers;
		initCrawlerController();
	}

	void initCrawlerController() throws Exception {
		if (null == seeds) {
			seeds = new ArrayList<String>();
		}
		PageFetcher pageFetcher = new MyFetcher(crawlConfig);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
				pageFetcher);
		crawlController = new CrawlController(crawlConfig, pageFetcher,
				robotstxtServer);

	}

	public void addSeed(String url) {
		seeds.add(url);
	}

	public void setCustomData(Object data) {
		crawlController.setCustomData(data);
	}

	public void onStart() throws NonSeedException {
		if (0 == seeds.size()) {
			throw new NonSeedException();
		}
		for (String seed : seeds) {
			crawlController.addSeed(seed);
		}
	}

	public void start(Class<? extends WebCrawler> clz) throws NonSeedException {
		onStart();
		crawlController.start(clz, numberOfCrawlers);
	}

	public void startNonBlocking(Class<? extends WebCrawler> clz)
			throws NonSeedException {
		onStart();
		crawlController.startNonBlocking(clz, numberOfCrawlers);
	}

	public void shutdown() {
		crawlController.Shutdown();
		crawlController.waitUntilFinish();
	}

	/**
	 * 重新启动的时候，需要清空本地内存的信息
	 */
	public void clear() {
		crawlController.Shutdown();
		crawlController.waitUntilFinish();
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setAllowCreate(true);
		envConfig.setTransactional(crawlConfig.isResumableCrawling());
		envConfig.setLocking(crawlConfig.isResumableCrawling());
		Environment env = new Environment(new File(
				crawlConfig.getCrawlStorageFolder() + "/frontier"), envConfig);
		long docCount = env.truncateDatabase(null, "DocIDs", true);
		env.truncateDatabase(null, "PendingURLsDB", true);
		env.close();
		IO.deleteFolderContents(new File(crawlConfig.getCrawlStorageFolder()));
	}

	public boolean isFinish() {
		return crawlController.isFinished();
	}

	public void restartNonBlocking(Class<? extends WebCrawler> clz)
			throws NonSeedException {
		startNonBlocking(clz);
	}

	class MyFetcher extends PageFetcher {

		public MyFetcher(CrawlConfig config) {
			super(config);

			if (config.isIncludeHttpsPages()) {
				try {
					httpClient.getConnectionManager().getSchemeRegistry()
							.unregister("https");
					httpClient
							.getConnectionManager()
							.getSchemeRegistry()
							.register(
									new Scheme("https", 443,
											new MockSSLSocketFactory()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
