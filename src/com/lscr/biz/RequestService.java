package com.lscr.biz;

import com.lscr.entity.Param;
/**
 * 请求业务接口
 * @author Administrator
 *
 */
public interface RequestService {
	/**
	 * 判断应用是否开启接口开关
	 * @param param
	 * @return
	 */
	public boolean lscrInterfaceswitch(Param param);
	public int lscrcount(Param param);
	/**
	 * 判断应用是否开启解锁开关
	 * @param param
	 * @return
	 */
	public boolean lscrnoInterfaceswitch(Param param);
	/**
	 * 返回请求结果
	 * @param param
	 * @return
	 */
	public Object getResult(Param param);
	/**
	 * 测试返回数据
	 * @param param
	 * @return
	 */
	public Object getTestResult(Param param);
	/**
	 * 内测使用删除请求记录
	 * @param param
	 * @return
	 */
	public int delete(Param param);
 
}
