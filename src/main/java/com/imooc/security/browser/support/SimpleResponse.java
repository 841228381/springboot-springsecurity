/**
 * 
 */
package com.imooc.security.browser.support;

/**
 * @author xzp
 * 定义数据返回格式
 *
 */
public class SimpleResponse {
	
	public SimpleResponse(Object content){
		this.content = content;
	}
	
	private Object content;

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
	
}
