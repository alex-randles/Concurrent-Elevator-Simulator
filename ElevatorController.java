
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


// this clas will allow sychronized access to the elevator thread 
// will control the actions of the elevator 
import java.util.concurrent.*;
import java.util.*; 


public class ElevatorController{
	public gridLayout gui; 
    public boolean goingUp; 
    public boolean goingDown; 
    public int currentFloor; 
    public int currentTime; 
    public int numPeople; 
    public int currentWeight; 
    public int maxWeight; 
    // a person will be added to the waiting queue when we the current time has reached there arrival time 
    public ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> waiting; 
    // all the people who will want to use the elevator 
    public ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> request; 
    public ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> inElevator;   
    // the request hashmap maps the request to the floor they are at 
    // the person making the request will join the linked queue for that floor 
    // (hashmap) {floor 0 -> linkedQueue{0 : [person1 - > person 2 -> 3}
    
    public ElevatorController(){
		this.goingUp = true; 
		this.goingDown = false; 
		// all of the people who want to request the elevator
		// maps to their arrrival floor 
		this.waiting = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>>();
		// all of the people currently in the elevator  
		// maps the person to their destination floor
		this.inElevator = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>>(); 
		this.request = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>>();
		this.currentFloor = 0; 
		this.currentTime = 0; 
		this.currentWeight = 0 ;
		this.maxWeight = 200; 
		this.gui = new gridLayout(); 
		gui.display(); 





	}
	
	
	// add a person into the in elevator, request or waiting queue 
	public synchronized void addPerson(Person person, int floor, ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> hashmap){
		ConcurrentLinkedQueue<Person> tmp = new ConcurrentLinkedQueue<Person>(); 
		if (hashmap.containsKey(floor)){
			tmp = hashmap.get(floor);
			tmp.add(person);
			hashmap.put(floor, tmp); 
		}
		else{
			tmp.add(person); 
			hashmap.put(floor, tmp); 
			
		}
		
	}
	
		// add a person into the in elevator, request or waiting queue to the tail 
	public synchronized void addPersonTail(Person person, int floor, ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> hashmap){
		ConcurrentLinkedQueue<Person> tmp = new ConcurrentLinkedQueue<Person>(); 
		if (hashmap.containsKey(floor)){
			tmp = hashmap.get(floor);
			tmp.offer(person);
			hashmap.put(floor, tmp); 
		}
		else{
			tmp.add(person); 
			hashmap.put(floor, tmp); 
			
		}
		
	}
	
	// remove a person from either the inElevator, request or waiting queue 
	public synchronized Person removeAPerson(int floor, ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> hashmap) {
		ConcurrentLinkedQueue<Person> tmp = new ConcurrentLinkedQueue<Person>(); 
		if (hashmap.containsKey(floor)){
			tmp = hashmap.get(floor);
			Person personRemoved = tmp.poll();
			hashmap.put(floor, tmp); 
			if (hashmap.get(floor).isEmpty()){
				hashmap.remove(floor); 
			}
			return personRemoved; 
		}
		
		return null; 
		
	}
	
	// A person wants to request the elevator, add them into request hashmap 
	public synchronized void makeRequest(Person person){
		addPerson(person,person.arrivalTime, request); 

		
	}
    
    
    // if the persons arrival time is <= current time add them to the waiting queue 
    public  synchronized void acceptRequest(){
	   
	   Person personWaiting = removeAPerson(currentTime, request); 
	   while(personWaiting != null){
		   addPerson(personWaiting,personWaiting.arrivalFloor, waiting); 
		   removeAPerson(personWaiting.arrivalFloor, request);
		   personWaiting  =   removeAPerson(currentTime, request); 
	   }
			
		
		
		
	}
    
   
    // this will decide which floor the elevator goes to next 
    public synchronized void changeFloor(int minFloor,int maxFloor) throws InterruptedException{
	    


     
     // Example for below - this will tell which direction the elevator should go 
     // Current floor -> 5
     // InElevator Keys -> 1,2,3
     // these keys are the peoples destination  floor
     // max = 3 
     // since max is less then current floor the elevator must go down 

	 if(!(inElevator.isEmpty())){
       int max = Collections.max(inElevator.keySet());
       if(max < currentFloor){
		   goingUp = false;
		   goingDown = true;                   
	   }
	   else{
		   goingUp = true;
		   goingDown = false;  
	   }
       
       }
       
       else{

	      // if there is no one in the elevator and its going up
	      // and there are no people waiting on the upper floors
	      // go down 
	      if(!(waiting.isEmpty()) && goingUp == true){
			 int max = Collections.max(waiting.keySet());
			 if (max < currentFloor){
				 goingUp = false;
				 goingDown = true; 
			 }
		  }

	   }
    

		
      if(!(waiting.isEmpty()) || !(inElevator.isEmpty()) ){
		if (goingUp == true){
			   
			   if(currentFloor == maxFloor){
				    currentFloor--;	   
				    goingUp = false; 
				    goingDown = true; 			    
			   }
		       else{
				   currentFloor++;
				}
	       }
			
		   else{
			   
			   if(currentFloor == minFloor){
					goingUp = true; 
				    goingDown = false; 	
				    currentFloor++;
		   
				 }
				else{
			      currentFloor--;
                 }
			   
	 }  


			 // print what floor the elevator is on 
			System.out.printf("Elevator is on floor %s************\n",this.currentFloor); 	   

 }
			 


    }


