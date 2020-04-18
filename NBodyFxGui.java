

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * @author ashrafhossain
 * <p>
 * Note that javafx uses a coordinate system with origin top left.
 */
public class NBodyFxGui extends Application {
    /** Number of seconds to update the model with for each iteration (delta T) */
    public static final double TIME_SLICE = 60000*30000;
    
    /** initial scale pixel/meter */
    public static final double INITIAL_SCALE = 1e3;

    /** radius in pixels of body in gui */
    public static final double BODY_RADIUS_GUI = 2;

    private static final int BOTTOM_AREA_HEIGHT = 100;
    
    
    private static final int SEC_IN_MINUTE = 60;
    private static final int SEC_IN_HOUR = SEC_IN_MINUTE * 60;
    private static final int SEC_IN_DAY = SEC_IN_HOUR * 24;
    private static final int SEC_IN_YEAR = 31556926;
    private long elapsedSeconds = 0;
    
    
    
	public int frames = 10;
	public String title = "My App";

	public static double width = 1020;
	public static double height = 780;
	


	

    /** bodies in system rendered by gui */
    private BodySystem bodySystem;

    /** transforms between coordinates in model and coordinates in gui */
    private CoordinatesTransformer transformer = new CoordinatesTransformer();
    
	Canvas canvas = createCanvas();
	GraphicsContext gc = canvas.getGraphicsContext2D();
	

    /** utility for counting frames per second */
    private FPSCounter fps = new FPSCounter();

    private double canvasWidth = 0;
    private double canvasHeight = 0;
    private Vector3D dragPosStart;
    private Label timeLabel;
    private Label fpsLabel;
    private Label scaleLabel;
    
	int particleNumber = 0;
	
	Planet[] planets;
	double[] xForces;  
	double[] yForces;
    
	private Text timeText = new Text();
	private Text fpsText = new Text();
//    
//	int particleNumber = 3;
//	
//	Planet[] planets = new Planet[particleNumber];
//	double[] xForces = new double[particleNumber];  
//	double[] yForces = new double[particleNumber];
	
	

    @Override
    public void start(Stage stage) throws IOException {
        //createBodies();
    	
    	canvas.setHeight(height);
		canvas.setWidth(width);
    	
    	initBodies();
        
        
        transformer.setScale(INITIAL_SCALE);
        transformer.setOriginXForOther(500);
        transformer.setOriginYForOther(500);
        
        timeText.setFont(Font.font("verdana",FontWeight.BOLD, FontPosture.REGULAR, 15));
        timeText.setFill(Color.RED);
        
        fpsText.setFont(Font.font("verdana",FontWeight.BOLD, FontPosture.REGULAR, 15));
        fpsText.setFill(Color.RED);
        
  
        //GraphicsContext gc = createGui(stage);
       // gc.setFill(Color.BLACK);
        
//		planets[0] = new Planet(400,250,0,0, 1E+15);
//		planets[1] = new Planet(400,200,20,0, 1);
//		planets[2] = new Planet(400,300,-20,0, 2);
        
        BorderPane border = new BorderPane(canvas);
        createTimeLabel();
        createFPSLabel();
        createScaleLabel();
        HBox hbox = new HBox();
        
        border.setBottom(hbox);
        //border.setCenter(canvas);
        
        
        StackPane root = new StackPane(canvas);
        
        //timeText.setTextAlignment(TextAlignment.LEFT);
        
        root.getChildren().addAll(timeText, fpsText);
        StackPane.setAlignment(timeText,Pos.TOP_LEFT);
        StackPane.setAlignment(fpsText, Pos.TOP_CENTER);
        
        
		stage.setTitle("SpaceBum");
		stage.setScene(new Scene(root, width, height));
		//stage.setScene(border);
		stage.show();

		canvas.requestFocus();
		KeyFrame frame = new KeyFrame(Duration.millis(1000 / frames), e -> updateFrame());
		Timeline timeline = new Timeline(frame);
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
		
		
//        Timeline timeline = new Timeline();
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        KeyFrame kf = new KeyFrame(
//                Duration.millis(1000 / 30),
//                new EventHandler<ActionEvent>() {
//                    public void handle(ActionEvent ae) {
//                        updateFrame(gc);            
//                    }
//                });
//        timeline.getKeyFrames().add(kf);
//        timeline.play();
//        stage.show();
    }

    /**
     * Draw single frame
     *
     * @param gc
     */
    protected void updateFrame() {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, width, height);
 //       gc.clearRect(0, 0, canvasWidth, canvasHeight);

//        for (Body body : bodySystem.getBodies()) {
//
//
//            double otherX = transformer.modelToOtherX(body.location.x);
//            double otherY = transformer.modelToOtherY(body.location.y);
//
//            // draw object circle
//            gc.setFill(Color.BLACK);
//            gc.fillOval(otherX - BODY_RADIUS_GUI, otherY - BODY_RADIUS_GUI, BODY_RADIUS_GUI * 2, BODY_RADIUS_GUI * 2);
//
//            // draw label
//            //Text text = new Text(body.name);
//            //gc.fillText(body.name, otherX - (text.getLayoutBounds().getWidth() / 2), otherY - BODY_RADIUS_GUI - (text.getLayoutBounds().getHeight() / 2));
//        }
        
        
		
		
        for (int i = 0; i < particleNumber; i++){
            xForces[i] = planets[i].calcNetForceExertedByX(planets);
            yForces[i] = planets[i].calcNetForceExertedByY(planets);
            
            //System.out.println(xForces[i]);
        }

        /**
         * Updates each planet's position and velocity
         */
        for (int i = 0; i < particleNumber; i++){
            planets[i].update(TIME_SLICE, xForces[i], yForces[i]);
            planets[i].draw(gc, transformer);
        }
        
        
        
        timeText.setText(getElapsedTimeAsString());
        fpsText.setText("FPS: "+ fps.countFrame());
        
        elapsedSeconds += TIME_SLICE;
        

        //bodySystem.update(TIME_SLICE);
        //timeLabel.setText(bodySystem.getElapsedTimeAsString());
        //fpsLabel.setText("FPS: " + fps.countFrame());
        //scaleLabel.setText(String.format("Scale: %d km/pixel", Math.round(transformer.getScale()/1000)));
    }

//    protected void createBodies() throws IOException {
//        this.bodySystem = new SolarSystem();
//    }

