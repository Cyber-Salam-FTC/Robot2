package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name = "servoContinuous", group = "Test")
public class servoTest extends OpMode {

    private CRServo servo1, servo2;
    private double savedPower = 0.0;

    @Override
    public void init() {
        servo1 = hardwareMap.get(CRServo.class, "servo");
        servo2 = hardwareMap.get(CRServo.class, "servo");

        savedPower = 0.0;
        servo1.setPower(0.0);
        servo2.setPower(0.0);

        telemetry.addLine("X: continuous forward");
        telemetry.addLine("Y: continuous reverse");
        telemetry.addLine("A: stop");
        telemetry.update();
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            savedPower = 0.0;
        } else if (gamepad1.x) {
            savedPower = 1.0;
        } else if (gamepad1.y) {
            savedPower = -1.0;
        }

        servo1.setPower(savedPower);
        servo2.setPower(savedPower);

        telemetry.addData("Commanded power", savedPower);
        telemetry.addData("Controls", "X: forward | Y: reverse | A: stop");
        telemetry.update();
    }

    @Override
    public void stop() {
        servo.setPower(0.0);
    }
}
