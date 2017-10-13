/* ISU: Fish Game
  * Author: Sabina Shin
  * Date: January 20, 2016
  * Purpose: java game, purpose is to catch the fish
 */
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.applet.*;
import java.net.*;
import java.io.*;

//textinput and output
//time gors down by twos


public class FishGame extends Frame
{
    Image offScreenImage, background, fish, barrel, hook, fishhook, barrelhook, fishRight, mainScreen, startButton, hookImage, gameOverScreen, playAgainButton, exitButton, instructions, highScoreScreen;
    AudioClip backgroundMusic, clickSound;
    Graphics offScreenBuffer;
    Timer timer;
    boolean timerOn, hookClicked = false, gameOver = false;
    int[] [] fishArray = new int [4] [10];
    int[] [] barrelArray = new int [4] [5];
    int score, screen = 0, hook_y = 0, newNum;
    int time = 10, timeAllowed;
    int countHook = 1, hookDirection = 1;
    //    int clicked_x, clicked_y;
    int fishCaught, barrelCaught;
    //textinput/output
    String firstScore, secondScore, thirdScore, extraName;
    int first, second, third;
    double num;
    String firstName, secondName, thirdName, name = " ";
    MoveFish fishmove;
    MoveHook hookmove;
    MoveBarrel barrelmove;
    GameTimer gameTimer;

    Font scoreFont = new Font ("Arial", Font.PLAIN, 50);
    Font inGameFont = new Font ("Arial", Font.PLAIN, 25);

    //Description: Main Method
    //this method loads all the pictures and audio files that are required for the game to work
    public FishGame ()
    {
	super ("FishGame");
	setSize (1000, 800);

	MediaTracker tracker = new MediaTracker (this);
	mainScreen = Toolkit.getDefaultToolkit ().getImage ("mainscreen.jpg");
	tracker.addImage (mainScreen, 0);
	instructions = Toolkit.getDefaultToolkit ().getImage ("instructions.jpg");
	tracker.addImage (instructions, 1);
	background = Toolkit.getDefaultToolkit ().getImage ("background.jpg");
	tracker.addImage (background, 2);
	gameOverScreen = Toolkit.getDefaultToolkit ().getImage ("gameover.jpg");
	tracker.addImage (gameOverScreen, 3);
	highScoreScreen = Toolkit.getDefaultToolkit ().getImage ("highscore.jpg");
	tracker.addImage (highScoreScreen, 4);
	startButton = Toolkit.getDefaultToolkit ().getImage ("play.png");
	tracker.addImage (startButton, 5);
	playAgainButton = Toolkit.getDefaultToolkit ().getImage ("playagain.png");
	tracker.addImage (playAgainButton, 6);
	exitButton = Toolkit.getDefaultToolkit ().getImage ("exit.png");
	tracker.addImage (exitButton, 7);
	fish = Toolkit.getDefaultToolkit ().getImage ("fish.png");
	tracker.addImage (fish, 8);
	fishRight = Toolkit.getDefaultToolkit ().getImage ("fishRight.png");
	tracker.addImage (fishRight, 9);
	barrel = Toolkit.getDefaultToolkit ().getImage ("barrel.png");
	tracker.addImage (barrel, 10);
	hook = Toolkit.getDefaultToolkit ().getImage ("hook.png");
	tracker.addImage (hook, 11);
	fishhook = Toolkit.getDefaultToolkit ().getImage ("fishhook.png");
	tracker.addImage (fishhook, 12);
	barrelhook = Toolkit.getDefaultToolkit ().getImage ("barrelhook.png");
	tracker.addImage (barrelhook, 13);

	backgroundMusic = Applet.newAudioClip (getCompleteURL ("final.wav"));
	clickSound = Applet.newAudioClip (getCompleteURL ("sound.wav"));
	try
	{
	    tracker.waitForAll ();
	}
	catch (InterruptedException e)
	{
	}

	Image iconImage = Toolkit.getDefaultToolkit ().getImage ("fish.png");
	setIconImage (iconImage);


	timerOn = true;
	timeAllowed = 100;
	XYFish ();
	XYBarrel ();
	show ();
	backgroundMusic.loop ();
    }



    public URL getCompleteURL (String fileName)
    {
	try
	{
	    return new URL ("file:" + System.getProperty ("user.dir") + "/" + fileName);
	}
	catch (MalformedURLException e)
	{
	    System.err.println (e.getMessage ());
	}
	return null;
    }


