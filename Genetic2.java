/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genetic2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Genetic2 extends Application {
    Map map;
    int poolSize = 15;
    int[][] mat = matMap();
    ArrayList<Gene> pool = new ArrayList<>();
    int generation = 0;
    int generationsCount;
    
    Label gen = new Label(); //Pro train
    Label fit = new Label();
    int aproxFit = 0;
    
    Label totalGen = new Label(); //Pra main
    Label maxFit = new Label();
    
    ArrayList<Float> maxFitness = new ArrayList<>();
    ArrayList<Float> medFitness = new ArrayList<>();
    
    @Override
    public void start(Stage primaryStage) {
	map = new Map(mat, 600);
	
        Button train = new Button();
        train.setText("Train");
        train.setOnAction((ActionEvent event) -> {
	    Train();
	});
	Button test = new Button();
        test.setText("Test");
        test.setOnAction((ActionEvent event) -> {
	    Test();
	});
	Button reset = new Button();
        reset.setText("Reset");
        reset.setOnAction((ActionEvent event) -> {
	    Reset();
	});
	Button results = new Button();
        results.setText("Results");
        results.setOnAction((ActionEvent event) -> {
	    Results();
	});
	
	StackPane root = new StackPane();
	root.getChildren().add(test);
	test.setTranslateY(40);
	root.getChildren().add(train);
	train.setTranslateY(10);
	root.getChildren().add(results);
	results.setTranslateY(-20);
	root.getChildren().add(reset);
	reset.setTranslateY(-50);
	Scene menu = new Scene(root, 400, 300);
	primaryStage.setScene(menu);
	root.getChildren().add(totalGen);
	root.getChildren().add(maxFit);
	totalGen.setTranslateY(-95);
	totalGen.setText("Geração atual: " + generation);
	maxFit.setTranslateY(-80);
	maxFit.setText("Fitness alcançado: " + aproxFit);
	
	createPool();
	
	primaryStage.setTitle("Corrida Maluca");
	primaryStage.show();
    }
    
    private void Results() {
	int i;
	Stage stage = new Stage();
	stage.setTitle("Results");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);
                
        lineChart.setTitle("Resultados até geração " + generationsCount);
	
        XYChart.Series seriesmaxf = new XYChart.Series();
        seriesmaxf.setName("Fitness Máximo");
	for(i=0; i<maxFitness.size(); i++){
	    seriesmaxf.getData().add(new XYChart.Data(i, maxFitness.get(i)));
	}
	
	XYChart.Series seriesmedf = new XYChart.Series();
        seriesmedf.setName("Fitness Médio");
	for(i=0; i<maxFitness.size(); i++){
	    seriesmedf.getData().add(new XYChart.Data(i, medFitness.get(i)));
	}
        
        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(seriesmaxf);
	lineChart.getData().add(seriesmedf);
       
        stage.setScene(scene);
        stage.show();
    }
    
    public void Reset(){
	pool.clear();
	createPool();
	generation = 0;
	totalGen.setText("Geração atual: " + generation);
	aproxFit = 0;
	maxFit.setText("Fitness alcançado: " + aproxFit);
	maxFitness.clear();
	medFitness.clear();
    }
    
    public void createPool(){
	pool.ensureCapacity(poolSize);
	
	for(int i=0; i<poolSize; i++){
	    pool.add(new Gene());
	}
    }
    
    public void rearrangePool(ArrayList<Car> carros){
	carros.sort(new Comparator<Car>() {
	    @Override
	    public int compare(Car c1, Car c2) {
		return (int) ((int)c2.getFitness() - (int)c1.getFitness());
	    }
	});
	maxFitness.add(carros.get(0).getFitness());
	aproxFit = (int) carros.get(0).getFitness();
	fit.setText("Max Fitness = " + aproxFit);
	
	float med = 0;
	for(Car c : carros){
	    med += c.getFitness();
	}
	med = med/carros.size();
	medFitness.add(med);
	
	int i=0, size = poolSize;
	pool.clear();
	
	for(i=0; i<size; i++){
	    if(carros.get(i).getFitness() > 1)
		pool.add(carros.get(i).getGene());
	    else
		pool.add(new Gene());
	}
	
	for(i=0; i<2*size/3; i++){
	    pool.get(size - i - 1).Mutate();
	}
    }
    
    public void Train(){
	generationsCount = 0;
	ArrayList<Car> carros = new ArrayList<>();
	StackPane root = new StackPane();
	Scene trainScene = new Scene(root, 100, 200);
	Stage trainStage = new Stage();
	trainStage.setScene(trainScene);
	trainStage.setTitle("Training...");
	Button stop = new Button("Stop");
	
	Timeline timer = new Timeline(new KeyFrame(Duration.seconds(0.02), (ActionEvent e) -> {
	    int i, j, k;
	    int maxIterations = 300;
	    boolean finished = false;
	    
	    carros.clear();
	    for(j=0; j<poolSize; j++){
		carros.add(new Car(pool.get(j)));
	    }
	    
            for(j=0; j<carros.size(); j++){
                double fitness = 0;
                maxIterations = 500;
                while(maxIterations > 0){
                    finished = true;
                    carros.get(j).takeAction(map);
		    
                    if(map.checkCollision(carros.get(j).getpX(), carros.get(j).getpY())){
                        carros.get(j).Collide();
                    }
                    maxIterations--;
                }
	    }
	    rearrangePool(carros);
	    generationsCount++;
	    if(generationsCount >= 1000){
		stop.fire();
	    }
	    gen.setText("Generation: " + generationsCount);
	}));
	timer.setCycleCount(Timeline.INDEFINITE);
	
	stop.setOnAction((ActionEvent event) -> {
	    timer.stop();
	    trainStage.close();
	    generation += generationsCount;
	    totalGen.setText("Geração atual: " + generation);
	    maxFit.setText("Fitness alcançado: " + aproxFit);
	});
	root.getChildren().add(stop);
	gen.setTranslateY(40);
	root.getChildren().add(gen);
	fit.setTranslateY(55);
	root.getChildren().add(fit);
	fit.setText("Max Fitness = " + aproxFit);
	
	timer.play();
	
	trainStage.show();
    }
    
    public void Test(){
	int size = 600;
	Group root = new Group();
	Scene testScene = new Scene(root, size, size);
	Stage testStage = new Stage();
	testStage.setTitle("Test");
	testStage.setScene(testScene);
	ArrayList<Circle> carVisual = new ArrayList<>();
	ArrayList<Car> car = new ArrayList<>();
	int i, j;
	double auxSize = size/Map.msize;
	
	for(i=0; i<Map.msize; i++){
	    for(j=0; j<Map.msize; j++){
		Rectangle rec = new Rectangle(i*auxSize, j*auxSize, auxSize, auxSize);
		if(mat[i][j] == 1) rec.setFill(Color.WHITESMOKE);
		else if(mat[i][j] == 0) rec.setFill(Color.GREY);
		
		root.getChildren().add(rec);
	    }
	}
	
	for(i=0; i<5; i++){
	    Circle c = new Circle(0, 0, 10);
	    carVisual.add(c);
	    root.getChildren().add(c);
	    car.add(new Car(pool.get(i)));
	    if(i==0) c.setFill(Color.BROWN);
	    else if(i==1) c.setFill(Color.DARKMAGENTA);
	    else if(i==2) c.setFill(Color.GOLD);
	    else if(i==3) c.setFill(Color.YELLOWGREEN);
	    else if(i==4) c.setFill(Color.AQUAMARINE);
	}
	
	Timeline timer = new Timeline(new KeyFrame(Duration.seconds(0.1), (ActionEvent e) -> {
	    int k;
            for(k=0; k<car.size(); k++){
		Car c = car.get(k);
		c.takeAction(map);
		carVisual.get(k).setTranslateX(c.getpX());
		carVisual.get(k).setTranslateY(c.getpY());
		if(map.checkCollision(c.getpX(), c.getpY()))
		    c.Collide();
	    }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
	
	Button clear = new Button("Clear");
	Button run = new Button("Run");
	run.setOnAction((ActionEvent event) -> { run.setDisable(true); timer.play(); });
	clear.setOnAction((ActionEvent event) -> { timer.stop();  run.setDisable(false);});
	run.setTranslateX(20);
	run.setTranslateY(20);
	clear.setTranslateX(100);
	clear.setTranslateY(20);
	root.getChildren().add(run);
	root.getChildren().add(clear);
	
	testStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private int[][] matMap() {
	int[][] mat = new int[10][10];

	mat[0][0] = 0; mat[0][1] = 0; mat[0][2] = 0; mat[0][3] = 0; mat[0][4] = 0;mat[0][5] = 0; mat[0][6] = 0; mat[0][7] = 0; mat[0][8] = 0; mat[0][9] = 0;
	mat[1][0] = 0; mat[1][1] = 1; mat[1][2] = 1; mat[1][3] = 1; mat[1][4] = 0;mat[1][5] = 0; mat[1][6] = 1; mat[1][7] = 1; mat[1][8] = 1; mat[1][9] = 0;
	mat[2][0] = 0; mat[2][1] = 1; mat[2][2] = 0; mat[2][3] = 1; mat[2][4] = 1;mat[2][5] = 1; mat[2][6] = 1; mat[2][7] = 0; mat[2][8] = 1; mat[2][9] = 0;
	mat[3][0] = 0; mat[3][1] = 1; mat[3][2] = 1; mat[3][3] = 1; mat[3][4] = 1;mat[3][5] = 1; mat[3][6] = 1; mat[3][7] = 0; mat[3][8] = 0; mat[3][9] = 0;
	mat[4][0] = 0; mat[4][1] = 1; mat[4][2] = 1; mat[4][3] = 1; mat[4][4] = 1;mat[4][5] = 1; mat[4][6] = 1; mat[4][7] = 1; mat[4][8] = 1; mat[4][9] = 0;
	mat[5][0] = 0; mat[5][1] = 1; mat[5][2] = 0; mat[5][3] = 0; mat[5][4] = 1;mat[5][5] = 1; mat[5][6] = 0; mat[5][7] = 0; mat[5][8] = 1; mat[5][9] = 0;
	mat[6][0] = 0; mat[6][1] = 1; mat[6][2] = 1; mat[6][3] = 1; mat[6][4] = 1;mat[6][5] = 1; mat[6][6] = 0; mat[6][7] = 0; mat[6][8] = 1; mat[6][9] = 0;
	mat[7][0] = 0; mat[7][1] = 1; mat[7][2] = 1; mat[7][3] = 1; mat[7][4] = 1;mat[7][5] = 1; mat[7][6] = 1; mat[7][7] = 1; mat[7][8] = 1; mat[7][9] = 0;
	mat[8][0] = 0; mat[8][1] = 1; mat[8][2] = 1; mat[8][3] = 1; mat[8][4] = 1;mat[8][5] = 1; mat[8][6] = 1; mat[8][7] = 1; mat[8][8] = 1; mat[8][9] = 0;
	mat[9][0] = 0; mat[9][1] = 0; mat[9][2] = 0; mat[9][3] = 0; mat[9][4] = 0;mat[9][5] = 0; mat[9][6] = 0; mat[9][7] = 0; mat[9][8] = 0; mat[9][9] = 0;
	
	return mat;
    }
}

/*private int[][] matMap() {
	int[][] mat = new int[5][5];
	
	mat[0][0] = 0; mat[0][1] = 0; mat[0][2] = 0; mat[0][3] = 0; mat[0][4] = 0;
	mat[1][0] = 0; mat[1][1] = 1; mat[1][2] = 1; mat[1][3] = 1; mat[1][4] = 0;
	mat[2][0] = 0; mat[2][1] = 1; mat[2][2] = 0; mat[2][3] = 1; mat[2][4] = 0;
	mat[3][0] = 0; mat[3][1] = 1; mat[3][2] = 1; mat[3][3] = 1; mat[3][4] = 0;
	mat[4][0] = 0; mat[4][1] = 0; mat[4][2] = 0; mat[4][3] = 0; mat[4][4] = 0;
	
	return mat;
    }

public void Train(){
	int i, j, k;
	int generationsCount = 100;
	ArrayList<Car> carros = new ArrayList<>();
	//Cria palco com barra de progresso
	Stage trainStage = new Stage();
	trainStage.show();
	
	for(i=0; i<generationsCount; i++){
	    int maxIterations = 100;
	    boolean finished = false;
	    
	    carros.clear();
	    for(j=0; j<poolSize; j++){
		carros.add(new Car(pool.get(j)));
	    }
	    
            for(j=0; j<carros.size(); j++){
                double fitness = 0;
                maxIterations = 500;
                while(maxIterations > 0){
                    finished = true;
                    carros.get(j).takeAction(map);
		    
                    if(map.checkCollision(carros.get(j).getpX(), carros.get(j).getpY())){
                        carros.get(j).Collide();
                    }
                    maxIterations--;
                }
	    }
	    rearrangePool(carros);
	}
	
	trainStage.close();
    }

*/