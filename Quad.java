/**
 *@author ashrafhossain
 * */

public class Quad {
	private double xmid;
	private double ymid;
	private double length;
	
	public Quad(double xmid, double ymid, double length) {
		this.xmid = xmid;
		this.ymid = ymid;
		this.length = length;
	}
	
	//Checking x,y falling inside the given length 
	
	
	public boolean inRange(double x, double y) {
		double halfLen = length/2.0;
		
		return 
			(x <= xmid + halfLen && x >= xmid - halfLen
			&& 
			y <= ymid + halfLen && y >= ymid - halfLen);
	}
	
	//Bounds of North West Quadrant 
	
    public Quad NW() {
        double x = xmid - length / 4.0;
        double y = ymid + length / 4.0;
        double len = length / 2.0;
        Quad NW = new Quad(x, y, len);
        return NW;
    }
    
  //Bounds of North East Quadrant
    
    
    public Quad NE() {
        double x = xmid + length / 4.0;
        double y = ymid + length / 4.0;
        double len = length / 2.0;
        Quad NE = new Quad(x, y, len);
        return NE;
    }
    
  //Bounds of South West Quadrant
    
    
    public Quad SW() {
        double x = xmid - length / 4.0;
        double y = ymid - length / 4.0;
        double len = length / 2.0;
        Quad SW = new Quad(x, y, len);
        return SW;
    }
    
  //Bounds of South East Quadrant
    
    
    public Quad SE() {
        double x = xmid + length / 4.0;
        double y = ymid - length / 4.0;
        double len = length / 2.0;
        Quad SE = new Quad(x, y, len);
        return SE;
    }
    
    public double length() {
        return length;
    }

}
