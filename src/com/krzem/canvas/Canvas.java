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



public class Canvas{
	public static void main(String[] args){
		Canvas c=new Canvas(){
			public void update(){
				if (this.pressed(27)){
					this.quit();
				}
			}
			public void draw(){
				this.stroke(0);
				this.strokeWeight(50);
				this.line(0,0,MOUSE_X,MOUSE_Y);
			}
		};
		c.run();
	}



	public static final int DISPLAY_ID=0;
	public static final GraphicsDevice SCREEN=GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[DISPLAY_ID];
	public static final Rectangle WINDOW_SIZE=SCREEN.getDefaultConfiguration().getBounds();
	public static final int MAX_FPS=60;
	public double FPS=1;
	public int MOUSE=0;
	public int MOUSE_COUNT=0;
	public int MOUSE_BUTTON=0;
	public int MOUSE_X;
	public int MOUSE_Y;
	public int SCROLL_D=0;
	private JFrame frame;
	private JComponent canvas;
	private Graphics2D _g;
	private Map<String,Object> _cfg=new HashMap<String,Object>();
	private int _mouse;
	private int _mouseC;
	private int _mouseB;
	private MouseEvent _mouseM;
	private int _sc;
	private Map<Integer,Integer> _kl=new HashMap<Integer,Integer>();
	private boolean _klc=true;
	private boolean _ru=false;
	private boolean _break=false;



