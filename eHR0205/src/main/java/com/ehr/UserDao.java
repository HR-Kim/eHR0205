package com.ehr;

import java.util.List;

import org.apache.log4j.Logger;

public interface UserDao {
	
	public int add(User user);
	public User get(String id);
	public List<User> getAll();
	public int deleteUser(User user);
	public int count(String uId);
	
}
