
/**
 * @author ashrafhossain
 **/


//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.Arrays;
//
//
//public class SolarSystem extends BodySystem {
//
//    //private static CelestialBody[] CELESTIAL_BODIES_IN_SYSTEM = new CelestialBody[] {SUN, MERCURY, VENUS, EARTH, MARS, JUPITER, SATURN, NEPTUNE, URANUS, PLUTO};
//
//    //private static CelestialBody[] CELESTIAL_BODIES_IN_SYSTEM = new CelestialBody[] {CelestialBody.SUN, CelestialBody.VENUS,CelestialBody.MERCURY,CelestialBody.JUPITER, CelestialBody.EARTH, CelestialBody.SATURN, CelestialBody.MARS};
//	
//	//static CelestialBody body1 = new CelestialBody(1E+30, 0, 0, 0, 0, 0, 0);
//	//static CelestialBody body2 = new CelestialBody(1,0.03854807810554345,-0.4332369572474206,-0.7907243707173921,0.7249014083009561,-2.7352445251473445,1.1624560687432175);
////	
//	//static CelestialBody[] CELESTIAL_BODIES_IN_SYSTEM = new CelestialBody[] {body1, body2};
//	
//	static CelestialBody body;
//	static CelestialBody[] CELESTIAL_BODIES_IN_SYSTEM;
//
//    public SolarSystem() throws IOException {
//        super();
//        
//        initBodies();
//        createSolarSystem();
//    }
//    
//    private void initBodies() throws IOException {
//    	
//    	File file = new File("plummer_data.txt"); 
//    	  
//    	BufferedReader br = new BufferedReader(new FileReader(file)); 
//    	  
//    	  String st;
//    	  int flag= 0;
//    	  int n = 0;
//    	  
//    	  while ((st = br.readLine()) != null) {
//    		  if(flag == 0) CELESTIAL_BODIES_IN_SYSTEM = new CelestialBody[Integer.parseInt(st.trim())];
//    		  else {
//    			  String[] values = st.split(" ");
//    			  
//    			  double mass = Double.parseDouble(values[0].trim());
//    			  double x = Double.parseDouble(values[1].trim());
//    			  double y = Double.parseDouble(values[2].trim());
//    			  double z = Double.parseDouble(values[3].trim());
//    			  double vx = Double.parseDouble(values[4].trim());
//    			  double vy = Double.parseDouble(values[5].trim());
//    			  double vz = Double.parseDouble(values[6].trim());
//    			  
//    			  body = new CelestialBody(mass, x, y, z,vx,vy, vz);
//    			  
//    			 CELESTIAL_BODIES_IN_SYSTEM[flag-1] = body;
//    			  
//    			  
//    		  }
//    		  
//    		  flag++;
//    		  
//    	  }
//    	  
//    	 
//    	  
//    	 //System.out.println(n);
//    	
//    	//CELESTIAL_BODIES_IN_SYSTEM = new CelestialBody[];
//    	
//    	
//    }
//
//    private  void createSolarSystem() {
// 
//
//        Arrays.stream(CELESTIAL_BODIES_IN_SYSTEM).forEach((celestialBody) -> {
//            final Body body = celestialBody.getAsBody();
//
//            /*
//
//
//            // position body at random angle from sun
//            if (celestialBody != CelestialBody.SUN) {
//                int deg = rand.nextInt(360) + 1;
//
//                // update location
//                double x = Math.cos(Math.toRadians(deg)) * celestialBody.location.x;
//                double y = Math.sin(Math.toRadians(deg)) * celestialBody.location.x;
//                body.location.x = x;
//                body.location.y = y;
//
//                // velocity is stored for celestial body with only Y-component so convert to angle in tangent with movement
//                double x_vel = Math.cos(Math.toRadians(deg + 90)) * celestialBody.avgOrbSpeed;
//                double y_vel = Math.sin(Math.toRadians(deg + 90)) * celestialBody.avgOrbSpeed;
//                body.velocity.x = x_vel;
//                body.velocity.y = y_vel;
//            }
//            */
//
//            addBody(body);
//        });
//    }
//
//}
