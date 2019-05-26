package com.imooc.security.browser.config;

import com.google.common.collect.Lists;
import com.imooc.security.browser.dto.MyResource;
import com.imooc.security.browser.dto.MyUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by XZP on 2019/5/25.
 */
@Component
public class MyUserDetailsService implements UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        logger.info("表单登录用户名:" + s);
        return buildUser(s);
    }

    private UserDetails buildUser(String userId) {
        // 根据用户名查找用户信息
        //根据查找到的用户信息判断用户是否被冻结
        //此处加密应该是在新增用户的时候加密的
        String password = passwordEncoder.encode("123456");
        logger.info("数据库密码是:"+password);
        /**
         * 注意：在页面使用springsecurity标签的时候，这儿封装权限的时候必须加ROLE_前缀
         * 否则页面识别不到权限，直接设置内存用户就不需要
         */
//        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
//        auths.add(authority);
//        return new User(userId, password,
//                true, true, true, true,auths);
 /*       return new User(userId, password,
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN"));*/

        List<MyResource> myResourceList = Lists.newArrayList();
        MyResource myResource = new MyResource();
        myResource.setUrl("/admin/url");
        myResourceList.add(myResource);
        List<String> roles = Lists.newArrayList();
        roles.add("ADMIN");

        Set<GrantedAuthority> auths = new HashSet<GrantedAuthority>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        auths.add(authority);

        MyUser myUserDetails = new MyUser(userId,password,true,true,true,true,auths);
        return myUserDetails;
    }
}
