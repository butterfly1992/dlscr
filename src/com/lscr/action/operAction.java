package com.lscr.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lscr.biz.OperateService;
import com.lscr.entity.Param;
import com.lscr.tool.Utils;
import com.lscr.tool.Variable;

/**
 * 操作action入口
 * 
 * @author Administrator
 * 
 */
@Controller
public class operAction {
	private OperateService operservice;

	// 0---APK展示---1---ENTITY展示----2----点击下载----3----下载完成----4----安装完成----5----entity查看详情
	@RequestMapping(value = "/oper", method = { RequestMethod.POST })
	public @ResponseBody
	Object operate(Param param) {// 处理产品操作方法，参数验证
		if (Utils.isNULL(param.getAppid()) || Utils.isNULL(param.getImei())
				|| Utils.isNULL(param.getImsi())
				|| Utils.isNULL(param.getAdvindex())
				|| Utils.isNULL(param.getGysdkv())
				|| Utils.isNULL(param.getOper())) {
			Utils.log.error("+Operate【" + param.getOper() + ";appid："
					+ param.getAppid() + ";softid：" + param.getSoftid()
					+ ";imsi:" + param.getImsi() + ";Imei:" + param.getImei()
					+ ";index：" + param.getAdvindex() + ";Gysdkv:"
					+ param.getGysdkv() + "】");
			return Variable.errorJson;
		}
		if (!Utils.getMD5(param.getImei()).equals(param.getKey())) {
			Utils.log.error("Key Error|key:" + param.getImei());
			return Variable.errorJson;
		}
		if (Variable.testId.contains(param.getAppid())
				|| Variable.invalidImei.contains(param.getImei())) {
			Utils.log.info("测试id不统计数据");
			return Variable.errorJson;
		}
		if (Utils.isNULL(param.getSoftid())) {
			String sid = operservice.getReqsoftId(param.getAdvindex());
			Utils.log.info("Softid无：" + sid + ";appid:" + param.getAppid()
					+ ";");
			param.setSoftid(sid);
		}
		Utils.log.info("===============Operate=【" + param.getOper() + ";appid："
				+ param.getAppid() + ";softid：" + param.getSoftid() + ";index："
				+ param.getAdvindex() + "】");
		Object object = operservice.getOperResult(param);
		Utils.log.info("=result：【" + object.toString() + "】");
		return object;
	}

	@Autowired
	public void setOperservice(OperateService operservice) {
		this.operservice = operservice;
	}

}
