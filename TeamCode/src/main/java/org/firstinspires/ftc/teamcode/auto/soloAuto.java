package org.firstinspires.ftc.teamcode.auto;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedropathing.Constants;

@Autonomous(name = "soloAuto", group = "Autonomous")
@Configurable
public class soloAuto extends LinearOpMode {

    public static double DRIVE_SPEED = 0.65;
    public static double INTAKE_PATH_SPEED = 0.4;
    public static double SHOOTER_POWER = 0.55;
    public static double INTAKE_POWER = 1.0;
    public static double TRANSFER_POWER = 0.6;

    public static double SPINUP_SECONDS = 1.5;
    public static double FEED_SECONDS = 1.8;
    public static double FEED_PAUSE_SECONDS = 1.9;
    public static double SETTLE_SECONDS = 0.1;

    public static int SHOTS_PER_STOP = 4;

    private TelemetryManager panelsTelemetry;
    public Follower follower;

    private final PoseFactory p = PoseFactory.degrees();
    private final ElapsedTime pathTimer = new ElapsedTime();
    private final ElapsedTime shotTimer = new ElapsedTime();

    private Paths paths;
    private int pathState;
    private int shotsRemaining;
    private boolean feeding;

    private DcMotor shooterMotor;
    private DcMotor intakeMotor;
    private CRServo transferServo;
    private CRServo intakeServo1;
    private CRServo intakeServo2;

    @Override
    public void runOpMode() {
        shooterMotor = hardwareMap.get(DcMotor.class, "shooterMotor");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        transferServo = hardwareMap.get(CRServo.class, "transferServo");
        intakeServo1 = hardwareMap.get(CRServo.class, "intakeServo1");
        intakeServo2 = hardwareMap.get(CRServo.class, "intakeServo2");

        transferServo.setDirection(DcMotorSimple.Direction.REVERSE);

        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        follower = Constants.createFollower(hardwareMap);
        follower.setPose(p.of(56.0, 8.0, -90.0));

        paths = new Paths();

        shooterMotor.setPower(0);
        transferServo.setPower(0);
        intake(0);

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);

        waitForStart();

        if (isStopRequested()) {
            return;
        }

        Constants.foresightConfig.maxPathSpeed.set(DRIVE_SPEED);
        shooterMotor.setPower(SHOOTER_POWER);
        setPathState(0);

