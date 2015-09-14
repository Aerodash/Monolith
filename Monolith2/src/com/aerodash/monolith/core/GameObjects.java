package com.aerodash.monolith.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.aerodash.monolith.entities.Building;
import com.aerodash.monolith.entities.Minion;
import com.aerodash.monolith.main.Monolith;
import com.aerodash.monolith.utils.Assets;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class GameObjects {

	private static int nextId = 0;
	private static HashMap<Integer, GameObject> objects = new HashMap<>();
	private static HashMap<Integer, GameObject> minions = new HashMap<>();
	private static HashMap<Vector2, Integer> nbMinionsAtPos = new HashMap<>();

	public static void addObject(GameObject ob) {
		ob.id = nextId;
		if (ob.getClass().equals(Minion.class))
			minions.put(nextId, ob);
		else
			objects.put(nextId, ob);
		nextId++;
	}

	public static GameObject getObject(int id) {
		return objects.get(id);
	}

	public static GameObject getMinion(int id) {
		return minions.get(id);
	}

	public static void removeObject(int id) {
		objects.get(id).toBeRemoved = true;
		if (objects.get(id).getClass().getSuperclass().equals(Building.class)) {// remove
																				// all
																				// its
																				// tiles
																				// if
																				// its
																				// a
																				// building
			Building b = (Building) objects.get(id);
			for (Tile t : b.getShape().tiles) {
				t.toBeRemoved = true;
			}
		}
	}

	public static void removeMinion(int id) {
		minions.get(id).toBeRemoved = true;
	}

	// grid pos
	public static boolean isObjectAt(float gridX, float gridY) {
		for (GameObject o : objects.values()) {
			if (o.getGridX() == gridX && o.getGridY() == gridY) {
				return true;
			}
		}
		return false;
	}

	public static boolean isMinionAt(float gridX, float gridY) {
		for (GameObject o : minions.values()) {
			if (o.getGridX() == gridX && o.getGridY() == gridY) {
				return true;
			}
		}
		return false;
	}

	public static GameObject objectAt(float gridX, float gridY) {
		for (GameObject o : objects.values()) {
			if (o.getGridX() == gridX && o.getGridY() == gridY) {
				return o;
			}
		}
		return null;
	}

	public static GameObject minionAt(float gridX, float gridY) {
		for (GameObject o : minions.values()) {
			if (o.getGridX() == gridX && o.getGridY() == gridY) {
				return o;
			}
		}
		return null;
	}

	public static Class<?> getObjectTypeAt(float gridX, float gridY) {
		for (GameObject o : objects.values()) {
			if (o.getGridX() == gridX && o.getGridY() == gridY) {

				if (o.getClass().equals(Tile.class)) {// if its a tile return
														// the class of the
														// building it belongs
														// to

					Tile t = (Tile) o;
					if (t.shape != null)
						return t.shape.getBuilding().getClass();

				}

				return o.getClass();
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isObjectOfType(float gridX, float gridY, Class clazz) {
		Class c = getObjectTypeAt(gridX, gridY);
		if (c == null || !clazz.equals(c))
			return false;
		return true;
	}

	public static int getCount() {
		return objects.size() + minions.size();
	}

	public static int getMinionCount() {
		return minions.size();
	}

	public static void render(SpriteBatch sb) {
		for (GameObject o : objects.values()) {
			// tiles must not be updated else updated multiple times
			// objects scheduled for removal must not be rendered
			// the selected building must be rendered last so it always overlaps
			// all the buildings
			// the selected building after built must be rendered last so it
			//the selected building before built must be rendered last so it
			// always overlaps all the buildings
			// and is able to be rendered
			// minions must be rendered last they are on top of everything
			if (!o.getClass().equals(Tile.class) && !o.toBeRemoved && !o.equals(Building.selectedBuilding)
					&& !o.equals(Building.selectedAfterBuiltBuilding) && !o.equals(Building.selectedBeforeBuiltBuilding) 
					&& o.doRender
					&& !o.getClass().equals(Minion.class))
				o.render(sb);
		}
		if (Building.selectedBuilding != null)
			Building.selectedBuilding.render(sb);
		if (Building.selectedAfterBuiltBuilding != null)
			Building.selectedAfterBuiltBuilding.render(sb);
		if (Building.selectedBeforeBuiltBuilding != null)
			Building.selectedBeforeBuiltBuilding.render(sb);

		for (GameObject o : minions.values()) {
			Vector2 pos = new Vector2(o.getGridX(), o.getGridY());
			if (nbMinionsAtPos.get(pos) == 1) {
				o.render(sb);
			} else {// if many minions only draw one
				sb.begin();
				Assets.font.setColor(Color.WHITE);
				Assets.layout.setText(Assets.font, Integer.toString(nbMinionsAtPos.get(pos)));
				Assets.font.draw(sb, Integer.toString(nbMinionsAtPos.get(pos)),
						o.getX() + Monolith.tileSize / 2 - Assets.layout.width / 2,
						o.getY() + Monolith.tileSize / 2 + Assets.layout.height / 2);
				sb.end();
			}
		}
	}

	public static void update(float delta) {
		// set the selectedAfterBuiltBuilding
		boolean buildingFound = false;
		for (GameObject o : objects.values()) {
			if (o.getClass().getSuperclass().equals(Building.class)) {
				Building b = (Building) o;
				if (b.selectedAfterBuilt) {
					buildingFound = true;
					Building.selectedAfterBuiltBuilding = b;
					break;
				}
			}
		}
		if (!buildingFound) {
			Building.selectedAfterBuiltBuilding = null;
		}

		// set the selectedBeforeBuiltBuilding
		buildingFound = false;
		for (GameObject o : objects.values()) {
			if (o.getClass().getSuperclass().equals(Building.class)) {
				Building b = (Building) o;
				if (b.selectedBeforeBuilt) {
					buildingFound = true;
					System.out.println("FOUND ******************************************************");
					Building.selectedBeforeBuiltBuilding = b;
					break;
				}
			}
		}
		if (!buildingFound) {
			Building.selectedBeforeBuiltBuilding = null;
		}

		// update all objects
		Iterator<GameObject> it = objects.values().iterator();
		while (it.hasNext()) {
			GameObject o = it.next();
			if (!o.toBeRemoved) {
				if (o.doUpdate)
					o.update(delta);
			} else {
				it.remove();// if its to be removed remove it => can only remove
							// with the iterator or will get a
							// concurrentModificationException
			}
		}

		// update all minions
		it = minions.values().iterator();
		while (it.hasNext()) {
			GameObject o = it.next();
			if (!o.toBeRemoved) {
				if (o.doUpdate)
					o.update(delta);
			} else {
				it.remove();// if its to be removed remove it => can only remove
							// with the iterator or will get a
							// concurrentModificationException
			}
		}

		// get number of minions at each minionPositions
		it = minions.values().iterator();
		nbMinionsAtPos.clear();
		while (it.hasNext()) {
			GameObject o = it.next();
			Vector2 pos = new Vector2(o.getGridX(), o.getGridY());
			if (nbMinionsAtPos.containsKey(pos)) {
				nbMinionsAtPos.put(pos, nbMinionsAtPos.get(pos).intValue() + 1);
			} else {
				nbMinionsAtPos.put(pos, 1);
			}
		}

	}

	public static String stringify() {
		StringBuilder sb = new StringBuilder();
		sb.append("Objects : \n");
		for (Entry<Integer, GameObject> o : objects.entrySet()) {
			sb.append(o.getKey() + " => " + o.getValue().getClass());
			sb.append("\n");
		}
		sb.append("Minions : \n");
		for (Entry<Integer, GameObject> o : minions.entrySet()) {
			sb.append(o.getKey() + " => " + o.getValue().getClass());
			sb.append("\n");
		}

		return sb.toString();
	}

	public static boolean collides(GameObject obj) {
		if (obj.getClass().getSuperclass().equals(Building.class)) {// if its a
																	// building
																	// check
																	// collision
																	// of all
																	// its tiles
			Building b = (Building) obj;
			for (GameObject o : objects.values()) {
				if (o.getClass().getSuperclass().equals(Building.class)) {
					Building b1 = (Building) o;
					if (b.collides(b1) && !b.equals(b1))
						return true;
				}
			}

		} else {// else only check that specific gameobject (Tile)
			if (obj.getClass().equals(Tile.class)) {
				Tile t = (Tile) obj;
				for (GameObject o : objects.values()) {
					if (o.getClass().equals(Tile.class)) {
						Tile t1 = (Tile) o;
						if (t.collides(t1) && !t.equals(t1))
							return true;
					}
				}
			}
		}
		return false;
	}

}
