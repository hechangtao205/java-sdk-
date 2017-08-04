package cn.com.plustv;

public class SmartCard {
	static {
		System.loadLibrary("MyDLL5");
		//System.loadLibrary("M2CCGKeyer");// 后面使用C/C++编写的JAVA能直接调用的库
		// 后面使用C/C++编写的JAVA能直接调用的库

	}

	/*方法一：
	 * ***********************************************
	 * 上载logo到设备，支持文件格式有：BMP和TGA。 
	 * 支持BMP和TGA 24bit或32bit像素深度； 支持TGA RLE压缩格式
	 * 1、fileName：输入参数。 表示本地存储的Logo文件名，例如C://test.tga
	 * ×************************************************
	 */
	private native boolean M2CLogoCgKeyerUpLoadFile(String  fileName);
	/*方法二：
	 * ******************************************** 
	 * 删除设备上的logo文件 文件名应该是
	 * "/logo/filename"样式
	 * 1、fileName：输入参数。 表示设备上存储的Logo文件名，例如/logo/test.tga
	 */
	 
	private native boolean M2CLogoCgKeyerDeleteFile(String  fileName);

	/*方法三：
	 * ********************************************
	 * 把指定文件名的logo文件（已加载到了设备上）叠加到视频上
	 * 文件名应该是 "/logo/filename"样式 1、fileName：输入参数。
	 * 表示设备上存储的Logo文件名，例如/logo/test.tga 2、x, y：输入参数。
	 *  表示logo叠加在视频上的坐标位置。
	 * 3、bEnabled：输入参数。 表示logo是否显示。
	 */
	private native boolean M2CLogoCgKeyerSetupLogo(String fileName, int x, int y, boolean bEnabled);
	/* 方法四：
	 * ********************************************
	   取消已叠加在视频上的logo图像
		 文件名应该是 "/logo/filename"样式
		1、fileName：输入参数。
		   表示设备上存储的Logo文件名，例如/logo/test.tga
	 ********************************************* */
	private native boolean M2CLogoCgKeyerCancelLogo(String fileName);
	/*方法五：
	 *  *********************************************** 
	  得到设备空闲空间大小, 一般在向设备加载Logo文件时要
		检查设备的空闲空间容量
		1、freeSpace：输出参数。
		   表示剩余空间大小，字节为单位。
	 *********************************************** */
	private native boolean M2CLogoCgKeyerGetSystemCapacity(int freeSpace);
	/*方法六：
	 *  *********************************************** 
	  得到设备上已加载的文件链表。
		链表格式：filename1;filename2;filename3;....;filenamen;
		文件名字符串由分号(;)区分。
	1、filelist: 输入输出参数。
	    必须分配空间，最大到1500字节。
	2、fileNums：输出参数。
	    表示输出fileList中包含文件个数。
	 *********************************************** */
	private native boolean M2CLogoCgKeyerLogoFileList(String filelist, int fileNums);
	/* 方法七：
	 * ******************************************************
	 得到错误代码表示的字符串错误信息.
	 1、ErrorCode：错误代码，调用GetLastError()得到。
	 2、errString: 字符串信息指针。
	 ******************************************************/
	private native boolean M2CLogoCgKeyerGetErrorString(int ErrorCode, String errString);
	/*方法八：
	 *  ******************************************************
	 删除设备上的模版信息.

	 !!! 注意:
	   执行该函数将删除设备上保存的模版文件信息，在下次开机重启后，以前的模版不会再显示，
		 需重新编辑模版文件。
	 
	 ******************************************************/
	private native boolean M2CLogoCgKeyerDeletePayout();

	/*方法九：
	 * ****************************************************** 
	 * 得到设备上的模版信息.
	 * 1、filelist: 输入输出参数。
	 *  必须分配空间，最大到1500字节。 
	 * filelist输出时包含fileNums个m2cLogoInfo_t结构记录。
	 *  2、fileNums：输出参数。 表示输出filelist中包含m2cLogoInfo_t个数。
	 * 
	 * !!! 注意： 必须检查返回值，返回值为TRUE时，filelist和fileNums中的参数才有意义。
	 * 
	 * 
	 ******************************************************/
	private native boolean M2CLogoCgKeyerGetPayout(String filelist, int fileNums);

	/*方法十：
	 * ****************************************************** 
	 * 得到设备上指定文件名的信息
	 * 1、fileName: 输入。 指定设备上存在的文件名称。如/logo/test.tga形式
	 * 2、fileInfo：输出参数，是一个m2cLogoInfo_t 结构，必须分配空间。 !!! 注意：
	 * 必须检查返回值，返回值为TRUE时，fileInfo中的参数才有意义。
	 ******************************************************/
	private native boolean M2CLogoCgKeyerGetFileInfo( String fileName, M2cLogoInfo_t m2cLogoInfo_t);

	// 外部类能调用的方法一
	public boolean tjSDK(String fileName) {

		return this.M2CLogoCgKeyerUpLoadFile(fileName);
	}
	//方法二：
	public boolean tjSDK02(String fileName) {
		
		return this.M2CLogoCgKeyerDeleteFile(fileName);
	}
	//方法三：
	public boolean tjSDK03(String fileName, int x, int y, boolean bEnabled) {
		
		return this.M2CLogoCgKeyerSetupLogo(fileName, x , y , bEnabled);
	}
	//方法四：
	public boolean tjSDK04(String fileName) {
		
		return this.M2CLogoCgKeyerCancelLogo(fileName);
	}
	//方法五：
	public boolean tjSDK05(int freeSpace) {
		
		return this.M2CLogoCgKeyerGetSystemCapacity(freeSpace);
	}
	//方法六：
	public boolean tjSDK06(String filelist, int fileNums) {
		
		return this.M2CLogoCgKeyerLogoFileList(filelist,fileNums);
	}
	//方法七：
	public boolean tjSDK07(int ErrorCode, String errString) {
		
		return this.M2CLogoCgKeyerGetErrorString(ErrorCode,errString);
	}
	//方法八：
	public boolean tjSDK08() {
		
		return this.M2CLogoCgKeyerDeletePayout();
	}
	//方法九：
	public boolean tjSDK09(String filelist, int fileNums) {
		
		return this.M2CLogoCgKeyerGetPayout(filelist,fileNums);
	}
	//方法十：
	public boolean tjSDK10(String fileName, M2cLogoInfo_t m2cLogoInfo_t) {
		
		return this.M2CLogoCgKeyerGetFileInfo(fileName,m2cLogoInfo_t);
	}
	
}
