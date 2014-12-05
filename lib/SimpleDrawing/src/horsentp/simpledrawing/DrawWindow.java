package horsentp.simpledrawing;

import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.awt.Insets;
import java.util.ArrayList;

/**
	The DrawWindow object is to be a single, unified object that
	can be drawn on, and report user input.
	</p>
	<p>
	Should simplify game-making so that drawing and input should
	be setup by calling the constructor only.
	</p>
	<p>
	DrawWindow requires at least a 1 millisecond delay between
	input queue polling.
	</p>
*/
public class DrawWindow {
	
	private BufferStrategy bs;
	private ArrayList<MouseEvent> mousePressedQueue;
	private ArrayList<MouseEvent> mouseReleasedQueue;
	private ArrayList<KeyEvent> keyPressedQueue;
	private ArrayList<KeyEvent> keyReleasedQueue;
	private Frame frame;
	private final int offsetX;
	private final int offsetY;
	private final GraphicsDevice gd;
	private boolean exists, closing;
	
	/**
		Creates a non-resizeable DrawWindow that can report user input and be drawn on.
		@param title the title of the window
		@param drawingRegionWidth the width in pixels of the drawing region
		@param drawingRegionHeight the height in pixels of the drawing region
		@param fullscreen whether or not the DrawWindow should be in fullscreen.
	*/
	public DrawWindow(String title, int drawingRegionWidth, int drawingRegionHeight, boolean fullscreen) {
		bs = null;
		mousePressedQueue = new ArrayList<MouseEvent>();
		mouseReleasedQueue = new ArrayList<MouseEvent>();
		keyPressedQueue = new ArrayList<KeyEvent>();
		keyReleasedQueue = new ArrayList<KeyEvent>();
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gd = ge.getDefaultScreenDevice();
		DisplayMode dm = gd.getDisplayMode();
		frame = new Frame(title);
		frame.setIgnoreRepaint(true);
		frame.setResizable(false);
		if (fullscreen) {
			frame.setUndecorated(true);
			DisplayMode mode = new DisplayMode(drawingRegionWidth, drawingRegionHeight, dm.getBitDepth(), dm.getRefreshRate());
			gd.setFullScreenWindow(frame);
			gd.setDisplayMode(mode);
			offsetX = 0;
			offsetY = 0;
		} else {
			frame.setVisible(true);
			Insets i = frame.getInsets();
			frame.setSize(
				drawingRegionWidth+i.left+i.right,
				drawingRegionHeight+i.top+i.bottom
			);
			offsetX = i.left;
			offsetY = i.top;
			frame.setLocationRelativeTo(null);
		}
		frame.setFocusable(true);
		frame.addMouseListener(
			new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					e.translatePoint(offsetX, offsetY);
					addMousePressed(e);
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					e.translatePoint(offsetX, offsetY);
					addMouseReleased(e);
				}
			}
		);
		frame.addKeyListener(
			new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					addKeyPressed(e);
				}
				@Override
				public void keyReleased(KeyEvent e) {
					addKeyReleased(e);
				}
			}
		);
		frame.addWindowListener(
			new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					closing = true;
					destroy();
				}
				@Override
				public void windowClosed(WindowEvent e) {
					exists = false;
					System.out.println("DrawWindow: The window has been closed.");
				}
			}
		);
		closing = false;
		exists = true;
	}
	
	/*
		Methods to add key events
	*/
	protected void addMousePressed(MouseEvent e) { 
		mousePressedQueue.add(e); 
	}
	protected void addMouseReleased(MouseEvent e) { mouseReleasedQueue.add(e); }
	protected void addKeyPressed(KeyEvent e) { keyPressedQueue.add(e); }
	protected void addKeyReleased(KeyEvent e) { keyReleasedQueue.add(e); }
	
	/**
		Gets the mouse position (after adding the window inset offset).
	*/
	public Point getMousePosition() {
		if (!closing) {
			Point p = frame.getMousePosition();
			if (p!=null) {
				p.x += offsetX;
				p.y += offsetY;
				return p;
			} else {
				return null;
			}
		}
		return null;
	}
	
	public void destroy() {
		mousePressedQueue.clear();
		mouseReleasedQueue.clear();
		keyPressedQueue.clear();
		keyReleasedQueue.clear();
		//Remove all the listeners
		if (gd.getFullScreenWindow()!=null) {
			gd.setFullScreenWindow(null);
		}
		frame.dispose();
		if (bs!=null) {
			bs.dispose();
			bs = null;
		}
	}
	
	public boolean exists() {
		return exists;
	}
	
	/**
		Get the Graphics object to draw to this DrawWindow.
		@return the graphics context of this object's window.
	*/
	public Graphics getDrawGraphics() {
		if (!closing) {
			if (bs==null) {
				createBuffer();
			}
			Graphics g =  bs.getDrawGraphics();
			g.translate(offsetX, offsetY);
			return g;
		} else {
			return null;
		}
	}
	
	/**
		Updates the window to show the graphics there where just
		drawn to it.
		@param g the graphics context obtained through {@link #getDrawGraphics() getDrawGraphics}.
	*/ 
	public void showBuffer(Graphics g) {
		g.dispose();
		bs.show();
	}
	
	/**
		Get the next mouse pressed event.
		@return the next mouse pressed event, or null if there is none.
	*/
	public MouseEvent nextMousePressedEvent() {
		if (!mousePressedQueue.isEmpty()) {
			MouseEvent me = mousePressedQueue.get(0);
			mousePressedQueue.remove(me);
			//System.out.println(mousePressedQueue.size() + " mouse pressed events left in queue");
			return me;
		} else {
			return null;
		}
	}
	
	/**
		Get the next mouse released event.
		@return the next mouse released event, or null if there is none.
	*/
	public MouseEvent nextMouseReleasedEvent() {
		if (!mouseReleasedQueue.isEmpty()) {
			MouseEvent me = mouseReleasedQueue.get(0);
			mouseReleasedQueue.remove(me);
			//System.out.println(mouseReleasedQueue.size() + " mouse released events left in queue");
			return me;
		} else {
			return null;
		}
	}
	
	/**
		Get the next key pressed event.
		@return the next key pressed event, or null if there is none.
	*/
	public KeyEvent nextKeyPressedEvent() {
		if (!keyPressedQueue.isEmpty()) {
			KeyEvent k = keyPressedQueue.get(0);
			keyPressedQueue.remove(k);
			//System.out.println(keyPressedQueue.size() + " key pressed events left in queue");
			return k;
		} else {
			return null;
		}
	}
	
	/**
		Get the next key released event.
		@return the next key released event, or null if there is none.
	*/
	public KeyEvent nextKeyReleasedEvent() {
		if (!keyReleasedQueue.isEmpty()) {
			KeyEvent k = keyReleasedQueue.get(0);
			keyReleasedQueue.remove(k);
			//System.out.println(keyReleasedQueue.size() + " key released events left in queue");
			return k;
		} else {
			return null;
		}
	}
	
	/**	
		Creates the buffer strategy if the window.
	*/
	private void createBuffer() {
		if (bs!=null) {
			bs.dispose();
		}
		if (!closing) {
			frame.createBufferStrategy(2);
			bs = frame.getBufferStrategy();
		}
	}
	
	/**
		Gives more flexibility to the user by letting them add events and changing the frame directly.
	*/
	public Frame getRawFrame() {
		return frame;
	}
}