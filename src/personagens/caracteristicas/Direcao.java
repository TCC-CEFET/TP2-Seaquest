package personagens.caracteristicas ;

import java.util.Random;

import ambiente.*;
import personagens.aliados.*;

// Enum para a direcao em que os personagens estao indo
public enum Direcao {
	ESQUERDA, DIREITA ;
	
	// Pega uma direcao aleatoria
	static public Direcao getDirecaoAleatoria() {
		return new Random().nextFloat() <= 0.5 ? Direcao.DIREITA : Direcao.ESQUERDA ;
	}
	
	// Pega o X inicial de spawn de acordo com a direcao
	public int getXInicial(int largura, Background fundo) {
		if (largura == Mergulhador.getLargura()) return this == Direcao.DIREITA ? 0-largura : fundo.getLargura() ;
		else return this == Direcao.DIREITA ? 0-largura-200 : fundo.getLargura()+200 ;
	}
} ;
