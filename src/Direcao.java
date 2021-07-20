import java.util.Random;

enum Direcao {
	ESQUERDA, DIREITA ;
	
	static public Direcao getDirecaoAleatoria() {
		return new Random().nextBoolean() ? Direcao.DIREITA : Direcao.ESQUERDA ;
	}
	
	public int getXInicial(int largura) {
		if (largura == Mergulhador.getLargura()) return this == Direcao.DIREITA ? 0-largura : Background.getLargura() ;
		else return this == Direcao.DIREITA ? 0-largura-200 : Background.getLargura()+200 ;
	}
} ;
