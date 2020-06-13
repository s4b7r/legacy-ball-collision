/**
 * 
 */
package pac1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;

/**
 * @author 
 *
 */
public class Main extends Frame implements Runnable{

	private static final long serialVersionUID = 4587090594982357275L;

	private Start myParent = null;
	
	public Thread iThread = null;

	private BufferStrategy bufStrat = null;

	private long lastDrawTime = 0;
	private double fps = 0;
	// Zeitspanne zwischen den einzelnen Schritten in Millisekunden
	private int timeStep = 15;

	private boolean stop = false;

	// X- und Y-Koordinaten des Fensters auf dem Bildschirm in Pixeln
	private int windowX = 100;
	private int windowY = 100;
	// Breite und H�he des Fensters in Pixeln
	private int windowWidth = 500;
	private int windowHeight = 500;
	// Breite der "Au�enwand" in Pixeln
	private int borderWidth = 5;

	private LinkedList<Ball> ballList = new LinkedList<Ball>();

	// Z�hler f�r Ball-Wand bzw. Ball-Ball Kollisionen
	private int bwCollisionCounter = 0;
	private int bbCollisionCounter = 0;

	// Summe aller Momente des Systems vor der Kollisionsbehandlung
	private float sumMomentum = 0;
	// Summe aller Momente des Systems nach der Kollisionsbehandlung
	private float sumMomentumN = 0;
	// Summe aller Energien des Systems vor der Kollisionsbehandlung
	private float sumEnergy = 0;
	// Summe aller Energien des System nach der Kollisionsbehandlung
	private float sumEnergyN = 0;
	// Z�hler f�r Fehler in der Momentums-Konservation
	private int mConsErrCounter = 0;
	// Fehlerdifferenz in der letzten fehlerhaften Momentums-Konservation
	private float mConsDiff = 0;
	// Bisher maximale Fehlerdifferenz in der Momentums-Konservation
	private float maxMConsDiff = 0;
	// Schwellwert ab dem Differenzen in der Momentums-Konservation st�rend auffallen
	private float maxMConsDiffTresh = 20;
	// Z�hler f�r Fehler in der Energie-Konservation
	private int eConsErrCounter = 0;
	// Fehlerdifferenz in der letzten fehlerhaften Energie-Konservation
	private float eConsDiff = 0;
	// Bisher maximale Fehlerdifferenz in der Energie-Konservation
	private float maxEConsDiff = 0;
	// Schwellwert ab dem Differenzen in der Energie-Konservation st�rend auffallen
	private float maxEConsDiffTresh = (float) 0.0001;
	
