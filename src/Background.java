import com.badlogic.gdx.graphics.Texture;

public class Background {
	private static int largura=800, altura=480 ;
	private static int limiteInferior=84 ;
	private static int alturaLinhas=68 ;
	private static int quantidadeLinhas=4 ;
	private Texture imagem;
	
	public Background(String caminho) {
		super();
		this.imagem = new Texture(caminho);
	}
	
	public Texture getImage() {
		return this.imagem;
	}

	static public int getLargura() {
		return largura;
	}

	static public int getAltura() {
		return altura;
	}
	
	static public int getLimiteInferior() {
		return limiteInferior;
	}
	
	static public int getAlturaLinha(int indice) {
		if (indice < 0) indice = 0 ;
		else if (indice > 4) indice = 3 ;
		
		// Faz o calculo para enviar a altura da linha imaginaria onde spawna os inimigos
		return limiteInferior + (alturaLinhas*indice) + (alturaLinhas/2) ;
	}
	
	static public int getQuantidadeLinhas() {
		return quantidadeLinhas ;
	}
}
