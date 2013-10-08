package org.antstudio.esaydbexporter.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 数据库管理类
 * @author gavin
 *
 */
public class DBManager {
	public static  ComboPooledDataSource ds;
	private static String dbName;
	public static void connect(String dbPath,String dbUser,String dbPassword){
		ds = new ComboPooledDataSource();
		ds.setUser(dbUser);
		ds.setPassword(dbPassword);
		ds.setJdbcUrl(dbPath);
		ds.setUnreturnedConnectionTimeout(0);
	}
	
	public static String getDbName(){
		Pattern p = Pattern.compile("/(\\w+)");
		Matcher m = p.matcher(ds.getJdbcUrl());
		while(m.find()){
			dbName=m.group(1);
		}
		return dbName;
	}
	public static Connection getConnection() throws SQLException{
		return ds.getConnection();
	}
	
}
