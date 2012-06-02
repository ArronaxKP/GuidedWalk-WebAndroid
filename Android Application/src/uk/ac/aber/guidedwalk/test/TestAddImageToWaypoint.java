package uk.ac.aber.guidedwalk.test;

import org.junit.Test;

import junit.framework.TestCase;
import uk.ac.aber.guidedwalk.model.Image;
import uk.ac.aber.guidedwalk.model.Waypoint;



/**
 * Simple test to check that the Image is 
 * being added to the waypoint properly. 
 * 
 * @author Karl Parry (kdp8)
 *
 */
public class TestAddImageToWaypoint extends TestCase{

	@Test
	public void testAddImageToWaypoint(){
		 Image image = new Image();
		 Waypoint waypoint = new Waypoint();
		 waypoint.addImage(image);
		 assertEquals(waypoint.getImages().get(0),image);
	}

}
