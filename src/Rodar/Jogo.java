package Rodar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Ambiente.* ;
import Mobs.Inimigos.* ;
import Mobs.Passivos.* ;

public class Jogo extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Background fundo;
	
	private Submarino submarino ;
	private Ondas ondas ;
	private ArrayList<Inimigo> inimigos ;
	private ArrayList<Mergulhador> mergulhadores ;
	
	private float stateTime ; // Controla o tempo do jogo
	private float proximaLeva ; // Determinha o tempo da proxima leva de seres spawnados
	private float proximaLevaPatrulha ; // Determinha o tempo da proximpatrulha spawnar
	
	@Override
	public void create() {
		stateTime = 0f ;
		proximaLeva = 1f ;
		proximaLevaPatrulha = 1f ;
		
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
		
		verificaPosicoes() ;
		
		movimentaObjetos() ;
	}
	
	public void criaObjetos(float stateTime) {
		if (submarino.getMorreu()) {
			inimigos.clear() ;
			mergulhadores.clear() ;
			return ;
		}
		
		if (submarino.getDesembarcou6() >= 2 && stateTime > proximaLevaPatrulha) {
			proximaLevaPatrulha = stateTime+(fundo.getLargura()+200)/Patrulha.getVelocidade() ;
			if (new Random().nextFloat() <= 0.70) inimigos.add(new Patrulha(fundo)) ;
		}
		
		if (stateTime < proximaLeva) return ;

		proximaLeva = stateTime + (fundo.getLargura()+200)/Inimigo.getVelocidadePadrao() ;
		
		for (int i=0; i < fundo.getQuantidadeLinhas(); i++) {
			if (new Random().nextFloat() <= 0.75) { // Verifica se vai spawnar algo na linha i
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
		
		if (submarino.getEstaDesembarcando()) {
			submarino.anima(batch, stateTime, camera) ;
			ondas.anima(batch, stateTime) ;
			batch.end() ;
			return ;
		}
		
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
		if (Gdx.input.isKeyPressed(Keys.R)) resetaJogo() ;
		
		submarino.controla(stateTime) ;
		
		Iterator<Inimigo> iterInimigos = inimigos.iterator() ;
		while (iterInimigos.hasNext()) {
			Inimigo inimigo = iterInimigos.next() ;
			
			inimigo.controla(stateTime) ;
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
	
	public void resetaJogo() {
		dispose() ;
		create() ;
	}
	
	@Override
	public void dispose() {
		batch.dispose() ;
		fundo.dispose() ;
		
		Iterator<Inimigo> iterInimigos = inimigos.iterator() ;
		while (iterInimigos.hasNext()) {
			Inimigo inimigo = iterInimigos.next() ;
			inimigo.dispose() ;
		}
		
		Iterator<Mergulhador> iterMergulhadores = mergulhadores.iterator() ;
		while (iterMergulhadores.hasNext()) {
			Mergulhador mergulhador = iterMergulhadores.next() ;
			mergulhador.dispose() ;
		}
		
		submarino.dispose() ;
	}
}