    //Creates a new game by setting the score back to zero and resetting the timer back to 30 seconds
    public void newGame ()
    {
	score = 0;
	time = 30;
	gameOver = false;
	fishmove = new MoveFish ();
	hookmove = new MoveHook ();
	barrelmove = new MoveBarrel ();
	gameTimer = new GameTimer ();

	gameTimer.start ();
	barrelmove.start ();
	hookmove.start ();
	fishmove.start ();
    }


    //increase the number of the timer
    public class GameTimer extends Thread //timer
    {
	public void run ()
	{
	    while (time != 0)
	    {
		time--;

		try
		{
		    sleep (2000);
		}
		catch (Exception e)
		{
		}
	    }
	    gameOver = true;

	}
    }


    //randomly generates the position of the barrel when the game starts
    public void XYBarrel ()
    {
	for (int i = 0 ; i < 5 ; i++)
	{
	    barrelArray [0] [i] = (int) ((Math.round (Math.random () * 1320) - 200));
	    barrelArray [1] [i] = (int) ((Math.round (Math.random () * 500) + 300));
	    barrelArray [2] [i] = (int) (Math.round (Math.random ()));
	    barrelArray [3] [i] = 1;
	}
    }


    //randomly generates the position of the fish when the game starts
    public void XYFish ()
    {
	for (int i = 0 ; i < 10 ; i++)
	{
	    fishArray [0] [i] = (int) ((Math.round (Math.random () * 1320) - 200));
	    fishArray [1] [i] = (int) ((Math.round (Math.random () * 500) + 300));
	    fishArray [2] [i] = (int) (Math.round (Math.random ()));
	    fishArray [3] [i] = (int) (Math.round (Math.random () * 4) + 1);
	}
    }


    //moves the barrels by moving the barrel by the randomized speed and repaint
    public class MoveBarrel extends Thread
    {
	public void run ()
	{
	    while (true)
	    {
		for (int i = 0 ; i < 5 ; i++)
		{
		    if (barrelArray [2] [i] == 0 && barrelArray [0] [i] > -200)
			barrelArray [0] [i] -= barrelArray [3] [i];

		    else if (-220 <= barrelArray [0] [i] && barrelArray [0] [i] <= -200)
			barrelArray [2] [i] = 1;

		    if (barrelArray [2] [i] == 1 && barrelArray [0] [i] < 1100)
			barrelArray [0] [i] += barrelArray [3] [i];

		    else if (1100 <= barrelArray [0] [i] && barrelArray [0] [i] <= 1120)
			barrelArray [2] [i] = 0;
		}

		repaint ();
		try
		{
		    sleep (7);
		}
		catch (Exception e)
		{
		}
		if (gameOver == true)
		    break;
	    }
	}
    }


    //moves the fish by moving the fish by the randomized speed and repaint
    public class MoveFish extends Thread
    {
	public void run ()
	{
	    while (true)
	    {
		for (int i = 0 ; i < 10 ; i++)
		{
		    if (fishArray [2] [i] == 0 && fishArray [0] [i] > -200)
			fishArray [0] [i] -= fishArray [3] [i];

		    else if (-220 <= fishArray [0] [i] && fishArray [0] [i] <= -200)
			fishArray [2] [i] = 1;

		    if (fishArray [2] [i] == 1 && fishArray [0] [i] < 1100)
			fishArray [0] [i] += fishArray [3] [i];

		    else if (1100 <= fishArray [0] [i] && fishArray [0] [i] <= 1120)
			fishArray [2] [i] = 0;
		}

		repaint ();
		try
		{
		    sleep (7);
		}
		catch (Exception e)
		{
		}
		if (gameOver == true)
		    break;
	    }
	}
    }


    //checks if the player caught the fish; if the hook is in the boundaries of the fish
    public int checkFish ()
    {
	for (int i = 0 ; i < 10 ; i++)
	{
	    if (fishArray [0] [i] <= 525 && 525 <= fishArray [0] [i] + 150)
	    {
		if (fishArray [1] [i] <= (315 + hook_y) && (315 + hook_y) <= (fishArray [1] [i] + 100))
		{
		    fishCaught = 1;
		    score += 5;

		    newNum = (int) (Math.round (Math.random ()));
		    if (newNum == 0)
			fishArray [0] [i] = -199;
		    else
			fishArray [0] [i] = 1100;

		    fishArray [1] [i] = (int) ((Math.round (Math.random () * 500) + 300));
		    fishArray [2] [i] = (int) (Math.round (Math.random ()));
		    fishArray [3] [i] = (int) (Math.round (Math.random () * 4) + 1);
		    return (1);
		}


	    }
	}

	return (0);
    }


