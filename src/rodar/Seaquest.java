package rodar;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication ;

// Classe que chama o jogo
public class Seaquest {

	public static void main (String[] args) {
		// Dimensoes da tela
		int larguraTela = 800 ;
		int alturaTela = 480 ;
		
		// Inicia o programa
		System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true") ;
		new LwjglApplication(new Jogo(), "Seaquest", larguraTela, alturaTela, false) ;
	}
}
