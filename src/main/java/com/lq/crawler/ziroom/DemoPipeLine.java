package com.lq.crawler.ziroom;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class DemoPipeLine implements Pipeline {
	public static Integer num = 0;
	@Override
	public void process(ResultItems resultItems, Task task) {
		num++;
		System.out.println(resultItems.toString()+"当前第"+num+"个");
	}

}
