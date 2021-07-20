import com.badlogic.gdx.math.Rectangle;

public class SubmarinoInimigo extends Inimigo {
	static private String caminhoSpriteSheet="sprites\\inimigo_spritesheet.png";
	static float tempoEntreFrame = 0.08f ;
	static private int largura=48, altura=30 ;
	static private int colunas=3, linhas=2 ;
	
	public SubmarinoInimigo(int linha) {
		super(linha, caminhoSpriteSheet, largura, altura, colunas, linhas, tempoEntreFrame) ;
	}
}
