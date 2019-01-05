package com.soc.v4ward.log.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author NieYinjun
 */
public class ClientUtil {
	private static final String UNKNOWN = "unknown";
	/**
	 * 获取客户端真实ip
	 * @param request 请求
	 * @return 远端ip
	 */
	public static String getClientIp(HttpServletRequest request){
		String ip = request.getHeader("x-forwarded-for");
		if (ip==null||ip.length()==0||UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip==null||ip.length()==0||UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip==null||ip.length()==0||UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
