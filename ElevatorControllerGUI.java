
import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.*;
import javax.swing.JLabel;
import javax.swing.OverlayLayout;
import javax.swing.JLayeredPane;
import java.awt.*;
import javax.swing.*;
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
import  java.util.concurrent.atomic.*; 
import java.util.*; 


public class ElevatorControllerGUI{
	// GUI variables 
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
	
	// elevator controller variables
    public boolean goingUp; 
    public boolean goingDown;
    private boolean doorsOpen;  
    public  int currentFloor; 
    public int currentTime; 
    public int numPeople; 
    public int currentWeight; 
    public int maxWeight; 
    private static final AtomicInteger elevatorNumberGenerator = new AtomicInteger(1);  
    public int elevatorNumber;
    public Elevator elevator; 
    // a person will be added to the waiting queue when we the current time has reached there arrival time 
    public static ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> waiting; 
    // all the people who will want to use the elevator 
    public ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> request; 
    public ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> inElevator;   
    // the request hashmap maps the request to the floor they are at 
    // the person making the request will join the linked queue for that floor 
    // (hashmap) {floor 0 -> linkedQueue{0 : [person1 - > person 2 -> 3}
    public grid gui; 
    public ElevatorControllerGUI(){
		// decide which direction the elevator is going 
		this.goingUp = true; 
		this.goingDown = false; 
		this.doorsOpen = false; 
		// all the request the elevator must handle 
		this.request = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>>();
		// all of the people who have arrived and are waiting on a floor
		this.waiting = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>>();
		// all the people currently in the elevator 
		this.inElevator = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>>(); 
		this.currentFloor = 1; 
		// time will go up in every time the elevator goes up a floor 
		this.currentTime = 0; 
		this.currentWeight = 0 ;
		this.maxWeight = 200; 
		// assign each elevator a number
		this.elevatorNumber = elevatorNumberGenerator.getAndIncrement(); 
		gui = new grid(); 

						//gui.test(); 

				//gui.display(); 
						//gui.changeFloor(10);
		//gui.changeFloor(1); 
			   gui.frame.setVisible(true);

				//gui.test(); 
		//grid.elevatorFloor7.setVisible(true);




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
	
	
	// add a person into the in elevator, request or waiting queue in first 
	// this is mainly used when a fault happens 
	// the people who are in the elevator at the time should be put first in queue for next elevator 
	public synchronized void addPersonFirst(Person person, int floor, ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> hashmap){
		ConcurrentLinkedQueue<Person> oldQueue = new ConcurrentLinkedQueue<Person>(); 
	    ConcurrentLinkedQueue<Person> newQueue = new ConcurrentLinkedQueue<Person>(); 
		if (hashmap.containsKey(floor)){
			oldQueue = hashmap.get(floor); 
			newQueue.add(person); 
			for (Person p : oldQueue){
				newQueue.add(p);  
			}
			hashmap.put(floor, newQueue); 
		}
		else{
			oldQueue.add(person); 
			hashmap.put(floor, oldQueue); 
			
		}
		
	}
	
	   	// A person wants to request the elevator, add them into request hashmap 
	public synchronized void makeRequest(Person person){
		addPerson(person,person.getArrivalTime(), request); 

		
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
	

    
    
    // if the persons arrival time is <= current time add them to the waiting queue 
    public  synchronized void acceptRequest(){
	   Person personWaiting = removeAPerson(currentTime, request); 
	   while(personWaiting != null){
		   gui.addPerson(personWaiting); 
		   addPerson(personWaiting,personWaiting.getArrivalFloor(), waiting); 
		   removeAPerson(personWaiting.getArrivalFloor(), request);
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
		   this.goingUp = false;
		   this.goingDown = true;                   
	   }
	   else{
		   this.goingUp = true;
		   this.goingDown = false;  
	   }
       
       }
       
       else{

	      // if there is no one in the elevator and its going up
	      // and there are no people waiting on the upper floors
	      // go down 
	      if(!(waiting.isEmpty()) && goingUp == true){
			 int max = Collections.max(waiting.keySet());
			 if (max < currentFloor){
				this.goingUp = false;
				this.goingDown = true; 
			 }
		  }

	   }
    
    

	  // if there are people waiting or in the elevator 
      if(!(waiting.isEmpty()) || !(inElevator.isEmpty()) ){
		  
		 // print what floor the elevator is on 
	    System.out.printf("Elevator %d is on floor %s************\n",this.elevatorNumber,this.currentFloor); 	 
	    
	    // check if the next floor is max or min then decide direction   
		if (goingUp == true){
			   
			   if(this.currentFloor == maxFloor){
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
			 
	   



		 }
		 
		 	   if (waiting.isEmpty() && inElevator.isEmpty()){
				System.out.printf("The elevator is sleeping on floor %d\n",currentFloor); 			
			}
		  
		  

	      //change floor on GUI
		  gui.changeFloor(currentFloor);
		  // update the time
		  this.currentTime++; 	 


    }


    public synchronized void enterElevator() throws InterruptedException{
		
		



	   boolean headingPrinted = false; 
	   // remove a person who's waiting at the current floor
	   Person personEntering = removeAPerson(currentFloor, waiting); 
	   // while there is people waiting on the current floor
       while(personEntering!=null){
		        
		        
				// print heading 
		   	    if (!(headingPrinted)){
					System.out.printf("Stopping on floor %d for people\n", currentFloor); 
					System.out.printf("****************\nAllowing people in on floor %s...\n",currentFloor);
					headingPrinted = true; 
				}
				
				// check if is overloaded 
				int combinedWeight =  this.currentWeight + personEntering.getTotalWeight();
				
				if (combinedWeight >= maxWeight){
					// if the elevator is over weight 
					// the last person in exits the elevator and is put back at the front of the queue on that floor 
					addPersonFirst(personEntering, currentFloor, waiting); 
					System.out.printf("**********************************************************\nElevator %d too heavy with combined weight %d and max weight limit %d\nPerson %s is leaving the elevator\n**********************************************************\n", this.elevatorNumber, combinedWeight, maxWeight, personEntering); 
					break; 
				}
				
				// add the persons weight to the current weight 
				this.currentWeight += personEntering.getTotalWeight(); 
				// add the person to the in elevator 
				addPerson(personEntering, personEntering.getDestinationFloor(),inElevator);
				// remove from the GUI
				gui.removePerson(personEntering); 
				gui.addPersonToElevator(personEntering); 
				// print persons details
				System.out.println(personEntering); 
				// remove another waiting person on the current floor 
		        personEntering = removeAPerson(currentFloor, waiting); 

	   }


		
	}
   
   
   public synchronized void exitElevator() throws InterruptedException{
      
      // remove a person from the current floor who's in the elevator        
	  Person personLeaving = removeAPerson(currentFloor, this.inElevator);   
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
        // remove person from the elevator GUI
        //gui.removePersonFromElevator(personLeaving);
        // remove another person from this floor 
	    personLeaving = removeAPerson(currentFloor,inElevator); 
	  
     	}
		
	  
	   
	 }

   

    
   
   public synchronized void transferPeople(ElevatorController faulty, ElevatorController backUp){
	   
	   // transfer over current values 
	   
	   backUp.request = faulty.request;
	   backUp.waiting = faulty.waiting;
	   backUp.currentTime = faulty.currentTime + 1; 
	   
	   // transfer people in elevator to current floor of backup elevator waiting queue 
	   for ( Map.Entry<Integer,ConcurrentLinkedQueue<Person>> entry : inElevator.entrySet()) {
		  for (Person person : entry.getValue()){
			  addPersonFirst(person, currentFloor,backUp.waiting); 
			  System.out.printf("Person %s is exiting on floor %d and waiting for backup elevator\n", person.getId(), currentFloor); 
		  }
	   }
	   System.out.println(backUp.waiting); 

	   
   }
	 

 

	public synchronized int getTime(){
		return this.currentTime;
	}
	
}
	



class grid {

	private JLabel[] elevatorFloors;
	private Thread pause;
	private Map<Dimension, JLabel> people;
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
    public  ConcurrentHashMap<Person,JLabel> assignedLabels;
    public ConcurrentHashMap<Dimension,JLabel> peopleInElevator; 
    public int count;
   // public  ConcurrentHashMap<Integer,ConcurrentLinkedQueue<JLabel>> peopleInElevator;

	public  void display() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					grid window = new grid();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
		public  void test() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//addPerson(1); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	
	public grid() {
		initialize();
		count = 1;
		assignedLabels = new ConcurrentHashMap<Person,JLabel>();
	    

	}

	public void initialize() {
		frame = new JFrame("Elevator Simulator");
		frame.setBounds(100, 100, 600, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 1, 1,1,1,1,1,1,1,1,1,1,1,1,1};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[] {0.0, 0.2,0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
		gridBagLayout.rowWeights = new double[]{0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);

		
		int floor = 10;
		for(int i=0;i<10;i++) {
			JLabel tmpLabel = new JLabel("" + floor);
			GridBagConstraints tmp = new GridBagConstraints();
			tmp.insets = new Insets(0, 0, 5, 5);
			tmp.gridx = 0;
			tmp.gridy = i;
			frame.getContentPane().add(tmpLabel, tmp);
			floor--;

		}
		
		Image person = new ImageIcon(this.getClass().getResource("greenPersonSmall.png")).getImage();
		Image img = new ImageIcon(this.getClass().getResource("/elevator.jpg")).getImage();


	    elevatorFloor10 = new JLabel();
	    elevatorFloor10.setLayout(new GridLayout(2,5));
		GridBagConstraints gbc_elevatorFloor10 = new GridBagConstraints();
		gbc_elevatorFloor10.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor10.gridx = 1;
		gbc_elevatorFloor10.gridy = 0;
		elevatorFloor10.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor10, gbc_elevatorFloor10);
		
		
		elevatorFloor9 = new JLabel();
		elevatorFloor9.setLayout(new GridLayout(2,5));
		GridBagConstraints gbc_elevatorFloor9 = new GridBagConstraints();
		gbc_elevatorFloor9.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor9.gridx = 1;
		gbc_elevatorFloor9.gridy = 1;
		elevatorFloor9.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor9, gbc_elevatorFloor9);
		
		


		elevatorFloor8 = new JLabel("");
		elevatorFloor8.setLayout(new GridLayout(2,5));
		GridBagConstraints gbc_elevatorFloor8 = new GridBagConstraints();
		gbc_elevatorFloor8.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor8.gridx = 1;
		gbc_elevatorFloor8.gridy = 2;
		elevatorFloor8.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor8, gbc_elevatorFloor8);





		
	    elevatorFloor7 = new JLabel();
	    elevatorFloor7.setLayout(new GridLayout(2,5));
		GridBagConstraints gbc_elevatorFloor7 = new GridBagConstraints();
		gbc_elevatorFloor7.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor7.gridx = 1;
		gbc_elevatorFloor7.gridy = 3;
		elevatorFloor7.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor7, gbc_elevatorFloor7);
		
