package org.bo.creeper.core.dao;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * 数据库交互对象
 * 
 * @author 杨波
 * 
 *         2014-1-3
 */
public class SqlSessionDaoSupport {

	private static SqlSessionFactory factory;

	// 读取MyBatis配置文件，创建SqlSessionFactory
	public SqlSessionDaoSupport() {
		try {
			Reader reader = Resources
					.getResourceAsReader("mybatis/mybatis-config.xml");
			factory = new SqlSessionFactoryBuilder().build(reader, "mybatis3");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取SqlSession
	 * 
	 * @return
	 */
	public SqlSession getSqlSession() {
		return factory.openSession();
	}
}
