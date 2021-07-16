import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

enum Direcao {ESQUERDA, DIREITA} ;

public class Submarino {
	static private String caminhoImagem="sprites\\submarino_spritesheet.png";
	static int largura=96, altura=36 ;
	static int colunas=3, linhas=2 ;
	static int larguraRealSheet=largura*colunas, alturaRealSheet=altura*linhas ;
	private Rectangle retangulo ;
	private int vidas ;
	private int mergulhadoresSalvos ;
	private int levelO2 ;
	private Direcao direcao ;
	
	//private boolean mergulhado=false;
	
	TextureRegion[] framesDireita ;
	Animation animacaoDireita ;
	TextureRegion[] framesEsquerda ;
	Animation animacaoEsquerda ;
	
	
	public Submarino() {
		retangulo = new Rectangle((PropriedadesTela.getLargura()/2)-largura, PropriedadesTela.getAltura()-altura*3, largura, altura) ;
		direcao = Direcao.DIREITA ;
		this.montaAnimacao() ;
	}
	
	public void salvarMergulhador() {
		
	}
	
	public void diminuiLevelO2() {
		
	}
	
	public void aumentaLevelO2() {
	//prende o jogador ate encher o oxigenio	
	}
	
	public void verficaLocal() {
		
	}
	
	public void desembarca() {
		
	}
	
	public void montaAnimacao() {
		float tempoEntreFrame = 0.057f ;
		
		Texture imagem = new Texture(caminhoImagem) ;
		
		TextureRegion[][] matrizFrames = TextureRegion.split(imagem, larguraRealSheet/colunas, alturaRealSheet/linhas) ;

		framesDireita = new TextureRegion[colunas+1] ;
		int i ;
		for (i=0; i < colunas; i++) {
			framesDireita[i] = matrizFrames[0][i];
		}
		framesDireita[i] = matrizFrames[0][i] ;
		
		framesEsquerda = new TextureRegion[colunas+1];
		for (i=0; i < colunas; i++) {
			framesEsquerda[i] = matrizFrames[1][i];
		}
		framesEsquerda[i] = matrizFrames[1][i] ;
		
		animacaoDireita = new Animation(tempoEntreFrame, framesDireita) ;
		animacaoEsquerda = new Animation(tempoEntreFrame, framesEsquerda) ;
	}
	
	public void anima(SpriteBatch batch, float stateTime) {
		TextureRegion frameAtual ;
		
		if (direcao == Direcao.DIREITA) frameAtual = animacaoDireita.getKeyFrame(stateTime, true);
		else frameAtual = animacaoEsquerda.getKeyFrame(stateTime, true);
		
		batch.draw(frameAtual, retangulo.x, retangulo.y) ;
	}
}
