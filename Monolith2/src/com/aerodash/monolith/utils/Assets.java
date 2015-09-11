package com.aerodash.monolith.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Assets {

	public static final TextureRegion tile = new TextureRegion(new Texture(Gdx.files.internal("res/img/tile.png")));
	public static final TextureRegion tileOutlined = new TextureRegion(new Texture(Gdx.files.internal("res/img/tileOutlined.png")));
	public static final TextureRegion outline = new TextureRegion(new Texture(Gdx.files.internal("res/img/outline.png")));
	public static final TextureRegion triangle = new TextureRegion(new Texture(Gdx.files.internal("res/img/triangle.png")));
	public static final TextureRegion headlessTriangle = new TextureRegion(new Texture(Gdx.files.internal("res/img/headlessTriangle.png")));
	public static final TextureRegion x = new TextureRegion(new Texture(Gdx.files.internal("res/img/X.png")));
	public static final TextureRegion minion = new TextureRegion(new Texture(Gdx.files.internal("res/img/minion.png")));
	
	public static final GlyphLayout layout = new GlyphLayout();
	public static BitmapFont font;
	public static BitmapFont smallFont;
	static{
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("res/fonts/trench.otf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.magFilter = Texture.TextureFilter.Linear;
		parameter.minFilter = Texture.TextureFilter.Nearest;
		parameter.size = 16;
		font = generator.generateFont(parameter);
		parameter.size = 12;
		smallFont = generator.generateFont(parameter);
		generator.dispose();
	}
	
	public static void dispose(){
		font.dispose();
		smallFont.dispose();
	}
}
	
