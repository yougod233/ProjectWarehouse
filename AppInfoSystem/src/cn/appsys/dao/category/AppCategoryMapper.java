package cn.appsys.dao.category;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;

public interface AppCategoryMapper {
	public List<AppCategory> getCategoryList();
	
	public List<AppCategory> getCategoryListById(@Param("pid")int pid);
}
