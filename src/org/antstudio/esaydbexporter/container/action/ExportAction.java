package org.antstudio.esaydbexporter.container.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.antstudio.esaydbexporter.db.DBManager;
import org.antstudio.esaydbexporter.domain.ExportTable;
import org.antstudio.esaydbexporter.utils.Tools;

/**
 * @author Gavin
 * @Date 2013-8-19 下午5:40:08
 */
public class ExportAction {
	
	
	public void exportByConfig(Properties p) throws SQLException{
		List<ExportTable> config = new ArrayList<ExportTable>();
		//dl_station.originColumns
		for(Object key:p.keySet()){
			if(key.toString().contains(".originColumns")){
				String tableName = key.toString().split("\\.")[0];
				String originColumns = p.getProperty((String) key);
				String newColumns = p.getProperty(tableName+".newColumns");
				String[] types = p.getProperty(tableName+".types").split(",");
				int priority = Integer.parseInt(p.getProperty(tableName+".priority").toLowerCase());
				config.add(new ExportTable(tableName,originColumns, newColumns, types,priority));
			}
		}
		Comparator<ExportTable> cmp = new Comparator<ExportTable>() {

			@Override
			public int compare(ExportTable o1, ExportTable o2) {
				if(o1.getPriority()>o2.getPriority())
					return -1;
				else
					return 1;
			}
		};
		Collections.sort(config, cmp);
		ResultSet rs;
		StringBuilder sql = new StringBuilder();
		for(ExportTable table:config){
			rs = DBManager.getConnection()
					.prepareStatement("select "+table.getNames()+" from "+table.getTableName()+" ").executeQuery();
			while(rs.next()){
				sql.append("INSERT INTO "+table.getTableName()+"("+table.getNewNames()+") values(");
				int column = 0;
				boolean needQuotation = false;//是否需要加引号
				String[] types = table.getTypes();
				while(column<types.length){
					column++;
					if(!types[column-1].contains("int")&&!types[column-1].contains("bit")&&!types[column-1].contains("Number")){
						needQuotation = true;
					}
					
					if(needQuotation){
						sql.append("'");
					}
					sql.append(rs.getObject(column));
					if(needQuotation){
						sql.append("'");
					}
					sql.append(",");
					needQuotation = false;
				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(");\n");
			}
			Tools.writeFile("/export.sql", sql.toString(),true);
			sql = new StringBuilder();
		}
		
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {
		Properties p = new Properties();
		p.load(new FileInputStream(new File("D://11")));
		new ExportAction().exportByConfig(p);
	}
}
