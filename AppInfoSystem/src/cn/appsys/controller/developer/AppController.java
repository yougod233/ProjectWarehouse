package cn.appsys.controller.developer;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.User;
import cn.appsys.service.category.AppCategoryService;
import cn.appsys.service.dictionary.DataDictionaryService;
import cn.appsys.service.info.InfoService;
import cn.appsys.service.version.AppVersionService;
import cn.appsys.tools.Constants;
import cn.appsys.tools.PageSupport;

@Controller
@RequestMapping("/sys/app")
public class AppController {
	@Resource
	InfoService infoService;
	@Resource
	DataDictionaryService dataDictionaryService;
	@Resource
	AppCategoryService appCategoryService;
	@Resource
	AppVersionService appVersionService;
	
	Logger logger = Logger.getLogger(AppController.class);
	
	@RequestMapping("/list")
	public String getAppList(HttpServletRequest req) {
		String pageIndex = req.getParameter("pageIndex");
		int pageSize = Constants.pageSize;
		int currentPageNo = 1;
		if(pageIndex != null){
    		try{
    			currentPageNo = Integer.valueOf(pageIndex);
    		}catch(NumberFormatException e){
    			return "/AppInfoSystem/error";
    		}
    	}
		AppInfo info = new AppInfo();
		info.setSoftwareName(req.getParameter("querySoftwareName"));
		info.setStatus(req.getParameter("queryStatus")!=""&&req.getParameter("queryStatus")!=null?Integer.parseInt(req.getParameter("queryStatus")):0);
		info.setFlatformId(req.getParameter("queryFlatformId")!=""&&req.getParameter("queryFlatformId")!=null?Integer.parseInt(req.getParameter("queryFlatformId")):0);
		info.setCategoryLevel3(req.getParameter("queryCategoryLevel3")!=""&&req.getParameter("queryCategoryLevel3")!=null?Integer.parseInt(req.getParameter("queryCategoryLevel3")):0);
		info.setCategoryLevel1(req.getParameter("queryCategoryLevel1")!=""&&req.getParameter("queryCategoryLevel1")!=null?Integer.parseInt(req.getParameter("queryCategoryLevel1")):0);
		info.setCategoryLevel2(req.getParameter("queryCategoryLevel2")!=""&&req.getParameter("queryCategoryLevel2")!=null?Integer.parseInt(req.getParameter("queryCategoryLevel2")):0);
		int totalCount	= infoService.getAppInfoCount(info);
		PageSupport pages=new PageSupport();
    	pages.setCurrentPageNo(currentPageNo);
    	pages.setPageSize(pageSize);
    	pages.setTotalCount(totalCount);
    	int totalPageCount = pages.getTotalPageCount();
    	if(currentPageNo < 1){
    		currentPageNo = 1;
    	}else if(currentPageNo > totalPageCount){
    		currentPageNo = totalPageCount;
    	}
		List<AppInfo> list = infoService.getAppInfoList(info,currentPageNo,pageSize);
		List<DataDictionary> statusList = dataDictionaryService.getDataDictionaryList("APP_STATUS");
		List<DataDictionary> flatFormList = dataDictionaryService.getDataDictionaryList("APP_FLATFORM");
		List<AppCategory> appCategorys = appCategoryService.getCategoryList();
		req.setAttribute("appInfoList", list);
		req.setAttribute("statusList", statusList);
		req.setAttribute("flatFormList", flatFormList);
		req.setAttribute("categoryLevel1List", appCategorys);
		req.setAttribute("queryCategoryLevel1",info.getCategoryLevel1());
		if(info.getCategoryLevel1()>0) {
			List<AppCategory> appCategorys2 = appCategoryService.getCategoryListById(info.getCategoryLevel1());
			req.setAttribute("categoryLevel2List",appCategorys2);
		}
		req.setAttribute("queryCategoryLevel2",info.getCategoryLevel2());
		if(info.getCategoryLevel2()>0) {
			List<AppCategory> appCategorys3 = appCategoryService.getCategoryListById(info.getCategoryLevel2());
			req.setAttribute("categoryLevel3List",appCategorys3);
		}
		req.setAttribute("queryCategoryLevel3",info.getCategoryLevel3());
		req.setAttribute("queryStatus", info.getStatus());
		req.setAttribute("queryFlatformId", info.getFlatformId());
		req.setAttribute("pages", pages);
		return "developer/appinfolist";
	}
	@RequestMapping("/categorylevellist")
	@ResponseBody
	public Object getCategory(@RequestParam String pid) {
		int id = 0;
		List<AppCategory> appcategory = null;
		if(StringUtils.isNullOrEmpty(pid)) {
			appcategory = appCategoryService.getCategoryList();
		}else {
			id = Integer.parseInt(pid);
			appcategory = appCategoryService.getCategoryListById(id);
		}
		return JSONArray.toJSON(appcategory);
	}
	@RequestMapping("/delapp")
	@ResponseBody
	public Object delAppInfo(@RequestParam int id) {
		HashMap<String,String> result = new HashMap<String,String>();
		if(infoService.getInfoCountById(id,null)>0) {
			appVersionService.delAppVersion(id);
			if(infoService.delInfoById(id)) {
				result.put("delResult", "true");
			}else {
				result.put("delResult", "false");
			}
		}else{
			result.put("delResult", "notexist");
		}
		return JSONArray.toJSON(result);
	}
	
