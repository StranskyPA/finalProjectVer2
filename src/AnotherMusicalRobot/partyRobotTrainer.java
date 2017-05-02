package AnotherMusicalRobot;

import modeselection.vision.landmarks.LandmarkTrainer;

public class partyRobotTrainer {
public static final String FILENAME = "partyRobotTrainer.txt";
	
	public static void main(String[] args) {
		new LandmarkTrainer(FILENAME, 10, 8).run();
	}
}