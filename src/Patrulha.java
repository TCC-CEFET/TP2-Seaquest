
import java.util.Random;

import com.badlogic.gdx.math.Rectangle;

public class Patrulha extends Inimigo {
	static private String caminhoSpriteSheet="sprites\\inimigo_spritesheet.png";
	static float tempoEntreFrame = 0.08f ;
	static private int largura=48, altura=30 ;
	static private int colunas=3, linhas=2 ;
	
	public Patrulha() {
		super(new Random().nextInt(2) == 0 ? 0-largura : PropriedadesTela.getLargura(), Ondas.getPosY()+6, caminhoSpriteSheet, largura, altura, colunas, linhas, tempoEntreFrame) ;
		
		if (retangulo.x == 0-largura) direcao = Direcao.DIREITA ;
		else direcao = Direcao.ESQUERDA ;
	}
}
