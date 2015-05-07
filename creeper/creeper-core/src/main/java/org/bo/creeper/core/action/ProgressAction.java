package org.bo.creeper.core.action;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.creeper.entity.CreeperTemp;
import org.apache.creeper.service.CreeperContentService;
import org.bo.creeper.core.service.CreeperContentServiceImpl;

/**
 * 数据采集进度信息Action
 * 
 * @author 杨波
 * 
 *         2014-1-10
 */
public class ProgressAction implements Serializable {

	private static final long serialVersionUID = 1828597362432935089L;

	private CreeperContentService creeperContentService = new CreeperContentServiceImpl();

	/**
	 * 进度数据
	 */
	public String data(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		int percent = this.creeperContentService.getPercent();
		
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD>");
		out.println("	<TITLE>数据采集进度信息</TITLE>");
		out.println("	<meta http-equiv='Refresh' content='1;./pgs_d.do'>");
		out.println("	</HEAD>");
		out.println("  <BODY onload=\"javascript: window.parent.showPercentInfo(" + percent + ");\">");
		
		List<CreeperTemp> list = this.creeperContentService.getProgressInfo();
		for (int a = 0; a < list.size(); a++) {

			CreeperTemp temp = list.get(a);
			StringBuilder sb = new StringBuilder();
			sb.append(temp.getPercent()).append("%\t第").append(temp.getSeq())
					.append("条\t").append(temp.getContentUrl()).append("\t")
					.append(temp.getTitle()).append("\t")
					.append(temp.getDescription());
			System.out.println(sb.toString());
			out.println(sb.toString());
			out.println("<BR/>");
		}

		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
		
		return null;
	}
}
