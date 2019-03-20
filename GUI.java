
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class GUI {

	private JFrame frame;
	private JTextField arrivalFloor;
	private JTextField destinationFloor;
	private static JLabel elevator;
	//private JLabel requestElevator; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
					//changeLocation(); 

			        
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	private static void changeLocation() throws InterruptedException{
		//Thread.sleep(10000);
		elevator.setLocation(20,10);
		
		
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Elevator");
		frame.setBounds(100, 100, 1000, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("images/greenPerson.png")).getImage();
		lblNewLabel.setIcon(new ImageIcon(img));
		//lblNewLabel.setBounds(185, 421, 68, 39);
		lblNewLabel.setVisible(false);
		frame.getContentPane().add(lblNewLabel);

		


		
		arrivalFloor = new JTextField();
		arrivalFloor.setBounds(940, 315, 68, 39);
		frame.getContentPane().add(arrivalFloor);
		arrivalFloor.setColumns(10);
		
		destinationFloor = new JTextField();
		destinationFloor.setBounds(940, 366, 68, 39);
		frame.getContentPane().add(destinationFloor);
		destinationFloor.setColumns(10);

		
		JLabel lblArrivalFloor = new JLabel("Arrival Floor");
		lblArrivalFloor.setBounds(800,327, 117, 15);
		frame.getContentPane().add(lblArrivalFloor);
		
		JLabel lblDestinationFloor = new JLabel("Destination Floor");
		lblDestinationFloor.setBounds(800, 372, 117, 27);

		lblNewLabel.setBounds(171, 52, 60, 89);
		lblNewLabel.setVisible(true);
		frame.getContentPane().add(lblDestinationFloor);
		
		
		//lblNewLabel.setBounds(186, 278, 60, 89); 278 for waiting at fourth floor 



		// dimensions 
		// top left -> elevator.setBounds(12, -81, 250, 300);
		// 1 down -> elevator.setBounds(12, 12, 250, 300);
		// person out on top floor -> lblNewLabel.setBounds(188, 12, 60, 89);
		// person out on second top floor -> lblNewLabel.setBounds(189, 107, 60, 89);
		// second number must be up and down 
        /* elevator floors 
         * 12, -82 -> 10th floor 
		   12, -22   
			12, 38
			12, 98  
			12, 158  -> 6th floor 
			12, 218
			12, 278
			12, 338
			12, 398
			12, 458
         */

		elevator = new JLabel("");
		elevator.setBounds(12, -81, 250, 300);
		Image img2 = new ImageIcon(this.getClass().getResource("images/elevator.jpg")).getImage();
		elevator.setIcon(new ImageIcon(img2));
		frame.getContentPane().add(elevator);
			JButton requestElevator = new JButton("Request Elevator");
			requestElevator.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
				   JOptionPane.showMessageDialog(null, "Please wait");
		           //requestElevator.setText("Please wait"); 
				   
				   
				   

					
				 	int arrivalFloorNumber = Integer.parseInt(arrivalFloor.getText());
				 	int destinationFloorNumber = Integer.parseInt(destinationFloor.getText());
				 	int floorDifference = 60; 
			        int elevatorPosition = 458 - ((arrivalFloorNumber-1) * floorDifference); // 158 
			        int arrivalPersonPosition = (11 - arrivalFloorNumber ) * floorDifference - 10 ; 
			        int currentPosition = -82; // 10th floor 
		
		

					/*JLabel[] labels = new JLabel[20]; 
					for(int i = 0; i<20; i++){
						labels[i] = new JLabel("");
					    labels[i].setIcon(new ImageIcon(img));
					    frame.getContentPane().add(labels[i]);
					    labels[i].setBounds(186+ (i *50), arrivalPersonPosition , 60, 89);


					}*/
					lblNewLabel.setBounds(186, arrivalPersonPosition, 60, 89);
					lblNewLabel.setText("1"); 
					//frame.getContentPane().add(lblNewLabel);
					lblNewLabel.setVisible(true);

			        // -82    
			        // -22 
			        // 38
			        // 98 
			        // 158 collecting a person at floor 6 
			        // 218
			        // 278 let person off at floor 4
			        System.out.println(elevatorPosition); 
	           
				 	
				new Thread(new Runnable() {
					 
					 public void run() {
						 try {
							 for(int i = currentPosition ; i<=elevatorPosition ; i+=60) {
								 Thread.sleep(1000);
							 	 elevator.setBounds(12, i, 250, 300);
							 	//lblNewLabel.setBounds(12, i, 250, 300);		
								 System.out.println(i);
							 }
							 Thread.sleep(1000);
							 lblNewLabel.setLocation(12,arrivalPersonPosition)	;
							 Thread.sleep(1000);
							 
							 // in the elevator
							 //lblNewLabel.setLocation(12,arrivalPersonPosition)	;
							 //outside the elevator 
							 //lblNewLabel.setLocation(12,arrivalPersonPosition)
							 
							 if (arrivalFloorNumber == destinationFloorNumber) {
								 lblNewLabel.setLocation(186,arrivalPersonPosition)	;

							 }
							 
							 else {
								 								 int y = elevator.getY();
								  int difference = arrivalFloorNumber - destinationFloorNumber;
								  int destinationFloorPosition = 458 - ((destinationFloorNumber-2) * 60); 
								 if (arrivalFloorNumber > destinationFloorNumber){
								 for(int i = y; i>= destinationFloorPosition; i+=60  ) {
									 Thread.sleep(1000);
								 	 elevator.setBounds(12, i, 250, 300);		
								    lblNewLabel.setBounds(12, i + 25  , 250, 300);				 
								 
									}
								} 
								
								else{
									destinationFloorPosition = (destinationFloorNumber - arrivalFloorNumber) * 60; 
									System.out.printf("floor position is %s and y is %s",destinationFloorPosition, y); 
									//y = 278 278 >= 180 
								for(int i = 0; i>= destinationFloorPosition; i+=60  ) {
									 Thread.sleep(1000);
								 	 elevator.setBounds(12, y - i, 250, 300);		
								    lblNewLabel.setBounds(12, y - i + 25  , 250, 300);				 
								 
									}
									
									
									
								}
								 y =  lblNewLabel.getY();
								 lblNewLabel.setLocation(186,y)	;

							/*  int y = elevator.getY();
							  int difference = arrivalFloorNumber - destinationFloorNumber;
							  int destinationFloorPosition = 458 - ((destinationFloorNumber-1) * 60); 
							 for(int i = y; i<= destinationFloorPosition; i+=60  ) {
								 Thread.sleep(3000);
							 	 elevator.setBounds(12, i, 250, 300);		
							    lblNewLabel.setBounds(12, i, 250, 300);				 

							 }
							 y = lblNewLabel.getY();
							 int x =  lblNewLabel.getX();
							 System.out.println(x);
							 lblNewLabel.setBounds(x+200, y, 250, 300);	
							 }*/
							 }
						 }
						 catch(InterruptedException e) {
							 
						 }
						 
					 }
				 }).start(); 

	   	 
			     
			     
					

				}
			});
			requestElevator.setBounds(817, 434, 171, 39);
			frame.getContentPane().add(requestElevator);
			

	
	
	
	
	
	}
	
	
	
	
	
	
	
	
}
