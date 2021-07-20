
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Ondas {
	static private String caminhoImagem="sprites\\ondas_spritesheet.png";
	static private int largura=Background.getLargura(), altura=16 ;
	static private int colunas=1, linhas=1 ;
	static private int larguraRealSheet=largura*colunas, alturaRealSheet=altura*linhas ;
	static private int posY=356 ;
	
	static private TextureRegion[] frames ;
	static private Animation animacao ;
	
	private Rectangle retangulo ;
	
	public Ondas() {
		retangulo = new Rectangle(0, Ondas.posY, largura, altura) ;
		this.montaAnimacao();
	}
	
	static public int getPosY() {
		return Ondas.posY ;
	}
	
	public void montaAnimacao() {
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
	
	public void anima(SpriteBatch batch, float stateTime) {
		TextureRegion frameAtual = animacao.getKeyFrame(stateTime, true);
				
		batch.draw(frameAtual, retangulo.x, retangulo.y) ;
	}
}
