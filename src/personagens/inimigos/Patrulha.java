package personagens.inimigos;

import ambiente.*;

// Classe para patrulha inimiga que passa na superficie
public class Patrulha extends Inimigo {
	// Caracteristicas de todas as patrulhas
	static private String caminhoSpriteSheet="sprites\\inimigo_spritesheet.png";
	static private String caminhoAudio="sounds\\destroyEnemySub.mp3";
	static float tempoEntreFrame = 0.08f ;
	static private int largura=48, altura=30 ;
	static private int velocidade=75 ;
	
	public Patrulha(Background fundo) {
		super(4, caminhoSpriteSheet, largura, altura, tempoEntreFrame, caminhoAudio, fundo, velocidade) ;
	}
	
	static public int getVelocidade() {
		return velocidade ;
	}
}
