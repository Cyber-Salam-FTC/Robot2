package org.firstinspires.ftc.teamcode.pedropathing;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("leftFront");
        c.frontRightName.set("rightFront");
        c.backLeftName.set("leftRear");
        c.backRightName.set("rightRear");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpointComputer");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.yPodOffset.set(-1.890);
        c.xPodOffset.set(-6.142);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.2057411797280683);
                Controller secondaryTranslationalForward = Controller.proportional(0.07601586840394989);
                Controller primaryTranslationalLateral = Controller.proportional(0.2597494028113951);
                Controller secondaryTranslationalLateral = Controller.proportional(0.09597046370693992);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.016411267665212736));
                c.brake.set(Controller.proportionalFeedforward(0.013949577515430824));

                c.headingFeedback.set(Controller.proportional(3.968652918387855));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04018501094118686, 0.004847441288196206));

                c.linearBrakeCoefficients.set(Matrix.diag(0.06388424567614269, 0.03320236678280321));
                c.quadraticBrakeCoefficients.set(Matrix.diag(9.5983483411547E-4, 0.0016739439833614957));

                c.maxAchievableForwardVelocity.set(64.70355478000414);
                c.maxAchievableStrafeVelocity.set(54.985017023557916);
                c.naturalForwardDeceleration.set(33.009799231554695);
                c.naturalStrafeDeceleration.set(59.96352270241583);
            }
    );

    public static Follower createFollower(HardwareMap h) {
        return new Follower(
            new PinpointLocalizer(h, localizerConfig),
            new Mecanum(h, drivetrainConfig),
            new Foresight(foresightConfig)
        );
    }
}