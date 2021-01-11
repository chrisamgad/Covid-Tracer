
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Platform;


public class HumanBeingThread implements Runnable {

    private int RandomRowIndex;
    private int RandomColumnIndex;
    private int CurrentRowIndex;
    private int CurrentColumnIndex;
    private int CurrentXLocation;
    private int CurrentYLocation;
    private int ArrayNRows;
    private int ArrayNColumns;
    private int MetersToMoveInIndex; // example 1 meter equals 1 index, 2 meters equals 2 indexes, etc...
    private int PersonCovidState; // 0 means this thread doesnt have covid and 1 means this thread has covid
    private ImageView IMGview;
    private boolean NearCovidNode;
    private int SafeDistanceMeters;
    private long elapsedtime;
    private Timer CheckingInfectiontimer;
    private int RandomTime;
    private boolean Infected;
    private int MinMillisecondsForNodeToWait;
    private int MaxMillisecondsForNodeToWait;
    private int TimeInUnsafeAreaTillInfected;
    private boolean StopTimers;  
    private Timer GameTimer = new Timer();

    public HumanBeingThread(int PersoncovidState, int ArrayNRows, int ArrayNColumns, int SafeDistanceMeters,
            int MetersToMoveInIndex, int MinMillisecondsForNodeToWait, int MaxMillisecondsForNodeToWait,
            int TimeInUnsafeAreaTillInfected) {

        this.PersonCovidState = PersoncovidState;// 0 means this thread doesnt have covid and 1 means this thread has
                                                 // covid
        this.ArrayNRows = ArrayNRows;
        this.ArrayNColumns = ArrayNColumns;
        this.NearCovidNode = false;
        this.SafeDistanceMeters = SafeDistanceMeters;
        this.MetersToMoveInIndex = MetersToMoveInIndex;
        this.Infected = false;
        this.CheckingInfectiontimer = new Timer();
        this.MinMillisecondsForNodeToWait = MinMillisecondsForNodeToWait;
        this.MaxMillisecondsForNodeToWait = MaxMillisecondsForNodeToWait;
        this.TimeInUnsafeAreaTillInfected = TimeInUnsafeAreaTillInfected;
        this.StopTimers=false;

        if (PersoncovidState == 0)// if thread doesnt have covid
        {
            Image BlueImg = new Image("/bluestar10x10.png");
            IMGview = new ImageView(BlueImg); // sets image to the imageview;
            IMGview.setFitHeight(BlueImg.getHeight());
            IMGview.setFitWidth(BlueImg.getWidth());
        } else {
            Image RedImg = new Image("/redstar10x10.png");
            IMGview = new ImageView(RedImg); // sets image to the imageview;
            IMGview.setFitHeight(RedImg.getHeight());
            IMGview.setFitWidth(RedImg.getWidth());
        }

        Platform.runLater(() -> {
            App.root.getChildren().add(IMGview);
        });

    }

