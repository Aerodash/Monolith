package com.aerodash.monolith.utils;

import com.aerodash.monolith.entities.Building;
import com.aerodash.monolith.main.Monolith;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class MyInputProcessor implements InputProcessor {
	private OrthographicCamera cam;
	private static final float CAMERA_ZOOM_SPEED = 2.0f;
	private static final float CAMERA_SPEED = 300f;
	private int dragX, dragY;

	public MyInputProcessor(OrthographicCamera cam) {
		this.cam = cam;
	}

	@Override
	public boolean scrolled(int amount) {

		// each time you scroll rotate the selected building
		if (Building.selectedBuilding != null)
			Building.selectedBuilding.rotate();
		else {
			// Page up/down control the zoom
			cam.zoom += CAMERA_ZOOM_SPEED * Gdx.graphics.getDeltaTime() * amount;
			cam.update();
		}
		return true;
	}

	public boolean touchDragged(int x, int y, int pointer) {
		if (mouseInUi()) return false;
		
//		float dX = (float) (x - dragX) / (float) Gdx.graphics.getWidth();
//		float dY = (float) (dragY - y) / (float) Gdx.graphics.getHeight();
//		dragX = x;
//		dragY = y;
//		Vector3 v = new Vector3(-dX * CAMERA_SPEED, -dY * CAMERA_SPEED, 0f);
//		cam.position.add(v);
//		cam.update();
		
		return true;
	}

	private boolean mouseInUi() {
		if (Monolith.height - Gdx.input.getY() <= 40 && Gdx.input.getX() >= Monolith.width - 50) return true;
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		dragX = screenX;
		dragY = screenY;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

}
