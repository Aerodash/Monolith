package com.aerodash.monolith.core;

import com.aerodash.monolith.exception.CostException;

public class Cost {

	public static final Cost free = new Cost();
	public static final Cost starter = new Cost(15, 10, 8);
	
	public static final Cost corridor = new Cost(1, 1, 0);
	public static final Cost extractor =  new Cost(3, 2, 0);
	public static final Cost reactor = new Cost(2, 6, 0);
	public static final Cost garden = new Cost(1, 3, 1);
	public static final Cost kitchen = new Cost(2, 3, 3);
	public static final Cost weapons = new Cost(2, 2, 2);
	public static final Cost quarters = new Cost(6, 4, 4);
	
	public int particles;
	public int energy;
	public int food;
	
	private Cost(int particles, int energy, int food) {
		this.particles = particles;
		this.energy = energy;
		this.food = food;
	}

	private Cost() {
		particles = 0;
		energy = 0;
		food = 0;
	}
	
	public void plus(int particles, int energy, int food){
		this.particles += particles;
		this.energy += energy;
		this.food += food;
	}
	
	public void plus(Cost cost){
		plus(cost.particles, cost.energy, cost.food);
	}
	
	public void minus(int particles, int energy, int food) throws CostException{
		if (this.particles < particles || this.energy < energy || this.food < food) throw new CostException();
		this.particles -= particles;
		this.energy -= energy;
		this.food -= food;
	}
	
	public void minus(Cost cost) throws CostException{
		minus(cost.particles, cost.energy, cost.food);
	}
	
}
