package org.bo.creeper.core.service;

import java.util.Date;
import java.util.List;

import org.apache.creeper.config.CreeperConfig;
import org.apache.creeper.control.CreeperControl;
import org.apache.creeper.service.CreeperService;
import org.bo.creeper.core.dao.CommonDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 采集运行管理业务逻辑实现类
 * 
 * @author 杨波
 * 
 *         2013-10-15
 */
public class CreeperServiceImpl implements CreeperService {

	private static Logger logger = LoggerFactory
			.getLogger(CreeperServiceImpl.class);
	
	@SuppressWarnings("rawtypes")
	private CommonDao commonDao = new CommonDao();
	
	/**
	 * 查询是否有采集正在运行，若无，则返回0；若有，则返回最大采集队列号，即Queue
	 * 
	 * @return Integer
	 */
	public Integer hasStarted() {
		
		/*
		 * 说明：若 getStarted()方法返回null，则返回0；否则返回最大队列号值+1
		 * 一定要为最大值加1哦
		 */
		
		return getStarted() == null ? 0 : getMaxQueue() + 1;
	}

	/**
	 * 获取采集最大队列号
	 * 
	 * @return Integer
	 */
	public Integer getMaxQueue() {
		
		/*
		 * 说明：从数据库中，查询表tbl_acquisition中queue列的最大值，可通过max函数取得。 
		 * SQL如下：
		 * select max(queue) from tbl_acquisition;
		 */
		Object queueObj = commonDao.selectOne("CreeperControl.getMaxQueue");
		return queueObj != null ? (Integer) queueObj : 0;
	}

	/**
	 * 获取当前正在运行的数据采集对象
	 * 
	 * @return CreeperControl
	 */
	public CreeperControl getStarted() {
		
		/*
		 * 说明：从数据库表tbl_acquisition中，查询状态标识为CreeperConfig.START，即为1的记录，有且仅有一条。 
		 * SQL如下：
		 * select * from tbl_acquisition t where t.`status` = 1;
		 */
		CreeperControl cpcl = new CreeperControl();
		cpcl.setStatus(CreeperConfig.START);
		Object obj = commonDao.selectOne(
				"CreeperControl.findCreeperControlByCondition", cpcl);
		return obj != null ? (CreeperControl) obj : null;
	}

	/**
	 * 将要运行的数据采集排队，然后程序会按号依次执行
	 * 
	 * @param ids
	 *            ----> 采集IDs
	 * @param queueNum
	 *            ----> 最大队列号
	 */
	public void saveToQueue(String[] ids, Integer queueMaxNum) {
		
		/*
		 * 说明：根据主键ID值，从表tbl_acquisition中，逐一查询每条记录，若当前记录为非运行状态或待排队的记录，则用最大队列号++更新其值。 
		 * SQL略...
		 */
		for (String id : ids) {
			CreeperControl cpcl = findById(id);
			if (cpcl.getStatus() == CreeperConfig.START || cpcl.getQueue() > 0) { // 此处代码作用：判断当前记录是否已运行或已排队
				queueMaxNum++;
				continue; // 满足条件，则直接跳过循环，进行下一次循环
			}
			cpcl.setQueue(queueMaxNum++); // 未排队的采集，则进行最大队列号++操作。
			
			// 执行DB持久化更新作用，将新Queue的值保存到DB，此处代码略...
			commonDao.update("CreeperControl.updateCreeperControl", cpcl);
		}
		logger.info("执行saveToQueue排队算法.Over");
	}

	/**
	 * 根据ID查询数据采集对象
	 * 
	 * @param id
	 * @return CreeperControl
	 */
	public CreeperControl findById(String id) {
		
		/*
		 * 说明：根据主键ID值，从表tbl_acquisition中，查询唯一一条记录。 
		 * SQL如下：
		 * select * from tbl_acquisition t where t.acquisition_id='1001';
		 */
		CreeperControl cpcl = new CreeperControl();
		cpcl.setAcquisitionId(id);
		Object obj = commonDao.selectOne(
				"CreeperControl.findCreeperControlByCondition", cpcl);
		return obj != null ? (CreeperControl) obj : null;
	}

	/**
	 * 数据采集启动后，更新该采集的状态、开始时间、结束时间等信息。
	 * 
	 * @param id
	 *            ----> 采集ID
	 * @return CreeperControl
	 */
	public CreeperControl update_start(String id) {
		
		/*
		 * 说明：根据主键ID值，从表tbl_acquisition中，查询唯一一条记录，然后更新相关属性：状态、开始时间、结束时间、总条目等信息，最后持久化到DB。
		 * 若根据ID查询到的对象为null，则直接返回。
		 * SQL如下：
		 * select * from tbl_acquisition t where t.acquisition_id='1001';
		 */
		
		CreeperControl cpcl = findById(id);
		if (cpcl == null) { // 判断是否为null，若成立，则直接返回null
			return cpcl;
		}
		
		cpcl.setStatus(CreeperConfig.START);
		cpcl.setStartTime(new Date());
		cpcl.setEndTime(null);
		cpcl.setTotalItem(0);
		
		// 持久化到DB，此处代码略...
		commonDao.update("CreeperControl.updateCreeperControl", cpcl);
		logger.info("执行update_start启动采集.Over");
		return cpcl;
	}

