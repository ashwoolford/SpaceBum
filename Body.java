/**
 *@author ashrafhossain
 * */


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Body {
	
	public static final double BODY_RADIUS_GUI = 2;
	private static final double G = 6.67e-11;  //Universal Gravitational Constant 
	
	private double Px, Py; 	// Position
	private double Vx, Vy;  //Velocity
	private double Fx, Fy; 	//Force
	private double mass;	//Mass
	private Color color;	//RGB color
	
	public Body(double Px, double Py, double Vx, double Vy, double mass) {
		this.Px = Px;
		this.Py = Py;
		this.Vx = Vx;
		this.Vy = Vy;
		this.mass = mass;
		this.color = color;
	}
	
	//Reseting Force values
	
    public void resetForce() {
        Fx = 0.0;
        Fy = 0.0;
    }
    
    //Adding force between two bodies
    
    public void addForce(Body b) {
        Body a = this;
        double eps = 3.0E4;
        double dx = b.Px - a.Px;
        double dy = b.Py - a.Py;
        double dist = Math.sqrt(dx*dx + dy*dy);
        double F = (G * a.mass * b.mass) / (dist*dist + eps*eps);
        a.Fx += F * dx / dist;
        a.Fy += F * dy / dist;
    }
    
    //Updating the position using leapfrog method
    
    public void update(double dt) {
        Vx += dt * Fx / mass;
        Vy += dt * Fy / mass;
        Px += dt * Vx;
        Py += dt * Vy;
    }
    
    //Updating center of mass and position
	
	
	 public Body plus(Body b) {
	        Body a = this;

	        double m = a.mass + b.mass;
	        double x = (a.Px * a.mass + b.Px * b.mass) / m;
	        double y = (a.Py * a.mass + b.Py * b.mass) / m;

	        return new Body(x, y, a.Vx, b.Vx, m);
	}
	 
	
	 //checking Quadrant using position values
    public boolean in(Quad q) {
        return q.inRange(Px, Py); 
    }
    
    //Calculating Euclidean Distance
    
    public double distanceTo(Body b) {
        double dx = Px - b.Px;
        double dy = Py - b.Py;
        return Math.sqrt(dx*dx + dy*dy);
    }
    
	/**
     * Draw the planet on the screen
     */
    public void draw(GraphicsContext gc, CoordinatesTransformer transformer) {
        gc.setFill(Color.GRAY);
       gc.fillOval(transformer.modelToOtherX(Px) - BODY_RADIUS_GUI, transformer.modelToOtherX(Py) - BODY_RADIUS_GUI, 1, 1);
        //gc.fillOval(xxPos, yyPos, BODY_RADIUS_GUI * 2, BODY_RADIUS_GUI * 2);
        
       //System.out.println(transformer.modelToOtherX(xxPos)+ " " +transformer.modelToOtherX(yyPos));
    }
	
	
}


