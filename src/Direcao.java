import java.util.Random;

enum Direcao {
	ESQUERDA, DIREITA ;
	
	static public Direcao getDirecaoAleatoria() {
		return new Random().nextFloat() <= 0.5 ? Direcao.DIREITA : Direcao.ESQUERDA ;
	}
	
	public int getXInicial(int largura, Background fundo) {
		if (largura == Mergulhador.getLargura()) return this == Direcao.DIREITA ? 0-largura : fundo.getLargura() ;
		else return this == Direcao.DIREITA ? 0-largura-200 : fundo.getLargura()+200 ;
	}
} ;
