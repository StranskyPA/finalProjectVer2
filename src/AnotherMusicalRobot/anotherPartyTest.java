package AnotherMusicalRobot;

import java.io.File;
import java.io.IOException;
import AnotherMusicalRobot.Mode;
import AnotherMusicalRobot.Condition;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import modeselection.ModeSelector;
import modeselection.MotorFlagger;
import modeselection.Transitions;
import modeselection.vision.CameraFlagger;
import modeselection.vision.landmarks.LandmarkFlagger;
import AnotherMusicalRobot.partyRobotTrainer;

public class anotherPartyTest {
	public static void main(String[] args) throws IOException {
		
		File testSong1 = new File("test1.wav");
		File testSong2 = new File("dumb.wav");
		File testSong3 = new File("test3.wav");
		
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		LandmarkFlagger<Condition> landmarker = new LandmarkFlagger<>(partyRobotTrainer.FILENAME);
		landmarker
		//Find the nodes for each landmark using the viewer
		.add(Condition.GREEN, 4)
		.add(Condition.BLUE, 5)
		.add(Condition.ORANGE, 8)
		.add(Condition.NO_COLOR, 2,7,1,3);
		camera.addSub(landmarker);
		
		MotorFlagger<Condition> backupFlag = new MotorFlagger<>(Motor.D);
		backupFlag.add2(Condition.IN_TURN, Condition.TURN_DONE, i -> Math.abs(i) < Math.abs(255));
		
		Transitions<Condition,Mode>  firstLandmark = new Transitions<>();
		firstLandmark
		.add(Condition.BLUE, Mode.TURN_1)
		.add(Condition.NO_COLOR, Mode.FORWARD_1);
		
		Transitions<Condition,Mode>  secondLandmark = new Transitions<>();
		secondLandmark
		.add(Condition.ORANGE, Mode.TURN_2)
		.add(Condition.NO_COLOR, Mode.FORWARD_2);
		
		Transitions<Condition,Mode>  thirdLandmark = new Transitions<>();
		thirdLandmark 
		.add(Condition.GREEN, Mode.TURN_3)
		.add(Condition.NO_COLOR, Mode.FORWARD_2);
		
		Transitions<Condition,Mode> turn1 = new Transitions<>();
		turn1.add(Condition.TURN_DONE, Mode.FORWARD_2)
		.add(Condition.IN_TURN, Mode.TURN_1);
		
		Transitions<Condition,Mode> turn2 = new Transitions<>();
		turn2.add(Condition.TURN_DONE, Mode.FORWARD_3)
		.add(Condition.IN_TURN, Mode.TURN_2);
		
		Transitions<Condition,Mode> turn3 = new Transitions<>();
		turn3.add(Condition.TURN_DONE, Mode.FORWARD_1)
		.add(Condition.IN_TURN, Mode.TURN_3);
		
		ModeSelector<Condition,Mode> controller =
				new ModeSelector<>(Condition.class, Mode.class, Mode.FORWARD_1)
				.flagger(camera)
				.flagger(backupFlag)
				.mode(Mode.FORWARD_1, firstLandmark, () -> {
					Motor.A.setSpeed(225);
					Motor.D.setSpeed(225);
					Motor.A.forward();
					Motor.D.forward();
				})
				.mode(Mode.FORWARD_2, secondLandmark, () -> {
					Motor.A.setSpeed(225);
					Motor.D.setSpeed(225);
					Motor.A.forward();
					Motor.D.forward();
				})
				.mode(Mode.FORWARD_3, thirdLandmark, () -> {
					Motor.A.setSpeed(225);
					Motor.D.setSpeed(225);
					Motor.A.forward();
					Motor.D.forward();
				})
				.mode(Mode.TURN_1, turn1, () -> {
					Motor.D.resetTachoCount();
					Motor.A.stop(true);
					Motor.D.stop();
					Sound.playSample(testSong2, 50);
				//	Motor.D.resetTachoCount();
					Motor.A.setSpeed(90);
					Motor.D.setSpeed(90);
					Motor.A.backward();
					Motor.D.forward();
				})
				.mode(Mode.TURN_2, turn2, () -> {
					Motor.D.resetTachoCount();
					Motor.A.stop(true);
					Motor.D.stop();
					//Sound.playSample(testSong2, 50);
					//Motor.D.resetTachoCount();
					Motor.A.setSpeed(90);
					Motor.D.setSpeed(90);
					Motor.A.backward();
					Motor.D.forward();
				})
				.mode(Mode.TURN_3, turn3, () -> {
					Motor.D.resetTachoCount();
					Motor.A.stop(true);
					Motor.D.stop();
					//Sound.playSample(testSong3, 50);
					//Motor.D.resetTachoCount();
					Motor.A.setSpeed(90);
					Motor.D.setSpeed(90);
					Motor.A.backward();
					Motor.D.forward();
				});
		controller.control();
		
		System.exit(0);
	}
}