package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.cybersalam.hardware.MecanumDrive;

@TeleOp(name = "mainop")
public class MainOp extends LinearOpMode {

    private DcMotor intakeMotor;
    private DcMotorEx shooterMotor;
    private CRServo intakeServo1, intakeServo2;
    private CRServo transferServo;

    private static final double TRANSFER_STEP = 90.0 / 180.0;

    private double intakePower = 0;
    private double shooterPower = 0;
    private double transferPosition = 0.5;

    private boolean previousDpadRight = false;
    private boolean previousDpadLeft = false;
    private boolean IS_SHOOTING = false;

    public final int LAUNCHER_TARGET_VELOCITY = 1300;
    public final int LAUNCHER_MIN_VELOCITY = 1200;

    @Override
    public void runOpMode() {
        initHardware();

        MecanumDrive drive = new MecanumDrive();

        intakeServo1.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeServo2.setDirection(DcMotorSimple.Direction.FORWARD);

        transferServo.setDirection(DcMotorSimple.Direction.REVERSE);

        drive.init(hardwareMap,true);

        waitForStart();

        try {
            while (opModeIsActive()) {
                if (IS_SHOOTING) {
                    shooterMotor.setVelocity(LAUNCHER_TARGET_VELOCITY);
                }

                if (!IS_SHOOTING) {
                    shooterMotor.setVelocity(0);
                }

                readControls();
                applyOutputs();

                double forward = gamepad1.right_trigger - gamepad1.left_trigger;
                double strafe = gamepad1.left_stick_x;
                double rotate = gamepad1.right_stick_x;

                drive.drive(forward, strafe, rotate);

                telemetry.addData("Intake power", intakePower);
                telemetry.addData("Shooter power", shooterPower);
                telemetry.addData("Transfer position", transferPosition);
                telemetry.addData("Shooter Power", shooterMotor.getPower());
                telemetry.update();
            }
        } finally {
            stopAll();
        }
    }

    private void initHardware() {
        intakeMotor = hardwareMap.get(
                DcMotor.class, "intakeMotor");
        shooterMotor = hardwareMap.get(
                DcMotorEx.class, "shooterMotor");

        intakeServo1 = hardwareMap.get(
                CRServo.class, "intakeServo1");
        intakeServo2 = hardwareMap.get(
                CRServo.class, "intakeServo2");
        transferServo = hardwareMap.get(
                CRServo.class, "transferServo");

        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(40, 0, 0, 12.5));

        stopAll();
    }

    private void readControls() {
        if (gamepad2.circle) {
            intakePower = 0;
        } else if (gamepad2.cross) {
            intakePower = 1;
        } else if (gamepad2.square) {
            intakePower = -1;
        }

        if (gamepad2.dpad_down) {
            IS_SHOOTING = false;
        }

        if (gamepad2.dpad_up) {
            IS_SHOOTING = true;
        }

//        if (gamepad1.dpad_up) {
//            shooterMotor.setPower(shooterMotor.getPower() + 0.05);
//        }
//
//        if (gamepad1.dpad_down) {
//            shooterMotor.setPower(shooterMotor.getPower() + 0.05);
//        }

        boolean rightPressed =
                gamepad2.dpad_right && !previousDpadRight;
        boolean leftPressed =
                gamepad2.dpad_left && !previousDpadLeft;

        if (gamepad2.dpad_right) {
            transferServo.setPower(0.6);
        } else {
            transferServo.setPower(0);
        }

        previousDpadRight = gamepad2.dpad_right;
        previousDpadLeft = gamepad2.dpad_left;
    }

    private void applyOutputs() {
        setIntake(intakePower);
//        shooterMotor.setPower(shooterPower);
    }

    private void setIntake(double power) {
        intakeServo1.setPower(power);
        intakeServo2.setPower(-power);
        intakeMotor.setPower(power);
    }

    private void stopAll() {
        setIntake(0);
        shooterMotor.setPower(0);
    }
}