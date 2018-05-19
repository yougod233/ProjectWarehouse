package cn.appsys.service.info;

import java.util.List;

import cn.appsys.pojo.AppInfo;

public interface InfoService {
	public List<AppInfo> getAppInfoList(AppInfo info,int currentPageNo,int pageSize);

	public int getAppInfoCount(AppInfo info);

	public int getInfoCountById(int aid,String apkName);

	public boolean delInfoById(int aid);
	
	public AppInfo getInfo(int aid,String APKName);
	
	public boolean add(AppInfo appInfo);
	
	public boolean deleteAppLogo(int id);
	
	public boolean modify(AppInfo appInfo);
	
	public boolean appsysUpdateSaleStatusByAppId(AppInfo appInfoObj) throws Exception;
	
	public boolean appInfocheck(AppInfo appInfo);
}
