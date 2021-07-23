package tiros;

import com.badlogic.gdx.Gdx;

import ambiente.*;
import personagens.caracteristicas.*;

public class TiroSubmarino extends Tiro {
	///variaveis estaticas que guardam o caminho e as dimensoes da imagem do tiro, que sao sempre os mesmos 
	static private String caminhoImagem="sprites\\tiroSubmarino.png";
	static private int largura=48, altura=4 ;
	
	public TiroSubmarino(float x, float y, Direcao direcao, Background fundo) {
		super(caminhoImagem, x, y, largura, altura, 800, direcao, fundo) ;
		//toca o som de tiro
		Gdx.audio.newSound(Gdx.files.internal("sounds\\fireTorpedo.mp3")).play() ;
	}

	public static int getLargura() {
		return largura;
	}
}
