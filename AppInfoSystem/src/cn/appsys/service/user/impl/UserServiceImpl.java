package cn.appsys.service.user.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.user.UserMapper;
import cn.appsys.pojo.User;
import cn.appsys.service.user.UserService;
@Service
public class UserServiceImpl implements UserService {
	@Resource
	UserMapper userMapper;

	@Override
	public User login(String username, String userpwd) {
		User user = userMapper.getUserByName(username);
		if(user!=null) {
			if(!user.getDevPassword().equals(userpwd)) {
				user = null;
			}
		}
		return user;
	}
	
}
