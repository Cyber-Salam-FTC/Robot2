package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.pedropathing.Constants;
@TeleOp
public class VelocityChange extends LinearOpMode{
    private static final double CELL_A_X = 72, CELL_A_Y = 81.4;
    private static final double CELL_B_X = 72, CELL_B_Y = 62.6;

    private double shooterPower = 0;
    private static final double GOAL_X = 72, GOAL_Y = 81.4;

    private Follower follower = Constants.createFollower(hardwareMap);
    private DcMotorEx shooter;
    Pose pose = follower.pose();

    double distance = Math.hypot(GOAL_X - pose.x(), GOAL_Y - pose.y());
    private double velocityFor(double distance) {
        double[] d = {  24,   48,   72,   96,  120}; // inches
        double[] v = {1100, 1300, 1550, 1800, 2050}; // ticks per second

        if (distance <= d[0]) return v[0];
        for (int i = 1; i < d.length; i++) {
            if (distance <= d[i]) {
                return v[i-1] + (distance - d[i-1]) / (d[i] - d[i-1]) * (v[i] - v[i-1]);
            }
        }
        return v[v.length - 1];
    }
    @Override
    public void runOpMode() {
        while (opModeIsActive()) {
            follower.update();
            if (gamepad1.right_bumper) {
                shooter.setVelocity(velocityFor(distance));
            } else {
                shooter.setPower(0);
            }
        }
    }
}
