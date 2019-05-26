package com.imooc.security.browser.config;

import com.imooc.security.browser.authentication.ImoocAuthenctiationFailureHandler;
import com.imooc.security.browser.authentication.ImoocAuthenticationSuccessHandler;
import com.imooc.security.browser.dto.MyUser;
import com.imooc.security.browser.filter.ValidateCodeFilter;
import com.imooc.security.browser.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;


/**
 * Created by XZP on 2019/5/25.
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter{
    /**
     * 读取配置文件数据
     */
    @Autowired
    private SecurityProperties securityProperties;
    /**
     * 数据源
     */
    @Autowired
    private DataSource dataSource;
    /**
     * 自定义查询用户信息和权限
     */
    @Autowired
    private UserDetailsService userDetailsService;
    /**
     * 自定义登陆成功的操作
     */
    @Autowired
    private ImoocAuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

    /**
     * 自定义登陆失败操作
     */
    @Autowired
    private ImoocAuthenctiationFailureHandler imoocAuthenctiationFailureHandler;

    //加此bean， security会自动加密对比密码，
    // 可以实现PasswordEncoder的encode来自定义MD5加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //内存用户
   /* @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("yiibai").password("123456").roles("USER");
        auth.inMemoryAuthentication().withUser("admin").password("123456").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("dba").password("123456").roles("ADMIN","DBA");
    }*/
    /**
     * 配置记住我获取数据库token的bean
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        //启动的时候创建token表
//		tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    @Bean
    public UserDetails userDetails() {
        return new MyUser();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //验证码过滤器。放在密码之前验证
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setImoocAuthenctiationFailureHandler(imoocAuthenctiationFailureHandler);
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/authentication/require")//没登陆就先跳转到controller
                .loginProcessingUrl("/authentication/form")//form表单提交路径
                .successForwardUrl("/authentication/successUrl")//登陆成功调转到controller,再通过controller调转到指定页面
//                .successHandler(imoocAuthenticationSuccessHandler)//登陆成功调用此方法处理自己的业务逻辑
                .failureHandler(imoocAuthenctiationFailureHandler)
                .and()
                //记住我配置
                //1、配置数据库操作实现
                //2、配置token过期时间秒数
                //3、配置根据用户名查询信息并将信息放入securityContext中的实现类
                .rememberMe()
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                    .userDetailsService(userDetailsService)
                .and()
                .authorizeRequests()
                .antMatchers("/authentication/require"
                    ,securityProperties.getBrowser().getLoginPage()
                    ,"/code/image"
                ).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable();//取消掉token验证
    }
}
