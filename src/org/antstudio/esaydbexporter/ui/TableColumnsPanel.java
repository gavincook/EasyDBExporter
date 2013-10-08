package org.antstudio.esaydbexporter.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import org.antstudio.esaydbexporter.db.DBManager;
import org.antstudio.esaydbexporter.domain.ExportTable;
import org.antstudio.esaydbexporter.domain.TableModel;
import org.antstudio.esaydbexporter.utils.Tools;

/**
 * @author Gavin
 * @Date 2013-7-30 下午4:53:14
 */
public class TableColumnsPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private JTable table ;
	private Vector rowData,columnNames;
	private TableModel model;
	public TableColumnsPanel(String tableName){
		
		rowData = new Vector();
		columnNames = new Vector();
		columnNames.add("字段名");
		columnNames.add("字段别名");
		columnNames.add("类型");
		renderData(tableName);
		setLayout(null);
		model = new TableModel(rowData,columnNames);
		model.setEidtColumn(1);
		String[] types = {"String","Number"};
		String[] cellValues = {types[0],types[1],"#Custom#"};
		model.setCellValues(cellValues);
		JComboBox combox = new JComboBox(types);
		this.table = new JTable(model);
		table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(combox));
		show();
	}
	
	public void getSelected(){
		StringBuilder originColumns = new StringBuilder(),newColumns = new StringBuilder();
		List<String> types = new ArrayList<String>();
		Map<String,String> extraRows = new TreeMap<String,String>();
		for(int row :table.getSelectedRows()){
			String temp = model.getValueAt(row, 0).toString();
			if(temp.matches("^\\[.+\\]$")){
				temp = removeBracket(temp);
				if(model.getValueAt(row, 2).toString().equals("String")){
					temp="'"+temp+"'";
				}
				extraRows.put(model.getValueAt(row, 1).toString(), temp);
				continue;
			}
			originColumns.append(model.getValueAt(row, 0)+",");
			newColumns.append(model.getValueAt(row, 1)+",");
			types.add(model.getValueAt(row, 2).toString());
		}
		originColumns.deleteCharAt(originColumns.length()-1);
		newColumns.deleteCharAt(newColumns.length()-1);
		
		String currentTable = TablesPanel.getCurrentTable();
		boolean noOrginalColumns = false;
		ResultSet rs;
		StringBuilder sql = new StringBuilder();
		try {
			rs = DBManager.getConnection()
					.prepareStatement("select "+originColumns+" from "+currentTable/*+" limit 10"*/).executeQuery();
			while(rs.next()){
				sql.append("INSERT INTO "+currentTable+"("+newColumns);
				if(newColumns.equals("")){
					noOrginalColumns = true;
				}
				for(String extraColumnName:extraRows.keySet()){
					if(!noOrginalColumns){
						sql.append(",").append(extraColumnName);
					}else{
						sql.append(extraColumnName).append(",");
					}
				}
				if(noOrginalColumns){
					sql.deleteCharAt(sql.length()-1);
				}
				sql.append(") values(");
				int column = 0;
				boolean needQuotation = false;//是否需要加引号
				while(column<types.size()){
					column++;
					if(!types.get(column-1).contains("int")&&!types.get(column-1).contains("bit")&&!types.get(column-1).contains("Number")){
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
				for(String extraColumnName:extraRows.keySet()){
					sql.append(extraRows.get(extraColumnName)).append(",");
				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(");\n");
			}
			
			System.out.println(sql);
			Tools.writeFile("/"+currentTable+".sql", sql.toString(),false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void show(){
		table.setRowHeight(30);
		DefaultTableCellRenderer render = new DefaultTableCellRenderer();
	    render.setHorizontalAlignment(SwingConstants.CENTER);
		//table.setIntercellSpacing(new Dimension(10,0));
		JScrollPane jsc = new JScrollPane(table);
		jsc.setBounds(0, 0, 440, 300);
		add(jsc);
		registerRightMenu(jsc,table);
	}
	
	public void registerRightMenu(JComponent...components){
		for(final JComponent component:components){
			component.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e){
					if(e.getButton()==3){
						showPopupMenu(component,e);
					}
				}
			});
		}
		
	}
	
	public void showPopupMenu(JComponent c,MouseEvent e){
		JPopupMenu popupMenu = new JPopupMenu();
		ImageIcon addIcon = new ImageIcon(Tools.loadImage("add.png"));
		ImageIcon deleteIcon = new ImageIcon(Tools.loadImage("delete.png"));
		JMenuItem addRow = new JMenuItem("增加一行",addIcon);
		JMenuItem deleteRow = new JMenuItem("删除行",deleteIcon);
		JMenuItem addMultiRows = new JMenuItem("增加多行",addIcon);
		popupMenu.add(addRow);
		popupMenu.add(addMultiRows);
		popupMenu.addSeparator();
		popupMenu.add(addMultiRows);
		popupMenu.add(deleteRow);
		
		final Vector<String> vec = new Vector<String>();
		vec.add("#Custom#");
		vec.add("别名");
		vec.add("String");
		addRow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.addRow(vec);
			}
		});
		deleteRow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] rows=table.getSelectedRows();
				for(int rowIndex=rows.length-1;rowIndex>=0;rowIndex--){
					model.removeRow(rowIndex);
				}
			}
		});
		addMultiRows.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String addRowCountString = JOptionPane.showInputDialog(table, "请输入要添加的行数:", 1);
				int addRowCount = Integer.parseInt(addRowCountString);
				for(int i=0;i<addRowCount;i++){
					model.addRow(vec);
				}
			}
		});
		popupMenu.show(c,e.getX(), e.getY());
	}
	public  synchronized  void renderData(String tableName){
		ResultSet rs;
		rowData.clear();
		try {
			rs = DBManager.getConnection()
					.prepareStatement("SELECT column_name , data_type , character_maximum_length , numeric_precision , numeric_scale, is_nullable , column_default, column_comment" +
							" FROM Information_schema.columns WHERE table_Name='"+tableName+"' and table_schema='"+DBManager.getDbName()+"'").executeQuery();
			while(rs.next()){
				Vector<String> vec = new Vector<String>();
				vec.add(rs.getString(1));
				vec.add(rs.getString(1));
				vec.add(rs.getString(2));
				rowData.add(vec);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void reRenderData(String tableName){
		renderData(tableName);
		model.setDataVector(rowData, columnNames);
		model.fireTableDataChanged(); 
		this.validate();
		this.repaint();
	}
	
	public void exportConfig(){
		StringBuilder originColumns = new StringBuilder(),newColumns = new StringBuilder(),types  = new StringBuilder();
		for(int row :table.getSelectedRows()){
			String temp = model.getValueAt(row, 0).toString();
			if(temp.matches("^\\[.+\\]$")){
				temp = "\""+temp.substring(1,temp.length()-2)+"\"";
			}
			originColumns.append(temp+",");
			newColumns.append(model.getValueAt(row, 1)+",");
			types.append(model.getValueAt(row, 2).toString()+",");
		}
		originColumns.deleteCharAt(originColumns.length()-1);
		newColumns.deleteCharAt(newColumns.length()-1);
		types.deleteCharAt(types.length()-1);
		String currentTable = TablesPanel.getCurrentTable();
		StringBuilder config = new StringBuilder();
		config.append(currentTable+".originColumns="+originColumns+"\n");
		config.append(currentTable+".newColumns="+newColumns+"\n");
		config.append(currentTable+".types="+types+"\n");
		config.append(currentTable+".priority="+(ExportTable.PRIORITY--)+"\n\n");
		Tools.writeFile("/exportConfig", config.toString(),true);
		
	}
	
	private String removeBracket(String src){
		if(src.startsWith("[")&&src.endsWith("]")){
			return src.substring(1,src.length()-2);
		}
		return src;
	}
	
}
