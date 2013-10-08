package org.antstudio.esaydbexporter.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.antstudio.esaydbexporter.Constants;
import org.antstudio.esaydbexporter.container.ComponentContainer;
import org.antstudio.esaydbexporter.container.action.ExportAction;
import org.antstudio.esaydbexporter.db.DBManager;
import org.antstudio.esaydbexporter.ui.ConfigLoadingPanel;
import org.antstudio.esaydbexporter.ui.ExportBtnPanel;
import org.antstudio.esaydbexporter.ui.TableColumnsPanel;
import org.antstudio.esaydbexporter.ui.TablesPanel;
import org.antstudio.esaydbexporter.utils.Tools;

/**
 * 
 * @author Gavin
 * 按钮事件处理类
 */
public class ButtonActionListener implements ActionListener{

	private Object[] params;
	
	public ButtonActionListener(Object...params){
		this.params = params;
	}
	
	@SuppressWarnings("serial")
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton button = (JButton) event.getSource();
		Component c = button;
		String name = button.getName();
		final JFrame frame;
		if(Constants.CONNECT.equals(name)){//连接数据库，并展示数据表
			JComboBox dbPath = (JComboBox) params[0];
			JTextField dbUser = (JTextField) params[1];
			JPasswordField dbPassword = (JPasswordField) params[2];
			Tools.saveConifg("dbpath", dbPath.getSelectedItem().toString(), true, Constants.CONFIGFILENAME);
			DBManager.connect(dbPath.getSelectedItem().toString(), dbUser.getText(), new String(dbPassword.getPassword()));
			try {
				final ResultSet rs = DBManager.getConnection().prepareStatement("show tables;").executeQuery();
				Vector rowData = new Vector<Vector<String>>(),columnNames=new Vector<String>(){{add("Tables");}};
				while(rs.next()){
					rowData.add(new Vector<String>(){{add(rs.getString(1));}});
				}
				while(!(c instanceof JFrame)){
					c = c.getParent();
				}
				frame = (JFrame) c;
				JPanel tempPanel = (JPanel) ComponentContainer.getComponent("panelContainer");
				if(tempPanel==null){
					tempPanel = new JPanel();
				}
				final JPanel panel = tempPanel;
				TablesPanel tablePanel = (TablesPanel) ComponentContainer.getComponent("tablesPanel");
				if(tablePanel==null){
					tablePanel = new TablesPanel(rowData, columnNames);
					final JTable table = tablePanel.getTable();
					tablePanel.setMouseEventAdapter(new MouseEventAdapter(){
						@Override
						public void mouseDoubleClicked(MouseEvent e) {
							TableColumnsPanel columnsPanel = (TableColumnsPanel) ComponentContainer.getComponent("tableColumnsPanel");
							table.changeSelection(table.rowAtPoint(e.getPoint()), 0, false, false);
							TablesPanel.setCurrentTable(table.getValueAt(table.rowAtPoint(e.getPoint()),0).toString());
							if(columnsPanel!=null){
								columnsPanel.reRenderData(table.getValueAt(table.rowAtPoint(e.getPoint()),0).toString());
								return;
							}
							columnsPanel = new TableColumnsPanel(table.getValueAt(table.rowAtPoint(e.getPoint()),0).toString());
							columnsPanel.setBounds(330, 0, 440, 300);
							ComponentContainer.addComponent("tableColumnsPanel", columnsPanel);
							
							ExportBtnPanel btnPanel = (ExportBtnPanel) ComponentContainer.getComponent("btnPanel");
							if(btnPanel==null){
								btnPanel = new ExportBtnPanel();
							}
							btnPanel.setBounds(10, 310, 750, 30);
							ComponentContainer.addComponent("btnPanel",btnPanel);
							panel.add(columnsPanel);
							panel.add(btnPanel);
							panel.validate();
							panel.repaint();
						}
					});
					panel.setLayout(null);
					tablePanel.setBounds(10, 0, 300, 300);
					panel.setBounds(0, 0, 600, 300);
					panel.add(tablePanel);
					
				}else{
					tablePanel.reRenderData(rowData, columnNames);
					return ;
				}
				ComponentContainer.addComponent("tablesPanel", tablePanel);
				ComponentContainer.addComponent("panelContainer", panel);
				 
				JTabbedPane tab = new JTabbedPane();
				tab.setBounds(0, 110,780, 380);
				tab.add("数据表", panel);
				tab.add("配置文件加载", new ConfigLoadingPanel());
				frame.add(tab);
				frame.validate();
				frame.repaint();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if(Constants.EXPORT.equals(name)){
			TableColumnsPanel columnsPanel = (TableColumnsPanel) ComponentContainer.getComponent("tableColumnsPanel");
			columnsPanel.getSelected();
		}else if(Constants.EXPORTCONFIG.equals(name)){
			TableColumnsPanel columnsPanel = (TableColumnsPanel) ComponentContainer.getComponent("tableColumnsPanel");
			columnsPanel.exportConfig();
		}else if(Constants.LOADINGCONFIGBTN.equals(name)){
			try {
				new ExportAction().exportByConfig(Tools.getConfig());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog( (Component) event.getSource(), "未找到配置文件.");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog( (Component) event.getSource(), "加载配置出错,请检查配置文件.");
				e.printStackTrace();
			}
		}
	}
}
