import java.awt.font.ShapeGraphicAttribute;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class Jogo extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Background fundo;
	
	private Submarino submarino ;
	private Ondas ondas ;
	private ArrayList<Inimigo> inimigos ;
	private ArrayList<Mergulhador> mergulhadores ;
	private ArrayList<TiroInimigo> tirosInimigos ;
	
	private float stateTime ; // Controla o tempo do jogo
	private float proximaLeva ; // Determinha o tempo da proxima leva de seres spawnados
	
	@Override
	public void create() {
		stateTime = 0f ;
		proximaLeva = 1f ;
		
		fundo = new Background() ;
		
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, fundo.getLargura(), fundo.getAltura());
		
		ondas = new Ondas(fundo) ;
		submarino = new Submarino(fundo, ondas) ;
		inimigos = new ArrayList<Inimigo>() ;
		mergulhadores = new ArrayList<Mergulhador>() ;
	}
	

	@Override
	public void render() {
		stateTime += Gdx.graphics.getDeltaTime() ;
		
		criaObjetos(stateTime) ;
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT) ;
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		desenhaObjetos() ;
		
		movimentaObjetos() ;
		
		verificaPosicoes() ;
	}
	
	public void criaObjetos(float stateTime) {
		if (stateTime < proximaLeva) return ;

		proximaLeva = stateTime + (fundo.getLargura()+200)/Inimigo.getVelocidade() ;
		
		if (submarino.getDesembarcou6() >= 2 && new Random().nextBoolean()) inimigos.add(new Patrulha(fundo)) ;
		for (int i=0; i < fundo.getQuantidadeLinhas(); i++) {
			if (new Random().nextInt(20) <= 13) { // Verifica se vai spawnar algo na linha i
				switch (new Random().nextInt(5)) { // Verifica o que vai spawnar na linha
					case 0, 1, 2:
						inimigos.add(new Random().nextInt(2) == 0 ? new Tubarao(i, fundo) : new SubmarinoInimigo(i, fundo)) ;
						break ;
					default:
						mergulhadores.add(new Mergulhador(i, fundo)) ;
						inimigos.add(new Tubarao(i, fundo)) ; // Spawna o tubarao atras
				}
			}
		}
	}
	
	public void desenhaObjetos() {
		batch.begin();
		batch.draw(fundo.getImagem(), 0, 0) ;
		
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
		
		submarino.anima(batch, stateTime, camera) ;
		ondas.anima(batch, stateTime) ;
		batch.end();
	}
	
	public void movimentaObjetos() {
		submarino.controla(stateTime) ;
		
		Iterator<Inimigo> iterInimigos = inimigos.iterator() ;
		while (iterInimigos.hasNext()) {
			Inimigo inimigo = iterInimigos.next() ;
			
			inimigo.controla() ;
		}
		
		Iterator<Mergulhador> iterMergulhadores = mergulhadores.iterator() ;
		while (iterMergulhadores.hasNext()) {
			Mergulhador mergulhador = iterMergulhadores.next() ;
			
			mergulhador.movimenta() ;
		}
	}
	
	public void verificaPosicoes() {
		submarino.verificaPosicao(inimigos, mergulhadores) ;
		
		Iterator<Mergulhador> iterMergulhadores = mergulhadores.iterator() ;
		while (iterMergulhadores.hasNext()) {
			Mergulhador mergulhador = iterMergulhadores.next() ;
			
			if (mergulhador.paraRemover()) iterMergulhadores.remove() ;
			else mergulhador.verificaPosicao(inimigos) ;
		}
		
		Iterator<Inimigo> iterInimigos = inimigos.iterator() ;
		while (iterInimigos.hasNext()) {
			Inimigo inimigo = iterInimigos.next() ;
			
			if (inimigo.paraRemover()) iterInimigos.remove() ;
			else inimigo.verificaPosicao(submarino.getTiros(), submarino) ;
		}
	}
	
	@Override
	public void dispose() {
		batch.dispose() ;
	}
}
