package cn.com.plustv;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.com.plustv.utils.Pic;
import cn.com.plustv.utils.PropertiesUtils;

public class InteractionCode {
	private static SmartCard sc = new SmartCard();
	private static Logger logger = Logger.getLogger(InteractionCode.class);
	private static int uploadTime;
	private static int onlineTime;
	private static int offlineTime;
	private static int TimerTime;
	private static int timeinterval;
	private static String xmlurl;
	static {
		String imageupload1 = "imageupload";
		String onlineimage1 = "onlineimage";
		String offlineimage1 = "offlineimage";
		String timer = "timer";
		String timeintervalStr = "timeinterval";
		String url ="url";//xml文件的地址
		String uploadValue = PropertiesUtils.readProperty(imageupload1);
		String onlineValue = PropertiesUtils.readProperty(onlineimage1);
		String offlineValue = PropertiesUtils.readProperty(offlineimage1);
		String timertimer = PropertiesUtils.readProperty(timer);
		String timeintervalValue = PropertiesUtils.readProperty(timeintervalStr);
		xmlurl = PropertiesUtils.readProperty(url);
		uploadTime = Integer.parseInt(uploadValue);
		onlineTime = Integer.parseInt(onlineValue);
		offlineTime = Integer.parseInt(offlineValue);
		TimerTime = Integer.parseInt(timertimer);
		timeinterval = Integer.parseInt(timeintervalValue);
	}
	public static List<Element> elementList = new ArrayList<>();

	public static List<Element> getElementList() {
		return elementList;
	}

