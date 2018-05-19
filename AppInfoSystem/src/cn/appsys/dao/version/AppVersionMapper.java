package cn.appsys.dao.version;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;

public interface AppVersionMapper {
	public int delAppVersion(@Param("aid")int aid);
	
	public AppVersion AppVersionById(@Param("aid")int aid,@Param("id")int id);
	
	public int deleteApkFile(int id);
	
	public List<AppVersion> getAppVersionList(@Param("appId")Integer appId);
	
	public int AddVersion(AppVersion appVersion);
	
	public int modify(AppVersion appVersion);
}
