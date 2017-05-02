package musicalRobot;

import modeselection.vision.landmarks.LandmarkTrainer;

public class musicalTrainer {
public static final String FILENAME = "musicTrainer.txt";
	
	public static void main(String[] args) {
		new LandmarkTrainer(FILENAME, 10, 8).run();
	}
}

