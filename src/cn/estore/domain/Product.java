package cn.estore.domain;

import java.io.Serializable;

/**
 * 商品类
 * 
 * 
 * 
 */
public class Product implements Serializable {
	
	private String id; // 商品编号
	private String name; // 名称
	private double price; // 价格
	private String category; // 分类
	private int pnum; // 数量
	private String imgurl; // 图片路径
	private String description; // 描述

	private int totalSaleNum; // 总销售数量

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getPnum() {
		return pnum;
	}

	public void setPnum(int pnum) {
		this.pnum = pnum;
	}

	// 原图路径
	public String getImgurl() {
		return imgurl;
	}

	// 返回小图路径
	public String getImgurl_s() {
		String prefix = imgurl.substring(0, imgurl.lastIndexOf("."));
		String ext = imgurl.substring(imgurl.lastIndexOf("."));
		return prefix + "_s" + ext;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTotalSaleNum(int totalSaleNum) {
		this.totalSaleNum = totalSaleNum;
	}

	public int getTotalSaleNum() {
		return totalSaleNum;
	}

}
