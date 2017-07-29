package com.game.utils.common.exception;

/**基础异常类
 * 
 * @author XiaYong
 *
 */

public class BaseException extends Exception {

    private static final long serialVersionUID = 3358888911029354719L;

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }
    
    public BaseException(String message,Throwable e) {
        super(message,e);
    }

}
