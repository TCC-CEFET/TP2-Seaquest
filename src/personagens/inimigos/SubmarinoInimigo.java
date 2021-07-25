package personagens.inimigos;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ambiente.*;
import personagens.aliados.*;
import personagens.caracteristicas.*;
import personagens.inimigos.*;
import rodar.*;
import tiros.*;

// Classe submarino inimigo que eh um inimigo
public class SubmarinoInimigo extends Inimigo {
	// Caracteristicas de todos os submarinos inimigos
	static private String caminhoSpriteSheet="sprites\\inimigo_spritesheet.png";
	static private String caminhoAudio="sounds\\destroyEnemySub.mp3";
	static float tempoEntreFrame = 0.08f ;
	static private int largura=48, altura=30 ;
	
	private TiroInimigo tiro ; // Tiro atual do inimigo
	
	public SubmarinoInimigo(int linha, Background fundo) {
		super(linha, caminhoSpriteSheet, largura, altura, tempoEntreFrame, caminhoAudio, fundo, null) ;
	}
	
	// Funcao controla que adiciona a funcionalidade de atirar
	@Override
	public void controla(float stateTime) {
		super.controla(stateTime);
		
		// Sempre atira caso o tiro do submarino nao exista
		if (tiro == null && fundo.estaEmTela(retangulo)) {
			tiro = new TiroInimigo(direcao == Direcao.DIREITA ? retangulo.x+largura : retangulo.x-TiroInimigo.getLargura(), retangulo.y+(altura/2), direcao, fundo) ;
		}
		// Movimenta o tiro
		if (tiro != null) {
			tiro.movimenta() ;
		}
	}
	
	// Funcao de colocar em tela que adiciona o tiro junto
	@Override
	public void anima(SpriteBatch batch, float stateTime) {
		super.anima(batch, stateTime) ;
		
		if (tiro != null) tiro.anima(batch) ;
	}
	
	// Funcao para verificar posicao que adiciona a verificacao do tiro
	@Override
	public void verificaPosicao(ArrayList<TiroSubmarino> tiros, Submarino submarino) {
		if (tiro != null) {
			// Verifica se o jogdor foi acertado
			if (tiro.getRetangulo().overlaps(submarino.getRetangulo()) && !submarino.getExplodindo()) {
				submarino.some() ;
			}
			
			if (tiro.paraRemover()) tiro = null ;
		}
		
		super.verificaPosicao(tiros, submarino) ;
	}
	
	@Override
	public void dispose() {
		super.dispose() ;
		if (tiro != null) tiro.dispose() ;
	}
}
