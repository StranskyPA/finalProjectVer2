package musicalRobot;

import java.io.FileNotFoundException;

import modeselection.vision.landmarks.LandmarkViewer;
import musicalRobot.musicalTrainer;

public class musicalViewer {
	public static void main(String[] args) throws FileNotFoundException {
		new LandmarkViewer(musicalTrainer.FILENAME).run();
	}
}
