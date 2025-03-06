package man;
import robocode.*;

public class TestBadBot extends AdvancedRobot{
    public void run() {
        while (true) {
            turnGunRight(360);
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        fire(3);
        // turn roomba towards enemy
        setTurnRight(e.getBearing() + 90);
    }
}
