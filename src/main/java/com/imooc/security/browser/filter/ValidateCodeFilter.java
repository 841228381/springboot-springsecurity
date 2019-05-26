package com.imooc.security.browser.filter;

import com.imooc.security.browser.authentication.ImoocAuthenctiationFailureHandler;
import com.imooc.security.browser.controller.ValidateCodeController;
import com.imooc.security.browser.exception.ValidateCodeException;
import com.imooc.security.browser.validate.ImageCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by XZP on 2019/5/25.
 * OncePerRequestFilter顾名思义，他能够确保在一次请求只通过一次filter，而不需要重复执行
 */
public class ValidateCodeFilter extends OncePerRequestFilter {
    /**
     * 用处理登陆出错的来处理验证码出错
     */
    private ImoocAuthenctiationFailureHandler imoocAuthenctiationFailureHandler;
    /**
     * 操作session的工具类
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.equals("/authentication/form",httpServletRequest.getRequestURI())
                && StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(),"post")) {
            try {
                validate(new ServletWebRequest(httpServletRequest));
            } catch (ValidateCodeException e) {
                imoocAuthenctiationFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private void validate(ServletWebRequest servletWebRequest) throws ServletException{
        ImageCode codeInSession = (ImageCode)sessionStrategy.getAttribute(servletWebRequest, ValidateCodeController.SESSION_KEY);
        String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(),"imageCode");

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeInSession.isExpried()) {
            sessionStrategy.removeAttribute(servletWebRequest, ValidateCodeController.SESSION_KEY);
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }

        sessionStrategy.removeAttribute(servletWebRequest, ValidateCodeController.SESSION_KEY);
    }

    public ImoocAuthenctiationFailureHandler getImoocAuthenctiationFailureHandler() {
        return imoocAuthenctiationFailureHandler;
    }

    public void setImoocAuthenctiationFailureHandler(ImoocAuthenctiationFailureHandler imoocAuthenctiationFailureHandler) {
        this.imoocAuthenctiationFailureHandler = imoocAuthenctiationFailureHandler;
    }
}
