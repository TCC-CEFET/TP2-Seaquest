package mobs.inimigos;

import com.badlogic.gdx.Gdx;

import ambiente.*;

public class Tubarao extends Inimigo {	
	static private String caminhoSpriteSheet="sprites\\tubarao_spritesheet.png";
	static private String caminhoAudio="sounds\\destroyShark.mp3";
	static float tempoEntreFrame = 0.25f ;
	static private int largura=48, altura=16 ;
	static private int colunas=3, linhas=2 ;
	static private int variacaoVertical = altura*2 ;
	private float tempoOscilacao ; 
	
	public Tubarao(int linha, Background fundo) {
		super(linha, caminhoSpriteSheet, largura, altura, colunas, linhas, tempoEntreFrame, caminhoAudio, fundo, null) ;
		tempoOscilacao = 1.2f ;
	}
	
	@Override
	public void controla(float stateTime) {
		super.controla(stateTime) ;
		
		float oscilacaoAtual = stateTime%tempoOscilacao ;
		if (oscilacaoAtual <= (tempoOscilacao/4)*1 && oscilacaoAtual > 0) {
			this.retangulo.y -= variacaoVertical * Gdx.graphics.getDeltaTime() ;
		} else if (oscilacaoAtual <= (tempoOscilacao/4)*2) {
			this.retangulo.y += variacaoVertical * Gdx.graphics.getDeltaTime() ;
		} else if (oscilacaoAtual <= (tempoOscilacao/4)*3) {
			this.retangulo.y += variacaoVertical * Gdx.graphics.getDeltaTime() ;
		} else {
			this.retangulo.y -= variacaoVertical * Gdx.graphics.getDeltaTime() ;
		}
	}
}