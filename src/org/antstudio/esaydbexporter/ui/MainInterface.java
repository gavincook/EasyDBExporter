package org.antstudio.esaydbexporter.ui;

import javax.swing.JFrame;

import org.antstudio.esaydbexporter.utils.Tools;

/**
 * Ö÷½çÃæ
 * @author gavin
 *
 */
public class MainInterface extends JFrame{
	
	private static final long serialVersionUID = -3019500582772820540L;

	public MainInterface(){
		super("EasyDBExporter (by gavin)");
		setLayout(null);
		add(new DBSettingPanel(800));
		setLocation(Tools.getCenterPotion(800, 530));
		setSize(800,530);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