	public static void setElementList(List<Element> elementList) {
		InteractionCode.elementList = elementList;
	}

	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure(System.getProperty("user.dir") + "/conf/log4j.properties");
		final long startTime = System.currentTimeMillis();// 这是程序启动的时间
		logger.debug("程序启动的时间是=============---------=========" + startTime + "----" + new Date());
		// 创建SAXReader对象
		SAXReader reader = new SAXReader();
		// 读取文件，取得Document
		Document document = reader.read(new FileInputStream(xmlurl));
		// 取得根标签对象，得到books的标签
		Element root = document.getRootElement();
		// 根据实际需要取得相应的值
		List<Element> elements = root.elements();
		// List<Element> elementList = new ArrayList<>();
		for (Element element : elements) {
			elementList.add(element);
		}
		final SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		for (Element element : elementList) {
			String onlinetime = element.elementText("onlinetime");
			String offlinetime = element.elementText("offlinetime");
			String[] splitoNlineTime = onlinetime.split(":");
			String[] splitoFflineTime = offlinetime.split(":");
			String sonM = splitoNlineTime[0];
			String sonS = splitoNlineTime[1];
			String sofM = splitoFflineTime[0];
			String sofS = splitoFflineTime[1];
			int minuteOn = Integer.parseInt(sonM);
			int secondOn = Integer.parseInt(sonS);
			int minuteOf = Integer.parseInt(sofM);
			int secondOf = Integer.parseInt(sofS);
			int onlinetimeMinute = (minuteOn * 60 + secondOn) * 1000;
			int offlinetimeMinute = (minuteOf * 60 + secondOf) * 1000;
			String oTime = String.valueOf(onlinetimeMinute);
			String fTime = String.valueOf(offlinetimeMinute);
			element.remove(element.element("onlinetime"));
			element.remove(element.element("offlinetime"));
			Element newElementOn = element.addElement("onlinetime");
			Element newElementOf = element.addElement("offlinetime");
			newElementOn.setText(oTime);
			newElementOf.setText(fTime);
		}
		String picName0 = elements.get(0).elementText("pic");
		String picName1 = elements.get(1).elementText("pic");
		String picName2 = elements.get(2).elementText("pic");
		String picName3 = elements.get(3).elementText("pic");
		List<String> list = new ArrayList<String>();
		list.add(picName0);
		list.add(picName1);
		list.add(picName2);
		list.add(picName3);
		boolean flag = false;
		for (String picName : list) {
			flag = sc.tjSDK("D:/bizcode_20170123172330/" + picName);
		}
		if (flag == true) {
			logger.debug("第一轮第一张和第二张和第三张图片上传成功！");
		}
		String length = elements.get(elements.size() - 1).elementText("videotime");
		String[] split = length.split(":");
		String videoLengthMinute = split[0];
		String videoLengthSecond = split[0];
		int lengthM = Integer.parseInt(videoLengthMinute);
		int lengthS = Integer.parseInt(videoLengthSecond);
		final int lengthTime = (lengthM * 60 + lengthS) * 1000;
		long endTime = System.currentTimeMillis();
		long k = endTime - startTime;
		logger.debug("前两张上传所花的时间" + k);
		Runnable upRunnable = new Runnable() {
			public void run() {
				try {
					Iterator<Element> it = elementList.iterator();

					while (it.hasNext()) {
						Element element = it.next();
						// 解析xml文件获取图片名称

						String onlinetime = element.elementText("onlinetime");
						int lOnlinetime = Integer.parseInt(onlinetime);

						long now = System.currentTimeMillis();
						long onTime = startTime + lOnlinetime;
						if (onTime - now >= uploadTime-1000 && onTime - now < uploadTime) {
							String elementText = element.elementText("pic");
							Pic.setUpPicName(elementText);
							UploadPic uploadPic = new UploadPic();
							Thread thread = new Thread(uploadPic);
							thread.start();

						}
					}

				} catch (RuntimeException e) {
					// 记录error级别的信息
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		};
		ScheduledExecutorService upService = Executors.newSingleThreadScheduledExecutor();
		// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
		upService.scheduleAtFixedRate(upRunnable, 0, TimerTime, TimeUnit.MILLISECONDS);

		Runnable onRunnable = new Runnable() {
			public void run() {
				try {
					Iterator<Element> it = elementList.iterator();

					while (it.hasNext()) {
						Element element = it.next();
						// 解析xml文件获取图片名称onPicName
						String onlinetime = element.elementText("onlinetime");
						int lOnlinetime = Integer.parseInt(onlinetime);

						long now = System.currentTimeMillis();
						long onTime = startTime + lOnlinetime;
						if ((onTime - now >= onlineTime-1000 && onTime - now < onlineTime) || (onTime < now)) {
							String elementText = element.elementText("pic");
							Pic.setOnPicName(elementText);
							String onPicName = Pic.getOnPicName();
							logger.debug("当前将要上线的图片onlinetime是" + onPicName);
							OnlinePic onlinePic = new OnlinePic();
							Thread thread = new Thread(onlinePic);
							thread.start();

							logger.debug("before add time :" + element.elementText("onlinetime"));

							String onlinetimeText = element.elementText("onlinetime");
							int time1 = Integer.parseInt(onlinetimeText);
							int times = time1 + lengthTime + timeinterval;
							String str = String.valueOf(times);
							element.remove(element.element("onlinetime"));
							Element newElement = element.addElement("onlinetime");
							newElement.setText(str);
							logger.debug("after add time :" + element.elementText("onlinetime"));
						}
					}

				} catch (RuntimeException e) {
					// 记录error级别的信息
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		};
		ScheduledExecutorService onService = Executors.newSingleThreadScheduledExecutor();
		// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
		onService.scheduleAtFixedRate(onRunnable, 0, TimerTime, TimeUnit.MILLISECONDS);

		Runnable ofRunnable = new Runnable() {
			public void run() {
				try {
					for (Element element : elementList) {
						String offlinetime = element.elementText("offlinetime");
						int lofflinetime = Integer.parseInt(offlinetime);
						long now = new Date().getTime();
						long ofTime = startTime + lofflinetime;
						if (ofTime - now >= offlineTime-1000 && ofTime - now < offlineTime) {
							String elementText = element.elementText("pic");
							Pic.setOfPicName(elementText);
							OfflinePic offlinePic = new OfflinePic();
							Thread thread = new Thread(offlinePic);
							thread.start();

							logger.debug("下线图片before add time :" + element.elementText("offlinetime"));
							String offlinetimeText = element.elementText("offlinetime");
							int time1 = Integer.parseInt(offlinetimeText);
							int times = time1 + lengthTime + timeinterval;
							String str = String.valueOf(times);
							element.remove(element.element("offlinetime"));
							Element newElement = element.addElement("offlinetime");
							newElement.setText(str);
							logger.debug("下线图片after add time :" + element.elementText("offlinetime"));

						}
					}
				} catch (RuntimeException e) {
					// 记录error级别的信息
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		};
		ScheduledExecutorService ofService = Executors.newSingleThreadScheduledExecutor();
		// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
		ofService.scheduleAtFixedRate(ofRunnable, 0, TimerTime, TimeUnit.MILLISECONDS);

	}
}
