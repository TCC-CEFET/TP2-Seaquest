package Ambiente;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Background {
	private int largura, altura ;
	private int limiteInferior ;
	private int alturaLinhas ;
	private int quantidadeLinhas ;
	private Texture imagem ;
	
	public Background() {
		super();
		
		this.imagem = new Texture("sprites\\background.png") ;
		
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
	
	public boolean estaEmTela(Rectangle retangulo) {
		if ((retangulo.x <= largura - retangulo.width) && (retangulo.x >= 0)) {
			if ((retangulo.y <= altura - retangulo.height) && (retangulo.y >= 0)) {
				return true ;
			}
		}
		return false ;
	}
	
	public void dispose() {
		imagem.dispose() ;
	}
}
