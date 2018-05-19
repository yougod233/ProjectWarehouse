package cn.appsys.dao.user;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.User;

public interface UserMapper {
	public User getUserByName(@Param("username")String username);
}
