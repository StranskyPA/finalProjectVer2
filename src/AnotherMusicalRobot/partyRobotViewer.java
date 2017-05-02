package AnotherMusicalRobot;

import java.io.FileNotFoundException;

import modeselection.vision.landmarks.LandmarkViewer;
import AnotherMusicalRobot.partyRobotTrainer;

public class partyRobotViewer {
	public static void main(String[] args) throws FileNotFoundException {
		new LandmarkViewer(partyRobotTrainer.FILENAME).run();
	}
}