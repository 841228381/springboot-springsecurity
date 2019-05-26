/**
 * 
 */
package com.imooc.security.browser.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.imooc.security.browser.properties.SecurityProperties;
import com.imooc.security.browser.support.SimpleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author xzp
 *
 */
@Controller
public class BrowserSecurityController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	//请求缓存里面拿请求
	//是springSecurity会将请求放入session
	//此类可以将请求从session中拿出来
	private RequestCache requestCache = new HttpSessionRequestCache();

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	@Autowired
	private SecurityProperties securityProperties;
	/**
	 * 当需要身份认证时，跳转到这里
	 * 是html跳转到登陆页面，不是就报错
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/authentication/require")
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (savedRequest != null) {
			String targetUrl = savedRequest.getRedirectUrl();
			logger.info("引发跳转的请求是:" + targetUrl);
			if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
				logger.info(request.getContextPath() + securityProperties.getBrowser().getLoginPage());
//				response.sendRedirect(request.getContextPath() + securityProperties.getBrowser().getLoginPage());
				redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
			}
		}

		return new SimpleResponse("访问的服务需要身份认证，请引导用户到登录页");
	}

	@RequestMapping("/authentication/successUrl")
	public ModelAndView successUrl() {
		ModelAndView modelAndView = new ModelAndView("/index");
		return modelAndView;
	}


}
