package musicalRobot;

import java.io.File;
import java.io.IOException;
import musicalRobot.Condition;
import musicalRobot.Mode;
import musicalRobot.musicalTrainer;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import modeselection.MotorFlagger;
import modeselection.StateClassifier;
import modeselection.planning.Executor;
import modeselection.planning.GoalPicker;
import modeselection.planning.Planner;
import modeselection.vision.CameraFlagger;
import modeselection.vision.landmarks.LandmarkFlagger;

public class musicTestOne {
	public static void main(String[] args) throws IOException {
		File testSong1 = new File("test1.wav");
		File testSong2 = new File("dumb.wav");
		File testSong3 = new File("putFileNameHere.wav");
		
		MotorFlagger<Condition> backupFlag = new MotorFlagger<>(Motor.D);
		backupFlag.add2(Condition.STOP_1_DONE, Condition.BUSY, i -> Math.abs(i) < Math.abs(1));
		backupFlag.add2(Condition.STOP_2_DONE, Condition.BUSY, i -> Math.abs(i) < Math.abs(2));
		backupFlag.add2(Condition.STOP_3_DONE, Condition.BUSY, i -> Math.abs(i) < Math.abs(3));
		
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		LandmarkFlagger<Condition> landmarker = new LandmarkFlagger<>(musicalTrainer.FILENAME);
		//change these values
		landmarker.add(Condition.NO_OBJECT, 1, 4, 5, 7)
		  .add(Condition.OBJECT_ONE, 3)
		  .add(Condition.OBJECT_TWO, 6)
		  .add(Condition.OBJECT_THREE, 9)
		  .add(Condition.VEER_LEFT, 0, 8, 2);
		camera.addSub(landmarker);
		StateClassifier<Condition> sensors = new StateClassifier<>(Condition.class);
		sensors.add(camera);
		//sensors.add(backupFlag);
		
		Planner<Condition,Mode> planner = new Planner<>(Condition.class);
		planner.add(Condition.NO_OBJECT, Mode.FORWARD, Condition.OBJECT_ONE, Condition.OBJECT_TWO, Condition.OBJECT_THREE)
		.add(Condition.OBJECT_ONE, Mode.STOP_1, Condition.STOP_1_DONE)
		.add(Condition.OBJECT_TWO, Mode.STOP_2, Condition.STOP_2_DONE)
		.add(Condition.OBJECT_THREE, Mode.STOP_3, Condition.STOP_3_DONE)
		.add(Condition.STOP_1_DONE, Mode.TURN,  Condition.NO_OBJECT)
		.add(Condition.STOP_2_DONE, Mode.TURN,  Condition.NO_OBJECT)
		.add(Condition.STOP_3_DONE, Mode.TURN,  Condition.NO_OBJECT)
		.add(Condition.VEER_LEFT, Mode.TURN, Condition.NO_OBJECT, Condition.OBJECT_ONE, Condition.OBJECT_TWO, Condition.OBJECT_THREE);
		
		Executor<Condition,Mode> executor = new Executor<>(Mode.class, Mode.FORWARD);
		executor.mode(Mode.FORWARD, () -> {
					Motor.D.resetTachoCount();
					Motor.A.setSpeed(180);
					Motor.D.setSpeed(180);
					Motor.A.forward();
					Motor.D.forward();
				})
				.mode(Mode.STOP_1, () -> {
					Motor.D.resetTachoCount();
					Motor.A.stop(true);
					Motor.D.stop();
					Sound.playSample(testSong1, 80);
					Motor.A.forward();
					Motor.D.forward();
				})
				.mode(Mode.STOP_2, () -> {
					Motor.D.resetTachoCount();
					Motor.A.stop(true);
					Motor.D.stop();
					Sound.playSample(testSong2, 80);
					Motor.A.forward();
					Motor.D.forward();
				})
				.mode(Mode.STOP_3, () -> {
					Motor.D.resetTachoCount();
					Motor.A.stop(true);
					Motor.D.stop();
					Sound.playSample(testSong3, 80);
					Motor.A.forward();
					Motor.D.forward();
				})
				.mode(Mode.TURN, () -> {
					Motor.D.resetTachoCount();
					Motor.A.backward();
					Motor.D.forward();
				});
		
		GoalPicker<Condition,Mode> controller = new GoalPicker<>(planner, executor, sensors); 
		controller.control();
		
		System.exit(0);
	}

}
