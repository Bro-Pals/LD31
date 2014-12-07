package com.modwiz.ld31.entities;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import com.modwiz.ld31.world.Dimension;

/**
 * A simple entity for placing text into the world.
 */
public class TextBlock extends GameBlock {
	
	private String textLabel;
	private int fontSize;
	private Font font;
	
	/**
	 * Represents a {@link GameObject} with a width and height
	 * @param parent The dimension for this object to be loaded into
	 * @param x The initial x position of this GameBlock
	 * @param y The initial y position of this GameBlock
	 * @param w The width of this GameBlock
	 * @param h The height of this GameBlock
	 * @param textLabel the text that this block displays
	 * @param fontSize the size of this TextBlock's font
	 * @see com.modwiz.ld31.entities.GameObject
	 */
	public TextBlock(Dimension parent, float x, float y, float w, float h, String textLabel, int fontSize) {
		super(parent, x, y, w, h);
		setStaticBlock(true);
		setCanCollide(false);
		this.textLabel = textLabel;
		setFontSize(fontSize);
	}
	
	public void setTextLabel(String textLabel) {
		this.textLabel = textLabel;
	}
	
	public String getTextLabel() {
		return textLabel;
	}
	
	public int getFontSize() {
		return fontSize;
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		font = new Font(Font.SANS_SERIF, Font.PLAIN, this.fontSize);
	}
	
	@Override
	public void update() {  }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(Graphics g, float camX, float camY) {
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawString(getTextLabel(), (int)(getX()-camX), (int)((getY()-camY)+(getHeight()/3)));
	}
	
	@Override
	public Object clone() {
		TextBlock t =  new TextBlock(null, getX(), getY(), getWidth(), getHeight(), getTextLabel(), getFontSize());
		t.setName(getName());
		return t;
	}
}