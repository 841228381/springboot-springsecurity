package com.imooc.security.browser.tag;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.Arrays;
import java.util.List;

/**
 * Created by XZP on 2019/5/26.
 * 自定义标签
 * 就可以使用springsecurity的标签来动态做按钮的显示
 * 教程地址：https://blog.csdn.net/zzhou1990/article/details/52129651
 */
@Component
public class AuthorizeTag extends BodyTagSupport {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String buttonUrl;
    private String currentUser;


    public String getButtonUrl() {
        return buttonUrl;
    }


    public void setButtonUrl(String buttonUrl) {
        this.buttonUrl = buttonUrl;
    }


    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }


    @Override
    public int doStartTag(){
        /**
         * 此处根据springsecurity中登陆成功的用户数据查询
         * 所有的权限list,然后跟页面配置的路径做对比。看是否有路径权限
         */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> list = Arrays.asList("/delete","/add","/update");
        if (list != null && list.contains(buttonUrl)) {
            return EVAL_BODY_INCLUDE;
        }
        return this.SKIP_BODY;
    }
}
