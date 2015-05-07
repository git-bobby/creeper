package org.bo.creeper.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 共通数据访问对象
 * 
 * @author 杨波
 * 
 * @param <T>
 */
public class CommonDao<T> extends SqlSessionDaoSupport {
	
	protected final Logger logger = LoggerFactory.getLogger(CommonDao.class);

	/**
	 * 保存
	 * 
	 * @param sqlName
	 *            SQL语句ID
	 * @param parames
	 *            SQL语句参数
	 */
	public int save(String sqlName, Object parames) {
		int result = 0;
		SqlSession sqlSession = getSqlSession();
		try {
			result = sqlSession.insert(sqlName, parames);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return result;
	}

	/**
	 * 保存
	 * 
	 * @param sqlName
	 *            SQL语句ID
	 * @param paramList
	 *            SQL语句参数
	 */
	@SuppressWarnings("rawtypes")
	public int save(String sqlName, List paramList) {
		int result = 0;
		SqlSession sqlSession = getSqlSession();
		try {
			result = sqlSession.insert(sqlName, paramList);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return result;
	}

	/**
	 * 更新
	 * 
	 * @param sqlName
	 *            SQL语句ID
	 * @param parames
	 *            SQL语句参数
	 */
	public int update(String sqlName, Object parames) {
		int result = 0;
		SqlSession sqlSession = getSqlSession();
		try {
			result = sqlSession.update(sqlName, parames);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return result;
	}

	/**
	 * 更新
	 * 
	 * @param sqlName
	 *            SQL语句ID
	 * @param parames
	 *            SQL语句参数
	 */
	@SuppressWarnings("rawtypes")
	public int update(String sqlName, Map parames) {
		int result = 0;
		SqlSession sqlSession = getSqlSession();
		try {
			result = sqlSession.update(sqlName, parames);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return result;
	}

	/**
	 * 更新
	 * 
	 * @param sqlName
	 *            SQL语句ID
	 * @param paramList
	 *            SQL语句参数
	 */
	@SuppressWarnings("rawtypes")
	public int update(String sqlName, List paramList) {
		int result = 0;
		SqlSession sqlSession = getSqlSession();
		try {
			result = sqlSession.update(sqlName, paramList);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return result;
	}

	/**
	 * 查询单条记录
	 * 
	 * @param sqlName
	 *            SQL语句ID
	 * @return 查询结果
	 */
	@SuppressWarnings("unchecked")
	public T selectOne(String sqlName) {
		T result = null;
		SqlSession sqlSession = getSqlSession();
		try {
			result = (T) sqlSession.selectOne(sqlName);
		} finally {
			sqlSession.close();
		}
		return result;
	}

	/**
	 * 查询单条记录
	 * 
	 * @param sqlName
	 *            SQL语句ID
	 * @param params
	 *            SQL语句参数
	 * @return 查询结果
	 */
	@SuppressWarnings("unchecked")
	public T selectOne(String sqlName, Object params) {
		T result = null;
		SqlSession sqlSession = getSqlSession();
		try {
			result = (T) sqlSession.selectOne(sqlName, params);
		} finally {
			sqlSession.close();
		}
		return result;
	}

	/**
	 * 查询多条记录
	 * 
	 * @param sqlName
	 *            SQL语句ID
	 * @return 查询结果
	 */
	@SuppressWarnings("unchecked")
	public List<T> selectList(String sqlName) {
		List<T> result = null;
		SqlSession sqlSession = getSqlSession();
		try {
			result = (List<T>) sqlSession.selectList(sqlName);
		} finally {
			sqlSession.close();
		}
		return result;
	}

	/**
	 * 查询多条记录
	 * 
	 * @param sqlName
	 *            SQL语句ID
	 * @param params
	 *            SQL语句参数
	 * @return 查询结果
	 */
	@SuppressWarnings("unchecked")
	public List<T> selectList(String sqlName, Object params) {
		List<T> result = null;
		SqlSession sqlSession = getSqlSession();
		try {
			result = (List<T>) sqlSession.selectList(sqlName, params);
		} finally {
			sqlSession.close();
		}
		return result;
	}

	/**
	 * 查询多条记录
	 * 
	 * @param sqlName
	 *            SQL语句ID
	 * @param params
	 *            SQL语句参数
	 * @param pageNo
	 *            需要查询的实际页
	 * @param pageSize
	 *            每页记录行数
	 * @return 查询结果
	 */
	@SuppressWarnings("unchecked")
	public List<T> selectList(String sqlName, Object params, int pageNo,
			int pageSize) {
		List<T> result = null;
		int skipResults = (pageNo - 1) * pageSize;
		SqlSession sqlSession = getSqlSession();
		try {
			result = (List<T>) sqlSession.selectList(sqlName, params,
					new RowBounds(skipResults, pageSize));
		} finally {
			sqlSession.close();
		}
		return result;
	}

	/**
	 * 删除
	 * 
	 * @param sqlName
	 *            SQL语句ID
	 * @param id
	 *            SQL语句参数
	 */
	public int delete(String sqlName, String id) {
		int result = 0;
		SqlSession sqlSession = getSqlSession();
		try {
			result = sqlSession.delete(sqlName, id);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return result;
	}

	/**
	 * 删除
	 * 
	 * @param sqlName
	 *            SQL语句ID
	 * @param o
	 *            SQL语句参数
	 */
	public int delete(String sqlName, Object o) {
		int result = 0;
		SqlSession sqlSession = getSqlSession();
		try {
			result = sqlSession.delete(sqlName, o);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return result;
	}

}
