
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
public class homePage  extends JFrame implements ActionListener{
	// Creates a container for the frame
	Container frame;
	CardLayout mainLayout = new CardLayout();
	int accountNumber;
	private JPanel[] panels = new JPanel[3];
	private JLabel title = new JLabel("TANK MANIA",JLabel.CENTER);
	//Holds Username and password labels
	private JLabel[] titles = new JLabel[4];
	private JLabel[] info = new JLabel[10];
	private JButton[] buttons = new JButton[14];
	private JTextField[] fields = new JTextField[4];
	private ArrayList[] accountInfo = new ArrayList[6];
	private JPanel mainPanel = new JPanel();
	private JPanel titlePn = new JPanel();
	public homePage(){
		super("Home Page");
		frame = getContentPane();
		title.setBackground(Color.ORANGE);
		title.setFont(new Font("Serif", Font.BOLD, 90));
		titlePn.setLayout(new GridLayout(1,1));
		titlePn.setSize(800,200);
		titlePn.setLocation(0,0);
		mainPanel.setSize(800,410);
		mainPanel.setLocation(0,200);
		mainPanel.setLayout(mainLayout);
		frame.setBackground(Color.LIGHT_GRAY);
		panels[0] = new JPanel();
		panels[1] = new JPanel();
		panels[2] = new JPanel();
		
		panels[0].setLayout(new GridLayout(8,1));
		panels[0].setSize(800,350);
		
		panels[1].setLayout(new GridLayout(6,1));
		panels[1].setSize(800,350);
		
		panels[2].setLayout(new GridLayout(9,2));
		panels[2].setSize(800,350);
		
		titles[0] = new JLabel("Username:");
		titles[1] = new JLabel("Password:");
		titles[0].setFont(new Font("Serif", Font.BOLD, 30));
		titles[1].setFont(new Font("Serif", Font.BOLD, 30));
		titles[2] = new JLabel("Username:");
		titles[3] = new JLabel("Password:");
		titles[2].setFont(new Font("Serif", Font.BOLD, 30));
		titles[3].setFont(new Font("Serif", Font.BOLD, 30));
		
		info[0] = new JLabel("Welcome: ");
		info[0].setFont(new Font("Serif", Font.BOLD, 25));
		
		info[1] = new JLabel("");
		info[1].setFont(new Font("Serif", Font.BOLD, 25));
		
		info[2] = new JLabel("Total Games Played: ");
		info[2].setFont(new Font("Serif", Font.BOLD, 25));
		
		info[3] = new JLabel("");
		info[3].setFont(new Font("Serif", Font.BOLD, 25));
		
		info[4] = new JLabel("Total Wins: ");
		info[4].setFont(new Font("Serif", Font.BOLD, 25));
		
		info[5] = new JLabel("");
		info[5].setFont(new Font("Serif", Font.BOLD, 25));
		
		info[6] = new JLabel("Total Losses: ");
		info[6].setFont(new Font("Serif", Font.BOLD, 25));
		
		info[7] = new JLabel("");
		info[7].setFont(new Font("Serif", Font.BOLD, 25));
		
		info[8] = new JLabel("Win Loss Ratio: ");
		info[8].setFont(new Font("Serif", Font.BOLD, 25));
		
		info[9] = new JLabel("");
		info[9].setFont(new Font("Serif", Font.BOLD, 25));
		
		
		buttons[0] = new JButton("Login");
		buttons[1] = new JButton("Regisiter");
		buttons[2] = new JButton("Instructions");
		buttons[3] = new JButton("Quit");
		
		buttons[4] = new JButton("Regisiter");
		buttons[5] = new JButton("Back");
		
		buttons[6] = new JButton("A.I. Easy");
		buttons[7] = new JButton("A.I. Medium");
		buttons[8] = new JButton("A.I. GOD Mode");
		buttons[9] = new JButton("Local Mulitplayer");
		buttons[10] = new JButton("Search Player");
		buttons[11] = new JButton("HighScore Board");
		buttons[12] = new JButton("Logout");
		buttons[13] = new JButton("Quit");
		
		buttons[0].addActionListener(this);
		buttons[1].addActionListener(this);
		buttons[2].addActionListener(this);
		buttons[3].addActionListener(this);
		buttons[4].addActionListener(this);
		buttons[5].addActionListener(this);
		//Buttons for window after logging in
		buttons[6].addActionListener(this);
		buttons[7].addActionListener(this);
		buttons[8].addActionListener(this);
		buttons[9].addActionListener(this);
		buttons[10].addActionListener(this);
		buttons[11].addActionListener(this);
		buttons[12].addActionListener(this);
		buttons[13].addActionListener(this);
		
				
		fields[0] = new JTextField(25);
		fields[1] = new JTextField(25);
		fields[2] = new JTextField(25);
		fields[3] = new JTextField(25);
		
		
		
		
		panels[0].add(titles[0]);
		panels[0].add(fields[0]);
		panels[0].add(titles[1]);
		panels[0].add(fields[1]);
		
		panels[0].add(buttons[0]);
		panels[0].add(buttons[1]);
		panels[0].add(buttons[2]);
		panels[0].add(buttons[3]);
		
		panels[1].add(titles[2]);
		panels[1].add(fields[2]);
		panels[1].add(titles[3]);
		panels[1].add(fields[3]);
		panels[1].add(buttons[4]);
		panels[1].add(buttons[5]);
		
		panels[2].add(info[0]);
		panels[2].add(info[1]);
		panels[2].add(info[2]);
		panels[2].add(info[3]);
		panels[2].add(info[4]);
		panels[2].add(info[5]);
		panels[2].add(info[6]);
		panels[2].add(info[7]);
		panels[2].add(info[8]);
		panels[2].add(info[9]);
		panels[2].add(buttons[6]);
		panels[2].add(buttons[7]);
		panels[2].add(buttons[8]);
		panels[2].add(buttons[9]);
		panels[2].add(buttons[10]);
		panels[2].add(buttons[11]);
		panels[2].add(buttons[12]);
		panels[2].add(buttons[13]);
		
		mainPanel.add(panels[0],"home");
		mainPanel.add(panels[1],"regisiter");
		mainPanel.add(panels[2],"playerHome");
		titlePn.add(title);
		frame.add(mainPanel);
		frame.add(titlePn);
		frame.setLayout(null);
		setSize(800, 640);
		setResizable(false);
		setVisible(true);
		loadAccounts();
	}
	
