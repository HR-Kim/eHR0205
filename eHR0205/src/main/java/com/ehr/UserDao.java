/**
 * @Class Name : UserDao.java
 * @Description : 
 * @Modification Information
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2019-08-19           최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2019-08-19 
 * @version 1.0
 * @see
 *
 *  Copyright (C) by H.R. KIM All right reserved.
 */
package com.ehr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import org.apache.log4j.Logger;

/**
 * @author sist
 *
 */
public class UserDao {

	static final Logger LOG= Logger.getLogger(UserDao.class);
	
	private RowMapper<User> userMapper = new RowMapper<User>() {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User tmp=new User();
			tmp.setU_id(rs.getString("u_id"));
			tmp.setName(rs.getString("name"));
			tmp.setPasswd(rs.getString("passwd"));
			
			return tmp;
		}
	};	
		
	private JdbcTemplate jdbcTemplate;

	private DataSource dataSource;

	public UserDao() {
		
	}
	
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		
		this.dataSource = dataSource;
	}
	
	/**
	 * 전체 조회 u_id asc순
	 * @return
	 */
	public List<User> getAll(){
		StringBuilder sb=new StringBuilder();
		
		sb.append(" SELECT u_id    \n");
		sb.append("      , name    \n");
		sb.append("      , passwd  \n");
		sb.append(" FROM users     \n");
		sb.append(" ORDER BY u_id  \n");
		
		LOG.debug("=============================");
		LOG.debug("02. sql=\n"+sb.toString());
		LOG.debug("=============================");		
		List<User> list =jdbcTemplate.query(sb.toString(), userMapper);
		return list;
	}
	
	
	/**
	 * 사용자 Count
	 * @param user
	 * @return int
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int count(String uId)throws ClassNotFoundException,SQLException{

		int cnt = 0;//조회 count
	
		//----------------------------------------
		//02.SQL작성
		//----------------------------------------		
		StringBuilder sb=new StringBuilder();
		sb.append(" SELECT COUNT(*) cnt      \n");
		sb.append(" FROM users               \n");
		sb.append(" WHERE u_id like ?        \n");
		
		LOG.debug("=============================");
		LOG.debug("02. sql=\n"+sb.toString());
		LOG.debug("=============================");		
		
		cnt = jdbcTemplate.queryForObject(sb.toString()
				, new Object[] { "%"+uId+"%"}
				, Integer.class);
		
		
		LOG.debug("=============================");
		LOG.debug("04. Run cnt=\n"+cnt);
		LOG.debug("=============================");		
		
		return cnt;
	}	
	

	public PreparedStatement makeStatement(Connection  c)
			throws SQLException{
		//----------------------------------------
		//02.SQL작성
		//----------------------------------------
		PreparedStatement ps = null;
		StringBuilder sb=new StringBuilder();
		sb.append(" DELETE FROM users  \n");
		
		LOG.debug("=============================");
		LOG.debug("02. sql=\n"+sb.toString());
		LOG.debug("=============================");		
		
		//----------------------------------------
		//03.PreparedStatement
		//----------------------------------------			
		ps = c.prepareStatement(sb.toString());
		
		
				
		return ps;
	}
	
	public int jdbcContextWithStatementStrategy(StatementStrategy st)
	  throws SQLException{
		//----------------------------------------
		//01.DB 연결
		//----------------------------------------		
		Connection  c = null;
		PreparedStatement ps = null;
		int flag = 0;
		
		try {
		
			c = dataSource.getConnection();
			LOG.debug("=============================");
			LOG.debug("01. Connection="+c);
			LOG.debug("=============================");		
			
			//========================================
			//=변하는 부분 메소드로 추출                                            =
			//========================================
			ps = st.makePrepardStatement(c);
			
			
			//----------------------------------------
			//04.Run sql
			//----------------------------------------	
			flag = ps.executeUpdate();
			LOG.debug("=============================");
			LOG.debug("04. Run flag=\n"+flag);
			LOG.debug("=============================");		
		}catch(SQLException s) {
			LOG.debug("=============================");
			LOG.debug("04. SQLException=\n"+s.getMessage());
			LOG.debug("=============================");				
		}finally {
			//----------------------------------------
			//05.자원반납
			//----------------------------------------	
			if(null !=ps) {
				try {
					ps.close();
				}catch(SQLException s) {
					
				}
			}
			
			if(null !=c) {
				try {
					c.close();
				}catch(SQLException s) {
					
				}
			}			
		}

		
		return flag;		
	}


	
	/**
	 * 삭제
	 * @param user
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int deleteUser(User user) throws SQLException{
		String query =" DELETE FROM users WHERE u_id = ? ";
//		int flag =jdbcContext.executeSql(query, user);
		Object[] args = {user.getU_id()};
		int flag = jdbcTemplate.update(query, args);
		
		return flag;
	}
	
	
	/**
	 * 
	 * @Method Name  : add
	 * @작성일   : 2019. 8. 19.
	 * @작성자   : sist
	 * @변경이력  : 최초작성
	 * @Method 설명 : 단건등록
	 * @param user
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int add(User user)throws ClassNotFoundException,SQLException{		
		
		int flag = 0;	
		//----------------------------------------
		//02.SQL작성
		//----------------------------------------		
		StringBuilder sb=new StringBuilder();
		sb.append(" INSERT INTO users (  \n");
		sb.append("     u_id,            \n");
		sb.append("     name,            \n");
		sb.append("     passwd           \n");
		sb.append(" ) VALUES (           \n");
		sb.append("     ?,               \n");
		sb.append("     ?,               \n");
		sb.append("     ?                \n");
		sb.append(" )                    \n");
		
		LOG.debug("=============================");
		LOG.debug("02. sql=\n"+sb.toString());
		LOG.debug("=============================");		
		Object[] args = {user.getU_id()
				        ,user.getName()
				        ,user.getPasswd()
		};
		
		flag = jdbcTemplate.update(sb.toString(), args);
		
		return flag;
	}
	
	/**
	 * 
	 * @Method Name  : get
	 * @작성일   : 2019. 8. 19.
	 * @작성자   : sist
	 * @변경이력  : 최초작성
	 * @Method 설명 :단건조회
	 * @param id
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public User get(String id)throws ClassNotFoundException,SQLException{
		User outVO=null;
	
		//----------------------------------------
		//02.SQL작성
		//----------------------------------------		
		StringBuilder sb=new StringBuilder();
		sb.append(" SELECT          \n");
		sb.append("     u_id,       \n");
		sb.append("     name,       \n");
		sb.append("     passwd      \n");
		sb.append(" FROM users      \n");
		sb.append(" WHERE u_id = ?  \n");
		
		LOG.debug("=============================");
		LOG.debug("02. sql=\n"+sb.toString());
		LOG.debug("=============================");		
		
		outVO = jdbcTemplate.queryForObject(sb.toString()
				, new Object[] {id}
				, userMapper);
		
		//----------------------------------------
		//06.outVO==null 예외발생
		//----------------------------------------
		if(null == outVO) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return outVO;
	}	
	


}
