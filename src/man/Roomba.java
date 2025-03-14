package man;

import robocode.*;

import java.awt.*;

/**
 * Roomba! - Happily tries to wipe every enemy.
 * <p>
 * Drives at robots trying to ram them.
 * Fires when very close to the enemy.
 * Dances upon winning.
 *
 * @author João Guedes - up202203859
 * @author Ricardo Figueiredo - up202105430
 */
public class Roomba extends AdvancedRobot {
    /**
     * Direction in which the radar is turning.
     * 1 = right, -1 = left.
     */
    private int radarTurnDirection = 1;

    /**
     * Time since the last collision with an enemy.
     */
    private long lastEnemyCollisionTime = 0;

    /**
     * Main loop of the robot.
     * Sets the colors and continuously scans for enemies.
     */
    public void run() {
        setBodyColor(Color.black);
        setGunColor(Color.green);
        setRadarColor(Color.red);
        setBulletColor(Color.green);

        while (true) {
            turnRadarRight(360);
        }
    }

    /**
     * Called when an enemy is scanned by the radar.
     * <p>
     * Roomba proceeds to run to the enemy.
     * Radar locks on to the enemy.
     * Roomba never looses track of the enemy.
     *
     * @param e The event containing the scanned robot's details.
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        setTurnRight(e.getBearing());
        double gunTurnAngle = getHeading() + e.getBearing() - getGunHeading();
        setTurnGunRight(gunTurnAngle);
        double enemyHeadingToRoombaAngle = Math.abs(robocode.util.Utils.normalRelativeAngleDegrees(
                e.getHeading() - (getHeading() + e.getBearing() + 180)));
        int maxAngleDelta = 10;
        if (enemyHeadingToRoombaAngle < maxAngleDelta) {
            if (getEnergy() >= 2 * Rules.MAX_BULLET_POWER)
                setFire(Rules.MAX_BULLET_POWER);
            else
                setFire(Math.max(0.1, getEnergy() / 2));
        }

        setAhead(500);

        if (shouldFire(e.getBearing(), e.getDistance()))
            setFire(Rules.MAX_BULLET_POWER);

        radarTurnDirection = -radarTurnDirection;
        setTurnRadarRight(radarTurnDirection);

        execute();
    }

    /**
     * Determines if the robot should fire based on the enemy's distance and bearing.
     *
     * @param eventBearing The bearing of the enemy.
     * @param eventDistance The distance to the enemy.
     * @return True if the robot should fire, false otherwise.
     */
    private boolean shouldFire(double eventBearing, double eventDistance) {
        int maxAngleDelta = 10;
        int maxDistance = 80;
        double gunAngleToEnemy = Math.abs(robocode.util.Utils
                .normalRelativeAngleDegrees((getHeading() + eventBearing) - getGunHeading()));
        return gunAngleToEnemy < maxAngleDelta && eventDistance < maxDistance;
    }

    /**
     * Executes a dance upon winning a match.
     * The robot wiggles left and right.
     *
     * @param e The event signaling the win.
     */
    public void onWin(WinEvent e) {
        for (int i = 0; i < 50; i++) {
            ahead(0);
            turnRight(30);
            turnLeft(30);
        }
    }



}