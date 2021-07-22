import com.badlogic.gdx.Gdx;

public class TiroSubmarino extends Tiro {
	static private String caminhoImagem="sprites\\tiroSubmarino.png";
	static private int largura=48, altura=4 ;
	static private int velocidade=800 ;
	
	public TiroSubmarino(float x, float y, Direcao direcao, Background fundo) {
		super(caminhoImagem, x, y, largura, altura, velocidade, direcao, fundo) ;
		Gdx.audio.newSound(Gdx.files.internal("sounds\\fireTorpedo.mp3")).play() ;
	}

	public static int getLargura() {
		return largura;
	}
}
