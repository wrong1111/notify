package com.game.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.game.utils.Base64ImageUtil;
import com.game.utils.Constants;
import com.game.utils.DateUtils;
import com.game.utils.ImageUtils;
import com.game.utils.TradUtil;
import com.game.utils.common.BaseAction;
import com.game.utils.encription.DESADESecuritys;


/**
 * 上传文件 下载文件
 * 
 * @author wyong
 */
@Controller
@RequestMapping("/interface/file")
public class FileManage extends BaseAction {

	 
	private static final Logger logger = Logger.getLogger(FileManage.class);
	
	/**
	 * @param data
	 * @param callback
	 * @param path
	 * @param filename
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/baseload",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> baseload(@RequestParam(value = "data", defaultValue = "") String data,
			@RequestParam(value = "callback", defaultValue = "") String callback,
			@RequestParam(value = "path", defaultValue = "tmp") String path,
			@RequestParam(value = "filename", defaultValue = "") String filename,
			HttpServletRequest request, HttpServletResponse response){
//		String origin = request.getHeader("origin");
		if(logger.isInfoEnabled()){
			logger.info("baseload-上传进来了。。。");
		}
		Map<String, Object> result = super.SUCESS();
//		if(StringUtils.isNotBlank(origin) && origin.indexOf(".bdbvip.com")>-1){
//			String origins[] = StringUtils.split(origin,":");
//			if(origins[1].endsWith(".bdbvip.com")){
//				response.setHeader("Access-Control-Allow-Origin", "bus.bdbvip.com,cus.bdbvip.com");
//				response.setHeader("Access-Control-Allow-Origin", "*");
//				response.setHeader("Access-Control-Allow-Methods","POST");
//				response.setHeader("Access-Control-Allow-Headers", "Origin,X-Requested-With,Content-Type,Accept");
//			}
//		}
		if(StringUtils.isNotBlank(filename)){
			filename = StringUtils.replace(filename, ",", "");
		}
		String contentType = request.getContentType();
		String maxupload = Constants.getConfigkey("upload.max.size");
		if(StringUtils.isBlank(maxupload)){
			maxupload ="5";
		}
		try {
			if("application/octet-stream".equalsIgnoreCase(contentType)){
				if(logger.isInfoEnabled()){
					logger.info("baseload-octet上传了。。。。");
				}
//				String dispoString = request.getHeader("Content-Disposition");
				int i = request.getContentLength();
	            byte buffer[] = new byte[i];
	            int j = 0;
	            while(j < i) { //获取表单的上传文件
	                int k = request.getInputStream().read(buffer, j, i-j);
	                j += k;
	            }
	            data = new String(buffer,0,j);
	            if(logger.isInfoEnabled()){
	            	logger.info("baseload-octet接收到了。。。。");
	            }
	            if (buffer.length == 0) { //文件是否为空
	            	result.put("status", "-1");
	                result.put("msg", "上传文件不能为空");
	                return result;
	            }
	            int maxSize  =Integer.valueOf(maxupload);
	            if (maxSize > 0 && buffer.length > maxSize*1024*1024) { //检查文件大小
	            	result.put("status","-2");
	                result.put("msg", "上传文件"+maxupload+"M的大小超出限制");
	                return result;
	            }
	      
	            String rootpath = Constants.getConfigkey("common.file.path");
	    		if (StringUtils.isBlank(rootpath)) {
	    			rootpath = "base";
	    		}
	    		rootpath = rootpath+File.separator;//生成路径
	    		String resultpath =  path + File.separator;//返回路径
	    		if (StringUtils.isBlank(filename)) {
	    			filename = TradUtil.getTradingNo("pic");
	    		}
	    		String domain = Constants.getConfigkey("upload.image.base64.domain");
	    		String[] str = StringUtils.split(data,",");
	    		if (str != null
	    				&& str.length == 2
	    				&& (str[0].indexOf("png") > -1 || str[0].indexOf("jpg") > -1 || str[0].indexOf("jpeg") > -1|| str[0]
	    						.indexOf("gif") > -1)) {
	    			// data:image/png;base64,
	    			result.put("status", "0");
	    			result.put("msg", "上传成功");
	    			String[] suf = StringUtils.split(StringUtils.split(str[0], ";")[0],
	    					"/");
	    			filename = filename + "." + suf[1];
	    			resultpath = resultpath+StringUtils.replace(DateUtils.getDateToStr(new Date(), "yyyy-MM-dd"),"-",File.separator)+ File.separator+filename;
	    			if(log.isInfoEnabled()){
	    				log.info("FileManage.baseupload.resultpath="+resultpath);
	    			}
	    			File newfile = newfile = new File(rootpath+resultpath);
	    			 
	    			newfile.getParentFile().mkdirs();
	    			if(log.isInfoEnabled()){
	    				log.info("FileManage.baseupload.contentpath="+rootpath+resultpath);
	    			}
	    			if(!newfile.exists()){
	    				newfile.createNewFile();
	    			}
	    			if(log.isInfoEnabled()){
	    				log.info("FileManage.baseupload.getpath="+newfile.getParent()+"|filename="+filename);
	    			}
	    			boolean flag = Base64ImageUtil.GenerateImage(str[1], newfile.getParent(),
	    					filename);
	    			if (flag) {
//	    					if(resultpath.startsWith(File.separator)){
//	    						//水印生成
//	    						addWater(rootpath+resultpath);
//	    						resultpath = domain + resultpath;
//	    					}else{
//	    						//水印生成
//	    						addWater(rootpath+File.separator+resultpath);
//	    						resultpath = domain +File.separator+ resultpath;
//	    					}
	    					
	    				resultpath = domain +File.separator+ resultpath;
	    				result.put("path", resultpath);
	    			}else{
	    				result.put("path","");
	    				result.put("msg","上传生成失败");
	    				result.put("status", "-1");
	    			}
	    		} else {
	    			result.put("status", "-1");
	    			result.put("msg", "上传文件异常");
	    			result.put("path","");
	    		}
	    		 if(logger.isInfoEnabled()){
		            	logger.info("baseload-生成文件。。。。");
		         }
			}else{
				if(logger.isInfoEnabled()){
	            	logger.info("baseload-接收到了。。。。");
	            }
				if(StringUtils.isBlank(data)){
					result.put("status","-2");
			        result.put("msg", "data没有传入数据。");
			        return result;
				}
				String rootpath = Constants.getConfigkey("common.file.path");
				if (StringUtils.isBlank(rootpath)) {
					rootpath = "base";
				}
				rootpath = rootpath+File.separator;//生成路径
				String resultpath =  path + File.separator;//返回路径
				if (StringUtils.isBlank(filename)) {
					filename = TradUtil.getTradingNo("pic");
				}
				String domain = Constants.getConfigkey("upload.image.base64.domain");
				
				String[] str = StringUtils.split(data,",");
				try{
				if (str != null
						&& str.length == 2
						&& (str[0].indexOf("png") > -1 || str[0].indexOf("jpg") > -1 || str[0].indexOf("jpeg") > -1|| str[0]
								.indexOf("gif") > -1)) {
					// data:image/png;base64,
					result.put("status", "0");
					result.put("msg", "上传成功");
					String[] suf = StringUtils.split(StringUtils.split(str[0], ";")[0],
							"/");
					filename = filename + "." + suf[1];
					resultpath = resultpath+StringUtils.replace(DateUtils.getDateToStr(new Date(), "yyyy-MM-dd"),"-",File.separator)+ File.separator+filename;
					if(log.isInfoEnabled()){
						log.info("baseload--FileManage.baseupload.resultpath="+resultpath);
					}
					File newfile =  new File(rootpath+resultpath);
					newfile.getParentFile().mkdirs();
					if(log.isInfoEnabled()){
						log.info("FileManage.baseupload.contentpath="+rootpath+resultpath);
					}
					if(!newfile.exists()){
						newfile.createNewFile();
					}
					if(log.isInfoEnabled()){
						log.info("FileManage.baseupload.getpath="+newfile.getParent()+"|filename="+filename);
					}
					boolean flag = Base64ImageUtil.GenerateImage(str[1], newfile.getParent(),
							filename);
					if(logger.isInfoEnabled()){
		            	logger.info("baseload-生成文件。。。。");
		            }
					if (flag) {
//						if(resultpath.startsWith(File.separator)){
//    						//水印生成
//    						addWater(rootpath+resultpath);
//    						resultpath = domain + resultpath;
//    					}else{
//    						//水印生成
//    						addWater(rootpath+File.separator+resultpath);
//    						resultpath = domain +File.separator+ resultpath;
//    					}
						resultpath = domain +File.separator+ resultpath;
						result.put("path", resultpath);
					}else{
						result.put("path","");
						result.put("msg","上传生成失败");
						result.put("status", "-1");
					}
				} else {
					result.put("status", "-1");
					result.put("msg", "上传文件异常");
					result.put("path","");
				}
				}catch(Exception e){
					e.printStackTrace();
					result.put("status", "-1");
					result.put("msg", "上传失败"+e.getMessage());
					result.put("path", "");
				}
			}
		} catch (IOException e) {
			 log.error(e.getMessage(),e);
			 result.put("status", "-1");
			 result.put("msg","系统异常"+e.getMessage());
		}
		if(logger.isInfoEnabled()){
			logger.info("baseload-上传成功了。。。。");
		}
		return result;
	}


	@RequestMapping(value = "/baseupload", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> baseupload(
			@RequestParam(value = "data", defaultValue="") String data,
			@RequestParam(value = "callback", defaultValue = "") String callback,
			@RequestParam(value = "path", defaultValue = "tmp") String path,
			@RequestParam(value = "filename", defaultValue = "") String filename,
			HttpServletRequest request, HttpServletResponse response) {
		return baseload(data,callback,path,filename,request,response);
	}

	/**
	 * 文件上传 dir /base/{userid} /shop/{year}/{month}/{day}/{userid}
	 * 
	 * @param name
	 * @param file
	 * @param dir
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> upload(
			@RequestParam(value = "name",defaultValue="") String name,
			@RequestParam("file") MultipartFile file,
			@RequestParam(value = "dir", required = false, defaultValue = "tmp") String dir,
			@RequestParam(value = "bool", required = false, defaultValue = "true") boolean bool,
			@RequestParam(value = "encoding", required = false, defaultValue = "") String encoding,
			@RequestParam(value = "callback", required = false, defaultValue = "") String callback,
			HttpServletRequest request, HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "*");
//		response.setHeader("Access-Control-Allow-Headers", "Origin,X-Requested-With,Content-Type,Accept");
		if (logger.isInfoEnabled()) {
			logger.info("upload--[name:" + name + ",filename:"
					+ file.getOriginalFilename() + ",size:" + file.getSize()
					+ ",dir:" + dir + ",bool:" + bool + ",encoding:" + encoding
					+ ",fileName:"+file.getName()+"]");
		}
 		if(StringUtils.isNotBlank(name)){
			name = StringUtils.replace(name, ",", "");
		}else{
			name = file.getName();
		}
		Map<String, Object> resultmap = new HashMap<String, Object>();
		boolean passflag = true;
		if (passflag) {
			 String rootpath = Constants.getConfigkey("common.file.path");
	    		if (StringUtils.isBlank(rootpath)) {
	    			rootpath = "base";
	    		}
			dir = (File.separator + dir)+File.separator+StringUtils.replace(DateUtils.getDateToStr(new Date(), "yyyy-MM-dd"),"-",File.separator);
			String dirPath = rootpath+File.separator + dir;
			if (!file.isEmpty()) {
				try {
					String realName = this.copyFile(file.getInputStream(),
							dirPath, name, bool, encoding);
					String domain = Constants.getConfigkey("upload.image.base64.domain");
					resultmap.put("status", "0");
					resultmap.put("msg", "上传成功");
					dir = domain+dir +File.separator+ realName;
					resultmap.put("path", dir);
					logger.info("[name:" + name + ",returnName:" + dir
							+ File.separator + realName + "]");
					return resultmap;
				} catch (IOException e) {
					logger.error("文件上传", e);
				}
			} else {
				resultmap.put("status", "9012");
				resultmap.put("msg",
						Constants.getParamterkey("common.file.nofiles"));
				logger.info("[name:" + name + "] 文件为空");
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("upload--[上传成功]");
		}
		return resultmap;
	}

	/**
	 * 多文件上传
	 * 
	 * @param request
	 * @param dir
	 * @return
	 */
	@RequestMapping(value = "/multipartUpload", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> upload2(
			MultipartHttpServletRequest request,
			@RequestParam(value = "dir", required = false, defaultValue = "/tmp") String dir,
			@RequestParam(value = "bool", required = false, defaultValue = "true") boolean bool,
			@RequestParam(value = "encoding", required = false, defaultValue = "") String encoding,
			@RequestParam(value = "callback", required = false, defaultValue = "") String callback,
			HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "*");
//		response.setHeader("Access-Control-Allow-Methods","POST");
//		response.setHeader("Access-Control-Allow-Headers", "Origin,X-Requested-With,Content-Type,Accept");
		Map<String, Object> resultmap = new HashMap<String, Object>();
		boolean passflag = true;
		if (passflag) {
			 String rootpath = Constants.getConfigkey("common.file.path");
	    		if (StringUtils.isBlank(rootpath)) {
	    			rootpath = "base";
	    		}
			List<String> realNames = new ArrayList<String>();
			List<MultipartFile> files = request.getFiles("file");
			logger.info("[size:" + files.size() + ",dir:" + dir + ",bool:"
					+ bool + ",encoding:" + encoding + "]" + files);
			dir = dir == null || "".equals(dir) ? "/" : (File.separator + dir)+StringUtils.replace(DateUtils.getDateToStr(new Date(), "yyyy-MM-dd"),"-",File.separator);
			String dirPath = rootpath +File.separator + dir;
			for (int i = 0; i < files.size(); i++) {
				if (!files.get(i).isEmpty()) {
					String realName = null;
					try {
						realName = this.copyFile(files.get(i).getInputStream(),
								dirPath, files.get(i).getOriginalFilename(),
								bool, encoding);
						String domain = Constants.getConfigkey("upload.image.base64.domain");
						realName = StringUtils.replace(realName, ",", "");
						realName = domain+dir + File.separator + realName;
					} catch (IOException e) {
						logger.error("多文件上传", e);
					}
					realNames.add(realName);
				}
			}
			if (realNames != null && !realNames.isEmpty()) {
				resultmap.put("status", "0");
				resultmap.put("msg", files.size() + "个文件上传成功");
				resultmap.put("path", StringUtils.join(realNames, ","));
			}
		}
		return resultmap;
	}