    //checks if the player caught the barrel; if the hook is in the boundaries of the barrel
    public int checkBarrel ()
    {
	for (int i = 0 ; i < 5 ; i++)
	{
	    if (barrelArray [0] [i] <= 525 && 525 <= barrelArray [0] [i] + 150)
	    {
		if (barrelArray [1] [i] <= (315 + hook_y) && (315 + hook_y) <= (barrelArray [1] [i] + 100))
		{
		    barrelCaught = 1;
		    score -= 2;

		    newNum = (int) (Math.round (Math.random ()));
		    if (newNum == 0)
			barrelArray [0] [i] = -199;
		    else
			barrelArray [0] [i] = 1100;

		    barrelArray [1] [i] = (int) ((Math.round (Math.random () * 500) + 300));
		    barrelArray [2] [i] = (int) (Math.round (Math.random ()));
		    barrelArray [3] [i] = (int) (Math.round (Math.random () * 4) + 1);
		    return (1);
		}


	    }
	}

	return (0);
    }


    //moves the hook when the player clicks the screen
    public class MoveHook extends Thread
    {
	public void run ()
	{
	    while (true)
	    {
		if (hookClicked == true)
		{
		    countHook += 1;
		    if (hookDirection == 1 && hook_y < 470)
			hook_y += 2;

		    else if (hook_y == 470 || fishCaught == 1 || barrelCaught == 1)
		    {
			hookDirection = -1;
			hook_y -= 2;
		    }
		    if (hookDirection == -1 && hook_y > 0)
			hook_y -= 2;

		    checkFish ();
		    checkBarrel ();
		    repaint ();

		    try
		    {
			sleep (7);
		    }
		    catch (Exception e)
		    {
		    }
		}

		if (hookDirection == -1 && hook_y < 0)
		{
		    hookClicked = false;
		    countHook = 1;
		    hookDirection = 1;
		    fishCaught = 0;
		    barrelCaught = 0;
		}
		if (gameOver == true)
		{

		    break;

		}

	    }
	    try
	    {
		HighScore ();
	    }
	    catch (Exception e)
	    {
	    }
	    repaint ();
	}
    }


    //detects when the player clicks the screen because depending on the screen the player needs to click a button
    public boolean mouseDown (Event event, int x, int y)
    {
	if (screen == 2) //in game
	{
	    hookClicked = true;
	    clickSound.play ();
	}
	if (screen == 0) //main
	    if (400 <= x && x <= 580 && 500 <= y && y <= 580)
	    {
		name = JOptionPane.showInputDialog (this, "Please enter your name");
		screen = 1;
		newGame ();
	    }
	if (screen == 1) //instructions
	{
	    if (600 <= x && x <= 780 && 550 <= y && y <= 630)
	    {
		screen = 2;
		newGame ();
	    }
	}

	if (screen == 4) //score
	{
	    if (220 <= x && x <= 400 && 600 <= y && y <= 680)
	    {
		screen = 1;
		newGame ();
	    }
	    if (600 <= x && x <= 780 && 600 <= y && y <= 680)
		hide ();
	}
	return false;
    }


    //reads in highscore from the textfile and rewrites the highscore
    public void HighScore () throws Exception
    {
	BufferedReader in = new BufferedReader (new FileReader ("highscores.txt"));
	System.out.println ("reading in highscores");
	firstName = in.readLine ();
	firstScore = in.readLine ();
	secondName = in.readLine ();
	secondScore = in.readLine ();
	thirdName = in.readLine ();
	thirdScore = in.readLine ();
	in.close ();
	first = Integer.parseInt (firstScore);
	second = Integer.parseInt (secondScore);
	third = Integer.parseInt (thirdScore);

	if (score >= first)
	{
	    third = second;
	    thirdName = secondName;
	    second = first;
	    secondName = firstName;
	    first = score;
	    firstName = name;
		System.out.println ("first  " + firstName);
	}
	else if (score >= second)
	{
	    third = second;
	    thirdName = secondName;
	    second = score;
	    secondName = name;
	}
	else if (score >= third)
	{
	    third = score;
	    thirdName = name;
	}

	if (score >= first || score >= second || score >= third)
	{
	    BufferedWriter out = new BufferedWriter (new FileWriter ("highscores.txt"));
	    out.write (firstName);
	    out.newLine ();
	    out.write (first + "");
	    out.newLine ();
	    out.write (secondName);
	    out.newLine ();
	    out.write (second + "");
	    out.newLine ();
	    out.write (thirdName);
	    out.newLine ();
	    out.write (third + "");
	    out.close ();
	}

    }