	public Main(Start parent) 
	{ 
		this.myParent = parent;
		
		setSize(windowWidth + 2 * borderWidth, windowHeight + 2 * borderWidth);
		setLocation(windowX, windowY);

		addWindowListener( new WindowAdapter() { 
			public void windowClosing ( WindowEvent e ) { System.exit( 0 ); } 
		}); 
		addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {
				String input = String.valueOf(e.getKeyChar());

				if(input.equalsIgnoreCase("w")){

				}
			}

			public void keyReleased(KeyEvent e) {
				String input = String.valueOf(e.getKeyChar());

				if(input.equalsIgnoreCase("w")){

				}
			}

			public void keyPressed(KeyEvent e) {
				int input = e.getKeyCode();
				//System.out.println(input);

				if(input==27){ // ESC
					System.exit(0);
				}
				if(input==32){ // Leerzeichen
					//stop = !stop;
				}
				if(input==38){ // Pfeil Hoch

				}
				if(input==37){ // Pfeil Links

				}
				if(input==40){ // Pfeil Runter

				}
				if(input==39){ // Pfeil Rechts

				}
			};
		});
		addMouseMotionListener(new MouseMotionListener() {

			public void mouseMoved(MouseEvent e) {

			}

			public void mouseDragged(MouseEvent e) {

			}
		});
		addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {

			}

			public void mousePressed(MouseEvent e) {

			}

			public void mouseExited(MouseEvent e) {

			}

			public void mouseEntered(MouseEvent e) {

			}

			public void mouseClicked(MouseEvent e) {

			}
		});
		addMouseWheelListener(new MouseWheelListener() {

			public void mouseWheelMoved(MouseWheelEvent e) {

			}
		});
	}

	public void init(){
		createBufferStrategy(2);
		bufStrat = getBufferStrategy();

		// Ball zum Testen hinzuf�gen
		//ballList.add(new Ball(250, 250, 0, 0, 10, 10));
		//ballList.add(new Ball(220, 220, 0, 0, 10, 10));
		//ballList.add(new Ball(280, 220, 0, 0, 10, 10));
		//ballList.add(new Ball(250, 220, 0, 0, 10, 10));
		ballList.add(new Ball(235, 200, 0, 0, 10, 10));
		ballList.add(new Ball(265, 200, 0, 0, 10, 10));
		ballList.add(new Ball(250, 450, 0, -2, 10, 10));

		Thread th = new Thread(this);
		this.iThread = th;
		th.start();
	}

	public void run(){
		while(true){
			if(!stop){
				Graphics2D gfx = (Graphics2D) bufStrat.getDrawGraphics();
				calcFps();
				calcSumEnergyBefore();
				collisionBallWall();
				collisionBallBall();
				moveObjects();
				calcSumEnergyAfter();
				checkEnergyConservation();
				paintField(gfx);
				bufStrat.show();
				lastDrawTime=System.currentTimeMillis();
				try {
					iThread.sleep(timeStep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void calcFps(){
		try {
			fps = 1000 / (System.currentTimeMillis()-lastDrawTime);
		} catch (ArithmeticException e) {
			System.out.println("TIMING ERROR! No time difference!");
			fps = 8888;
		}
	}

	private void calcSumEnergyBefore(){
		sumMomentum = 0;
		sumEnergy = 0;
		for(int i = 0; i < ballList.size(); i++){
			sumMomentum += ballList.get(i).getMass() * ballList.get(i).getVelocity().getAbsolute();
			sumEnergy += 0.5 * ballList.get(i).getMass() * Math.pow(ballList.get(i).getVelocity().getAbsolute(), 2);
		}
	}

	private void calcSumEnergyAfter(){
		sumMomentumN = 0;
		sumEnergyN = 0;
		for(int i = 0; i < ballList.size(); i++){
			sumMomentumN += ballList.get(i).getMass() * ballList.get(i).getVelocity().getAbsolute();
			sumEnergyN += 0.5 * ballList.get(i).getMass() * Math.pow(ballList.get(i).getVelocity().getAbsolute(), 2);
		}
	}

	private void checkEnergyConservation(){
		if(sumMomentumN != sumMomentum){
			mConsErrCounter++;
		}
		if(sumEnergyN != sumEnergy){
			eConsErrCounter++;
		}
		mConsDiff += sumMomentumN - sumMomentum;
		eConsDiff += sumEnergyN - sumEnergy;
		if(Math.abs(mConsDiff) > maxMConsDiff){
			maxMConsDiff = Math.abs(mConsDiff);
			myParent.addHisto("New maximal momentum conservation error difference: " + maxMConsDiff);
		}
		if(Math.abs(eConsDiff) > maxEConsDiff){
			maxEConsDiff = Math.abs(eConsDiff);
			myParent.addHisto("New maximal energy conservation error difference: " + maxEConsDiff);
		}
	}

	private void collisionBallWall(){
		for(int i = 0; i < ballList.size(); i++){
			if(ballList.get(i).getPositionX() - ballList.get(i).getRadius() < 0){
				ballList.get(i).setPosition(ballList.get(i).getRadius(), ballList.get(i).getPositionY());
				ballList.get(i).setVelocity(ballList.get(i).getVelocityX() * -1, ballList.get(i).getVelocityY());
				bwCollisionCounter++;
				myParent.addHisto("Ball " + i + " collided with top-wall");
			}else if(ballList.get(i).getPositionX() + ballList.get(i).getRadius() > getWidth() - (2 * borderWidth + 1)){
				ballList.get(i).setPosition(getWidth() - ballList.get(i).getRadius() - (2 * borderWidth + 1), ballList.get(i).getPositionY());
				ballList.get(i).setVelocity(ballList.get(i).getVelocityX() * -1, ballList.get(i).getVelocityY());
				bwCollisionCounter++;
				myParent.addHisto("Ball " + i + " collided with bottom-wall");
			}
			if(ballList.get(i).getPositionY() - ballList.get(i).getRadius() < 0){
				ballList.get(i).setPosition(ballList.get(i).getPositionX(), ballList.get(i).getRadius());
				ballList.get(i).setVelocity(ballList.get(i).getVelocityX(), ballList.get(i).getVelocityY() * -1);
				bwCollisionCounter++;
				myParent.addHisto("Ball " + i + " collided with left-wall");
			}else if(ballList.get(i).getPositionY() + ballList.get(i).getRadius() > getHeight() - (2 * borderWidth + 1)){
				ballList.get(i).setPosition(ballList.get(i).getPositionX(), getHeight() - ballList.get(i).getRadius() - (2 * borderWidth + 1));
				ballList.get(i).setVelocity(ballList.get(i).getVelocityX(), ballList.get(i).getVelocityY() * -1);
				bwCollisionCounter++;
				myParent.addHisto("Ball " + i + " collided with right-wall");
			}
		}
	}

	private void collisionBallBall(){
		for(int i = 0; i < ballList.size(); i++){
			Ellipse2D.Float ballGfx = new Ellipse2D.Float();
			ballGfx.x = ballList.get(i).getPositionX();
			ballGfx.y = ballList.get(i).getPositionY();
			ballGfx.width = ballList.get(i).getRadius() * 2;
			ballGfx.height = ballGfx.width;
			for(int j = i + 1; j < ballList.size(); j++){
				float dx = ballGfx.x - ballList.get(j).getPositionX();
				float dy = ballGfx.y - ballList.get(j).getPositionY();
				float dd = (float) (Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
				float minDist = ballGfx.width / 2 + ballList.get(j).getRadius();

				if(dd < minDist){
					Vector n = new Vector(dx, dy);
					Vector un = n.multiply((float) (1 / n.getAbsolute()));
					Vector ut = new Vector(un.getY() * -1, un.getX());
					Vector v1 = ballList.get(i).getVelocity();
					Vector v2 = ballList.get(j).getVelocity();
					float v1n = un.dotProduct(v1);
					float v1t = ut.dotProduct(v1);
					float v2n = un.dotProduct(v2);
					float v2t = ut.dotProduct(v2);
					float v1tn = v1t;
					float v2tn = v2t;
					float m1 = ballList.get(i).getMass();
					float m2 = ballList.get(j).getMass();
					float v1nn = (v1n * (m1 - m2) + 2 * m2 * v2n)/(m1 + m2);
					float v2nn = (v2n * (m2 - m1) + 2 * m1 * v1n)/(m1 + m2);
					Vector v1nnVect = un.multiply(v1nn);
					Vector v1tnVect = ut.multiply(v1tn);
					Vector v2nnVect = un.multiply(v2nn);
					Vector v2tnVect = ut.multiply(v2tn);
					Vector v1nVect = v1nnVect.add(v1tnVect);
					Vector v2nVect = v2nnVect.add(v2tnVect);
					ballList.get(i).setVelocity(v1nVect.getX(), v1nVect.getY());
					ballList.get(j).setVelocity(v2nVect.getX(), v2nVect.getY());
					bbCollisionCounter++;
					myParent.addHisto("Ball " + i + " collided with ball " + j);
				}
			}
		}
	}

	private void moveObjects(){
		for(int i = 0; i < ballList.size(); i++){
			ballList.get(i).setPosition(ballList.get(i).getPosition().add(ballList.get(i).getVelocity()));
		}
	}

	private void paintField(Graphics gfx){
		// Schriftart festlegen
		gfx.setFont(new Font("Courier", 0, 11));
		// Fenster lehren
		gfx.clearRect(0, 0, getWidth(), getHeight());
		// B�lle zeichnen
		for(int i = 0; i < ballList.size(); i++){
			paintOval(gfx, ballList.get(i).getPositionX() - ballList.get(i).getRadius() + borderWidth, ballList.get(i).getPositionY() - ballList.get(i).getRadius() + borderWidth, ballList.get(i).getRadius() * 2, ballList.get(i).getRadius() * 2, new Color(255, 0, 0));
		}
		// Entwicklungs-Informationen anzeigen
		paintString(gfx, borderWidth + 5, 25, new Color(0, 0, 0), "FPS: " + fps);
		//paintString(gfx, borderWidth + 5, 45, new Color(0, 0, 0), "Colli Ball-Wall: " + bwCollisionCounter);
		//paintString(gfx, borderWidth + 5, 65, new Color(0, 0, 0), "Colli Ball-Ball: " + bbCollisionCounter);
		//paintString(gfx, borderWidth + 5, 85, new Color(0, 0, 0), "Error Mome-Cons: " + mConsErrCounter);
		//paintString(gfx, borderWidth + 5, 125, new Color(0, 0, 0), "Error Mome-Diff: " + mConsDiff);
		//paintString(gfx, borderWidth + 5, 165, new Color(0, 0, 0), "Maxim Mome-Diff: " + maxMConsDiff);
		if(mConsErrCounter > 0){
			//paintString(gfx, borderWidth + 5, 85, new Color(255, 0, 0), "Error Mome-Cons: " + mConsErrCounter);
			paintString(gfx, borderWidth + 5, 85, new Color(255, 0, 0), "Error in momentum conservation");
		}
		if(mConsDiff != 0){
			//paintString(gfx, borderWidth + 5, 125, new Color(255, 0, 0), "Error Mome-Diff: " + mConsDiff);
		}
		if(maxMConsDiff > maxMConsDiffTresh){
			//paintString(gfx, borderWidth + 5, 165, new Color(255, 0, 0), "Maxim Mome-Diff: " + maxMConsDiff);
		}
		//paintString(gfx, borderWidth + 5, 105, new Color(0, 0, 0), "Error Ener-Cons: " + eConsErrCounter);
		//paintString(gfx, borderWidth + 5, 145, new Color(0, 0, 0), "Error Ener-Diff: " + eConsDiff);
		//paintString(gfx, borderWidth + 5, 185, new Color(0, 0, 0), "Maxim Ener-Diff: " + maxEConsDiff);
		if(eConsErrCounter > 0){
			//paintString(gfx, borderWidth + 5, 105, new Color(255, 0, 0), "Error Ener-Cons: " + eConsErrCounter);
			paintString(gfx, borderWidth + 5, 105, new Color(255, 0, 0), "Error in energy conservation");
		}
		if(eConsDiff != 0){
			//paintString(gfx, borderWidth + 5, 145, new Color(255, 0, 0), "Error Ener-Diff: " + eConsDiff);
		}
		if(maxEConsDiff > maxEConsDiffTresh){
			//paintString(gfx, borderWidth + 5, 185, new Color(255, 0, 0), "Maxim Ener-Diff: " + maxEConsDiff);
		}
		// Rand zeichnen
		paintRect(gfx, 0, 0, borderWidth, getHeight(), new Color(0, 0, 0));
		paintRect(gfx, 0, 0, getWidth(), borderWidth, new Color(0, 0, 0));
		paintRect(gfx, getWidth() - borderWidth, 0, borderWidth, getHeight(), new Color(0, 0, 0));
		paintRect(gfx, 0, getHeight() - borderWidth, getWidth(), borderWidth, new Color(0, 0, 0));
	}

	private void paintRect(Graphics gfx, float x, float y, float w, float h, Color color){
		gfx.setColor(color);
		gfx.drawRect((int) x, (int) y, (int) w, (int) h);
		gfx.fillRect((int) x, (int) y, (int) w, (int) h);
	}

	private void paintOval(Graphics gfx, float x, float y, float w, float h, Color color){
		gfx.setColor(color);
		gfx.drawOval((int) x, (int) y, (int) w, (int) h);
		gfx.fillOval((int) x, (int) y, (int) w, (int) h);
	}

	private void paintString(Graphics gfx, float x, float y, Color color, String text){
		gfx.setColor(color);
		gfx.drawString(text, (int) x, (int) y);
	}

	public void reqPause(){
		stop = true;
	}
	
	public void reqUnpause(){
		stop = false;
	}
	
	public void addBall(float positionX, float positionY, float velocityX, float velocityY, float radius, float mass){
		ballList.add(new Ball(positionX, positionY, velocityX, velocityY, radius, mass));
	}
	
	public void delBall(int ballId){
		ballList.remove(ballId);
	}
}
