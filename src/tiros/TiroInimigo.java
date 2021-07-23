package tiros;

import ambiente.*;
import personagens.caracteristicas.*;
import personagens.inimigos.*;

//classe que herda as caracteristicas de Tiro e funciona como o tiro do submarino inimigo
public class TiroInimigo extends Tiro {
	//variaveis estaticas que guardam o caminho e as dimensoes da imagem do tiro, que sao sempre os mesmos
	static private String caminhoImagem="sprites\\tiroInimigo.png";
	static private int largura=48, altura=16 ;
	
	public TiroInimigo(float x, float y, Direcao direcao, Background fundo) {
		//no campo de velocidade recebe-se a velocidade do inimigo para ser somada a do tiro
		super(caminhoImagem, x, y, largura, altura, Inimigo.getVelocidadePadrao()+150, direcao, fundo);
	}
	
	public static int getLargura() {
		return largura;
	}
}
