package cn.appsys.service.backenduser.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.backenduser.BackendUserMapper;
import cn.appsys.pojo.BackendUser;
import cn.appsys.service.backenduser.BackendUserService;

@Service
public class BackendUserServiceImpl implements BackendUserService {
	
	@Resource
	BackendUserMapper backendUserMapper;

	@Override
	public BackendUser login(String userCode, String userPwd) {
		BackendUser backendUser = null;
		try {
			backendUser = backendUserMapper.getLoginUser(userCode);
			if(null==backendUser||!backendUser.getUserPassword().equals(userPwd)) {
				backendUser = null;
			}
		} catch (Exception e) {
			backendUser = null;
		}
		return backendUser;
	}
}
