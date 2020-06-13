/**
 * 
 */
package pac1;

import java.awt.Font;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author 
 *
 */
public class Start extends Frame{

	private static final long serialVersionUID = 4159798692403386757L;

	public Thread iThread = null;

	private TextField txfInput = null;
	private TextArea txfHisto = null;

	private Main simEnv = null;

	public static void main(String[] args) {
		Start a = new Start();
		a.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {

			}

			public void windowIconified(WindowEvent e) {

			}

			public void windowDeiconified(WindowEvent e) {

			}

			public void windowDeactivated(WindowEvent e) {

			}

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

			public void windowClosed(WindowEvent e) {

			}

			public void windowActivated(WindowEvent e) {

			}
		});
		a.setLayout(null);
		a.setResizable(false);
		a.setVisible(true);
		a.init();
	}

	public Start(){
		setSize(500, 350);
		setLocation(0, 0);
	}

	public void init(){
		txfInput = new TextField();
		txfInput.setFocusable(true);
		txfInput.setBounds(0, 330, getWidth(), 20);
		txfInput.setFont(new Font("Courier", 0, 12));
		txfInput.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {

			}

			public void keyReleased(KeyEvent e) {

			}

			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10){
					txfHisto.append("\n> " + txfInput.getText());
					doCommand(txfInput.getText());
					txfInput.setText("");
				}
			}
		});
		this.add(txfInput);
		txfHisto = new TextArea();
		txfHisto.setFocusable(true);
		txfHisto.setBounds(0, 25, getWidth(), 305);
		txfHisto.setFont(new Font("Courier", 0, 12));
		this.add(txfHisto);
		
		doCommand("start");
	}

	private void doCommand(String input){
		String[] inputArr = input.split(" ");
		if(inputArr[0].equalsIgnoreCase("start")){
			simEnv = new Main(this);
			simEnv.setUndecorated(true);
			simEnv.setVisible(true);
			simEnv.init();
			txfHisto.append("\nSimulation started");
		}else if(inputArr[0].equalsIgnoreCase("pause")){
			simEnv.reqPause();
			txfHisto.append("\nSimulation paused");
		}else if(inputArr[0].equalsIgnoreCase("unpause")){
			simEnv.reqUnpause();
			txfHisto.append("\nSimulation unpaused");
		}else if(inputArr[0].equalsIgnoreCase("quit")){
			System.exit(0);
		}else if(inputArr[0].equalsIgnoreCase("add")){
			if(inputArr.length > 1){
				if(inputArr[1].equalsIgnoreCase("ball")){
					if(inputArr.length == 8){
						simEnv.addBall(Float.parseFloat(inputArr[2]), Float.parseFloat(inputArr[3]), Float.parseFloat(inputArr[4]), Float.parseFloat(inputArr[5]), Float.parseFloat(inputArr[6]), Float.parseFloat(inputArr[7]));
						txfHisto.append("\nBall added at " + inputArr[2] + ", " + inputArr[3] + " - Velocity: " + inputArr[4] + ", " + inputArr[5] + " - Radius: " + inputArr[6] + " - Mass: " + inputArr[7]);
					}else{
						txfHisto.append("\nUse ADD BALL ~xPosition~ ~yPosition~ ~xVelocity~ ~yVelocity~ ~Radius~ ~Mass~");
					}
				}else{
					txfHisto.append("\nUse ADD BALL");
				}
			}else{
				txfHisto.append("\nUse ADD BALL");
			}
		}else if(inputArr[0].equalsIgnoreCase("del")){
			if(inputArr.length > 1){
				if(inputArr[1].equalsIgnoreCase("ball")){
					if(inputArr.length == 3){
						simEnv.delBall(Integer.parseInt(inputArr[2]));
						txfHisto.append("\nBall " + inputArr[2] + " deleded");
					}else{
						txfHisto.append("\nUse DEL BALL ~ballID~");
					}
				}else{
					txfHisto.append("\nUse DEL BALL");
				}
			}else{
				txfHisto.append("\nUse DEL BALL");
			}
		}else{
			txfHisto.append("\nUnknown command!");
		}
	}
	
	public void addHisto(String input){
		txfHisto.append("\n" + input);
	}
}
