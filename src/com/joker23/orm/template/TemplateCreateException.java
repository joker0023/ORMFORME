package com.joker23.orm.template;

/**
 * 生成模板 SQL 异常类
 */
public class TemplateCreateException extends RuntimeException {

	private static final long serialVersionUID = 7280448225662668614L;

	public TemplateCreateException() {
		super();
	}

	public TemplateCreateException(String message) {
		super(message);
	}
	
	public TemplateCreateException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TemplateCreateException(Throwable cause) {
		super(cause);
	}
}
