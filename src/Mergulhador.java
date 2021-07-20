
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Mergulhador {
	static private int pontos=50;
	static private float velocidade=125;
	
	static private String caminhoImagem="sprites\\mergulhador_spritesheet.png";
	static private int largura=52, altura=56 ;
	static private int colunas=3, linhas=2 ;
	static private int larguraRealSheet=largura*colunas, alturaRealSheet=altura*linhas ;
	
	static private TextureRegion[] framesDireita ;
	static private Animation animacaoDireita ;
	static private TextureRegion[] framesEsquerda ;
	static private Animation animacaoEsquerda ;
	
	private Rectangle retangulo ;
	private Direcao direcao ;
	
	public Mergulhador(int linha) {
		this.direcao = Direcao.getDirecaoAleatoria() ;
		retangulo = new Rectangle(direcao.getXInicial(largura), Background.getAlturaLinha(linha)-(altura/2), largura, altura) ;
	}
	
	static public void montaAnimacao() {
		float tempoEntreFrame = 0.15f ;
		
		Texture imagem = new Texture(caminhoImagem) ;
		
		TextureRegion[][] matrizFrames = TextureRegion.split(imagem, larguraRealSheet/colunas, alturaRealSheet/linhas) ;

		framesDireita = new TextureRegion[colunas+1] ;
		int i ;
		for (i=0; i < colunas; i++) {
			framesDireita[i] = matrizFrames[1][i];
		}
		framesDireita[i] = matrizFrames[1][1] ;
		
		framesEsquerda = new TextureRegion[colunas+1];
		for (i=0; i < colunas; i++) {
			framesEsquerda[i] = matrizFrames[0][i];
		}
		framesEsquerda[i] = matrizFrames[0][1] ;
		
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
		if (retangulo.x > Background.getLargura()) return true ;
		
		return false ;
	}
}
