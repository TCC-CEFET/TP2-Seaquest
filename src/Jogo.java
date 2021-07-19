import java.util.ArrayList;
import java.util.Iterator;

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
	
	@Override
	public void create() {
		stateTime = 0f ;
		
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, PropriedadesTela.getLargura(), PropriedadesTela.getAltura());
		fundo = new Background("sprites\\background.png");
		
		ondas = new Ondas() ;
		submarino = new Submarino() ;
		inimigos = new ArrayList<Inimigo>() ;
		mergulhadores = new ArrayList<Mergulhador>() ;
		
		inimigos.add(new Patrulha()) ;
		inimigos.add(new SubmarinoInimigo(0, PropriedadesTela.getAlturaLinha(0))) ;
	}
	

	@Override
	public void render() {
		stateTime += Gdx.graphics.getDeltaTime();
		
		criaObjetos() ;
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		batch.draw(fundo.getImage(), 0, 0) ;
		submarino.anima(batch, stateTime) ;
		
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
		
		ondas.anima(batch, stateTime) ;
		batch.end();
		
		movimentaObjetos() ;
	}
	
	public void movimentaObjetos() {
		submarino.movimenta() ;
		
		Iterator<Inimigo> iterInimigo = inimigos.iterator() ;
		while (iterInimigo.hasNext()) {
			Inimigo inimigo = iterInimigo.next() ;
			
			inimigo.movimenta() ;
			if (inimigo.paraRemover()) {
				iterInimigo.remove() ;
				System.out.println("renavam");
			}
		}
		
		Iterator<Mergulhador> iterMergulhadores = mergulhadores.iterator() ;
		while (iterMergulhadores.hasNext()) {
			Mergulhador mergulhador = iterMergulhadores.next() ;
			
			mergulhador.movimenta() ;
			if (mergulhador.paraRemover()) iterMergulhadores.remove() ;
		}
	}
	
	public void criaObjetos() {
		
	}
}