	/**
	 * 文件下载
	 * 
	 * @param response
	 * @param request
	 * @param pathDecrypt
	 *            加密内容 DESADESecuritys.encodeBASE64(DESADESecuritys.encryptEde(
	 *            '/t/t/1.txt')) download/t/t/1.txt download?pathDecrypt=
	 *            /t/t/1.txt&flag=1
	 */
	@RequestMapping("/download")
	public void download(
			HttpServletResponse response,
			HttpServletRequest request,
			String pathDecrypt,
			@RequestParam(value = "flag", required = false, defaultValue = "0") String flag) {
		logger.info("[pathDecrypt:" + pathDecrypt + ",flag:" + flag + "]");
		OutputStream os = null;
		response.reset();
		if (pathDecrypt == null || "".equals(pathDecrypt))
			return;

		String filePath = null;
		try {
			pathDecrypt = pathDecrypt.replaceAll(" ", "");
			if (flag != null && !"1".equals(flag))
				filePath = DESADESecuritys.decryptEde(DESADESecuritys
						.decodeBASE64(pathDecrypt));
			else
				filePath = pathDecrypt.trim();
		} catch (Exception e) {
			logger.error("文件下载", e);

		}

		if (filePath == null || "".equals(filePath))
			return;
		 String rootpath = Constants.getConfigkey("common.file.path");
 		if (StringUtils.isBlank(rootpath)) {
 			rootpath = "base";
 		}
		filePath = rootpath+File.separator + filePath;

		String realName = "";
		if (filePath.lastIndexOf("/") > -1) {
			realName = filePath.substring(filePath.lastIndexOf("/") + 1);
		}
		response.setHeader("Content-Disposition", "attachment; filename="
				+ realName);
		response.setContentType("application/octet-stream; charset=utf-8");
		try {
			logger.info("下载路径" + filePath);
			os = response.getOutputStream();
			os.write(FileUtils.readFileToByteArray(new File(filePath)));
			os.flush();
		} catch (IOException e) {
			logger.error("文件下载", e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					logger.error("文件下载", e);
				}
			}
		}
	}

	/**
	 * 移动文件
	 * 
	 * @param srcFile
	 *            源文件路径 windows\abc.txt
	 * @param destPath
	 *            移动路径 temp\ww
	 * @return
	 */
	@RequestMapping("/moveFile")
	@ResponseBody
	public String moveFile(String srcFile, String destPath) {
		logger.info("[srcFile:" + srcFile + ",destPath:" + destPath + "]");
		if (srcFile == null || "".equals(srcFile) || destPath == null
				|| "".equals(destPath))
			return null;
		 String rootpath = Constants.getConfigkey("common.file.path");
	 		if (StringUtils.isBlank(rootpath)) {
	 			rootpath = "base";
	 		}
		File fileStr = new File(rootpath+File.separator + srcFile);
		try {
			File destFile = new File(rootpath +File.separator + destPath, fileStr.getName());
			if (!destFile.exists()) {
				if (!destFile.getParentFile().exists()) {
					destFile.getParentFile().mkdirs();
				}
				destFile.createNewFile();
			}
			FileUtils.copyFile(fileStr, destFile);
			logger.info("[srcFile:" + srcFile + ",destPath:" + destPath
					+ "] 复制文件成功");
			return fileStr.getName();
		} catch (Exception e) {
			logger.error("移动文件", e);
		}
		return null;
	}

	/**
	 * 写文件到当前目录的download目录中
	 * 
	 * @param in
	 * @param fileName
	 * @param bool
	 * @throws java.io.IOException
	 */
	private String copyFile(InputStream in, String dir, String fileName,
			boolean bool, String encoding) throws IOException {
		String pre = "";
		if (fileName.indexOf(".") > -1)
			pre = fileName.substring(fileName.lastIndexOf("."));
		String realName = null;
		if (bool) {
			realName = UUID.randomUUID().toString() + pre;
		} else {
			realName = fileName;
		}
		File file = new File(dir, realName);
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();

		}
		if (encoding == null || "".equals(encoding)) {
			FileUtils.copyInputStreamToFile(in, file);
		} else {
			copyInputStreamToFile(in, file, encoding);
		}

		return realName;
	}

	private void copyInputStreamToFile(InputStream in, File file,
			String encoding) {
		InputStreamReader isr = null;
		OutputStreamWriter out = null;
		try {
			isr = new InputStreamReader(in);
			out = new OutputStreamWriter(new FileOutputStream(file), encoding);
			char[] buff = new char[1024 * 4];
			int data = 0;
			while ((data = isr.read(buff)) != -1) {
				out.write(buff, 0, data);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
				if (isr != null)
					isr.close();
				if (in != null)
					in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * <pre>
	 * @param imagePath
	 *         图片路径
	 * </pre>
	 */
	private void addWater(String imagePath){
		//水印生成
		try {
			String img = Constants.getConfigkey("sys.water.logo.path");
			if(StringUtils.isBlank(img)) return ;
			File f = new File(img);
			if(!f.exists()){
				return ;
			}
			ImageUtils.addImageWatermark(imagePath, Constants.getConfigkey("sys.water.logo.path"), 1.0f);
		} catch (Exception e) {
			logger.error("添加水印错误----->"+imagePath);
			e.printStackTrace();
		}
	}

}
