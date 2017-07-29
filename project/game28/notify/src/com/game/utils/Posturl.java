package com.game.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

@SuppressWarnings("unused")
public class Posturl {

	static int Timeout = 15000;
	static Logger logger = Logger.getLogger(Posturl.class);
	
	private static void getRequestconfig(HttpGet httpget,int second){
		 //配置请求的超时设置
		second = second>Timeout ? second : Timeout;
        RequestConfig requestConfig = RequestConfig.custom()  
                .setConnectionRequestTimeout(second)
                .setConnectTimeout(second)  
                .setSocketTimeout(second).build();
        httpget.setConfig(requestConfig);
	}
	private static void postRequestconfig(HttpPost httppost,int second){
		 //配置请求的超时设置
		second = second > Timeout ? second : Timeout;
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(second)
				.setConnectTimeout(second)  
				.setSocketTimeout(second).build();
       httppost.setConfig(requestConfig);
	}
	private static void postRequestconfig(HttpPost httppost){
		 //配置请求的超时设置
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(Timeout)
				.setConnectTimeout(Timeout)  
				.setSocketTimeout(Timeout).build();
        httppost.setConfig(requestConfig);
	}
	public static String getRequest(String url) {
		StringBuilder sb = new StringBuilder();
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet(url);   
        getRequestconfig(httpget,10000);
        
        try{
	        CloseableHttpResponse response = httpclient.execute(httpget);        
	        int statusOk = response.getStatusLine().getStatusCode();
	        if(logger.isInfoEnabled()){
	        	logger.info(url+"result-statusCode=>>"+statusOk);
	        }
	        if(statusOk == HttpStatus.SC_OK){
		        HttpEntity entity = response.getEntity();        
		        BufferedInputStream instream = new BufferedInputStream(entity.getContent()); 
		        byte[] chars = new byte[2048];
		        int len=0;
		        while((len=instream.read(chars))!=-1){
		        	sb.append(new String(chars,0,len));
		        }
	        }else{
	        	logger.error(url+"result->网络异常.."+statusOk);
	        }
	        if(logger.isInfoEnabled()){
	        	logger.info(url+"-result->"+sb.toString());
	        }
        }catch(Exception e){
        	e.printStackTrace();
        }finally {
        	httpget.releaseConnection();
        	try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return sb.toString();
	}
	/**
	 * @param url
	 * @param data
	 * @param encode
	 *            指定头编码
	 * @param second 指定超时
	 * @return
	 */
	public static String postRequest(String url, Map<String, Object> data,
			String encode,int second) {
		if(StringUtils.isBlank(encode)){
			encode = "utf8";
		}
		StringBuilder sb = new StringBuilder();
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(url);
		postRequestconfig(httppost,second);
        try{
        	if (data != null) {
        		List<NameValuePair> dataary = new ArrayList<NameValuePair>();
				for (String mapKey : data.keySet()) {
					// 填入各个表单域的值
					String vString = data.get(mapKey) == null ? "" : data.get(
							mapKey).toString();
					NameValuePair data1 = new BasicNameValuePair(mapKey, vString);
					dataary.add(data1);
				}
				// 将表单的值放入postMethod中
				 httppost.setEntity(new UrlEncodedFormEntity(dataary,encode));
			}
	        CloseableHttpResponse response = httpclient.execute(httppost);
	        int statusOk = response.getStatusLine().getStatusCode();
	        if(logger.isInfoEnabled()){
	        	logger.info(url+"result-statusCode=>>"+statusOk);
	        }
	        if(statusOk == HttpStatus.SC_OK){
	        	HttpEntity entity = response.getEntity();
	        	BufferedInputStream instream = new BufferedInputStream(entity.getContent()); 
		        byte[] chars = new byte[2048];
		        int len=0;
		        while((len=instream.read(chars))!=-1){
		        	sb.append(new String(chars,0,len,encode));
		        }
		        instream.close();
		        response.close();
	        }else{
	        	logger.error(url+"result->网络异常.."+statusOk);
	        }
	        if(logger.isInfoEnabled()){
	        	logger.info(url+"--result->"+sb.toString());
	        }
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	httppost.releaseConnection();
        	try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return sb.toString();
	}
	/**
	 * @param url
	 * @param data
	 * @param encode
	 *            指定头编码
	 * @return
	 */
	public static String postRequest(String url, Map<String, Object> data,
			String encode) {
		if(StringUtils.isBlank(encode)){
			encode = "utf8";
		}
		StringBuilder sb = new StringBuilder();
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(url);
		postRequestconfig(httppost);
        try{
        	if (data != null) {
        		List<NameValuePair> dataary = new ArrayList<NameValuePair>();
				for (String mapKey : data.keySet()) {
					// 填入各个表单域的值
					String vString = data.get(mapKey) == null ? "" : data.get(
							mapKey).toString();
					NameValuePair data1 = new BasicNameValuePair(mapKey, vString);
					dataary.add(data1);
				}
				// 将表单的值放入postMethod中
				 httppost.setEntity(new UrlEncodedFormEntity(dataary,encode));
			}
	        CloseableHttpResponse response = httpclient.execute(httppost);
	        int statusOk = response.getStatusLine().getStatusCode();
	        if(logger.isInfoEnabled()){
	        	logger.info(url+"result-statusCode=>>"+statusOk);
	        }
	        if(statusOk == HttpStatus.SC_OK){
	        	HttpEntity entity = response.getEntity();
	        	BufferedInputStream instream = new BufferedInputStream(entity.getContent()); 
		        byte[] chars = new byte[2048];
		        int len=0;
		        while((len=instream.read(chars))!=-1){
		        	sb.append(new String(chars,0,len,encode));
		        }
		        instream.close();
		        response.close();
	        }else{
	        	logger.error(url+"result->网络异常.."+statusOk);
	        }
	        if(logger.isInfoEnabled()){
	        	logger.info(url+"result->"+sb.toString());
	        }
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	httppost.releaseConnection();
        	try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return sb.toString();
	}
//
	public static String postRequest(String url, Map<String, Object> data) {
		  return postRequest(url, data, "utf8");
	}
	public static String postRequest(String url, Map<String, Object> data,int second) {
		  return postRequest(url, data, "utf8",second);
	}

	public static String postRequest(String url, Map<String, Object> data,
			Map<String, String> cookies, String domain,String encode) {
		if(StringUtils.isBlank(encode)){
			encode = "utf8";
		}
		StringBuilder sb = new StringBuilder();
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(url);
		postRequestconfig(httppost);
		
        try{
        	if (data != null) {
        		List<NameValuePair> dataary = new ArrayList<NameValuePair>();
				for (String mapKey : data.keySet()) {
					// 填入各个表单域的值
					String vString = data.get(mapKey) == null ? "" : data.get(
							mapKey).toString();
					NameValuePair data1 = new BasicNameValuePair(mapKey, vString);
					dataary.add(data1);
				}
				// 将表单的值放入postMethod中
				 httppost.setEntity(new UrlEncodedFormEntity(dataary,encode));
			}
        	if(cookies!=null && !cookies.isEmpty()){
        		String cookie = "";
				for (String cookieName : cookies.keySet()) {
					//Cookie c = new Cookie(domain, cookieName,cookies.get(cookieName), "/", -1, true);
					cookie = cookieName+"="+cookies.get(cookieName)+",";
				}
				cookie = "path=/";
				if(StringUtils.isNotBlank(domain)){
					cookie = cookie+",domain="+domain;
				}
//				if(cookie.endsWith(",")){
//					cookie = cookie.substring(0, cookie.length()-1);
//				}
				httppost.addHeader(new BasicHeader("Cookie",cookie)); 
        	}
	        CloseableHttpResponse response = httpclient.execute(httppost);
	        int statusOk = response.getStatusLine().getStatusCode();
	        if(logger.isInfoEnabled()){
	        	logger.info(url+"result-statusCode=>>"+statusOk);
	        }
	        if(statusOk == HttpStatus.SC_OK){
	        	HttpEntity entity = response.getEntity();
	        	BufferedInputStream instream = new BufferedInputStream(entity.getContent()); 
		        byte[] chars = new byte[2048];
		        int len=0;
		        while((len=instream.read(chars))!=-1){
		        	sb.append(new String(chars,0,len,encode));
		        }
	        }else{
	        	logger.error(url+"result->网络异常.."+statusOk);
	        }
	        if(logger.isInfoEnabled()){
	        	logger.info(url+"result->"+sb.toString());
	        }
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	httppost.releaseConnection();
        	try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return sb.toString();
		 
	}
	public static String postRequest(String url, Map<String, Object> data,
			Map<String, String> cookies,Map<String,String> header, String domain,String encode) {
		if(StringUtils.isBlank(encode)){
			encode = "utf8";
		}
		StringBuilder sb = new StringBuilder();
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(url);
		postRequestconfig(httppost);
		
        try{
        	if (data != null) {
        		List<NameValuePair> dataary = new ArrayList<NameValuePair>();
				for (String mapKey : data.keySet()) {
					// 填入各个表单域的值
					String vString = data.get(mapKey) == null ? "" : data.get(
							mapKey).toString();
					NameValuePair data1 = new BasicNameValuePair(mapKey, vString);
					dataary.add(data1);
				}
				// 将表单的值放入postMethod中
				 httppost.setEntity(new UrlEncodedFormEntity(dataary,encode));
			}
        	if(cookies!=null && !cookies.isEmpty()){
        		String cookie = "";
				for (String cookieName : cookies.keySet()) {
					//Cookie c = new Cookie(domain, cookieName,cookies.get(cookieName), "/", -1, true);
					cookie = cookieName+"="+cookies.get(cookieName)+",";
				}
				cookie = "path=/";
				if(StringUtils.isNotBlank(domain)){
					cookie = cookie+",domain="+domain;
				}
//				if(cookie.endsWith(",")){
//					cookie = cookie.substring(0, cookie.length()-1);
//				}
				httppost.addHeader(new BasicHeader("Cookie",cookie)); 
				if(header!=null && !header.isEmpty()){
					Iterator<String> it = header.keySet().iterator();
					while(it.hasNext()){
						String key = it.next();
						String dataHeader = header.get(key);
						httppost.addHeader(new BasicHeader(key,dataHeader));
					}
				}
        	}
	        CloseableHttpResponse response = httpclient.execute(httppost);
	        int statusOk = response.getStatusLine().getStatusCode();
	        if(logger.isInfoEnabled()){
	        	logger.info(url+"result-statusCode=>>"+statusOk);
	        }
	        if(statusOk == HttpStatus.SC_OK){
	        	HttpEntity entity = response.getEntity();
	        	BufferedInputStream instream = new BufferedInputStream(entity.getContent()); 
		        byte[] chars = new byte[2048];
		        int len=0;
		        while((len=instream.read(chars))!=-1){
		        	sb.append(new String(chars,0,len,encode));
		        }
	        }else{
	        	logger.error(url+"result->网络异常.."+statusOk);
	        }
	        if(logger.isInfoEnabled()){
	        	logger.info(url+"result->"+sb.toString());
	        }
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	httppost.releaseConnection();
        	try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return sb.toString();
		 
	}
	public static String postRequestJson(String url,Map<String,Object> mp,String contentType,String charset){
		if (StringUtils.isBlank(charset)) {
			charset = "UTF-8";
		}
		if (StringUtils.isBlank(contentType)) {
			contentType = "application/json;charset=" + charset;
		}
		StringBuilder sb = new StringBuilder();
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(url);
		postRequestconfig(httppost);
		httppost.addHeader("Content-Type", contentType); 
		try{
			if(mp!=null && mp.size()>0){
				httppost.setEntity(new StringEntity(JSON.toJSONString(mp),charset));
			}
		 CloseableHttpResponse response = httpclient.execute(httppost);
		 int statusOk = response.getStatusLine().getStatusCode();
		 if(logger.isInfoEnabled()){
	        	logger.info(url+"result-statusCode=>>"+statusOk);
	     }
        if(statusOk == HttpStatus.SC_OK){
        	HttpEntity entity = response.getEntity();
        	BufferedInputStream instream = new BufferedInputStream(entity.getContent()); 
	        byte[] chars = new byte[2048];
	        int len=0;
	        while((len=instream.read(chars))!=-1){
	        	sb.append(new String(chars,0,len,charset));
	        }
        }else{
        	logger.error(url+"result->网络异常.."+statusOk);
        }
        if(logger.isInfoEnabled()){
        	logger.info(url+"result->"+sb.toString());
        }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			httppost.releaseConnection();
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb.toString();
//		PostMethod post = new PostMethod(url);
//		// 设置请求的内容直接从文件中读�?
//		HttpClient httpclient = new HttpClient();
//		httpclient.getParams().setContentCharset("UTF-8");
//		String resultstring = "";
//		try {
//			if(mp!=null && mp.size()>0){
//				post.setRequestEntity(new StringRequestEntity(JSON.toJSONString(mp),
//					contentType, charset));
//			}
//			int result = httpclient.executeMethod(post);
//			resultstring = post.getResponseBodyAsString();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		post.releaseConnection();
//		return resultstring;
	}
	public static String postRequestJson(String url,String jsonstring,String contentType,String charset){
		if (StringUtils.isBlank(charset)) {
			charset = "UTF-8";
		}
		if (StringUtils.isBlank(contentType)) {
			contentType = "application/json;charset=" + charset;
		}
		PostMethod post = new PostMethod(url);
		// 设置请求的内容直接从文件中读�?
		HttpClient httpclient = new HttpClient();
		httpclient.getParams().setContentCharset(charset);
		String resultstring = "";
		try {
			if(StringUtils.isNotBlank(jsonstring)){
				post.setRequestEntity(new StringRequestEntity(JSON.toJSONString(jsonstring),
					contentType, charset));
			}
			int statusOk = httpclient.executeMethod(post);
			 if(logger.isInfoEnabled()){
		        	logger.info(url+"result-statusCode=>>"+statusOk);
		     }
			resultstring = post.getResponseBodyAsString();
			 if(logger.isInfoEnabled()){
		        	logger.info(url+"result=>>"+resultstring);
		     }
		} catch (Exception e) {
			e.printStackTrace();
		}
		post.releaseConnection();
		return resultstring;
	}	
	public static String postRequest(String url, String xmlstring,String contentType, String charset) {
		if (StringUtils.isBlank(charset)) {
			charset = "UTF-8";
		}
		if (StringUtils.isBlank(contentType)) {
			contentType = "text/html;charset=" + charset;
		}
		PostMethod post = new PostMethod(url);
		// 设置请求的内容直接从文件中读�?
		HttpClient httpclient = new HttpClient();
		httpclient.getParams().setContentCharset("UTF-8");
		String resultstring = "";
		try {
			post.setRequestEntity(new StringRequestEntity(xmlstring,
					contentType, charset));
			int result = httpclient.executeMethod(post);
			resultstring = post.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		post.releaseConnection();
		return resultstring;
	}
	
	public static void getFile(String fileurl, String destFileName)  
            throws ClientProtocolException, IOException {  
        // 生成一个httpclient对象  
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet(fileurl); 
        getRequestconfig(httpget,10000);
        
        HttpResponse response = httpclient.execute(httpget);  
        HttpEntity entity = response.getEntity();  
        InputStream in = entity.getContent();  
        File file = new File(destFileName);  
        try {  
            FileOutputStream fout = new FileOutputStream(file);  
            int l = -1;  
            byte[] tmp = new byte[1024];  
            while ((l = in.read(tmp)) != -1) {  
                fout.write(tmp, 0, l);  
                // 注意这里如果用OutputStream.write(buff)的话，图片会失真，大家可以试试  
            }  
            fout.flush();  
            fout.close();  
        } finally {  
            // 关闭低层流。  
            in.close();  
        }  
        httpclient.close();  
    }  
	/**
	 * 微信客服消息，上传文件接口使用
	 * @param url 上传路径
	 * @param filename 上传文件url地址
	 * @param filename2 对应的上传文件名
	 * @return
	 */
	public static String postfile(String url,String file,String filename){
		StringBuilder sb = new StringBuilder();
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(url); 
        postRequestconfig(httppost);
        
        try {
        	HttpEntity entityfile = MultipartEntityBuilder.create()  
        			.addBinaryBody("file", new File(file), ContentType.DEFAULT_BINARY, file).addTextBody("filename",filename)  
        			.build();  
        	httppost.setEntity(entityfile);  
	    	HttpResponse response = httpclient.execute(httppost);
			int statusOk = response.getStatusLine().getStatusCode();
			if(logger.isInfoEnabled()){
		        	logger.info(url+"result-statusCode=>>"+statusOk);
		    }
	        if(statusOk == HttpStatus.SC_OK){
	        	HttpEntity entity = response.getEntity();
	        	BufferedInputStream instream = new BufferedInputStream(entity.getContent()); 
		        byte[] chars = new byte[2048];
		        int len=0;
		        while((len=instream.read(chars))!=-1){
		        	sb.append(new String(chars,0,len));
		        }
	        }else{
	        	logger.error(url+"result->网络异常.."+statusOk);
	        }
	        if(logger.isInfoEnabled()){
	        	logger.info(url+"result->"+sb.toString());
	        }
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return sb.toString();
	}
	public static void main(String[] args) {
//		Map<String, String> testmap = Constants.smsmap;
//		Iterator<String> itor = testmap.keySet().iterator();
//		while (itor.hasNext()) {
//			String key = itor.next();
//			String value = testmap.get(key);
			Map<String, Object> datamap = new HashMap<String, Object>();
			datamap.put("Action", "Recharge");
			datamap.put("Qort", "3");
			datamap.put("ID", 6);
			datamap.put("BankCode", 0);
			datamap.put("Money", 10);
			datamap.put("PayUser", 1221);
//			// Posturl.postRequest("http://localhost:8080/sms/send", datamap);
//			System.out.println(datamap.toString());
//			;
//		}
		Map<String,String> cookies  =new HashMap<String,String>();
		cookies.put("ASP.NET_SessionId","plotjo433bixoql4x1qiuiun");
		cookies.put("ef_cookie_url_referrer", "http%3a%2f%2fk377.net%2fregister.html");
		
		Map<String, String> header = new HashMap<String, String>();
		header.put("Host", "k377.net");
		header.put("Origin", "http://k377.net");
		header.put("Referer","http://k377.net/alipay.html");
		header.put("User-Agent","Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Mobile Safari/537.36" );
		
		String url = "http://k377.net/tools/ssc_ajax.ashx";
//		System.out.println(Posturl.postRequestJson(url, "", "", "utf-8"));
		String result = Posturl.postRequest(url, datamap,cookies,header,"k377.net", null);
		System.out.println(result);
	}
}
