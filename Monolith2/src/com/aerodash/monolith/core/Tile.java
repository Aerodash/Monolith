package com.aerodash.monolith.core;

import com.aerodash.monolith.core.shapes.TetrisShape;
import com.aerodash.monolith.entities.Building;
import com.aerodash.monolith.entities.buildings.Corridor;
import com.aerodash.monolith.main.Monolith;
import com.aerodash.monolith.utils.Assets;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Tile extends GameObject {

	private float size;
	public TetrisShape shape;
	public boolean hasResource = false;
	public boolean collided = false;
	public boolean outlined = false;
	public boolean isNode = false;
	public int nodeId = -1;
	public Color lightColor;

	public Tile(float x, float y, Color color, TetrisShape shape) {
		super(x * Monolith.tileSize, y * Monolith.tileSize, Monolith.tileSize, Monolith.tileSize, color, true, true);
		this.size = Monolith.tileSize;
		this.shape = shape;
		lightColor = new Color(color.r * .75f, color.g * .75f, color.b * .75f, color.a * .75f);
	}

	@Override
	public void update(float delta) {
		if (isJustClicked()){
			System.out.println("Top : " + top());
			System.out.println("Bot : " + bot());
			System.out.println("Left : " + left());
			System.out.println("Right : " + right());
		}
	}

	@Override
	public void render(SpriteBatch sb) {

		sb.begin();

		sb.setColor(color);
		if (collided)
			sb.setColor(new Color(1, .2f, .2f, .5f));

		if (outlined)
			sb.draw(Assets.tileOutlined, x, y, width, height);
		else if (shape.getBuilding().selected)
			sb.draw(Assets.outline, x, y, width, height);
		else if (shape.getBuilding().selectedAfterBuilt) {
			sb.draw(Assets.tile, x, y, width, height);
			sb.setColor(lightColor);
			sb.draw(Assets.outline, x - 3, y - 3, width + 6, height + 6);
		} else
			sb.draw(Assets.tile, x, y, width, height);

		if (isNode) {
			Assets.font.setColor(Color.WHITE);
			Assets.layout.setText(Assets.font, Integer.toString(nodeId));
			Assets.font.draw(sb, Integer.toString(nodeId), x + Monolith.tileSize / 2 - Assets.layout.width / 2,
					y + Monolith.tileSize / 2 + Assets.layout.height / 2);
		}

		sb.end();

	}

	public void renderForUI(Batch b, float scale) {

		b.setColor(color);
		if (collided)
			b.setColor(new Color(1, .2f, .2f, .5f));

		if (outlined)
			b.draw(Assets.tileOutlined, x / Monolith.tileSize, y / Monolith.tileSize, 0, 0, width, height, scale, scale,
					0);
		else
			b.draw(Assets.tile, x / Monolith.tileSize, y / Monolith.tileSize, 0, 0, width, height, scale, scale, 0);

	}

	@Override
	public void setX(float x) {
		this.x = x * size;
	}

	public void setY(float y) {
		this.y = y * size;
	}

	@Override
	public void setPos(float x, float y) {
		setX(x);
		setY(y);
	}

	@Override
	public void setPos(Vector2 pos) {
		setX(pos.x);
		setY(pos.y);
	}

	public float getGridX() {
		return x / size;
	}

	public float getGridY() {
		return y / size;
	}

	public boolean collides(Tile tile) {
		return getBounds().overlaps(tile.getBounds());
	}

	public boolean contains(float x, float y) {
		return getBounds().contains(x, y);
	}

	public boolean contains(Vector2 pos) {
		return contains(pos.x, pos.y);
	}

	public Array<Vector2> getSurrounding4TilesPoses() {
		Array<Vector2> poses = new Array<Vector2>();
		float x = getGridX();
		float y = getGridY();

		poses.add(new Vector2(x, y + 1));
		poses.add(new Vector2(x + 1, y));
		poses.add(new Vector2(x, y - 1));
		poses.add(new Vector2(x - 1, y));

		return poses;
	}

	public Array<Vector2> getSurrounding8TilesPoses() {
		Array<Vector2> poses = new Array<Vector2>();
		float x = getGridX();
		float y = getGridY();

		poses.add(new Vector2(x, y + 1));
		poses.add(new Vector2(x + 1, y + 1));
		poses.add(new Vector2(x + 1, y));
		poses.add(new Vector2(x + 1, y - 1));
		poses.add(new Vector2(x, y - 1));
		poses.add(new Vector2(x - 1, y - 1));
		poses.add(new Vector2(x - 1, y));
		poses.add(new Vector2(x - 1, y + 1));

		return poses;
	}

	public boolean isTopCorridor() {
		float x = getGridX();
		float y = getGridY();
		return GameObjects.isObjectOfType(x, y + 1, Corridor.class)
				&& !shape.tiles.contains((Tile) GameObjects.objectAt(x, y + 1), false);
	}

	public boolean isBotCorridor() {
		float x = getGridX();
		float y = getGridY();
		return GameObjects.isObjectOfType(x, y - 1, Corridor.class)
				&& !shape.tiles.contains((Tile) GameObjects.objectAt(x, y - 1), false);
	}

	public boolean isLeftCorridor() {
		float x = getGridX();
		float y = getGridY();
		return GameObjects.isObjectOfType(x - 1, y, Corridor.class)
				&& !shape.tiles.contains((Tile) GameObjects.objectAt(x - 1, y), false);
	}

	public boolean isRightCorridor() {
		float x = getGridX();
		float y = getGridY();
		return GameObjects.isObjectOfType(x + 1, y, Corridor.class)
				&& !shape.tiles.contains((Tile) GameObjects.objectAt(x + 1, y), false);
	}

	public GameObject top() {
		float x = getGridX();
		float y = getGridY();
		GameObject obj = GameObjects.objectAt(x, y + 1);
		if (obj != null && ((obj.getClass().equals(Tile.class) && ((Tile)obj).shape.getBuilding().getClass().equals(Corridor.class)) || (obj.getClass().equals(Corridor.class)))){
			if (obj.getClass().equals(Corridor.class)){
				Corridor c = (Corridor) obj;
				for (Tile t : c.getShape().tiles){
					if (t.getGridX() == c.getGridX() && t.getGridY() == c.getGridY()){
						return t;
					}
				}
			}
			return obj;
		}
		return null;
	}

	public GameObject bot() {
		float x = getGridX();
		float y = getGridY();
		GameObject obj = GameObjects.objectAt(x, y - 1);
		if (obj != null && ((obj.getClass().equals(Tile.class) && ((Tile)obj).shape.getBuilding().getClass().equals(Corridor.class)) || (obj.getClass().equals(Corridor.class)))){
			if (obj.getClass().equals(Corridor.class)){
				Corridor c = (Corridor) obj;
				for (Tile t : c.getShape().tiles){
					if (t.getGridX() == c.getGridX() && t.getGridY() == c.getGridY()){
						return t;
					}
				}
			}
			return obj;
		}
		return null;
	}

	public GameObject left() {
		float x = getGridX();
		float y = getGridY();
		GameObject obj = GameObjects.objectAt(x - 1, y);
		if (obj != null && ((obj.getClass().equals(Tile.class) && ((Tile)obj).shape.getBuilding().getClass().equals(Corridor.class)) || (obj.getClass().equals(Corridor.class)))){
			if (obj.getClass().equals(Corridor.class)){
				Corridor c = (Corridor) obj;
				for (Tile t : c.getShape().tiles){
					if (t.getGridX() == c.getGridX() && t.getGridY() == c.getGridY()){
						return t;
					}
				}
			}
			return obj;
		}
		return null;
	}

	public GameObject right() {
		float x = getGridX();
		float y = getGridY();
		GameObject obj = GameObjects.objectAt(x + 1, y);
		if (obj != null && ((obj.getClass().equals(Tile.class) && ((Tile)obj).shape.getBuilding().getClass().equals(Corridor.class)) || (obj.getClass().equals(Corridor.class)))){
			if (obj.getClass().equals(Corridor.class)){
				Corridor c = (Corridor) obj;
				for (Tile t : c.getShape().tiles){
					if (t.getGridX() == c.getGridX() && t.getGridY() == c.getGridY()){
						return t;
					}
				}
			}
			return obj;
		}
		return null;
	}

	@Override
	public String toString() {
		return "[" + getGridX() + ", " + getGridY() + "]";
	}
}
