package org.bo.creeper.core.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.creeper.config.CreeperConfig;
import org.apache.creeper.config.CreeperConfig.AcquisitionResultType;
import org.apache.creeper.control.CreeperControl;
import org.apache.creeper.entity.CreeperHistory;
import org.apache.creeper.entity.CreeperTemp;
import org.apache.creeper.service.CreeperContentService;
import org.bo.creeper.core.dao.CommonDao;
import org.bo.creeper.core.utils.MD5;
import org.bo.creeper.core.utils.SerialNoUtil;

/**
 * 采集内容处理业务逻辑实现类
 * 
 * @author 杨波
 * 
 *         2013-10-15
 */
public class CreeperContentServiceImpl implements CreeperContentService {

	@SuppressWarnings("rawtypes")
	private CommonDao commonDao = new CommonDao();
	
	/**
	 * 根据标题唯一码查询采集历史记录
	 * 
	 * @param history
	 *            ----> 采集历史记录条件实体
	 * @return CreeperHistory
	 */
	@SuppressWarnings("unchecked")
	public CreeperHistory findSingleCreeperHistory(CreeperHistory history) {
		
		CreeperHistory cphis = null;
		history.setUniqueCode(MD5.getMD5(history.getTitle()));
		final List<CreeperHistory> list = commonDao.selectList("CreeperContent.findCreeperHistory", history);
		if (list != null && list.size() > 0) {
			cphis = list.get(0);
		}
		return cphis;
	}

	public void saveCreeperHistory(CreeperHistory history) {

		// 若此条内容未被录入，则执行以下操作（判断依据：标题是否已存在于DB）
		if (!AcquisitionResultType.TITLEEXIST.name().equals(history.getDescription())) {
			
			// 保存采集历史记录
			history.setHistoryId(SerialNoUtil.getTabPkSn("tbl_acquisition_history"));
			history.setUniqueCode(MD5.getMD5(history.getTitle()));
			commonDao.save("CreeperContent.saveCreeperHistory", history);
		}
	}

	public void saveCreeperTemp(CreeperTemp temp) {

		/*
		 * 说明：保存采集进度的同时，删除非本次采集的进度信息（即保存采集进度前，删除其他采集的进度信息）。 
		 */
		
		temp.setTempId(SerialNoUtil.getTabPkSn("tbl_acquisition_temp"));
		commonDao.save("CreeperContent.saveCreeperTemp", temp);
		
		doClearTemp(temp.getChannelUrl());
	}

	/**
	 * 删除非channelUrl的采集进度
	 * 
	 * @param channelUrl
	 */
	public void doClearTemp(String channelUrl) {
		
		/*
		 * 说明：删除非channelUrl的采集进度，进度表中同一时刻，只保存一次采集的进度。
		 * SQL语句如下：
		 *  delete from tbl_acquisition_temp where channel_url <> 'http:/xxx.com/index.jhtml'
		 * 注：这里的 http:/xxx.com/index.jhtml 即为 channelUrl 的值。
		 */
		
		commonDao.delete("CreeperContent.deleteCreeperTemp", channelUrl);
		
		if (StringUtils.isNotBlank(channelUrl) && getPercent() >= 100) {
			CreeperControl cpcl = new CreeperControl();
			cpcl.setStatus(CreeperConfig.START);
			cpcl = (CreeperControl) commonDao.selectOne("CreeperControl.findCreeperControlByCondition", cpcl);
			if (cpcl.getCurrNum() < cpcl.getTotalNum())
				commonDao.delete("CreeperContent.deleteCreeperTemp", "");
		}
	}

	/**
	 * 获取采集进度百分比
	 * 
	 * @return Integer ----> 返回百分比整数部分
	 */
	public Integer getPercent() {
		
		/*
		 * 说明： 获取采集进度表中的最大percent值。
		 * SQL语句如下：
		 *  select max(t.percent) from tbl_acquisition_temp t
		 * 或
		 *  select t.percent from tbl_acquisition_temp t order by t.temp_id desc limit 0,1  
		 */
		
		Object obj = commonDao.selectOne("CreeperContent.getCurrentProgress");
		return (Integer) (obj == null? 0 : obj);
	}

	/**
	 * 获取采集进度信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CreeperTemp> getProgressInfo() {
		
		return commonDao.selectList("CreeperContent.findCreeperTemp");
	}
}