	public static void main(String[] args) {
		new homePage();
		
		//Launch Game
		//new mainGame(1);
	}
	public void actionPerformed (ActionEvent e){
		
		if(e.getSource() == buttons[0]){
			String uN, uP;
			uN = fields[0].getText();
			uP = fields[1].getText();
			if(!uN.equals("") && !uN.equals(" ") && !uP.equals("") && !uP.equals(" ")){
				if(uNameExists(fields[0].getText(),1)){
					String check = fields[1].getText();
					if(check.equals(accountInfo[1].get(accountNumber))){
						setLabels();
						mainLayout.show(mainPanel,"playerHome");
						fields[0].setText("");
						fields[1].setText("");
					}else{
						JOptionPane.showMessageDialog(null,"User password is incorrect");
						fields[1].setText("");
					}
				}else{
					JOptionPane.showMessageDialog(null,"User does not exist");
				}
			}else{
				JOptionPane.showMessageDialog(null,"Please enter fill all fields");
			}
		}
		else if(e.getSource() == buttons[1]){
			mainLayout.show(mainPanel,"regisiter");
		}
		else if(e.getSource() == buttons[2]){
			//Instructions
		}
		else if(e.getSource() == buttons[3]){
			System.exit(0);
		}
		else if(e.getSource() == buttons[4]){
			String uNameCheck;
			uNameCheck = fields[2].getText();
			String uN = fields[2].getText();
			String uP = fields[3].getText();
			//If the username is not found allow it to be add to the file
			if(!uN.equals("") && !uN.equals(" ") && !uP.equals("") && !uP.equals(" ")){
				if(uNameExists(uNameCheck,0) == false){
					accountInfo[0].add(uNameCheck);
					accountInfo[1].add(fields[3].getText());
					accountInfo[2].add(accountInfo[0].size());
					accountInfo[3].add(0);
					accountInfo[4].add(0);
					accountInfo[5].add(0);
					updateAccountFile();
					fields[2].setText("");
					fields[3].setText("");
					mainLayout.show(mainPanel,"home");
				}
				else{
					fields[2].setText("");
					JOptionPane.showMessageDialog(null, "Username Taken, try a new name");
				}
			}else{
				JOptionPane.showMessageDialog(null,"Please enter fill all fields");
			}
		}
		else if(e.getSource() == buttons[5]){
			mainLayout.show(mainPanel,"home");
		}
		else if(e.getSource() == buttons[6]){
			
		}
		else if(e.getSource() == buttons[7]){
			
		}
		else if(e.getSource() == buttons[8]){
			
		}
		else if(e.getSource() == buttons[9]){
			
		}
		else if(e.getSource() == buttons[10]){
			
		}
		else if(e.getSource() == buttons[11]){
			
		}
		else if(e.getSource() == buttons[12]){
			updateAccountFile();
			mainLayout.show(mainPanel,"home");
		}
		else if(e.getSource() == buttons[13]){
			updateAccountFile();
			System.exit(0);
		}
	}
	public void loadAccounts(){
		File check = new File(System.getProperty("user.dir")+"\\accounts.txt");
		if(check.exists() == true){
			try{
				//Set up array lists
				//Username
				accountInfo[0] = new ArrayList<String>();
				//Password
				accountInfo[1] = new ArrayList<String>();
				//Account Number
				accountInfo[2] = new ArrayList<Integer>();
				//Games Played
				accountInfo[3] = new ArrayList<Integer>();
				//Games Won
				accountInfo[4] = new ArrayList<Integer>();
				//Games Lost
				accountInfo[5] = new ArrayList<Integer>();
				String[]bits;
				BufferedReader fr = new BufferedReader(new FileReader(check));
				String line;
				do
				{
					//Load up array lists
					line = fr.readLine ();
					if(line != null){
						bits = line.split(",");
						accountInfo[0].add((String)bits[0]);
						accountInfo[1].add((String)bits[1]);
						accountInfo[2].add(Integer.parseInt(bits[2]));
						accountInfo[3].add(Integer.parseInt(bits[3]));
						accountInfo[4].add(Integer.parseInt(bits[4]));
						accountInfo[5].add(Integer.parseInt(bits[5]));
					}
				}
	            while (line != null);
				fr.close();
				
				
			}catch(IOException j){
				System.out.println("Regisitration Failure");
			}
		}
		else{
			try{
				//Create the file if it does not exist and set up the accountInfo array lists
				PrintWriter pw = new PrintWriter(new FileWriter(check));
				pw.close();
				accountInfo[0] = new ArrayList<String>();
				//Password
				accountInfo[1] = new ArrayList<String>();
				//Account Number
				accountInfo[2] = new ArrayList<Integer>();
				//Games Played
				accountInfo[3] = new ArrayList<Integer>();
				//Games Won
				accountInfo[4] = new ArrayList<Integer>();
				//Games Lost
				accountInfo[5] = new ArrayList<Integer>();
			}catch(IOException j){
				System.out.println("Regisitration Failure");
			}
		}
	}
	//Check if user name exists
	public boolean uNameExists(String name, int modifier){
		boolean exists = false;
		//Loop though and check if any user names are the same if so return true and not set account number
		if(modifier == 0){
			for(int i = accountInfo[0].size()-1; i >-1; i--){
				if(name.equals(accountInfo[0].get(i))){
					exists = true;
					break;
				}
			}
		}
		else{
			for(int i = accountInfo[0].size()-1; i >-1; i--){
				if(name.equals(accountInfo[0].get(i))){
					accountNumber = i;
					exists = true;
					break;
				}
			}
		}
		return exists;
	}
	public void updateAccountFile(){
		
		//get path to file
		File check = new File(System.getProperty("user.dir")+"\\accounts.txt");
		try{
			//create file to path
			PrintWriter pw = new PrintWriter (new FileWriter(check));
			//Dump arraylists into file
				for(int i = 0; i < accountInfo[0].size();i++){
					pw.println(accountInfo[0].get(i) + "," + accountInfo[1].get(i) +  "," + accountInfo[2].get(i) + "," + accountInfo[3].get(i) + "," + accountInfo[4].get(i) + "," + accountInfo[5].get(i));
				}
			pw.close();
	
		}catch(IOException j){
			
		}
	}
	public void setLabels (){
		//Set username
		info[1].setText((String)(accountInfo[0].get(accountNumber)));
		//Set total games played
		info[3].setText(Integer.toString(((int)accountInfo[3].get(accountNumber))));
		//Set total wins 
		info[5].setText(Integer.toString((int)accountInfo[4].get(accountNumber)));
		//Set total losses
		info[7].setText(Integer.toString((int)accountInfo[5].get(accountNumber)));
		if((int)(accountInfo[4].get(accountNumber)) == 0){
			info[9].setText(Integer.toString((int)accountInfo[3].get(accountNumber)));
		}else{
			info[9].setText(Long.toString(Math.round(((double)(accountInfo[3].get(accountNumber))/(double)(accountInfo[5].get(accountNumber))))));
		}
	}
}
