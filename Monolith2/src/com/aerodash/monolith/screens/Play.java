package com.aerodash.monolith.screens;

import com.aerodash.monolith.core.Cost;
import com.aerodash.monolith.core.GameObjects;
import com.aerodash.monolith.core.shapes.TetrisShape;
import com.aerodash.monolith.core.shapes.TetrisShape.InitialState;
import com.aerodash.monolith.core.shapes.TetrisShape.TypeState;
import com.aerodash.monolith.entities.buildings.Corridor;
import com.aerodash.monolith.entities.buildings.Extractor;
import com.aerodash.monolith.entities.buildings.Gardens;
import com.aerodash.monolith.entities.buildings.Kitchen;
import com.aerodash.monolith.entities.buildings.Quarters;
import com.aerodash.monolith.entities.buildings.Reactor;
import com.aerodash.monolith.entities.buildings.Stock;
import com.aerodash.monolith.entities.buildings.Weapons;
import com.aerodash.monolith.main.Monolith;
import com.aerodash.monolith.ui.BuildingLabel;
import com.aerodash.monolith.ui.ExpandingLabel;
import com.aerodash.monolith.ui.LabelListener;
import com.aerodash.monolith.ui.NextShapePanel;
import com.aerodash.monolith.utils.Assets;
import com.aerodash.monolith.utils.Colors;
import com.aerodash.monolith.utils.Debug;
import com.aerodash.monolith.utils.MyInputProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Play extends ScreenAdapter{
	
	public static Cost resRemaining = Cost.starter;
	private int width;
	private int height;
	private ShapeRenderer sr;
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private Viewport viewport;
	private static Vector3 mouse;
	private static Vector3 unprojectedMouse;
	private Stage hudStage;
	
	public static NextShapePanel panel;
	public static ExpandingLabel expLabel;
	public static boolean switchToNextShape = false;
	
	public Play(){
		hudStage = new Stage();
		initHud();
		
		width = Monolith.width;
		height = Monolith.height;
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, width, height);
		viewport = new FitViewport(width, height, cam);
		sb.setProjectionMatrix(cam.combined);
		sr.setProjectionMatrix(cam.combined);
		unprojectedMouse = new Vector3();
		mouse = new Vector3();
		Gdx.input.setInputProcessor(new MyInputProcessor(cam));
		setupInitialMap();
	}
	
	@Override
	public void show() {
	}
	
	@Override
	public void render(float delta) {
		sb.setProjectionMatrix(cam.combined);
		Debug.setProjectionMatrix(cam.combined);
		//update mouse pos
		unprojectedMouse.x = Gdx.input.getX();
		unprojectedMouse.y = Gdx.input.getY();

		mouse = cam.unproject(unprojectedMouse);
		
		GameObjects.update(delta);
		GameObjects.render(sb);
		
		hudStage.act(delta);
		hudStage.draw();
		
		//temp enemy wave timer (add to ui package)
		sr.begin(ShapeType.Filled);
		
		sr.setColor(Colors.waveBarBgColor);
		sr.rect(0, 48, Monolith.width - panel.getWidth(), 10);
		sr.setColor(Colors.waveBarColor);
		sr.rect(0, 48, Monolith.width - panel.getWidth() - 150, 10);
		
		sr.end();
		
		sb.begin();
		Assets.smallFont.setColor(new Color(0.91f, 0.21f, 0.21f, 1f));
		Assets.smallFont.draw(sb, "Time to next wave (2 enemies) :", 5, 75);
		sb.end();
	}
	Corridor c; 
	Stock stock, stock2, stock3;
	private void setupInitialMap(){
		c = new Corridor(5, 5, new TypeState(InitialState.ONE, TetrisShape.Type.Tile));
		stock = new Stock(5, 6);
		stock2 = new Stock(5, 4);
		stock3 = new Stock(4, 5);
		c.built = stock2.built = stock3.built = stock.built = true;
		c.selectable = stock.selectable = stock2.selectable = stock3.selectable = false;
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void dispose() {
		sb.dispose();
		sr.dispose();
		hudStage.dispose();
		Assets.dispose();
	}
	
	public static Vector2 getMouse(){
		return new Vector2(mouse.x, mouse.y);
	}

	public static Vector2 getGridMouse(){
		return new Vector2((int)(mouse.x / Monolith.tileSize), ((int)mouse.y / Monolith.tileSize));
	}
	
	private void initHud() {
		hudStage.setDebugAll(true);
		
		BuildingLabel corridor = new BuildingLabel(0, 0, "Corridor", Colors.corridor, Cost.corridor);
		corridor.setListener(new LabelListener(){

			@Override
			public void onClick() {
				new Corridor(getGridMouse().x, getGridMouse().y, panel.getCurrentShapeTypeState()).setSelected(true);
			}
			
		});
		hudStage.addActor(corridor);
		BuildingLabel extractor = new BuildingLabel(corridor.getX() + corridor.getWidth(), 0, "Extractor", Colors.extractor, Cost.extractor);
		extractor.setListener(new LabelListener(){

			@Override
			public void onClick() {
				new Extractor(getGridMouse().x, getGridMouse().y, panel.getCurrentShapeTypeState()).setSelected(true);
			}
			
		});
		hudStage.addActor(extractor);
		BuildingLabel reactor = new BuildingLabel(extractor.getX() + extractor.getWidth(), 0, "Reactor", Colors.reactor, Cost.reactor);
		reactor.setListener(new LabelListener(){

			@Override
			public void onClick() {
				new Reactor(getGridMouse().x, getGridMouse().y, panel.getCurrentShapeTypeState()).setSelected(true);
			}
			
		});
		hudStage.addActor(reactor);
		BuildingLabel gardens = new BuildingLabel(reactor.getX() + reactor.getWidth(), 0, "Gardens", Colors.garden, Cost.garden);
		gardens.setListener(new LabelListener(){

			@Override
			public void onClick() {
				new Gardens(getGridMouse().x, getGridMouse().y, panel.getCurrentShapeTypeState()).setSelected(true);
			}
			
		});
		hudStage.addActor(gardens);
		BuildingLabel kitchen = new BuildingLabel(gardens.getX() + gardens.getWidth(), 0, "Kitchen", Colors.kitchen, Cost.kitchen);
		kitchen.setListener(new LabelListener(){

			@Override
			public void onClick() {
				new Kitchen(getGridMouse().x, getGridMouse().y, panel.getCurrentShapeTypeState()).setSelected(true);
			}
			
		});
		hudStage.addActor(kitchen);
		BuildingLabel weapons = new BuildingLabel(kitchen.getX() + kitchen.getWidth(), 0, "Weapons", Colors.weapons, Cost.weapons);
		weapons.setListener(new LabelListener(){

			@Override
			public void onClick() {
				new Weapons(getGridMouse().x, getGridMouse().y, panel.getCurrentShapeTypeState()).setSelected(true);
			}
			
		});
		hudStage.addActor(weapons);
		BuildingLabel quarters = new BuildingLabel(weapons.getX() + weapons.getWidth(), 0, "Quarters", Colors.quarters, Cost.quarters);
		quarters.setListener(new LabelListener(){

			@Override
			public void onClick() {
				new Quarters(getGridMouse().x, getGridMouse().y, panel.getCurrentShapeTypeState()).setSelected(true);
			}
			
		});
		hudStage.addActor(quarters);
		
		BuildingLabel resLabel = new BuildingLabel(0, Monolith.height - 28, "", Color.WHITE, resRemaining);
		resLabel.setHeight(28);
		resLabel.setWidth(Monolith.width / 8 + 15);
		hudStage.addActor(resLabel);
		
		panel = new NextShapePanel(quarters.getX() + quarters.getWidth(), 0);
		hudStage.addActor(panel);
		
		expLabel = new ExpandingLabel(resLabel.getX() + resLabel.getWidth(), resLabel.getY() + resLabel.getHeight() / 2 - 22 / 2);
		hudStage.addActor(expLabel);
		
	}
	
}
