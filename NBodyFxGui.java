

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * @author ashrafhossain
 * <p>
 * Note that javafx uses a coordinate system with origin top left.
 */
public class NBodyFxGui extends Application {
    /** Number of seconds to update the model with for each iteration (delta T) */
    public static final double TIME_SLICE = 0.1;
    
    /** initial scale pixel/meter */
    public static final double INITIAL_SCALE = 1E04;
    
    public static final double Radius = 2.83800E06;

    /** radius in pixels of body in gui */
    public static final double BODY_RADIUS_GUI = 2;

    
    private static final int SEC_IN_MINUTE = 60;
    private static final int SEC_IN_HOUR = SEC_IN_MINUTE * 60;
    private static final int SEC_IN_DAY = SEC_IN_HOUR * 24;
    private static final int SEC_IN_YEAR = 31556926;
    private long elapsedSeconds = 0;
    
    
    
	public int frames = 10;

	public static double width = 1020;
	public static double height = 780;
	

    /** transforms between coordinates in model and coordinates in gui */
    private CoordinatesTransformer transformer = new CoordinatesTransformer();
    
	Canvas canvas = createCanvas();
	GraphicsContext gc = canvas.getGraphicsContext2D();
	

    /** utility for counting frames per second */
    private FPSCounter fps = new FPSCounter();

    private Vector3D dragPosStart;
	int particleNumber = 0;
	
	Body[] bodies;
    
	private Text timeText = new Text();
	private Text fpsText = new Text();
	
	

    @Override
    public void start(Stage stage) throws IOException {
    	
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

        BorderPane border = new BorderPane(canvas);
        HBox hbox = new HBox();
        
        border.setBottom(hbox);
        
        
        StackPane root = new StackPane(canvas);
        
        root.getChildren().addAll(timeText, fpsText);
        StackPane.setAlignment(timeText,Pos.TOP_LEFT);
        StackPane.setAlignment(fpsText, Pos.TOP_CENTER);
        
        
		stage.setTitle("SpaceBum");
		stage.setScene(new Scene(root, width, height));
		stage.show();

		canvas.requestFocus();
		KeyFrame frame = new KeyFrame(Duration.millis(1000 / frames), e -> updateFrame());
		Timeline timeline = new Timeline(frame);
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();

    }

    /**
     * Draw single frame
     *
     * @param gc
     */
    protected void updateFrame() {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, width, height);
		
		
		 Quad quad = new Quad(0, 0, Radius*2);
         QuadTree tree = new QuadTree(quad);

         // build the Barnes-Hut tree
         for (int i = 0; i < particleNumber; i++)
             if (bodies[i].in(quad))
                tree.addNode(bodies[i]);
     
         // update the forces, positions, velocities, and accelerations
         for (int i = 0; i < particleNumber; i++) {
             bodies[i].resetForce();
             tree.updateForce(bodies[i]);
             bodies[i].update(TIME_SLICE);
         }

         // draw the bodies

         for (int i = 0; i < particleNumber; i++)
             bodies[i].draw(gc, transformer);
		
         DecimalFormat df = new DecimalFormat();
         df.applyPattern("0.00");
         
        timeText.setText(df.format(elapsedSeconds/2.25e8)+" Gyr");
        fpsText.setText("FPS: "+ fps.countFrame());
        
        elapsedSeconds += 1000000;
        
        System.out.println(elapsedSeconds);
       
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


    
    private void initBodies() throws NumberFormatException, IOException {
    	File file = new File("galaxy10k.txt"); 
  	  
  	BufferedReader br = new BufferedReader(new FileReader(file)); 
  	  
  	  String st;
  	  int flag= 0;
  	  int n = 0;
  	  
  	  while ((st = br.readLine()) != null) {
  		  if(flag == 0) {
  			  
  			  particleNumber = Integer.parseInt(st.trim());  			  
  			  bodies = new Body[particleNumber];
  			  
  		  }
  		  else {
  			  String[] values = st.split(" ");
  			  
  			  
  			  double x = Double.parseDouble(values[0].trim());
  			  double y = Double.parseDouble(values[1].trim());
  			  double vx = Double.parseDouble(values[2].trim());
  			  double vy = Double.parseDouble(values[3].trim());
  			  double mass = Double.parseDouble(values[4].trim());
//  			  int R = Integer.parseInt(values[5].trim());
//  			  int G = Integer.parseInt(values[6].trim());
//  			  int B = Integer.parseInt(values[7].trim());
  			  
  			  //Planet body = new Planet(x, y,vx,vy,mass);
  			  //planets[flag-1] = body;
  			  
  			  Body body = new Body(x, y,vx,vy,mass);
  			  bodies[flag-1] = body;
  			  
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
