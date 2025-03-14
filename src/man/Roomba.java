package man;

import robocode.*;
import java.awt.*;

/**
 * Roomba! - Happily wipes every enemy.
 * <p>
 * This robot continuously scans for opponents, locks onto targets, and fires at
 * maximum power when it is sure it will hit.
 * Upon winning a round, it performs a celebratory dance.
 * </p>
 *
 * @author João Guedes
 * @author Ricardo Figueiredo
 */
public class Roomba extends AdvancedRobot {
    /**
     * Direction in which the radar is turning.
     * 1 for right, -1 for left.
     */
    private int radarTurnDirection = 1;

    /**
     * Time since the last collision with an enemy.
     */
    private long lastEnemyCollisionTime = 0;

    /**
     * Main execution loop.
     * <p>
     * Sets the robot's colors and continuously rotates the radar 360 degrees
     * searching for enemies.
     * </p>
     */
    @Override
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
     * Called when the robot's radar scans another robot.
     * <p>
     * This method adjusts the robot's heading to face the enemy while aligning the
     * gun accordingly.
     * It calculates the optimal firing angle relative to the enemy's position and
     * fires if conditions are met.
     * </p>
     *
     * @param e The ScannedRobotEvent containing details about the detected enemy.
     */
    @Override
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
     * Determines whether the robot should fire based on the enemy's position.
     * <p>
     * The robot checks if the gun is well-aligned with the enemy and if the enemy
     * is within a close range.
     * </p>
     *
     * @param eventBearing  the bearing from the robot to the enemy.
     * @param eventDistance the distance from the robot to the enemy.
     * @return true if conditions are favorable for firing; false otherwise.
     */
    private boolean shouldFire(double eventBearing, double eventDistance) {
        int maxAngleDelta = 10;
        int maxDistance = 100;
        double gunAngleToEnemy = Math.abs(robocode.util.Utils
                .normalRelativeAngleDegrees((getHeading() + eventBearing) - getGunHeading()));
        return gunAngleToEnemy < maxAngleDelta && eventDistance < maxDistance;
    }

    /**
     * Called when the robot wins the match.
     * <p>
     * Executes a celebratory dance by wiggling left and right.
     * </p>
     *
     * @param e the WinEvent signaling the victory.
     */
    @Override
    public void onWin(WinEvent e) {
        for (int i = 0; i < 50; i++) {
            ahead(0);
            turnRight(30);
            turnLeft(30);
        }
    }

}