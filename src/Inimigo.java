
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Inimigo {
	static private int pontos=20;
	static private float velocidade=125 ; // px por s
	
	protected String caminhoImagem ;
	protected int largura, altura ;
	protected int colunas, linhas ;
	protected int larguraRealSheet, alturaRealSheet ;
	protected float tempoEntreFrame ;
	
	protected Rectangle retangulo ;
	protected Direcao direcao=Direcao.DIREITA ;
	
	protected TextureRegion[] framesDireita ;
	protected Animation animacaoDireita ;
	protected TextureRegion[] framesEsquerda ;
	protected Animation animacaoEsquerda ;
	
	public Inimigo(int x, int y, String caminhoImagem, int largura, int altura, int colunas, int linhas, float tempoEntreFrame) {
		this.caminhoImagem = caminhoImagem ;
		this.largura = largura ; this.altura = altura ;
		this.colunas = colunas ; this.linhas = linhas ;
		larguraRealSheet=largura*colunas ; alturaRealSheet=altura*linhas ;
		this.tempoEntreFrame = tempoEntreFrame ;
		
		retangulo = new Rectangle(x, y, largura, altura) ;
		
		montaAnimacao() ;
	}
	
	public void montaAnimacao() {
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
	
	public void movimenta() {
		if (direcao == Direcao.DIREITA) retangulo.x += velocidade * Gdx.graphics.getDeltaTime() ;
		else retangulo.x -= velocidade * Gdx.graphics.getDeltaTime() ;
	}
	
	public boolean paraRemover() {
		if (retangulo.x < 0-largura) return true ;
		if (retangulo.x > PropriedadesTela.getLargura()) return true ;
		
		return false ;
	}
}
