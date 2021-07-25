package personagens.inimigos;

import com.badlogic.gdx.Gdx;

import ambiente.*;

// Tubarao do jogo que eh um inimigo
public class Tubarao extends Inimigo {
	// Variaveis das caracteristicas de todos os tubaroes
	static private String caminhoSpriteSheet="sprites\\tubarao_spritesheet.png";
	static private String caminhoAudio="sounds\\destroyShark.mp3";
	static float tempoEntreFrame = 0.25f ;
	static private int largura=48, altura=16 ;
	static private int variacaoVertical=altura*2 ;
	static private float tempoOscilacaoVertical=1.2f ; 
	
	public Tubarao(int linha, Background fundo) {
		super(linha, caminhoSpriteSheet, largura, altura, tempoEntreFrame, caminhoAudio, fundo, null) ;
	}
	
	// Funcao controla que adiciona a funcionalidade de movimentacao vertical
	@Override
	public void controla(float stateTime) {
		super.controla(stateTime) ;
		
		// Realiza a movimentacao vertical de acordo com o momento
		float oscilacaoAtual = stateTime%tempoOscilacaoVertical ;
		if (oscilacaoAtual <= (tempoOscilacaoVertical/4)*1 && oscilacaoAtual > 0) {
			this.retangulo.y -= variacaoVertical * Gdx.graphics.getDeltaTime() ;
		} else if (oscilacaoAtual <= (tempoOscilacaoVertical/4)*2) {
			this.retangulo.y += variacaoVertical * Gdx.graphics.getDeltaTime() ;
		} else if (oscilacaoAtual <= (tempoOscilacaoVertical/4)*3) {
			this.retangulo.y += variacaoVertical * Gdx.graphics.getDeltaTime() ;
		} else {
			this.retangulo.y -= variacaoVertical * Gdx.graphics.getDeltaTime() ;
		}
	}
}
