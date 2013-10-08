package org.antstudio.esaydbexporter.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Timer;

/**
 * @author Gavin
 * @Date 2013-7-30 下午2:53:31
 */
public  class MouseEventAdapter extends MouseAdapter{

	  private int clickNum=0;//用来判断是否该执行双击事件
	  private boolean flag = false;//是否执行了双击事件
	 public void mouseClicked(MouseEvent e) {
		 final MouseEvent event = e;
		  if (this.clickNum == 2) {
	         this.mouseDoubleClicked(e); 
	         this.clickNum=1;//默认点击一次
	         flag = true;
	         return;
		  }
		 Timer timer = new Timer();
		 timer.schedule(new java.util.TimerTask(){
			 int times = 0;
			@Override
			public void run() {
				if(flag){
					clickNum = 1;
					this.cancel();
					flag = false;
					return ;
				}
				if(times==1){
					mouseSingleClicked(event);
					this.cancel();
					clickNum = 1;
					return;
				}
				clickNum++; 
				times++;
		 }},new Date(),200); 
	 }
	 
	 /**
	  * 单击事件
	  * @param e
	  */
	 public void mouseSingleClicked(MouseEvent e){}
	 
	 /**
	  * 双击事件
	  * @param e
	  */
	 public void mouseDoubleClicked(MouseEvent e){}
	
}
