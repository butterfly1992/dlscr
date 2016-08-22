package com.lscr.biz.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lscr.biz.OperateService;
import com.lscr.dao.OperateDao;
import com.lscr.entity.Param;
import com.lscr.entity.Req;
import com.lscr.tool.Utils;
import com.lscr.tool.Variable;

/**
 * 操作实现类
 * @author Administrator
 *
 */
@Service("operservice")
@Transactional
public class OperateServiceImpl implements OperateService {

	private OperateDao operatedao;
	private String time_flag = Variable.formater.format(System
			.currentTimeMillis() - 4 * 24 * 60 * 60 * 1000);// 过期时间

	// 0---APK展示---1---ENTITY展示----2----点击下载----3----下载完成----4----安装完成----5----entity查看详情
	@Override
	public Object getOperResult(Param param) {
		// TODO Auto-generated method stub
		String time = Utils.DateTime();
		String oper = param.getOper();
		int flag = 0;
		param.setTime(time);
		if (oper.equals("0")) {// apk展示
			flag = operatedao.updateOper(param);
			if (flag == 0) {
				flag = operatedao.insertOper(param);
			}
			if (flag == 1) {// 记录用户展示量
				flag = operatedao.updateuserlook(param);
				if (flag == 0) {
					flag = operatedao.insertuserlook(param);
				}
			} else {
				return Variable.errorJson;
			}
		} else if (oper.equals("1")) {// 品牌广告展示
			flag = operatedao.updateOper(param);
			if (flag == 0) {
				flag = operatedao.insertOper(param);
			}
		} else if (oper.equals("2")) {// 点击
			flag = operatedao.updateOper(param);
			if (flag == 0) {
				flag = operatedao.insertOper(param);
			}
		} else if (oper.equals("3")) {// 下载
			flag = operatedao.updateOper(param);
			if (flag == 0) {
				flag = operatedao.insertOper(param);
			}
		} else if (oper.equals("4")) {// 安装
			List<Req> reqs = null;
			param.setAdvindex("ls" + param.getAdvindex());
			param.setTable(Utils.analyzeImsi(param));
			reqs = operatedao.getIndexs(param);
			if (reqs.size() == 0) {
				// 没有展示过 此处不执行
				Req req = new Req();
				req.setImei(param.getImei());
				req.setImsi(param.getImsi());
				req.setSetupindex(param.getAdvindex());
				req.setTime(time);// 当前时间
				req.setTable(param.getTable());
				flag = operatedao.insertIndex(req);
				flag = 1;
			} else {
				String setupindex = reqs.get(0).getSetupindex();
				String date_time = reqs.get(0).getTime();
				if (isContain(setupindex, param.getAdvindex())) {
					// 安装过 此处不执行
					return Variable.errorJson;
				} else {
					Req req = new Req();
					req.setImei(param.getImei());
					req.setImsi(param.getImsi());
					req.setSetupindex(param.getAdvindex());
					req.setTime(time);// 当前时间
					req.setTime_flag(date_time);// 之前时间
					req.setTable(param.getTable());
					flag = operatedao.updateNoovertimeIndex(req);
				}
			}
			param.setTime(time);
			if (flag == 1) {
				flag = operatedao.updateOper(param);
				if (flag == 0) {
					flag = operatedao.insertOper(param);
				}
			} else {
				return Variable.errorJson;
			}
		} else if (oper.equals("5")) {
			flag = operatedao.updateOper(param);
			if (flag == 0) {
				Utils.log.error("更新品牌详情error");
			}
		}
		if (flag > 0) {
			return Variable.correntJson;
		} else
			return Variable.errorJson;
	}

	@Autowired
	public void setOperatedao(OperateDao operatedao) {
		this.operatedao = operatedao;
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
	public String getReqsoftId(String advindex) {
		// TODO Auto-generated method stub
		return operatedao.getReqsoftId(advindex);
	}
}