	public Canvas(){
		Canvas cls=this;
		this.frame=new JFrame("TITLE");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setUndecorated(true);
		this.frame.setResizable(false);
		this.frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				cls._quit();
			}
		});
		SCREEN.setFullScreenWindow(this.frame);
		this.canvas=new JComponent(){
			public void paintComponent(Graphics g){
				cls._draw(g);
			}



			public void addNotify(){
				super.addNotify();
				this.requestFocus();
			}
		};
		this.canvas.setSize(WINDOW_SIZE.width,WINDOW_SIZE.height);
		this.canvas.setPreferredSize(new Dimension(WINDOW_SIZE.width,WINDOW_SIZE.height));
		this.canvas.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				cls._mouse=1;
				cls._mouseC=e.getClickCount();
				cls._mouseB=e.getButton();
			}
			public void mouseReleased(MouseEvent e){
				cls._mouse=2;
				cls._mouseC=e.getClickCount();
				cls._mouseB=e.getButton();
			}
			public void mouseClicked(MouseEvent e){
				cls._mouse=3;
				cls._mouseC=e.getClickCount();
				cls._mouseB=e.getButton();
			}
		});
		this.canvas.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseMoved(MouseEvent e){
				cls._mouseM=e;
			}
			public void mouseDragged(MouseEvent e){
				cls._mouseM=e;
			}
		});
		this.canvas.addMouseWheelListener(new MouseWheelListener(){
			public void mouseWheelMoved(MouseWheelEvent e){
				if (e.getWheelRotation()<0){
					cls._sc=1;
				}
				else{
					cls._sc=-1;
				}
			}
		});
		this.canvas.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e){
				cls._k_down(e);
			}
			public void keyReleased(KeyEvent e){
				cls._k_up(e);
			}
			public void keyTyped(KeyEvent e){
				cls._k_press(e);
			}
		});
		this.frame.setContentPane(this.canvas);
		this.canvas.requestFocus();
	}



	public final void run(){
		if (this._ru==true){
			return;
		}
		this._ru=true;
		Canvas cls=this;
		new Thread(new Runnable(){
			@Override
			public void run(){
				while (cls._break==false){
					Long s=System.currentTimeMillis();
					try{
						cls._update();
						cls.canvas.repaint();
					}
					catch (Exception e){
						e.printStackTrace();
					}
					Long d=System.currentTimeMillis()-s;
					if (d==0){
						d=1L;
					}
					if ((double)Math.floor(1/(double)d*1e8)/1e5>cls.MAX_FPS){
						try{
							Thread.sleep((long)(1/(double)cls.MAX_FPS*1e3)-d);
						}
						catch (InterruptedException e){}
					}
					cls.FPS=(double)Math.floor(1/(double)(System.currentTimeMillis()-s)*1e8)/1e5;
				}
			}
		}).start();
	}



	public void update(){

	}



	public void draw(){

	}



	public final boolean pressed(int k){
		if (this._kl.get(k)==null){
			return false;
		}
		return (this._kl.get(k)>0);
	}



	public final void stroke(Object... a){
		this._cfg.put("stroke",this._color(a));
		this._cfg.put("strokeB",true);
	}



	public final void strokeWeight(int w){
		this._cfg.put("strokeW",w);
	}



	public final void noStroke(){
		this._cfg.put("strokeB",false);
	}



	public final void fill(Object... a){
		this._cfg.put("fill",this._color(a));
		this._cfg.put("fillB",true);
	}



	public final void noFill(){
		this._cfg.put("fillB",false);
	}



	public final void line(int ax,int ay,int bx,int by){
		if (this._stroke()==true){
			this._g.drawLine(ax,ay,bx,by);
		}
	}



	public final void quit(){
		this._quit();
	}



	private void _quit(){
		if (this._break==true){
			return;
		}
		this._break=true;
		this.frame.dispose();
		this.frame.dispatchEvent(new WindowEvent(this.frame,WindowEvent.WINDOW_CLOSING));
	}



	private void _update(){
		this.MOUSE=this._mouse+0;
		this.MOUSE_COUNT=this._mouseC+0;
		this.MOUSE_BUTTON=this._mouseB+0;
		if (this._mouse!=1){
			this._mouse=0;
			this._mouseC=0;
			this._mouseB=0;
		}
		if (this._mouseM!=null){
			this.MOUSE_X=this._mouseM.getPoint().x;
			this.MOUSE_Y=this._mouseM.getPoint().y;
			this._mouseM=null;
		}
		this.SCROLL_D=this._sc+0;
		this._sc=0;
		this.update();
		this._klc=false;
		int[] r=new int[this._kl.size()];
		int idx=0;
		for (int i:this._kl.keySet()){
			if (this._kl.get(i)==2){
				r[idx]=i;
				idx++;
			}
		}
		for (int j=0;j<idx;j++){
			this._kl.remove(r[j]);
		}
		this._klc=true;
	}



	private void _draw(Graphics g){
		this._default_cfg();
		this._g=(Graphics2D)g;
		this._g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		this._g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		this._g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		this._g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		this._g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		this._g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		this.draw();
		this._g=null;
		this._cfg.clear();
	}



	private void _default_cfg(){
		this._cfg.put("x",0);
		this._cfg.put("y",0);
		this._cfg.put("stroke",this._color(new Object[]{0,0,0}));
		this._cfg.put("strokeW",1);
		this._cfg.put("strokeB",true);
		this._cfg.put("fill",this._color(new Object[]{255,255,255}));
		this._cfg.put("fillB",true);
	}



	private int[] _color(Object[] a){
		int[] c=new int[]{255,255,255,255};
		if (a.length==1&&a[0].getClass().getName().equals("java.awt.Color")){
			Color co=(Color)a[0];
			c=new int[]{co.getRed(),co.getGreen(),co.getBlue(),co.getAlpha()};
		}
		else if (a.length==1&&a[0].getClass().getName().equals("java.lang.Integer")){
			c=new int[]{(int)a[0],(int)a[0],(int)a[0],255};
		}
		else if (a.length==2&&a[0].getClass().getName().equals("java.lang.Integer")&&a[1].getClass().getName().equals("java.lang.Integer")){
			c=new int[]{(int)a[0],(int)a[0],(int)a[0],(int)a[1]};
		}
		else if (a.length==3&&a[0].getClass().getName().equals("java.lang.Integer")&&a[1].getClass().getName().equals("java.lang.Integer")&&a[2].getClass().getName().equals("java.lang.Integer")){
			c=new int[]{(int)a[0],(int)a[1],(int)a[2],255};
		}
		else if (a.length==4&&a[0].getClass().getName().equals("java.lang.Integer")&&a[1].getClass().getName().equals("java.lang.Integer")&&a[2].getClass().getName().equals("java.lang.Integer")&&a[3].getClass().getName().equals("java.lang.Integer")){
			c=new int[]{(int)a[0],(int)a[1],(int)a[2],(int)a[3]};
		}
		else{
			throw new IllegalArgumentException("Invalid color");
		}
		return c;
	}



	private boolean _stroke(){
		if ((boolean)this._cfg.get("strokeB")==false){
			return false;
		}
		int[] c=(int[])this._cfg.get("stroke");
		this._g.setColor(new Color(c[0],c[1],c[2],c[3]));
		this._g.setStroke(new BasicStroke((int)this._cfg.get("strokeW")));
		return true;
	}



	private void _k_down(KeyEvent e){
		while (this._klc==false){

		}
		this._kl.put(e.getKeyCode(),1);
	}



	private void _k_up(KeyEvent e){
		while (this._klc==false){

		}
		this._kl.remove(e.getKeyCode());
	}



	private void _k_press(KeyEvent e){
		while (this._klc==false){

		}
		this._kl.put(e.getKeyCode(),2);
	}
}