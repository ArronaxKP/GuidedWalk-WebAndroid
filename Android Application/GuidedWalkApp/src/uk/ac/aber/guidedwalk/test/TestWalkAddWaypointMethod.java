package uk.ac.aber.guidedwalk.test;

import org.junit.Test;

import junit.framework.TestCase;
import uk.ac.aber.guidedwalk.model.Walk;
import uk.ac.aber.guidedwalk.model.Waypoint;

/**
 * Simple test to check that the waypoint is 
 * being added to the walk properly. 
 * 
 * @author Karl Parry (kdp8)
 *
 */
public class TestWalkAddWaypointMethod extends TestCase {
	
	@Test
	public void testAddWaypointToWalk(){
		 Walk walk = new Walk();
		 Waypoint waypoint = new Waypoint();
		 walk.addWaypoint(waypoint);
		 assertEquals(walk.getRoute().get(0),waypoint);
	}
}
