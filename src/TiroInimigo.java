
public class TiroInimigo extends Tiro {
	static private String caminhoImagem="sprites\\tiroInimigo.png";
	static private int largura=48, altura=16 ;
	static private int velocidade=Inimigo.getVelocidade()+200 ;
	
	public TiroInimigo(float x, float y, Direcao direcao, Background fundo) {
		super(caminhoImagem, x, y, largura, altura, velocidade, direcao, fundo);
	}
	
	public static int getLargura() {
		return largura;
	}
}
