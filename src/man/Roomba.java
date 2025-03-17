package man;

import robocode.*;
import java.awt.*;

/**
 * Roomba! - Happily wipes every enemy.
 * <p>
 * This robot continuously scans for opponents, locks onto targets, and fires at
 * when it is certain it will hit.
 * Upon winning a round, it performs a celebratory dance.
 * </p>
 *
 * @author João Guedes
 * @author Ricardo Figueiredo
 */
public class Roomba extends AdvancedRobot {

    /**
     * Time since the last collision with an enemy.
     */
    private long lastEnemyCollisionTime = 0;

    /**
     * Timeout before switching to zigzag mode.
     */
    private static final long COLLISION_TIMEOUT = 3000; // 3 seconds

    /**
     * Whether the robot is in zigzag mode.
     */
    private boolean zigzagMode = false;

    /**
     * Main execution loop.
     * <p>
     * Sets the robot's colors and continuously rotates the radar 360 degrees
     * searching for enemies. Also controls zigzag mode.
     * </p>
     */
    @Override
    public void run() {
        setBodyColor(Color.black);
        setGunColor(Color.green);
        setRadarColor(Color.red);
        setBulletColor(Color.green);

        lastEnemyCollisionTime = System.currentTimeMillis();

        while (true) {
            long timeSinceLastCollision = System.currentTimeMillis() - lastEnemyCollisionTime;

            if (timeSinceLastCollision > COLLISION_TIMEOUT && !zigzagMode) {
                startZigzagMode();
            }

            turnRadarRight(360);
        }
    }

    /**
     * Called when the robot's radar scans another robot.
     * <p>
     * This method adjusts the robot's alignment of the gun accordingly.
     * It calculates the optimal firing angle relative to the enemy's position and
     * fires if conditions are met. Shooting power taking distance in consideration.
     * </p>
     *
     * @param e The ScannedRobotEvent containing details about the detected enemy.
     */
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        double enemyDistance = e.getDistance();
        double bulletPower;

        if (enemyDistance < 100) {
            bulletPower = Rules.MAX_BULLET_POWER;
        }
        else if (enemyDistance < 500) {
            bulletPower = 2.0;
        }
        else {
            bulletPower = 1.0;
        }

        double predictedX = getX() + enemyDistance * Math.sin(getHeadingRadians() + e.getBearingRadians());
        double predictedY = getY() + enemyDistance * Math.cos(getHeadingRadians() + e.getBearingRadians());
        double aimAngle = Math.toDegrees(Math.atan2(predictedX - getX(), predictedY - getY()));
        double radarAdjustment = robocode.util.Utils.normalRelativeAngleDegrees(getHeading() + e.getBearing() - getRadarHeading());
        double gunAdjustment = robocode.util.Utils.normalRelativeAngleDegrees(aimAngle - getGunHeading());

        setTurnRadarRight(radarAdjustment);
        setTurnGunRight(gunAdjustment);

        if (Math.abs(gunAdjustment) < 5) {
            setFire(bulletPower);
        }

        setAhead(50);
        setTurnRight(20);


        execute();
    }

    /**
     * Called when the robot collides with another robot.
     * <p>
     * Updates the last collision time. In addition, Handles shooting and mechanics.
     * </p>
     *
     * @param e The collision event.
     */
    public void onHitRobot(HitRobotEvent e) {
        lastEnemyCollisionTime = System.currentTimeMillis();
        zigzagMode = false;
        setTurnRight(e.getBearing());
        double gunTurnAngle = getHeading() + e.getBearing() - getGunHeading();
        setTurnGunRight(gunTurnAngle);
        setFire(Math.max(0.1, getEnergy() / 2));
    }

    /**
     * Starts the opposite direction movement
     * <p>
     * Changes direction to make it less predictable.
     * </p>
     */
    private void startZigzagMode() {

        setTurnLeft(30);
        setAhead(150);

        execute();

        zigzagMode = false;
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