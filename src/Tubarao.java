import com.badlogic.gdx.math.Rectangle;

public class Tubarao extends Inimigo {	
	static private String caminhoSpriteSheet="sprites\\tubarao_spritesheet.png";
	static float tempoEntreFrame = 0.25f ;
	static private int largura=48, altura=16 ;
	static private int colunas=3, linhas=2 ;
	
	public Tubarao(int linha) {
		super(linha, caminhoSpriteSheet, largura, altura, colunas, linhas, tempoEntreFrame) ;
	}
	
}
