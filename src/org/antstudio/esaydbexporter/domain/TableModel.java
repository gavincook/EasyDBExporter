package org.antstudio.esaydbexporter.domain;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * @author Gavin
 * @Date 2013-7-30 下午4:11:27
 */
public class TableModel extends DefaultTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 可编辑行或列
	 */
	private Integer editRow = -1,eidtColumn=-1;
	private Object[] cellValues;
	public TableModel(Vector rowData, Vector columnNames) {
		super(rowData, columnNames);
	}

	@Override
    public boolean isCellEditable(int row, int column) {
		if(this.editRow.equals(row)||this.eidtColumn.equals(column)){
	        return true;
		}
		if(cellValues!=null){
			Object cellValue = this.getValueAt(row, column);
			for(Object o:cellValues){
				if(o.equals(cellValue)){
					return true;
				}
			}
		}
		return false;
   }

	public void setEditRow(Integer editRow) {
		this.editRow = editRow;
	}

	public void setCellValues(Object[] cellValues) {
		this.cellValues = cellValues;
	}
	
	public void setEidtColumn(Integer eidtColumn) {
		this.eidtColumn = eidtColumn;
	}
	
   
}
