/**
 * 上午11:58:22
 */
package com.wutianyi.crawler.configs;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;

/**
 * @author hanjiewu
 * 
 */
public class CrawlerCustomData {
	private String name;

	private String urlPattern;

	private String railgunConfigs;

	private String[] seeds;

	private String[] shouldVisitPatterns;

	private String[] shouldUnVisitPatterns;

	/**
	 * 当前任务完成情况，用于多个爬取线程共享状态信息
	 */
	private CrawlConfig crawlConfig;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the urlPattern
	 */
	public String getUrlPattern() {
		return urlPattern;
	}

	/**
	 * @param urlPattern
	 *            the urlPattern to set
	 */
	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	/**
	 * @return the railgunConfigs
	 */
	public String getRailgunConfigs() {
		return railgunConfigs;
	}

	/**
	 * @param railgunConfigs
	 *            the railgunConfigs to set
	 */
	public void setRailgunConfigs(String railgunConfigs) {
		this.railgunConfigs = railgunConfigs;
	}

	/**
	 * @return the seeds
	 */
	public String[] getSeeds() {
		return seeds;
	}

	/**
	 * @param seeds
	 *            the seeds to set
	 */
	public void setSeeds(String[] seeds) {
		this.seeds = seeds;
	}

	/**
	 * @return the shouldVisitPatterns
	 */
	public String[] getShouldVisitPatterns() {
		return shouldVisitPatterns;
	}

	/**
	 * @param shouldVisitPatterns
	 *            the shouldVisitPatterns to set
	 */
	public void setShouldVisitPatterns(String[] shouldVisitPatterns) {
		this.shouldVisitPatterns = shouldVisitPatterns;
	}

	/**
	 * @return the shouldUnVisitPatterns
	 */
	public String[] getShouldUnVisitPatterns() {
		return shouldUnVisitPatterns;
	}

	/**
	 * @param shouldUnVisitPatterns
	 *            the shouldUnVisitPatterns to set
	 */
	public void setShouldUnVisitPatterns(String[] shouldUnVisitPatterns) {
		this.shouldUnVisitPatterns = shouldUnVisitPatterns;
	}

	public CrawlConfig getCrawlConfig() {
		return crawlConfig;
	}

	public void setCrawlConfig(CrawlConfig crawlConfig) {
		this.crawlConfig = crawlConfig;
	}

}
