
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
/*import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;*/
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

 
public class App extends Application {
    
    public static Pane root; 
    public static ArrayState[][] GameArray; // to be manipulated by the threads
    enum ArrayState{Free,HasNeutralPerson,HasCovidPerson};
    int NPeople; // must be int in slider
    int ArrayNRows;// must be int in slider
    int ArrayNColumns;//must be int in slider
    static int GameLifeTime; // Total LengthOfWalkSlidergth of the walk 
    int SafeDistanceMeters; // minimum 1 meters and go uo to 5 meters, must be INT in slider not decimal 
    int NCovidPeople; // NCovidPeople USING PERCENTAGE NEB2a Ne3mlha
    int MetersToMoveInIndex; // must be int in slider
    int MinMillisecondsForNodeToWait;// in milliseconds from 50ms to 500ms from slider
    int MaxMillisecondsForNodeToWait;// in milliseconds 550 to 2000ms from slider
    int TimeInUnsafeAreaTillInfected; // in milliseconds

    boolean GameStart=false;
    public Thread myThreads[];
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        

        primaryStage.setTitle("Covid Tracker");
       
        Button btn = new Button( "Start Simulation");
        Slider X = new Slider(); // X corrodinate slide r
        Slider Y = new Slider(); // Y corrodinate slider
        Slider PercentageNCovidSlider = new Slider(); // percentage slider
        Slider TotalNumberOfNodesSlider = new Slider(); // number of TotalNumberOfNodesSlider slider
        Slider LengthOfWalkSlider = new Slider(); // LengthOfWalkSlidergth slider
        Slider starttime = new Slider(); // start time slider
        Slider endtime = new Slider(); // end time slider
        Slider distance = new Slider(); // distance to move
        Slider safeD = new Slider(); // safe distance without infection
        Slider TimeUntilInfectionSlider = new Slider(); // time in infected area until

        Label labelX = new Label("Choose X(number of columns) in the grid");
        Label labelY = new Label("Choose Y(number of rows) in the grid");
        Label label1 = new Label("Choose Percentage of Covid People from the total number of People");
        Label label2 = new Label("Choose Total Number of People");
        Label label3 = new Label("Total Length of Walk in Seconds");
        Label label4 = new Label("Choose the Minimum time in Milliseconds to wait after each move ");
        Label label5 = new Label("Choose the Maximum time in Milliseconds to wait after each move");
        Label label6 = new Label("Choose Distance in Meters that each person will move (Integer)");
        Label label7 = new Label("Choose Safe Distance in Meters between each person and the covid (Integer)");
        Label label8 = new Label("Choose time in Milliseconds that each person can stay until they get infected");

        X.setShowTickLabels(true);
        X.setShowTickMarks(true);

        Y.setShowTickLabels(true);
        Y.setShowTickMarks(true);

       /* PercentageCovidSlider.setShowTickLabels(true);
        PercentageCovidSlider.setShowTickMarks(true);
        */
        TotalNumberOfNodesSlider.setShowTickLabels(true);
        TotalNumberOfNodesSlider.setShowTickMarks(true);

        LengthOfWalkSlider.setShowTickLabels(true);
        LengthOfWalkSlider.setShowTickMarks(true);

        starttime.setShowTickLabels(true);
        starttime.setShowTickMarks(true);

        endtime.setShowTickLabels(true);
        endtime.setShowTickMarks(true);

        distance.setShowTickLabels(true);
        distance.setShowTickMarks(true);

        safeD.setShowTickLabels(true);
        safeD.setShowTickMarks(true);

        TimeUntilInfectionSlider.setShowTickLabels(true);
        TimeUntilInfectionSlider.setShowTickMarks(true);

        X.setMin(1);
        X.setMax(50);
        X.setValue(30);
        X.setBlockIncrement(1);
        X.setMajorTickUnit(1);
        X.setMinorTickCount(0);
        X.setShowTickLabels(true);
        X.setSnapToTicks(true);

        Y.setMin(1);
        Y.setMax(50);
        Y.setValue(30);
        Y.setBlockIncrement(1);
        Y.setMajorTickUnit(1);
        Y.setMinorTickCount(0);
        Y.setShowTickLabels(true);
        Y.setSnapToTicks(true);

        PercentageNCovidSlider.setMin(10);
        PercentageNCovidSlider.setMax(100);
        PercentageNCovidSlider.setBlockIncrement(1);
        PercentageNCovidSlider.setValue(10);
        PercentageNCovidSlider.setMajorTickUnit(10);
        PercentageNCovidSlider.setMinorTickCount(0);
        PercentageNCovidSlider.setShowTickLabels(true);
        
        TotalNumberOfNodesSlider.setMin(1);
        TotalNumberOfNodesSlider.setMax(50);
        TotalNumberOfNodesSlider.setValue(30);
        TotalNumberOfNodesSlider.setBlockIncrement(1);
        TotalNumberOfNodesSlider.setMajorTickUnit(1);
        TotalNumberOfNodesSlider.setMinorTickCount(0);
        TotalNumberOfNodesSlider.setShowTickLabels(true);
        TotalNumberOfNodesSlider.setSnapToTicks(true);

