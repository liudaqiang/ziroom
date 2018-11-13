package com.lq.crawler.ziroom;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.lq.crawler.ziroom.common.SqlPipeLine;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class RentHouse implements PageProcessor {
	// 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

	// process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
	@Override
	public void process(Page page) {
		//获得房子编号
		page.putField("NUMBER", page.getHtml().xpath("//div[@class='aboutRoom gray-6']/h3//text()").toString());
		
		if (page.getResultItems().get("NUMBER") == null) {
			page.setSkip(true);
		}
		page.addTargetRequests(page.getHtml().links().regex("http://sh\\.ziroom\\.com/z/vr/\\d+.*").all());
		//获得对应路径
		page.putField("ICON",page.getHtml().xpath("//ul[@class='lof-navigator']/li/div/img/@src").all());
		page.putField("NAME", page.getHtml().xpath("//div[@class='room_name']/h2/text()").toString());
		page.putField("LOCATION",page.getHtml().xpath("//span[@class='ellipsis']/text()").toString());
		//获得面积 朝向 户型 楼层
		page.putField("AREA", page.getHtml().xpath("//ul[@class='detail_room']/li/text()").all());
		//获得价格
		page.putField("PRICE", page.getHtml().xpath("//span[@class='room_price']/text()").toString());
		page.putField("IS_SAIL", page.getHtml().xpath("//div[@class='room_btns clearfix']/a/text()").toString());
		page.putField("LNG", page.getHtml().xpath("//div[@class='msCon clearfix']/input/@data-lng").get());
		page.putField("LAT", page.getHtml().xpath("//div[@class='msCon clearfix']/input/@data-lat").get());
		//以下是获取各个区域内的各种链接
//		List<String> areas = page.getHtml().xpath("//div[@class='clearfix zIndex6']/dd/ul").links().all();
//		page.addTargetRequests(areas);
		// 以下是获取分页内的各种链接
		List<String> urls = page.getHtml().css("div.pages").links().all();
		page.addTargetRequests(urls);
	}
	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		Spider oschinaSpider = Spider.create(new RentHouse());
		oschinaSpider.addUrl("http://sh.ziroom.com/z/nl/z1.html")
		//.addPipeline(new FilePipeline("D:\\webmagic\\")) // 保存到本地路径
		//.addPipeline(new DemoPipeLine())
		.addPipeline(new SqlPipeLine())
		// 开启5个线程抓取
		.thread(10)
		// 启动爬虫
		.run();
		
		// 注册监听，但是我这里注册不了找不到类

//		try {
//			SpiderMonitor.instance().register(oschinaSpider);
//		} catch (JMException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		oschinaSpider.start();
				 
	}
}
