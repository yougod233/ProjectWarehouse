package cn.appsys.service.category;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;

public interface AppCategoryService {
	public List<AppCategory> getCategoryList();
	
	public List<AppCategory> getCategoryListById(int pid);
}
