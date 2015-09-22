package com.aerodash.monolith.screens;

import com.aerodash.monolith.core.Cost;
import com.aerodash.monolith.core.GameObjects;
import com.aerodash.monolith.core.shapes.TetrisShape;
import com.aerodash.monolith.core.shapes.TetrisShape.InitialState;
import com.aerodash.monolith.core.shapes.TetrisShape.TypeState;
import com.aerodash.monolith.djikstra.Graph;
import com.aerodash.monolith.djikstra.Vertex;
import com.aerodash.monolith.entities.Building;
import com.aerodash.monolith.entities.Minion;
import com.aerodash.monolith.entities.Minion.Job;
import com.aerodash.monolith.entities.ParticleMine;
import com.aerodash.monolith.entities.Resource;
import com.aerodash.monolith.entities.Resource.Type;
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
import com.aerodash.monolith.ui.IconLabel;
import com.aerodash.monolith.ui.IconLabel.VisibilityCondition;
import com.aerodash.monolith.ui.LabelListener;
import com.aerodash.monolith.ui.NextShapePanel;
import com.aerodash.monolith.ui.ResourceBuildingBar;
import com.aerodash.monolith.utils.Assets;
import com.aerodash.monolith.utils.Colors;
import com.aerodash.monolith.utils.Debug;
import com.aerodash.monolith.utils.MyInputProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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

public class Play extends ScreenAdapter {

	public static int currentNodeId = 0;
	public static final boolean cheatMode = false;
	public static Cost resRemaining = Cost.starter;
	private int width;
	private int height;
	private ShapeRenderer sr;
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private Viewport viewport;
	private static Vector3 mouse;
	private static Vector3 unprojectedMouse;
	private static Stage hudStage;

	public static NextShapePanel panel;
	public static ExpandingLabel expLabel;
	public static boolean switchToNextShape = false;

	public Play() {
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
		configGraph();
		setupInitialMap();
	}

	private void configGraph() {
		// Lets check from location Loc_1 to Loc_10
		graph = new Graph();

		graph.addEdge("Edge_0", 0, 1, 85);
		graph.addEdge("Edge_1", 0, 2, 217);
		graph.addEdge("Edge_2", 0, 4, 173);
		graph.addEdge("Edge_3", 2, 6, 186);
		graph.addEdge("Edge_4", 2, 7, 103);
		graph.addEdge("Edge_5", 3, 7, 183);
		graph.addEdge("Edge_6", 5, 8, 250);
		graph.addEdge("Edge_7", 8, 9, 84);
		graph.addEdge("Edge_8", 7, 9, 167);
		graph.addEdge("Edge_9", 4, 9, 502);
		graph.addEdge("Edge_10", 9, 10, 40);
		graph.addEdge("Edge_11", 1, 10, 600);

		// shortest path from 0 to 10
		// DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		// dijkstra.execute(nodes.get(0));
		// LinkedList<Vertex> path = dijkstra.getPath(nodes.get(10));
		//
		// for (Vertex vertex : path) {
		// System.out.println(vertex);
		// }
	}

	public static Graph graph;

