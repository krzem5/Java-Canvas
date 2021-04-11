package com.krzem.canvas;



import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.Exception;
import java.lang.Math;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;



public class Main{
	public static void main(String[] args){
		Canvas c=new Canvas(){
			@Override
			public void update(){
				if (this.pressed(27)){
					this.quit();
				}
			}
			@Override
			public void draw(){
				this.stroke(0);
				this.strokeWeight(50);
				this.line(0,0,MOUSE_X,MOUSE_Y);
			}
		};
		c.run();
	}
}
