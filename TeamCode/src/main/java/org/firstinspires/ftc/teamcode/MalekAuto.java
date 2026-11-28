package org.firstinspires.ftc.teamcode;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.api.Paths.path;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.interpolator.Interpolator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;

@Autonomous(name = "AutoPath", group = "Autonomous")
public class MalekAuto extends LinearOpMode {
    @Override
    public void runOpMode(){

        follower = Constants.createFollower(hardwareMap);
        follower.setPose(p.of(56.000, 8.000, -90.000));
        pathTimer = new ElapsedTime();
        paths = new Paths(follower);
        follower.update();
        pathState = autonomousPathUpdate();

    }

    public Follower follower;
    private final PoseFactory p = PoseFactory.degrees();
    private int pathState;
    private ElapsedTime pathTimer;
    private Paths paths;

    public static class Paths {

        private static final PoseFactory p = PoseFactory.degrees();
        public Path DriveToShootOne;
        public Path TurnToBall;
        public Path DriveToBall;
        public Path DriveToShootTwo;
        public Path BackForFlower;
        public Path TakeFromFlower;
        public Path DriveToShootThree;
        public Path park;

        public Paths(Follower follower) {
            DriveToShootOne = line(
                    p.of(56.000, 8.000, 0.0),
                    p.of(58.000, 15.000, 0.0)
            ).linear(Math.toRadians(-90), Math.toRadians(-90));

            TurnToBall = line(
                    p.of(58.000, 15.000, 0.0),
                    p.of(29.000, 10.000, 0.0)
            ).linear(Math.toRadians(-90), Math.toRadians(180));

            DriveToBall = line(
                    p.of(29.000, 10.000, 0.0),
                    p.of(10.000, 10.000, 0.0)
            ).linear(Math.toRadians(180), Math.toRadians(180));

            DriveToShootTwo = curve(
                    p.of(10.000, 10.000, 0.0),
                    p.of(25.532, 99.000, 0.0),
                    p.of(58.000, 130.000, 0.0)
            ).linear(Math.toRadians(180), Math.toRadians(90));

            BackForFlower = line(
                    p.of(58.000, 130.000, 0.0),
                    p.of(64.000, 134.000, 0.0)
            ).linear(Math.toRadians(90), Math.toRadians(90));

            TakeFromFlower = line(
                    p.of(64.000, 134.000, 0.0),
                    p.of(42.000, 134.000, 0.0)
            ).linear(Math.toRadians(90), Math.toRadians(90));

            DriveToShootThree = line(
                    p.of(42.000, 134.000, 0.0),
                    p.of(58.000, 130.000, 0.0)
            ).linear(Math.toRadians(90), Math.toRadians(90));

            park = line(p.of(58.000, 130.000, 0.0), p.of(10.000, 99.000, 0.0)).linear(
                    Math.toRadians(90),
                    Math.toRadians(90)
            );
        }
    }

    public int autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.follow(paths.DriveToShootOne);
                setPathState(1);
                break;
            case 1:
                if (!follower.isBusy()) {
                    setPathState(2);
                }
                break;
            case 2:
                setPathState(3);
                break;
            case 3:
                if (pathTimer.milliseconds() > 2000) {
                    shoot();
                    setPathState(4);
                }
                break;
            case 4:
                follower.follow(paths.TurnToBall);
                setPathState(5);
                break;
            case 5:
                if (!follower.isBusy()) {
                    setPathState(6);
                }
                break;
            case 6:
                follower.follow(paths.DriveToBall);
                setPathState(7);
                break;
            case 7:
                if (!follower.isBusy()) {
                    setPathState(8);
                }
                break;
            case 8:
                setPathState(9);
                break;
            case 9:
                if (pathTimer.milliseconds() > 500) {
                    setPathState(10);
                }
                break;
            case 10:
                follower.follow(paths.DriveToShootTwo);
                setPathState(11);
                break;
            case 11:
                if (!follower.isBusy()) {
                    setPathState(12);
                }
                break;
            case 12:
                setPathState(13);
                break;
            case 13:
                if (pathTimer.milliseconds() > 2000) {
                    shoot();
                    setPathState(14);
                }
                break;
            case 14:
                follower.follow(paths.BackForFlower);
                setPathState(15);
                break;
            case 15:
                if (!follower.isBusy()) {
                    setPathState(16);
                }
                break;
            case 16:
                follower.follow(paths.TakeFromFlower);
                setPathState(17);
                break;
            case 17:
                if (!follower.isBusy()) {
                    setPathState(18);
                }
                break;
            case 18:
                setPathState(19);
                break;
            case 19:
                if (pathTimer.milliseconds() > 1000) {
                    setPathState(20);
                }
                break;
            case 20:
                follower.follow(paths.DriveToShootThree);
                setPathState(21);
                break;
            case 21:
                if (!follower.isBusy()) {
                    setPathState(22);
                }
                break;
            case 22:
                setPathState(23);
                break;
            case 23:
                if (pathTimer.milliseconds() > 2000) {
                    shoot();
                    setPathState(24);
                }
                break;
            case 24:
                follower.follow(paths.park);
                setPathState(25);
                break;
            case 25:
                if (!follower.isBusy()) {
                    setPathState(26);
                }
                break;
            case 26:
                requestOpModeStop();
                pathState = -1;
                break;
        }
        return pathState;
    }
    DcMotor shooterMotor = hardwareMap.get(DcMotor.class, "shooterMotor");
    void shoot(){
        shooterMotor.setPower(0.53);
        for (double i = 0; i < 5; i++) {
            sleep(500);
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.reset();
    }
}
