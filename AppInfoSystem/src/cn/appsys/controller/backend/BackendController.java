package cn.appsys.controller.backend;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.service.category.AppCategoryService;
import cn.appsys.service.dictionary.DataDictionaryService;
import cn.appsys.service.info.InfoService;
import cn.appsys.service.version.AppVersionService;
import cn.appsys.tools.Constants;
import cn.appsys.tools.PageSupport;

@Controller
@RequestMapping("/manager/backend")
public class BackendController {

	@Resource
	DataDictionaryService dataDicService;

	@Resource
	AppCategoryService appCateService;

	@Resource
	InfoService infoService;

	@Resource
	AppVersionService appVersionService;

	@RequestMapping("/app/list")
	public String List(HttpServletRequest req) {
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
		List<DataDictionary> statusList = dataDicService.getDataDictionaryList("APP_STATUS");
		List<DataDictionary> flatFormList = dataDicService.getDataDictionaryList("APP_FLATFORM");
		List<AppCategory> appCategorys = appCateService.getCategoryList();
		req.setAttribute("appInfoList", list);
		req.setAttribute("statusList", statusList);
		req.setAttribute("flatFormList", flatFormList);
		req.setAttribute("categoryLevel1List", appCategorys);
		req.setAttribute("queryCategoryLevel1",info.getCategoryLevel1());
		if(info.getCategoryLevel1()>0) {
			List<AppCategory> appCategorys2 = appCateService.getCategoryListById(info.getCategoryLevel1());
			req.setAttribute("categoryLevel2List",appCategorys2);
		}
		req.setAttribute("queryCategoryLevel2",info.getCategoryLevel2());
		if(info.getCategoryLevel2()>0) {
			List<AppCategory> appCategorys3 = appCateService.getCategoryListById(info.getCategoryLevel2());
			req.setAttribute("categoryLevel3List",appCategorys3);
		}
		req.setAttribute("queryCategoryLevel3",info.getCategoryLevel3());
		req.setAttribute("queryStatus", info.getStatus());
		req.setAttribute("queryFlatformId", info.getFlatformId());
		req.setAttribute("pages", pages);
		return "backend/applist";
	}

	@RequestMapping("/app/check")
	public String Check(@RequestParam("aid") String aid,@RequestParam("vid") String vid,HttpServletRequest req) {
		AppInfo appInfo = null;
		AppVersion appVersion = null;
		if(aid!=null) {
			appInfo = infoService.getInfo(Integer.parseInt(aid), null);
		}
		if(vid!=null) {
			appVersion = appVersionService.AppVersionById(0, Integer.parseInt(vid));
		}
		req.setAttribute("appInfo",appInfo);
		req.setAttribute("appVersion", appVersion);
		return "backend/appcheck";
	}

	@RequestMapping(value="/app/checksave",method=RequestMethod.POST)
	public String checksave(AppInfo appInfo){
		if(infoService.getInfoCountById(appInfo.getId(), null)>0) {
			if(infoService.appInfocheck(appInfo)) {
				return "redirect:/manager/backend/app/list";
			}
		}
		return "/AppInfoSystem/error";
	}
}
