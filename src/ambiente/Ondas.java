package ambiente;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

// Classe das ondas da "superficie"
public class Ondas {
	// Variaveis de posicao
	private int largura, altura ;
	private int posY ;
	
	// Textura
	private TextureRegion[] frames ;
	private Animation animacao ;
	
	private Rectangle retanguloBaixo ; // Parte de cima das ondas
	private Rectangle retanguloCima ; // Parte de cima das ondas
	
	public Ondas(Background fundo) {
		largura=fundo.getLargura() ;
		altura=16 ;
		posY=356 ;
		
		retanguloBaixo = new Rectangle(0, posY, largura, altura) ;
		retanguloCima = new Rectangle(0, posY + altura, largura, altura) ;
		this.montaAnimacao();
	}
	
	public int getPosY() {
		return this.posY ;
	}
	
	public Rectangle getRetanguloCima() {
		return this.retanguloCima ;
	}
	
	// Monta a animacao
	public void montaAnimacao() {
		String caminhoImagem="sprites\\ondas_spritesheet.png";
		int colunas=1, linhas=1 ;
		int larguraRealSheet=largura*colunas, alturaRealSheet=altura*linhas ;
		
		float tempoEntreFrame = 0.15f ;
		
		Texture imagem = new Texture(caminhoImagem) ;
		
		TextureRegion[][] matrizFrames = TextureRegion.split(imagem, larguraRealSheet/colunas, alturaRealSheet/linhas) ;

		frames = new TextureRegion[colunas] ;
		for (int i=0; i < colunas; i++) {
			for (int j=0; j < linhas; j++) {
				frames[i] = matrizFrames[j][i];
			}
		}
		
		animacao = new Animation(tempoEntreFrame, frames) ;
	}
	
	// Coloca a imagem no local
	public void anima(SpriteBatch batch, float stateTime) {
		TextureRegion frameAtual = animacao.getKeyFrame(stateTime, true);
				
		batch.draw(frameAtual, retanguloBaixo.x, retanguloBaixo.y) ;
	}
}