    //paints the screen according to the screen number
    public void paint (Graphics g)
    {
	if (offScreenBuffer == null)
	{
	    offScreenImage = createImage (size ().width, size ().height);
	    offScreenBuffer = offScreenImage.getGraphics ();
	}

	offScreenBuffer.clearRect (0, 0, size ().width, size ().height);
	if (screen == 0) //main screen
	{
	    offScreenBuffer.drawImage (mainScreen, 0, 0, 1000, 800, this);
	    offScreenBuffer.drawImage (startButton, 400, 500, 180, 80, this);

	}

	if (screen == 1) //instructions
	{
	    offScreenBuffer.drawImage (instructions, 0, 0, 1000, 800, this);
	    offScreenBuffer.drawImage (startButton, 600, 550, 180, 80, this);

	}

	if (screen == 2) //in game
	{
	    offScreenBuffer.drawImage (background, 0, 0, 1000, 800, this);

	    for (int i = 0 ; i < 10 ; i++) //display fish
	    {
		if (fishArray [2] [i] == 0)
		    offScreenBuffer.drawImage (fish, fishArray [0] [i], fishArray [1] [i], 150, 100, this);
		else
		    offScreenBuffer.drawImage (fishRight, fishArray [0] [i], fishArray [1] [i], 150, 100, this);

	    }

	    for (int i = 0 ; i < 5 ; i++) //display barrel
	    {
		if (barrelArray [2] [i] == 0)
		    offScreenBuffer.drawImage (barrel, barrelArray [0] [i], barrelArray [1] [i], 110, 100, this);
	    }


	    if (fishCaught == 1)
	    {
		hookImage = fishhook;
		offScreenBuffer.drawImage (hookImage, 470, (265 + hook_y), 100, 80, this);
		offScreenBuffer.fill3DRect (528, 265, 1, hook_y, true);
	    }
	    if (barrelCaught == 1) //barrel caught
	    {
		hookImage = barrelhook;
		offScreenBuffer.drawImage (hookImage, 470, (265 + hook_y), 100, 80, this);
		offScreenBuffer.fill3DRect (528, 265, 1, hook_y, true);
	    }

	    else
	    {
		hookImage = hook;
		offScreenBuffer.drawImage (hookImage, 510, (265 + hook_y), 30, 50, this);
		offScreenBuffer.fill3DRect (528, 265, 1, hook_y, true);
	    }
	    offScreenBuffer.setFont (inGameFont);
	    offScreenBuffer.drawString ("Hello " + name, 100, 70);
	    offScreenBuffer.drawString ("Time: " + time, 100, 120);
	    offScreenBuffer.drawString ("Score: " + score, 690, 100);
	    if (gameOver == true)
	    {
		screen = 3;

	    }
	}
	if (screen == 3) //game over page
	{
	    offScreenBuffer.drawImage (gameOverScreen, 0, 0, 1000, 800, this);
	    offScreenBuffer.setFont (scoreFont);
	    offScreenBuffer.drawString (score + "", 690, 100);

	    g.drawImage (offScreenImage, 0, 0, this);
	    delay (3000);
	    screen = 4;
	}

	if (screen == 4) //highscore page
	{

	    offScreenBuffer.drawImage (highScoreScreen, 0, 0, 1000, 800, this);
	    offScreenBuffer.drawString (firstName, 450, 330);
	    offScreenBuffer.drawString (first + "", 280, 330);
	    offScreenBuffer.drawString (secondName, 450, 430);
	    offScreenBuffer.drawString (second + "", 280, 430);
	    offScreenBuffer.drawString (thirdName, 450, 530);
	    offScreenBuffer.drawString (third + "", 280, 530);
	    offScreenBuffer.drawImage (playAgainButton, 220, 600, 180, 80, this);
	    offScreenBuffer.drawImage (exitButton, 600, 600, 180, 80, this);
	}


	g.drawImage (offScreenImage, 0, 0, this);

    } // paint method


    //updates the screen
    public void update (Graphics g)
    {
	paint (g);
    }


    private void delay (int milliSec)
    {
	try
	{
	    Thread.sleep (milliSec);
	}

	catch (InterruptedException e)
	{
	}
    }


    public boolean handleEvent (Event evt)
    {
	if (evt.id == Event.WINDOW_DESTROY)
	{
	    hide ();
	    System.exit (0);
	    return true;
	}


	// If not handled, pass the event along
	return super.handleEvent (evt);
    }



    public static void main (String[] args)
    {
	new FishGame ();        // Create a FishGame frame
    } // main method
} // FishGame class


