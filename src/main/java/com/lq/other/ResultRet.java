package com.lq.other;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.Field;

public class ResultRet {
	
	public static Connection conn = null;
	public ResultRet(){
		
	}
	
	public static void main(String[] args) {
		
		 String conn_str = "jdbc:mysql://116.62.244.135:3306/qsdb?"  
	                + "user=root&password=root&useUnicode=true&characterEncoding=UTF8"; 
		 try {
			Class.forName("com.mysql.jdbc.Driver");
			if(conn == null){
				conn = DriverManager.getConnection(conn_str);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		String sql = "select id,name from qd_words limit 0,1000000";	
		PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        System.out.println("============查询开始==========");
	        long beginQuery = System.currentTimeMillis();
	        ResultSet rs = pstmt.executeQuery();
	        System.out.println("查询结束总共用时"+(System.currentTimeMillis()-beginQuery)+"ms");
	        System.out.println("============查询结束==========");
	        System.out.println("============遍历开始==========");
	        long beginTime = System.currentTimeMillis();
	        try {
				List list = populate(rs,Words.class);
				System.out.println(list.size());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        System.out.println("100W条数据用时"+(System.currentTimeMillis()-beginTime)+"ms");
	        System.out.println("========遍历结束=============");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	 public static List populate(ResultSet rs , Class clazz) throws SQLException, InstantiationException, IllegalAccessException{
	        //结果集的元素对象 
	        ResultSetMetaData rsmd = rs.getMetaData();
	        //获取结果集的元素个数
	         int colCount = rsmd.getColumnCount();
	         //返回结果的列表集合
	         List list = new ArrayList();
	         //业务对象的属性数组
	         Field[] fields = clazz.getDeclaredFields();
	         while(rs.next()){//对每一条记录进行操作
	             Object obj = clazz.newInstance();//构造业务对象实体
	             //将每一个字段取出进行赋值
	             for(int i = 1;i<=colCount;i++){
	                 Object value = rs.getObject(i);
	                 //寻找该列对应的对象属性
	                 for(int j=0;j<fields.length;j++){
	                     Field f = fields[j];
	                     //如果匹配进行赋值
	                     if(f.getName().equalsIgnoreCase(rsmd.getColumnName(i))){
	                         boolean flag = f.isAccessible();
	                         f.setAccessible(true);
	                         f.set(obj, value);
	                         f.setAccessible(flag);
	                     }
	                 }
	             }
	             list.add(obj);
	         }
	        return list;
	    }
}
