
import java.awt.*;
import javax.swing.*;
public class DrawGame extends JPanel {
	//Prevents drawing of certain objects from drawing until the information is available
	int trust = 0;
	int trust1 = 0;
	//Creates an array of the polygons that make up the world
	Polygon[][] tankPoly;
	Polygon[] worldPoly;
	//Information about shell and where to draw shell
	int[] shell;
	public void paint(Graphics g){
		super.paint( g );
		//System.out.println("Paint Ran");
		//Set color of background
		g.setColor(Color.CYAN);
		//Draw background
		g.fillRect(0,0,700,500);
		
		/*
		for (int i = 0; i < tanksInfo.length; i++){
			g.fillRect(tanksInfo[i][0][0],tanksInfo[i][0][1],tanksInfo[i][0][2],tanksInfo[i][0][3]);	
		}
		*/
		//Set the color to draw the background
		g.setColor(Color.WHITE);
		
		///*
		//Loop through all of the polygons and draw them to the screen
		for(int j = 0; j < worldPoly.length; j++){
			g.fillPolygon(worldPoly[j]);
		}
		//*/
		/*
		g.setColor(Color.PINK);
		g.fillPolygon(worldPoly[0]);
		
		g.setColor(Color.BLUE);
		g.fillPolygon(worldPoly[1]);
		
		g.setColor(Color.PINK);
		g.fillPolygon(worldPoly[2]);
		
		g.setColor(Color.YELLOW);
		g.fillPolygon(worldPoly[3]);
		
		g.setColor(Color.ORANGE);
		g.fillPolygon(worldPoly[4]);
		
		g.setColor(Color.GREEN);
		g.fillPolygon(worldPoly[5]);
		
		g.setColor(Color.WHITE);
		g.fillPolygon(worldPoly[6]);
		*/
		//Create a green color with specific RGB
		Color greenColor = new Color(34, 139, 34);
		//Set the tank to draw with these colors
		g.setColor(greenColor);
		//Check if its time to draw tank
		if (trust == 1){
			//Draw main body of tank
			for(int i = 0; i < 2; i++){
				
					g.fillPolygon(tankPoly[i][0]);
				
			}
			//Change color to black
			g.setColor(Color.BLACK);
			//Draw tank treads
			for(int i = 0; i < 2; i++){
				
				g.fillPolygon(tankPoly[i][1]);
			
			}
			g.setColor(greenColor);
			//Draw tank arm
			for(int i = 0; i < 2; i++){
				
				g.fillPolygon(tankPoly[i][2]);
			
			}
		
		}
		g.setColor(Color.BLACK);
		//Check if its time to draw tank shell. if it is draw shell
		if(trust1 == 1){
			g.fillOval(shell[0], shell[1], shell[2], shell[3]);
		}
	}
	
	//Receives input of tank polygons
	public void drawTank(Polygon[][] tankPoly)
	{
		//Sets the classes tankPoly array to be the input received
	     this.tankPoly = tankPoly; 
	     //Tells the drawing canvas to paint the tank
	     trust = 1;
	     //Repaint canvas
	    
	     this.repaint();
	}
	//Receives polygons that make up the world
	public void drawWorld(Polygon[] worldPoly)
	{
		//Set the classes world poly to be the input poly array
	     this.worldPoly = worldPoly; 
	     //Repaint the canvas
	     
	     this.repaint();
	}
	
	//Draw the tank shell 	
	public void drawShell (int[] shell){
		//Set the classes shell array equal to the input array
		this.shell = shell;
		//Tells the canvas to draw the tank shell
		trust1 = 1;
		//Repaint the canvas
		//System.out.println("Trying to paint");
		this.repaint();
		
	}
	
}
