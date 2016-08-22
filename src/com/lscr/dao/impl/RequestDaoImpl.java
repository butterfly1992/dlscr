package com.lscr.dao.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lscr.dao.RequestDao;
import com.lscr.entity.App;
import com.lscr.entity.Entity;
import com.lscr.entity.Param;
import com.lscr.entity.Soft;
import com.lscr.mapper.RequestMapper;
import com.lscr.tool.Utils;

/**
 * 请求实现类
 * @author Administrator
 *
 */
@Repository("reqdao")
public class RequestDaoImpl implements RequestDao {
	@Autowired
	private RequestMapper mapper;

	@Override
	public boolean lscrInterfaceswitch(Param param) {
		// TODO Auto-generated method stub
		App app = mapper.lscrswitch(param);
		if (app == null)
			return false;
		else if (app.getLscr_in_status() == 0)
			return false;
		else
			return true;
	}

	@Override
	public boolean lscrnoInterfaceswitch(Param param) {
		// TODO Auto-generated method stub
		App app = mapper.lscrswitch(param);
		if (app == null)
			return false;
		else if (app.getLscr_out_status() == 0)
			return false;
		else
			return true;
	}

	@Override
	public int lscrcount(Param param) {
		// TODO Auto-generated method stub
		App app = mapper.lscrcount(param);
		if (app == null)
			return 0;
		else if (app.getLscr_out_count() >= 1)
			return app.getLscr_out_count();
		else
			return 0;
	}

	/**
	 * 获取soft广告
	 */
	@Override
	public Soft getSoftAdv(Param param) {
		// TODO Auto-generated method stub
		Soft soft = mapper.getSoftAdv(param);
		if (soft != null) {// 处理要推送的产品
			Utils.log.info("result—Soft—:【name:" + soft.getName() + ";pck："
					+ soft.getPck() + ";wareindex：" + soft.getLsindex() + "；】");
		}
		return soft;
	}

	/**
	 * 获取实体广告
	 */
	@Override
	public Entity getEntityAdv(Param param) {
		// TODO Auto-generated method stub
		Entity entity = mapper.getEntityAdv(param);
		if (entity != null) {
			Utils.log.info("result—Entity—【name:" + entity.getName()
					+ ";wareindex：" + entity.getLsindex() + "；】");
		}
		return entity;
	}

	public boolean isContain(String s, String c) {
		String a[] = s.split(",");
		for (int i = 0; i < a.length; i++) {
			if (a[i].equals(c)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int delete(Param param) {
		// TODO Auto-generated method stub
		return mapper.delete(param);
	}

}