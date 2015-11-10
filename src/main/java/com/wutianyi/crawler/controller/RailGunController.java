/**
 * 下午3:04:26
 */
package com.wutianyi.crawler.controller;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.wutianyi.crawler.configs.CrawlerCustomData;
import com.wutianyi.crawler.exception.NonSeedException;
import com.wutianyi.crawler.webcrawler.RailgunCrawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;

/**
 * @author hanjiewu
 * 
 */
public class RailGunController extends Controller {

	CrawlerCustomData customData;

	/**
	 * @param crawlConfig
	 * @param numberOfCrawlers
	 * @throws Exception
	 */
	public RailGunController(CrawlConfig crawlConfig, int numberOfCrawlers,
			CrawlerCustomData customData) throws Exception {
		super(crawlConfig, numberOfCrawlers);
		this.customData = customData;
		setCustomData(customData);
	}

	public RailGunController(String crawlStorageFolder, String proxyHost,
			int proxyPort, int numberOfCrawlers, CrawlerCustomData customData)
			throws Exception {
		super(crawlStorageFolder, proxyHost, proxyPort, numberOfCrawlers, null);
		this.customData = customData;
		setCustomData(customData);
	}

	/*
	 * @see com.qq.crawler.Controller#onStart()
	 */
	@Override
	public void onStart() throws NonSeedException {
		if (0 == seeds.size()
				&& (null == customData.getSeeds() || customData.getSeeds().length == 0)) {
			throw new NonSeedException();
		}
		if (null != customData.getSeeds()) {
			for (String seed : customData.getSeeds()) {
				seeds.add(seed);
			}
		}
		customData.setSeeds(seeds.toArray(new String[seeds.size()]));
		for (String seed : seeds) {
			crawlController.addSeed(seed);
		}
	}

	public void start() throws NonSeedException {
		super.start(RailgunCrawler.class);
	}

	public void startNonBlocking() throws NonSeedException {
		super.startNonBlocking(RailgunCrawler.class);
	}

	public void restartNonBlocking() throws NonSeedException {
		super.startNonBlocking(RailgunCrawler.class);
	}

	public static void main(String[] args) throws Exception {
		CrawlConfig crawlConfig = new CrawlConfig();
		crawlConfig.setCrawlStorageFolder("data/crawl");
		crawlConfig.setProxyHost("web-proxy.oa.com");
		crawlConfig.setProxyPort(8080);
		crawlConfig.setMaxDownloadSize(Integer.MAX_VALUE);

		CrawlerCustomData customData = new CrawlerCustomData();
		customData.setName("mytophome");
		customData.setUrlPattern(".*(shopiagent/search.do).*");
		customData
				.setShouldVisitPatterns(new String[] { ".*(shopiagent/search.do).*" });
		customData.setRailgunConfigs(FileUtils.readFileToString(new File(
				RailGunController.class.getResource("/crawler/mytophome.json")
						.getFile())));
		;

		RailGunController controller = new RailGunController(crawlConfig, 4,
				customData);
		controller
				.addSeed("http://gz.mytophome.com/shopiagent/search.do?cityId=20&areaId=&subAreaId=&satis=&visit=&keyword=&p=0");
		controller
				.addSeed("http://dg.mytophome.com/shopiagent/search.do?cityId=769&areaId=&subAreaId=&satis=&visit=&keyword=&p=0");
		controller
				.addSeed("http://fs.mytophome.com/shopiagent/search.do?cityId=757&areaId=&subAreaId=&satis=&visit=&keyword=&p=0");
		controller
				.addSeed("http://zh.mytophome.com/shopiagent/search.do?cityId=756&areaId=&subAreaId=&satis=&visit=&keyword=&p=0");
		controller
				.addSeed("http://zs.mytophome.com/shopiagent/search.do?cityId=760&areaId=&subAreaId=&satis=&visit=&keyword=&p=0");
		controller
				.addSeed("http://nj.mytophome.com/shopiagent/search.do?cityId=25&areaId=&subAreaId=&satis=&visit=&keyword=&p=0");
		controller
				.addSeed("http://cd.mytophome.com/shopiagent/search.do?cityId=28&areaId=&subAreaId=&satis=&visit=&keyword=&p=0");
		controller.start();

	}
}
