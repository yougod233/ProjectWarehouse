package cn.appsys.controller.backend;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.User;
import cn.appsys.service.backenduser.BackendUserService;
import cn.appsys.tools.Constants;

@Controller
public class UserLoginController {
	
	@Resource
	BackendUserService bUserService;
	
	@RequestMapping("/manager/login")
	public String login() {
		return "backendlogin";
	}
	
	@RequestMapping("/manager/backdologin")
	public String blogin(@RequestParam String userCode,@RequestParam String userPassword,HttpSession session,HttpServletRequest req) {
		BackendUser user = bUserService.login(userCode, userPassword);
		if(user!=null) {
			session.setAttribute(Constants.USER_SESSION, user);
			return "backend/main";
		}else {
			req.setAttribute("error", "用户名或密码错误!");
			return "redirect:/manager/login";
		}
	}
	
	@RequestMapping("/manager/logout")
	public String LogOut(HttpSession session) {
		if(session.getAttribute(Constants.USER_SESSION)!=null) {
			session.removeAttribute(Constants.USER_SESSION);
		}
		return "redirect:/index.jsp";
	}
}
