package cn.appsys.dao.info;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.AppInfo;

public interface InfoMapper {
	public List<AppInfo> getInfoList(@Param("info")AppInfo info,@Param("currentPageNo")int currentPageNo,@Param("pageSize")int pageSize);
	
	public int getInfoCount(AppInfo info);
	
	public int getInfoCountById(@Param("aid")int aid,@Param("apkName")String apkName);
	
	public int delInfoById(@Param("aid")int aid);
	
	public AppInfo getInfo(@Param("id")Integer aid,@Param("APKName")String APKName);
	
	public int add(AppInfo appInfo);
	
	public int deleteAppLogo(int id);
	
	public int modify(AppInfo appInfo);
	
	public int updateVersionId(@Param("versionId") int versionId,@Param("AppId") int AppId);
	
	public int updateSatus(AppInfo appInfo);
}