	@RequestMapping("/appinfoadd")
	public String Appinfoadd(HttpServletRequest req) {
		List<DataDictionary> statusList = dataDictionaryService.getDataDictionaryList("APP_STATUS");
		List<DataDictionary> flatFormList = dataDictionaryService.getDataDictionaryList("APP_FLATFORM");
		List<AppCategory> appCategorys = appCategoryService.getCategoryList();
		req.setAttribute("statusList", statusList);
		req.setAttribute("flatFormList", flatFormList);
		req.setAttribute("categoryLevel1List", appCategorys);
		return "developer/appinfoadd";
	}
	
	@RequestMapping("/datadictionarylist")
	@ResponseBody
	public Object Datadictionarylist(@RequestParam String tcode) {
		List<DataDictionary> flatFormList = dataDictionaryService.getDataDictionaryList(tcode);
		return JSONArray.toJSON(flatFormList);
	}
	
	@RequestMapping("/apkexist")
	@ResponseBody
	public Object apkexist(@RequestParam String APKName) {
		HashMap<String,String> result = new HashMap<String,String>();
		if(StringUtils.isNullOrEmpty(APKName)) {
			result.put("APKName", "empty");
		}else if(infoService.getInfoCountById(0,APKName)>0) {
			result.put("APKName", "exist");
		}else {
			result.put("APKName", "noexist");
		}
		return JSONArray.toJSON(result);
	}
	
