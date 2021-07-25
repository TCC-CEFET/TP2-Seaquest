package rodar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ambiente.*;
import personagens.aliados.*;
import personagens.inimigos.*;

// Classe que controla os elementos do jogo
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
		//Variaveis para marcação de tempo
		stateTime = 0f ;
		proximaLeva = 1f ;
		proximaLevaPatrulha = 1f ;
		
		fundo = new Background() ;
		
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, fundo.getLargura(), fundo.getAltura());
		
		//arraylist dos objetos do tipo Ondas,Submarino,Inimigo e Mergulhadores
		ondas = new Ondas(fundo) ;
		submarino = new Submarino(fundo, ondas) ;
		inimigos = new ArrayList<Inimigo>() ;
		mergulhadores = new ArrayList<Mergulhador>() ;
	}
	
	//Função que faz as operações a cada repetição
	@Override
	public void render() {
		stateTime += Gdx.graphics.getDeltaTime() ;
		
		criaObjetos(stateTime) ;
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT) ;
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		movimentaObjetos() ;
		
		verificaPosicoes() ;
		
		desenhaObjetos() ;
	}
	//Função para criar todos os objetos
	public void criaObjetos(float stateTime) {
		// Apaga os objetos caso o jogador esteja morto e para de criar novos ate renascer
		if (submarino.getMorreu()) {
			inimigos.clear() ;
			mergulhadores.clear() ;
			return ;
		}
		//Se o submarino já tiver resgatado seis mergulhadores duas vezes aumenta a velocidade da proxima leva de patrulha e há 70% de chance de gerar uma nova patrulha
		if (submarino.getDesembarcou6() >= 2 && stateTime > proximaLevaPatrulha) {
			proximaLevaPatrulha = stateTime+(fundo.getLargura()+200)/Patrulha.getVelocidade() ;
			if (new Random().nextFloat() <= 0.70) inimigos.add(new Patrulha(fundo)) ;
		}
		
		if (stateTime < proximaLeva) return ;

		proximaLeva = stateTime + (fundo.getLargura()+200)/Inimigo.getVelocidadePadrao() ;
		
		//for para spawnar os objetos na linha i
		for (int i=0; i < fundo.getQuantidadeLinhas(); i++) {
			if (new Random().nextFloat() <= 0.75) { // Verifica se vai spawnar algo na linha i.Há 75% de chance de spawnar
				switch (new Random().nextInt(5)) { // Verifica o que vai spawnar na linha
					case 0, 1, 2://Define 3 casos de 5 para spawnar inimigos.Há 60% de chance de spawnar
						inimigos.add(new Random().nextInt(2) == 0 ? new Tubarao(i, fundo) : new SubmarinoInimigo(i, fundo)) ;//define se o inimigo sera submarino ou tubarão
						break ;
					default://define os 2 casos restantes para spawnar um mergulhador e consequentemente um tubarão atrás dele
						mergulhadores.add(new Mergulhador(i, fundo)) ;
						inimigos.add(new Tubarao(i, fundo)) ; // Spawna o tubarao atras.Há 40% de chance de spawnar
				}
			}
		}
	}
	//função para desenhar todos os objetos
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
	//movimenta todos os objetos
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
		submarino.verificaPosicao(inimigos, mergulhadores, ondas) ;
		
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
	//função parar resetar o jogo
	public void resetaJogo() {
		dispose() ;//libera todos os objetos
		create() ;//chama a função create novamente assim reiniciando o jogo
	}
	
	//função para liberar os objetos
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
