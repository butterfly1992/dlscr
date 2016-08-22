package com.lscr.mapper;


import com.lscr.entity.App;
import com.lscr.entity.Entity;
import com.lscr.entity.Param;
import com.lscr.entity.Soft;

/**
 * 请求映射接口
 * @author Administrator
 *
 */
public interface RequestMapper extends SqlMapper {
	public App lscrswitch(Param param);//判断广告开关


	public Soft getSoftAdv(Param param);//产品请求

	public Entity getEntityAdv(Param param);//实体广告请求


	public App lscrcount(Param param);//解锁弹窗广告次数


	/**
	 * 内测删除请求记录
	 */
	public int delete(Param param);
}
