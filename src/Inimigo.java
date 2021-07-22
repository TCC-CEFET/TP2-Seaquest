import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Inimigo {
	protected Background fundo ;
	
	static private int pontos=20 ;
	static private int velocidadePadrao=125 ;
	
	protected int velocidade ; // px por s
	private int largura, altura ;
	private int colunas, linhas ;
	private int larguraRealSheet, alturaRealSheet ;
	private float tempoEntreFrame ;
	private String caminhoAudio ;
	
	protected Rectangle retangulo ;
	protected Direcao direcao ;
	
	private TextureRegion[] framesDireita ;
	private Animation animacaoDireita ;
	private TextureRegion[] framesEsquerda ;
	private Animation animacaoEsquerda ;
	
	public Inimigo(int linha, String caminhoImagem, int largura, int altura, int colunas, int linhas, float tempoEntreFrame, String caminhoAudio, Background fundo, Integer velocidade) {
		this.caminhoAudio = caminhoAudio ;
		this.largura = largura ; this.altura = altura ;
		this.colunas = colunas ; this.linhas = linhas ;
		larguraRealSheet=largura*colunas ; alturaRealSheet=altura*linhas ;
		this.tempoEntreFrame = tempoEntreFrame ;
		this.direcao = Direcao.getDirecaoAleatoria() ;
		if (velocidade != null) this.velocidade = velocidade ;
		else this.velocidade = velocidadePadrao ;
		
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
	
	public void montaAnimacao(String caminhoImagem) {
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
	
	public void anima(SpriteBatch batch, float stateTime) {
		TextureRegion frameAtual ;
		
		if (direcao == Direcao.DIREITA) frameAtual = animacaoDireita.getKeyFrame(stateTime, true);
		else frameAtual = animacaoEsquerda.getKeyFrame(stateTime, true);
				
		batch.draw(frameAtual, retangulo.x, retangulo.y) ;
	}
	
	public void controla(float stateTime) {
		if (direcao == Direcao.DIREITA) retangulo.x += velocidade * Gdx.graphics.getDeltaTime() ;
		else retangulo.x -= velocidade * Gdx.graphics.getDeltaTime() ;
	}
	
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
	
	static public void aumentaPontos() {
		if (pontos < 90) pontos += 10 ;
	}
	
	static void aumentaVelocidadePadrao() {
		velocidadePadrao += 10 ;
	}
	
	public void some(Submarino submarino) {
		submarino.aumentaPontuacao(Inimigo.pontos) ;
		Gdx.audio.newSound(Gdx.files.internal(caminhoAudio)).play() ;
	}
	
	public void verificaPosicao(ArrayList<TiroSubmarino> tiros, Submarino submarino) {
		Iterator<TiroSubmarino> iterTiros = tiros.iterator() ;
		while (iterTiros.hasNext()) {
			TiroSubmarino tiro = iterTiros.next() ;
			
			if (retangulo.overlaps(tiro.getRetangulo())) {
				some(submarino) ;
				iterTiros.remove() ;
				this.morre(submarino) ;
			}
		}
	}
	
	public void morre(Submarino submarino) {
		retangulo.y = fundo.getAltura()*-1 ;
		some(submarino) ;
	}
	
	public void dispose() {
		fundo.dispose() ;
	}
}