	/**
	 * 数据采集结束后，更新该采集的状态、结束时间等信息。
	 * 
	 * @param id
	 *            ----> 采集ID
	 * @return CreeperControl
	 */
	public void update_stop(String id) {
		
		/*
		 * 说明：根据主键ID值，从表tbl_acquisition中，查询唯一一条记录，
		 * 然后更新相关属性：状态、结束时间、当前页、当前条目、总条目等信息，最后持久化到DB。
		 * 若根据ID查询到的对象为null，则直接返回。
		 * SQL如下：
		 * select * from tbl_acquisition t where t.acquisition_id='1001';
		 */
		
		CreeperControl cpcl = findById(id);
		if (cpcl == null) {
			return;
		}
		
		cpcl.setStatus(CreeperConfig.STOP);
		cpcl.setEndTime(new Date());
		cpcl.setCurrNum(0);
		cpcl.setCurrItem(0);
		cpcl.setTotalItem(0);
		//cpcl.setQueue(0);
		
		// 持久化到DB，代码略...
		commonDao.update("CreeperControl.updateCreeperControl", cpcl);
	}

	/**
	 * 暂停数据采集
	 * 
	 * @param id
	 *            ----> 采集ID
	 * @return CreeperControl
	 */
	public void update_pause(String id) {
		
		/*
		 * 说明：根据主键ID值，从表tbl_acquisition中，查询唯一一条记录，若查询结果为null，则直接返回
		 * 如果不为null，且状态为开始状态，则将其状态设置为暂停，然后持久化到DB。
		 * 在中断操作中，自动会将该采集暂停运行。
		 */
		
		CreeperControl cpcl = findById(id);
		if (cpcl == null) {
			return;
		}
		
		if (cpcl.getStatus() == CreeperConfig.START) {
			cpcl.setStatus(CreeperConfig.PAUSE);
			
			// 持久化到DB，此处代码略...
			commonDao.update("CreeperControl.updateCreeperControl", cpcl);
		}
	}

	/**
	 * 取消数据采集
	 * 
	 * @param id
	 *            ----> 采集ID
	 */
	public void update_cancel(String id) {
		
		/*
		 * 说明：根据主键ID值，从表tbl_acquisition中，查询唯一一条记录，然后将其属性queue的值设置为0，最后执行持久化操作。
		 */
		
		CreeperControl cpcl = findById(id);
		cpcl.setQueue(0);
		
		// 持久化到DB，此处代码略... 		
		commonDao.update("CreeperControl.updateCreeperControl", cpcl);
	}

	/**
	 * 是否需要强制中断，暂停与停止都称为中断
	 * 
	 * @param id
	 *            ----> 采集ID
	 * @param currNum
	 *            ----> 采集地址当前页码
	 * @param currItem
	 *            ----> 已采集到当前页的第几条
	 * @param totalItem
	 *            ----> 当前页共有多少条目
	 * @return
	 */
	public boolean doIsNeedBreak(String id, int currNum, int currItem,
			int totalItem) {
		
		/*
		 * 说明：根据主键ID值，从表tbl_acquisition中，查询唯一一条记录。
		 * 若查询结果为null、采集为暂停状态、采集为停止状态，则均要执行中断操作，返回true，否则返回false。
		 * 不同状态操作，请参照以下代码更新相关属性，然后进行持久化操作。
		 */
		
		boolean flag = false;
		CreeperControl cpcl = findById(id);
		if (cpcl == null) {
			flag = true;
		} else if (cpcl.isPuase()) { // 已暂停
			cpcl.setCurrNum(currNum);
			cpcl.setCurrItem(currItem);
			cpcl.setTotalItem(totalItem);
			cpcl.setEndTime(new Date());
			flag = true;
		} else if (cpcl.isStop()) { // 已停止
			cpcl.setCurrNum(0);
			cpcl.setCurrItem(0);
			cpcl.setTotalItem(0);
			cpcl.setEndTime(new Date());
			flag = true;
		} else {
			cpcl.setCurrNum(currNum);
			cpcl.setCurrItem(currItem);
			cpcl.setTotalItem(totalItem);
			flag = false;
		}
		
		// 将更新的数据，持久化到DB
		commonDao.update("CreeperControl.updateCreeperControl", cpcl);
		return flag;
	}

	/**
	 * 获取下一个待运行的采集对象
	 * 
	 * @return CreeperControl
	 */
	@SuppressWarnings("unchecked")
	public CreeperControl doPopCreeperFromQueue() {
		
		/*
		 * 说明：从表tbl_acquisition中，查询下一个待运行的采集活动。待采集的标识为：queue>0  
		 * 只要是待运行的采集，其queue值必大于0；只要是运行状态的采集，其queue值必等于0
		 * queue>0 的记录可能为多条，按queue正排序，只取第一条出来（可通过sql抓取第一条，也可以从DB抓取一个列表后，取索引为0的记录。）。
		 * SQL如下：
		 * select * from tbl_acquisition t where t.queue > 0 ORDER BY t.queue LIMIT 1,1
		 */
		
		List<Object> list = commonDao.selectList(
				"CreeperControl.popCreeperFromQueue", null, 1, 1);
		CreeperControl cpcl = null;
		if (list != null && list.size() > 0) {
			cpcl = (CreeperControl) list.get(0);
			update_cancel(cpcl.getAcquisitionId());
		}
		return cpcl;
	}
}
