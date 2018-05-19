package cn.appsys.service.user;

import cn.appsys.pojo.User;

public interface UserService {
	public User login(String username,String userpwd);
}