		elevatorFloor6 = new JLabel();
		elevatorFloor6.setLayout(new GridLayout(2,5));
		GridBagConstraints gbc_elevatorFloor6 = new GridBagConstraints();
		gbc_elevatorFloor6.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor6.gridx = 1;
		gbc_elevatorFloor6.gridy = 4;
		elevatorFloor6.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor6, gbc_elevatorFloor6);
		
		elevatorFloor5 = new JLabel();
		elevatorFloor5.setLayout(new GridLayout(2,5));
		GridBagConstraints gbc_elevatorFloor5 = new GridBagConstraints();
		gbc_elevatorFloor5.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor5.gridx = 1;
		gbc_elevatorFloor5.gridy = 5;
		elevatorFloor5.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor5, gbc_elevatorFloor5);
		
		elevatorFloor4 = new JLabel();
		elevatorFloor4.setLayout(new GridLayout(2,5));
		GridBagConstraints gbc_elevatorFloor4 = new GridBagConstraints();
		gbc_elevatorFloor4.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor4.gridx = 1;
		gbc_elevatorFloor4.gridy = 6;
		elevatorFloor4.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor4, gbc_elevatorFloor4);
		
		elevatorFloor3 = new JLabel();
		elevatorFloor3.setLayout(new GridLayout(2,5));
		GridBagConstraints gbc_elevatorFloor3 = new GridBagConstraints();
		gbc_elevatorFloor3.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor3.gridx = 1;
		gbc_elevatorFloor3.gridy = 7;
		elevatorFloor3.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor3, gbc_elevatorFloor3);
		
		elevatorFloor2 = new JLabel();
		elevatorFloor2.setLayout(new GridLayout(2,5));
		GridBagConstraints gbc_elevatorFloor2 = new GridBagConstraints();
		gbc_elevatorFloor2.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor2.gridx = 1;
		gbc_elevatorFloor2.gridy = 8;
		elevatorFloor2.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor2, gbc_elevatorFloor2);
		
		elevatorFloor1 = new JLabel();
		elevatorFloor1.setLayout(new GridLayout(2,5));
		GridBagConstraints gbc_elevatorFloor1 = new GridBagConstraints();
		gbc_elevatorFloor1.insets = new Insets(0, 0, 5, 5);
		gbc_elevatorFloor1.gridx = 1;
		gbc_elevatorFloor1.gridy = 9;
		elevatorFloor1.setIcon(new ImageIcon(img));
		frame.getContentPane().add(elevatorFloor1, gbc_elevatorFloor1);
		
		
		 
		

	    elevatorFloors = new JLabel[] {elevatorFloor1, elevatorFloor2,elevatorFloor3,elevatorFloor4,elevatorFloor5,elevatorFloor6,elevatorFloor7,elevatorFloor8,elevatorFloor9,elevatorFloor10};
	    
		
	

		
		peopleInElevator = new ConcurrentHashMap<Dimension,JLabel>();
		for(int i = 0; i<=10;i++){
			for(int j=0;j<elevatorFloors.length;j++){
				JLabel over = new JLabel("");
				over.setIcon(new ImageIcon(person));
				over.setText(""+i); 
				over.setVisible(false);
				elevatorFloors[j].add(over);
				peopleInElevator.put(new Dimension(j,i), over);
		}
	}
		

	    // create all the people
		people = new ConcurrentHashMap<Dimension, JLabel>();
		for (int x = 2;x<10;x++) {
			
			for (int y =0;y<10;y++) {
				
				JLabel tmpLabel = new JLabel();
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


       
       
        // hide all elevator floors initially
	 	for (JLabel l : elevatorFloors) {
	 		l.setVisible(false);
	 	}
	 	
	 	// set elevator visible at bottom floor 
	 	elevatorFloors[0].setVisible(true); 
	 	//changeFloor(1); 
	    /*elevatorFloor1.setLayout(new BorderLayout());
	 	elevatorFloor1.add(over,BorderLayout.NORTH);
	 	elevatorFloor1.add(over,BorderLayout.SOUTH);*/
	 			


		
		
		
		




	}
	
	
	public int getCurrentFloor(){
		int currentFloor = 0; 
		for(int i = 0;i<=9;i++) {
		if(elevatorFloors[i].isVisible()) {
						currentFloor = i;
						break; 
							    }
						}
		return currentFloor; 
	}


	
    
	public void changeFloor(int floorNumber) {

		//9 9 8 7 6 
		         								//int floorNumber = floor -  1; 

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
									  // 9 8 7 6 5 4 4 3 2 1
									  
									    
									   	//1 1 < 9   
										for(int x=currentFloor;x<floorNumber-1;x++) {
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
	


 
	
	

	
	public void removePersonFromElevator(Person person){
		for(int i = 0; i<6;i++){
			JLabel tmp = peopleInElevator.get(new Dimension(person.getDestinationFloor(), i));
			String id  = String.valueOf(person.getId());
			if (tmp.getText().equals(id)){
				tmp.setVisible(false); 
			}

			
		}
	}
  public void addPersonToElevator(Person person){
		

	
      new Thread(new Runnable() {
						 
						 public void run() {
							 try {
								int currentFloor = getCurrentFloor(); 
								String id  = String.valueOf(person.getId());
								JLabel tmp = peopleInElevator.get(new Dimension(currentFloor, count));
								if (person.getArrivalFloor() < person.getDestinationFloor()){
								for(int i=person.getArrivalFloor(); i<person.getDestinationFloor();i++){
										tmp.setVisible(true);
										tmp.setText(id); 
										tmp = peopleInElevator.get(new Dimension(i, count ));
										Thread.sleep(100); 


									}
	
							   }
							else{
								for(int i=person.getArrivalFloor(); i>person.getDestinationFloor();i--){
												tmp.setVisible(true);
												tmp.setText(id); 
												tmp = peopleInElevator.get(new Dimension(i, count));
								}
							}
						count++; 
		

								}
							 
								

							 catch(InterruptedException e) {
								 
							 }
						 }
							 

					 }).start(); 			
	
	
		
	}
	 
		
		
	
	public void addPerson(Person person) {
		int floor = 10 -person.getArrivalFloor();
		String id  = String.valueOf(person.getId());
		JLabel tmpLabel; 
		// max floor is (9,9) 
		for(int y = 2; y<10;y++){
			tmpLabel = people.get(new Dimension(y,floor));
			if (!(tmpLabel.isVisible())){
				tmpLabel.setVisible(true); 
				tmpLabel.setText(id); 
				assignedLabels.put(person, tmpLabel); 
				break; 
			}
		}
	}
	
	
	public void removePerson(Person person){
	    	


	
				 new Thread(new Runnable() {
						 
						 public void run() {
							 try {
							        		Thread.sleep(1000); 

							    	   JLabel assignedLabel = assignedLabels.get(person);
	   assignedLabel.setVisible(false); 

								}
							 
								

							 catch(InterruptedException e) {
								 
							 }
							 
						 }
					 }).start(); 			
	
	}
		

	}
	



