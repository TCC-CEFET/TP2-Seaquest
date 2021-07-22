import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Submarino {
	// Parametros para imagens
	private int velocidadeX=200, velocidadeY=120 ;
	private int largura=96, altura=36 ;
	private int larguraVida=48, alturaVida=18 ;
	private int larguraMergulhadorSalvo=39, alturaMergulhadorSalvo=28 ;
	
	// Texturas prontas
	private TextureRegion[] framesDireita ;
	private Animation animacaoDireita ;
	private TextureRegion[] framesEsquerda ;
	private Animation animacaoEsquerda ;
	private Texture imagemVida ;
	private Texture imagemMergulhadorSalvo;
	
	private Rectangle retangulo ;
	private int vidas ;
	private int mergulhadoresSalvos ;
	private float levelO2 ;
	private Direcao direcao ;
	private int pontuacao ;
	private boolean jaDesembarcou ;
	private int desembarcou6 ;
	private boolean estaEnchendo ;
	boolean somEnchendoEstaTocando ;
	private Integer idSomEnchendo ;
	private boolean somPoucoO2EstaTocando ;
	private Integer idSomPoucoO2 ;
	private float ultimoTiro ;
	private float ultimoDesenho ;
	
	private ArrayList<TiroSubmarino> tiros ;
	
	
	public Submarino() {
		mergulhadoresSalvos = 0 ;
		desembarcou6 = 0 ;
		jaDesembarcou = true ;
		pontuacao = 0 ;
		vidas = 4 ;
		levelO2 = 0 ;
		estaEnchendo = false ;
		somEnchendoEstaTocando = false ;
		idSomEnchendo = null ;
		somPoucoO2EstaTocando = false ;
		idSomPoucoO2 = null ;
		ultimoTiro = 0 ;
		retangulo = new Rectangle((Background.getLargura()/2)-largura, Ondas.getPosY(), largura, altura) ;
		direcao = Direcao.DIREITA ;
		ultimoDesenho = 0 ;
		tiros = new ArrayList<TiroSubmarino>() ;
		this.montaAnimacao() ;
	}
	
	public void respawna() {
		if (vidas <= 0) {
			retangulo.setX(Background.getLargura()) ;
			return ;
		}
		
		retangulo.setX((Background.getLargura()/2)-largura) ;
		retangulo.setY(Ondas.getPosY()) ;
		jaDesembarcou = true ;
	}
	
	public void controla(float stateTime) {
		// Movimenta os tiros
		Iterator<TiroSubmarino> iterTiros = tiros.iterator() ;
		while (iterTiros.hasNext()) {
			TiroSubmarino tiro = iterTiros.next() ;
			tiro.movimenta() ;
		}
		
		// Sai se nao puder movimentar
		if (vidas <= 0 || estaEnchendo) {
			return ;
		}
		
		// Controla os movimentos
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			direcao = Direcao.ESQUERDA ;
			retangulo.x -= velocidadeX * Gdx.graphics.getDeltaTime();
		}
	    if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
	    	direcao = Direcao.DIREITA ;
	    	retangulo.x += velocidadeX * Gdx.graphics.getDeltaTime();
	    }
	    if (Gdx.input.isKeyPressed(Keys.DOWN)) retangulo.y -= velocidadeY * Gdx.graphics.getDeltaTime();
	    if (Gdx.input.isKeyPressed(Keys.UP)) retangulo.y += velocidadeY * Gdx.graphics.getDeltaTime();
	    
	    if (retangulo.x < 0) retangulo.x = 0;
	    else if(retangulo.x > Background.getLargura()-largura) retangulo.x = Background.getLargura()-largura;
	    
	    if (retangulo.y < Background.getLimiteInferior()) retangulo.y = Background.getLimiteInferior();
	    else if(retangulo.y > Ondas.getPosY()) retangulo.y = Ondas.getPosY();
	    
	    // Controla o tiro
	    if (Gdx.input.isKeyPressed(Keys.SPACE) && retangulo.y < Ondas.getPosY() && stateTime >= ultimoTiro+0.3) {
	    	tiros.add(new TiroSubmarino(direcao == Direcao.DIREITA ? retangulo.x+largura : retangulo.x-TiroSubmarino.getLargura(), retangulo.y+(altura/2), direcao)) ;
	    	ultimoTiro = stateTime ;
	    }
	}
	
	public boolean salvaMergulhador() {
		if (mergulhadoresSalvos >= 6) return false ;
		
		mergulhadoresSalvos++ ;
		return true ;
	}
	
	public void diminuiLevelO2() {
		Sound somPoucoO2 = Gdx.audio.newSound(Gdx.files.internal("sounds\\lowOxygen.mp3")) ;
		jaDesembarcou = false ; // Volta para nao ter desebarcado quando afundar
		
		if (levelO2 <= 0) {
			some() ; // Some se o level de O2 acabou
		}
		
		if (levelO2 > 0 && levelO2 <= 8) {
			if (!somPoucoO2EstaTocando) {
				idSomPoucoO2 = (int) somPoucoO2.loop() ;
				somPoucoO2EstaTocando = true ;
			}
		} else {
			if (somPoucoO2EstaTocando) {
				somPoucoO2.stop(idSomPoucoO2);
				somPoucoO2EstaTocando = false ;
			}
		}
		
		levelO2 -= Gdx.graphics.getDeltaTime() ; // Decrmeta O2 do tanque
	}
	
	public void aumentaLevelO2() {
		Sound somEnchendo = Gdx.audio.newSound(Gdx.files.internal("sounds\\refitOxygen.mp3")) ;
		final int maximoTanque=35 ;
		
		desembarca() ;
		
		if (somPoucoO2EstaTocando) { // Se tiver tocando o alarme para
			somEnchendo.stop(idSomPoucoO2) ;
			somPoucoO2EstaTocando = false ;
		}
		
		if (levelO2 >= maximoTanque) {
			estaEnchendo = false ;
			somEnchendo.stop(idSomEnchendo);
			somEnchendoEstaTocando = false ;
			return ;
		}
		
		if (!somEnchendoEstaTocando) {
			idSomEnchendo = (int) somEnchendo.play() ;
			somEnchendoEstaTocando = true ;
		}
		
		estaEnchendo = true ;
		levelO2 += 16*Gdx.graphics.getDeltaTime() ; // Enche o tanque
	}
	
	public void aumentaPontuacao(int pontos) {
		pontuacao += pontos ;
	}
	
	public void desembarca() {
		if (jaDesembarcou) return ; // Se já fez o desembarque uma vez, nao faz mais até mergulhar novamente
		
		jaDesembarcou = true ;
		
		Inimigo.aumentaVelocidade();
		
		if (mergulhadoresSalvos == 6) {
			desembarcou6 += 1 ;
			mergulhadoresSalvos = 0 ;
			aumentaPontuacao(Mergulhador.getPontos()*6) ;
			
			Inimigo.aumentaPontos() ;
			Mergulhador.aumentaPontos() ;
		} else if (mergulhadoresSalvos == 0) {
			some() ;
		} else {
			mergulhadoresSalvos-- ;
		}
	}
	
	public void montaAnimacao() {
		String caminhoImagem="sprites\\submarino_spritesheet.png";
		int colunas=3, linhas=2 ;
		int larguraRealSheet=largura*colunas, alturaRealSheet=altura*linhas ;
		String caminhoImagemVida="sprites\\vida.png";
		String caminhoImagemMergulhadorSalvo="sprites\\mergulhadorSalvo.png" ;
		
		imagemVida = new Texture(caminhoImagemVida) ;
		imagemMergulhadorSalvo = new Texture(caminhoImagemMergulhadorSalvo) ;
		
		float tempoEntreFrame = 0.08f ;
		
		Texture imagem = new Texture(caminhoImagem) ;
		
		TextureRegion[][] matrizFrames = TextureRegion.split(imagem, larguraRealSheet/colunas, alturaRealSheet/linhas) ;

		framesDireita = new TextureRegion[colunas+1] ;
		int i ;
		for (i=0; i < colunas; i++) {
			framesDireita[i] = matrizFrames[0][i];
		}
		framesDireita[i] = matrizFrames[0][1] ;
		
		framesEsquerda = new TextureRegion[colunas+1];
		for (i=0; i < colunas; i++) {
			framesEsquerda[i] = matrizFrames[1][i];
		}
		framesEsquerda[i] = matrizFrames[1][1] ;
		
		animacaoDireita = new Animation(tempoEntreFrame, framesDireita) ;
		animacaoEsquerda = new Animation(tempoEntreFrame, framesEsquerda) ;
	}
	
	public void anima(SpriteBatch batch, float stateTime) {
		// Anima o submarino
		TextureRegion frameAtual ;
		
		if (direcao == Direcao.DIREITA) frameAtual = animacaoDireita.getKeyFrame(stateTime, true);
		else frameAtual = animacaoEsquerda.getKeyFrame(stateTime, true);
				
		batch.draw(frameAtual, retangulo.x, retangulo.y) ;
		
		// desenha os tiros
		Iterator<TiroSubmarino> iterTiros = tiros.iterator() ;
		while (iterTiros.hasNext()) {
			TiroSubmarino tiro = iterTiros.next() ;
			tiro.anima(batch) ;
		}
		
		// Escreve a pontuacao
		BitmapFont font = new BitmapFont() ;
		font.setColor(239.0f/255.0f, 172.0f/255.0f, 40.0f/255.0f, 255.0f/255.0f) ;
		font.setScale(2) ;
		font.draw(batch, String.valueOf(pontuacao), 420, Background.getAltura()-15) ;
		
		// Desenha as vidas extras
		int xVida=220, yVida=420 ;
		for (int i=0; i < vidas-1; i++) {
			batch.draw(imagemVida, xVida+(i*(larguraVida*1.2f)), yVida) ;
		}
		
		// Desenha os mergulhadores salvos
		int xMergulhadorSalvo=220, yMergulhadorSalvo=2 ;
		for (int i=0; i < mergulhadoresSalvos; i++){
			if (mergulhadoresSalvos >= 6) {
				float tempoPiscar=0.3f ;
				if (stateTime >= ultimoDesenho+tempoPiscar && stateTime <= ultimoDesenho+(tempoPiscar*2)) {
					batch.draw(imagemMergulhadorSalvo, xMergulhadorSalvo+(i*(larguraMergulhadorSalvo*1.2f)), yMergulhadorSalvo) ;
				} else if (stateTime > ultimoDesenho+(tempoPiscar*2)) {
					ultimoDesenho = stateTime ;
				}
			} else {
				batch.draw(imagemMergulhadorSalvo, xMergulhadorSalvo+(i*(larguraMergulhadorSalvo*1.2f)), yMergulhadorSalvo) ;
			}
		}
	}
	
	public void verificaPosicao(ArrayList<Inimigo> inimigos, ArrayList<Mergulhador> mergulhadores, Ondas ondas) {
		// Verifica os inimigos
		Iterator<Inimigo> iterInimigos = inimigos.iterator() ;
		while (iterInimigos.hasNext()) {
			Inimigo inimigo = iterInimigos.next() ;
			
			if (retangulo.overlaps(inimigo.getRetangulo())) {
				some() ;
				inimigo.some(this) ;
				iterInimigos.remove() ;
			}
		}
		
		// Verifica os mergulhadores
		Iterator<Mergulhador> iterMergulhadores = mergulhadores.iterator() ;
		while (iterMergulhadores.hasNext()) {
			Mergulhador mergulhador = iterMergulhadores.next() ;
			
			if (retangulo.overlaps(mergulhador.getRetangulo())) {
				if (salvaMergulhador()) {
					mergulhador.some() ;
					iterMergulhadores.remove() ;
				}
			}
		}
		
		// Verifica se ta no topo
		if (this.retangulo.y >= 356) {
			aumentaLevelO2() ;
		} if (this.retangulo.overlaps(ondas.getRetanguloCima())) {
			// Mantem o O2 constante
		} else {
			diminuiLevelO2() ;
		}
	}
	
	public void some() {
		Gdx.audio.newSound(Gdx.files.internal("sounds\\destroyPlayer.mp3")).play() ;
		
		vidas-- ;
		levelO2 = 0 ;
		if (mergulhadoresSalvos > 0) mergulhadoresSalvos-- ;
		respawna() ;
	}
	
	public int getDesembarcou6() {
		return this.desembarcou6 ;
	}
	
	public ArrayList<TiroSubmarino> getTiros() {
		return tiros ;
	}
}
