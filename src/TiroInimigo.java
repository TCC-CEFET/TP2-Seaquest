
public class TiroInimigo extends Tiro {
	static private String caminhoImagem="sprites\\tiroInimigo.png";
	static private int largura=48, altura=16 ;
	static private int velocidade=Inimigo.getVelocidade()+100 ;
	
	public TiroInimigo(float x, float y, Direcao direcao) {
		super(caminhoImagem, x, y, largura, altura, velocidade, direcao);
	}
	
	public static int getLargura() {
		return largura;
	}
}
