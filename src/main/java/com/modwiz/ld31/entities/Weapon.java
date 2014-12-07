package com.modwiz.ld31.entities;

import horsentpmath.Vector2;


public class Weapon {
	
	private Creature holder;
	private double range, damage;
	private int attackCooldown, attackMaxCooldown;
	
	/**
	 * Creates a new Weapon
	 * @param hldr The holder of the weapon
	 * @param rg The range of the weapon
	 * @param dm The damage of the weapon
	 * @param attackCd The attack cooldown in frames
	 */
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
				float startPosX = (float)(holder.getX() + (holder.getWidth() / 2) + (direction.getX() * 30));
				float startPosY = (float)(holder.getY() + (holder.getHeight() / 2) + (direction.getY() * 30));
				// make a projectile
				Projectile projectile = new Projectile(holder, holder.getParent(), startPosX, startPosY, 16, 16, 
					null, null, (float)range);
				projectile.getVelocity().set(0, direction.getX() * 10);
				projectile.getVelocity().set(1, direction.getY() * 10);
				holder.getParent().getObjects().add(projectile);
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