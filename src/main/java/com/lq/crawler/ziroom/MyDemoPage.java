package com.lq.crawler.ziroom;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class MyDemoPage implements PageProcessor {
	// 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

	@Override
	// process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
	public void process(Page page) {

		// page.addTargetRequests(page.getHtml().links().regex("(https://sh\\.ziroom\\.com/[\\w\\-]+/[\\w\\-]+)").all());
		// 部分二：定义如何抽取页面信息，并保存下来
		// page.putField("author",
		// page.getUrl().regex("https://github\\.com/code4craft/(\\w+)/.*").toString());
		// page.putField("author",
		// page.getUrl().regex("http://sh.ziroom.com/z/vr/(\\w+)/.*").toString());
		page.putField("name", page.getHtml().xpath("//div[@class='aboutRoom gray-6']/h3//text()").toString());

		// page.putField("name",
		// page.getHtml().xpath("//h1[@class='public']/strong/a/text()").toString());
		if (page.getResultItems().get("name") == null) {
			// skip this page
			page.setSkip(true);
		}
		// page.putField("readme",
		// page.getHtml().xpath("//div[@id='readme']/tidyText()"));

		// 部分三：从页面发现后续的url地址来抓取
		// page.addTargetRequests(page.getHtml().links().regex("(http://sh.ziroom.com/z/vr/[\\w\\.\\-]/.*").all());
		page.addTargetRequests(page.getHtml().links().regex("http://sh\\.ziroom\\.com/z/vr/\\d+.*").all());
		// page.addTargetRequests(page.getHtml().links().regex("https://www\\.zhihu\\.com/question/\\d+/answer/\\d+.*").all());
		// 以下是获取分页内的各种链接
		// List<String> urls =
		// page.getHtml().css("div.pagination").links().regex(".*/search?l=java.*").all();
		 List<String> urls =
		 page.getHtml().css("div#page.pages").links().all();
		 page.addTargetRequests(urls);
	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		// 流程
		// create里边的addUrl是让框架找对应的路径
		// 找到路径后addTargetRequests是获取爬行条件
		// 爬行条件成功后，会进入该页面
		// putField 获得对应页面的一些规则
		Spider oschinaSpider = Spider.create(new MyDemoPage());
		// 从"https://github.com/code4craft"开始抓
		oschinaSpider.addUrl("http://sh.ziroom.com/z/nl/z2.html")
			//.addPipeline(new FilePipeline("D:\\webmagic\\")) // 保存到本地路径
				.addPipeline(new DemoPipeLine())
				// 开启5个线程抓取
				.thread(5)
				// 启动爬虫
				.run();
		// 注册监听，但是我这里注册不了找不到类
		/*
		 * SpiderMonitor.instance().register(oschinaSpider);
		 * oschinaSpider.start();
		 */
	}
}
