package org.antstudio.esaydbexporter.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.antstudio.esaydbexporter.Constants;
import org.antstudio.esaydbexporter.listener.ButtonActionListener;
import org.antstudio.esaydbexporter.utils.Tools;

/**
 * 数据库信息设置界面
 * @author Gavin
 *
 */
public class DBSettingPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1756645394766270061L;
	JLabel dbPathLabel,dbUserLabel,dbPasswordLabel;
	JButton connect;
	JComboBox dbPath;
	JTextField dbUser;
	JPasswordField dbPassword;
	GridBagLayout layout = new GridBagLayout(); 
	public DBSettingPanel(int width){
		initComponent();
		setBounds(0, 0, width-20, 100);
	}
	
	public void initComponent(){
		this.setLayout(layout);
		dbPathLabel = new JLabel("数据库路径:");
		dbUserLabel = new JLabel("用户名:");
		dbPasswordLabel = new JLabel("密码:");
		dbPath = new JComboBox(getDbPathData());
		dbPath.setEditable(true);
		dbUser = new JTextField("root");
		dbPassword = new JPasswordField("123456");
		connect = new JButton("连接");
		
		GridBagConstraints gbc=new GridBagConstraints();  
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.weightx=1;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		add(dbPathLabel,gbc);
		
		gbc.gridx = 1;
		gbc.gridwidth = 6;
		add(dbPath,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		add(dbUserLabel,gbc);
		
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		add(dbUser,gbc);
		
		gbc.weightx=0;
		gbc.gridx = 3;
		gbc.gridwidth = 1;
		add(dbPasswordLabel,gbc);
		
		gbc.weightx=1;
		gbc.gridx = 4;
		gbc.gridwidth = 2;
		add(dbPassword,gbc);
		
		gbc.gridx = 6;
		gbc.gridwidth = 1;
		add(connect,gbc);
		connect.setName(Constants.CONNECT);
		connect.addActionListener(new ButtonActionListener(dbPath,dbUser,dbPassword));
		
	}
	
	private String[] getDbPathData(){
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(new File(Tools.getJarPath()+Constants.CONFIGFILENAME)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(p.containsKey("dbpath")){
			String[] dbPaths = p.get("dbpath").toString().split(",");
			return dbPaths;
		}
		return new String[1];
	}
}
