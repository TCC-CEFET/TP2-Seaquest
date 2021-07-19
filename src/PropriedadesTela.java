
public class PropriedadesTela {
	static private int largura=800, altura=480 ;
	static private int limiteInferior=84 ;
	private static int alturaLinhas=68 ;

	public static int getLargura() {
		return largura;
	}

	public static int getAltura() {
		return altura;
	}
	
	public static int getLimiteInferior() {
		return limiteInferior;
	}
	
	public static int getAlturaLinha(int indice) {
		// Faz o calculo para enviar a altura da linha imaginaria onde spawna os inimigos
		return PropriedadesTela.limiteInferior + (PropriedadesTela.alturaLinhas*indice) + (PropriedadesTela.alturaLinhas/2) ;
	}
}
