package Rodar;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication ;

public class Seaquest {

	public static void main (String[] args) {
		int larguraTela = 800 ;
		int alturaTela = 480 ;
		
		System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true") ;
		new LwjglApplication(new Jogo(), "Seaquest", larguraTela, alturaTela, false) ;
	}
}