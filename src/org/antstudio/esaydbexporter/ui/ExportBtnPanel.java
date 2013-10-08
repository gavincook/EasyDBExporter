package org.antstudio.esaydbexporter.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.antstudio.esaydbexporter.Constants;
import org.antstudio.esaydbexporter.listener.ButtonActionListener;

/**
 * @author Gavin
 * @Date 2013-8-13 上午11:25:27
 */
public class ExportBtnPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2153908977799044141L;

	private JButton export,exportConfig;
	public ExportBtnPanel(){
		setLayout(new GridBagLayout());
		initComponent();
		GridBagConstraints gbc=new GridBagConstraints();  
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx=1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(export,gbc);
		gbc.gridx = 1;
		add(exportConfig,gbc);
		setVisible(true);
	}
	
	private void initComponent(){
		export = new JButton("导出");
		export.setName(Constants.EXPORT);
		exportConfig = new JButton("导出配置");
		exportConfig.setName(Constants.EXPORTCONFIG);
		export.addActionListener(new ButtonActionListener());
		exportConfig.addActionListener(new ButtonActionListener());
	}
}
