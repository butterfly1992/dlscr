package com.lscr.biz.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danga.MemCached.MemCachedClient;
import com.lscr.biz.RequestService;
import com.lscr.dao.RequestDao;
import com.lscr.entity.Entity;
import com.lscr.entity.Param;
import com.lscr.entity.Soft;
import com.lscr.tool.MemcacheUtil;
import com.lscr.tool.Utils;
import com.lscr.tool.Variable;
/**
 * 请求实现类
 * @author Administrator
 *
 */
@Service("reqservice")
@Transactional
public class RequestServiceImpl implements RequestService {

	private RequestDao reqdao;
	private MemCachedClient mcc = MemcacheUtil.mcc;

	@Override
	public boolean lscrInterfaceswitch(Param param) {
		// TODO Auto-generated method stub
		return reqdao.lscrInterfaceswitch(param);
	}

	@Override
	public boolean lscrnoInterfaceswitch(Param param) {
		// TODO Auto-generated method stub
		return reqdao.lscrnoInterfaceswitch(param);
	}

	@Override
	public Object getResult(Param param) {
		// TODO Auto-generated method stub
		Object object = null;
		int mode_flag = param.getMode();
		Utils.log.info("【imei：" + param.getImei() + "；imsi：" + param.getImsi()
				+ "；appid：" + param.getAppid() + "；sdkv：" + param.getGysdkv()
				+ "；mode：" + mode_flag + "】");
		/**
		 * 应用外展示（解锁广告）
		 */
		if (mode_flag == 1 && lscrnoInterfaceswitch(param)) {// 無接口開關並且判斷參數是否為解鎖請求
			int effect = 0;
			if (mcc.get("nlscr_out_" + param.getImei() + param.getImsi()) == null) {// 获取今天有效解锁弹出广告次数
				effect = lscrcount(param);
			} else {
				effect = Integer.parseInt(mcc.get(
						"nlscr_out_" + param.getImei() + param.getImsi())
						.toString());
			}
			if (effect > 0) {
				object = getObject(param, effect, 0);
				Utils.log.info("this is 应用外：【" + (5 - effect) + "】次");
			} else {
				Utils.log.info("this is 应用外： 0 次");
				return Variable.validJson;
			}
		}
		/**
		 * 应用内展示（接口广告）
		 */
		else if (mode_flag == 0 && lscrInterfaceswitch(param)) {
			int effect = 0;
			if (mcc.get("nlscr_in_" + param.getImei() + param.getImsi()) == null) {// 获取今天有效解锁弹出广告次数
				effect = 10;
			} else {
				effect = Integer.parseInt(mcc.get(
						"nlscr_in_" + param.getImei() + param.getImsi())
						.toString());
			}
			if (effect > 0) {
				object = getObject(param, 0, effect);
				Utils.log.info("this is 应用内： 【" + (11 - effect) + "】次");
			} else {
				Utils.log.info("this is 应用内： 0 次");
				return Variable.validJson;
			}
		}
		/**
		 * 其他就是错误了
		 */
		else
			object = Variable.errorJson;
		return object;
	}

