package com.game.utils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLUtil {
	
	public static String parseReq(HttpServletRequest request) throws Exception{
		InputStream inputStream = request.getInputStream();
		StringBuilder sb = new StringBuilder();
		BufferedInputStream bs = new BufferedInputStream(inputStream);
		byte[] bytes = new byte[2048];
		int len = 0 ;
		while((len = bs.read(bytes))!=-1){
			sb.append(new String(bytes,0,len));
		}
		bs.close();
		return sb.toString();
	}
	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		InputStream inputStream = request.getInputStream();
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		Element root = document.getRootElement();
		List<Element> elementList = root.elements();
		for (Element e : elementList)
			map.put(e.getName(), e.getTextTrim());
		inputStream.close();
		inputStream = null;
		return map;
	}
	

    /**
     * 将xml转换为Map
     * @param xml
     * @return
     * @throws Exception
     */
    public static Map<String, Object> xml2Map(String xml) throws Exception {
        return xmlDoc2Map(DocumentHelper.parseText(xml));
    }
    
    /**
     * 将xml文件转成Map
     * @param xmlDoc
     * @return
     */
    public static Map<String, Object> xmlDoc2Map(Document xmlDoc) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (xmlDoc == null) {
            return map;
        }
        Element root = xmlDoc.getRootElement();
        for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
            Element e = (Element) iterator.next();
            List list = e.elements();
            if (list.size() > 0) {
                map.put(e.getName(), Dom2Map(e,map));
            } else {
                map.put(e.getName(), e.getText());
            }
        }
        return map;
    }

    private static Map Dom2Map(Element e,Map map){
        List list = e.elements();
        if(list.size() > 0){
            for (int i = 0;i < list.size(); i++) {
                Element iter = (Element) list.get(i);
                List mapList = new ArrayList();
                if (iter.elements().size() > 0) {
                    Map m = Dom2Map(iter,map);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.putAll(m);
                    }
                } else {
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(iter.getText());
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(iter.getText());
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), iter.getText());
                    }
                }
            }
        } else {
            map.put(e.getName(), e.getText());
        }
        return map;
    }
    public static void main(String[] args) {
		try {
			Map<String,Object> o = XMLUtil.xml2Map("<root><returncode>1</returncode><imgurl>http://storenew.yzch.net/XingYeWxPayOwn1.aspx?OrderNo=E117020600139459</imgurl><orderno>E117020600139459</orderno></root>");
			System.out.println(o.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

