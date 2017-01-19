
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class mainGame extends JFrame  {

	
	Container frame;
	private JPanel gameInfo;
	private DrawGame display;
	//Tank health
	int [] tankHealth = new int [2];
	//If a tank has fired and which tank
	int [] tankFired = new int [2];
	//Power and fire angle
	int [] fireInfo = new int [2];
	//Location of tank
	int [][][][] tankInfo= new int[2][3][4][2];;
	//World Map
	int[][][] world = new int [7][4][2];
	//Sector Slopes and reciprocal
	double[][] secSlopes = new double[7][2];
	Polygon[][] tankPoly = new Polygon[2][3];
	Polygon[] worldPoly = new Polygon[7];
	int shellDi = 8;
	int[] shell = {0,0,shellDi,shellDi};
	public mainGame(){
		setTitle("Tank Game");
		
		frame = getContentPane();
		gameInfo = new JPanel();
		display = new DrawGame();
		frame.setLayout(null);
		gameInfo.setSize(100,50);
		gameInfo.setLocation(0,0);
		display.setSize(700,450);
		display.setLocation(50,100);
		frame.add(gameInfo);
		frame.add(display);
		setResizable(false);
		setSize(800, 640);
		setVisible(true);
		run();
	}
	
	public static void main(String[] args) {
		
			
			new mainGame();
			System.exit(0); 
		 
	}
	/*
	public void drawTanks() {
		tankInfo = new int[2][1][4];
		tankInfo[0][0][0] = 50;
		tankInfo[0][0][1] = 380;
		tankInfo[0][0][2] = 60;
		tankInfo[0][0][3] = 20;
		tankInfo[1][0][0] = 600;
		tankInfo[1][0][1] = 380;
		tankInfo[1][0][2] = 60;
		tankInfo[1][0][3] = 20;
		display.drawTanks(tankInfo);

	}
	*/
	public void run(){
		
		System.out.println("Setting Up");
		setupGame();
		while (tankHealth[0]>0 && tankHealth[1]>0){
				
			fireInfo[0] = (Integer.parseInt(JOptionPane.showInputDialog("Pleace enter the fire angle"))+180);
			//If shell travels the wrong direction 
			fireInfo[1] = (Integer.parseInt(JOptionPane.showInputDialog("Pleace enter the fire power")));
			shellCalc();
			break;
		}
	}
	public void setupGame(){
		//drawTanks();
		createWorld();
		calcSlope();
		tankStartPos();
		display.drawTest(tankPoly);
		tankHealth[0] = 100;
		tankHealth[1] = 100;
		tankFired[0] = 0;
		tankFired[1] = 0;
	}
	public void shellCalc(){
		double xSpeed = (double)fireInfo[1]*Math.cos(Math.toRadians(fireInfo[0]));
		double ySpeed = (double)fireInfo[1]*Math.sin(Math.toRadians(fireInfo[0]));
		double gravity = 9.8;
		double time = 0.00;
		System.out.println("Fire Angle: "+ fireInfo[0]);
		System.out.println("xSpeed: " + xSpeed+ " ySpeed: "+ ySpeed);
		tankArmUpdater(0,2);
		double x,y; 
		try{
			do{
				time += 0.04;
				x = (xSpeed*time);
				y = ((ySpeed*time) + (((0.5)*gravity)*(Math.pow(time, 2))));
				posTranslator(tankInfo[0][0][3][0],tankInfo[0][0][3][1]-20,(int)(Math.round(x)),(int)(Math.round(y)));
				System.out.println("Calculated X: " + x + " Y: " + y+ " Time: " + time);
				display.drawShell(shell);
				
				Thread.sleep(17);
			}while(false == hitDet());	
		}
		catch(Exception e){
			System.out.println("Hit Detection failure");
		
		}
	}

	
	public boolean hitDet (){
		//System.out.println("Hit Detection Starting");
		boolean hitDet = false;
		//System.out.println("Inilizing Points array");
		Point[] points = shellRadiusPoints();
		//System.out.println("Array Inilized");
		if(calcHitDual(tankPoly,points) == true){
			hitDet = true;
			System.out.println("Tank Hit");
		}
		if(calcHit(worldPoly,points) == true){
			hitDet = true;
			System.out.println("World Hit");
		}
		if(shell[0] < -16 || shell[0] > 700){
			hitDet = true;
			System.out.println("Out of Bounds");
		}
		//System.out.println("No hit");
		return hitDet;
	}
	
	public boolean calcHit (Polygon[] currentPoly, Point[] points){
		boolean hit = false;
		//System.out.println("Calc Start");
		for(int i = 0; i < currentPoly.length; i++){
			//System.out.println("Checking next poly");
			for(int j = 0; j < points.length; j++){
				//System.out.println("About to check point: " + points[j]);
				if (currentPoly[i].contains(points[j])){
					hit = true;
					System.out.println("Set hit to true"+" Point:"+ points[j]);
					break;
				}
			}
		};
		//System.out.println("Calc Run");
		return hit;
	}
	public boolean calcHitDual (Polygon[][] currentPoly, Point[] points){
		boolean hit = false;
		//System.out.println("Calc Start");
		for(int i = 0; i < currentPoly.length; i++){
			//System.out.println("Checking next poly");
			for(int o = 0; o < 3; o++){
				for(int j = 0; j < points.length; j++){
				//System.out.println("About to check point: " + points[j]);
					if (currentPoly[i][o].contains(points[j])){
						hit = true;
						System.out.println("Set hit to true"+" Point:"+ points[j]);
						break;
					}
				}
			}
		};
		//System.out.println("Calc Run");
		return hit;
	}
	//Maybe Fix this make it more smooth
	public void createWorld(){
		int x[] = new int[4];
		int y[] = new int[4];
		world[0][0][0] = 0;
		world[0][0][1] = (int)((Math.random()*100)+300);
		world[0][1][0] = 0;
		world[0][1][1] = 450;
		world[0][2][0] = 100;
		world[0][2][1] = 450;
		world[0][3][0] = 100;
		world[0][3][1] = (int)((Math.random()*100)+300);
		//world[i][0] = (700/5)*(i+1);
		//world[i][1] = (int)((Math.random()*100)+350);	
		for(int i = 1; i < world.length;i++ ){
			world[i][0][0] = (700/7)*(i);
			world[i][0][1] = world[i-1][3][1];
			world[i][1][0] = (700/7)*(i);
			world[i][1][1] = 450;
			world[i][2][0] = (700/7)*(i+1);
			world[i][2][1] = 450;
			world[i][3][0] = (700/7)*(i+1);
			world[i][3][1] = (int)((Math.random()*100)+300);
		}
		
		for(int j = 0; j < 7; j++){
			for(int i = 0; i < 4;i++){
				x[i] = world[j][i][0];
				y[i] = world[j][i][1];	
			}
			worldPoly[j] = new Polygon(x,y,4);
		}
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
			//JOptionPane.showMessageDialog(null,""+secSlopes[i][0]);
			
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
			tankInfo[i][2][1][1] = tankInfo[i][0][0][1] + (int)(Math.round(secSlopes[sector][0]*16));
			//Creating points up vertical
			tankInfo[i][2][0][0] = xPos(270,i,1,sector,10,2);
			tankInfo[i][2][0][1] = yPos(270,i,1,sector,10,2);
			startMod = 2;
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
			tankInfo[i][2][1][1] = tankInfo[i][0][0][1] + (int)(Math.round(secSlopes[sector][0]*16));
			//Creating points up vertical
			tankInfo[i][2][0][0] = xPos(90,i,1,sector,10,2);
			tankInfo[i][2][0][1] = yPos(90,i,1,sector,10,2);
			startMod = 2;
			//Generates origin in which the print is than moved
			tankInfo[i][2][2][0] = tankInfo[i][2][1][0] + (int)(startMod); 
			tankInfo[i][2][2][1] = tankInfo[i][2][1][1] + (int)(Math.round(secSlopes[sector][0]*startMod));
			//Moves generated point
			tankInfo[i][2][2][0] = xPos(angle2,i,2,sector,4,2); 
			tankInfo[i][2][2][1] = yPos(angle2,i,2,sector,4,2);
			
			tankInfo[i][2][3][0] = xPos(90,i,2,sector,10,2);
			tankInfo[i][2][3][1] = yPos(90,i,2,sector,10,2);
		
		}
		
		
		//tankInfo[1][0][3][0] = world[sector][3][0] + (int)(startMod); 
		//tankInfo[i][0][3][1] = world[sector][3][1] + (int)(Math.round(secSlopes[sector][0]*startMod));
		//angle1 = angleCalcP1(i,3,sector,tankH);
		
		
		/*
		tankTopSlope = (((double)(tankInfo[i][0][3][1]-(double)(tankInfo[i][0][0][1]))/((double)(tankInfo[i][0][3][0])-(double)(tankInfo[i][0][0][0]))));
		angle1 = Math.round(Math.toDegrees(Math.atan2((tankTopSlope),1)));
		tankInfo[i][1][1][0] = xPos(angle2,i,0,sector,7,0);
		tankInfo[i][1][1][1] = yPos(angle2,i,0,sector,7,0);
		
		double angle3 = angleCalcP3(i,1,sector,30,1,0);
		
		tankInfo[i][1][0][0] = xPos(angle3,i,1,sector,10,1);
		tankInfo[i][1][0][1] = yPos(angle3,i,1,sector,10,1);
		
		
		angle2 = angleCalcP2(i,1,sector,30,1);
		tankInfo[i][1][2][0] = xPos(angle2,i,1,sector,30,1);
		tankInfo[i][1][2][1] = yPos(angle2,i,1,sector,30,1);
		angle3 = angleCalcP3(i,2,sector,30,1,1);
		tankInfo[i][1][3][0] = xPos(angle3,i,2,sector,10,1);
		tankInfo[i][1][3][1] = yPos(angle3,i,2,sector,10,1);
	*/
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
	//Recalucates tank arm based on imput angle
	public void tankArmUpdater(int tank, int sector){
		double startMod = 25;
		double angle;
		double angle2 = angleCalcP2(tank,2,sector,35,0);
		if(secSlopes[sector][0]< 0){
			angle = fireInfo[0]+180;
			//This method takes the angle, the current tank, the point being rotated around , the slope, the length of the line, and the current shape being worked on
			//Creates the point in the bottom left corner of the tank cannon
			tankInfo[tank][2][1][0] = tankInfo[tank][0][0][0] + (int)(16);
			tankInfo[tank][2][1][1] = tankInfo[tank][0][0][1] + (int)(Math.round(secSlopes[sector][0]*16));
			//Creating points up vertical
			tankInfo[tank][2][0][0] = xPos(angle,tank,1,sector,10,2);
			tankInfo[tank][2][0][1] = yPos(angle,tank,1,sector,10,2);
			startMod = 2;
			//Generates origin in which the print is than moved
			tankInfo[tank][2][2][0] = tankInfo[tank][2][1][0] + (int)(startMod); 
			tankInfo[tank][2][2][1] = tankInfo[tank][2][1][1] + (int)(Math.round(secSlopes[sector][0]*startMod));
			//Moves generated point
			tankInfo[tank][2][2][0] = xPos(angle2,tank,2,sector,4,2); 
			tankInfo[tank][2][2][1] = yPos(angle2,tank,2,sector,4,2);
			
			tankInfo[tank][2][3][0] = xPos(angle,tank,2,sector,10,2);
			tankInfo[tank][2][3][1] = yPos(angle,tank,2,sector,10,2);
			
		}else{
			angle = fireInfo[0];
			//This method takes the angle, the current tank, the point being rotated around , the slope, the length of the line, and the current shape being worked on
			//Creates the point in the bottom left corner of the tank cannon
			tankInfo[tank][2][1][0] = tankInfo[tank][0][0][0] + (int)(16);
			tankInfo[tank][2][1][1] = tankInfo[tank][0][0][1] + (int)(Math.round(secSlopes[sector][0]*16));
			//Creating points up vertical
			tankInfo[tank][2][0][0] = xPos(angle,tank,1,sector,10,2);
			tankInfo[tank][2][0][1] = yPos(angle,tank,1,sector,10,2);
			startMod = 2;
			//Generates origin in which the print is than moved
			tankInfo[tank][2][2][0] = tankInfo[tank][2][1][0] + (int)(startMod); 
			tankInfo[tank][2][2][1] = tankInfo[tank][2][1][1] + (int)(Math.round(secSlopes[sector][0]*startMod));
			//Moves generated point
			tankInfo[tank][2][2][0] = xPos(angle2,tank,2,sector,4,2); 
			tankInfo[tank][2][2][1] = yPos(angle2,tank,2,sector,4,2);
			
			tankInfo[tank][2][3][0] = xPos(angle,tank,2,sector,10,2);
			tankInfo[tank][2][3][1] = yPos(angle,tank,2,sector,10,2);
		
		}
		int x[] = new int[4];
		int y[] = new int[4];
		for(int i = 0; i < 4;i++){
			x[i] = tankInfo[tank][2][i][0];
			y[i] = tankInfo[tank][2][i][1];	
		}
		tankPoly[tank][2] = new Polygon(x,y,4);
		display.drawTest(tankPoly);
	}
	
}