        LengthOfWalkSlider.setMin(10);
        LengthOfWalkSlider.setMax(180);
        LengthOfWalkSlider.setBlockIncrement(1);
        LengthOfWalkSlider.setValue(60);
        LengthOfWalkSlider.setMajorTickUnit(10);
        LengthOfWalkSlider.setMinorTickCount(0);
        LengthOfWalkSlider.setShowTickLabels(true);

        starttime.setMin(100);
        starttime.setMax(950);
        starttime.setBlockIncrement(1);
        starttime.setValue(500);
        starttime.setMajorTickUnit(50);
        starttime.setMinorTickCount(0);
        starttime.setShowTickLabels(true);
        //starttime.setSnapToTicks(true);

        endtime.setMin(1000);
        endtime.setMax(3000);
        endtime.setBlockIncrement(1);
        endtime.setValue(1000);
        endtime.setMajorTickUnit(500);
        endtime.setMinorTickCount(0);
        endtime.setShowTickLabels(true);

        distance.setMin(1);
        distance.setMax(30);
        distance.setValue(1);
        distance.setBlockIncrement(1);
        distance.setMajorTickUnit(1);
        distance.setMinorTickCount(0);
        distance.setShowTickLabels(true);
        distance.setSnapToTicks(true);

        safeD.setMin(1);
        safeD.setMax(30);
        safeD.setValue(1);
        safeD.setBlockIncrement(1);
        safeD.setMajorTickUnit(1);
        safeD.setMinorTickCount(0);
        safeD.setShowTickLabels(true);
        safeD.setSnapToTicks(true);

        TimeUntilInfectionSlider.setMin(1000);
        TimeUntilInfectionSlider.setMax(5000);
        TimeUntilInfectionSlider.setBlockIncrement(1);
        TimeUntilInfectionSlider.setValue(2000);
        TimeUntilInfectionSlider.setMajorTickUnit(500);
        TimeUntilInfectionSlider.setMinorTickCount(0);
        TimeUntilInfectionSlider.setShowTickLabels(true);

        //get value of each slider

        //System.out.println("lets goooooooooooo");

        VBox layout1 = new VBox();

        Insets insets= new Insets(50,50,0,50);
		layout1.setPadding(insets); 
        //layout1.setSpacing(10); 
        
        layout1.getChildren().addAll( labelX, X,  labelY,Y,label2, TotalNumberOfNodesSlider,label1,PercentageNCovidSlider,label3, LengthOfWalkSlider,label4, starttime,label5, endtime,label6, distance,  label7,safeD,label8, TimeUntilInfectionSlider,btn );

       

        root=new Pane();
        
        Scene Pre = new Scene(layout1,800,700);
        Scene Post = new Scene(root,500,500);
    
        btn.setOnAction(e ->
        {

            ArrayNColumns= (int) X.getValue(); // x coordinate
            ArrayNRows = (int) Y.getValue(); // x coordinate

            NPeople = (int) TotalNumberOfNodesSlider.getValue(); // TotalNumberOfNodesSlider value
            NCovidPeople=(int) (NPeople*PercentageNCovidSlider.getValue()/100);
            //System.out.println("Ncovic people = "+NCovidPeople);
            MinMillisecondsForNodeToWait = (int) starttime.getValue(); // start time value
            MaxMillisecondsForNodeToWait = (int) endtime.getValue(); // start time value
            MetersToMoveInIndex  = (int) distance.getValue(); // distanc// e to move from each point
            SafeDistanceMeters = (int) safeD.getValue(); // safe distance to move
            TimeInUnsafeAreaTillInfected = (int) TimeUntilInfectionSlider.getValue(); // start time value
            GameLifeTime=(int) LengthOfWalkSlider.getValue();
            
            if ( NPeople > ArrayNRows*ArrayNColumns){
                System.out.println("The Total number of People you choose are more than the Grid size(X * Y). Please try again");
            }
            else if ( MetersToMoveInIndex > ArrayNColumns || MetersToMoveInIndex > ArrayNRows){
                System.out.println("The Distance Meters is either more than X or Y");
            }
            
            else if ( SafeDistanceMeters > ArrayNColumns || SafeDistanceMeters> ArrayNRows){
                System.out.println("The Safe Distance is either more than X or Y ");
            }
           else{
                GameStart = true;
                GameArray = new ArrayState[ArrayNRows][ArrayNColumns];// 2d array containing 100 squares in width and height while actual game is

                for(int i=0;i<ArrayNRows;i++)
                    for(int j=0;j<ArrayNColumns;j++)
                    {
                        GameArray[i][j]=ArrayState.Free;
                    }
                Thread TheGameThread = new Thread(new GameThread(ArrayNRows,ArrayNColumns,NPeople,SafeDistanceMeters,NCovidPeople,GameLifeTime,MetersToMoveInIndex,MinMillisecondsForNodeToWait,MaxMillisecondsForNodeToWait,TimeInUnsafeAreaTillInfected));
                TheGameThread.start();
                primaryStage.setScene(Post);

                
            }
        }
        );    
            
        primaryStage.setScene(Pre);
        primaryStage.setResizable(false);//disables maximize window
        primaryStage.show();
       

                
    }


    @Override
    public void stop() {
        // executed when the application shuts down
        if(GameStart==true && GameThread.SimulationDone==false)
            for(int i=0; i<GameThread.HumanBeingsArrayList.size();i++)
            {
                GameThread.HumanBeingsArrayList.get(i).StopTimers();
            }
        
    }



}