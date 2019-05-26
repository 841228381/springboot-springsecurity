/**
 * 
 */
package com.imooc.security.browser.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author xzp
 * 自定义异常处理器
 *
 */
public class ValidateCodeException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7285211528095468156L;

	public ValidateCodeException(String msg) {
		super(msg);
	}

}
