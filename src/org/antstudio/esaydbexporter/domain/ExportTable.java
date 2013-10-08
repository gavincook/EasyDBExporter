package org.antstudio.esaydbexporter.domain;
/**
 * columnÊµÌå
 * @author Gavin
 * @Date 2013-8-22 ÏÂÎç4:52:28
 */
public class ExportTable {

	public static int PRIORITY = 100;
	private String tableName;
	private String names;
	private String newNames;
	private String[] types;
	private int priority;
	
	public ExportTable(String tableName,String names,String newNames,String[] types,int priority){
		this.names = names;
		this.newNames = newNames;
		this.types = types;
		this.tableName = tableName;
		this.priority = priority;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getNewNames() {
		return newNames;
	}

	public void setNewNames(String newNames) {
		this.newNames = newNames;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	 
}
