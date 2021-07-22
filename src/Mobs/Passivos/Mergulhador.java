package Mobs.Passivos;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import Ambiente.* ;
import Mobs.Caracteristicas.* ;
import Mobs.Inimigos.* ;

public class Mergulhador {
	private Background fundo ;
	
	static private int pontos=50;
	static private float velocidadePadrao=75;
	static float tempoEntreFramePadrao=0.15f ;
	
	static private int largura=52, altura=56 ;	
	
	static private TextureRegion[] framesDireita ;
	private Animation animacaoDireita ;
	static private TextureRegion[] framesEsquerda ;
	private Animation animacaoEsquerda ;
	
	private Rectangle retangulo ;
	private Direcao direcao ;
	private float velocidade ;
	private float tempoEntreFrame ;
	private boolean animacaoNormal ;
	
	public Mergulhador(int linha, Background fundo) {
		this.direcao = Direcao.getDirecaoAleatoria() ;
		retangulo = new Rectangle(direcao.getXInicial(largura, fundo), fundo.getAlturaLinha(linha)-(altura/2), largura, altura) ;
		velocidade = velocidadePadrao ;
		animacaoNormal = true ;
		
		this.fundo = fundo ;
		
		tempoEntreFrame = tempoEntreFramePadrao ;
		
		montaAnimacao() ;
	}
	
	public void montaAnimacao() {
		String caminhoImagem="sprites\\mergulhador_spritesheet.png" ;
		int colunas=3, linhas=2 ;
		int larguraRealSheet=largura*colunas, alturaRealSheet=altura*linhas ;
		
		Texture imagem = new Texture(caminhoImagem) ;
		
		TextureRegion[][] matrizFrames = TextureRegion.split(imagem, larguraRealSheet/colunas, alturaRealSheet/linhas) ;

		framesDireita = new TextureRegion[colunas+1] ;
		int i ;
		for (i=0; i < colunas; i++) {
			framesDireita[i] = matrizFrames[0][i];
		}
		framesDireita[i] = matrizFrames[0][1] ;
		
		framesEsquerda = new TextureRegion[colunas+1];
		for (i=0; i < colunas; i++) {
			framesEsquerda[i] = matrizFrames[1][i];
		}
		framesEsquerda[i] = matrizFrames[1][1] ;
		
		animacaoDireita = new Animation(tempoEntreFrame, framesDireita) ;
		animacaoEsquerda = new Animation(tempoEntreFrame, framesEsquerda) ;
	}
	
	public void atualizaAnimacao() {
		animacaoDireita = new Animation(tempoEntreFrame, framesDireita) ;
		animacaoEsquerda = new Animation(tempoEntreFrame, framesEsquerda) ;
	}
	
	public void anima(SpriteBatch batch, float stateTime) {
		TextureRegion frameAtual ;
		
		if (direcao == Direcao.DIREITA) frameAtual = animacaoDireita.getKeyFrame(stateTime, true);
		else frameAtual = animacaoEsquerda.getKeyFrame(stateTime, true);
				
		batch.draw(frameAtual, retangulo.x, retangulo.y) ;
	}
	
	public void movimenta() {
		if (direcao == Direcao.DIREITA) retangulo.x += velocidade * Gdx.graphics.getDeltaTime() ;
		else retangulo.x -= velocidade * Gdx.graphics.getDeltaTime() ;
	}
	
	public boolean paraRemover() {
		if (retangulo.x < 0-largura && this.direcao == Direcao.ESQUERDA) return true ;
		if (retangulo.x > fundo.getLargura() && this.direcao == Direcao.DIREITA) return true ;
		
		return false ;
	}
	
	public static int getLargura() {
		return Mergulhador.largura;
	}
	
	public Rectangle getRetangulo() {
		return this.retangulo ;
	}
	
	static public int getPontos() {
		return pontos ;
	}
	
	static public void aumentaPontos() {
		if (pontos < 1000) pontos += 50 ;
	}
	
	public void some() {
		Gdx.audio.newSound(Gdx.files.internal("sounds\\rescueDiver.mp3")).play() ;
	}
	
	public void verificaPosicao(ArrayList<Inimigo> inimigos) {
		// Verifica os inimigos para aumentar velocidade
		Iterator<Inimigo> iterInimigos = inimigos.iterator() ;
		while (iterInimigos.hasNext()) {
			Inimigo inimigo = iterInimigos.next() ;
			
			if (retangulo.overlaps(inimigo.getRetangulo())) {
				if (animacaoNormal) {
					animacaoNormal = false ;
					velocidade = Inimigo.getVelocidadePadrao() ;
					direcao = inimigo.getDirecao() ;
					tempoEntreFrame = 0.07f ;
					atualizaAnimacao() ;
				}
				return ;
			}
		}
		
		animacaoNormal = true ;
		tempoEntreFrame = tempoEntreFramePadrao ;
		velocidade = velocidadePadrao ; // Deixa na velocidade padrao
		atualizaAnimacao() ;
	}
	
	public void dispose() {
		fundo.dispose() ;
	}
}
