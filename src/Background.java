import com.badlogic.gdx.graphics.Texture;

public class Background {
	private Texture imagem;
	
	public Background(String caminho) {
		super();
		this.imagem = new Texture(caminho);
	}
	public Texture getImage() {
		return this.imagem;
	}
}
