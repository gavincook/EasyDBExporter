package org.antstudio.esaydbexporter.container;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

/**
 * @author Gavin
 * @Date 2013-7-30 ÏÂÎç6:06:53
 * ×é¼şÈİÆ÷
 */
public class ComponentContainer {

	private static Map<String,JComponent> components = new HashMap<String,JComponent>();
	
	public static void  addComponent(String name,JComponent component){
		components.put(name, component);
	}
	
	public static JComponent getComponent(String name){
		return components.get(name);
	}
}
