package tiros;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture ;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle ;

import ambiente.*;
import personagens.caracteristicas.*;

public class Tiro {
	private Background fundo ;
	
	//imagem e caracteristicas
	private Texture imagem ; // textura do tiro
	private Rectangle retangulo ;
	private int velocidade ; // veloidade do tiro
	private Direcao direcao ;
	
	public Tiro(String caminhoImagem, float x, float y, int largura, int altura, int velocidade, Direcao direcao, Background fundo) {
		imagem = new Texture(caminhoImagem) ;
		retangulo = new Rectangle(x, y-(altura), largura, altura);
		this.velocidade = velocidade ;
		this.direcao = direcao ;
		this.fundo = fundo ;
	}
	//movimenta o tiro de acordo com a direcao
	public void movimenta() {
		if (direcao == Direcao.DIREITA) retangulo.x += velocidade * Gdx.graphics.getDeltaTime() ;
		else retangulo.x -= velocidade * Gdx.graphics.getDeltaTime() ;
	}
	//desenha o tiro na tela
	public void anima(SpriteBatch batch) {	
		batch.draw(imagem, retangulo.x, retangulo.y) ;
	}
	//retorna true se for para remover o tiro
	public boolean paraRemover() {
		if (retangulo.x < 10-retangulo.getWidth() && direcao == Direcao.ESQUERDA) return true ;
		if (retangulo.x > fundo.getLargura()+10 && direcao == Direcao.DIREITA) return true ;
		
		return false ;
	}
	
	public Rectangle getRetangulo() {
		return retangulo;
	}
	//libera o background e a imagem do tiro
	public void dispose() {
		fundo.dispose() ;
		imagem.dispose() ;
	}
}
