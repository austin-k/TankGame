
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class mainGame extends JFrame  {

	/**
	 * @param args
	 */
	
	Container frame;
	private JPanel gameInfo;
	private DrawGame display;
	//Tank health
	int [] tankHealth = new int [2];
	//If a tank has fired and which tank
	int [] tankFired = new int [2];
	//Includes X in array pos 0 and Y int array pos 1
	int [] tankShellPos = new int [2];
	//Power and fire angle
	int [] fireInfo = new int [2];
	//Location of tank
	int [][][] tankInfo= new int[2][4][2];;
	//World Map
	int[][][] world = new int [7][4][2];
	//Sector Slopes and reciporcal
	double[][] secSlopes = new double[7][2];
	Polygon[] tankPoly = new Polygon[2];
	Polygon[] worldPoly = new Polygon[7];
	int shellDi = 20;
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
		// TODO Auto-generated method stub
			
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
				
			fireInfo[0] = Integer.parseInt(JOptionPane.showInputDialog("Pleace enter the fire angle"));
			fireInfo[1] = (Integer.parseInt(JOptionPane.showInputDialog("Pleace enter the fire power"))/-1);
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
		tankShellPos[0] = 0;
		tankShellPos[1] = 0;
	}
	public void shellCalc(){
		double xSpeed = fireInfo[1]*Math.cos(Math.toRadians(fireInfo[0]));
		double ySpeed = fireInfo[1]*Math.sin(Math.toRadians(fireInfo[0]));
		final double gravity = 0.98;
		double time = 0.0;
		try{
			do{
				
				break;
			}while(false == hitDet());
		
		}
		catch(Exception e){
			System.out.println("Hit Detection failure");
		}
	}

	
	public boolean hitDet (){
		boolean hitDet = false;
		Point[] points = shellRadiusPoints();
		if(calcHit(tankPoly,points) == true){
			hitDet = true;
		}
		if(calcHit(worldPoly,points) == true){
			hitDet = true;
		}
		return hitDet;
	}
	public boolean calcHit (Polygon[] currentPoly, Point[] points){
		boolean hit = false;
		for(int i = 0; i < currentPoly.length; i++){
			for(int j = 0; j < points.length; i++){
				if (currentPoly[i].contains(points[j])){
					hit = true;
				}
			}
		}
		return hit;
	}
	//Fix this make it more smooth
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
		for(int i = 0; i < tankInfo.length;i++ ){
			double startMod = 25;
			if(i == 0){
				sector = 1;
				tankInfo[i][1][0] = world[sector][0][0] + (int)(startMod); 
				tankInfo[i][1][1] = world[sector][0][1] + (int)(Math.round(secSlopes[sector][0]*startMod));
				double angle1 = angleCalcP1(i,1,sector,tankH);
				tankInfo[i][0][0] = xPos(angle1,i,1,sector,tankH);
				tankInfo[i][0][1] = yPos(angle1,i,1,sector,tankH);
				startMod = 35;
				
				tankInfo[i][2][0] = (((700/7)*sector)+(int)startMod); 
				tankInfo[i][2][1] = world[sector][0][1] + (int)(Math.round(secSlopes[sector][0]*startMod));
				double angle2 = angleCalcP2(i,2,sector,tankW);
				tankInfo[i][2][0] = xPos(angle2,i,2,sector,tankW); 
				tankInfo[i][2][1] = yPos(angle2,i,2,sector,tankW);
				
				tankInfo[1][3][0] = world[sector][3][0] + (int)(startMod); 
				tankInfo[i][3][1] = world[sector][3][1] + (int)(Math.round(secSlopes[sector][0]*startMod));
				//angle1 = angleCalcP1(i,3,sector,tankH);
				tankInfo[i][3][0] = xPos(angle1,i,2,sector,tankH);
				tankInfo[i][3][1] = yPos(angle1,i,2,sector,tankH);
			}
			else if(i == 1){
				sector = 5;
				tankInfo[i][1][0] = world[sector][0][0] + (int)(startMod); 
				tankInfo[i][1][1] = world[sector][0][1] + (int)(Math.round(secSlopes[sector][0]*startMod));
				double angle1 = angleCalcP1(i,1,sector,tankH);
				tankInfo[i][0][0] = xPos(angle1,i,1,sector,tankH);
				tankInfo[i][0][1] = yPos(angle1,i,1,sector,tankH);
				startMod = 35;
				
				tankInfo[i][2][0] = (((700/7)*sector)+(int)startMod); 
				tankInfo[i][2][1] = world[sector][0][1] + (int)(Math.round(secSlopes[sector][0]*startMod));
				double angle2 = angleCalcP2(i,2,sector,tankW);
				tankInfo[i][2][0] = xPos(angle2,i,2,sector,tankW); 
				tankInfo[i][2][1] = yPos(angle2,i,2,sector,tankW);
				
				tankInfo[1][3][0] = world[sector][3][0] + (int)(startMod); 
				tankInfo[i][3][1] = world[sector][3][1] + (int)(Math.round(secSlopes[sector][0]*startMod));
				//angle1 = angleCalcP1(i,3,sector,tankH);
				tankInfo[i][3][0] = xPos(angle1,i,2,sector,tankH);
				tankInfo[i][3][1] = yPos(angle1,i,2,sector,tankH);
			}
			
		}
		for(int j = 0; j < 2; j++){
			for(int i = 0; i < 4;i++){
				x[i] = tankInfo[j][i][0];
				y[i] = tankInfo[j][i][1];	
			}
			tankPoly[j] = new Polygon(x,y,4);
		}
	System.out.println("x: " + tankInfo[0][0][0]+" y: "+ tankInfo[0][0][1]);
	
	}
	public double angleCalcP1 (int i,int y,int slope,double length){
		double angle = Math.round(Math.toDegrees(Math.atan2(((tankInfo[i][y][0]-((700/7)*slope))*secSlopes[slope][0]),length)));
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
	public double angleCalcP2 (int i,int y,int slope,double length){
		double angle = Math.round(Math.toDegrees(Math.atan2(((tankInfo[i][y][0]-((700/7)*slope))*secSlopes[slope][0]),length)));
		System.out.println("Angle2: "+ angle);
		System.out.println("tankInfo[i][y][0] "+ tankInfo[i][y][0]);
		if(secSlopes[slope][0] < 0){
			angle = (angle+360);
		}
		else{
			
			angle = angle+180;
		}
		System.out.println("Angle2: "+ angle);
		return angle;
	}
	public int xPos (double angle,int i,int j,int slope,double length){
		int xPos = 0;
		xPos = (int)(Math.round(length*(Math.cos(Math.toRadians(angle)))));
		int hShift = 0;
		System.out.println("x: " +xPos+" ");
		if(secSlopes[slope][0] < 0){
			hShift = (tankInfo[i][j][0]+xPos);
		}
		else{
			hShift = (tankInfo[i][j][0]-xPos);
		}
		xPos = hShift;
		return xPos;
	}
	public int yPos (double angle,int i,int j,int slope,double length){
		int yPos = 0;
		yPos = (int)(Math.round(length*(Math.sin(Math.toRadians(angle)))));
		int vShift = 0;
		System.out.println("y: " +yPos+"");
		if(secSlopes[slope][0] < 0){
			vShift = (tankInfo[i][j][1] + yPos);
		}
		else{
			vShift = (tankInfo[i][j][1] - yPos);
		}
		yPos = vShift;
		return yPos;
	}
	public Point[] shellRadiusPoints (){
		Point[] points = new Point[90];
		double rad = (shellDi/2);
		for(int i = 0; i < points.length; i++){
			double curAngle = (((double)360/(double)4) + ((double)360/(double)4)*(double)i);
			int x,y;
			int vShift, hShift;
			x = (int)(Math.round(rad*(Math.cos(Math.toRadians(curAngle)))));
			y = (int)(Math.round(rad*(Math.sin(Math.toRadians(curAngle)))));
			if(x < 0 && y < 0){
				hShift = shell[0] + x;
				vShift = shell[1] + y;
			}
			else if(x > -1 && y < 0){
				hShift = shell[0] - x;
				vShift = shell[1] + y;
			}
			else if(x < 0 && y > -1){
				hShift = shell[0] + x;
				vShift = shell[1] - y;
			}
			else if(x > -1 && y > -1){
				hShift = shell[0] - x;
				vShift = shell[1] - y;
			}
			else{
				hShift = x;
				vShift = y;
			}
			x = hShift + 10;
			y = vShift + 10;
			points[i] = new Point (x,y);
		}
		return points;
	}
}