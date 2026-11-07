package org.firstinspires.ftc.teamcode.auto;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.api.Paths.path;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.interpolator.Interpolator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;

@Autonomous(name = "AutoPath", group = "Autonomous")
@Configurable
public class soloAuto extends OpMode {

    private TelemetryManager panelsTelemetry;
    public Follower follower;
    private final PoseFactory p = PoseFactory.degrees();
    private int pathState;
    private ElapsedTime pathTimer;
    private Paths paths;

    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);

        follower = Constants.createFollower(hardwareMap);
        follower.setPose(p.of(59.979, 8, 90));

        pathTimer = new ElapsedTime();
        paths = new Paths(follower);
    }

    @Override
    public void loop() {
        follower.update();
        pathState = autonomousPathUpdate();

        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("X", follower.pose().x());
        panelsTelemetry.debug("Y", follower.pose().y());
        panelsTelemetry.debug("Heading", follower.pose().heading());
        panelsTelemetry.update(telemetry);
    }

    public static class Paths {

        private static final PoseFactory p = PoseFactory.degrees();
        public Path DriveToShoot;
        public Path line2;
        public Path line3;
        public Path line4;
        public Path line5;
        public Path line6;
        public Path line7;
        public Path line8;

        public Paths(Follower follower) {
            DriveToShoot = line(
                    p.of(56, 8, 0.0),
                    p.of(56, 24, 0.0)
            ).linear(Math.toRadians(90), Math.toRadians(90));

            line2 = line(p.of(58.979, 38, 0.0), p.of(58.298, 38.617, 0.0)).linear(
                    Math.toRadians(90),
                    Math.toRadians(90)
            );

            line3 = line(p.of(58.298, 38.617, 0.0), p.of(11.872, 10.851, 0.0)).linear(
                    Math.toRadians(180),
                    Math.toRadians(180)
            );

            line4 = line(
                    p.of(11.872, 10.851, 0.0),
                    p.of(29, 112, 0.0)
            ).linear(Math.toRadians(180), Math.toRadians(180));

            line5 = line(
                    p.of(29, 112, 0.0),
                    p.of(58, 107, 0.0)
            ).linear(Math.toRadians(180), Math.toRadians(-90));

            line6 = line(
                    p.of(58, 107, 0.0),
                    p.of(49, 131, 0.0)
            ).linear(Math.toRadians(-90), Math.toRadians(90));

            line7 = line(
                    p.of(49, 131, 0.0),
                    p.of(58, 107, 0.0)
            ).linear(Math.toRadians(90), Math.toRadians(-90));

            line8 = line(
                    p.of(58, 107, 0.0),
                    p.of(14, 107, 0.0)
            ).linear(Math.toRadians(-90), Math.toRadians(0));
        }
    }

    public int autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.follow(paths.DriveToShoot);
                setPathState(1);
                break;
            case 1:
                if (!follower.isBusy()) {
                    setPathState(2);
                }
                break;
            case 2:
                follower.follow(paths.line2);
                setPathState(3);
                break;
            case 3:
                if (!follower.isBusy()) {
                    setPathState(4);
                }
                break;
            case 4:
                follower.follow(paths.line3);
                setPathState(5);
                break;
            case 5:
                if (!follower.isBusy()) {
                    setPathState(6);
                }
                break;
            case 6:
                follower.follow(paths.line4);
                setPathState(7);
                break;
            case 7:
                if (!follower.isBusy()) {
                    setPathState(8);
                }
                break;
            case 8:
                follower.follow(paths.line5);
                setPathState(9);
                break;
            case 9:
                if (!follower.isBusy()) {
                    setPathState(10);
                }
                break;
            case 10:
                follower.follow(paths.line6);
                setPathState(11);
                break;
            case 11:
                if (!follower.isBusy()) {
                    setPathState(12);
                }
                break;
            case 12:
                follower.follow(paths.line7);
                setPathState(13);
                break;
            case 13:
                if (!follower.isBusy()) {
                    setPathState(14);
                }
                break;
            case 14:
                follower.follow(paths.line8);
                setPathState(15);
                break;
            case 15:
                if (!follower.isBusy()) {
                    setPathState(16);
                }
                break;
            case 16:
                requestOpModeStop();
                pathState = -1;
                break;
        }
        return pathState;
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.reset();
    }
}
