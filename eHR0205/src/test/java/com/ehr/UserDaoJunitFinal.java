package com.ehr;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoJunitFinal {

	private Logger LOG=Logger.getLogger(UserDaoJunitFinal.class);
	
	@Autowired
	ApplicationContext  context;
	@Autowired
	private UserDao dao;
	private User user01;
	private User user02;
	private User user03;
	
	
	@Before
	public void setUp() {
		user01=new User("j01_124","이상무01","1234");
		user02=new User("j02_124","이상무02","1234");
		user03=new User("j03_124","이상무03","1234");
		LOG.debug("==============================");
		LOG.debug("=01 context="+context);
		LOG.debug("=01 dao="+dao);
		LOG.debug("==============================");
	}
	
	
	@After
	public void tearDown() {
		LOG.debug("^^^^^^^^^^^^^^^^^^^^^^");
		LOG.debug("99 tearDown()");
		LOG.debug("^^^^^^^^^^^^^^^^^^^^^^");		
	}
	
	@Test(expected=DuplicateKeyException.class)
	public void duplicatedKey() {
		//org.springframework.dao.DuplicateKeyException
		//ORA-00001: 무결성 제약 조건(HRSPRING.PK_USERS)에 위배됩니다
		//-------------------------------------------
		//삭제
		//-------------------------------------------
		dao.deleteUser(user01);
		dao.deleteUser(user02);
		dao.deleteUser(user03);
		
		dao.add(user01);
		dao.add(user01);
	}
	
	
	@Test
	@Ignore
	public void getAll() throws SQLException {
		//-------------------------------------------
		//삭제
		//-------------------------------------------
		//dao.deleteUser(user01);
		///dao.deleteUser(user02);
		//dao.deleteUser(user03);
		
		List<User> list = dao.getAll();
		for(User user:list) {
			LOG.debug(user);
		}
		
	}
	
	
	@Test(expected = EmptyResultDataAccessException.class)
	@Ignore
	public void getFailure()throws ClassNotFoundException, SQLException {

		User user01=new User("j01_124","이상무01","1234");
		User user02=new User("j02_124","이상무02","1234");
		User user03=new User("j03_124","이상무03","1234");
		
		//-------------------------------------------
		//삭제
		//-------------------------------------------
		dao.deleteUser(user01);
		dao.deleteUser(user02);
		dao.deleteUser(user03);
		assertThat(dao.count("_124"), is(0));
		
		dao.get("unkonwnUserId");
	}
	
	@Test
	@Ignore
	public void count() throws ClassNotFoundException, SQLException {

		//-------------------------------------------
		//삭제
		//-------------------------------------------
		dao.deleteUser(user01);
		dao.deleteUser(user02);
		dao.deleteUser(user03);
		assertThat(dao.count("_124"), is(0));
		
		//-------------------------------------------
		//1건추가
		//-------------------------------------------	
		dao.add(user01);
		assertThat(dao.count("_124"), is(1));
		//-------------------------------------------
		//1건추가
		//-------------------------------------------	
		dao.add(user02);
		assertThat(dao.count("_124"), is(2));
		//-------------------------------------------
		//1건추가
		//-------------------------------------------	
		dao.add(user03);
		assertThat(dao.count("_124"), is(3));
			
		
	}
	
	@Test(timeout = 5000) //1.JUnit에게 테스트용 메소드임을 알려줌
	@Ignore
	public void addAndGet() {//2. public
		//j01_ip: j01_124

			dao.deleteUser(user01);
			dao.deleteUser(user02);
			dao.deleteUser(user03);
			assertThat(dao.count("_124"), is(0));
			
			
			LOG.debug("==============================");
			LOG.debug("=01 단건등록=");
			LOG.debug("==============================");			
			
			int flag = dao.add(user01);
			flag = dao.add(user02);
			flag = dao.add(user03);			
			assertThat(dao.count("_124"), is(3));
			
			LOG.debug("==============================");
			LOG.debug("=01.01 add flag="+flag);
			LOG.debug("==============================");
			
			assertThat(flag,is(1));
			
			LOG.debug("==============================");
			LOG.debug("=02 단건조회");
			LOG.debug("==============================");			
			
			User  userOne = dao.get(user01.getU_id());
			
			assertThat(userOne.getU_id(), is(user01.getU_id()));
			assertThat(userOne.getName(), is(user01.getName()));
			assertThat(userOne.getPasswd(), is(user01.getPasswd()));

	}
	
}
