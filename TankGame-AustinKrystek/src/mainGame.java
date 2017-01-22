
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class mainGame extends JFrame  {

	//Creates a container for the frame
	Container frame;
	//Creates gameInfo object to hold important information
	private JPanel gameInfo;
	//Creates health bar object
	private JPanel[] hBarPanel;
	//Sets the array of health bars
	private JProgressBar[] hpBar = new JProgressBar[2];
	
	//Creates display object
	private DrawGame display;
	//Tank health
	int [] tankHealth = new int [2];
	//If a tank has fired and which tank
	int [] tankFired = new int [2];
	//Power and fire angle
	int [] fireInfo = new int [2];
	//Location of tank
	int [][][][] tankInfo= new int[2][3][4][2];;
	//World Map of points
	int[][][] world = new int [7][4][2];
	//Sector Slopes and reciprocal
	double[][] secSlopes = new double[7][2];
	//Creates an array of polygons that make up the tank
	Polygon[][] tankPoly = new Polygon[2][3];
	//Creates an array of polygons that make up the world
	Polygon[] worldPoly = new Polygon[7];
	//Sets the diameter of the tank round
	int shellDi = 8;
	//Creates an array to hold the information about the tank shell
	int[] shell = {0,0,shellDi,shellDi};
	//Which players turn is it
	int currentTurn = 0;
	//Creates main window of game
	public mainGame(){
		//Set the title of the window
		setTitle("Tank Game");
		//Set array of JPanels to hold health bars
		hBarPanel = new JPanel[2];
		//Sets health bars to exist in the hpBar array
		hpBar[0] = new JProgressBar(0,100);
		hpBar[1] = new JProgressBar(0,100);
		//Set frame to getContentPane
		frame = getContentPane();
		//Sets the gameInfo window to be a jpanel
		gameInfo = new JPanel();
		//Sets the display to be a DrawGame object
		display = new DrawGame();
		//Sets the layout to null so items can be placed anywhere
		frame.setLayout(null);
		//Sets size of the game info window
		gameInfo.setSize(800,100);
		//Sets location of gameInfo
		gameInfo.setLocation(0,0);
		//Set the background color
		gameInfo.setBackground(Color.BLACK);
		//Sets the layout of the gameinfo window
		gameInfo.setLayout(null);
		//Set the display size(game is played in here)
		display.setSize(700,450);
		//Set top left start point
		display.setLocation(50,100);
		//Set the hParPanel array elements to be a new JPanel
		hBarPanel[0] = new JPanel();
		hBarPanel[1] = new JPanel();
		//Set the layout of the Panels to be null
		hBarPanel[0].setLayout(null);
		hBarPanel[1].setLayout(null);
		//Set the background of these panels(Not needed)
		hBarPanel[0].setBackground(Color.GREEN);
		hBarPanel[1].setBackground(Color.RED);
		//Set location of each health bar panel
		hBarPanel[0].setLocation(100, 80);
		hBarPanel[1].setLocation(600, 80);
		
		//Set the size of each health bar panel
		hBarPanel[0].setSize(100,15);
		hBarPanel[1].setSize(100,15);
		//Set the size of the hp bar
		hpBar[0].setSize(100,15);
		hpBar[1].setSize(100,15);
		//Add the hpBar to each the health bar panel
		hBarPanel[0].add(hpBar[0]);
		hBarPanel[1].add(hpBar[1]);
		//add the health bar panels to the game info panel
		gameInfo.add(hBarPanel[0]);
		gameInfo.add(hBarPanel[1]);
		//adds the gameinfo panel panel to the main window
		frame.add(gameInfo);
		//adds the display panel to the main window
		frame.add(display);
		//Set the window to not be resizeable
		setResizable(false);
		//Set the size of the application window
		setSize(800, 640);
		//Set the window to visible
		setVisible(true);
		//Run main game
		run();
	}
	
	public static void main(String[] args) {
		
			//Create the maine game
			new mainGame();
			//Close the application 
			System.exit(0); 
		 
	}
	

	public void run(){
		//Out put game setting up
		System.out.println("Setting Up");
		//Sets up game
		setupGame();
		//Run game as long as both players have more than 0 health
		while (tankHealth[0]>0 && tankHealth[1]>0){
			//Check which players turn it is
			if(currentTurn == 0){	
				//Get the input angle, and flips it 180 because 0,0 is in the top left
				fireInfo[0] = (Integer.parseInt(JOptionPane.showInputDialog("Pleace enter the fire angle"))+180);
				//Gets input fire power
				fireInfo[1] = (Integer.parseInt(JOptionPane.showInputDialog("Pleace enter the fire power")));
				//Calculate where the shell should be and if it hits
				shellCalc(currentTurn);
				//Set the turn to be the next players turn
				currentTurn = 1;
			}else if(currentTurn == 1){
				//Get the input angle, and flips it 180 because 0,0 is in the top left
				fireInfo[0] = (Integer.parseInt(JOptionPane.showInputDialog("Pleace enter the fire angle"))+180);
				//Gets input fire power
				fireInfo[1] = (Integer.parseInt(JOptionPane.showInputDialog("Pleace enter the fire power")));
				//Calculate where the shell should be and if it hits
				shellCalc(currentTurn);
				//Set the turn to be the next players turn
				currentTurn = 0;
			}
			//Update the health bars with the new health
			updateHeatlhBars();
		}
	}
	public void setupGame(){
		//drawTanks();
		//Create world
		createWorld();
		//Calculate the slopes of each line
		calcSlope();
		//Create the tanks start positions
		tankStartPos();
		//Display the new tanks
		display.drawTank(tankPoly);
		//Set the tanks health
		tankHealth[0] = 100;
		tankHealth[1] = 100;
		//Sets which tank has fired(unused atm)
		tankFired[0] = 0;
		tankFired[1] = 0;
		//Update the health bars
		updateHeatlhBars();
	}
	public void shellCalc(int tank){
		//Determine the horizontal speed based on the input speed and input fire angle
		double xSpeed = (double)fireInfo[1]*Math.cos(Math.toRadians(fireInfo[0]));
		//Determine the vertical speed based on the input speed and input fire angle
		double ySpeed = (double)fireInfo[1]*Math.sin(Math.toRadians(fireInfo[0]));
		//Set the speed of gravity
		double gravity = 9.8;
		//Set the current time to do physics
		double time = 0.00;
		//Update the angle of the tank firing arm
		tankArmUpdater(tank,2);
		//Create an x and y points
		double x,y; 
		try{
			do{
				//Sets the time 0.04 seconds forward
				time += 0.04;
				//Find how far horizontal distance traveled
				x = (xSpeed*time);
				//Sets the current height of the shell based on how long it has been in the air
				y = ((ySpeed*time) + (((0.5)*gravity)*(Math.pow(time, 2))));
				//Translate the point from working at a 0,0 to the origin point being a just above the tank arm
				posTranslator(tankInfo[tank][2][3][0],tankInfo[tank][2][3][1]-15,(int)(Math.round(x)),(int)(Math.round(y)));
				System.out.println("Calculated X: " + x + " Y: " + y+ " Time: " + time);
				//Draw the shell
				display.drawShell(shell);
				//Sleep for 17 milliseconds(this is 60fps)
				Thread.sleep(17);
				//Run the loop until a hit is calculated
			}while(false == hitDet());	
		}
		//if an exception happens out put hit detection failure
		catch(Exception e){
			System.out.println("Hit Detection failure");
		
		}
	}

	
	public boolean hitDet (){
		//Set the return to false(default response) 
		boolean hitDet = false;
		//Creates an array of points based around the tank shell
		Point[] points = shellRadiusPoints();
		//Check if the shell hits a tank
		if(calcHitDual(tankPoly,points) == true){
			hitDet = true;
			
		}
		//Check if the shell hits the world
		if(calcHit(worldPoly,points) == true){
			hitDet = true;
			
		}
		//Check if the shell goes out of bounds
		if(shell[0] < -16 || shell[0] > 700){
			hitDet = true;
			
		}
		//System.out.println("No hit");
		return hitDet;
	}
	//Methods for calculating hit on world
	public boolean calcHit (Polygon[] currentPoly, Point[] points){
		boolean hit = false;
		//Set hit to false, loop though all polygons
		for(int i = 0; i < currentPoly.length; i++){
			//Loop though all points and check for a hit
			for(int j = 0; j < points.length; j++){
				//Check for hit
				if (currentPoly[i].contains(points[j])){
					//If a hit is detected return true
					hit = true;
					//Break out of the loops
					break;
				}
			}
		}
		//Return if a hit was detected or not
		return hit;
	}
	//Methods for calculating hits on tanks
	public boolean calcHitDual (Polygon[][] currentPoly, Point[] points){
		boolean hit = false;
		//Set hit to false, loop though all polygons
		for(int i = 0; i < currentPoly.length; i++){
			//Loop though all parts of a tank
			for(int o = 0; o < 3; o++){
				//Loop though all points and check for a hit
				for(int j = 0; j < points.length; j++){
					//Check for hit
					if (currentPoly[i][o].contains(points[j])){
						hit = true;
						//If a hit is detected return true
						//Set the the tank which was hit to have 50 less health
						tankHealth[i] = tankHealth[i]-50;
						//Break out of the loops
						break;
					}
				}
			}
		}
		//Return if a hit was detected or not
		return hit;
	}
	//Generates the world
	public void createWorld(){
		//Creates and array of x and y points to generate the world polygons
		int x[] = new int[4];
		int y[] = new int[4];
		//Sets the first point of 4 to be up against the left wall
		world[0][0][0] = 0;
		//Sets the height of this point ot be random
		world[0][0][1] = (int)((Math.random()*100)+300);
		//Sets the bottom left point
		world[0][1][0] = 0;
		world[0][1][1] = 450;
		//Sets the bottom point 100 pixels right
		world[0][2][0] = 100;
		world[0][2][1] = 450;
		//Sets the last point 100 pixels right at a random height
		world[0][3][0] = 100;
		world[0][3][1] = (int)((Math.random()*100)+300);
		//Loop though and create the rest if the world
		for(int i = 1; i < world.length;i++ ){
			//Creates a point right based on the current point being polygon being made
			world[i][0][0] = (700/7)*(i);
			//Sets the height equal to height of the last point made previously
			world[i][0][1] = world[i-1][3][1];
			//Creates a point right of the origin based on the current point being generated and the height to the bottom window
			world[i][1][0] = (700/7)*(i);
			world[i][1][1] = 450;
			//Creates a point 100 right of the previous made point
			world[i][2][0] = (700/7)*(i+1);
			world[i][2][1] = 450;
			//Generates point 100 to the right of the first point and with a random height
			world[i][3][0] = (700/7)*(i+1);
			world[i][3][1] = (int)((Math.random()*100)+300);
		}
		//Loop though and generate the polygons that make up the world
		for(int j = 0; j < 7; j++){
			for(int i = 0; i < 4;i++){
				//Get the points which make up the polygon
				x[i] = world[j][i][0];
				y[i] = world[j][i][1];	
			}
			//Make the polygon
			worldPoly[j] = new Polygon(x,y,4);
		}
		//Display the world
		display.drawWorld(worldPoly);
		
	}
	
	public void calcSlope (){
		for(int i = 0; i < secSlopes.length;i++){
			secSlopes[i][0] = (((double)(world[i][3][1])-(double)(world[i][0][1]))/((double)(world[i][3][0])-(double)(world[i][0][0])));
			
			secSlopes[i][1] = (((double)(world[i][3][0])-(double)(world[i][0][0]))/((double)(world[i][3][1])-(double)(world[i][0][1]))/(double)-1);
			System.out.println(secSlopes[i][0]);
			System.out.println(secSlopes[i][1]);
		}
		for(int i = 0; i < secSlopes.length;i++){
			System.out.println(world[i][3][1] + " " + world[i][0][1] + " " + world[i][3][0] + " " +world[i][0][0] );
			
			
		}
	}
	
	public void tankStartPos (){
		int sector;
		double tankH = 15;
		double tankW = 35;
		int x[] = new int[4];
		int y[] = new int[4];
		System.out.println(tankInfo.length+"");
		for(int i = 0; i < tankInfo.length;i++){
			double startMod = 25;
			if(i == 0){
				sector = 1;
				startPosMeth(i,sector,startMod,tankH,tankW);
				
			}
			else if(i == 1){
				sector = 5;
				startPosMeth(i,sector,startMod,tankH,tankW);
			}
			
		}
		for(int j = 0; j < 2; j++){
			for(int o = 0; o < 3;o++){
				for(int i = 0; i < 4;i++){
					x[i] = tankInfo[j][o][i][0];
					y[i] = tankInfo[j][o][i][1];	
				}
				tankPoly[j][o] = new Polygon(x,y,4);
			}
		}
	System.out.println("x: " + tankInfo[0][0][0]+" y: "+ tankInfo[0][0][1]);
	
	}
	public void startPosMeth(int i, int sector, double startMod, double tankH, double tankW){
		//double tankTopSlope;
		tankInfo[i][0][1][0] = world[sector][0][0] + (int)(startMod); 
		tankInfo[i][0][1][1] = world[sector][0][1] + (int)(Math.round(secSlopes[sector][0]*startMod));
		double angle1 = angleCalcP1(i,1,sector,tankH,0);
		tankInfo[i][0][0][0] = xPos(angle1,i,1,sector,tankH,0);
		tankInfo[i][0][0][1] = yPos(angle1,i,1,sector,tankH,0);
		startMod = 35;
		
		tankInfo[i][0][2][0] = (((700/7)*sector)+(int)startMod); 
		tankInfo[i][0][2][1] = world[sector][0][1] + (int)(Math.round(secSlopes[sector][0]*startMod));
		
		double angle2 = angleCalcP2(i,2,sector,tankW,0);
		tankInfo[i][0][2][0] = xPos(angle2,i,2,sector,tankW,0); 
		tankInfo[i][0][2][1] = yPos(angle2,i,2,sector,tankW,0);
		
		//tankInfo[1][0][3][0] = world[sector][3][0] + (int)(startMod); 
		//tankInfo[i][0][3][1] = world[sector][3][1] + (int)(Math.round(secSlopes[sector][0]*startMod));
		//angle1 = angleCalcP1(i,3,sector,tankH);
		tankInfo[i][0][3][0] = xPos(angle1,i,2,sector,tankH,0);
		tankInfo[i][0][3][1] = yPos(angle1,i,2,sector,tankH,0);
		
		startMod = 25;
		tankH = 5;
		tankInfo[i][1][1][0] = world[sector][0][0] + (int)(startMod); 
		tankInfo[i][1][1][1] = world[sector][0][1] + (int)(Math.round(secSlopes[sector][0]*startMod));
		
		tankInfo[i][1][0][0] = xPos(angle1,i,1,sector,tankH,0);
		tankInfo[i][1][0][1] = yPos(angle1,i,1,sector,tankH,0);
		startMod = 35;
		//Generates origin in which the print is than moved
		tankInfo[i][1][2][0] = (((700/7)*sector)+(int)startMod); 
		tankInfo[i][1][2][1] = world[sector][0][1] + (int)(Math.round(secSlopes[sector][0]*startMod));
		//Moves generated point
		tankInfo[i][1][2][0] = xPos(angle2,i,2,sector,tankW,1); 
		tankInfo[i][1][2][1] = yPos(angle2,i,2,sector,tankW,1);
		
		tankInfo[i][1][3][0] = xPos(angle1,i,2,sector,tankH,1);
		tankInfo[i][1][3][1] = yPos(angle1,i,2,sector,tankH,1);
		if(secSlopes[sector][0]< 0){
			//This method takes the angle, the current tank, the point being rotated around , the slope, the length of the line, and the current shape being worked on
			//Creates the point in the bottom left corner of the tank cannon
			tankInfo[i][2][1][0] = tankInfo[i][0][0][0] + (int)(16);
			tankInfo[i][2][1][1] = tankInfo[i][0][0][1] + (int)(Math.round(secSlopes[sector][0]*16)) + 3;
			//Creating points up vertical
			tankInfo[i][2][0][0] = xPos(270,i,1,sector,10,2);
			tankInfo[i][2][0][1] = yPos(270,i,1,sector,10,2);
			startMod = 4;
			//Generates origin in which the print is than moved
			tankInfo[i][2][2][0] = tankInfo[i][2][1][0] + (int)(startMod); 
			tankInfo[i][2][2][1] = tankInfo[i][2][1][1] + (int)(Math.round(secSlopes[sector][0]*startMod));
			//Moves generated point
			tankInfo[i][2][2][0] = xPos(angle2,i,2,sector,4,2); 
			tankInfo[i][2][2][1] = yPos(angle2,i,2,sector,4,2);
			
			tankInfo[i][2][3][0] = xPos(270,i,2,sector,10,2);
			tankInfo[i][2][3][1] = yPos(270,i,2,sector,10,2);
			
		}else{
			//This method takes the angle, the current tank, the point being rotated around , the slope, the length of the line, and the current shape being worked on
			//Creates the point in the bottom left corner of the tank cannon
			tankInfo[i][2][1][0] = tankInfo[i][0][0][0] + (int)(16);
			tankInfo[i][2][1][1] = tankInfo[i][0][0][1] + (int)(Math.round(secSlopes[sector][0]*16))+ 3;
			//Creating points up vertical
			tankInfo[i][2][0][0] = xPos(90,i,1,sector,10,2);
			tankInfo[i][2][0][1] = yPos(90,i,1,sector,10,2);
			startMod = 4;
			//Generates origin in which the print is than moved
			tankInfo[i][2][2][0] = tankInfo[i][2][1][0] + (int)(startMod); 
			tankInfo[i][2][2][1] = tankInfo[i][2][1][1] + (int)(Math.round(secSlopes[sector][0]*startMod));
			//Moves generated point
			tankInfo[i][2][2][0] = xPos(angle2,i,2,sector,4,2); 
			tankInfo[i][2][2][1] = yPos(angle2,i,2,sector,4,2);
			
			tankInfo[i][2][3][0] = xPos(90,i,2,sector,10,2);
			tankInfo[i][2][3][1] = yPos(90,i,2,sector,10,2);
		
		}
		
		System.out.println("X0: " + tankInfo[i][2][0][0] +" Y0:" + tankInfo[i][2][0][1]);
		System.out.println("X1: " + tankInfo[i][2][1][0] +" Y1:" + tankInfo[i][2][1][1]);
		System.out.println("X2: " + tankInfo[i][2][2][0] +" Y2:" + tankInfo[i][2][2][1]);
		System.out.println("X3: " + tankInfo[i][2][3][0] +" Y3:" + tankInfo[i][2][3][1]);
		
	}
	public double angleCalcP1 (int i,int y,int slope,double length,int tankShape){
		double angle = Math.round(Math.toDegrees(Math.atan2(((tankInfo[i][tankShape][y][0]-((700/7)*slope))*secSlopes[slope][0]),length)));
		System.out.println("Angle1: "+ angle);
		System.out.println("tankInfo[i][y][0] "+ tankInfo[i][y][0]);
		if(secSlopes[slope][0] < 0){
			angle = (angle+270);
		}
		else{
			
			angle = 90 + angle;
		}
		System.out.println("Angle1: "+ angle);
		return angle;
	}
	public double angleCalcP2 (int i,int y,int slope,double length,int tankShape){
		double angle = Math.round(Math.toDegrees(Math.atan2(((tankInfo[i][tankShape][y][0]-((700/7)*slope))*secSlopes[slope][0]),length)));
		System.out.println("Angle2: "+ angle);
		System.out.println("tankInfo[i][y][0] "+ tankInfo[i][tankShape][y][0]);
		if(secSlopes[slope][0] < 0){
			angle = (angle+360);
		}
		else{
			
			angle = angle+180;
		}
		System.out.println("Angle2: "+ angle);
		return angle;
	}
	//This method takes the angle, the current tank, the point being rotated around , the slope, the length of the line, and the current shape being worked on
	public int xPos (double angle,int i,int j,int slope,double length,int tankShape){
		int xPos = 0;
		xPos = (int)(Math.round(length*(Math.cos(Math.toRadians(angle)))));
		int hShift = 0;
		System.out.println("x: " +xPos+" ");
		if(secSlopes[slope][0] < 0){
			hShift = (tankInfo[i][tankShape][j][0]+xPos);
		}
		else{
			hShift = (tankInfo[i][tankShape][j][0]-xPos);
		}
		xPos = hShift;
		return xPos;
	}
	public int yPos (double angle,int i,int j,int slope,double length,int tankShape){
		int yPos = 0;
		yPos = (int)(Math.round(length*(Math.sin(Math.toRadians(angle)))));
		int vShift = 0;
		System.out.println("y: " +yPos+"");
		if(secSlopes[slope][0] < 0){
			vShift = (tankInfo[i][tankShape][j][1] + yPos);
		}
		else{
			vShift = (tankInfo[i][tankShape][j][1] - yPos);
		}
		yPos = vShift;
		return yPos;
	}
	
	public Point[] shellRadiusPoints (){
		//Calculating Shell Points
		//System.out.println("Calculating Points");
		Point[] points = new Point[20];
		double rad = (shellDi/2);
		//System.out.println("Generating New Points");
		for(int i = 0; i < points.length; i++){
			double curAngle = (((double)360/(double)points.length) + ((double)360/(double)points.length)*(double)i);
			//System.out.println("Current Angle: "+ curAngle);
			int x,y;
			int vShift, hShift;
			x = (int)(Math.round(rad*(Math.cos(Math.toRadians(curAngle)))));
			y = (int)(Math.round(rad*(Math.sin(Math.toRadians(curAngle)))));
			//System.out.println("Current Point Generated: " + "X: "+ x + " Y: "+ y);
			hShift = shell[0] + x;
			vShift = shell[1] + y;
			x = hShift + (int)rad;
			y = vShift + (int)rad;
			points[i] = new Point (x,y);
		}
		return points;
	}
	public void posTranslator (int xOrig, int yOrig, int xNew, int yNew){
		shell[0] = (xOrig + xNew);
		shell[1] = (yOrig + yNew);
	}
	//Recalucates tank arm based on input angle
	public void tankArmUpdater(int tank, int sector){
		double startMod = 25;
		double angle;
		double angle2 = angleCalcP2(tank,1,sector,35,0);
		if(secSlopes[sector][0]< 0){
			angle = fireInfo[0];
			//This method takes the angle, the current tank, the point being rotated around , the slope, the length of the line, and the current shape being worked on
			//Creates the point in the bottom left corner of the tank cannon
			tankInfo[tank][2][1][0] = tankInfo[tank][2][1][0];
			tankInfo[tank][2][1][1] = tankInfo[tank][2][1][1];
			//Creating points up vertical
			tankInfo[tank][2][0][0] = xPos(angle,tank,1,sector,10,2);
			tankInfo[tank][2][0][1] = yPos(angle,tank,1,sector,10,2);
			startMod = 4;
			//Generates origin in which the print is than moved
			tankInfo[tank][2][2][0] = tankInfo[tank][2][1][0] + (int)(startMod); 
			tankInfo[tank][2][2][1] = tankInfo[tank][2][1][1] + (int)(Math.round(secSlopes[sector][0]*startMod));
			//Moves generated point
			tankInfo[tank][2][2][0] = xPos(angle2,tank,2,sector,5,2); 
			tankInfo[tank][2][2][1] = yPos(angle2,tank,2,sector,5,2);
			
			tankInfo[tank][2][3][0] = xPos(angle,tank,2,sector,10,2);
			tankInfo[tank][2][3][1] = yPos(angle,tank,2,sector,10,2);
			
		}else{
			angle = fireInfo[0]-180;
			//This method takes the angle, the current tank, the point being rotated around , the slope, the length of the line, and the current shape being worked on
			//Creates the point in the bottom left corner of the tank cannon
			tankInfo[tank][2][1][0] = tankInfo[tank][2][1][0];
			tankInfo[tank][2][1][1] = tankInfo[tank][2][1][1];
			//Creating points up vertical
			tankInfo[tank][2][0][0] = xPos(angle,tank,1,sector,10,2);
			tankInfo[tank][2][0][1] = yPos(angle,tank,1,sector,10,2);
			startMod = 4;
			//Generates origin in which the print is than moved
			tankInfo[tank][2][2][0] = tankInfo[tank][2][1][0] + (int)(startMod); 
			tankInfo[tank][2][2][1] = tankInfo[tank][2][1][1] + (int)(Math.round(secSlopes[sector][0]*startMod));
			//Moves generated point
			tankInfo[tank][2][2][0] = xPos(angle2,tank,2,sector,5,2); 
			tankInfo[tank][2][2][1] = yPos(angle2,tank,2,sector,5,2);
			
			tankInfo[tank][2][3][0] = xPos(angle,tank,2,sector,10,2);
			tankInfo[tank][2][3][1] = yPos(angle,tank,2,sector,10,2);
		
		}
		System.out.println("X0: " + tankInfo[tank][2][0][0] +" Y0:" + tankInfo[tank][2][0][1]);
		System.out.println("X1: " + tankInfo[tank][2][1][0] +" Y1:" + tankInfo[tank][2][1][1]);
		System.out.println("X2: " + tankInfo[tank][2][2][0] +" Y2:" + tankInfo[tank][2][2][1]);
		System.out.println("X3: " + tankInfo[tank][2][3][0] +" Y3:" + tankInfo[tank][2][3][1]);
		int x[] = new int[4];
		int y[] = new int[4];
		for(int i = 0; i < 4;i++){
			x[i] = tankInfo[tank][2][i][0];
			y[i] = tankInfo[tank][2][i][1];	
		}
		tankPoly[tank][2] = new Polygon(x,y,4);
		display.drawTank(tankPoly);
	}
	public void updateHeatlhBars(){
		for (int i = 0 ; i < 2; i++){
			hpBar[i].setStringPainted(true);
			hpBar[i].setValue(tankHealth[i]);
			hpBar[i].updateUI();
		}
	}
}