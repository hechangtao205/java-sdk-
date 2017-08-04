
package cn.com.plustv;

import java.util.Date;

import org.apache.log4j.Logger;

import cn.com.plustv.utils.Pic;

public class OfflinePic implements Runnable {
	SmartCard sc = new SmartCard();
	private static Logger logger = Logger.getLogger(OfflinePic.class);

	@Override
	public void run() {
		synchronized ("") {

			try {
				String picName = Pic.getOfPicName();
				long time = new Date().getTime();
				logger.debug("图片下线时间" + new Date());
				// 执行图片图片下线方法
				boolean flag = sc.tjSDK04("/logo/" + picName);
				logger.debug("图片下线成功的时间是=============" + (new Date().getTime() - time));
				logger.debug("end to offlineTime " + picName);
				int countOffline = 0;
				if (flag == false) {
					// 如果失败一直执行图片下线方法 如果失败次数小于3，删除设备上的logo文件
					while (true) {
						sc.tjSDK04("/logo/" + picName);

						countOffline = countOffline + 1;
						if (countOffline > 2) {
							break;
						}
					}
				}
				// 一旦下线删除设备上的logo文件,只要走到这步，说明下线成功，调用删除图片的方法
				logger.debug("图片删除时间" + new Date());
				boolean flagDelete = sc.tjSDK02("/logo/" + picName);
				int countDelete = 0;
				if (flagDelete == false) {
					while (true) {
						sc.tjSDK02("/logo/" + picName);

						countDelete = countDelete + 1;
						if (countDelete > 3) {
							// logger.debug("台标机故障，请及时维修！");//
							// 此处需抛异常，后面细化
							try {
								throw new InteractionException("台标机故障，请及时维修！");
							} catch (RuntimeException e) {
								logger.debug(e.getMessage());
								// 强制性的推出程序
								System.exit(1);
							}
						}
					}

				}
			} catch (RuntimeException e) {
				// 记录error级别的信息
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}

		// }

	}
}
