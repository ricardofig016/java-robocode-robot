package man;
import robocode.*;

public class Roomba extends AdvancedRobot {
    public void run() {
        while (true) {
            turnGunRight(360);
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        // turn roomba towards enemy
        setTurnRight(e.getBearing());
        fire(3);
    }
}