import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class Tubarao extends Inimigo {	
	static private String caminhoSpriteSheet="sprites\\tubarao_spritesheet.png";
	static private String caminhoAudio="sounds\\destroyShark.mp3";
	static float tempoEntreFrame = 0.25f ;
	static private int largura=48, altura=16 ;
	static private int colunas=3, linhas=2 ;
	
	public Tubarao(int linha) {
		super(linha, caminhoSpriteSheet, largura, altura, colunas, linhas, tempoEntreFrame, caminhoAudio) ;
	}
	
//	static private int velocidadeVertical = 25;
//	private float tempoOscilacao = 0 ; 
//	@Override
//	public void movimenta() {
//		tempoOscilacao += Gdx.graphics.getDeltaTime();
//		
//		if (tempoOscilacao >= 0.5) {
//			tempoOscilacao = 0;
//			velocidadeVertical *= -1 ;
//		}
//		retangulo.y += velocidadeVertical * Gdx.graphics.getDeltaTime() ;
//		
//		super.movimenta() ;
//	}
}