    @Override
    public void run() {
        // int randomtime;
        // Random rand = new Random();

        // Displaying the thread that is running
        System.out.println("A Human Being represented by a Thread allocated an ID of " + Thread.currentThread().getId() + " is spawned");

        long TotalWalkLifeTime = App.GameLifeTime * 1000; // 1 seconds
        
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() { // After 1 minute, this function runs
                
                StopTimers();
                
                if(Infected)
                    System.out.println("Thread Node with ID " + Thread.currentThread().getId() + " was infected");
                
                // After stopping all timers do the coding
                //System.out.println("Exiting System because lifetime of program has passed");

            }
        }, TotalWalkLifeTime);
        
        FirstTimeAssignRandomLocationToThread(); // First Time  
        new RandomTimTask().run(); // Start first timer in RandomTimTask class
        CheckIfLessThanOneMeter(); // Contains another timer that runs every 1ms and checks every 1ms if the node is inside the unsafe area (within the RandomTime)
    }

    public void StopTimers() {
        

        StopTimers=true;
        if (Infected ==false) // Ensures timer isnt canelled more than twice
        {
           
                CheckingInfectiontimer.cancel(); // Pause timer if the node finally got infected
                CheckingInfectiontimer.purge(); // Purge timer if node finally got infectedd
               
        }
        GameTimer.cancel();
        GameTimer.purge();
        ///System.out.println("da5alt hena");
        /*try {
            Thread.sleep(1000); // 1 second delay
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
        
    }
    class RandomTimTask extends TimerTask {
        public void run() {
            Random rand = new Random();
            
            RandomTime= rand.nextInt(MaxMillisecondsForNodeToWait-MinMillisecondsForNodeToWait)+MinMillisecondsForNodeToWait; // using rule rand.nextInt(max-min)+min
            //RandomTime=2000;
                   
            GameTimer.schedule(new RandomTimTask(), RandomTime);
            //Code down this statement runs every RandomTime seconds
            
            //System.out.println(RandomTime+" second has passed");              
            UpdateThreadArrayAndLocation();
           
        }
    }

    public synchronized void UpdateThreadArrayAndLocation() 
    {       
        MoveThread(); 
        
              
    }
    
    public synchronized void CheckIfLessThanOneMeter()
    {      
        
        if(Infected==false && PersonCovidState!=1) // If still not infected and it is not the covid node, continue timer(no need to start timer for covid node because it cant infect itself)
                                                    //This extra personcovidstate!=1 is just an optimization statement to prevent extra timers from executing which will take up from the memory
            {
            CheckingInfectiontimer.schedule( new TimerTask(){  
                public void run() { 
                        
                        NearCovidNode=false; 
                        //startTime=System.currentTimeMillis();
                        for( int i=1;i<=SafeDistanceMeters;i++) //SafeDistance =1
                            {
                            if(CurrentRowIndex+i<ArrayNRows)
                                {   
                                    if(App.GameArray[CurrentRowIndex+i][CurrentColumnIndex] ==App.ArrayState.HasCovidPerson && PersonCovidState!=1) //if near index has a covid person and, this person checking the other index is not a covid person
                                    {
                                        //System.out.println("There is a covid near by after this 0.5 second "); 
                                        NearCovidNode=true;                          
                                    }                          
                                    
                                }
                            }
                        for( int i=1;i<=SafeDistanceMeters;i++) //SafeDistance =1
                            {
                                if(CurrentRowIndex-i>=0)
                                {
                                    if (App.GameArray[CurrentRowIndex-i][CurrentColumnIndex] ==App.ArrayState.HasCovidPerson && PersonCovidState!=1)
                                    {
                                        //System.out.println("There is a covid near by after this 0.5 second "); 
                                        NearCovidNode=true;                         
                                    }
                                
                                }
                        }
                        
                        for( int i=1;i<=SafeDistanceMeters;i++) //SafeDistance =1
                        { 
                            if(CurrentColumnIndex-i>=0)
                            {
                                if (App.GameArray[CurrentRowIndex][CurrentColumnIndex-i] ==App.ArrayState.HasCovidPerson && PersonCovidState!=1)
                                {
                                    //System.out.println("There is a covid near by after this 0.5 second "); 
                                    NearCovidNode=true;
                                    
                                }
                            }
                        }
                       
                        for( int i=1;i<=SafeDistanceMeters;i++) //SafeDistance =1
                        {
                            if(CurrentColumnIndex+i<ArrayNColumns)
                            {
                                if (App.GameArray[CurrentRowIndex][CurrentColumnIndex+i] ==App.ArrayState.HasCovidPerson && PersonCovidState!=1)
                                {
                                    //System.out.println("There is a covid near by after this 0.5 second "); 
                                    NearCovidNode=true;
                                    
                                }

                            }
                        }     
                        
                        for( int i=1;i<=SafeDistanceMeters;i++) //SafeDistance =1
                        {
                            if(CurrentColumnIndex+i<ArrayNColumns && CurrentRowIndex+i<ArrayNRows)
                            {
                                if (App.GameArray[CurrentRowIndex+i][CurrentColumnIndex+i] ==App.ArrayState.HasCovidPerson && PersonCovidState!=1)
                                {
                                    //System.out.println("There is a covid near by after this 0.5 second "); 
                                    NearCovidNode=true;
                                    
                                }
                            }
                        }

                        for( int i=1;i<=SafeDistanceMeters;i++) //SafeDistance =1
                        {
                            if(CurrentColumnIndex-i>=0 && CurrentRowIndex+i<ArrayNRows)
                            {
                                if (App.GameArray[CurrentRowIndex+i][CurrentColumnIndex-i] ==App.ArrayState.HasCovidPerson && PersonCovidState!=1)
                                {
                                    //System.out.println("There is a covid near by after this 0.5 second "); 
                                    NearCovidNode=true;                     
                                }
                            }
                        }

                        for( int i=1;i<=SafeDistanceMeters;i++) //SafeDistance =1
                        {
                            if(CurrentRowIndex-i>=0 && CurrentColumnIndex+i<ArrayNColumns)
                            {
                                if (App.GameArray[CurrentRowIndex-i][CurrentColumnIndex+i] ==App.ArrayState.HasCovidPerson && PersonCovidState!=1)
                                {
                                    //System.out.println("There is a covid near by after this 0.5 second "); 
                                    NearCovidNode=true;
                                    
                                }
                            }
                        }

                        for( int i=1;i<=SafeDistanceMeters;i++) //SafeDistance =1
                        {
                            if(CurrentRowIndex-i>=0 && CurrentColumnIndex-i>=0)
                            {
                                if (App.GameArray[CurrentRowIndex-i][CurrentColumnIndex-i] ==App.ArrayState.HasCovidPerson && PersonCovidState!=1)
                                {
                                    //System.out.println("There is a covid near by after this 0.5 second ");                          
                                    NearCovidNode=true;
                                    
                                }
                            }       
                        }
                        if(NearCovidNode==true)
                            {
                                
                                elapsedtime= elapsedtime + 10;
                                //System.out.println("Found near covid so elapsed time is updated to "+elapsedtime);
                            }
                        //else         
                        //    elapsedtime=0;
                        
                            
                        //elapsedtime=elapsedtime+10;
                        //System.out.println("elapsed time is "+elapsedtime);
                            
                        if (elapsedtime>=TimeInUnsafeAreaTillInfected) 
                        {
                            //System.out.println("2 seconds passed where there is a near coivid SO INFECT THIS STAR and Infect turns true ");
                            Image OrangeStarImg = new Image("/orangestar10x10.png");
                            IMGview.setImage(OrangeStarImg); // Replace New Orange image into the node
                            Infected=true; // Mark this node as infected
                            try {
                                CheckingInfectiontimer.cancel(); //Pause timer if the node finally got infected
                                CheckingInfectiontimer.purge(); // Purge timer if node finally got infectedd
                            } catch (IllegalStateException  e) {
                                System.out.println("EXCEPTED FOUND");
                            }
                        }  
                        
                    }

                },0,  10 );//runs each 10 ms
            }
    }
    

    public void MoveThread()
    {
        //Get a Random number between 0 and 7, where each number represents a direction
        //If for example direction 1  , Check if the Place is free,
        // If free, move in direction 1 to go to the new index then update location
        // If not free, make the thread wait for some milliseconds for the other thread in that location to move then try again by entering the re calling the function again

        Random rand = new Random();
        int RandomDirection = rand.nextInt((8 - 0)) + 0 ; // gets random integers from 0 to 7
        //int RandomDirection=4;
        if(RandomDirection==0) // 0 represents down (South)
        {
            //System.out.println("CurrentRowIndex+1 is "+ (CurrentRowIndex+1));
            if(CurrentRowIndex+MetersToMoveInIndex<ArrayNRows)
            {
                if(App.GameArray[CurrentRowIndex+MetersToMoveInIndex][CurrentColumnIndex] == App.ArrayState.Free ) 
                {       
                        ResetIndexToFree();
                        if(PersonCovidState==0)
                        {
                            App.GameArray[CurrentRowIndex+MetersToMoveInIndex][CurrentColumnIndex]=App.ArrayState.HasNeutralPerson;
                            //System.out.println("Moved Down and Became NEUTRAL in i= "+(CurrentRowIndex+1)+ " and j= "+ (CurrentColumnIndex)); 
                        }
                        else if (PersonCovidState==1)
                        { 
                            App.GameArray[CurrentRowIndex+MetersToMoveInIndex][CurrentColumnIndex]=App.ArrayState.HasCovidPerson;
                            //System.out.println("Moved Down and Became COVID in i= "+(CurrentRowIndex+1)+ " and j= "+ (CurrentColumnIndex)); 
                        }
                       
                        CurrentRowIndex=CurrentRowIndex+MetersToMoveInIndex; // Update CurrentRowIndex
                        Platform.runLater(
                            () -> {
                            IMGview.setX(IMGview.getX());
                            IMGview.setY(IMGview.getY()+MetersToMoveInIndex*(500/ArrayNRows));
                            });
                }
                else{
                    //System.out.println("not a free place for the thread to go to");
                    if(StopTimers==false)
                    {
                        try { 
                            wait(100); // wait for 100ms then relinquish thread to try to access index again
                            UpdateThreadArrayAndLocation(); // Get into function again to re-try to move the thead
                        } catch (InterruptedException e)  {
                            System.out.println("Interrupt Exception --> "+ e);
                        }  
                    }              
                }
            }
            //else
                //System.out.println("out of array size"); //
        }

        else if (RandomDirection==1)//1 represents up (north)
        {
             //System.out.println("CurrentRowIndex-1 is "+ (CurrentRowIndex-1));
             if(CurrentRowIndex-MetersToMoveInIndex>=0)
             {
                 if(App.GameArray[CurrentRowIndex-MetersToMoveInIndex][CurrentColumnIndex] == App.ArrayState.Free ) 
                 {       
                         ResetIndexToFree();
                         if(PersonCovidState==0)
                         {
                             App.GameArray[CurrentRowIndex-MetersToMoveInIndex][CurrentColumnIndex]=App.ArrayState.HasNeutralPerson;
                            // System.out.println("Moved up and Became NEUTRAL in i= "+(CurrentRowIndex-1)+ " and j= "+ (CurrentColumnIndex)); 
                         }
                         else if (PersonCovidState==1)
                         { 
                             App.GameArray[CurrentRowIndex-MetersToMoveInIndex][CurrentColumnIndex]=App.ArrayState.HasCovidPerson;
                            // System.out.println("Moved up and Became COVID in i= "+(CurrentRowIndex-1)+ " and j= "+ (CurrentColumnIndex)); 
                         }
                        
                         CurrentRowIndex=CurrentRowIndex-MetersToMoveInIndex; // Update CurrentRowIndex
                         Platform.runLater(
                            () -> {
                            IMGview.setX(IMGview.getX());
                            IMGview.setY(IMGview.getY()-MetersToMoveInIndex*(500/ArrayNRows));
                            });
                 }
                 else{
                     //System.out.println("not a free place for the thread to go to");
                     if(StopTimers==false)
                     {
                            try { 
                                wait(100); // wait for 100ms then relinquish thread to try to access index again
                                UpdateThreadArrayAndLocation();// Get into function again to re-try to move the thead
                            } catch (InterruptedException e)  {
                                System.out.println("Interrupt Exception --> "+ e);
                            }
                        }
                     
                 }
             }
            // else
              //   System.out.println("the index you want to move is is below 0");
        }

        else if (RandomDirection==2)//2 represents left 
        {
             
             if(CurrentColumnIndex-MetersToMoveInIndex>=0)
             {
                 if(App.GameArray[CurrentRowIndex][CurrentColumnIndex-MetersToMoveInIndex] == App.ArrayState.Free ) 
                 {       
                         ResetIndexToFree();
                         if(PersonCovidState==0)
                         {
                             App.GameArray[CurrentRowIndex][CurrentColumnIndex-MetersToMoveInIndex]=App.ArrayState.HasNeutralPerson;
                             //System.out.println("Moved left and Became NEUTRAL in i= "+(CurrentRowIndex)+ " and j= "+ (CurrentColumnIndex-1)); 
                         }
                         else if (PersonCovidState==1)
                         { 
                             App.GameArray[CurrentRowIndex][CurrentColumnIndex-MetersToMoveInIndex]=App.ArrayState.HasCovidPerson;
                            // System.out.println("Moved left and Became COVID in i= "+(CurrentRowIndex)+ " and j= "+ (CurrentColumnIndex-1)); 
                         }
                        
                         CurrentColumnIndex=CurrentColumnIndex-MetersToMoveInIndex; // Update CurrentRowIndex
                         Platform.runLater(
                            () -> {
                            IMGview.setX(IMGview.getX()-MetersToMoveInIndex*(500/ArrayNColumns));
                            IMGview.setY(IMGview.getY());
                            });
                 }
                 else{
                     //System.out.println("not a free place for the thread to go to");
                        if(StopTimers==false)
                        {
                            try { 
                                wait(100); // wait for 100ms then relinquish thread to try to access index again
                                UpdateThreadArrayAndLocation();// Get into function again to re-try to move the thead
                            } catch (InterruptedException e)  {
                                System.out.println("Interrupt Exception --> "+ e);
                            }
                        }
                     
                 }
             }
            // else
              //   System.out.println("the index you want to move is is below 0");
        }

        else if (RandomDirection==3)//3 represents right 
        {
             
             if(CurrentColumnIndex+MetersToMoveInIndex<ArrayNColumns)
             {
                 if(App.GameArray[CurrentRowIndex][CurrentColumnIndex+MetersToMoveInIndex] == App.ArrayState.Free ) 
                 {       
                         ResetIndexToFree();
                         if(PersonCovidState==0)
                         {
                             App.GameArray[CurrentRowIndex][CurrentColumnIndex+MetersToMoveInIndex]=App.ArrayState.HasNeutralPerson;
                            // System.out.println("Moved right and Became NEUTRAL in i= "+(CurrentRowIndex)+ " and j= "+ (CurrentColumnIndex+1)); 
                         }
                         else if (PersonCovidState==1)
                         { 
                             App.GameArray[CurrentRowIndex][CurrentColumnIndex+MetersToMoveInIndex]=App.ArrayState.HasCovidPerson;
                            // System.out.println("Moved right and Became COVID in i= "+(CurrentRowIndex)+ " and j= "+ (CurrentColumnIndex+1)); 
                         }
                        
                         CurrentColumnIndex=CurrentColumnIndex+MetersToMoveInIndex; // Update CurrentRowIndex
                         Platform.runLater(
                            () -> {
                            IMGview.setX(IMGview.getX()+MetersToMoveInIndex*(500/ArrayNColumns));
                            IMGview.setY(IMGview.getY());
                            });
                 }
                 else{
                     //System.out.println("not a free place for the thread to go to");
                     if(StopTimers==false)
                     {
                         try { 
                             wait(100); // wait for 100ms then relinquish thread to try to access index again
                             UpdateThreadArrayAndLocation();// Get into function again to re-try to move the thead
                         } catch (InterruptedException e)  {
                             System.out.println("Interrupt Exception --> "+ e);
                         }
                     }
                     
                 }
             }
            // else
              //   System.out.println("the index you want to move is is below 0");
        }

        else if (RandomDirection==4)// 4 represents north west   
        {
             
             if(CurrentRowIndex-MetersToMoveInIndex>=0 && CurrentColumnIndex-MetersToMoveInIndex>=0)
             {
                 if(App.GameArray[CurrentRowIndex-MetersToMoveInIndex][CurrentColumnIndex-MetersToMoveInIndex] == App.ArrayState.Free ) 
                 {       
                         ResetIndexToFree();
                         if(PersonCovidState==0)
                         {
                             App.GameArray[CurrentRowIndex-MetersToMoveInIndex][CurrentColumnIndex-MetersToMoveInIndex]=App.ArrayState.HasNeutralPerson;
                             //System.out.println("Moved NortWest and Became NEUTRAL in i= "+(CurrentRowIndex-1)+ " and j= "+ (CurrentColumnIndex-1)); 
                         }
                         else if (PersonCovidState==1)
                         { 
                             App.GameArray[CurrentRowIndex-MetersToMoveInIndex][CurrentColumnIndex-MetersToMoveInIndex]=App.ArrayState.HasCovidPerson;
                            // System.out.println("Moved NortWest and Became COVID in i= "+(CurrentRowIndex-1)+ " and j= "+ (CurrentColumnIndex-1)); 
                         }
                        
                         CurrentRowIndex=CurrentRowIndex-MetersToMoveInIndex; // Update CurrentRowIndex
                         CurrentColumnIndex=CurrentColumnIndex-MetersToMoveInIndex; // Update CurrentColumnIndex
                         Platform.runLater(
                            () -> { 
                            IMGview.setX(IMGview.getX()-MetersToMoveInIndex*(500/ArrayNColumns));
                            IMGview.setY(IMGview.getY()-MetersToMoveInIndex*(500/ArrayNRows));
                            });
                 }
                 else{
                     //System.out.println("not a free place for the thread to go to");
                     if(StopTimers==false)
                     {
                         try { 
                             wait(100); // wait for 100ms then relinquish thread to try to access index again
                             UpdateThreadArrayAndLocation();// Get into function again to re-try to move the thead
                         } catch (InterruptedException e)  {
                             System.out.println("Interrupt Exception --> "+ e);
                         }
                     }
                     
                 }
             }
            // else
              //   System.out.println("the index you want to move is is below 0");
        }

        else if (RandomDirection==5)// 5 represents north east   
        {
             
             if(CurrentColumnIndex+MetersToMoveInIndex<ArrayNColumns && CurrentRowIndex-MetersToMoveInIndex>=0)
             {
                 if(App.GameArray[CurrentRowIndex-MetersToMoveInIndex][CurrentColumnIndex+MetersToMoveInIndex] == App.ArrayState.Free ) 
                 {       
                         ResetIndexToFree();
                         if(PersonCovidState==0)
                         {
                             App.GameArray[CurrentRowIndex-MetersToMoveInIndex][CurrentColumnIndex+MetersToMoveInIndex]=App.ArrayState.HasNeutralPerson;
                             //System.out.println("Moved NortEast and Became NEUTRAL in i= "+(CurrentRowIndex-1)+ " and j= "+ (CurrentColumnIndex+1)); 
                         }
                         else if (PersonCovidState==1)
                         { 
                             App.GameArray[CurrentRowIndex-MetersToMoveInIndex][CurrentColumnIndex+MetersToMoveInIndex]=App.ArrayState.HasCovidPerson;
                            // System.out.println("Moved NortEast and Became COVID in i= "+(CurrentRowIndex-1)+ " and j= "+ (CurrentColumnIndex+1)); 
                         }
                        
                         CurrentRowIndex=CurrentRowIndex-MetersToMoveInIndex; // Update CurrentRowIndex
                         CurrentColumnIndex=CurrentColumnIndex+MetersToMoveInIndex; // Update CurrentColumnIndex
                         Platform.runLater(
                            () -> { 
                            IMGview.setX(IMGview.getX()+MetersToMoveInIndex*(500/ArrayNColumns));
                            IMGview.setY(IMGview.getY()-MetersToMoveInIndex*(500/ArrayNRows));
                            });
                 }
                 else{
                     //System.out.println("not a free place for the thread to go to");
                     if(StopTimers==false)
                     {
                         try { 
                             wait(100); // wait for 100ms then relinquish thread to try to access index again
                             UpdateThreadArrayAndLocation();// Get into function again to re-try to move the thead
                         } catch (InterruptedException e)  {
                             System.out.println("Interrupt Exception --> "+ e);
                         }
                     }
                     
                 }
             }
            // else
              //   System.out.println("the index you want to move is is below 0");
        }

        else if (RandomDirection==6)// 6 represents south west   
        {
             
             if(CurrentRowIndex+MetersToMoveInIndex<ArrayNRows && CurrentColumnIndex-MetersToMoveInIndex>=0)
             {
                 if(App.GameArray[CurrentRowIndex+MetersToMoveInIndex][CurrentColumnIndex-MetersToMoveInIndex] == App.ArrayState.Free ) 
                 {       
                         ResetIndexToFree();
                         if(PersonCovidState==0)
                         {
                             App.GameArray[CurrentRowIndex+MetersToMoveInIndex][CurrentColumnIndex-MetersToMoveInIndex]=App.ArrayState.HasNeutralPerson;
                            // System.out.println("Moved SouthWest and Became NEUTRAL in i= "+(CurrentRowIndex+1)+ " and j= "+ (CurrentColumnIndex-1)); 
                         }
                         else if (PersonCovidState==1)
                         { 
                             App.GameArray[CurrentRowIndex+MetersToMoveInIndex][CurrentColumnIndex-MetersToMoveInIndex]=App.ArrayState.HasCovidPerson;
                            // System.out.println("Moved SouthWest and Became COVID in i= "+(CurrentRowIndex+1)+ " and j= "+ (CurrentColumnIndex-1)); 
                         }
                        
                         CurrentRowIndex=CurrentRowIndex+MetersToMoveInIndex; // Update CurrentRowIndex
                         CurrentColumnIndex=CurrentColumnIndex-MetersToMoveInIndex; // Update CurrentColumnIndex
                         Platform.runLater(
                            () -> {
                            IMGview.setX(IMGview.getX()-MetersToMoveInIndex*(500/ArrayNColumns));
                            IMGview.setY(IMGview.getY()+MetersToMoveInIndex*(500/ArrayNRows));
                            });
                 }
                 else{
                     //System.out.println("not a free place for the thread to go to");
                     if(StopTimers==false)
                     {
                         try { 
                             wait(100); // wait for 100ms then relinquish thread to try to access index again
                             UpdateThreadArrayAndLocation();// Get into function again to re-try to move the thead
                         } catch (InterruptedException e)  {
                             System.out.println("Interrupt Exception --> "+ e);
                         }
                     }
                     
                 }
             }
             //else
             //    System.out.println("the index you want to move is is below 0");
        }

        else if (RandomDirection==7)// 7 represents south east   
        {
             
             if(CurrentRowIndex+MetersToMoveInIndex<ArrayNRows && CurrentColumnIndex+MetersToMoveInIndex<ArrayNColumns)
             {
                 if(App.GameArray[CurrentRowIndex+MetersToMoveInIndex][CurrentColumnIndex+MetersToMoveInIndex] == App.ArrayState.Free ) 
                 {       
                         ResetIndexToFree();
                         if(PersonCovidState==0)
                         {
                             App.GameArray[CurrentRowIndex+MetersToMoveInIndex][CurrentColumnIndex+MetersToMoveInIndex]=App.ArrayState.HasNeutralPerson;
                            // System.out.println("Moved SouthEast and Became NEUTRAL in i= "+(CurrentRowIndex+1)+ " and j= "+ (CurrentColumnIndex+1)); 
                         }
                         else if (PersonCovidState==1)
                         { 
                             App.GameArray[CurrentRowIndex+MetersToMoveInIndex][CurrentColumnIndex+MetersToMoveInIndex]=App.ArrayState.HasCovidPerson;
                             //System.out.println("Moved SouthEast and Became COVID in i= "+(CurrentRowIndex+1)+ " and j= "+ (CurrentColumnIndex+1)); 
                         }
                        
                         CurrentRowIndex=CurrentRowIndex+MetersToMoveInIndex; // Update CurrentRowIndex
                         CurrentColumnIndex=CurrentColumnIndex+MetersToMoveInIndex; // Update CurrentColumnIndex
                         Platform.runLater(
                            () -> {
                            IMGview.setX(IMGview.getX()+MetersToMoveInIndex*(500/ArrayNColumns));
                            IMGview.setY(IMGview.getY()+MetersToMoveInIndex*(500/ArrayNRows));
                            });
                 }
                 else{
                     //System.out.println("not a free place for the thread to go to");
                     if(StopTimers==false)
                     {    
                        try { 
                             wait(100); // wait for 100ms then relinquish thread to try to access index again
                             UpdateThreadArrayAndLocation();// Get into function again to re-try to move the thead
                         } catch (InterruptedException e)  {
                             System.out.println("Interrupt Exception --> "+ e);
                         }
                     }
                     
                 }
             }
             //else
               //  System.out.println("the index you want to move is is below 0");
        }
    }

    public synchronized void ResetIndexToFree()
    {
        App.GameArray[CurrentRowIndex][CurrentColumnIndex]=App.ArrayState.Free;
    }

    public synchronized void  FirstTimeAssignRandomLocationToThread(){
        Random rand = new Random();
        RandomRowIndex = rand.nextInt((ArrayNRows - 0)) + 0 ; // using the rule random.nextInt(max - min) + min;
        RandomColumnIndex = rand.nextInt((ArrayNColumns - 0)) + 0;
        
        if(App.GameArray[RandomRowIndex][RandomColumnIndex]==App.ArrayState.Free)
        {
            CurrentRowIndex=RandomRowIndex;
            CurrentColumnIndex=RandomColumnIndex;
            if(PersonCovidState==0) 
                {
                    App.GameArray[RandomRowIndex][RandomColumnIndex]=App.ArrayState.HasNeutralPerson;
                    //System.out.println("FIRST State updated to NEUTRAL in i= "+RandomRowIndex+ " and j= "+ RandomColumnIndex);
                }
            else
                {App.GameArray[RandomRowIndex][RandomColumnIndex]=App.ArrayState.HasCovidPerson; 
                    //System.out.println("FIRST State updated to COVID in i= "+RandomRowIndex+ " and j= "+ RandomColumnIndex);  
                }    
        } 
        else
            {
                //System.out.println("NOT FREEEE");
                while (App.GameArray[RandomRowIndex][RandomColumnIndex]!=App.ArrayState.Free) // while not free, find new random location
                {
                    //Getting new random Index
                    RandomRowIndex = rand.nextInt((ArrayNRows - 0)) + 0 ; //this gets random numbers from 0 to 2 if arraysize =3
                    RandomColumnIndex = rand.nextInt((ArrayNColumns - 0)) + 0;
                }
                // if we reached this line, this means that the RandomLocationX and Y is free in the array
                CurrentRowIndex=RandomRowIndex;
                CurrentColumnIndex=RandomColumnIndex;
                if(PersonCovidState==0)
                            {
                                App.GameArray[RandomRowIndex][RandomColumnIndex]=App.ArrayState.HasNeutralPerson;
                                //System.out.println("State updated to NEUTRAL in i= "+RandomRowIndex+ " and j= "+ RandomColumnIndex); 
                            }
                else if (PersonCovidState==1)
                       { 
                           App.GameArray[RandomRowIndex][RandomColumnIndex]=App.ArrayState.HasCovidPerson;
                           // System.out.println("State updated to COVID in i= "+RandomRowIndex+ " and j= "+ RandomColumnIndex); 
                       }
            }
            
            CurrentXLocation= ((RandomColumnIndex+1)*(500/ArrayNColumns)) - (500/(2*ArrayNColumns)) ;
            CurrentYLocation= ((RandomRowIndex+1)*(500/ArrayNRows)) - (500/(2*ArrayNRows));
            Platform.runLater(
                () -> {
                    IMGview.setX(CurrentXLocation);
                    IMGview.setY(CurrentYLocation);
                    
                    }
                );

    }
    
    public boolean getInfectedState()
    {
        return Infected;
    }

}
