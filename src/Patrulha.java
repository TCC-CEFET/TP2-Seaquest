
import java.util.Random;

import com.badlogic.gdx.math.Rectangle;

public class Patrulha extends Inimigo {
	static private String caminhoSpriteSheet="sprites\\inimigo_spritesheet.png";
	static private String caminhoAudio="sounds\\destroyEnemySub.mp3";
	static float tempoEntreFrame = 0.08f ;
	static private int largura=48, altura=30 ;
	static private int colunas=3, linhas=2 ;
	
	public Patrulha() {
		super(4, caminhoSpriteSheet, largura, altura, colunas, linhas, tempoEntreFrame, caminhoAudio) ;
	}
}
