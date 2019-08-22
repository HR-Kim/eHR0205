/**
 * @Class Name : User.java
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

/**
 * @author sist
 *
 */
public class User {

	/**사용자ID */
	private String u_id      ;
	/**사용자이름 */
	private String name      ;
	/**비번 */
	private String passwd    ;
	
	public User() { }

	public User(String u_id, String name, String passwd) {
		super();
		this.u_id = u_id;
		this.name = name;
		this.passwd = passwd;
	}

	public String getU_id() {
		return u_id;
	}

	public void setU_id(String u_id) {
		this.u_id = u_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	@Override
	public String toString() {
		return "User [u_id=" + u_id + ", name=" + name + ", passwd=" + passwd + "]";
	}
	
	
	
	
}
