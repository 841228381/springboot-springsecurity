/**
 * 
 */
package com.imooc.security.browser.properties;

/**
 * @author
 *此类获取aplication.yml中的配置，全系统都可以用
 */
public class BrowserProperties {

	private String loginPage = "/imooc-signIn.html";

	private LoginResponseType loginType = LoginResponseType.JSON;

	private int rememberMeSeconds = 3600;

	public String getLoginPage() {
		return loginPage;
	}

	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	public LoginResponseType getLoginType() {
		return loginType;
	}

	public void setLoginType(LoginResponseType loginType) {
		this.loginType = loginType;
	}

	public int getRememberMeSeconds() {
		return rememberMeSeconds;
	}

	public void setRememberMeSeconds(int rememberMeSeconds) {
		this.rememberMeSeconds = rememberMeSeconds;
	}

	@Override
	public String toString() {
		return "BrowserProperties{" +
				"loginPage='" + loginPage + '\'' +
				'}';
	}
}
