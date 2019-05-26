/**
 * 
 */
package com.imooc.security.browser.properties;

/**
 * @author xzp
 * 用此枚举来动态判定登陆成功和失败事跳转还是返回json字符串
 *
 */
public enum LoginResponseType {
	
	/**
	 * 跳转
	 */
	REDIRECT,
	
	/**
	 * 返回json
	 */
	JSON

}
