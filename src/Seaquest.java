
import com.badlogic.gdx.backends.lwjgl.LwjglApplication ;

public class Seaquest {

	public static void main (String[] args) {
	      System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");

        new LwjglApplication(new Jogo(), "Seaquest", 800, 480, false);
	}
}