	/**
	 * 获取广告对象
	 * 
	 * @param param
	 *            手机用户信息及appid
	 * @param times
	 *            应用外次数
	 * @param intimes
	 *            应用内次数
	 * @return 返回结果（查询出广告返回对象，查询不出返回flag:0）
	 */
	public Object getObject(Param param, int times, int intimes) {
		Object object = null;
		if (Utils.isNULL(param.getImsi())) {
			param.setImsi("123456789012345");
		}
		param.setCarrieroperator(operators(param.getImsi()));
		try {
			Date expiryd = getDefinedDateTime(23, 59, 59, 0);// 设置失效时间
			// TODO Auto-generated method stub
			if (mcc.get("nlscr_req" + param.getImei() + param.getImsi()) == null) {// 首次弹出
				param.setLscode(0);
				Soft soft = reqdao.getSoftAdv(param);// 查询出弹出的产品
				if (soft != null) {// 有弹出的产品，记录优先级，并减少一次弹出数目
					mcc.set("nlscr_apkindex_" + param.getImei()
							+ param.getImsi(), soft.getLscode());
					if (times > 0) {
						mcc.set("nlscr_out_" + param.getImei()
								+ param.getImsi(), times - 1, expiryd);
					}
					if (intimes > 0) {
						mcc.set("nlscr_in_" + param.getImei() + param.getImsi(),
								intimes - 1, expiryd);
					}
					int next = 0;// 0是产品广告，1是品牌广告
					if (times > 0)// 应用外次数记录
						next = 0;// 应用外全部展示产品广告
					if (intimes > 0)// 应用内次数判断
						next = 0;// 全部展示产品广告
					// next = ((intimes %3) > 0) ? 0 : 1;//3的倍数展示品牌广告
					mcc.set("nlscr_req" + param.getImei() + param.getImsi(),
							next);
				}
				return soft;
			}/* 判断首次弹窗结束 */
			Integer f = Integer
					.parseInt(mcc.get(
							"nlscr_req" + param.getImei() + param.getImsi())
							.toString());// 获取弹出什么类型广告
			if (f == 0) {// 产品广告
				if (mcc.get("nlscr_apkindex_" + param.getImei()
						+ param.getImsi()) == null) {
					mcc.set("nlscr_apkindex_" + param.getImei()
							+ param.getImsi(), 0);
					object = Variable.errorJson;
				} else {
					/* 展示apk */
					Integer code = (Integer) mcc.get("nlscr_apkindex_"
							+ param.getImei() + param.getImsi());
					param.setLscode(code);
					Soft soft = reqdao.getSoftAdv(param);
					if (soft == null) {// 查询不到了数据时，从数据库重新获取优先级
						param.setLscode(0);
						soft = reqdao.getSoftAdv(param);
					}
					if (soft != null) {
						mcc.set("nlscr_apkindex_" + param.getImei()
								+ param.getImsi(), soft.getLscode());
						if (times > 0) {
							mcc.set("nlscr_out_" + param.getImei()
									+ param.getImsi(), times - 1, expiryd);
						}
						if (intimes > 0) {
							mcc.set("nlscr_in_" + param.getImei()
									+ param.getImsi(), intimes - 1, expiryd);
						}
					}
					object = soft;
				}
				int next = 0;
				if (times > 0)
					next = 0;
				if (intimes > 0)
					next = 0;// 下次展示产品广告
				// next = ((intimes % 3) > 0) ? 0 : 1;//3的倍数展示品牌广告
				mcc.set("nlscr_req" + param.getImei() + param.getImsi(), next);// 下次展示广告
			} else if (f == 1) {// 品牌广告
				if (mcc.get("nlscr_entindex_" + param.getImei()
						+ param.getImsi()) == null) {
					mcc.set("nlscr_entindex_" + param.getImei()
							+ param.getImsi(), 0);
					param.setLscode(0);
					Entity entity = reqdao.getEntityAdv(param);
					if (entity != null) {
						object = entity;
						mcc.set("nlscr_entindex_" + param.getImei()
								+ param.getImsi(), entity.getPriority());
						if (times > 0) {
							mcc.set("nlscr_out_" + param.getImei()
									+ param.getImsi(), times - 1, expiryd);
						}
						if (intimes > 0) {
							mcc.set("nlscr_in_" + param.getImei()
									+ param.getImsi(), intimes - 1, expiryd);
						}
					}
				} else {
					/* 展示apk */
					Integer code = (Integer) mcc.get("nlscr_entindex_"
							+ param.getImei() + param.getImsi());
					param.setLscode(code);
					Entity entity = reqdao.getEntityAdv(param);
					if (entity == null) {// 查询不出品牌广告，从头开始查询弹出
						param.setLscode(0);
						entity = reqdao.getEntityAdv(param);
					}
					if (entity != null) {
						mcc.set("nlscr_entindex_" + param.getImei()
								+ param.getImsi(), entity.getPriority());
						if (times > 0) {
							mcc.set("nlscr_out_" + param.getImei()
									+ param.getImsi(), times - 1, expiryd);
						}
						if (intimes > 0) {
							mcc.set("nlscr_in_" + param.getImei()
									+ param.getImsi(), intimes - 1, expiryd);
						}
					}
					object = entity;
				}
				int next = 0;
				if (times > 0)
					next = 0;
				if (intimes > 0)
					next = 0;
				// next = ((intimes % 3) > 0) ? 0 : 1;//3的倍数展示品牌广告
				mcc.set("nlscr_req" + param.getImei() + param.getImsi(), next);
				// 下次展示产品广告
			} else {
				System.out.println("错误!广告类型不为null,0,1中的一个");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return object;
	}

	/**
	 * 测试请求
	 */
	@Override
	public Object getTestResult(Param param) {
		Object object = null;
		try {
			if (Utils.isNULL(param.getImsi())) {
				param.setImsi("123456789012345");
			}
			Date expiryd = getDefinedDateTime(23, 59, 59, 0);
			param.setCarrieroperator(operators(param.getImsi()));
			// TODO Auto-generated method stub
			if (mcc.get("nlscr_t_req" + param.getImei()) == null) {// 首次弹出
				mcc.set("nlscr_t_req" + param.getImei(), 0, expiryd);// 下次展示实体广告
				param.setLscode(0);
				Soft soft = reqdao.getSoftAdv(param);
				mcc.set("nlscr_tapk_" + param.getImei(), soft.getLscode());
				return soft;
			}
			Integer f = Integer.parseInt(mcc.get(
					"nlscr_t_req" + param.getImei()).toString());// 获取弹出什么类型广告
			if (f == 0) {// 产品广告
				if (mcc.get("nlscr_tapk_" + param.getImei()) == null) {
					param.setLscode(0);
					Soft soft = reqdao.getSoftAdv(param);
					mcc.set("nlscr_tapk_" + param.getImei(), soft.getLscode());
				} else {
					/* 展示apk */
					Integer code = (Integer) mcc.get("nlscr_tapk_"
							+ param.getImei());
					param.setLscode(code);
					Soft soft = reqdao.getSoftAdv(param);
					if (soft == null) {
						param.setLscode(0);
						soft = reqdao.getSoftAdv(param);
					}
					if (soft != null) {
						mcc.set("nlscr_tapk_" + param.getImei(),
								soft.getLscode());
					}
					object = soft;
				}
				mcc.set("nlscr_t_req" + param.getImei(), 0, expiryd);// 下次展示实体广告
			}
			// ///////////////////////////////////////////////////////////////////Entity
			// adv
			/*
			 * else if (f == 1) { if (mcc.get("nlscr_tent_" + param.getImei())
			 * == null) { mcc.set("nlscr_t_req" + param.getImei(), 0,
			 * expiryd);// 下次展示产品广告 param.setLscode(0); Entity entity =
			 * reqdao.getEntityAdv(param); if (entity != null)
			 * mcc.set("nlscr_tent_" + param.getImei(), entity.getPriority());
			 * object = entity; } else { 按照上次的顺序展示优先级 Integer code = (Integer)
			 * mcc.get("nlscr_tent_" + param.getImei()); param.setLscode(code);
			 * Entity entity = reqdao.getEntityAdv(param); if (entity == null) {
			 * param.setLscode(0); entity = reqdao.getEntityAdv(param); } if
			 * (entity != null) mcc.set("nlscr_tent_" + param.getImei(),
			 * entity.getPriority()); object = entity; } mcc.set("nlscr_t_req" +
			 * param.getImei(), 0, expiryd);// 下次展示产品广告 }
			 */
			else {
				System.out.println("错误!展示类型不是0,1中的一个");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return object;
	}

	public Date getDefinedDateTime(int hour, int minute, int second,
			int milliSecond) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MILLISECOND, milliSecond);
		Date date = new Date(cal.getTimeInMillis());
		return date;
	}

	/**
	 * 判断运营商
	 * 
	 * @param reqdao
	 */
	public int operators(String imsi) {
		// 判断是何种运营商 0:所有 1:移动 2:联通 3:电信 4:铁通
		Integer carrieroperator = 0;
		String mnc = imsi.substring(0, 5);
		if ("46000".equals(mnc) || "46002".equals(mnc) || "46007".equals(mnc)) {
			// 是移动用户
			carrieroperator = 1;
		} else if ("46001".equals(mnc) || "46006".equals(mnc)) {
			// 是联通用户
			carrieroperator = 2;
		} else if ("46003".equals(mnc) || "46005".equals(mnc)) {
			// 是电信用户`
			carrieroperator = 3;
		} else if ("46020".equals(mnc)) {
			// 是铁通用户
			carrieroperator = 4;
		}
		return carrieroperator;
	}

	@Autowired
	public void setReqdao(RequestDao reqdao) {
		this.reqdao = reqdao;
	}

	@Override
	public int lscrcount(Param param) {
		// TODO Auto-generated method stub
		return reqdao.lscrcount(param);
	}

	@Override
	public int delete(Param param) {
		// TODO Auto-generated method stub
		param.setTable(Utils.analyzeImsi(param));
		return reqdao.delete(param);
	}

}
