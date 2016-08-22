package com.lscr.mapper;



import java.util.List;

import com.lscr.entity.Param;
import com.lscr.entity.Req;

/**
 * 操作映射接口
 * @author Administrator
 *
 */
public interface OperateMapper extends SqlMapper {
	public Object getOperResult(Param param);
	public List<Req> getIndexs(Param param);
	public int insertIndex(Req req);
	public int updateTimeoutIndex(Req req);//更新过期的索引
	public int updateNoovertimeIndex(Req req);//更新未过期的索引
	
	/*插入数据**/
	public int insertlookOper(Param param);
	public int insertentlookOper(Param param);
	public int insertclickOper(Param param);
	public int insertdownOper(Param param);
	public int insertsetupOper(Param param);
	
	/*更新数据*/
	public int updatelookOper(Param param);//产品展示
	public int updateentlookOper(Param param);//实体展示
	public int updateclickOper(Param param);//产品点击
	public int updatedownOper(Param param);//产品下载
	public int updatesetupOper(Param param);//产品安装
	public int updateentdetialOper(Param param);//实体查看详情
	/*用户展示量	 */
	public int insertuserlook(Param param);

	public int updateuserlook(Param param);
	
	public String getReqsoftId(String advindex);

	
}
