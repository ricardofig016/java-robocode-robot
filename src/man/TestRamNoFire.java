package man;

import robocode.*;

import java.awt.Color;

public class TestRamNoFire extends Robot{
    int turnDirection = 1; // Clockwise or counterclockwise

    /**
     * run: Spin around looking for a target
     */
    public void run() {
        // Set colors
        setBodyColor(Color.lightGray);
        setGunColor(Color.gray);
        setRadarColor(Color.darkGray);

        while (true) {
            turnRight(5 * turnDirection);
        }
    }

    /**
     * onScannedRobot:  We have a target.  Go get it.
     */
    public void onScannedRobot(ScannedRobotEvent e) {

        if (e.getBearing() >= 0) {
            turnDirection = 1;
        } else {
            turnDirection = -1;
        }

        turnRight(e.getBearing());
        ahead(e.getDistance() + 5);
        scan(); // Might want to move ahead again!
    }

    /**
     * onHitRobot:  Turn to face robot, fire hard, and ram him again!
     */
    public void onHitRobot(HitRobotEvent e) {
        if (e.getBearing() >= 0) {
            turnDirection = 1;
        } else {
            turnDirection = -1;
        }
        turnRight(e.getBearing());

        // We want to ram him instead for bonus points
        ahead(40); // Ram him again!
    }
}
