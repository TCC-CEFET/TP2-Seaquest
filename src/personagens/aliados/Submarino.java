package personagens.aliados;
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

import ambiente.*;
import personagens.aliados.*;
import personagens.caracteristicas.*;
import personagens.inimigos.*;
import rodar.*;
import tiros.*;

// Classe submarino que é controlado pelo jogador
public class Submarino {
	private Background fundo ;
	private Ondas ondas ;
	
	// Parametros para imagens
	private int velocidadeX=200, velocidadeY=120 ;
	private int largura=96, altura=36 ;
	private int larguraVida=48, alturaVida=18 ;
	private int larguraMergulhadorSalvo=39, alturaMergulhadorSalvo=28 ;
	
	// Animacoes e texturas
	private Animation animacaoDireita ;	
	private Animation animacaoEsquerda ;
	private Animation animacaoExplosaoEsquerda ;
	private Animation animacaoExplosaoDireita ;
	private Texture imagemVida ;
	private Texture imagemMergulhadorSalvo ;
	
	// Prorpiedades do submarino
	private Rectangle retangulo ;
	private int vidas ;
	private int mergulhadoresSalvos ;
	private float levelO2 ;
	private Direcao direcao ;
	private int pontuacao ;
	
	private int maximoTanqueO2 = 35 ;
	
	// Tiros
	private ArrayList<TiroSubmarino> tiros ;
	
	// Variaveis de marcacao
	private boolean jaDesembarcou ;
	private int desembarcou6 ;
	
	private boolean estaEnchendo ;
	boolean somEnchendoEstaTocando ;
	private Integer idSomEnchendo ;
	private boolean somPoucoO2EstaTocando ;
	private Integer idSomPoucoO2 ;
	
	private float ultimoTiro ;
	private float ultimoDesenho ;
	private int vidasExtrasAdiquiridas ;
	
	private boolean estaDesembarcando ;
	private boolean explodindo ;
	private Float tempoInicialExplosao ;
	private boolean morreu ;
	
	public Submarino(Background fundo, Ondas ondas) {
		this.fundo = fundo ;
		this.ondas = ondas ;
		mergulhadoresSalvos = 0 ;
		desembarcou6 = 0 ;
		direcao = Direcao.DIREITA ;
		pontuacao = 0 ;
		vidas = 4 ;
		levelO2 = 0 ;
		retangulo = new Rectangle((fundo.getLargura()/2)-largura, ondas.getPosY(), largura, altura) ;
		
		tiros = new ArrayList<TiroSubmarino>() ;
		
		jaDesembarcou = true ;
		estaEnchendo = false ;
		somEnchendoEstaTocando = false ;
		idSomEnchendo = null ;
		somPoucoO2EstaTocando = false ;
		idSomPoucoO2 = null ;
		
		ultimoTiro = 0 ;
		ultimoDesenho = 0 ;
		estaDesembarcando = false ;
		explodindo = false ;
		morreu = false ;
		vidasExtrasAdiquiridas = 0 ;
		
		this.montaAnimacao() ;
	}
	
	// Monta as animacoes a partir do sprite sheet
	public void montaAnimacao() {
		String caminhoImagem="sprites\\submarino_spritesheet.png";
		int colunas=5, linhas=3 ;
		int colunasNormal=3 ; // Primeiras colunas
		int colunasExplosao=2 ; // colunas finais
		int larguraRealSheet=largura*colunas, alturaRealSheet=altura*linhas ;
		String caminhoImagemVida="sprites\\vida.png";
		String caminhoImagemMergulhadorSalvo="sprites\\mergulhadorSalvo.png" ;
		
		// Pega a imagem de vida
		imagemVida = new Texture(caminhoImagemVida) ;
		imagemMergulhadorSalvo = new Texture(caminhoImagemMergulhadorSalvo) ;
		
		float tempoEntreFrame = 0.08f ;
		float tempoEntreFrameExplosao = 0.15f ;
		
		// Pega o sprite sheet
		Texture imagem = new Texture(caminhoImagem) ;
		
		// Monta uma matriz de regiao
		TextureRegion[][] matrizFrames = TextureRegion.split(imagem, larguraRealSheet/colunas, alturaRealSheet/linhas) ;
		
		// Monta a ordem de texturas para direita
		TextureRegion[] framesDireita = new TextureRegion[colunasNormal+1] ;
		int i ;
		for (i=0; i < colunasNormal; i++) {
			framesDireita[i] = matrizFrames[0][i];
		}
		framesDireita[i] = matrizFrames[0][1] ;
		
		// Monta a ordem de texturas para esquerda
		TextureRegion[] framesEsquerda = new TextureRegion[colunasNormal+1];
		for (i=0; i < colunasNormal; i++) {
			framesEsquerda[i] = matrizFrames[1][i];
		}
		framesEsquerda[i] = matrizFrames[1][1] ;
		
		// Monta as animacoes
		animacaoDireita = new Animation(tempoEntreFrame, framesDireita) ;
		animacaoEsquerda = new Animation(tempoEntreFrame, framesEsquerda) ;
		
		// Monta a ordem de texturas para explosao direita
		int quantidadeFramesExplosao = 10 ;
		TextureRegion[] framesExplosaoDireita = new TextureRegion[quantidadeFramesExplosao] ;
		for (i=0; i < 4; i+=2) {
			framesExplosaoDireita[i] = matrizFrames[0][3];
			framesExplosaoDireita[i+1] = matrizFrames[0][4];
		}
		for (i=4; i < quantidadeFramesExplosao; i+=3) {
			framesExplosaoDireita[i] = matrizFrames[2][0];
			framesExplosaoDireita[i+1] = matrizFrames[2][1];
			framesExplosaoDireita[i+2] = matrizFrames[2][2];
		}
		
		// Monta a ordem de texturas para explosao esquerda
		TextureRegion[] framesExplosaoEsquerda = new TextureRegion[quantidadeFramesExplosao] ;
		for (i=0; i < 4; i+=2) {
			framesExplosaoEsquerda[i] = matrizFrames[1][3];
			framesExplosaoEsquerda[i+1] = matrizFrames[1][4];
		}
		for (i=4; i < quantidadeFramesExplosao; i+=3) {
			framesExplosaoEsquerda[i] = matrizFrames[2][0];
			framesExplosaoEsquerda[i+1] = matrizFrames[2][1];
			framesExplosaoEsquerda[i+2] = matrizFrames[2][2];
		}
		
		// Monta as animacoes explosao
		animacaoExplosaoDireita = new Animation(tempoEntreFrameExplosao, framesExplosaoDireita) ;
		animacaoExplosaoEsquerda = new Animation(tempoEntreFrameExplosao, framesExplosaoEsquerda) ;
	}
	
