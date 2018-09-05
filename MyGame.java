import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class MyGame extends SnakeGame{
	//things you inherited from SnakeGame
	//protected Snake player;
	//protected BodySegment food;
	//protected double waitSeconds;
	private boolean gameOver=false;

	//this is a changes
	int a = 5;
	//color schemes
	private Color[] BGColors={new Color(94,90,157),new Color(38,137,75),new Color(63, 0, 0),new Color(228,188,105)};
	private Color[] GridColors={new Color(38,34,104),new Color(0,54,20),new Color(40,0,0),new Color(159,88,32)};
	private Color[] SnakeColors={new Color(17,13,74),new Color(1,84,32),new Color(116,1,1),new Color(117,28,5)};

	//misc variables
	DecimalFormat dF1=new DecimalFormat("00");
	DecimalFormat dF2=new DecimalFormat("00.00");
	double currentTime;
	double startTime;
	double elapsedSec=0;
	double elapsedMin=0;
	int scheme=0;

	//these are my new changes
	//i hope the +'s appear here

	//score


	//I killed a guy that used to be here
	int scoreNum=0;
	public MyGame(){
		super();
		startTime= System.currentTimeMillis();
		currentTime=startTime;
		//color schemeing


		//difficulties
		int difficulty=this.difficultyPrompt();


		if(difficulty==1)
			waitSeconds=1;
		else if(difficulty==0)
			waitSeconds=2;
		else if(difficulty==2)
			waitSeconds=0.5;
		else if(difficulty==3)
			waitSeconds=0.25;

		//color scheming
		scheme=difficulty;
		setBG(BGColors[scheme]);
		setGridColor(GridColors[scheme]);
		setWallColor(gridColor);
		player.setHue(SnakeColors[scheme]);
		food.setHue(SnakeColors[scheme]);

		playGame();
	}


	public void gameFrame(){
		if(gameOver)
			return;

		//setting text box prefs
		Font f=new Font("Blackoak std",Font.BOLD, 20);
		timer.setForeground(SnakeColors[scheme]);
		timer.setBackground(gridColor);
		timer.setFont(f);
		timer.setHorizontalAlignment(JTextField.CENTER);

		score.setForeground(SnakeColors[scheme]);
		score.setBackground(gridColor);
		score.setFont(f);
		score.setHorizontalAlignment(JTextField.CENTER);

		//calculating the time
		//this is a new change, check this line out
		currentTime= System.currentTimeMillis();
		elapsedSec=(currentTime-startTime)/1000;
		if(elapsedSec>=60){
			elapsedMin+=1;
			elapsedSec-=60;
		}

		//timer text
		timer.setText(""+dF1.format(elapsedMin)+":"+dF2.format(elapsedSec));
		player.addFirst();
		player.removeLast();
		reColor();
		if(player.last.getNext().isTouching(food)){
			scoreNum++;
			player.addFirst();
			placeFood();
			food.setHue(SnakeColors[scheme]);
		}
		//score text
		score.setText(""+scoreNum);
		food.fader();
		drawGame();//do this at some point
		if(player.isCannibal()||isOutOfBounds()){
			gameOver=true;
			JOptionPane.showMessageDialog(null, "You Lost.");
			new ScoreFrame(new ScoreRecord(System.getProperty("user.name"),elapsedSec,player.size()-3));
		}




	}

	public boolean isOutOfBounds(){
		if(player.last.getNext().getX()>=SnakeGame.WIDTH)
			return true;
		else if(player.last.getNext().getX()<0)
			return true;
		else if(player.last.getNext().getY()>=SnakeGame.HEIGHT)
			return true;
		else if(player.last.getNext().getY()<0)
			return true;
		else
			return false;
	}

	public void reColor(){
		BodySegment curr=player.last.getNext().getNext();
		Color faceColor = player.last.getNext().getHue();
		int index=1;
		while(curr!=player.last.getNext()){
			Color prevColor=curr.getPrev().getHue();
			int changeInRed, changeInGreen, changeInBlue;
			changeInRed=((255-faceColor.getRed())/(player.size()-1));
			changeInGreen=((255-faceColor.getGreen())/(player.size()-1));
			changeInBlue=((255-faceColor.getBlue())/(player.size()-1));
			Color newColor=new Color(prevColor.getRed()+changeInRed, prevColor.getGreen()+changeInGreen, prevColor.getBlue()+changeInBlue);
			curr.setHue(newColor);
			curr=curr.getNext();
			index++;
		}

	}

	public static void main(String[] args){new MyGame();}
}