    private GraphicsContext createGui(Stage stage) {
        BorderPane border = new BorderPane();
        createTimeLabel();
        createFPSLabel();
        createScaleLabel();
        HBox hbox = createHBox();
        border.setBottom(hbox);
        Canvas canvas = createCanvas();
        border.setCenter(canvas);
        stage.setTitle("NBody simulation");
        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setMaximized(true);

        // Bind canvas size to stack pane size.
        canvas.widthProperty().bind(stage.widthProperty());
        canvas.heightProperty().bind(stage.heightProperty().subtract(BOTTOM_AREA_HEIGHT));
        return canvas.getGraphicsContext2D();
    }

    private Canvas createCanvas() {
        Canvas canvas = new ResizableCanvas();

        // dragging of map
        canvas.setOnDragDetected((event) -> this.dragPosStart = new Vector3D(event.getX(), event.getY(), 0));
        canvas.setOnMouseDragged((event) -> {
            if (this.dragPosStart != null) {
                Vector3D dragPosCurrent = new Vector3D(event.getX(), event.getY(), 0);
                dragPosCurrent.sub(this.dragPosStart);
                dragPosStart = new Vector3D(event.getX(), event.getY(), 0);
                transformer.setOriginXForOther(transformer.getOriginXForOther() + dragPosCurrent.x);
                transformer.setOriginYForOther(transformer.getOriginYForOther() + dragPosCurrent.y);
            }
        });
        canvas.setOnMouseReleased((event) -> this.dragPosStart = null);

        // zooming (scaling)
        canvas.setOnScroll((event) -> {
            if (event.getDeltaY() > 0) {
                transformer.setScale(transformer.getScale() * 0.9);
            } else {
                transformer.setScale(transformer.getScale() * 1.1);
            }
        });
        return canvas;
    }

    private HBox createHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);   // Gap between nodes
        hbox.setStyle("-fx-background-color: #336699;");
        hbox.setFillHeight(true);
        hbox.getChildren().add(this.timeLabel);
        hbox.getChildren().add(this.fpsLabel);
        hbox.getChildren().add(this.scaleLabel);
        return hbox;
    }

    private void createTimeLabel() {
        timeLabel = new Label();
        timeLabel.setPrefSize(500, 20);
    }

    private void createFPSLabel() {
        fpsLabel = new Label();
        fpsLabel.setPrefSize(100, 20);
    }

    private void createScaleLabel() {
        scaleLabel = new Label();
        scaleLabel.setPrefSize(300, 20);
    }
    
    private void initBodies() throws NumberFormatException, IOException {
    	File file = new File("plummer_data.txt"); 
  	  
  	BufferedReader br = new BufferedReader(new FileReader(file)); 
  	  
  	  String st;
  	  int flag= 0;
  	  int n = 0;
  	  
  	  while ((st = br.readLine()) != null) {
  		  if(flag == 0) {
  			  
  			  particleNumber = Integer.parseInt(st.trim());
  			  planets = new Planet[particleNumber];
  			  xForces = new double[particleNumber];
  			  yForces = new double[particleNumber];
  			  
  		  }
  		  else {
  			  String[] values = st.split(" ");
  			  
  			  double mass = Double.parseDouble(values[0].trim());
  			  double x = Double.parseDouble(values[1].trim());
  			  double y = Double.parseDouble(values[2].trim());
  			  double z = Double.parseDouble(values[3].trim());
  			  double vx = Double.parseDouble(values[4].trim());
  			  double vy = Double.parseDouble(values[5].trim());
  			  double vz = Double.parseDouble(values[6].trim());
  			  
  			  Planet body = new Planet(x, y,vx,vy,mass);
  			  planets[flag-1] = body;
  			  
  			  
  		  }
  		  
  		  flag++;
  		  
  	  }
    }
    
    public String getElapsedTimeAsString() {
        long years = elapsedSeconds / SEC_IN_YEAR;
        long days = (elapsedSeconds % SEC_IN_YEAR) / SEC_IN_DAY;
//        long hours = ( (elapsedSeconds % SEC_IN_YEAR) % SEC_IN_DAY) / SEC_IN_HOUR;
//        long minutes = ( ((elapsedSeconds % SEC_IN_YEAR) % SEC_IN_DAY) % SEC_IN_HOUR) / SEC_IN_MINUTE;
//        long seconds = ( ((elapsedSeconds % SEC_IN_YEAR) % SEC_IN_DAY) % SEC_IN_HOUR) % SEC_IN_MINUTE;
        return String.format("Years:%08d, Days:%03d", years, days);
    }
    
    

    public static void main(String[] args) {
        launch(args);
    }
}
