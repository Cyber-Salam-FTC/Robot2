package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Servos extends LinearOpMode {
    private CRServo servoRot, servoRot2;

    @Override
    public void runOpMode() {
        servoRot = hardwareMap.get(CRServo.class, "Servo_Rotation");
        servoRot2 = hardwareMap.get(CRServo.class, "Servo_Rotation_2");
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                setServoRot(1);
            }

            if (gamepad1.b) {
                setServoRot(-1);
            }
        }
    }

    public void setServoRot(double power) {
        servoRot.setPower(power);
        servoRot2.setPower(-power);
    }
}