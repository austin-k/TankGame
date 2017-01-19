
import java.awt.*;
import javax.swing.*;
public class DrawGame extends JPanel {
	int[][][] tanksInfo;
	int[][][] world;
	int trust = 0;
	int trust1 = 0;
	Polygon[][] tankPoly;
	Polygon[] worldPoly;
	int[] shell;
	public void paint(Graphics g){
		super.paint( g );
		g.setColor(Color.CYAN);
		g.fillRect(0,0,700,500);
		g.setColor(Color.RED);
		
		/*
		for (int i = 0; i < tanksInfo.length; i++){
			g.fillRect(tanksInfo[i][0][0],tanksInfo[i][0][1],tanksInfo[i][0][2],tanksInfo[i][0][3]);	
		}
		*/
		g.setColor(Color.WHITE);
		
		///*
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
		Color greenColor = new Color(34, 139, 34);
		g.setColor(greenColor);
		if (trust == 1){
			//g.drawLine(tanksInfo[0][1][0],tanksInfo[0][1][1],tanksInfo[0][0][0],tanksInfo[0][0][1]);
			for(int i = 0; i < 2; i++){
				
					g.fillPolygon(tankPoly[i][0]);
				
			}
			g.setColor(Color.BLACK);
			for(int i = 0; i < 2; i++){
				
				g.fillPolygon(tankPoly[i][1]);
			
			}
			g.setColor(greenColor);
			for(int i = 0; i < 2; i++){
				
				g.fillPolygon(tankPoly[i][2]);
			
			}
		
		}
		if(trust1 == 1){
			g.fillOval(shell[0], shell[1], shell[2], shell[3]);
		}
	}
	

	public void drawTest(Polygon[][] tankPoly)
	{
	     this.tankPoly = tankPoly; 
	     trust = 1;
	     repaint();
	}
	public void drawWorld(Polygon[] worldPoly)
	{
	     this.worldPoly = worldPoly; 
	     repaint();
	}
	public void drawTanks (int tanksInfo[][][]){
		this.tanksInfo = tanksInfo; 
	    //trust = 1;
	    repaint();
	}
		
	public void drawShell (int[] shell){
		this.shell = shell;
		trust1 = 1;
		repaint();
	}
	
}
