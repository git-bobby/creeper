package org.bo.creeper.core.action;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.creeper.CreeperExecutor;
import org.apache.creeper.service.CreeperContentService;
import org.apache.creeper.service.CreeperService;
import org.bo.creeper.core.service.CreeperContentServiceImpl;
import org.bo.creeper.core.service.CreeperServiceImpl;

/**
 * 数据采集Action
 * 
 * @author 杨波
 * 
 *         2014-1-10
 */
public class CreeperAction implements Serializable {

	private static final long serialVersionUID = 8746094045770628797L;

	private CreeperService creeperService = new CreeperServiceImpl();

	private CreeperContentService creeperContentService = new CreeperContentServiceImpl();

	private CreeperExecutor creeperExcExecutor;

	/**
	 * 构造函数
	 */
	public CreeperAction() {

		creeperExcExecutor = new CreeperExecutor();
		creeperExcExecutor.setCreeperService(this.creeperService);
		creeperExcExecutor.setCreeperContentService(this.creeperContentService);
	}

	/**
	 * 开始
	 */
	public String start(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		creeperExcExecutor
				.start(new String[] { request.getParameter("acqid") });
		return "/index.jsp";
	}

	/**
	 * 暂停
	 */
	public String pause(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		creeperExcExecutor.pause(request.getParameter("acqid"));
		return "/index.jsp";
	}

	/**
	 * 停止
	 */
	public String stop(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		creeperExcExecutor.stop(request.getParameter("acqid"));
		return "/index.jsp";
	}
}
