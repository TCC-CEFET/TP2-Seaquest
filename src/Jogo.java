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
	public SpriteBatch batch;
	private OrthographicCamera camera;
	private Background fundo;
	private Submarino submarino ;
	private Ondas ondas ;
	private ArrayList<Inimigo> inimigos ;
	float stateTime ; // Controla o tempo do jogo
	
	Tubarao tubarao ;
	SubmarinoInimigo inimigo ;
	Patrulha patrulha ;
	Mergulhador mergulhador ;
	@Override
	public void create() {
		stateTime = 0f ;
		
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, PropriedadesTela.getLargura(), PropriedadesTela.getAltura());
		fundo = new Background("sprites\\background.png");
		
		ondas = new Ondas() ;
		submarino = new Submarino() ;
		tubarao = new Tubarao(0, 0) ;
		inimigo = new SubmarinoInimigo(PropriedadesTela.getLargura()/2, PropriedadesTela.getAltura()-200) ;
		patrulha = new Patrulha() ;
		mergulhador = new Mergulhador(50, 50) ; mergulhador.montaAnimacao() ;
	}
	

	@Override
	public void render() {
		stateTime += Gdx.graphics.getDeltaTime();
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		batch.draw(fundo.getImage(), 0, 0) ;
		submarino.anima(batch, stateTime) ;
		tubarao.anima(batch, stateTime);
		inimigo.anima(batch, stateTime);
		patrulha.anima(batch, stateTime);
		mergulhador.anima(batch, stateTime);
		ondas.anima(batch, stateTime);
		batch.end();
	}
}