        try {
            while (opModeIsActive()) {
                follower.update();
                autonomousPathUpdate();

                panelsTelemetry.debug("Path State", pathState);
                panelsTelemetry.debug("Completion", follower.completion());
                panelsTelemetry.debug("X", follower.pose().x());
                panelsTelemetry.debug("Y", follower.pose().y());
                panelsTelemetry.debug(
                        "Heading Degrees",
                        Math.toDegrees(follower.pose().heading())
                );
                panelsTelemetry.debug("Shots Remaining", shotsRemaining);
                panelsTelemetry.update(telemetry);

                idle();
            }
        } finally {
            shooterMotor.setPower(0);
            transferServo.setPower(0);
            intake(0);
        }
    }

    public static class Paths {

        private static final PoseFactory p = PoseFactory.degrees();

        public final Path DriveToShoot;
//        public final Path line2;
//        public final Path line3;
//        public final Path line4;
//        public final Path line5;

        public Paths() {
            DriveToShoot = curve(
                    p.of(56.0, 8.0, -90.0),
                    p.of(0, 20, -90),
                    p.of(8.0, 98.0, 180.0)
            ).linear(
                    Math.toRadians(90.0),
                    Math.toRadians(180.0)
            );
//
//            line2 = line(
//                    p.of(20.0, 11.0, 180.0),
//                    p.of(8.0, 11.0, 180.0)
//            ).linear(
//                    Math.toRadians(180.0),
//                    Math.toRadians(180.0)
//            );
//
//            line3 = curve(
//                    p.of(8.0, 11.0, 180.0),
//                    p.of(20.0, 80.0, 180.0),
//                    p.of(30.0, 80.0, 270.0),
//                    p.of(63.0, 136.0, 270.0)
//            ).linear(
//                    Math.toRadians(180.0),
//                    Math.toRadians(270.0)
//            );
//
//            line4 = curve(
//                    p.of(63.0, 136.0, 270.0),
//                    p.of(63.0, 123.0, 270.0),
//                    p.of(56.0, 130.0, 135.0)
//            ).linear(
//                    Math.toRadians(270.0),
//                    Math.toRadians(135.0)
//            );
//
//            line5 = line(
//                    p.of(56.0, 130.0, 135.0),
//                    p.of(63.0, 134.0, 270.0)
//            ).linear(
//                    Math.toRadians(135.0),
//                    Math.toRadians(270.0)
//            );

        }
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                if (pathTimer.seconds() >= SPINUP_SECONDS) {
                    startShooting(SHOTS_PER_STOP);
                    setPathState(1);
                }
                break;
            case 1:
                if (updateShooting()) {
//                    Constants.foresightConfig.maxPathSpeed.set(DRIVE_SPEED);
                    follower.follow(paths.DriveToShoot);
                    setPathState(2);
                }
                break;
//
//            case 2:
//                if (!follower.isBusy()) {
//                    follower.follow(paths.line2);
//                    setPathState(3);
//                }
//                break;
//
//            case 3:
//                if (!follower.isBusy()) {
//                    intake(INTAKE_POWER);
//                    Constants.foresightConfig.maxPathSpeed.set(DRIVE_SPEED);
//                    follower.follow(paths.line3);
//                    setPathState(4);
//                }
//                break;
//
//            case 4:
//                if (follower.completion() >= 0.65) {
//                    Constants.foresightConfig.maxPathSpeed.set(
//                            INTAKE_PATH_SPEED
//                    );
//                }
//
//                if (!follower.isBusy()) {
//                    Constants.foresightConfig.maxPathSpeed.set(
//                            INTAKE_PATH_SPEED
//                    );
//                    follower.follow(paths.line4);
//                    setPathState(5);
//                }
//                break;
//
//            case 5:
//                if (!follower.isBusy()) {
//                    intake(0);
//                    setPathState(6);
//                }
//                break;
//
//            case 6:
//                if (pathTimer.seconds() >= SETTLE_SECONDS) {
//                    startShooting(SHOTS_PER_STOP);
//                    setPathState(7);
//                }
//                break;
//
//            case 7:
//                if (updateShooting()) {
//                    Constants.foresightConfig.maxPathSpeed.set(
//                            INTAKE_PATH_SPEED
//                    );
//                    follower.follow(paths.line5);
//                    setPathState(8);
//                }
//                break;
//
//            case 8:
//                if (!follower.isBusy()) {
//                    setPathState(9);
//                }
//                break;
//
//            case 9:
//                if (pathTimer.seconds() >= SETTLE_SECONDS) {
//                    startShooting(SHOTS_PER_STOP);
//                    setPathState(10);
//                }
//                break;
//
//            case 10:
//                if (updateShooting()) {
//                    setPathState(-1);
//                    requestOpModeStop();
//                }
//                break;

            default:
                break;
        }
    }

    public void setPathState(int state) {
        pathState = state;
        pathTimer.reset();
    }

    private void startShooting(int count) {
        shotsRemaining = Math.max(0, count);
        feeding = shotsRemaining > 0;
        transferServo.setPower(feeding ? TRANSFER_POWER : 0.0);
        shotTimer.reset();
    }

    private boolean updateShooting() {
        if (shotsRemaining <= 0) {
            transferServo.setPower(0);
            return true;
        }

        if (feeding) {
            if (shotTimer.seconds() >= FEED_SECONDS) {
                transferServo.setPower(0);
                feeding = false;
                shotsRemaining--;
                shotTimer.reset();
            }
        } else if (shotTimer.seconds() >= FEED_PAUSE_SECONDS) {
            transferServo.setPower(TRANSFER_POWER);
            feeding = true;
            shotTimer.reset();
        }

        return shotsRemaining <= 0;
    }

    public void intake(double power) {
        intakeMotor.setPower(power);
        intakeServo1.setPower(-power);
        intakeServo2.setPower(power);
    }
}