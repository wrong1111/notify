package com.game.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;


public class Percontion {

	private int total = 0;
	private ArrayList<String> arrangeList = new ArrayList<String>();

	private void swap(String list[], int k, int i) {
		String c3 = list[k];
		list[k] = list[i];
		list[i] = c3;
	}

	public void perm(String list[], int k, int m) {
		if (k > m) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i <= m; i++) {
				sb.append(list[i]).append(",");
			}
			if (sb.length() > 0) {
				sb.setLength(sb.length() - 1);
			}
			arrangeList.add(sb.toString());
			total++;
		} else {
			for (int i = k; i <= m; i++) {
				swap(list, k, i);
				perm(list, k + 1, m);
				swap(list, k, i);
			}
		}
	}
	
	/**
	 * @author wyong
	 * @version 1.0
	 * @2010-8-22
	 * @description
	 * @param list 要生排列的字符串数组如 {"a","b","c"}
	 * @param k  启始位置 0
	 * @param m  结束位置 2 list.length
	 * @param result 
	 * @return 
	 * StringBuffer a,b,c|a,c,b|b,a,c|b,c,a|c,b,a|c,a,b
	 */
	public StringBuilder perm(String list[], int k, int m,StringBuilder result) {
		if (k > m) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i <= m; i++) {
				sb.append(list[i]).append(",");
			}
			if (sb.length() > 0 && sb.toString().endsWith(",")) {
				sb.setLength(sb.length() - 1);
			}
			result.append(sb.toString()).append("|");//arrangeList.add(sb.toString());
			total++;
			
		} else {
			for (int i = k; i <= m; i++) {
				swap(list, k, i);
				result = perm(list, k + 1, m,result);
				swap(list, k, i);
			}
		}
		//修改多去掉一位 wyong 2010-9-2
		if(result.length()>0 && result.toString().endsWith(","))
			result.setLength(result.length()-1);
				
		return result;
	}
	 
	 
	 
	
	public int getTotal(){
		return this.total;
	}
	
	public int getPercontionCount(int n,int m) {
		int c = 1;
		for (int i=n-m; i<n; c*=++i);
		return c;
	}

	@SuppressWarnings("rawtypes")
	public ArrayList getPerconData() {
		return arrangeList;
	}

	public static Map<String,String>  cloneMap(Map<String,String> target,Object t){
		Map<String,String> number = new HashMap<String,String>();
		for(Iterator<String> itor = target.keySet().iterator();itor.hasNext();){
			String key = itor.next();
			if(key.equals(t.toString()))continue;
			number.put(key, target.get(key));
		}
		return number;
	}
	public static Map<String,String> init(){
		Map<String,String> number = new HashMap<String,String>();
		number.put("1","1");number.put("2","2");number.put("3","3");number.put("4","4");number.put("5","5");
		number.put("6","6");number.put("7","7");number.put("8","8");number.put("9","9");number.put("0","0");
		return number;
	}
	
	//[0/1/2, 0/1/3, 0/2/3, 1/2/3, 0/1/4, 0/2/4, 1/2/4, 0/3/4, 1/3/4, 2/3/4]
	public static int niu(int a,int b,int c,int d ,int e){
		int[] sort = new int[]{a,b,c,d,e};
		Arrays.sort(sort);
		if((sort[0]+sort[1]+sort[2])%10 == 0){
			if((sort[3]+sort[4])%10== 0){
				return 90;
			}else {
				int s = sort[3]+sort[4];
				return 80+(s>10?s%10:s);
			}
		}else 	if((sort[0]+sort[1]+sort[3])%10 == 0){
			if((sort[2]+sort[4])%10== 0){
				return 90;
			}else {
				int s = sort[2]+sort[4];
				return 80+(s>10?s%10:s);
			}
		}else	if((sort[0]+sort[2]+sort[3])%10 == 0){
			if((sort[1]+sort[4])%10== 0){
				return 90;
			}else {
				int s = sort[1]+sort[4];
				return 80+(s>10?s%10:s);
			}
		}else if((sort[1]+sort[2]+sort[3])%10 == 0){
			if((sort[0]+sort[4])%10== 0){
				return 90;
			}else {
				int s = sort[0]+sort[4];
				return 80+(s>10?s%10:s);
			}
		}else if((sort[0]+sort[1]+sort[4])%10 == 0){
			if((sort[2]+sort[3])%10== 0){
				return 90;
			}else {
				int s = sort[2]+sort[3];
				return 80+(s>10?s%10:s);
			}
		} else if((sort[0]+sort[2]+sort[4])%10 == 0){
			if((sort[1]+sort[3])%10== 0){
				return 90;
			}else {
				int s = sort[1]+sort[3];
				return 80+(s>10?s%10:s);
			}
		}else if((sort[1]+sort[2]+sort[4])%10 == 0){
			if((sort[0]+sort[3])%10== 0){
				return 90;
			}else {
				int s = sort[0]+sort[3];
				return 80+(s>10?s%10:s);
			}
		}else if((sort[0]+sort[3]+sort[4])%10 == 0){
			if((sort[1]+sort[2])%10== 0){
				return 90;
			}else {
				int s = sort[1]+sort[2];
				return 80+(s>10?s%10:s);
			}
		}else if((sort[1]+sort[3]+sort[4])%10 == 0){
			if((sort[0]+sort[2])%10== 0){
				return 90;
			}else {
				int s = sort[0]+sort[2];
				return 80+(s>10?s%10:s);
			}
		}else if((sort[2]+sort[3]+sort[4])%10 == 0){
			if((sort[0]+sort[1])%10== 0){
				return 90;
			}else {
				int s = sort[0]+sort[1];
				return 80+(s>10?s%10:s);
			}
		} 
		return 80;
	}
	public static String dugeniu(int a,int b,int c,int d ,int e){
		int[] sort = new int[]{a,b,c,d,e};
		Arrays.sort(sort);
		if((sort[0]+sort[1]+sort[2])%10 == 0){
			if((sort[3]+sort[4])%10== 0){
				return "牛牛";
			}else {
				int s = sort[3]+sort[4];
				return "牛"+(s>10?s%10:s);
			}
		}else 	if((sort[0]+sort[1]+sort[3])%10 == 0){
			if((sort[2]+sort[4])%10== 0){
				return "牛牛";
			}else {
				int s = sort[2]+sort[4];
				return "牛"+(s>10?s%10:s);
			}
		}else	if((sort[0]+sort[2]+sort[3])%10 == 0){
			if((sort[1]+sort[4])%10== 0){
				return  "牛牛";
			}else {
				int s = sort[1]+sort[4];
				return "牛"+(s>10?s%10:s);
			}
		}else if((sort[1]+sort[2]+sort[3])%10 == 0){
			if((sort[0]+sort[4])%10== 0){
				return  "牛牛";
			}else {
				int s = sort[0]+sort[4];
				return "牛"+(s>10?s%10:s);
			}
		}else if((sort[0]+sort[1]+sort[4])%10 == 0){
			if((sort[2]+sort[3])%10== 0){
				return  "牛牛";
			}else {
				int s = sort[2]+sort[3];
				return "牛"+(s>10?s%10:s);
			}
		} else if((sort[0]+sort[2]+sort[4])%10 == 0){
			if((sort[1]+sort[3])%10== 0){
				return  "牛牛";
			}else {
				int s = sort[1]+sort[3];
				return "牛"+(s>10?s%10:s);
			}
		}else if((sort[1]+sort[2]+sort[4])%10 == 0){
			if((sort[0]+sort[3])%10== 0){
				return  "牛牛";
			}else {
				int s = sort[0]+sort[3];
				return "牛"+(s>10?s%10:s);
			}
		}else if((sort[0]+sort[3]+sort[4])%10 == 0){
			if((sort[1]+sort[2])%10== 0){
				return  "牛牛";
			}else {
				int s = sort[1]+sort[2];
				return "牛"+(s>10?s%10:s);
			}
		}else if((sort[1]+sort[3]+sort[4])%10 == 0){
			if((sort[0]+sort[2])%10== 0){
				return  "牛牛";
			}else {
				int s = sort[0]+sort[2];
				return "牛"+(s>10?s%10:s);
			}
		}else if((sort[2]+sort[3]+sort[4])%10 == 0){
			if((sort[0]+sort[1])%10== 0){
				return  "牛牛";
			}else {
				int s = sort[0]+sort[1];
				return "牛"+(s>10?s%10:s);
			}
		} 
		return "无牛";
	}
	@SuppressWarnings("rawtypes")
	public static void main(String args[]) {
		Percontion percontion = new Percontion();
		 /*
		String[] s = new String[]{"1","2","3","4","5","6","7","8","9","10"};//
		 StringBuilder result = new StringBuilder();
		 result = percontion.perm(s, 0,9, result);
		 try {
			FileUtils.writeStringToFile(new File("d://a.txt"), result.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		*/
		 
		Map<String,ArrayList<String>> frontresult = new HashMap<String,ArrayList<String>>();
		String resultStr = "";
		 
	    Map<String,ArrayList<String>> backresult = new HashMap<String,ArrayList<String>>();
		Map<String,String> m1 = init();
		Object[] s1 =   m1.keySet().toArray();
		
		String backStr = "";
		for(Object t1 : s1){
			Map<String,String> m2 = init();
			Object[] s2 =  m2.keySet().toArray();
			
			for(Object t2 : s2){
				Map<String,String> m3 =  init();
				Object[] s3 = m3.keySet().toArray();
				for(Object t3 : s3){
					Map<String,String> m4 =  init();
					Object[] s4 =  m4.keySet().toArray();
					for(Object t4 : s4){
						Map<String,String> m5 =  init();
						Object[] s5 =  m5.keySet().toArray();
						for(Object t5 : s5){
							int n = niu(Integer.valueOf(t1.toString()),Integer.valueOf(t2.toString()),Integer.valueOf(t3.toString()),Integer.valueOf(t4.toString()),Integer.valueOf(t5.toString()));
							resultStr = String.valueOf(n);
							 
							ArrayList<String> blist = null;
							if(frontresult.get(resultStr) == null){
								blist  = new ArrayList<String>();
							}else{
								blist = frontresult.get(resultStr);
							}
							blist.add(t1+","+t2+","+t3+","+t4+","+t5);
							frontresult.put(resultStr, blist);
							 
							/*
							Map<String,String> m6 = cloneMap(m5,t5);
							Object[] s6 =  m6.keySet().toArray();
							for(Object t6 :s6){
								
								Map<String,String> m7 = cloneMap(m6,t6);
								Object[] s7 =  m7.keySet().toArray();
								for(Object t7 :s7){
									
									Map<String,String> m8 = cloneMap(m7,t7);
									Object[] s8 =  m8.keySet().toArray();
									for(Object t8 :s8){
										Map<String,String> m9 = cloneMap(m8,t8);
										Object[] s9 =  m9.keySet().toArray();
										for(Object t9 :s9){
											Map<String,String> m10 = cloneMap(m9,t9);
											Object[] s10 =  m10.keySet().toArray();
											for(Object t10 :s10){
												int n1 = niu(Integer.valueOf(t6.toString()),Integer.valueOf(t7.toString()),Integer.valueOf(t8.toString()),Integer.valueOf(t9.toString()),Integer.valueOf(t10.toString()));
												backStr = String.valueOf(n1);
												 
												ArrayList<String> bclist = null;
												if(backresult.get(backStr) == null){
													bclist  = new ArrayList<String>();
												}else{
													bclist = backresult.get(backStr);
												}
												bclist.add(t6+","+t7+","+t8+","+t9+","+t10);
												backresult.put(backStr, bclist);
												 
												if(n==90){
													ArrayList<String> bclist = null;
													if(backresult.get(backStr) == null){
														bclist  = new ArrayList<String>();
													}else{
														bclist = backresult.get(backStr);
													}
													bclist.add(t1+","+t2+","+t3+","+t4+","+t5+","+t6+","+t7+","+t8+","+t9+","+t10);
													backresult.put(backStr, bclist);
													//System.out.println("牛和:->("+backStr+"),号码->"+(t1+","+t2+","+t3+","+t4+","+t5+","+t6+","+t7+","+t8+","+t9+","+t10+","));
												}
											}
											
										}
									}
								}
							}*/
						}
					}
				}
			}
		} 
		 
		int total = 0;
		StringBuilder sb = new StringBuilder();
		
		for(Iterator<String> itor = frontresult.keySet().iterator();itor.hasNext();){
			String key = itor.next();
			ArrayList blist = frontresult.get(key);
			sb.append(key+"--key-->["+blist.size()+"]注");
			sb.append("\n\r");
			sb.append(StringUtils.join(blist.toArray(),"|"));
			sb.append("\n\r");
			try {
				FileUtils.writeStringToFile(new File("d:/f_"+key+".txt"),sb.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sb.setLength(0);
			total = total+blist.size();
		}
		System.out.println("-前共:"+total);
//		sb.setLength(0);
//		for(Iterator<String> itor = backresult.keySet().iterator();itor.hasNext();){
//			String key = itor.next();
//			ArrayList blist = backresult.get(key);
//			sb.append(key+"--key-->["+blist.size()+"]注");
//			sb.append("\n\r");
//			sb.append(StringUtils.join(blist.toArray(),"|"));
//			sb.append("\n\r");
//			try {
//				FileUtils.writeStringToFile(new File("d:/b1_"+key+".txt"),sb.toString());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			sb.setLength(0);
//			total = total+blist.size();
//		}
//		System.out.println("-后共:"+total);
		
//		StringBuilder sb = new StringBuilder();
//		sb = percontion.perm(new String[]{"a","b","c","d","e"}, 0, 4, sb);
//		System.out.println(sb.toString());
//		
		
//		Combine combine = new Combine();
//		combine.mn(new String[]{"1","2","3","4","5","6","7","8","9","10"}, 5);
//		System.out.println(combine.getCombineData().size());
//		List<String> resultList = combine.getCombineData();
//		for(String s : resultList){
//			String[] splitStr = StringUtils.splitPreserveAllTokens(s,"/");
//			ArrayList<String> bclist = null;
//			int n = niu(Integer.valueOf(splitStr[0]),Integer.valueOf(splitStr[1]),Integer.valueOf(splitStr[2]),Integer.valueOf(splitStr[3]),Integer.valueOf(splitStr[4]));
//			resultStr = String.valueOf(n);
//			if(frontresult.get(resultStr) == null){
//				bclist  = new ArrayList<String>();
//			}else{
//				bclist = frontresult.get(resultStr);
//			}
//			bclist.add(s);
//			frontresult.put(resultStr, bclist);
//			
//		}
		/*
		StringBuilder sb = new StringBuilder();
		for(Iterator itor = frontresult.keySet().iterator();itor.hasNext();){
			String key  = itor.next().toString();
			List<String> blist = frontresult.get(key);
			sb.append(key+"->key-"+blist.size()+"注");
			sb.append("\n\r");
			sb.append(StringUtils.join(blist.toArray(),"|"));
			sb.append("\n\r");
		}
		try {
			FileUtils.writeStringToFile(new File("d:/all.txt"),sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  */
//		System.out.println(percontion.getPercontionCount(10, 10));
	}

}
