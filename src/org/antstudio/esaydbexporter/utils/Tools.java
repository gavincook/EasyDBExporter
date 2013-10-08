package org.antstudio.esaydbexporter.utils;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

/**
 * 工具类
 * @author Gavin
 *
 */
public class Tools {

	public static Point getCenterPotion(int width,int height){
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		return new Point((int)((screensize.getWidth()-width)/2),((int)(screensize.getHeight()-height)/2));
	}
	
	/**
	 * 
	 * @param path
	 * @param content
	 * @param append 是否追加内容
	 */
	public static void writeFile(String path,String content,boolean append){
		try{
			String jarPath  = Tools.class.getProtectionDomain().getCodeSource().getLocation().getFile();
			System.out.println("jar File path(before decode):"+jarPath);
			jarPath = URLDecoder.decode(jarPath,"UTF-8");
			System.out.println("jar File path(after decode):"+jarPath);
			File jar = new File(jarPath);
			File sqlFolder =  new File(jar.getParent()+"/sql");
			if(!sqlFolder.exists()){
				sqlFolder.mkdir();
			}
			File f = new File(sqlFolder.getAbsolutePath()+  path);
			
			if(!f.exists()){
				f.createNewFile();
			}
			FileInputStream in = new FileInputStream(f);
			StringBuilder oldContent = new StringBuilder();
			if(append){
				byte[] tempData = new byte[10240];
				Integer length = -1;
				System.out.println(in.available());
				while((length=in.read(tempData))!=-1){
					oldContent.append(new String(tempData,0,length));
				}
			}
			in.close();
			FileOutputStream out = new FileOutputStream(f);
			out.write(oldContent.toString().getBytes());
			byte[] data = content.getBytes();
			out.write(data);
			out.close(); 
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Properties getConfig() throws FileNotFoundException, IOException{
		Properties p = new Properties();
		File sqlFolder =  new File(getJarPath()+"sql");
		File configFile = new File(sqlFolder+"/exportConfig");
		FileInputStream in = new FileInputStream(configFile);
		p.load(in);
		in.close();
		return p;
	}
	
	public static String getJarPath() throws UnsupportedEncodingException{
		String jarPath  = Tools.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		jarPath = URLDecoder.decode(jarPath,"UTF-8");
		File jar = new File(jarPath);
		return jar.getParent()+"/";
	}
	public static void saveConifg(String key,String value,Boolean append,String configPath) {
		try{
			Properties p = new Properties();
			File configFile = new File(getJarPath()+configPath);
			if(!configFile.exists()){
				configFile.createNewFile();
			}
			FileInputStream in = new FileInputStream(configFile);
			p.load(in);
			in.close();
			if(p.containsKey(key)&&append){
				String oldValue = p.get(key)+",";
				if(!oldValue.contains(value+",")){
					p.put(key,p.get(key)+","+value);
				}
			}else{
				p.put(key,value);
			}
			FileOutputStream out = new FileOutputStream(configFile);
			for(Object v:p.keySet()){
				out.write((v+"="+p.getProperty(v.toString())+"\n").getBytes());
			}
			out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] loadImage(String imageName){
		try{
			File icon = new File(getJarPath()+imageName);
			FileInputStream in = new FileInputStream(icon);
			byte[] data = new byte[in.available()];
			in.read(data);
			return data;
		}catch (Exception e) {
			e.printStackTrace();
			return new byte[0];
		}
	}
	public static void main(String[] args) throws FileNotFoundException, IOException {
		saveConifg("testKey","testValue",true,"D://i.ini");
	}
}
