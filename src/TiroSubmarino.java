
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class TiroSubmarino extends Tiro {
	static private String caminhoImagem="sprites\\tiroSubmarino.png";
	static private int largura=48, altura=4 ;
	static private int velocidade=800 ;
	
	public TiroSubmarino(float x, float y, Direcao direcao) {
		super(caminhoImagem, x, y, largura, altura, velocidade, direcao) ;
		Gdx.audio.newSound(Gdx.files.internal("sounds\\fireTorpedo.mp3")).play() ;
	}

	public static int getLargura() {
		return largura;
	}
}
