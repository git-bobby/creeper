package org.bo.creeper.core.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/**
 * Action中央控制器
 * 
 * @author 杨波
 * 
 *         2014-1-10
 */
public class ActionServlet extends HttpServlet {

	private static final long serialVersionUID = -8077984171016613760L;

	/**
	 * Action Map集合
	 */
	private Map<String, String> actionMap = new HashMap<String, String>();

	/**
	 * The init method
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {

		@SuppressWarnings("unchecked")
		Enumeration<String> em = config.getInitParameterNames();
		while (em.hasMoreElements()) {
			String p = em.nextElement();
			actionMap.put(p, config.getInitParameter(p));
		}
	}

	/**
	 * The doGet method
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response); // 转至doPost方法
	}

	/**
	 * The doPost method
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String returnUrl = "/error.jsp";
		String url = request.getRequestURI();
		System.out.println("访问地址：" + url);

		/******************************* Core S ****************************************/

		int index = -1;
		if ((index = url.lastIndexOf("/")) != -1) {
			url = url.substring(index + 1, url.length() - 3);
			String[] config = url.split("_");
			if (config.length > 1 && actionMap.containsKey(config[0])) {
				try {
					Class<?> clzz = Class.forName(actionMap.get(config[0]));
					Method method = clzz
							.getMethod(config[1], HttpServletRequest.class,
									HttpServletResponse.class);
					returnUrl = (String) method.invoke(clzz.newInstance(),
							request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		/******************************* Core E ****************************************/

		if (StringUtils.isNotBlank(returnUrl)) {
			final String separator = returnUrl.startsWith("/") ? "" : "/";
			response.sendRedirect(request.getContextPath() + separator
					+ returnUrl);
		}
	}

}