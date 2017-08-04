package cn.com.plustv;

//结构体对应的java类
public class M2cLogoInfo_t {
	// unsigned short ox;
	// unsigned short oy;
	// unsigned short width;
	// unsigned short height;
	// int showit;
	// char name[32];
	private short ox;
	private short oy;
	private short width;
	private short height;
	private int showit;
	private String name;

	public short getOx() {
		return ox;
	}

	public void setOx(short ox) {
		this.ox = ox;
	}

	public short getOy() {
		return oy;
	}

	public void setOy(short oy) {
		this.oy = oy;
	}

	public short getWidth() {
		return width;
	}

	public void setWidth(short width) {
		this.width = width;
	}

	public short getHeight() {
		return height;
	}

	public void setHeight(short height) {
		this.height = height;
	}

	public int getShowit() {
		return showit;
	}

	public void setShowit(int showit) {
		this.showit = showit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}