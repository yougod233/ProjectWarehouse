package cn.appsys.service.category.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.category.AppCategoryMapper;
import cn.appsys.pojo.AppCategory;
import cn.appsys.service.category.AppCategoryService;
@Service
public class AppCategoryServiceImpl implements AppCategoryService {

	@Resource
	AppCategoryMapper appCategoryMapper;
	
	@Override
	public List<AppCategory> getCategoryList() {
		return appCategoryMapper.getCategoryList();
	}

	@Override
	public List<AppCategory> getCategoryListById(int pid) {
		// TODO Auto-generated method stub
		return appCategoryMapper.getCategoryListById(pid);
	}

}
