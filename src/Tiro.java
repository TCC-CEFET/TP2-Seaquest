
import com.badlogic.gdx.audio.Sound ;
import com.badlogic.gdx.graphics.Texture ;
import com.badlogic.gdx.math.Rectangle ;

public class Tiro {
	protected Texture imagem;
	protected Rectangle retangulo;
	
	public Tiro(String caminhoImagem, String caminhoSom, int x, int y, int largura, int altura) {
		retangulo = new Rectangle(x, y, largura, altura);
	}

	protected void Atira(float velocidade) {
		
	}
}
