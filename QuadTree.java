/**
 *@author ashrafhossain
 * */
public class QuadTree {
	
	//threshold value theta 
	
	private final double theta = 0.5;
	
	//Properties of quad tree
	
    private Body body;     
    private Quad quad;     
    private QuadTree NW;     
    private QuadTree NE;     
    private QuadTree SW;     
    private QuadTree SE;
    
    
    public QuadTree(Quad quad) {
    	this.body = null;
    	this.quad = quad;
    	this.NW = null;
    	this.NE = null;
    	this.SW = null;
    	this.SE = null;
    }
    
    //adding new node
    
    public void addNode(Body body) {
    	
    	if(this.body == null) {
    		this.body = body;
    		
    		return;
    	}
    	
    	if (! isExternalNode()) {
    		this.body = this.body.plus(body);
    		
    		put(body);
    		
    	}
    	else {
    		NW = new QuadTree(quad.NW());
    		NE = new QuadTree(quad.NE());
    		SW = new QuadTree(quad.SW());
    		SE = new QuadTree(quad.SE());
    		
    		put(this.body);
    		put(body);
    		
    		this.body = this.body.plus(body);
    	}
    	
    }
    
    
    //is external node
    
    private boolean isExternalNode() {
    	return (NW == null && NE == null && SW == null && SE == null);
    }
    
    // put new body based on specific quadrant 
    
    private void put(Body body) {
        if (body.in(quad.NW()))
            NW.addNode(body);
        else if (body.in(quad.NE()))
            NE.addNode(body);
        else if (body.in(quad.SE()))
            SE.addNode(body);
        else if (body.in(quad.SW()))
            SW.addNode(body);
    }
    
    
    //updating net force acting on body a 
    
    public void updateForce(Body b) {
        
        if (body == null || b.equals(body))
            return;

        if (isExternalNode()) 
            b.addForce(body);
 
        else {
    
            double s = quad.length();
            
            double d = body.distanceTo(b);
            
            // is it far away to consider as a single body or not

            if ((s / d) < theta)
                b.addForce(body); 
            
            else {
                NW.updateForce(b);
                NE.updateForce(b);
                SW.updateForce(b);
                SE.updateForce(b);
            }
        }
    }

    
    
}
