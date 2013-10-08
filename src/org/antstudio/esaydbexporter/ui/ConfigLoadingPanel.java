package org.antstudio.esaydbexporter.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.antstudio.esaydbexporter.Constants;
import org.antstudio.esaydbexporter.listener.ButtonActionListener;

/**
 * @author Gavin
 * @Date 2013-8-19 œ¬ŒÁ5:21:15
 */
public class ConfigLoadingPanel extends JPanel{

	private static final long serialVersionUID = -3139873577446363640L;

	private JButton loadingBtn;
	public ConfigLoadingPanel(){
		init();
		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy=0;
		add(loadingBtn,gbc);
	}
	
	public void init(){
		loadingBtn = new JButton("º”‘ÿ≈‰÷√");
		loadingBtn.setName(Constants.LOADINGCONFIGBTN);
		loadingBtn.addActionListener(new ButtonActionListener());
	}
}