    public synchronized void enterElevator() throws InterruptedException{
		
		



	   boolean headingPrinted = false; 
	   // remove a person who's waiting at the current floor
	   Person personEntering = removeAPerson(currentFloor, waiting); 
	   // while there is people waiting on the current floor
       while(personEntering!=null){
		   
				// print heading 
		   	    if (!(headingPrinted)){
					System.out.printf("****************\nAllowing people in on floor %s...\n",currentFloor);
					headingPrinted = true; 
				}
				
				int combinedWeight =  this.currentWeight + personEntering.getTotalWeight()  ; 

				if (combinedWeight >= this.maxWeight){
					//ConcurrentLinkedQueue<Person> tmp = waiting.get(currentFloor);
					//System.out.println(tmp); 
					//tmp.offer(personEntering);
					//waiting.put(currentFloor,tmp); 
					//System.out.println("Max weight is too high with elevator: " +  waiting); 
					addPersonTail(personEntering, currentFloor, waiting); 
					System.out.printf("**********************************************************\nElevator too heavy with combined weight %d and max weight limit %d\nPerson %s is leaving the elevator\n**********************************************************\n", combinedWeight, maxWeight, personEntering); 
					break; 
				}
				
				// add the persons weight to the current weight 
				this.currentWeight += personEntering.getTotalWeight()  ; 
				// add the person to the in elevator 
				addPerson(personEntering, personEntering.destinationFloor,inElevator);
				// print persons details
				System.out.println(personEntering); 
				// remove another waiting person on the current floor 
		        personEntering = removeAPerson(currentFloor, waiting); 

	   }


	   notifyAll(); 
		
	}
   
   
   public synchronized void exitElevator() throws InterruptedException{
     // System.out.println(inElevator.isEmpty()); 
      
      // remove a person from the current floor who's in the elevator        
	  Person personLeaving = removeAPerson(currentFloor, inElevator);   
      boolean headingPrinted = false; 
      // while there are people on this floor remove them from the elevator
      while(personLeaving!=null){
		 
		 // print heading to show people are leaving 
		 if(!(headingPrinted)){
			System.out.printf("****************\nLetting people out on floor %s...\n",currentFloor); 
			headingPrinted = true; 
	     }
	     
	    // print out who's leaving 
        System.out.println(personLeaving); 
        // adjust weight according to the people who are leaving
        this.currentWeight-= personLeaving.getTotalWeight() ;
        // remove another person from this floor 
	    personLeaving = removeAPerson(currentFloor, inElevator); 
	  
     	}
		
	  
	   
	 notifyAll();   
	 }

   
   
   
   public synchronized void transferPeople(ElevatorController faulty, ElevatorController backUp){
	   
	   // transfer over current values 
	   
	   backUp.request = faulty.request;
	   backUp.waiting = faulty.waiting;
	   backUp.currentTime = faulty.currentTime + 1; 
	   
	   // transfer people in elevator to current floor of backup elevator waiting queue 
	   for ( Map.Entry<Integer,ConcurrentLinkedQueue<Person>> entry : inElevator.entrySet()) {
		  for (Person person : entry.getValue()){
			  addPersonTail(person, currentFloor,backUp.waiting); 
			  System.out.printf("Person %s is exiting on floor %d and waiting for next elevator\n", person.id, currentFloor); 
		  }
	   }
	   System.out.println(backUp.waiting); 
	   //personTransfer = removeAPerson(currentFloor,inElevator); 

	   
   }
	 

 

	public synchronized int getTime(){
		return this.currentTime;
	}
	
	

    
}







//// ********** GUI 







class gridLayout {

