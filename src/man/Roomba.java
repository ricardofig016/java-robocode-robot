package man;
import robocode.*;

public class Roomba extends Robot {
    public void run() {
        while (true) {

        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        fire(1);
    }
}