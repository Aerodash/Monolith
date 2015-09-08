package com.aerodash.monolith.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.aerodash.monolith.entities.Building;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class GameObjects {
	
	private static int nextId = 0;
	private static HashMap<Integer, GameObject> objects = new HashMap<>();

	public static void addObject(GameObject ob){
		ob.id = nextId;
		objects.put(nextId, ob);
		nextId++;
	}
	
	public static GameObject getObject(int id){
		return objects.get(id);
	}
	
	public static void removeObject(int id){
		objects.get(id).toBeRemoved = true;
		if (objects.get(id).getClass().getSuperclass().equals(Building.class)){//remove all its tiles if its a building
			Building b = (Building) objects.get(id);
			for (Tile t : b.getShape().tiles){
				t.toBeRemoved = true;
			}
		}
	}
	
	//grid pos
	public static boolean isObjectAt(float gridX, float gridY){
		for (GameObject o : objects.values()){
			if (o.getGridX() == gridX && o.getGridY() == gridY){
				return true;
			}
		}
		return false;
	}
	
	public static GameObject objectAt(float gridX, float gridY){
		for (GameObject o : objects.values()){
			if (o.getGridX() == gridX && o.getGridY() == gridY){
				return o;
			}
		}
		return null;
	}
	
	public static Class<?> getObjectTypeAt(float gridX, float gridY){
		for (GameObject o : objects.values()){
			if (o.getGridX() == gridX && o.getGridY() == gridY){
				
				if (o.getClass().equals(Tile.class)){//if its a tile return the class of the building it belongs to
					
					Tile t = (Tile) o;
					if (t.shape != null)
						return t.shape.getBuilding().getClass();
					
				}
				
				return o.getClass();
			}
		}
		return null;
	}
	
	public static boolean isObjectOfType(float gridX, float gridY, Class clazz){
		Class c = getObjectTypeAt(gridX, gridY);
		if (c == null || !clazz.equals(c)) return false;
		return true;
	}
	
	public static int getCount(){
		return objects.size();
	}
	
	public static void render(SpriteBatch sb){
		for (GameObject o : objects.values()){
			//tiles must not be updated else updated multiple times
			//objects scheduled for removal must not be rendered
			//the selected building must be rendered last so it always overlaps all the buildings
			//the selected building after built must be rendered last so it always overlaps all the buildings
			//and is able to be rendered
			if (!o.getClass().equals(Tile.class) && !o.toBeRemoved && !o.equals(Building.selectedBuilding) && !o.equals(Building.selectedAfterBuiltBuilding) && o.doRender)
				o.render(sb);
		}
		if (Building.selectedBuilding != null)
			Building.selectedBuilding.render(sb);
		if (Building.selectedAfterBuiltBuilding != null)
			Building.selectedAfterBuiltBuilding.render(sb);
	}
	
	public static void update(float delta){
		//set the selectedAfterBuiltBuilding
		boolean buildingFound = false;
		for (GameObject o : objects.values()){
			if (o.getClass().getSuperclass().equals(Building.class)){
				Building b = (Building) o;
				if (b.selectedAfterBuilt) {
					buildingFound = true;
					Building.selectedAfterBuiltBuilding = b;
					break;
				}
			}
		}
		if (!buildingFound){
			Building.selectedAfterBuiltBuilding = null;
		}
		
		//update all objects
		Iterator<GameObject> it = objects.values().iterator();
		while(it.hasNext()){
			GameObject o = it.next();
			if (!o.toBeRemoved){
				if (o.doUpdate)
					o.update(delta);
			}else{
				it.remove();//if its to be removed remove it => can only remove with the iterator or will get a concurrentModificationException
			}
		}
	}
	
	public static String stringify(){
		StringBuilder sb = new StringBuilder();
		
		for (Entry<Integer, GameObject> o : objects.entrySet()){
			sb.append(o.getKey() + " => " + o.getValue().getClass());
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public static boolean collides(GameObject obj){
		if (obj.getClass().getSuperclass().equals(Building.class)){//if its a building check collision of all its tiles
			Building b = (Building) obj;
			Array<Tile> btiles = b.getShape().tiles;
			for (GameObject o : objects.values()){
				if (o.getClass().getSuperclass().equals(Building.class)){
					Building b1 = (Building) o;
					if (b.collides(b1) && !b.equals(b1)) return true;
				}
			}
			
		}else{//else only check that specific gameobject (Tile)
			if (obj.getClass().equals(Tile.class)){
				Tile t = (Tile) obj;
				for (GameObject o : objects.values()){
					if (o.getClass().equals(Tile.class)){
						Tile t1 = (Tile) o;
						if (t.collides(t1) && !t.equals(t1)) return true;
					}
				}
			}
		}
		return false;
	}
	
}
