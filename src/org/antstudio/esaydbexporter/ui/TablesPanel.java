package org.antstudio.esaydbexporter.ui;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import org.antstudio.esaydbexporter.domain.TableModel;
import org.antstudio.esaydbexporter.listener.MouseEventAdapter;

/**
 * 
 * @author Gavin
 *
 */
public class TablesPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6744348589500670372L;

	JTable table ;
	Vector rowData,columnNames;
	TableModel model;
	public TablesPanel(){
		
	}
	public TablesPanel(Vector rowData,Vector columnNames){
		setLayout(null);
		model = new TableModel(rowData,columnNames);
		table = new JTable(model); 
		table.setRowHeight(30);
		DefaultTableCellRenderer render = new DefaultTableCellRenderer();
	    render.setHorizontalAlignment(SwingConstants.CENTER);
		table.setIntercellSpacing(new Dimension(10,0));
		JScrollPane jsc = new JScrollPane(table);
		jsc.setBounds(0, 0, 300, 300);
		add(jsc);
	}
	
	public void setMouseEventAdapter(MouseEventAdapter med){
		table.addMouseListener(med);
	}
	
	public JTable getTable(){
		return table;
	}
	
	public void reRenderData(Vector<String> rowData,Vector<String> columnNames){
		model.setDataVector(rowData, columnNames);
		model.fireTableDataChanged(); 
		this.validate();
		this.repaint();
	}
	
	private static String currentTable;

	public static String getCurrentTable() {
		return currentTable;
	}
	public static void setCurrentTable(String currentTable) {
		 TablesPanel.currentTable = currentTable;
	}
}
