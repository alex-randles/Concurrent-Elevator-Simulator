
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.*;
import javax.swing.JLabel;
import javax.swing.OverlayLayout;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

public class elevatorGUI {

	private JLabel[] elevatorFloors;
	private Thread pause;
	private Map<Dimension, JLabel> people;
	private JFrame frame;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gridLayout window = new gridLayout();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public elevatorGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("static-access")
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 1, 1,1,1,1,1,1,1,1,1,1,1,1,1};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[] {0.0, 0.2,0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
		gridBagLayout.rowWeights = new double[]{0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JLabel elevatorFloor10 = new JLabel(new ImageIcon("/elevator.jpg"));
		//frame.add(elevatorFloor10);
		GridBagConstraints gbc_elevatorFloor10 = new GridBagConstraints();
		gbc_elevatorFloor10.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor10.gridx = 1;
		gbc_elevatorFloor10.gridy = 0;
		//JLabel knight=new JLabel(new ImageIcon("knight.jpg"));
		//background1.add(knight);
		//frame.add(background1);
		Image img = new ImageIcon(this.getClass().getResource("/elevator.jpg")).getImage();
		//elevatorFloor10.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor10, gbc_elevatorFloor10);
		
		/*JLabel lblNewLabel_1 = new JLabel("New label");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		frame.getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);*/
		
		int floor = 10;
		for(int i=1;i<=10;i++) {
			JLabel tmpLabel = new JLabel("" + floor);
			GridBagConstraints tmp = new GridBagConstraints();
			tmp.insets = new Insets(0, 0, 5, 5);
			tmp.gridx = 0;
			tmp.gridy = i;
			frame.getContentPane().add(tmpLabel, tmp);
			floor--;

		}
		

		
		
		
		JLabel elevatorFloor9 = new JLabel(new ImageIcon("/home/alex/elevator/images/elevator.jpg"));
		GridBagConstraints gbc_elevatorFloor9 = new GridBagConstraints();
		gbc_elevatorFloor9.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor9.gridx = 1;
		gbc_elevatorFloor9.gridy = 1;
		//gbc_lblNewLabel_1.gridheight = 5;
		//elevatorFloor9.setIcon(new ImageIcon("/home/alex/elevator/images/elevator.jpg"));
		frame.getContentPane().add(elevatorFloor9, gbc_elevatorFloor9);
		
		


		//JLabel elevatorFloor8 = new JLabel(new ImageIcon("/home/alex/elevator/images/elevator.jpg"));
	     JLabel elevatorFloor8 = new JLabel("");
		//elevatorFloor8.setLayout(new OverlayLayout(elevatorFloor8));
	     elevatorFloor8.setLayout(new BorderLayout());
		elevatorFloor8.setIcon(new ImageIcon(img));
		Image person = new ImageIcon(this.getClass().getResource("/greenPersonSmall.png")).getImage();
		GridBagConstraints gbc_elevatorFloor8 = new GridBagConstraints();
		gbc_elevatorFloor8.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor8.gridx = 1;
		gbc_elevatorFloor8.gridy = 2;
		//elevatorFloor8.setIcon(new ImageIcon(person));
		JLabel over = new JLabel("");
        over.setLocation(200,300);

		over.setIcon(new ImageIcon(person));
        over.setVisible(true);       
        over.setBounds(50,50,100,100);

		elevatorFloor8.add(over,BorderLayout.SOUTH);

		//elevatorFloor8.add(over,2,0);

		frame.getContentPane().add(elevatorFloor8, gbc_elevatorFloor8);
		





		JLabel elevatorFloor7 = new JLabel("");
		GridBagConstraints gbc_elevatorFloor7 = new GridBagConstraints();
		gbc_elevatorFloor7.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor7.gridx = 1;
		gbc_elevatorFloor7.gridy = 3;
		elevatorFloor7.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor7, gbc_elevatorFloor7);
		
		JLabel elevatorFloor6 = new JLabel("");
		GridBagConstraints gbc_elevatorFloor6 = new GridBagConstraints();
		gbc_elevatorFloor6.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor6.gridx = 1;
		gbc_elevatorFloor6.gridy = 4;
		elevatorFloor6.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor6, gbc_elevatorFloor6);
		
		JLabel elevatorFloor5 = new JLabel("");
		GridBagConstraints gbc_elevatorFloor5 = new GridBagConstraints();
		gbc_elevatorFloor5.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor5.gridx = 1;
		gbc_elevatorFloor5.gridy = 5;
		elevatorFloor5.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor5, gbc_elevatorFloor5);
		
		JLabel elevatorFloor4 = new JLabel("");
		GridBagConstraints gbc_elevatorFloor4 = new GridBagConstraints();
		gbc_elevatorFloor4.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor4.gridx = 1;
		gbc_elevatorFloor4.gridy = 6;
		elevatorFloor4.setIcon(new ImageIcon(img));
		//lblNewLabel_6.setVisible(false);
		frame.getContentPane().add(elevatorFloor4, gbc_elevatorFloor4);
		
		JLabel elevatorFloor3 = new JLabel("");
		GridBagConstraints gbc_elevatorFloor3 = new GridBagConstraints();
		gbc_elevatorFloor3.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor3.gridx = 1;
		gbc_elevatorFloor3.gridy = 7;
		elevatorFloor3.setIcon(new ImageIcon(img));
		//lblNewLabel_7.setVisible(false);
		frame.getContentPane().add(elevatorFloor3, gbc_elevatorFloor3);
		
		JLabel elevatorFloor2 = new JLabel("");
		GridBagConstraints gbc_elevatorFloor2 = new GridBagConstraints();
		gbc_elevatorFloor2.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor2.gridx = 1;
		gbc_elevatorFloor2.gridy = 8;
		elevatorFloor2.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor2, gbc_elevatorFloor2);
		
		JLabel elevatorFloor1 = new JLabel("");
		GridBagConstraints gbc_elevatorFloor1 = new GridBagConstraints();
		gbc_elevatorFloor1.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor1.gridx = 1;
		gbc_elevatorFloor1.gridy = 9;
		elevatorFloor1.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor1, gbc_elevatorFloor1);
		

	    elevatorFloors = new JLabel[] {elevatorFloor1,elevatorFloor2,elevatorFloor3,elevatorFloor4,elevatorFloor5,elevatorFloor6,elevatorFloor7,elevatorFloor8,elevatorFloor9,elevatorFloor10};
	//	Image person = new ImageIcon(this.getClass().getResource("/greenPerson.png")).getImage();
	    

		
		
		class Coordinates {

			  private int x;
			  private int y;

			  public Coordinates(int x, int y) {
			     this.x = x;
			     this.y = y;
			  }
			  
			  public String toString() {
				  return String.format("x is %s and y is %s", this.x, this.y);
			  }
			}

	
		people = new HashMap<Dimension, JLabel>();
		for (int x = 1;x<10;x++) {
			
			for (int y =2;y<10;y++) {
			//	Image person = new ImageIcon(this.getClass().getResource("/greenPersonSmall.png")).getImage();
				
				JLabel tmpLabel = new JLabel("");
				GridBagConstraints tmp = new GridBagConstraints();
				tmp.insets = new Insets(0, 0, 5, 5);
				tmp.gridx = x;
				tmp.gridy = y;
				tmpLabel.setIcon(new ImageIcon(person));
				people.put(new Dimension(x, y),tmpLabel);
				tmpLabel.setVisible(false);
 				frame.getContentPane().add(tmpLabel, tmp);
			}
		}


       addPerson(9);
       addPerson(9);
       addPerson(9);

		

	/*	new Thread(new Runnable() {
			 
			 public void run() {
				 try {
					//JLabel tmp = people.get(new Dimension(4,3));
					//tmp.setVisible(true);
					 
					 int x = 9;
					 int y = 8;
					// 9 8 7 6 5 4 3 2 1
					 // 1 2 3 4 5 6 7 8 
					for (int i= 1;x-i>1;i++) {
					System.out.println(i);
					JLabel tmpLabel11 = people.get(new Dimension(x-i,y));
					tmpLabel11.setVisible(true);
					Thread.sleep(1000);
					JLabel tmpLabel22 = people.get(new Dimension(x-i-1,y));
					tmpLabel11.setVisible(false);
					tmpLabel22.setVisible(true);
					}
				 }

				 catch(InterruptedException e) {
					 
				 }
				 
			 }
		 }).start(); */
		
	  

       
		
		//JLabel
		
		  pause = new Thread(){
			    public void run(){
			       try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    }
			  };
		  
	 	for (JLabel l : elevatorFloors) {
	 		l.setVisible(false);
	 	}
	new Thread(new Runnable() {
		 
		 public void run() {
			 try {
				// lblNewLabel.setBounds(0,200,100,100);
				/* for(int i = 0;i<9;i++) {
					 elevatorFloors[i].setVisible(true);
					 Thread.sleep(500);
					 elevatorFloors[i].setVisible(false);
					 elevatorFloors[i+1].setVisible(true);
					 


				 }*/
				 elevatorFloors[3].setVisible(true);
				 Thread.sleep(1000);
				 changeFloor(8);

				 System.out.println(elevatorFloors[1].isVisible());
				 }

			 catch(InterruptedException e) {
				 
			 }
			 
		 }
	 }).start(); 
	}
	


	
    
	public void changeFloor(int floorNumber) {

		//9 9 8 7 6 
		
				 new Thread(new Runnable() {
						 
						 public void run() {
							 try {
								int currentFloor = 0; 
								for(int i = 0;i<=9;i++) {
									if(elevatorFloors[i].isVisible()) {
										currentFloor = i;
										break; 
							    	}
								}
									//System.out.println(currentFloor);
								if(currentFloor > floorNumber) {
								   for(int j=currentFloor;j>=floorNumber;j--) {
										 elevatorFloors[j].setVisible(true);
										 Thread.sleep(1000);
										 elevatorFloors[j].setVisible(false);
										 elevatorFloors[j-1].setVisible(true);

								 }
								}
								else {
									   // 6 > 3 6
										for(int x=currentFloor;x<floorNumber;x++) {
											 elevatorFloors[x].setVisible(true);
											 Thread.sleep(1000);
											 elevatorFloors[x].setVisible(false);
											 elevatorFloors[x+1].setVisible(true);

								}
								}
							    
								}
							 
								

							 catch(InterruptedException e) {
								 
							 }
							 
						 }
					 }).start(); 				 
			}
	
	public void addPerson(int floor) {
		int x = floor;
		int y = 2;
		for(int i=y;i<=9;i++) {
			JLabel tmpLabel = people.get(new Dimension(i,x));
			if(tmpLabel.isVisible() == false) {
				y = i;
				break;
			}

		}
		JLabel tmpLabel = people.get(new Dimension(y,x));
		tmpLabel.setText(y-1+"");
		tmpLabel.setVisible(true);

	}
		

	}
	