	@Override
	public void render(float delta) {
		sb.setProjectionMatrix(cam.combined);
		sr.setProjectionMatrix(cam.combined);
		Debug.setProjectionMatrix(cam.combined);
		// update mouse pos
		unprojectedMouse.x = Gdx.input.getX();
		unprojectedMouse.y = Gdx.input.getY();

		mouse = cam.unproject(unprojectedMouse);

		GameObjects.update(delta);
		GameObjects.render(sb);

		hudStage.act(delta);
		hudStage.draw();

		// graph.render(sr);

		// temp enemy wave timer (add to ui package)
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
		
		if (Gdx.input.isKeyJustPressed(Keys.ENTER)){
			m.go(1);
		}
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
			m.go(0);
		}

	}

	Corridor c;
	Stock sParticle, sEnergy, sFood;
	Minion m;
	ParticleMine mine = new ParticleMine(7, 5);
	private void setupInitialMap() {//setup initial map from Json
		c = new Corridor(5, 5, new TypeState(InitialState.ONE, TetrisShape.Type.Tile));
		c.getShape().tiles.get(0).isNode = true;
		c.getShape().tiles.get(0).nodeId = Play.currentNodeId;
		graph.addNode(new Vertex(Integer.toString(Play.currentNodeId), "NODE_" + Play.currentNodeId,
				c.getShape().tiles.get(0)));
		Play.currentNodeId++;
		Corridor c1 = new Corridor(6, 5, new TypeState(InitialState.ONE, TetrisShape.Type.S));
		sParticle = new Stock(5, 6, Resource.Type.Particle);
		sParticle.addResource(Type.Particle, Cost.starter.particles);
		sEnergy = new Stock(5, 4, Resource.Type.Energy);
		sEnergy.addResource(Type.Energy, Cost.starter.energy);
		sFood = new Stock(4, 5, Resource.Type.Food);
		sFood.addResource(Type.Food, Cost.starter.food);
		c1.built = c.built = sEnergy.built = sFood.built = sParticle.built = true;
		c1.selectable = c.selectable = sParticle.selectable = sEnergy.selectable = sFood.selectable = false;
		m = new Minion(5, 5);
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

	public static Vector2 getMouse() {
		return new Vector2(mouse.x, mouse.y);
	}

	public static Vector2 getGridMouse() {
		return new Vector2((int) (mouse.x / Monolith.tileSize), ((int) mouse.y / Monolith.tileSize));
	}

	private void initHud() {
		hudStage.setDebugAll(true);

		BuildingLabel corridor = new BuildingLabel(0, 0, "Corridor", Colors.corridor, Cost.corridor);
		corridor.setListener(new LabelListener() {

			@Override
			public void onClick() {
				new Corridor(getGridMouse().x, getGridMouse().y, panel.getCurrentShapeTypeState()).setSelected(true);
			}

		});
		hudStage.addActor(corridor);
		BuildingLabel extractor = new BuildingLabel(corridor.getX() + corridor.getWidth(), 0, "Extractor",
				Colors.extractor, Cost.extractor);
		extractor.setListener(new LabelListener() {

			@Override
			public void onClick() {
				new Extractor(getGridMouse().x, getGridMouse().y, panel.getCurrentShapeTypeState()).setSelected(true);
			}

		});
		hudStage.addActor(extractor);
		BuildingLabel reactor = new BuildingLabel(extractor.getX() + extractor.getWidth(), 0, "Reactor", Colors.reactor,
				Cost.reactor);
		reactor.setListener(new LabelListener() {

			@Override
			public void onClick() {
				new Reactor(getGridMouse().x, getGridMouse().y, panel.getCurrentShapeTypeState()).setSelected(true);
			}

		});
		hudStage.addActor(reactor);
		BuildingLabel gardens = new BuildingLabel(reactor.getX() + reactor.getWidth(), 0, "Gardens", Colors.garden,
				Cost.garden);
		gardens.setListener(new LabelListener() {

			@Override
			public void onClick() {
				new Gardens(getGridMouse().x, getGridMouse().y, panel.getCurrentShapeTypeState()).setSelected(true);
			}

		});
		hudStage.addActor(gardens);
		BuildingLabel kitchen = new BuildingLabel(gardens.getX() + gardens.getWidth(), 0, "Kitchen", Colors.kitchen,
				Cost.kitchen);
		kitchen.setListener(new LabelListener() {

			@Override
			public void onClick() {
				new Kitchen(getGridMouse().x, getGridMouse().y, panel.getCurrentShapeTypeState()).setSelected(true);
			}

		});
		hudStage.addActor(kitchen);
		BuildingLabel weapons = new BuildingLabel(kitchen.getX() + kitchen.getWidth(), 0, "Weapons", Colors.weapons,
				Cost.weapons);
		weapons.setListener(new LabelListener() {

			@Override
			public void onClick() {
				new Weapons(getGridMouse().x, getGridMouse().y, panel.getCurrentShapeTypeState()).setSelected(true);
			}

		});
		hudStage.addActor(weapons);
		BuildingLabel quarters = new BuildingLabel(weapons.getX() + weapons.getWidth(), 0, "Quarters", Colors.quarters,
				Cost.quarters);
		quarters.setListener(new LabelListener() {

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

		expLabel = new ExpandingLabel(resLabel.getX() + resLabel.getWidth(),
				resLabel.getY() + resLabel.getHeight() / 2 - 22 / 2);
		hudStage.addActor(expLabel);

		IconLabel refund = new IconLabel(320, 75, "Refund (75% of the price)", Assets.x, Colors.waveBarColor);
		refund.setListener(new LabelListener() {

			@Override
			public void onClick() {
				// refund 75% of cost
				Cost reducedCost = Building.selectedAfterBuiltBuilding.getCost().reduceTo75percent();
				resRemaining.plus(reducedCost);
				Building.selectedAfterBuiltBuilding.dispose();
			}

		});
		refund.setVisibilityCondition(new VisibilityCondition() {

			@Override
			public boolean isVisible() {
				return Building.selectedAfterBuiltBuilding != null;
			}

		});
		hudStage.addActor(refund);

		IconLabel cancel = new IconLabel(320, 75, "Cancel", Assets.x, Colors.waveBarColor);
		cancel.setListener(new LabelListener() {

			@Override
			public void onClick() {
				System.out.println("CANCELING");
				Building.selectedBeforeBuiltBuilding.dispose();
			}

		});
		cancel.setVisibilityCondition(new VisibilityCondition() {
			
			@Override
			public boolean isVisible() {
				return Building.selectedBeforeBuiltBuilding != null;
			}
		});
		hudStage.addActor(cancel);

	}

	public static void show(String text, float duration) {
		float x = expLabel.getX();
		float y = expLabel.getY();
		expLabel.remove();
		expLabel = new ExpandingLabel(x, y);
		hudStage.addActor(expLabel);
		expLabel.show(text, duration);
	}

	public static void warn(String text, float duration) {
		float x = expLabel.getX();
		float y = expLabel.getY();
		expLabel.remove();
		expLabel = new ExpandingLabel(x, y);
		hudStage.addActor(expLabel);
		expLabel.warn(text, duration);
	}

	public static void info(String text, float duration) {
		float x = expLabel.getX();
		float y = expLabel.getY();
		expLabel.remove();
		expLabel = new ExpandingLabel(x, y);
		hudStage.addActor(expLabel);
		expLabel.info(text, duration);
	}

}