	public JLabel[] elevatorFloors;
	public Thread pause;
	public Map<Dimension, JLabel> people;
	public JFrame frame;
	public JLabel elevatorFloor1; 
	public JLabel elevatorFloor2;
	public JLabel elevatorFloor3;
	public JLabel elevatorFloor4;
	public JLabel elevatorFloor5;
	public JLabel elevatorFloor6;
	public JLabel elevatorFloor7;
	public JLabel elevatorFloor8;
	public JLabel elevatorFloor9;
	public JLabel elevatorFloor10;

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
	public gridLayout() {
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("static-access")
	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 1, 1,1,1,1,1,1,1,1,1,1,1,1,1};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[] {0.0, 0.2,0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
		gridBagLayout.rowWeights = new double[]{0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		elevatorFloor10 = new JLabel(new ImageIcon("elevator.jpg"));
		//frame.add(elevatorFloor10);
		GridBagConstraints gbc_elevatorFloor10 = new GridBagConstraints();
		gbc_elevatorFloor10.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor10.gridx = 1;
		gbc_elevatorFloor10.gridy = 0;
		//JLabel knight=new JLabel(new ImageIcon("knight.jpg"));
		//background1.add(knight);
		//frame.add(background1);
		Image img = new ImageIcon(this.getClass().getResource("elevator.jpg")).getImage();
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
		

		
		
		
		elevatorFloor9 = new JLabel(new ImageIcon("elevator.jpg"));
		GridBagConstraints gbc_elevatorFloor9 = new GridBagConstraints();
		gbc_elevatorFloor9.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor9.gridx = 1;
		gbc_elevatorFloor9.gridy = 1;
		//gbc_lblNewLabel_1.gridheight = 5;
		//elevatorFloor9.setIcon(new ImageIcon("/home/alex/elevator/images/elevator.jpg"));
		frame.getContentPane().add(elevatorFloor9, gbc_elevatorFloor9);
		
		


		//JLabel elevatorFloor8 = new JLabel(new ImageIcon("/home/alex/elevator/images/elevator.jpg"));
	     elevatorFloor8 = new JLabel("");
		//elevatorFloor8.setLayout(new OverlayLayout(elevatorFloor8));
	     elevatorFloor8.setLayout(new BorderLayout());
		elevatorFloor8.setIcon(new ImageIcon(img));
		Image person = new ImageIcon(this.getClass().getResource("greenPersonSmall.png")).getImage();
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
		





		elevatorFloor7 = new JLabel("");
		GridBagConstraints gbc_elevatorFloor7 = new GridBagConstraints();
		gbc_elevatorFloor7.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor7.gridx = 1;
		gbc_elevatorFloor7.gridy = 3;
		elevatorFloor7.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor7, gbc_elevatorFloor7);
		
		elevatorFloor6 = new JLabel("");
		GridBagConstraints gbc_elevatorFloor6 = new GridBagConstraints();
		gbc_elevatorFloor6.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor6.gridx = 1;
		gbc_elevatorFloor6.gridy = 4;
		elevatorFloor6.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor6, gbc_elevatorFloor6);
		
		elevatorFloor5 = new JLabel("");
		GridBagConstraints gbc_elevatorFloor5 = new GridBagConstraints();
		gbc_elevatorFloor5.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor5.gridx = 1;
		gbc_elevatorFloor5.gridy = 5;
		elevatorFloor5.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor5, gbc_elevatorFloor5);
		
	    elevatorFloor4 = new JLabel("");
		GridBagConstraints gbc_elevatorFloor4 = new GridBagConstraints();
		gbc_elevatorFloor4.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor4.gridx = 1;
		gbc_elevatorFloor4.gridy = 6;
		elevatorFloor4.setIcon(new ImageIcon(img));
		//lblNewLabel_6.setVisible(false);
		frame.getContentPane().add(elevatorFloor4, gbc_elevatorFloor4);
		
		 elevatorFloor3 = new JLabel("");
		GridBagConstraints gbc_elevatorFloor3 = new GridBagConstraints();
		gbc_elevatorFloor3.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor3.gridx = 1;
		gbc_elevatorFloor3.gridy = 7;
		elevatorFloor3.setIcon(new ImageIcon(img));
		//lblNewLabel_7.setVisible(false);
		frame.getContentPane().add(elevatorFloor3, gbc_elevatorFloor3);
		
		 elevatorFloor2 = new JLabel("");
		GridBagConstraints gbc_elevatorFloor2 = new GridBagConstraints();
		gbc_elevatorFloor2.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor2.gridx = 1;
		gbc_elevatorFloor2.gridy = 8;
		elevatorFloor2.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor2, gbc_elevatorFloor2);
		
		elevatorFloor1 = new JLabel("");
		GridBagConstraints gbc_elevatorFloor1 = new GridBagConstraints();
		gbc_elevatorFloor1.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor1.gridx = 1;
		gbc_elevatorFloor1.gridy = 9;
		elevatorFloor1.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor1, gbc_elevatorFloor1);
		

	    elevatorFloors = new JLabel[] {elevatorFloor1,elevatorFloor2,elevatorFloor3,elevatorFloor4,elevatorFloor5,elevatorFloor6,elevatorFloor7,elevatorFloor8,elevatorFloor9,elevatorFloor10};
	//	Image person = new ImageIcon(this.getClass().getResource("/greenPerson.png")).getImage();

		
		


	
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





		  
	 	for (JLabel l : elevatorFloors) {
	 		l.setVisible(false);
	 	}
	 	
	 	
	 	elevatorFloor2.setVisible(true); 
	 	changeFloor(6); 
	 	addPerson(7);
	    addPerson(7);
	 	addPerson(7);


	
	
	}
	
	
	public void display(){
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
								   for(int j=currentFloor;j>=floorNumber-1;j--) {
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
	
