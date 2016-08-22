package com.lscr.dao;

import com.lscr.entity.Entity;
import com.lscr.entity.Param;
import com.lscr.entity.Soft;
/**
 * 请求接口
 * @author Administrator
 *
 */
public interface RequestDao {
	public boolean lscrInterfaceswitch(Param param);//接口开关

	public boolean lscrnoInterfaceswitch(Param param);//解锁开关


	public int lscrcount(Param param);//解锁弹出次数

	/**
	 * 获取产品广告
	 * 
	 * @param param
	 * @return
	 */
	public Soft getSoftAdv(Param param);

	/**
	 * 获取实体广告
	 * 
	 * @param param
	 * @return
	 */
	public Entity getEntityAdv(Param param);
	/**
	 * 内测删除请求记录
	 */
	public int delete(Param param);
}
