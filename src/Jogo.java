import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Jogo extends ApplicationAdapter {
	public SpriteBatch batch;
	private OrthographicCamera camera;
	private Background fundo;
	
	private Submarino submarino ;
	private Ondas ondas ;
	private ArrayList<Inimigo> inimigos ;
	private ArrayList<Mergulhador> mergulhadores ;
	
	float stateTime ; // Controla o tempo do jogo
	float proximaLeva ; // Determinha o tempo da proxima leva de seres spawnados
	
	@Override
	public void create() {
		stateTime = 0f ;
		proximaLeva = 1f ;
		
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Background.getLargura(), Background.getAltura());
		fundo = new Background("sprites\\background.png");
		
		ondas = new Ondas() ;
		submarino = new Submarino() ;
		inimigos = new ArrayList<Inimigo>() ;
		mergulhadores = new ArrayList<Mergulhador>() ;
		Mergulhador.montaAnimacao();
	}
	

	@Override
	public void render() {
		stateTime += Gdx.graphics.getDeltaTime();
		
		criaObjetos(stateTime) ;
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		desenhaObjetos(batch, stateTime) ;
		
		movimentaObjetos() ;
	}
	
	public void criaObjetos(float stateTime) {
		if (stateTime < proximaLeva) return ;

		proximaLeva = stateTime + Background.getLargura()/Inimigo.getVelocidade() ;
		
		for (int i=0; i < Background.getQuantidadeLinhas(); i++) {
			if (new Random().nextInt(3) <= 1) { // Verifica se vai spawnar algum inimigo na linha i
				switch (new Random().nextInt(5)) { // Verifica qual inimigo
					case 0, 1:
						inimigos.add(new Tubarao(i)) ;
						break ;
					case 2, 3:
						inimigos.add(new SubmarinoInimigo(i)) ;
						break ;
					default:
						mergulhadores.add(new Mergulhador(i)) ;
				}
			}
		}
	}
	
	public void desenhaObjetos(SpriteBatch batch, float StateTime) {
		batch.begin();
		batch.draw(fundo.getImage(), 0, 0) ;
		
		Iterator<Inimigo> iterInimigos = inimigos.iterator() ;
		while (iterInimigos.hasNext()) {
			Inimigo inimigo = iterInimigos.next() ;
			inimigo.anima(batch, stateTime) ;
		}
		
		Iterator<Mergulhador> iterMergulhadores = mergulhadores.iterator() ;
		while (iterMergulhadores.hasNext()) {
			Mergulhador mergulhador = iterMergulhadores.next() ;
			mergulhador.anima(batch, stateTime) ;
		}
		
		submarino.anima(batch, stateTime) ;
		ondas.anima(batch, stateTime) ;
		batch.end();
	}
	
	public void movimentaObjetos() {
		submarino.movimenta() ;
		
		Iterator<Inimigo> iterInimigo = inimigos.iterator() ;
		while (iterInimigo.hasNext()) {
			Inimigo inimigo = iterInimigo.next() ;
			
			inimigo.movimenta() ;
			if (inimigo.paraRemover()) iterInimigo.remove() ;
		}
		
		Iterator<Mergulhador> iterMergulhadores = mergulhadores.iterator() ;
		while (iterMergulhadores.hasNext()) {
			Mergulhador mergulhador = iterMergulhadores.next() ;
			
			mergulhador.movimenta() ;
			if (mergulhador.paraRemover()) iterMergulhadores.remove() ;
		}
	}
}
