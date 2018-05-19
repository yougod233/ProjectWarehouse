package cn.appsys.service.info.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.info.InfoMapper;
import cn.appsys.dao.version.AppVersionMapper;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.service.info.InfoService;
@Service
public class InfoServiceImpl implements InfoService {

	@Resource
	InfoMapper infoMapper;
	
	@Resource
	AppVersionMapper appVersionMapper;

	@Override
	public List<AppInfo> getAppInfoList(AppInfo info,int currentPageNo,int pageSize) {
		return infoMapper.getInfoList(info,(currentPageNo-1)*pageSize,pageSize);
	}

	public int getAppInfoCount(AppInfo info) {
		return infoMapper.getInfoCount(info);
	}

	@Override
	public int getInfoCountById(int aid,String apkName) {
		return infoMapper.getInfoCountById(aid,apkName);
	}

	@Override
	public AppInfo getInfo(int aid,String APKName) {
		return infoMapper.getInfo(aid,APKName);
	}

	@Override
	public boolean delInfoById(int aid) {

		String fileLocPath = null;
		try {
			fileLocPath = infoMapper.getInfo(aid,null).getLogoLocPath();
			File file = new File(fileLocPath);
			if(file.exists())
				if(file.delete()){//删除服务器存储的物理文件
					if(infoMapper.delInfoById(aid)>0){//更新表
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
	public boolean add(AppInfo appInfo) {
		return infoMapper.add(appInfo)>0?true:false;
	}

	@Override
	public boolean deleteAppLogo(int id) {
		return infoMapper.deleteAppLogo(id)>0?true:false;
	}

	@Override
	public boolean modify(AppInfo appInfo) {
		return infoMapper.modify(appInfo)>0?true:false;
	}
	
	public boolean appInfocheck(AppInfo appInfo) {
		boolean flag = false;
		if(infoMapper.updateSatus(appInfo)>0) {
			flag = true;
		}
		return flag;
	}

	public boolean appsysUpdateSaleStatusByAppId(AppInfo appInfoObj) throws Exception{
		Integer operator = appInfoObj.getModifyBy();
		if(operator<0||appInfoObj.getId()<0) {
			throw new Exception();
		}
		AppInfo appInfo = infoMapper.getInfo(appInfoObj.getId(), null);
		if(null==appInfo) {
			return false;
		}else {
			switch (appInfo.getStatus()) {
			case 2:
				OnSale(appInfo,operator,4,2);
				break;
			case 5:
				OnSale(appInfo,operator,4,2);
				break;
			case 4:
				offSale(appInfo, operator, 5);
				break;
			default:
				return false;
			}
		}
		return true;
	}
	
	private void OnSale(AppInfo appInfo,Integer operator,Integer appInfStatus,Integer versionStatus) throws Exception {
		offSale(appInfo,operator,appInfStatus);
		setSaleSwitchToAppVersion(appInfo,operator,versionStatus);
	}
	
	private boolean offSale(AppInfo appInfo,Integer operator,Integer appInfStatus) throws Exception{
		AppInfo _appInfo = new AppInfo();
		_appInfo.setId(appInfo.getId());
		_appInfo.setStatus(appInfStatus);
		_appInfo.setModifyBy(operator);
		_appInfo.setOffSaleDate(new Date(System.currentTimeMillis()));
		infoMapper.modify(_appInfo);
		return true;
	}
	
	private boolean setSaleSwitchToAppVersion(AppInfo appInfo,Integer operator,Integer saleStatus) throws Exception{
		AppVersion appVersion = new AppVersion();
		appVersion.setId(appInfo.getVersionId());
		appVersion.setPublishStatus(saleStatus);
		appVersion.setModifyBy(operator);
		appVersion.setModifyDate(new Date(System.currentTimeMillis()));
		appVersionMapper.modify(appVersion);
		return false;
	}

}
