import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Jogo extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Background fundo;
	private Submarino submarino ;
	private ArrayList<Inimigo> inimigos ;
	float stateTime ; // Controla o tempo do jogo
	

	@Override
	public void create() {
		stateTime = 0f ;
		
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, PropriedadesTela.getLargura(), PropriedadesTela.getAltura());
		fundo = new Background("sprites\\background.png");
		
		submarino = new Submarino() ;
	}

	@Override
	public void render() {
		batch.begin();
		
		batch.draw(fundo.getImage(), 0, 0) ;
		
		stateTime += Gdx.graphics.getDeltaTime();
		
//		submarino.anima(batch, stateTime) ;
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		
		batch.end();
	}
}
