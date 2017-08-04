package cn.com.plustv;

import java.util.Date;

import org.apache.log4j.Logger;

import cn.com.plustv.utils.Pic;

public class OnlinePic implements Runnable {
	SmartCard sc = new SmartCard();
	private static Logger logger = Logger.getLogger(OnlinePic.class);

	@Override
	public void run() {
		synchronized ("") {
			try {
				String picName = Pic.getOnPicName();
				// 执行图片上线方法
				long now = new Date().getTime();
				logger.debug("图片上线时间" + new Date());
				boolean flag = sc.tjSDK03("/logo/" + picName, 0, 0, true);
				logger.debug("调用玩接口图片上线的时间是==============" + (new Date().getTime() - now));
				logger.debug("start to onlinetime " + picName);
				int countOnline = 0;
				if (flag == false) {
					// 如果失败,尝试执行三次
					while (true) {
						sc.tjSDK03("/logo/" + picName, 0, 0, true);

						countOnline = countOnline + 1;
						if (countOnline > 2) {
							break;
						}
					}
				}
			} catch (RuntimeException e) {
				// 记录error级别的信息
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
