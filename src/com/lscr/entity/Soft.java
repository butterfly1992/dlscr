package com.lscr.entity;


/**
 * 产品广告
 * 
 * @author Administrator
 * 
 */
public class Soft {

	private String id;
	private String name;
	private String apkurl;
	private int lsindex;
	private String pck;
	private String imgurl;
	private String logo;
	private Integer category;
	private Integer whatadv = 0;
	private Integer lscode;
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
	public String getApkurl() {
		return apkurl;
	}
	public void setApkurl(String apkurl) {
		this.apkurl = apkurl;
	}
 
	public String getPck() {
		return pck;
	}
	public void setPck(String pck) {
		this.pck = pck;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public Integer getWhatadv() {
		return whatadv;
	}
	public void setWhatadv(Integer whatadv) {
		this.whatadv = whatadv;
	}
	public Integer getLscode() {
		return lscode;
	}
	public void setLscode(Integer lscode) {
		this.lscode = lscode;
	}
 
	public int getLsindex() {
		return lsindex;
	}
	public void setLsindex(int lsindex) {
		this.lsindex = lsindex;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public Integer getCategory() {
		return category;
	}
	public void setCategory(Integer category) {
		this.category = category;
	}
 
}
