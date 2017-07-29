package com.game.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CookieUtil {
	public static final Log logger = LogFactory.getLog(CookieUtil.class);

	/**
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request,
			String cookieName) {
		return CookieUtil.getCookieValue(request, cookieName, false);
	}

	/**
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request,
			String cookieName, boolean isDecoder) {
		Cookie cookieList[] = request.getCookies();
		if (cookieList == null || cookieName == null)
			return null;
		String retValue = null;
		try {
			for (int i = 0; i < cookieList.length; i++) {
				if (cookieList[i].getName().equals(cookieName)) {
					if (isDecoder) {
						retValue = URLDecoder.decode(cookieList[i].getValue(),
								"utf-8");
					} else {
						retValue = cookieList[i].getValue();
					}
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}

		return retValue;
	}

	/**
	 * setCookie
	 */
	public static void setCookie(HttpServletResponse response,
			String cookieName, String cookieValue) {
		if (cookieValue == null) {
			cookieValue = "";
		}
		setCookie(null, response, cookieName, cookieValue);
	}

	/**
	 * setCookie
	 */
	public static void setCookie(HttpServletRequest request,
			HttpServletResponse response, String cookieName, String cookieValue) {
		setCookie(request, response, cookieName, cookieValue, -1);
	}

	/**
	 * 设置域名
	 * setCookie domain
	 */
	public static void setCookie(HttpServletRequest request,
			HttpServletResponse response, String cookieName, String cookieValue, String domain) {
		setCookie(request, response, cookieName, cookieValue, -1, domain);
	}

	/**
	 * setCookie
	 */
	public static void setCookie(HttpServletRequest request,
			HttpServletResponse response, String cookieName,
			String cookieValue, int cookieMaxage) {
		setCookie(request, response, cookieName, cookieValue, cookieMaxage,
				false);
	}


	/**
	 * setCookie
	 */
	public static void setCookie(HttpServletRequest request,
			HttpServletResponse response, String cookieName,
			String cookieValue, int cookieMaxage, String domain) {
		setCookie(request, response, cookieName, cookieValue, cookieMaxage,
				false, domain);
	}

	/**
	 * setCookie
	 */
	public static void setCookie(HttpServletRequest request,
			HttpServletResponse response, String cookieName,
			String cookieValue, boolean isEncode) {
		setCookie(request, response, cookieName, cookieValue, -1, isEncode);
	}

	/**
	 * setCookie
	 */
	public static void setCookie(HttpServletRequest request,
			HttpServletResponse response, String cookieName,
			String cookieValue, int cookieMaxage, boolean isEncode) {
		doSetCookie(request, response, cookieName, cookieValue, cookieMaxage,
				isEncode);
	}

	/**
	 * setCookie
	 */
	public static void setCookie(HttpServletRequest request,
			HttpServletResponse response, String cookieName,
			String cookieValue, int cookieMaxage, boolean isEncode, String domain) {
		doSetCookie(request, response, cookieName, cookieValue, cookieMaxage,
				isEncode, domain);
	}

	/**
	 * deleteCookie
	 */
	public static void deleteCookie(HttpServletRequest request,
			HttpServletResponse response, String cookieName) {
		doSetCookie(request, response, cookieName, "", -1, false);
	}

	/**
	 * 
	 * @param response
	 * @param cookieName
	 */
	public static void deleteCookie(HttpServletResponse response,
			String cookieName) {
		// doSetCookie(null, response, cookieName, "", -1, false);
		doSetCookie(null, response, cookieName, null, 0, false);
	}

	/**
	 * 
	 * 
	 */
	private static final void doSetCookie(HttpServletRequest request,
			HttpServletResponse response, String cookieName,
			String cookieValue, int cookieMaxage, boolean isEncode) {
		try {
			if (cookieValue != null && cookieValue.equals("")) {
				cookieValue = null;
			} else if (isEncode) {
				cookieValue = URLEncoder.encode(cookieValue, "utf-8");
			}
			Cookie cookie = new Cookie(cookieName, cookieValue);
			if (cookieMaxage > 0)
				cookie.setMaxAge(cookieMaxage);
			else {
				if (cookieValue == null)
					cookie.setMaxAge(0);
			}
			if (null != request && request.getRequestURL() != null) {
				String domainName = getDomainName(request);
				if (domainName == null) {
					domainName = ".ecp888.com";
				}
				cookie.setDomain(domainName);
			}
			cookie.setPath("/");
			if (isEncode)
				response.setCharacterEncoding("utf-8");
			response.addCookie(cookie);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * 
	 */
	private static final void doSetCookie(HttpServletRequest request,
			HttpServletResponse response, String cookieName,
			String cookieValue, int cookieMaxage, boolean isEncode, String domain) {
		try {
			if (cookieValue != null && cookieValue.equals("")) {
				cookieValue = null;
			} else if (isEncode) {
				cookieValue = URLEncoder.encode(cookieValue, "utf-8");
			}
			Cookie cookie = new Cookie(cookieName, cookieValue);
			if (cookieMaxage > 0)
				cookie.setMaxAge(cookieMaxage);
			else {
				if (cookieValue == null)
					cookie.setMaxAge(0);
			}
			if (StringUtils.isBlank(domain)){
				if (null != request && request.getRequestURL() != null) {
					String domainName = getDomainName(request);
					if (domainName == null) {
						domainName = ".ecp888.com";
					}
					cookie.setDomain(domainName);
				}
			}else{
				cookie.setDomain(domain);
			}
			cookie.setPath("/");
			if (isEncode)
				response.setCharacterEncoding("utf-8");
			response.addCookie(cookie);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * @param request
	 * @return
	 */
	private static final String getDomainName(HttpServletRequest request) {
		String serverName = request.getRequestURL().toString();
		return getArea(serverName);
	}

	@SuppressWarnings("unused")
	private final static String getDomainNameEx(HttpServletRequest request) {
		String serverName = request.getRequestURL().toString();
		String domainName = null;
		if (serverName == null || serverName.equals("")) {
			domainName = "";
		} else {
			// 修改了要写Cookie的域 除去http://www 到端口号前
			domainName = getArea(serverName);
		}
		return domainName;
	}

	/*** 根据域名获取当前要写COOKIE的域 **/
	public static String getArea(String servletURL) {
		try {
			String str = servletURL.split("//")[1];
			String t = str.substring(str.indexOf(str.split("\\.")[0])
					+ str.split("\\.")[0].length());
			String[] d = t.split("\\/");
			if (d.length > 0){
				String domain = d[0].indexOf(":") == -1 ? d[0] : d[0].split(":")[0];
				if (domain.indexOf(".game.")!=-1){
					//判断是否为.ecp888.com的域名
					return domain.substring(domain.indexOf(".game"), domain.length());
				}else{
					return domain;
				}
			}else{
				return t;
			}
		} catch (Exception e) {
			if (servletURL.indexOf(".cn") > 0) {
				return ".game.com.cn";
			} else {
				return ".game.com";
			}
		}
	}

	/**
	 * 获取用户ＩＤ
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  static String getUserId(HttpServletRequest request) {
		Object cookieValue = getCookieValue(request, Constants.LOGIN_TOKEN_CODE);
		if (cookieValue == null || cookieValue.equals("")) {
			return "-1";
		} else {
			Map<String, String> map = (Map<String, String>) MemcacheUtil
					.get(cookieValue + "_base");
			if (map != null) {
				String id = map.get("_USERID"); // userID
				if (!StringUtils.isBlank(id)) {
					return id;
				}
			}
			return "-1";
		}
	}

	/**
	 * 获取用户名昵称[****2133]
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static String getUserName(HttpServletRequest request) {
		Object cookieValue = getCookieValue(request, Constants.LOGIN_TOKEN_CODE);
		if (cookieValue == null || cookieValue.equals("")) {
			return "-1";
		} else {
			Map<String, String> map = (Map<String, String>) MemcacheUtil
					.get(cookieValue + "_base");
			if (map != null) {
				String s1 = map.get("_USERNAME"); // username 【用户名/昵称】
				if (!StringUtils.isBlank(s1)) {
					return s1;
				}
				s1 = map.get("_LOGINNAME"); // loginName【登陆名】
				if (!StringUtils.isBlank(s1)) {
					return s1;
				}
			}
			return "-1";

		}
	}

	public static void main(String[] args) {
		String url = "http://passport.ecp888.net";
		System.out.println(getArea(url));
	}

}
