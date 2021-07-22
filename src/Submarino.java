import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class Submarino {
	private Background fundo ;
	private Ondas ondas ;
	
	// Parametros para imagens
	private int velocidadeX=200, velocidadeY=120 ;
	private int largura=96, altura=36 ;
	private int larguraVida=48, alturaVida=18 ;
	private int larguraMergulhadorSalvo=39, alturaMergulhadorSalvo=28 ;
	
	// Texturas prontas
	private Animation animacaoDireita ;	
	private Animation animacaoEsquerda ;
	
	private Animation animacaoExplosaoEsquerda ;
	private Animation animacaoExplosaoDireita ;
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
	private int maximoTanqueO2 = 35 ;
	private boolean estaDesembarcando ;
	private boolean explodindo ;
	private Float tempoInicialExplosao ;
	
	private ArrayList<TiroSubmarino> tiros ;
	
	
	public Submarino(Background fundo, Ondas ondas) {
		this.fundo = fundo ;
		this.ondas = ondas ;
		mergulhadoresSalvos = 0 ;
		desembarcou6 = 0 ;
		jaDesembarcou = true ;
		pontuacao = 0 ;
		vidas = 1 ;//4 ;
		levelO2 = 0 ;
		estaEnchendo = false ;
		somEnchendoEstaTocando = false ;
		idSomEnchendo = null ;
		somPoucoO2EstaTocando = false ;
		idSomPoucoO2 = null ;
		ultimoTiro = 0 ;
		retangulo = new Rectangle((fundo.getLargura()/2)-largura, ondas.getPosY(), largura, altura) ;
		direcao = Direcao.DIREITA ;
		ultimoDesenho = 0 ;
		tiros = new ArrayList<TiroSubmarino>() ;
		estaDesembarcando = false ;
		explodindo = false ;
		this.montaAnimacao() ;
	}
	
	public void respawna() {
		if (vidas <= 0) {
			retangulo.setX(fundo.getLargura()) ;
			return ;
		}
		
		retangulo.setX((fundo.getLargura()/2)-largura) ;
		retangulo.setY(ondas.getPosY()) ;
		jaDesembarcou = true ;
	}
	
	public void controla(float stateTime) {
		if (explodindo) return ;
		
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
	    else if(retangulo.x > fundo.getLargura()-largura) retangulo.x = fundo.getLargura()-largura;
	    
	    if (retangulo.y < fundo.getLimiteInferior()) retangulo.y = fundo.getLimiteInferior();
	    else if(retangulo.y > ondas.getPosY()) retangulo.y = ondas.getPosY();
	    
	    // Controla o tiro
	    if (Gdx.input.isKeyPressed(Keys.SPACE) && retangulo.y < ondas.getPosY() && stateTime >= ultimoTiro+0.3) {
	    	tiros.add(new TiroSubmarino(direcao == Direcao.DIREITA ? retangulo.x+largura : retangulo.x-TiroSubmarino.getLargura(), retangulo.y+(altura/2), direcao, fundo)) ;
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
		
		if (vidas > 0) levelO2 -= Gdx.graphics.getDeltaTime() ; // Decrmeta O2 do tanque
	}
	
	public void aumentaLevelO2() {
		Sound somEnchendo = Gdx.audio.newSound(Gdx.files.internal("sounds\\refitOxygen.mp3")) ;
		
		desembarca() ;
		if (estaDesembarcando) return ;
		
		if (somPoucoO2EstaTocando) { // Se tiver tocando o alarme para
			somEnchendo.stop(idSomPoucoO2) ;
			somPoucoO2EstaTocando = false ;
		}
		
		if (levelO2 >= maximoTanqueO2) {
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
		
		if (mergulhadoresSalvos == 6 || estaDesembarcando) {
			estaDesembarcando = true ;
			
			ShapeRenderer shapeRenderer = new ShapeRenderer() ;
			shapeRenderer.begin(ShapeType.FilledRectangle) ;
			shapeRenderer.setColor(35/255.0f, 14/255.0f, 94/255.0f, 1) ;
			shapeRenderer.filledRect(0, 84, fundo.getLargura(), 272);
			shapeRenderer.end() ;
			
			if (levelO2 >= 1) {
				levelO2 -= maximoTanqueO2/4 ;
				Gdx.audio.newSound(Gdx.files.internal("sounds\\dropOxygen.mp3")).play() ;
				aumentaPontuacao((int) levelO2/4*40) ;
				try { TimeUnit.MILLISECONDS.sleep(500) ; }
				catch (InterruptedException e) { e.printStackTrace(); }
				if (levelO2 < 0) levelO2 = 0 ;
				return ;
			}
			
			mergulhadoresSalvos -= 1 ;
			aumentaPontuacao(Mergulhador.getPontos()) ;
			
			Gdx.audio.newSound(Gdx.files.internal("sounds\\deliverDiver.mp3")).play() ;
			
			try { TimeUnit.MILLISECONDS.sleep(300) ; }
			catch (InterruptedException e) { e.printStackTrace(); }
			if (mergulhadoresSalvos == 0) {
				desembarcou6 += 1 ;
				jaDesembarcou = true ;
				Mergulhador.aumentaPontos() ;
				Inimigo.aumentaPontos() ;
				estaDesembarcando = false ;
				Inimigo.aumentaVelocidade();
			}
		} else if (mergulhadoresSalvos == 0) {
			some() ;
			jaDesembarcou = true ;
			Inimigo.aumentaVelocidade();
		} else {
			mergulhadoresSalvos-- ;
			jaDesembarcou = true ;
			Inimigo.aumentaVelocidade();
		}
	}
	
	public void montaAnimacao() {
		TextureRegion[] framesDireita ;
		TextureRegion[] framesEsquerda ;
		TextureRegion[] framesExplosaoEsquerda ;
		TextureRegion[] framesExplosaoDireita ;
		
		String caminhoImagem="sprites\\submarino_spritesheet.png";
		int colunas=5, linhas=2 ;
		int colunasNormal=3 ; // Primeiras colunas
		int colunasExplosao=2 ; // colunas finais
		int larguraRealSheet=largura*colunas, alturaRealSheet=altura*linhas ;
		String caminhoImagemVida="sprites\\vida.png";
		String caminhoImagemMergulhadorSalvo="sprites\\mergulhadorSalvo.png" ;
		
		imagemVida = new Texture(caminhoImagemVida) ;
		imagemMergulhadorSalvo = new Texture(caminhoImagemMergulhadorSalvo) ;
		
		float tempoEntreFrame = 0.08f ;
		float tempoEntreFrameExplosao = 0.15f ;
		
		Texture imagem = new Texture(caminhoImagem) ;
		
		TextureRegion[][] matrizFrames = TextureRegion.split(imagem, larguraRealSheet/colunas, alturaRealSheet/linhas) ;

		framesDireita = new TextureRegion[colunasNormal+1] ;
		int i ;
		for (i=0; i < colunasNormal; i++) {
			framesDireita[i] = matrizFrames[0][i];
		}
		framesDireita[i] = matrizFrames[0][1] ;
		
		framesEsquerda = new TextureRegion[colunasNormal+1];
		for (i=0; i < colunasNormal; i++) {
			framesEsquerda[i] = matrizFrames[1][i];
		}
		framesEsquerda[i] = matrizFrames[1][1] ;
		
		animacaoDireita = new Animation(tempoEntreFrame, framesDireita) ;
		animacaoEsquerda = new Animation(tempoEntreFrame, framesEsquerda) ;
		
		framesExplosaoDireita = new TextureRegion[colunasExplosao] ;
		for (i=0; i < 6; i+=2) {
			framesExplosaoDireita[0] = matrizFrames[0][colunas-colunasExplosao];
			framesExplosaoDireita[1] = matrizFrames[0][colunas-colunasExplosao+1];
		}
		
		framesExplosaoEsquerda = new TextureRegion[colunasExplosao] ;
		framesExplosaoEsquerda[0] = matrizFrames[1][colunas-colunasExplosao];
		framesExplosaoEsquerda[1] = matrizFrames[1][colunas-colunasExplosao+1];
		
		animacaoExplosaoDireita = new Animation(tempoEntreFrameExplosao, framesExplosaoDireita) ;
		animacaoExplosaoEsquerda = new Animation(tempoEntreFrameExplosao, framesExplosaoEsquerda) ;
	}

	public void anima(SpriteBatch batch, float stateTime, OrthographicCamera camera) {
		// Anima o submarino
		TextureRegion frameAtual = null ;
		
		float tempoExplosao=1.5f ;
		if (explodindo) {
			if (tempoInicialExplosao == null) {
				tempoInicialExplosao = stateTime ;
			} else if (tempoInicialExplosao+tempoExplosao < stateTime) {
				tempoInicialExplosao = null ;
				explodindo = false ;
				respawna() ;
				return ;
			}
			if (direcao == Direcao.DIREITA) frameAtual = animacaoExplosaoDireita.getKeyFrame(stateTime, true);
			else frameAtual = animacaoExplosaoEsquerda.getKeyFrame(stateTime, true);
		} else {
			if (direcao == Direcao.DIREITA) frameAtual = animacaoDireita.getKeyFrame(stateTime, true);
			else frameAtual = animacaoEsquerda.getKeyFrame(stateTime, true);
		}
				
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
		font.draw(batch, String.valueOf(pontuacao), 420, fundo.getAltura()-15) ;
		
		// Desenha o tanque de O2
		batch.end() ;
		ShapeRenderer shapeRenderer = new ShapeRenderer() ;
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.FilledRectangle) ;
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.filledRect(160, 33, 11.5f*maximoTanqueO2, 20);
		shapeRenderer.setColor(Color.LIGHT_GRAY) ;
		shapeRenderer.filledRect(160, 33, 11.5f*levelO2, 20);
		shapeRenderer.end();
		batch.begin() ;
		
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
	
	public void verificaPosicao(ArrayList<Inimigo> inimigos, ArrayList<Mergulhador> mergulhadores) {
		if (explodindo) return ;
		
		// Verifica os inimigos
		Iterator<Inimigo> iterInimigos = inimigos.iterator() ;
		while (iterInimigos.hasNext()) {
			Inimigo inimigo = iterInimigos.next() ;
			
			if (retangulo.overlaps(inimigo.getRetangulo())) {
				some() ;
				inimigo.morre(this) ;
				break ;
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
		
		if (explodindo) return ;
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
		if (explodindo || vidas <= 0) return ;
		Gdx.audio.newSound(Gdx.files.internal("sounds\\destroyPlayer.mp3")).play() ;
		explodindo = true ;
		
		vidas-- ;
		levelO2 = 0 ;
		if (mergulhadoresSalvos > 0) mergulhadoresSalvos-- ;
	}
	
	public int getDesembarcou6() {
		return this.desembarcou6 ;
	}
	
	public ArrayList<TiroSubmarino> getTiros() {
		return tiros ;
	}
	
	public Rectangle getRetangulo() {
		return retangulo ;
	}
	
	public boolean getExplodindo() {
		return explodindo ;
	}
}
