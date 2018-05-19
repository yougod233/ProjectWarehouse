package cn.appsys.service.version;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;

public interface AppVersionService {

	public boolean delAppVersion(int aid);

	public AppVersion AppVersionById(int aid,int id);

	public boolean deleteApkFile(int id);

	public List<AppVersion> getAppVersionList(Integer appId);

	public boolean AddVersion(AppVersion appVersion);

	public boolean modify(AppVersion appVersion);
}
