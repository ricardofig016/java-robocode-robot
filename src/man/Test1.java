package man;
import robocode.*;

public class Test1 extends Robot{
    public void run() {
        while (true) {
            ahead(100);
            turnGunRight(360);
            back(100);
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        fire(1);
    }
}
