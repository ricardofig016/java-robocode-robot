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
    private int radarTurnDirection = 1; // 1 = right, -1 = left
    private long lastEnemyCollisionTime = 0; // time since last collision with enemy

    public void run() {
        setBodyColor(Color.black);
        setGunColor(Color.green);
        setRadarColor(Color.red);
        setBulletColor(Color.green);
        setScanColor(Color.WHITE);
        while (true) {
            turnRadarRight(360);
        }
    }

    // called when an enemy is directly in front of the radar
    public void onScannedRobot(ScannedRobotEvent e) {
        // turn roomba and its gun towards the enemy
        setTurnRight(e.getBearing());
        double gunTurnAngle = getHeading() + e.getBearing() - getGunHeading();
        setTurnGunRight(gunTurnAngle);

        // fire if the enemy is directly looking at the roomba
        double enemyHeadingToRoombaAngle = Math.abs(robocode.util.Utils.normalRelativeAngleDegrees(
                e.getHeading() - (getHeading() + e.getBearing() + 180)));
        int maxAngleDelta = 10;
        if (enemyHeadingToRoombaAngle < maxAngleDelta) {
            if (getEnergy() >= 2 * Rules.MAX_BULLET_POWER)
                setFire(Rules.MAX_BULLET_POWER);
            else
                setFire(Math.max(0.1, getEnergy() / 2));
        }

        // go to enemy
        setAhead(500);

        if (shouldFire(e.getBearing(), e.getDistance()))
            setFire(Rules.MAX_BULLET_POWER);

        // switching the direction the radar was turning
        // will make the roomba never lose track of the enemy
        radarTurnDirection = -radarTurnDirection;
        setTurnRadarRight(radarTurnDirection);

        execute();
    }

    // roomba should only fire if the gun is pointing
    // at an enemy and the enemy is very close
    private boolean shouldFire(double eventBearing, double eventDistance) {
        int maxAngleDelta = 10;
        int maxDistance = 80;
        double gunAngleToEnemy = Math.abs(robocode.util.Utils
                .normalRelativeAngleDegrees((getHeading() + eventBearing) - getGunHeading()));
        return gunAngleToEnemy < maxAngleDelta && eventDistance < maxDistance;
    }

    // roomba dance upon winning
    // just wiggles left and right
    public void onWin(WinEvent e) {
        for (int i = 0; i < 50; i++) {
            ahead(0);
            turnRight(30);
            turnLeft(30);
        }
    }



}