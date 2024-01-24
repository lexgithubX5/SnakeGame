import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.util.*;

public class Panel extends JPanel implements ActionListener {
	static final int WINDOW_WIDTH = 700; //Height of Game Window
	static final int WINDOW_HEIGHT = 700; // Width of Game Window
	static final int UNIT_SIZE = 20; // Size of Unit Box
	static final int UNIT_AMOUNT = (WINDOW_WIDTH *WINDOW_HEIGHT)/UNIT_SIZE;// Total Amount of Unit Boxes
	static final int SPEED =60; // The timing delay or Speed of Snake
	
	//x, y Position of unit box
	final int x[]= new int [UNIT_AMOUNT];
	final int y[]= new int [UNIT_AMOUNT];
	
	
	int bodyParts =1;
	int fruitsEaten;
	int fruitX;
	int fruitY;
	char direction ='R';
	boolean running = false;
	Timer timer;
	Random random;
	
	
	Panel(){
		random = new Random();
		this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.setBackground(new Color(random.nextInt(30),random.nextInt(30),random.nextInt(30)));//avoid lighter backgrounds if changing this settings. 
		this.addKeyListener(new MyKeyAdapter());
		this.setFocusable(true);
		startGame();
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(SPEED,this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		
	}
	public void draw(Graphics g) {
		if(running) {
//			for(int i =0; i<WINDOW_HEIGHT/UNIT_SIZE;i++) {
//				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, WINDOW_HEIGHT);
//				g.drawLine(0, i*UNIT_SIZE,WINDOW_WIDTH,i*UNIT_SIZE);
//			}
			g.setColor(Color.red);
			g.fillOval(fruitX, fruitY, UNIT_SIZE, UNIT_SIZE);
			for(int i =0; i<bodyParts;i++) {
				if(i==0) {
					g.setColor(Color.green);
					g.fillOval(x[i], y[i], UNIT_SIZE,  UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45,180,0));
					g.fillOval(x[i], y[i], UNIT_SIZE,  UNIT_SIZE);
					
				}
			}
			g.setColor(Color.white);
			g.setFont(new Font("MV Boli", Font.BOLD,40));
			FontMetrics metric = getFontMetrics(g.getFont());
			g.drawString("Score: "+fruitsEaten, (WINDOW_WIDTH -metric.stringWidth("Score: "+fruitsEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	}
	public void newApple() {
		fruitX = random.nextInt((int)(WINDOW_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		fruitY = random.nextInt((int)(WINDOW_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
		
	}
	public void move() {
		for(int i = bodyParts; i>0;i--){
			x[i]= x[i-1];
			y[i]=y[i-1];
		}
		switch(direction) {
		case'U':
			y[0]=y[0]-UNIT_SIZE;
		break;
		case'D':
			y[0]=y[0]+UNIT_SIZE;
		break;
		case'L':
			x[0]=x[0]-UNIT_SIZE;
			break;
		case'R':
			x[0]=x[0]+UNIT_SIZE;
			break;
		}
		
	}
	public void checkApple() {
		if((x[0]==fruitX)&&(y[0]==fruitY)) {
			bodyParts++;
			fruitsEaten++;
			newApple();
		}
		
	}
	public void checkCollisions() {
		//head collides with body
		for(int i = bodyParts; i>0;i--) {
			if((x[0]==x[i]&&(y[0]==y[i]))) {
				running = false;
				
			}
		}
		//if head hits top border
		if (y[0]<0) {
			y[0]=WINDOW_HEIGHT;
			direction ='U';
			//running = false;
			}
		//if head hits bottom border
		if (y[0]>WINDOW_HEIGHT) {
			y[0]=0;
			direction ='D';
			//running = false;
		}
		// if head hits left border
		
		// I wanted the walls to not end game, if you like collisions, uncomment running= false
		if(x[0]<0) {
			x[0]=WINDOW_WIDTH;
			direction ='L';
			
		//	running = false;
		}
		//if head hits right border
		if (x[0]>WINDOW_WIDTH) {
			x[0]=0;
			direction ='R';
		//	running = false;
		}
		
		if(!running) {
			timer.stop();
		}
		
	}
	public void gameOver(Graphics g) {
		//GameOver
		g.setColor(Color.red);
		g.setFont(new Font("Arial", Font.BOLD,78));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Game Over!", (WINDOW_WIDTH -metrics1.stringWidth("Game Over!"))/2, WINDOW_HEIGHT/2);
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD,40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: "+fruitsEaten, (WINDOW_WIDTH -metrics2.stringWidth("Score: "+fruitsEaten))/2, g.getFont().getSize());
		/*JButton playAgain = new JButton("PlaY Again");
		playAgain.setBounds(WINDOW_WIDTH/2, WINDOW_HEIGHT/4, 80, 100);
		playAgain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				if(e.getSource()==playAgain) {
					startGame();
				}
				
			}
		});*/
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
			
		}
		repaint();
	}
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
		    case KeyEvent.VK_A:
		        if (direction != 'R') {
		            direction = 'L';
		        }
		        break;
		    case KeyEvent.VK_RIGHT:
		    case KeyEvent.VK_D:
		        if (direction != 'L') {
		            direction = 'R';
		        }
		        break;
		    case KeyEvent.VK_UP:
		    case KeyEvent.VK_W:
		        if (direction != 'D') {
		            direction = 'U';
		        }
		        break;
		    case KeyEvent.VK_DOWN:
		    case KeyEvent.VK_S:
		        if (direction != 'U') {
		            direction = 'D';
		        }
		        break;
			}
		}
		
	}
	

}
