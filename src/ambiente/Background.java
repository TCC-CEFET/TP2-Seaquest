package ambiente;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

//classe que possui as caracteristicas do plano de fundo e da tela
public class Background {
	//caracteristicas do plano de fundo e da tela
	private int largura, altura ;
	private int limiteInferior ;
	private int alturaLinhas ;
	
	private int quantidadeLinhas ; //quantidade de linhas em que aparecem os inimigos
	
	private Texture imagem ; //textura que guarda a imagem do background
	
	public Background() {
		super();
		
		this.imagem = new Texture("sprites\\background.png") ;//caminho da imagem do plano de fundo
		
		largura=800 ;
		altura=480 ;
		limiteInferior=84 ;
		alturaLinhas=68 ;
		quantidadeLinhas=4 ;
	}
	
	public Texture getImagem() {
		return this.imagem;
	}

	public int getLargura() {
		return largura;
	}

	public int getAltura() {
		return altura;
	}
	
	public int getLimiteInferior() {
		return limiteInferior;
	}
	
	public int getAlturaLinha(int indice) {
		if (indice < 0) indice = 0 ;
		else if (indice > 4) indice = 3 ;
		
		// Faz o calculo para enviar a altura da linha imaginaria onde spawna os inimigos
		return limiteInferior + (alturaLinhas*indice) + (alturaLinhas/2) ;
	}
	
	public int getQuantidadeLinhas() {
		return quantidadeLinhas ;
	}
	
	//metodo que determina se o objeto esta em tela
	public boolean estaEmTela(Rectangle retangulo) {
		if ((retangulo.x <= largura - retangulo.width) && (retangulo.x >= 0)) {
			if ((retangulo.y <= altura - retangulo.height) && (retangulo.y >= 0)) {
				return true ;
			}
		}
		return false ;
	}
	
	//libera a imagem do background
	public void dispose() {
		imagem.dispose() ;
	}
}
