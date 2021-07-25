package personagens.inimigos;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import ambiente.*;
import personagens.aliados.*;
import personagens.caracteristicas.*;
import tiros.*;

//classe mae que possui as funcoes comuns a todos os inimigos
public class Inimigo {
	protected Background fundo ;
	
	static private int pontos=20 ; //variavel estatica que determina os pontos, que sao comuns a todos os inimigos
	static private int velocidadePadrao=125 ; //determina a velocidade padrao de todos os inimigos
	
	protected int velocidade ; // pixels por segundo
	private int largura, altura ; //dimensoes da imagem do inimigo
	private float tempoEntreFrame ; //tempo entre a atualizacao de frame da textura
	private String caminhoAudio ; // caminho do audio do inimigo
	
	protected Rectangle retangulo ;
	protected Direcao direcao ;
	
	private TextureRegion[] framesDireita ; //array que guarda as texturas da posicao direita do inimigo
	private Animation animacaoDireita ; //faz a animacao para a posicao direita
	private TextureRegion[] framesEsquerda ; //array que guarda as texturas da posicao esquerda
	private Animation animacaoEsquerda ; //faz a animacao para a esquerda
	
	public Inimigo(int linha, String caminhoImagem, int largura, int altura, float tempoEntreFrame, String caminhoAudio, Background fundo, Integer velocidade) {
		this.caminhoAudio = caminhoAudio ;
		this.largura = largura ; this.altura = altura ;
		this.tempoEntreFrame = tempoEntreFrame ;
		this.direcao = Direcao.getDirecaoAleatoria() ;
		if (velocidade != null) this.velocidade = velocidade ;//se a velocidade recebida for null, e colocada como a velocidade padrao
		else this.velocidade = velocidadePadrao ;
		
		//cria o retangulo determinando uma direcao aleatoria e recebendo a linha pela classe jogo
		retangulo = new Rectangle(direcao.getXInicial(largura, fundo), linha == 4 ? fundo.getAlturaLinha(linha)-25 : fundo.getAlturaLinha(linha)-(altura/2), largura, altura) ;
		
		montaAnimacao(caminhoImagem) ;
		
		this.fundo = fundo ;
	}
	
	static public int getVelocidadePadrao() {
		return velocidadePadrao ;
	}
	
	public Direcao getDirecao() {
		return this.direcao ;
	}
	
	//metodo que faz a animacao do inimigo
	public void montaAnimacao(String caminhoImagem) {
		int colunas=3, linhas=2 ;
		int larguraRealSheet=largura*colunas, alturaRealSheet=altura*linhas ;
		
		Texture imagem = new Texture(caminhoImagem) ;
		
		TextureRegion[][] matrizFrames = TextureRegion.split(imagem, larguraRealSheet/colunas, alturaRealSheet/linhas) ;

		//guarda as texturas da posicao direita em framesDireita
		framesDireita = new TextureRegion[colunas+1] ;
		int i ;
		for (i=0; i < colunas; i++) {
			framesDireita[i] = matrizFrames[0][i];
		}
		framesDireita[i] = matrizFrames[0][1] ;
		
		//guarda as texturas da posicao essquerda em framesEsquerda
		framesEsquerda = new TextureRegion[colunas+1];
		for (i=0; i < colunas; i++) {
			framesEsquerda[i] = matrizFrames[1][i];
		}
		framesEsquerda[i] = matrizFrames[1][1] ;
		
		//faz a animacao de acordo com posicao
		animacaoDireita = new Animation(tempoEntreFrame, framesDireita) ;
		animacaoEsquerda = new Animation(tempoEntreFrame, framesEsquerda) ;
	}
	
	//metodo que realiza a animacao
	public void anima(SpriteBatch batch, float stateTime) {
		TextureRegion frameAtual ;
		
		//determina a animacao de acordo com a posicao
		if (direcao == Direcao.DIREITA) frameAtual = animacaoDireita.getKeyFrame(stateTime, true);
		else frameAtual = animacaoEsquerda.getKeyFrame(stateTime, true);
				
		//desenha a animacao em tela
		batch.draw(frameAtual, retangulo.x, retangulo.y) ;
	}
	
	//controla o movimento do inimigo
	public void controla(float stateTime) {
		if (direcao == Direcao.DIREITA) retangulo.x += velocidade * Gdx.graphics.getDeltaTime() ;
		else retangulo.x -= velocidade * Gdx.graphics.getDeltaTime() ;
	}
	
	//determina se e para remover o retangulo que ja foi usado
	public boolean paraRemover() {
		if (retangulo.x < 0-largura && this.direcao == Direcao.ESQUERDA) return true ;
		if (retangulo.x > fundo.getLargura() && this.direcao == Direcao.DIREITA) return true ;
		
		return false ;
	}
	
	public Rectangle getRetangulo() {
		return this.retangulo ;
	}
	
	public void setDirecao(Direcao direcao) {
		this.direcao = direcao ;
	}
	
	//aumenta os pontos que os inimigos oferecem
	static public void aumentaPontos() {
		if (pontos < 90) pontos += 10 ;
	}
	
	//aumenta a velocidade padrao de todos os inimigos
	public static void aumentaVelocidadePadrao() {
		velocidadePadrao += 10 ;
	}
	
	//verifica a posicao do inimigo e determina se deve ser removido de tela
	public void verificaPosicao(ArrayList<TiroSubmarino> tiros, Submarino submarino) {
		Iterator<TiroSubmarino> iterTiros = tiros.iterator() ;
		while (iterTiros.hasNext()) {
			TiroSubmarino tiro = iterTiros.next() ;
			
			if (retangulo.overlaps(tiro.getRetangulo())) {
				iterTiros.remove() ;
				this.some(submarino) ;
			}
		}
	}
	
	//tira o inimigo de tela
	public void some(Submarino submarino) {
		retangulo.y = fundo.getAltura()*-1 ;
		submarino.aumentaPontuacao(Inimigo.pontos) ;
		Gdx.audio.newSound(Gdx.files.internal(caminhoAudio)).play() ;
	}
	
	public void dispose() {
		fundo.dispose() ;
	}
}
