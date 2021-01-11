import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameThread implements Runnable {

    private Thread myThreads[];
    private int Npeople;
    private int ArrayNRows;
    private int ArrayNColumns;
    private int SafeDistanceMeters;
    private int NCovidPeople;
    private long GameLifeTime;
    private int MetersToMoveInIndex;
    private int MinMillisecondsForNodeToWait;
    private int MaxMillisecondsForNodeToWait;
    private int TimeInUnsafeAreaTillInfected;
    public static boolean SimulationDone; 
    public static ArrayList<HumanBeingThread> HumanBeingsArrayList;

    public GameThread(int ArrayNRows, int ArrayNColumns, int NPeople, int SafeDistanceMeters, int NCovidPeople,
            long GameLifeTime, int MetersToMoveInIndex, int MinMillisecondsForNodeToWait,
            int MaxMillisecondsForNodeToWait, int TimeInUnsafeAreaTillInfected) {

        this.Npeople = NPeople;
        this.ArrayNRows = ArrayNRows;
        this.ArrayNColumns = ArrayNColumns;
        this.SafeDistanceMeters = SafeDistanceMeters;
        this.NCovidPeople = NCovidPeople;
        this.GameLifeTime = GameLifeTime;
        this.MetersToMoveInIndex = MetersToMoveInIndex;
        this.MinMillisecondsForNodeToWait = MinMillisecondsForNodeToWait;
        this.MaxMillisecondsForNodeToWait = MaxMillisecondsForNodeToWait;
        this.TimeInUnsafeAreaTillInfected = TimeInUnsafeAreaTillInfected;
        this.HumanBeingsArrayList = new ArrayList<HumanBeingThread>();
        this.SimulationDone=false;
        myThreads = new Thread[Npeople];

    }

    @Override
    public void run() {

        
        long TotalWalkLifeTime = GameLifeTime * 1000; // 1 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
        boolean NoNodesWereInfected=true;
            public void run() { // After 1 minute, this function runs
                SimulationDone=true;
                // System.exit(0);
                for (int i = 0; i < HumanBeingsArrayList.size(); i++) 
                    {              
                    HumanBeingsArrayList.get(i).StopTimers();
                    if(HumanBeingsArrayList.get(i).getInfectedState() ==true)
                        NoNodesWereInfected=false;
                    }
                if(NoNodesWereInfected==true)    
                    System.out.println("No Nodes were infected");
                   //System.out.println("Exiting System because lifetime of program has passed");
                
               
               }
         },  TotalWalkLifeTime );
        
        
        for (int j = 0; j < Npeople; j++) {

            if (j<Npeople-NCovidPeople) // The people not having covid
            {
                HumanBeingThread NewThreadHuman=new HumanBeingThread(0,ArrayNRows,ArrayNColumns,SafeDistanceMeters,MetersToMoveInIndex,MinMillisecondsForNodeToWait,MaxMillisecondsForNodeToWait,TimeInUnsafeAreaTillInfected); // 0 means person not having covid
                HumanBeingsArrayList.add(NewThreadHuman);
                myThreads[j] = new Thread(NewThreadHuman); 
                myThreads[j].start();
            }
            else // the 3 people having covid
            {
                HumanBeingThread NewThreadHuman=new HumanBeingThread(1,ArrayNRows,ArrayNColumns,SafeDistanceMeters,MetersToMoveInIndex,MinMillisecondsForNodeToWait,MaxMillisecondsForNodeToWait,TimeInUnsafeAreaTillInfected); // 1 means person having covid
                HumanBeingsArrayList.add(NewThreadHuman);
                myThreads[j] = new Thread(NewThreadHuman); 
                myThreads[j].start();
            }
            
        }
    }
    boolean Get_SimulationStatus()
    {
        return SimulationDone;
    }

    
}
