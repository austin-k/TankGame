
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class mainGame extends JFrame {

	// Creates a container for the frame
	Container frame;
	
	int tankAiHit;
	//ArrayList to hold fire Possibitlties
	ArrayList<Integer>[] firePos = new ArrayList[2];
	// Creates gameInfo object to hold important information
	private JPanel gameInfo;
	// Creates health bar object
	private JPanel[] hBarPanel;
	private JPanel[] playerNames;
	private JLabel[] names = new JLabel[2];
	private JPanel scoreDisplay ;
	// Sets the array of health bars
	private JProgressBar[] hpBar = new JProgressBar[2];
	//Create labels to hold score
	private JLabel[] scoreDigits = new JLabel[2];
	// Creates display object
	private DrawGame display;
	// Tank health
	int[] tankHealth = new int[2];
	// If a tank has fired and which tank
	int[] tankFired = new int[2];
	// Power and fire angle
	int[] fireInfo = new int[2];
	// Location of tank
	int[][][][] tankInfo = new int[2][3][4][2];;
	// World Map of points
	int[][][] world = new int[7][4][2];
	// Sector Slopes and reciprocal
	double[][] secSlopes = new double[7][2];
	// Creates an array of polygons that make up the tank
	Polygon[][] tankPoly = new Polygon[2][3];
	// Creates an array of polygons that make up the world
	Polygon[] worldPoly = new Polygon[7];
	// Sets the diameter of the tank round
	int shellDi = 8;
	// Creates an array to hold the information about the tank shell
	int[] shell = { 0, 0, shellDi, shellDi };
	// Which players turn is it
	int currentTurn = 0;
	//Array to hold game score
	int [] score = new int [2];
	//Which type of game is running
	int gameType;
	// Creates main window of game
	public mainGame(int gameType) {
		this.gameType = gameType;
		// Set the title of the window
		setTitle("Tank Game");
		// Set array of JPanels to hold health bars
		hBarPanel = new JPanel[2];
		
		playerNames = new JPanel[2];
		playerNames[0] = new JPanel();
		playerNames[1] = new JPanel();
		playerNames[0].setBackground(Color.lightGray);
		playerNames[1].setBackground(Color.lightGray);
		
		
		//Creates score display panel
		scoreDisplay = new JPanel();
		// Sets health bars to exist in the hpBar array
		hpBar[0] = new JProgressBar(0, 100);
		hpBar[1] = new JProgressBar(0, 100);
		// Set frame to getContentPane
		frame = getContentPane();
		// Sets the gameInfo window to be a jpanel
		gameInfo = new JPanel();
		// Sets the display to be a DrawGame object
		display = new DrawGame();
		// Sets the layout to null so items can be placed anywhere
		frame.setLayout(null);
		// Sets size of the game info window
		gameInfo.setSize(800, 100);
		// Sets location of gameInfo
		gameInfo.setLocation(0, 0);
		// Set the background color
		gameInfo.setBackground(Color.lightGray);
		// Sets the layout of the gameinfo window
		gameInfo.setLayout(null);
		// Set the display size(game is played in here)
		display.setSize(700, 450);
		// Set top left start point
		display.setLocation(50, 100);
		//Set size of score board
		scoreDigits[0] = new JLabel("0", JLabel.CENTER);
		scoreDigits[1] = new JLabel("0", JLabel.CENTER);
		scoreDigits[0].setFont(new Font("Serif", Font.PLAIN, 60));
		scoreDigits[1].setFont(new Font("Serif", Font.PLAIN, 60));
		scoreDisplay.setSize(200,50);
		scoreDisplay.setLocation(300,50);
		scoreDisplay.setBackground(Color.LIGHT_GRAY);
		scoreDisplay.setLayout(new GridLayout(1,2));
		scoreDisplay.add(scoreDigits[0]);
		scoreDisplay.add(scoreDigits[1]);
		playerNames[0].setLayout(new GridLayout(1,1));
		playerNames[1].setLayout(new GridLayout(1,1));
		playerNames[0].setSize(60,20);
		playerNames[1].setSize(60,20);
		playerNames[0].setLocation(120,60);
		playerNames[1].setLocation(620,60);
		if(gameType == 0){
			names[0] = new JLabel("Player 1", JLabel.CENTER);
			names[1] = new JLabel("Player 2", JLabel.CENTER);
		}else if(gameType == 1){
			names[0] = new JLabel("Player 1", JLabel.CENTER);
			names[1] = new JLabel("A.I.", JLabel.CENTER);
		}
		names[0].setFont(new Font("Serif", Font.PLAIN, 16));
		names[1].setFont(new Font("Serif", Font.PLAIN, 16));
		playerNames[0].add(names[0]);
		playerNames[1].add(names[1]);
		
		// Set the hParPanel array elements to be a new JPanel
		hBarPanel[0] = new JPanel();
		hBarPanel[1] = new JPanel();
		// Set the layout of the Panels to be null
		hBarPanel[0].setLayout(null);
		hBarPanel[1].setLayout(null);
		// Set the background of these panels(Not needed)
		hBarPanel[0].setBackground(Color.GREEN);
		hBarPanel[1].setBackground(Color.RED);
		// Set location of each health bar panel
		hBarPanel[0].setLocation(100, 80);
		hBarPanel[1].setLocation(600, 80);

		// Set the size of each health bar panel
		hBarPanel[0].setSize(100, 15);
		hBarPanel[1].setSize(100, 15);
		// Set the size of the hp bar
		hpBar[0].setSize(100, 15);
		hpBar[1].setSize(100, 15);
		// Add the hpBar to each the health bar panel
		hBarPanel[0].add(hpBar[0]);
		hBarPanel[1].add(hpBar[1]);
		// add the health bar panels to the game info panel
		
		gameInfo.add(scoreDisplay);
		gameInfo.add(playerNames[0]);
		gameInfo.add(playerNames[1]);
		gameInfo.add(hBarPanel[0]);
		gameInfo.add(hBarPanel[1]);
		// adds the gameinfo panel panel to the main window
		frame.add(gameInfo);
		// adds the display panel to the main window
		frame.add(display);
		// Set the window to not be resizeable
		setResizable(false);
		// Set the size of the application window
		setSize(800, 640);
		// Set the window to visible
		setVisible(true);
		// Run main game
		if(gameType == 1){
			firePos[0] = new ArrayList<Integer>();
			firePos[1] = new ArrayList<Integer>();
		}
		run();
	}

	public static void main(String[] args) {
		System.out.println("mainRun");
		// Create the main game
		new mainGame(0);
		
		// Close the application
		System.exit(0);

	}

	public void run() {
		// Out put game setting up
		System.out.println("Setting Up");
		// Sets up game
		setupGame();
		score[0] = 0;
		score[1] = 0;
		// Run game as long as both players have more than 0 health
		while(score[0] < 3 && score[1] < 3){
			while (tankHealth[0] > 0 && tankHealth[1] > 0) {
				// Check which players turn it is
				if (currentTurn == 0) {
					// Get the input angle, and flips it 180 because 0,0 is in the
					// top left
					fireInfo[0] = (Integer.parseInt(JOptionPane.showInputDialog("Pleace enter the fire angle")) + 180);
					// Gets input fire power
					fireInfo[1] = (Integer.parseInt(JOptionPane.showInputDialog("Pleace enter the fire power")));
					// Calculate where the shell should be and if it hits
					shellCalc(currentTurn);
					// Set the turn to be the next players turn
					currentTurn = 1;
				} else if (currentTurn == 1) {
					// Get the input angle, and flips it 180 because 0,0 is in the
					// top left
					if(gameType == 0){
						fireInfo[0] = (Integer.parseInt(JOptionPane.showInputDialog("Pleace enter the fire angle")) + 180);
						// Gets input fire power
						fireInfo[1] = (Integer.parseInt(JOptionPane.showInputDialog("Pleace enter the fire power")));
						// Calculate where the shell should be and if it hits
						shellCalc(currentTurn);
					} else if (gameType == 1){
						firePos[0].clear();
						firePos[1].clear();
						firePos = findFireSettings();
						int rng = (int)Math.round(Math.random()*firePos[0].size());
						System.out.println(firePos[0].size());
						fireInfo[0] = firePos[0].get(rng);
						fireInfo[1] = firePos[1].get(rng);
						shellCalc(currentTurn);
						
					}
					// Set the turn to be the next players turn
					currentTurn = 0;
					
				}
				// Update the health bars with the new health
				updateHeatlhBars();
			}
			if(tankHealth[0] <= 0){
				score[1] += 1;
			}else{
				score[0] += 1;
			}
			updateScore();
			setupGame();
		}
	}

	public void setupGame() {
		// drawTanks();
		// Create world
		createWorld();
		// Calculate the slopes of each line
		calcSlope();
		// Create the tanks start positions
		tankStartPos();
		// Display the new tanks
		display.drawTank(tankPoly);
		// Set the tanks health
		tankHealth[0] = 100;
		tankHealth[1] = 100;
		// Sets which tank has fired(unused atm)
		tankFired[0] = 0;
		tankFired[1] = 0;
		// Update the health bars
		updateHeatlhBars();
	}

	public void shellCalc(int tank) {
		// Determine the horizontal speed based on the input speed and input
		// fire angle
		double xSpeed = (double) fireInfo[1] * Math.cos(Math.toRadians(fireInfo[0]));
		// Determine the vertical speed based on the input speed and input fire
		// angle
		double ySpeed = (double) fireInfo[1] * Math.sin(Math.toRadians(fireInfo[0]));
		// Set the speed of gravity
		double gravity = 9.8;
		// Set the current time to do physics
		double time = 0.00;
		// Update the angle of the tank firing arm
		tankArmUpdater(tank, 2);
		// Create an x and y points
		double x, y;
		try {
			do {
				// Sets the time 0.04 seconds forward
				time += 0.04;
				// Find how far horizontal distance traveled
				x = (xSpeed * time);
				// Sets the current height of the shell based on how long it has
				// been in the air
				y = ((ySpeed * time) + (((0.5) * gravity) * (Math.pow(time, 2))));
				// Translate the point from working at a 0,0 to the origin point
				// being a just above the tank arm
				posTranslator(tankInfo[tank][2][3][0], tankInfo[tank][2][3][1] - 15, (int) (Math.round(x)),
						(int) (Math.round(y)));
				System.out.println("Calculated X: " + x + " Y: " + y + " Time: " + time);
				// Draw the shell
				display.drawShell(shell);
				// Sleep for 17 milliseconds(this is 60fps)
				Thread.sleep(17);
				// Run the loop until a hit is calculated
			} while (false == hitDet());
		}
		// if an exception happens out put hit detection failure
		catch (Exception e) {
			System.out.println("Hit Detection failure");

		}
	}

	public boolean hitDet() {
		// Set the return to false(default response)
		boolean hitDet = false;
		// Creates an array of points based around the tank shell
		Point[] points = shellRadiusPoints();
		// Check if the shell hits a tank
		if (calcHitDual(tankPoly, points) == true) {
			hitDet = true;

		}
		// Check if the shell hits the world
		if (calcHit(worldPoly, points) == true) {
			hitDet = true;

		}
		// Check if the shell goes out of bounds
		if (shell[0] < -16 || shell[0] > 700) {
			hitDet = true;

		}
		// System.out.println("No hit");
		return hitDet;
	}

	// Methods for calculating hit on world
	public boolean calcHit(Polygon[] currentPoly, Point[] points) {
		boolean hit = false;
		// Set hit to false, loop though all polygons
		for (int i = 0; i < currentPoly.length; i++) {
			// Loop though all points and check for a hit
			for (int j = 0; j < points.length; j++) {
				// Check for hit
				if (currentPoly[i].contains(points[j])) {
					// If a hit is detected return true
					hit = true;
					// Break out of the loops
					break;
				}
			}
		}
		// Return if a hit was detected or not
		return hit;
	}

	// Methods for calculating hits on tanks
	public boolean calcHitDual(Polygon[][] currentPoly, Point[] points) {
		boolean hit = false;
		// Set hit to false, loop though all polygons
		for (int i = 0; i < currentPoly.length; i++) {
			// Loop though all parts of a tank
			for (int o = 0; o < 3; o++) {
				// Loop though all points and check for a hit
				for (int j = 0; j < points.length; j++) {
					// Check for hit
					if (currentPoly[i][o].contains(points[j])) {
						hit = true;
						// If a hit is detected return true
						// Set the the tank which was hit to have 50 less health
						tankHealth[i] = tankHealth[i] - 50;
						// Break out of the loops
						break;
					}
				}
			}
		}
		// Return if a hit was detected or not
		return hit;
	}

	// Generates the world
	public void createWorld() {
		// Creates and array of x and y points to generate the world polygons
		int x[] = new int[4];
		int y[] = new int[4];
		// Sets the first point of 4 to be up against the left wall
		world[0][0][0] = 0;
		// Sets the height of this point to be random
		world[0][0][1] = (int) ((Math.random() * 100) + 300);
		// Sets the bottom left point
		world[0][1][0] = 0;
		world[0][1][1] = 450;
		// Sets the bottom point 100 pixels right
		world[0][2][0] = 100;
		world[0][2][1] = 450;
		// Sets the last point 100 pixels right at a random height
		world[0][3][0] = 100;
		world[0][3][1] = (int) ((Math.random() * 100) + 300);
		// Loop though and create the rest if the world
		for (int i = 1; i < world.length; i++) {
			// Creates a point right based on the current point being polygon
			// being made
			world[i][0][0] = (700 / 7) * (i);
			// Sets the height equal to height of the last point made previously
			world[i][0][1] = world[i - 1][3][1];
			// Creates a point right of the origin based on the current point
			// being generated and the height to the bottom window
			world[i][1][0] = (700 / 7) * (i);
			world[i][1][1] = 450;
			// Creates a point 100 right of the previous made point
			world[i][2][0] = (700 / 7) * (i + 1);
			world[i][2][1] = 450;
			// Generates point 100 to the right of the first point and with a
			// random height
			world[i][3][0] = (700 / 7) * (i + 1);
			world[i][3][1] = (int) ((Math.random() * 100) + 300);
		}
		// Loop though and generate the polygons that make up the world
		for (int j = 0; j < 7; j++) {
			for (int i = 0; i < 4; i++) {
				// Get the points which make up the polygon
				x[i] = world[j][i][0];
				y[i] = world[j][i][1];
			}
			// Make the polygon
			worldPoly[j] = new Polygon(x, y, 4);
		}
		// Display the world
		display.drawWorld(worldPoly);

	}

	// Calculates the slope of the top of each world polygon
	public void calcSlope() {
		// Loop through each world polygon
		for (int i = 0; i < secSlopes.length; i++) {
			// Creates the slope of each like
			secSlopes[i][0] = (((double) (world[i][3][1]) - (double) (world[i][0][1]))
					/ ((double) (world[i][3][0]) - (double) (world[i][0][0])));
			// Creates the reciprocal slope
			secSlopes[i][1] = (((double) (world[i][3][0]) - (double) (world[i][0][0]))
					/ ((double) (world[i][3][1]) - (double) (world[i][0][1])) / (double) -1);
		}

	}

	// Methond to create the tanks starting location
	public void tankStartPos() {
		// Variable for the sector start point
		int sector;
		// Tank size dimensions
		double tankH = 15;
		double tankW = 35;
		// Array for the points that make up each polygon
		int x[] = new int[4];
		int y[] = new int[4];
		// Loop to generate each tank
		for (int i = 0; i < tankInfo.length; i++) {
			// Sets how far into a sector a tank starts drawing
			double startMod = 25;
			// Generates each tank
			if (i == 0) {
				// Picks one of 3 random sectors to spawn the tank in
				sector = (int) (Math.random() * 3);
				// Create the tank
				startPosMeth(i, sector, startMod, tankH, tankW);

			} else if (i == 1) {
				// Picks one of 3 random sectors to spawn the tank in
				sector = (int) (Math.random() * 3 + 4);
				// Create the tank
				startPosMeth(i, sector, startMod, tankH, tankW);
			}

		}
		// Loop to create the polygons which make up the tank
		for (int j = 0; j < 2; j++) {
			// Loop through all three parts which make up the tank
			for (int o = 0; o < 3; o++) {
				// Set the X and y array to be the points of the current shape
				for (int i = 0; i < 4; i++) {
					x[i] = tankInfo[j][o][i][0];
					y[i] = tankInfo[j][o][i][1];
				}
				// Create a polygon with these coordinates
				tankPoly[j][o] = new Polygon(x, y, 4);
			}
		}
	}

	// Method to generate all of the points which make up the tank
	public void startPosMeth(int i, int sector, double startMod, double tankH, double tankW) {
		// Create the first point of 4 which are part of a the tank shape
		tankInfo[i][0][1][0] = world[sector][0][0] + (int) (startMod);
		tankInfo[i][0][1][1] = world[sector][0][1] + (int) (Math.round(secSlopes[sector][0] * startMod));
		// Creates an angle which is perpendicular to the current slope of the
		// polygon the tank is spawning
		double angle1 = angleCalcP1(i, 1, sector, tankH, 0);
		// Generates the top left point by, inputing an angle and an length plus
		// which point is being moved
		tankInfo[i][0][0][0] = xPos(angle1, i, 1, sector, tankH, 0);
		tankInfo[i][0][0][1] = yPos(angle1, i, 1, sector, tankH, 0);
		// Start mod now becomes the width of the tank
		startMod = 35;
		// Creates a point to be manipulated
		tankInfo[i][0][2][0] = (((700 / 7) * sector) + (int) startMod);
		tankInfo[i][0][2][1] = world[sector][0][1] + (int) (Math.round(secSlopes[sector][0] * startMod));
		// Creates an angle which is parallel to the line
		double angle2 = angleCalcP2(i, 2, sector, tankW, 0);
		// Moves generated point
		tankInfo[i][0][2][0] = xPos(angle2, i, 2, sector, tankW, 0);
		tankInfo[i][0][2][1] = yPos(angle2, i, 2, sector, tankW, 0);

		// Creates the final point, point in the top right
		tankInfo[i][0][3][0] = xPos(angle1, i, 2, sector, tankH, 0);
		tankInfo[i][0][3][1] = yPos(angle1, i, 2, sector, tankH, 0);

		// This chuck below up until the next if is a repeat of the code above
		// except tankH is set us 5 and the shape being made is different
		startMod = 25;
		tankH = 5;
		tankInfo[i][1][1][0] = world[sector][0][0] + (int) (startMod);
		tankInfo[i][1][1][1] = world[sector][0][1] + (int) (Math.round(secSlopes[sector][0] * startMod));

		tankInfo[i][1][0][0] = xPos(angle1, i, 1, sector, tankH, 0);
		tankInfo[i][1][0][1] = yPos(angle1, i, 1, sector, tankH, 0);
		startMod = 35;
		// Generates origin in which the print is than moved
		tankInfo[i][1][2][0] = (((700 / 7) * sector) + (int) startMod);
		tankInfo[i][1][2][1] = world[sector][0][1] + (int) (Math.round(secSlopes[sector][0] * startMod));
		// Moves generated point
		tankInfo[i][1][2][0] = xPos(angle2, i, 2, sector, tankW, 1);
		tankInfo[i][1][2][1] = yPos(angle2, i, 2, sector, tankW, 1);
		// Creates the final point, point in the top right
		tankInfo[i][1][3][0] = xPos(angle1, i, 2, sector, tankH, 1);
		tankInfo[i][1][3][1] = yPos(angle1, i, 2, sector, tankH, 1);

		// If the slope is negative then the angle is 180 more then 90
		if (secSlopes[sector][0] < 0) {

			// Creates the point in the bottom left corner of the tank cannon
			tankInfo[i][2][1][0] = tankInfo[i][0][0][0] + (int) (16);
			tankInfo[i][2][1][1] = tankInfo[i][0][0][1] + (int) (Math.round(secSlopes[sector][0] * 16)) + 3;
			// Creating points up vertical, slope is negative so angle must be
			// 270
			tankInfo[i][2][0][0] = xPos(270, i, 1, sector, 10, 2);
			tankInfo[i][2][0][1] = yPos(270, i, 1, sector, 10, 2);
			// barrel width
			startMod = 4;
			// Generates origin in which the print is than moved
			tankInfo[i][2][2][0] = tankInfo[i][2][1][0] + (int) (startMod);
			tankInfo[i][2][2][1] = tankInfo[i][2][1][1] + (int) (Math.round(secSlopes[sector][0] * startMod));
			// Moves generated point
			tankInfo[i][2][2][0] = xPos(angle2, i, 2, sector, 4, 2);
			tankInfo[i][2][2][1] = yPos(angle2, i, 2, sector, 4, 2);
			// Creates the final point, point in the top right
			tankInfo[i][2][3][0] = xPos(270, i, 2, sector, 10, 2);
			tankInfo[i][2][3][1] = yPos(270, i, 2, sector, 10, 2);

		} else {

			// Creates the point in the bottom left corner of the tank cannon
			tankInfo[i][2][1][0] = tankInfo[i][0][0][0] + (int) (16);
			tankInfo[i][2][1][1] = tankInfo[i][0][0][1] + (int) (Math.round(secSlopes[sector][0] * 16)) + 3;
			// Creating points up vertical, since slope is positive the angle
			// must be 90
			tankInfo[i][2][0][0] = xPos(90, i, 1, sector, 10, 2);
			tankInfo[i][2][0][1] = yPos(90, i, 1, sector, 10, 2);
			// barrel width
			startMod = 4;
			// Generates origin in which the print is than moved
			tankInfo[i][2][2][0] = tankInfo[i][2][1][0] + (int) (startMod);
			tankInfo[i][2][2][1] = tankInfo[i][2][1][1] + (int) (Math.round(secSlopes[sector][0] * startMod));
			// Moves generated point
			tankInfo[i][2][2][0] = xPos(angle2, i, 2, sector, 4, 2);
			tankInfo[i][2][2][1] = yPos(angle2, i, 2, sector, 4, 2);
			// Creates the final point, point in the top right
			tankInfo[i][2][3][0] = xPos(90, i, 2, sector, 10, 2);
			tankInfo[i][2][3][1] = yPos(90, i, 2, sector, 10, 2);

		}

	}

	// Angle generator for perpendicular lines
	public double angleCalcP1(int i, int y, int slope, double length, int tankShape) {
		// Generates the angle angle of right angle triangle using tan
		// inverse(y/x) to find the angle of the line from the slope
		double angle = Math.round(Math.toDegrees(
				Math.atan2(((tankInfo[i][tankShape][y][0] - ((700 / 7) * slope)) * secSlopes[slope][0]), length)));
		// If the slope is negative add 270 degrees to the angle to ensure
		// perpendicular angle
		if (secSlopes[slope][0] < 0) {
			angle = (angle + 270);
		} else {
			// if the slope is positive only and 90 degrees to the angle to
			// ensure perpendicular angle
			angle = 90 + angle;
		}

		return angle;
	}

	// Angle generator for parallel lines
	public double angleCalcP2(int i, int y, int slope, double length, int tankShape) {
		// Generates the angle angle of right angle triangle using tan
		// inverse(y/x) to find the angle of the line from the slope
		double angle = Math.round(Math.toDegrees(
				Math.atan2(((tankInfo[i][tankShape][y][0] - ((700 / 7) * slope)) * secSlopes[slope][0]), length)));
		// If the slope is negative than add 360 degrees to ensure the line is
		// parallel, this is because out put angle will be negative if slope is
		// negative
		if (secSlopes[slope][0] < 0) {
			angle = (angle + 360);
		} else {
			// If the slope is positive then add only 180 degrees to ensure the
			// line is parallel
			angle = angle + 180;
		}

		return angle;
	}

	// This method takes the angle, the current tank, the point being rotated
	// around , the slope, the length of the line, and the current shape being
	// worked on and returns a new point
	
	public int xPos(double angle, int i, int j, int slope, double length, int tankShape) {
		//Set the return value to 0
		int xPos = 0;
		//Calculate the new x position by changing the angle to radians and then doing the equation rCosAngle
		xPos = (int) (Math.round(length * (Math.cos(Math.toRadians(angle)))));
		//Create a variable to hold the horizontal shift( this is because this math works around the 0,0
		int hShift = 0;
		//If the slope is negative then
		if (secSlopes[slope][0] < 0) {
			//Take a point on the tank and add the new point to this point
			hShift = (tankInfo[i][tankShape][j][0] + xPos);
		} else {
			//If the slope is negative then remove this new generated point value from the point currently being looked at
			hShift = (tankInfo[i][tankShape][j][0] - xPos);
		}
		//Set this shift equal to the point and return the point
		xPos = hShift;
		return xPos;
	}
	// This method takes the angle, the current tank, the point being rotated
	// around , the slope, the length of the line, and the current shape being
	// worked on and returns a new point
	public int yPos(double angle, int i, int j, int slope, double length, int tankShape) {
		int yPos = 0;
		//Calculate the new y position by changing the angle to radians and then doing the equation rSinAngle
		yPos = (int) (Math.round(length * (Math.sin(Math.toRadians(angle)))));
		int vShift = 0;
			//Take a point on the tank and add the new point to this point
		if (secSlopes[slope][0] < 0) {
			vShift = (tankInfo[i][tankShape][j][1] + yPos);
		} else {
			//If the slope is negative then remove this new generated point value from the point currently being looked at
			vShift = (tankInfo[i][tankShape][j][1] - yPos);
		}
		//Set this shift equal to the point and return the point
		yPos = vShift;
		return yPos;
	}

	public Point[] shellRadiusPoints() {
		// Calculating Shell Points
		//Create an array of points
		Point[] points = new Point[20];
		// Calculate the radius of the tank shell
		double rad = (shellDi / 2);
		//Loop through and generate points around the shell
		for (int i = 0; i < points.length; i++) {
			//Create the current angle to check which increases by a set amount each loop
			double curAngle = (((double) 360 / (double) points.length) + ((double) 360 / (double) points.length) * (double) i);
			//Create an x and y and shift
			int x, y;
			int vShift, hShift;
			//Use rCosAngle and rSinAngle to determine the new point
			x = (int) (Math.round(rad * (Math.cos(Math.toRadians(curAngle)))));
			y = (int) (Math.round(rad * (Math.sin(Math.toRadians(curAngle)))));
			
			//Set the point to be translated around the center of the shell
			hShift = shell[0] + x;
			vShift = shell[1] + y;
			//Add the radius so the points for a perfect circle around the mid point
			x = hShift + (int) rad;
			y = vShift + (int) rad;
			// Create the new point object
			points[i] = new Point(x, y);
		}
		//Return the new points array
		return points;
	}

	// Takes for points and manipulates them to return a new point,
	public void posTranslator(int xOrig, int yOrig, int xNew, int yNew) {
		// Changes the 0,0 to a new point on the plain
		shell[0] = (xOrig + xNew);
		shell[1] = (yOrig + yNew);
	}

	// Recalucates tank arm based on input angle
	public void tankArmUpdater(int tank, int sector) {
		// Sets how far to move from origin point
		double startMod = 25;
		double angle;
		// Create parallel angle
		double angle2 = angleCalcP2(tank, 1, sector, 35, 0);
		// If the slope is negative
		if (secSlopes[sector][0] < 0) {
			// Use the input fire angle which includes a 180 degree addition
			angle = fireInfo[0];

			// Creates the point in the bottom left corner of the tank cannon
			tankInfo[tank][2][1][0] = tankInfo[tank][2][1][0];
			tankInfo[tank][2][1][1] = tankInfo[tank][2][1][1];
			// Creating points up vertical
			tankInfo[tank][2][0][0] = xPos(angle, tank, 1, sector, 10, 2);
			tankInfo[tank][2][0][1] = yPos(angle, tank, 1, sector, 10, 2);
			startMod = 4;
			// Generates origin in which the print is than moved
			tankInfo[tank][2][2][0] = tankInfo[tank][2][1][0] + (int) (startMod);
			tankInfo[tank][2][2][1] = tankInfo[tank][2][1][1] + (int) (Math.round(secSlopes[sector][0] * startMod));
			// Moves generated point
			tankInfo[tank][2][2][0] = xPos(angle2, tank, 2, sector, 5, 2);
			tankInfo[tank][2][2][1] = yPos(angle2, tank, 2, sector, 5, 2);
			// Creates the final point, point in the top right
			tankInfo[tank][2][3][0] = xPos(angle, tank, 2, sector, 10, 2);
			tankInfo[tank][2][3][1] = yPos(angle, tank, 2, sector, 10, 2);

		} else {
			// Slope is positive so 180 degrees must be removed in order for the
			// arm to draw correctly
			angle = fireInfo[0] - 180;

			// Creates the point in the bottom left corner of the tank cannon
			tankInfo[tank][2][1][0] = tankInfo[tank][2][1][0];
			tankInfo[tank][2][1][1] = tankInfo[tank][2][1][1];
			// Creating points up vertical
			tankInfo[tank][2][0][0] = xPos(angle, tank, 1, sector, 10, 2);
			tankInfo[tank][2][0][1] = yPos(angle, tank, 1, sector, 10, 2);
			startMod = 4;
			// Generates origin in which the print is than moved
			tankInfo[tank][2][2][0] = tankInfo[tank][2][1][0] + (int) (startMod);
			tankInfo[tank][2][2][1] = tankInfo[tank][2][1][1] + (int) (Math.round(secSlopes[sector][0] * startMod));
			// Moves generated point
			tankInfo[tank][2][2][0] = xPos(angle2, tank, 2, sector, 5, 2);
			tankInfo[tank][2][2][1] = yPos(angle2, tank, 2, sector, 5, 2);
			// Creates the final point, point in the top right
			tankInfo[tank][2][3][0] = xPos(angle, tank, 2, sector, 10, 2);
			tankInfo[tank][2][3][1] = yPos(angle, tank, 2, sector, 10, 2);

		}
		// Creates an array of x and y points
		int x[] = new int[4];
		int y[] = new int[4];
		// Loops though and sets the array to be the new points generated
		for (int i = 0; i < 4; i++) {
			x[i] = tankInfo[tank][2][i][0];
			y[i] = tankInfo[tank][2][i][1];
		}
		// Create a new polygon for the tank barrel
		tankPoly[tank][2] = new Polygon(x, y, 4);
		// Redraw tank arms with new angle
		display.drawTank(tankPoly);
	}

	// Update health bars
	public void updateHeatlhBars() {
		// Loop through both health bars
		for (int i = 0; i < 2; i++) {
			// Set the health bar to display the percentage
			hpBar[i].setStringPainted(true);
			// Set the bars value
			hpBar[i].setValue(tankHealth[i]);
			// Update the bar
			hpBar[i].updateUI();
		}
	}
	public void updateScore(){
		//Update all scores
		scoreDigits[0].setText(score[0] + "");
		scoreDigits[1].setText(score[1] + "");
		//REPAINT the score on the screen
		repaint();
	}
	public ArrayList<Integer>[] findFireSettings(){
		ArrayList<Integer>[] settings = new ArrayList[2]; 
		settings[0] = new ArrayList<Integer>();
		settings[1] = new ArrayList<Integer>();
		for(int i = 200; i < 266; i++){
			for(int j = 20; j < 80; j++){
				shellCalcAI(1,i,j);
				if ( tankAiHit == 0){
					settings[0].add(i);
					settings[1].add(j);
					tankAiHit = 1;
				}
			}
		}
		
		System.out.println(settings[0].size());
		
		return settings;
	}
	public void shellCalcAI(int tank, int Angle, int Power) {
		// Determine the horizontal speed based on the input speed and input
		// fire angle
		double xSpeed = (double) Power * Math.cos(Math.toRadians(Angle));
		// Determine the vertical speed based on the input speed and input fire
		// angle
		double ySpeed = (double) Power * Math.sin(Math.toRadians(Angle));
		// Set the speed of gravity
		double gravity = 9.8;
		// Set the current time to do physics
		double time = 0.00;
		// Create an x and y points
		double x, y;
		try {
			do {
				// Sets the time 0.04 seconds forward
				time += 0.04;
				// Find how far horizontal distance traveled
				x = (xSpeed * time);
				// Sets the current height of the shell based on how long it has
				// been in the air
				y = ((ySpeed * time) + (((0.5) * gravity) * (Math.pow(time, 2))));
				// Translate the point from working at a 0,0 to the origin point
				// being a just above the tank arm
				posTranslator(tankInfo[tank][2][3][0], tankInfo[tank][2][3][1] - 15, (int) (Math.round(x)),
						(int) (Math.round(y)));
	
				// Draw the shell
				// Sleep for 17 milliseconds(this is 60fps)
				// Run the loop until a hit is calculated
			} while (false == hitDetAi());
		}
		// if an exception happens out put hit detection failure
		catch (Exception e) {
			System.out.println("Hit Detection failure");

		}
	}
	public boolean hitDetAi() {
		// Set the return to false(default response)
		boolean hitDet = false;
		// Creates an array of points based around the tank shell
		Point[] points = shellRadiusPoints();
		// Check if the shell hits a tank
		if (calcHitDualAi(tankPoly, points) == true) {
			hitDet = true;

		}
		// Check if the shell hits the world
		if (calcHitAi(worldPoly, points) == true) {
			hitDet = true;

		}
		// Check if the shell goes out of bounds
		if (shell[0] < -16 || shell[0] > 700) {
			hitDet = true;

		}
		// System.out.println("No hit");
		return hitDet;
	}
	// Methods for calculating hit on world
		public boolean calcHitAi(Polygon[] currentPoly, Point[] points) {
			boolean hit = false;
			// Set hit to false, loop though all polygons
			for (int i = 0; i < currentPoly.length; i++) {
				// Loop though all points and check for a hit
				for (int j = 0; j < points.length; j++) {
					// Check for hit
					if (currentPoly[i].contains(points[j])) {
						// If a hit is detected return true
						hit = true;
						
						// Break out of the loops
						break;
					}
				}
			}
			// Return if a hit was detected or not
			return hit;
		}

		// Methods for calculating hits on tanks
		public boolean calcHitDualAi(Polygon[][] currentPoly, Point[] points) {
			boolean hit = false;
			// Set hit to false, loop though all polygons
			for (int i = 0; i < currentPoly.length; i++) {
				// Loop though all parts of a tank
				for (int o = 0; o < 3; o++) {
					// Loop though all points and check for a hit
					for (int j = 0; j < points.length; j++) {
						// Check for hit
						if (currentPoly[i][o].contains(points[j])) {
							//If enemy tank is hit return true else return false
							hit = true;
							tankAiHit = i;
							// If a hit is detected return true
							
							
							// Break out of the loops
							break;
						}
					}
				}
			}
			// Return if a hit was detected or not
			return hit;
		}

}