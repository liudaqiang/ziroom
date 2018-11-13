package com.lq.crawler.ziroom.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class SqlPipeLine implements Pipeline{
	public static Connection conn = null;
	public static Integer num = 0;
	
	public SqlPipeLine(){
		 String conn_str = "jdbc:mysql://127.0.0.1:3306/ziroom?"  
	                + "user=root&password=mysql&useUnicode=true&characterEncoding=UTF8"; 
		 try {
			Class.forName("com.mysql.jdbc.Driver");
			if(conn == null){
				conn = DriverManager.getConnection(conn_str);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void process(ResultItems resultItems, Task task) {
		StringBuffer valueBuffer = new StringBuffer();
		StringBuffer keyBuffer = new StringBuffer();
		resultItems.getAll().forEach((k, v) ->{
			//System.out.println("key:value = " + k + ":" + v);
			if(v instanceof List){
				List<Object> vValue=(List)v;
				if("ICON".equals(k)){
					 keyBuffer.append(","+k.toString());
		          	 valueBuffer.append(",'"+vValue.get(vValue.size()-1)+"'");
				}
				if("AREA".equals(k)){
					 keyBuffer.append(","+"area");
					 valueBuffer.append(",'"+vValue.toArray()[0]+"'");
					 keyBuffer.append(","+"housetype");
					 valueBuffer.append(",'"+vValue.toArray()[2]+"'");
					 keyBuffer.append(","+"floor");
					 valueBuffer.append(",'"+vValue.toArray()[3]+"'");
				}
			}else{
				if("IS_SAIL".equals(k)){
					keyBuffer.append(","+k.toString());
					if("我要租房".equals(v.toString())){
			          	valueBuffer.append(",'"+1+"'");
					}else{
						valueBuffer.append(",'"+2+"'");
					}
				}else{
					keyBuffer.append(","+k.toString());
		          	valueBuffer.append(",'"+v.toString()+"'");
				}
			}
		});
		keyBuffer.append(","+"url");
		valueBuffer.append(",'"+resultItems.getRequest().getUrl()+"'");
		String sql = "INSERT INTO rent_house ("+keyBuffer.toString().substring(1)+")"+"VALUES"+"("+valueBuffer.toString().substring(1)+")";
		insertSql(sql);
	}
	public void insertSql(String sql){
		 try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
			int i = pstmt.executeUpdate();
		    pstmt.close();
		    if(i==1){
		    	System.out.println("成功---sql："+sql);
		    }else{
		    	System.out.println("失败---sql："+sql);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
