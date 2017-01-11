package com.systop.amol.user.agent.webapp;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alipay.util.httpClient.HttpResponse;
import com.systop.amol.user.agent.service.AgentManager;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.util.WebMockUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

@SuppressWarnings({ "serial", "unused", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LgoinAction extends DefaultCrudAction<User, AgentManager> implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("过滤器开始运行------");
		// TODO Auto-generated method stub
		HttpServletRequest servletRequest=(HttpServletRequest)request;
		HttpServletResponse servletResponse=(HttpServletResponse)response;
		HttpSession session=servletRequest.getSession();
		String reQuestpath=servletRequest.getRequestURI();
		System.out.println("请求路径为---------"+reQuestpath);
		User sessionUser = (User)session.getAttribute("userInSession");
		System.out.println("---------------session"+sessionUser);
		servletRequest.setCharacterEncoding("UTF-8");
		if(reQuestpath.indexOf("loginDEF.jsp")>-1||reQuestpath.indexOf("phone")>-1){
			chain.doFilter(request,response);
			return;
			}
		if(sessionUser==null){
			System.out.println("---------当前登录用户不存在");
			String path = servletRequest.getContextPath();
		    String basePath = servletRequest.getScheme() + "://"
		            + servletRequest.getServerName() + ":" + servletRequest.getServerPort()
		            + path + "/";
		    System.out.println("-------转发到的路径-------"+basePath+"loginDEF.jsp");
			servletResponse.sendRedirect(basePath+"loginDEF.jsp");

		}
		else{
			chain.doFilter(request,response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("初始化过滤器...............");
	}
	
	
	
	

}