	@RequestMapping(value="/appinfoaddsave",method=RequestMethod.POST)
	public String Appinfoaddsave(AppInfo appInfo,HttpSession session,HttpServletRequest request,@RequestParam(value="a_logoPicPath",required=false) MultipartFile attach) {
		String logoPicPath = null;
		String logoLocPath = null;
		if(!attach.isEmpty()) {
			String path = request.getSession().getServletContext().getRealPath("statics"+java.io.File.separator+"uploadfiles");
			logger.info("uploadFile path:"+path);
			String oldFileName = attach.getOriginalFilename();
			String prefix = FilenameUtils.getExtension(oldFileName);
			int filesize = 500000;
			if(attach.getSize() > filesize) {
				request.setAttribute("fileUploadError",Constants.FILEUPLOAD_ERROR_4);
				return "appinfoadd";
			}else if(prefix.equals("jpg")||prefix.equals("png")||prefix.equals("jepg")||prefix.equals("pneg")) {
				String fileName = appInfo.getAPKName()+".jpg";
				File targetFile = new File(path,fileName);
				if(!targetFile.exists()) {
					targetFile.mkdirs();
				}try {
					attach.transferTo(targetFile);
				}catch(Exception e) {
					e.printStackTrace();
					request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_2);
					return "appinfoadd";
				}
				logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
				logoLocPath = path+File.separator+fileName;
			}else {
				request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_3);
				return "appinfoadd";
			}
		}
		appInfo.setCreatedBy(((User)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setCreationDate(new Date());
		appInfo.setLogoPicPath(logoPicPath);
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setDevId(((User)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setStatus(1);
		try {
			if(infoService.add(appInfo)) {
				return "redirect:/sys/app/list";
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "appinfoadd";
	}
	
	@RequestMapping("/appinfomodify")
	public String Appinfomodify(HttpServletRequest req) {
		String aid = req.getParameter("id");
		int id = 0;
		if(aid != null||aid!="") {
			id = Integer.parseInt(aid);
		}
		logger.debug("modifyAppInfo --------- id: " + id);
		AppInfo appInfo = null;
		try {
			appInfo = infoService.getInfo(id,null);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		req.setAttribute("appInfo",appInfo);
		return "developer/appinfomodify";
	}
	
	@RequestMapping("/delfile")
	@ResponseBody
	public Object Delfile(@RequestParam(value="flag",required=false) String flag,@RequestParam(value="id",required=false) String id) {
		HashMap<String,String> resultMap = new HashMap<String,String>();
		String fileLocPath = null;
		if(flag ==null || flag.equals("")||id ==null || id.equals("")) {
			resultMap.put("result", "failed");
		}else if(flag.equals("logo")) {
			try {
				fileLocPath = (infoService.getInfo(Integer.parseInt(id), null)).getLogoLocPath();
				File file = new File(fileLocPath);
				if(file.exists()) {
					if(file.delete()) {
						if(infoService.deleteAppLogo(Integer.parseInt(id))) {
							resultMap.put("result","success");
						}
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else if(flag.equals("apk")) {
			try {
				fileLocPath = (appVersionService.AppVersionById(0,Integer.parseInt(id))).getApkLocPath();
				File file = new File(fileLocPath);
				if(file.exists()) {
					if(file.delete()) {
						if(appVersionService.deleteApkFile(Integer.parseInt(id))) {
							resultMap.put("result", "success");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return JSONArray.toJSON(resultMap);
	}
	
	@RequestMapping("/appinfomodifysave")
	public String Appinfomodifysave(AppInfo appInfo,HttpServletRequest req,HttpSession session,@RequestParam(value="attach",required= false) MultipartFile attach) {
		String logoPicPath = null;
		String logoLocPath = null;
		String APKName = appInfo.getAPKName();
		if(!attach.isEmpty()) {
			String path = req.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadFiles");
			logger.info("uploadFile path: " + path);
			String oldFileName = attach.getOriginalFilename();//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			int filesize = 500000;
			if(attach.getSize()>filesize) {
				return "redirect:/sys/app/appinfomodify?id="+appInfo.getId()+"&error=error4";
			}else if(prefix.equalsIgnoreCase("jpg")||prefix.equalsIgnoreCase("png")||prefix.equalsIgnoreCase("jepg")||prefix.equalsIgnoreCase("pneg")) {
				String fileName = APKName+".jpg";//上传LOGO图片命名:apk名称.apk
				File targetFile = new File(path,fileName);
				if(!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/sys/app/appinfomodify?id="+appInfo.getId()+"&error=error2";
				}
				logoPicPath = req.getContextPath()+"/statics/uploadfiles/"+fileName;
				logoLocPath = path+File.separator+fileName;
			}else {
				return "redirect:/sys/app/appinfomodify?id="+appInfo.getId()+"&error=error3";
			}
		}
		appInfo.setModifyBy(((User)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setModifyDate(new Date());
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setLogoPicPath(logoPicPath);
		try {
			if(infoService.modify(appInfo)) {
				return "redirect:/sys/app/list";
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "developer/appinfomodify";
	}
	
	//@PathVariable绑定URI模板变量值
	@RequestMapping(value="/appview/{id}",method=RequestMethod.GET)
	public String view(@PathVariable String id,Model model){
		AppInfo appInfo = null;
		List<AppVersion> appVersionList = null;
		try {
			appInfo = infoService.getInfo(Integer.parseInt(id),null);
			appVersionList = appVersionService.getAppVersionList(Integer.parseInt(id));
		}catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("appVersionList", appVersionList);
		model.addAttribute(appInfo);
		return "developer/appinfoview";
	}
	
	@RequestMapping("/appversionadd")
	public String Appversionadd(@RequestParam("id") int id,Model model) {
		AppInfo appInfo = null;
		List<AppVersion> appVersionsList = null;
		try {
			appInfo = infoService.getInfo(id, null);
			appVersionsList = appVersionService.getAppVersionList(id);
		}catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("appVersionList", appVersionsList);
		model.addAttribute(appInfo);
		return "developer/appversionadd";
	}
	
	@RequestMapping("/addversionsave")
	public String Addversionsave(AppVersion appVersion,@RequestParam("appId") String appid,@RequestParam(value="a_downloadLink",required= false) MultipartFile attach,HttpSession session,HttpServletRequest req) {
		String apkLocPath = null;
		String apkFileName = null;
		String downloadLink = null;
		if(!attach.isEmpty()) {
			String path = req.getServletContext().getRealPath("statics"+File.separator+"uploadFiles");
			String OldAPkName = attach.getOriginalFilename();
			String prefix = FilenameUtils.getExtension(OldAPkName);
			if(prefix.equalsIgnoreCase("apk")) {
				String apkName = null;
				try {
					apkName = infoService.getInfo(Integer.parseInt(appid), null).getAPKName();
				}catch (Exception e) {
					e.printStackTrace();
				}
				if(apkName == null || "".equals(apkName)){
					 return "redirect:/sys/app/appversionadd?id="+appVersion.getAppId()
							 +"&error=error1";
				}
				apkFileName = apkName+"-"+appVersion.getVersionNo()+".apk";
				File file = new File(path,apkFileName);
				if(!file.exists()) {
					file.mkdirs();
				}
				try {
					attach.transferTo(file);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/sys/app/appversionadd?id="+appVersion.getAppId()
					 +"&error=error2";
				}
				downloadLink = req.getContextPath()+"/statics/uploadfiles/"+apkFileName;
				apkLocPath = path+File.separator+apkFileName;
			}else {
				return "redirect:/sys/app/appversionadd?id="+appVersion.getAppId()
				 +"&error=error3";
			}
		}
		appVersion.setCreatedBy(((User)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appVersion.setCreationDate(new Date());
		appVersion.setDownloadLink(downloadLink);
		appVersion.setApkFileName(apkFileName);
		appVersion.setApkLocPath(apkLocPath);
		try {
			if(appVersionService.AddVersion(appVersion)) {
				return "redirect:/sys/app/list";
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/sys/app/appversionadd?id="+appVersion.getAppId();
	}
	
	@RequestMapping("appversionmodify")
	public String Appversionmodify(HttpServletRequest req,@RequestParam("vid") String versionid,@RequestParam("aid") String appinfoid) {
		List<AppVersion> vlist = null;
		AppVersion appVersion = null;
		try {
			vlist = appVersionService.getAppVersionList(Integer.parseInt(appinfoid));
			appVersion = appVersionService.AppVersionById(Integer.parseInt(appinfoid),Integer.parseInt(versionid));
		} catch (Exception e) {
			e.printStackTrace();
		}
		req.setAttribute("appVersion",appVersion);
		req.setAttribute("appVersionList", vlist);
		return "developer/appversionmodify";
	}
	
	@RequestMapping("appversionmodifysave")
	public String Appversionmodifysave(AppVersion appVersion,@RequestParam("appId") String appid,@RequestParam(value="attach",required= false) MultipartFile attach,HttpSession session,HttpServletRequest req) {
		String apkLocPath = null;
		String apkFileName = null;
		String downloadLink = null;
		if(!attach.isEmpty()) {
			String path = req.getServletContext().getRealPath("statics"+File.separator+"uploadFiles");
			String OldAPkName = attach.getOriginalFilename();
			String prefix = FilenameUtils.getExtension(OldAPkName);
			if(prefix.equalsIgnoreCase("apk")) {
				String apkName = null;
				try {
					apkName = infoService.getInfo(Integer.parseInt(appid), null).getAPKName();
				}catch (Exception e) {
					e.printStackTrace();
				}
				if(apkName == null || "".equals(apkName)){
					 return "redirect:/sys/app/appversionmodify?vid="+appVersion.getId()
					 +"&aid="+appid+"&error=error1";
				}
				apkFileName = apkName+"-"+appVersion.getVersionNo()+".apk";
				File file = new File(path,apkFileName);
				if(file.exists()) {
					file.delete();
				}
				if(!file.exists()) {
					file.mkdirs();
				}
				try {
					attach.transferTo(file);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/sys/app/appversionmodify?vid="+appVersion.getId()
					 +"&aid="+appid+"&error=error2";
				}
				downloadLink = req.getContextPath()+"/statics/uploadfiles/"+apkFileName;
				apkLocPath = path+File.separator+apkFileName;
			}else {
				return "redirect:/sys/app/appversionmodify?vid="+appVersion.getId()
				 +"&aid="+appid+"&error=error3";
			}
		}
		appVersion.setCreatedBy(((User)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appVersion.setCreationDate(new Date());
		appVersion.setDownloadLink(downloadLink);
		appVersion.setApkFileName(apkFileName);
		appVersion.setApkLocPath(apkLocPath);
		try {
			if(appVersionService.modify(appVersion)) {
				return "redirect:/sys/app/list";
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/sys/app/appversionmodify?id="+appVersion.getAppId();
	}
	
	@RequestMapping(value="/{appid}/sale",method=RequestMethod.PUT)
	@ResponseBody
	public Object Sale(@PathVariable String appid,HttpSession session) {
		HashMap<String, String> resultMap = new HashMap<String,String>();
		Integer appIdInteger = 0;
		try {
			appIdInteger = Integer.parseInt(appid);
		} catch (Exception e) {
			appIdInteger = 0;
		}
		resultMap.put("errorCode", "0");
		resultMap.put("appId", appid);
		if(appIdInteger>0) {
			try {
				User user = (User)session.getAttribute(Constants.DEV_USER_SESSION);
				AppInfo appInfo = new AppInfo();
				appInfo.setId(appIdInteger);
				appInfo.setModifyBy(user.getId());
				if(infoService.appsysUpdateSaleStatusByAppId(appInfo)) {
					resultMap.put("resultMsg", "success");
				}else{
					resultMap.put("resultMsg", "success");
				}
			} catch (Exception e) {
				resultMap.put("errorCode", "exception000001");
			}
		}else {
			resultMap.put("errorCode", "param000001");
		}
		return JSONArray.toJSON(resultMap);
	}
}
