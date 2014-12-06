package com.modwiz.ld31.entities;

import horsentpmath.Vector2;


public class Weapon {
	
	private Creature holder;
	private double range, damage;
	private int attackCooldown, attackMaxCooldown;
	
	public Weapon(Creature hldr, double rg, double dm, int attackCd) {
		this.holder = hldr;
		this.range = rg;
		this.damage = dm;
		this.attackMaxCooldown = attackCd;
		this.attackCooldown = 0;
	}
	
	/**
	 * Use the weapon at the target point
	 * @param direction The direction the weapon is used in
	 */
	public void use(Vector2 direction) {
		if (holder != null) {
			if (attackCooldown < 0) {
				attackCooldown = attackMaxCooldown;
				// use the weapon in the direction holder is facing
			}
		}
	}
	
	public void decreaseCooldown() {
		attackCooldown--;
	}
	
	public double getRange() {
		return range;
	}
	
	public double getDamage() {
		return damage;
	}
}