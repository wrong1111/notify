package com.game.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class DB {

	public static List<String> getFilecontent(){
		List<String> strlist = new ArrayList<String>();
		String file = "E:\\work\\project\\doc\\异常提示信息.txt";
		try {
			 FileInputStream f = new FileInputStream(file);
			//FileReader bis = new FileReader(new File(file));
			//InputStream br =   new FileInputStream(new File(file));
			//InputStreamReader isr = new InputStreamReader(br, "GBK");
			BufferedReader dr=new BufferedReader(new InputStreamReader(f));
			StringBuilder sb = new StringBuilder("");
			String s ="";
				 while((s=dr.readLine())!=null){
					 if(StringUtils.isNotBlank(s) && !s.startsWith("-")){
						 System.out.println(s);
						 strlist.add(s);
					}
				 }
			 System.out.println(sb.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return strlist;
	}
	
	public static void insert(List<String> strlist){
		DriverManager dm = null;
		Connection con = null;
		PreparedStatement pst =null;
		ResultSet rs = null;
		PreparedStatement psto = null;
		String insert = "insert into t_sys_dict(dictname,dictvalue,type,status)values(?,?,?,'1')";
		String select = "select count(1) from t_sys_dict where dictname=?";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://192.168.0.105:3306/bdbao?sendStringParametersAsUnicode=false", "bdbvip","123456");
			pst = con.prepareStatement(select);
			psto = con.prepareStatement(insert);
			for(String s :strlist){
				String[] strAry = StringUtils.split(s,"|");
				if(strAry.length==3){
					pst.setString(1,strAry[1]);
					rs = pst.executeQuery();
					if(rs.next()){
						int c = rs.getInt(1);
						if(c<=0){
							psto.setString(1, strAry[1]);
							psto.setString(2, strAry[2]);
							String ss[] = StringUtils.split(strAry[1],".");
							psto.setString(3, ss[0]);
							System.out.println(strAry[1]+"=="+strAry[2]+"=="+ss[0]);
							//psto.addBatch();
						}
					}
					rs.close();
				}
			}
			//psto.executeBatch();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	public static void main(String[] args) {
		List<String> blist = getFilecontent();
		insert(blist);
		System.out.println("dd");
	}
}
