package cn.appsys.service.version.impl;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.info.InfoMapper;
import cn.appsys.dao.version.AppVersionMapper;
import cn.appsys.pojo.AppVersion;
import cn.appsys.service.info.InfoService;
import cn.appsys.service.version.AppVersionService;
@Service
public class AppVersionServiceImpl implements AppVersionService {

	@Resource
	AppVersionMapper appVersionMapper;
	
	@Resource
	InfoMapper infoMapper;

	@Override
	public boolean delAppVersion(int aid) {
		AppVersion appVersion = appVersionMapper.AppVersionById(aid,0);
		String fileLocPath = null;
		try {
			fileLocPath = appVersion.getApkLocPath();
			File file = new File(fileLocPath);
			if(file.exists())
				if(file.delete()){//删除服务器存储的物理文件
					if(appVersionMapper.delAppVersion(aid)>0){//更新表
						return true;
					}
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public AppVersion AppVersionById(int aid,int id) {
		return appVersionMapper.AppVersionById(aid,id);
	}

	@Override
	public boolean deleteApkFile(int id) {
		return appVersionMapper.deleteApkFile(id)>0?true:false;
	}

	@Override
	public List<AppVersion> getAppVersionList(Integer appId) {
		return appVersionMapper.getAppVersionList(appId);
	}

	@Override
	public boolean AddVersion(AppVersion appVersion) {
		boolean flag = false;
		int versionId = 0;
		if(appVersionMapper.AddVersion(appVersion)>0) {
			versionId = appVersion.getId();
			flag = true;
		}
		if(flag && infoMapper.updateVersionId(versionId, appVersion.getAppId()) > 0) {
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean modify(AppVersion appVersion) {
		return appVersionMapper.modify(appVersion)>0?true:false;
	}

}
