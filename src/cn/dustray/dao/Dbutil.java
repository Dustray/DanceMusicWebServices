package cn.dustray.dao;
import java.sql.*;

public class Dbutil {
	private static final String CLS="com.mysql.jdbc.Driver";
	private static final String URL="jdbc:mysql://139.129.49.252:3306/musicstate_db?useUnicode=true&characterEncoding=utf-8";
	private static final String USER="root";
	private static final String PWD="e933629f61";
	
	public static Connection conn=null;
	public static Statement stmt=null;
	public static PreparedStatement pStmt=null;
	public static ResultSet rs=null;
	
	public static void getConnection(){
		try {
			Class.forName(CLS);
			conn=DriverManager.getConnection(URL,USER,PWD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void closeAll(){
		try {
			if(rs!=null){
				rs.close();
				rs=null;
			}if(pStmt!=null){
				pStmt.close();
				pStmt=null;
			}
			if(stmt!=null){
				stmt.close();
				stmt=null;
			}
			if(conn!=null){
				conn.close();
				conn=null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
