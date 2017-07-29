package com.game.utils.common.exception;

/**业务服务模块异常类
 * 
 * @author XiaYong
 *
 */

public class ServiceException extends BaseException {

    private static final long serialVersionUID = -7086855509188011754L;

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }
    
    public ServiceException(String message,Throwable e) {
    	super(message,e);
    }
}
