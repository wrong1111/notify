package com.game.utils.common.exception.utils;

import com.game.utils.common.exception.ActionException;
import com.game.utils.common.exception.BaseException;
import com.game.utils.common.exception.DaoException;
import com.game.utils.common.exception.DataException;
import com.game.utils.common.exception.ServiceException;

/**异常处理类，负责抛出各类异常，获取异常信息。
 * 
 * @author XiaYong
 *
 */

public class ExceptionHandle {
    /**
     * 基础异常
     */
    public static String BASE_ERROR = "ERROR.BASE";

    /**
     * 数据访问模块异常
     */
    public static String DAO_ERROR = "ERROR.DAO";

    /**
     * 数据访问模块查询异常
     */
    public static String DAO_ERROR_001 = "ERROR.DAO001";

    /**
     * 数据访问模块更新异常
     */
    public static String DAO_ERROR_002 = "ERROR.DAO002";

    /**
     * 数据访问模块删除异常
     */
    public static String DAO_ERROR_003 = "ERROR.DAO003";

    /**
     * 数据访问模块执行存储过程异常
     */
    public static String DAO_ERROR_004 = "ERROR.DAO004";

    /**
     * 业务服务模块异常
     */
    public static String SERVICE_ERROR = "ERROR.SERVICE";

    /**
     * 业务服务模块查询异常
     */
    public static String SERVICE_ERROR_001 = "ERROR.SERVICE001";

    /**
     * 业务服务模块更新异常
     */
    public static String SERVICE_ERROR_002 = "ERROR.SERVICE002";

    /**
     * 业务服务模块删除异常
     */
    public static String SERVICE_ERROR_003 = "ERROR.SERVICE003";

    /**
     * 业务服务模块执行存储过程异常
     */
    public static String SERVICE_ERROR_004 = "ERROR.SERVICE004";

    /**
     * 查询用户列表异常
     */
    public static String SERVICE_ERROR_005 = "ERROR.SERVICE005";

    /**
     * 保存用户异常
     */
    public static String SERVICE_ERROR_006 = "ERROR.SERVICE006";

    /**
     * 数据异常
     */
    public static String DATA_ERROR = "ERROR.DATA";

    /**
     * 页面控制模块异常
     */
    public static String ACTION_ERROR = "ERROR.ACTION";

    /**
     * 取不到计算精度的实现类
     */
    public static String SCALE_CALCULATE = "ERROR.SCALE.CALCULATE";

    //private static final MyLog log = MyLog.getLogger(ExceptionHandle.class);

    /**
     * 根据抛出BaseException
     * 
     * @param e
     *            捕获的异常
     * @param errorCode
     *            异常编码
     * @param classAndMethod
     *            捕获到异常的类名和方法名
     * @throws BaseException
     */
    public static void throwBaseException(Exception e, String errorCode, String classAndMethod)
        throws BaseException {
        //log.error(errorCode + ":" + classAndMethod);
        if (e.getCause() != null) {
            //log.error(e.getCause().getMessage());
        } else if (e.getMessage() != null) {
            //log.error(e.getMessage());
        }
        // 打印异常堆栈
        //log.error(e.getMessage(),e);
        // 抛出新封装的异常
        if (errorCode != null) {
            throw new BaseException(errorCode);
        } else {
            throw new BaseException(BASE_ERROR);
        }
    }

    /**
     * 根据抛出DaoException
     * 
     * @param e
     *            捕获的异常
     * @param errorCode
     *            异常编码
     * @param classAndMethod
     *            捕获到异常的类名和方法名
     * @throws DaoException
     */
    public static void throwDaoException(Exception e, String errorCode, String classAndMethod)
        throws DaoException {
        //log.error(errorCode + ":" + classAndMethod);
        if (e.getCause() != null) {
            //log.error(e.getCause().getMessage());
        } else if (e.getMessage() != null) {
            //log.error(e.getMessage());
        }
        // 打印异常堆栈
        //log.error(e.getMessage(),e);
        // 抛出新封装的异常
        if (errorCode != null) {
            throw new DaoException(errorCode);
        } else {
            throw new DaoException(DAO_ERROR);
        }
    }

    /**
     * 根据抛出ServiceException
     * 
     * @param e
     *            捕获的异常
     * @param errorCode
     *            异常编码
     * @param classAndMethod
     *            捕获到异常的类名和方法名
     * @throws ServiceException
     */
    public static void throwServiceException(Exception e, String errorCode, String classAndMethod)
        throws ServiceException {
        //log.error(errorCode + ":" + classAndMethod);
        if (e.getCause() != null) {
            //log.error(e.getCause().getMessage());
        } else if (e.getMessage() != null) {
            //log.error(e.getMessage());
        }
        // 打印异常堆栈
        //log.error(e.getMessage(),e);
        // 抛出新封装的异常
        if (errorCode != null) {
            throw new ServiceException(errorCode);
        } else {
            throw new ServiceException(SERVICE_ERROR);
        }
    }

    /**
     * 根据抛出DataException
     * 
     * @param e
     *            捕获的异常
     * @param errorCode
     *            异常编码
     * @param classAndMethod
     *            捕获到异常的类名和方法名
     * @throws DataException
     */
    public static void throwDataException(Exception e, String errorCode, String classAndMethod)
        throws DataException {
        //log.error(errorCode + ":" + classAndMethod);
        if (e.getCause() != null) {
            //log.error(e.getCause().getMessage());
        } else if (e.getMessage() != null) {
            //log.error(e.getMessage());
        }
        // 打印异常堆栈
        //log.error(e.getMessage(),e);
        // 抛出新封装的异常
        if (errorCode != null) {
            throw new DataException(errorCode);
        } else {
            throw new DataException(DATA_ERROR);
        }
    }

    /**
     * 根据抛出ActionException
     * 
     * @param e
     *            捕获的异常
     * @param errorCode
     *            异常编码
     * @param classAndMethod
     *            捕获到异常的类名和方法名
     */
    public static ActionException getActionException(Exception e, String errorCode,
        String classAndMethod) {
        //log.error(errorCode + ":" + classAndMethod);
        if (e.getCause() != null) {
            //log.error(e.getCause().getMessage());
        } else if (e.getMessage() != null) {
            //log.error(e.getMessage());
        }
        // 打印异常堆栈
        //log.error(e.getMessage(),e);
        // 抛出新封装的异常
        if (errorCode != null) {
            return new ActionException(errorCode);
        } else {
            return new ActionException(ACTION_ERROR);
        }
    }

}
