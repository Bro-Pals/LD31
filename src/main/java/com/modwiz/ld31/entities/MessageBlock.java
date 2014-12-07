package com.modwiz.ld31.entities;

import com.modwiz.ld31.world.Dimension;

public class MessageBlock extends GameBlock {
	
	private String[] messages;
	
	/**
	 * Make a block containing messages
	 * @param messages The messages in this block that will be seen when the 
			player is colliding with MessageBlock.
	 */
	public MessageBlock(Dimension parent, float x, float y, float w, float h, String[] msgs, String imageString) {
		super(parent, x, y, w, h, true);
		if (imageString!=null && !imageString.equals("null")) {
			setImageString(imageString);
		}
		setCanCollide(false);
		messages = msgs;
	}
	
	public void onCollide(GameObject other) {
		if (other instanceof Player) {
			((Player)other).setMessages(messages);
		}
	}
	
	public void setMessages(String[] messages) {
		this.messages = messages;
	}
	
	public String[] getMessages() {
		return messages;
	}
	
	@Override
	public Object clone() {
		MessageBlock t =  new MessageBlock(null, getX(), getY(), getWidth(), getHeight(), getMessages(), getImageForString());
		t.setName(getName());
		if (getImageForString()!=null) {
			t.setImageString(getImageForString());
		}
		return t;
	}
}