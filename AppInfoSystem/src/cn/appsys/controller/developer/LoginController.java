package cn.appsys.controller.developer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.User;
import cn.appsys.service.user.UserService;
import cn.appsys.tools.Constants;

@Controller
public class LoginController {
	
	@Resource
	UserService userService;
	
	@RequestMapping("dologin")
	public String login(@RequestParam String username,@RequestParam String userpwd,HttpSession session,HttpServletRequest req) {
		User user = userService.login(username, userpwd);
		if(user!=null) {
			session.setAttribute(Constants.DEV_USER_SESSION, user);
			return "developer/main";
		}else {
			req.setAttribute("error", "用户名或密码错误!");
			return "devlogin";
		}
	}
	
	@RequestMapping("login")
	public String login() {
		return "devlogin";
	}
	
	@RequestMapping("logout")
	public String LoginOut(HttpSession session) {
		if(session.getAttribute(Constants.DEV_USER_SESSION)!=null)
		session.removeAttribute(Constants.DEV_USER_SESSION);
		return "redirect:/index.jsp";
	}
}
