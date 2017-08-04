package cn.com.plustv;

import java.util.Date;

import org.apache.log4j.Logger;

import cn.com.plustv.utils.Pic;

public class UploadPic implements Runnable {
	SmartCard sc = new SmartCard();
	private static Logger logger = Logger.getLogger(UploadPic.class);

	@Override
	public void run() {
		synchronized ("") {
			try {

				String picName = Pic.getUpPicName();
				long time = new Date().getTime();
				logger.debug("时间" + new Date());
				// 执行上传图片方法
				boolean flag = sc.tjSDK("D:/bizcode_20170123172330/" + picName);
				logger.debug("调用完接口图片的时间是=============" + (new Date().getTime() - time));
				logger.debug("start to upload " + picName);
				// 如果失败继续执行上传图片的方法
				int count = 0;
				if (flag == false) {
					while (true) {
						sc.tjSDK("D:/bizcode_20170123172330/" + picName);
						// 如果失败次数等于3，本次循环结束
						if (count == 2) {
							break;
						}

					}

				}

			} catch (RuntimeException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