	// Coloca os itens em tela
	public void anima(SpriteBatch batch, float stateTime, OrthographicCamera camera) {
		// Anima o submarino
		TextureRegion frameAtual = null ;
		float tempoExplosao=1.5f ;
		
		// Verifica o estado
		if (explodindo) {
			if (tempoInicialExplosao == null) {
				tempoInicialExplosao = stateTime ;
			} else if (tempoInicialExplosao+tempoExplosao < stateTime) {
				tempoInicialExplosao = null ;
				explodindo = false ;
				respawna() ;
				return ;
			}
			if (direcao == Direcao.DIREITA) frameAtual = animacaoExplosaoDireita.getKeyFrame(stateTime-tempoInicialExplosao, true);
			else frameAtual = animacaoExplosaoEsquerda.getKeyFrame(stateTime-tempoInicialExplosao, true);
		} else {
			if (direcao == Direcao.DIREITA) frameAtual = animacaoDireita.getKeyFrame(stateTime, true);
			else frameAtual = animacaoEsquerda.getKeyFrame(stateTime, true);
		}
		
		batch.draw(frameAtual, retangulo.x, retangulo.y) ;
		
		// Desenha os tiros
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
			if (mergulhadoresSalvos >= 6) { // Faz piscar quando tiver 6 mergulhadores
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
	
	// Verifica as posicoes
	public void verificaPosicao(ArrayList<Inimigo> inimigos, ArrayList<Mergulhador> mergulhadores, Ondas ondas) {
		if (explodindo || morreu) return ; // Sai se nao for para fazer a verificacao
		
		// Verifica os inimigos
		Iterator<Inimigo> iterInimigos = inimigos.iterator() ;
		while (iterInimigos.hasNext()) {
			Inimigo inimigo = iterInimigos.next() ;
			
			if (retangulo.overlaps(inimigo.getRetangulo())) {
				some() ;
				inimigo.some(this) ;
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
		
		// Verifica se nao saiu de tela
		if (vidas > 0) {
			if (retangulo.x < 0) retangulo.x = 0;
			else if(retangulo.x > fundo.getLargura()-largura) retangulo.x = fundo.getLargura()-largura;
			    
			if (retangulo.y < fundo.getLimiteInferior()) retangulo.y = fundo.getLimiteInferior();
			else if(retangulo.y > ondas.getPosY()) retangulo.y = ondas.getPosY();
		}
		
		if (explodindo) return ; // Verifica se deve parar execucao
		// Verifica se ta no topo
		if (this.retangulo.y >= ondas.getPosY()) {
			desembarca() ;
			aumentaLevelO2() ;
		} if (this.retangulo.overlaps(ondas.getRetanguloCima())) {
			// Mantem o O2 constante
		} else {
			diminuiLevelO2() ;
		}
	}
	
	// Recebe os inputs do usuario e realiza as funcoes de movimento
	public void controla(float stateTime) {
		// Movimenta os tiros
		Iterator<TiroSubmarino> iterTiros = tiros.iterator() ;
		while (iterTiros.hasNext()) {
			TiroSubmarino tiro = iterTiros.next() ;
			tiro.movimenta() ;
		}

		// Sai se nao puder movimentar
		if (vidas <= 0 || estaEnchendo || explodindo) {
			return ;
		}

		// Controla os movimentos por tecla
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
	    
		// Controla o tiro
		if (Gdx.input.isKeyPressed(Keys.SPACE) && retangulo.y < ondas.getPosY() && stateTime >= ultimoTiro+0.3) {
			tiros.add(new TiroSubmarino(direcao == Direcao.DIREITA ? retangulo.x+largura : retangulo.x-TiroSubmarino.getLargura(), retangulo.y+(altura/2), direcao, fundo)) ;
			ultimoTiro = stateTime ;
		}
	}
	
	// Resgata o mergulhador se tiver espaco
	public boolean salvaMergulhador() {
		if (mergulhadoresSalvos >= 6) return false ;
		
		mergulhadoresSalvos++ ;
		return true ;
	}
	
	// Verifica o desembarque quando subir
	public void desembarca() {
		if (jaDesembarcou) return ; // Se ja fez o desembarque uma vez, nao faz mais ate mergulhar novamente
		
		// Verifica quantos mergulhadores estao quando sobe
		if (mergulhadoresSalvos == 6 || estaDesembarcando) {
			// Executa o caso para 6 mergulhadores ate terminar todos
			estaDesembarcando = true ;
			
			if (levelO2 > 0) { // Ate o O2 acabar
				levelO2 -= maximoTanqueO2/4 ; // Diminui 1/4 do tanque
				Gdx.audio.newSound(Gdx.files.internal("sounds\\dropOxygen.mp3")).play() ; // Toca o som de esvaziar para pegar money
				aumentaPontuacao((int) levelO2/4*40) ; // Aumenta parte da pontuacao
				try { TimeUnit.MILLISECONDS.sleep(500) ; } // Pausa por 0,5s para tocar o som
				catch (InterruptedException e) { e.printStackTrace(); }
				if (levelO2 < 0) levelO2 = 0 ;
				return ;
			}
			
			mergulhadoresSalvos -= 1 ;
			aumentaPontuacao(Mergulhador.getPontos()) ;
			
			Gdx.audio.newSound(Gdx.files.internal("sounds\\deliverDiver.mp3")).play() ; // Toca o som
			
			try { TimeUnit.MILLISECONDS.sleep(300) ; }
			catch (InterruptedException e) { e.printStackTrace(); }
			if (mergulhadoresSalvos == 0) {
				desembarcou6 += 1 ;
				jaDesembarcou = true ;
				Mergulhador.aumentaPontos() ;
				Inimigo.aumentaPontos() ;
				estaDesembarcando = false ;
				Inimigo.aumentaVelocidadePadrao();
			}
		} else if (mergulhadoresSalvos == 0) {
			// Executa o caso para 0 mergulhadores
			some() ;
			jaDesembarcou = true ;
			Inimigo.aumentaVelocidadePadrao();
		} else {
			// Executa o caso para 1-5 mergulhadores
			mergulhadoresSalvos-- ;
			jaDesembarcou = true ;
			Inimigo.aumentaVelocidadePadrao();
		}
	}
	
	// Diminui o levle de O2 caso emrgulhado
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
	
	// Enche o tanque de O2 caso nao mergulhado
	public void aumentaLevelO2() {
		Sound somEnchendo = Gdx.audio.newSound(Gdx.files.internal("sounds\\refitOxygen.mp3")) ;
		
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
		
		// Da mais vida caso passe de 10 mil pontos
		if (pontuacao >= 10000*(vidasExtrasAdiquiridas+1)) {
			aumentaVidas() ;
			vidasExtrasAdiquiridas++ ;
		}
	}
	
	public void aumentaVidas() {
		if (vidas < 7) vidas++ ;
	}
	
	// Aconada quando o submarino morre
	public void some() {
		if (explodindo || vidas <= 0) return ;
		morreu = true ;
		Gdx.audio.newSound(Gdx.files.internal("sounds\\destroyPlayer.mp3")).play() ;
		explodindo = true ;
		
		vidas-- ;
		levelO2 = 0 ;
		if (mergulhadoresSalvos > 0) mergulhadoresSalvos-- ;
	}
	
	// Nasce novamente o submarino
	public void respawna() {
		morreu = false ;
		
		if (vidas <= 0) {
			retangulo.setY(fundo.getAltura()*-3) ;
			return ;
		}
		
		retangulo.setX((fundo.getLargura()/2)-largura) ;
		retangulo.setY(ondas.getPosY()) ;
		jaDesembarcou = true ;
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
	
	public boolean getEstaDesembarcando() {
		return estaDesembarcando ;
	}
	
	public boolean getMorreu() {
		return morreu ;
	}
	
	public void dispose() {
		fundo.dispose() ;
		imagemVida.dispose() ;
		imagemMergulhadorSalvo.dispose() ;
		
		Iterator<TiroSubmarino> iterTiros = tiros.iterator() ;
		while (iterTiros.hasNext()) {
			TiroSubmarino tiro = iterTiros.next() ;
			tiro.dispose() ;
		}
		
		Sound som = Gdx.audio.newSound(Gdx.files.internal("sounds\\lowOxygen.mp3")) ;
		if (somPoucoO2EstaTocando) som.stop(idSomPoucoO2) ;
		if (somEnchendoEstaTocando) som.stop(idSomEnchendo) ;
		
		som.dispose() ;
	}
}
