import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class VisualTester extends PApplet {
	ArrayList<PImage> images;
	PImage current_image;
	int currentImageIndex = 0;
	int w = 1200;
	int h = 900;

	public void setup() {
		size(w, h);
		images = PDFHelper.getPImagesFromPdf("/omrtest.pdf");
	}

	public void draw() {
		background(255);
		if (images.size() > 0) {
			current_image = images.get(currentImageIndex);
			image(current_image, 0, 0); // display image i
		}

		stroke(0, 0, 255);
		if (OpticalMarkReader.isBubbled(mouseY, mouseX, 20, 20, .8, current_image)) {
			fill(0, 0, 255);
			rect(mouseX, mouseY, 20, 20);
		}
		text("" + mouseX + "   " + mouseY, mouseX, mouseY);

		noFill();
		stroke(255, 0, 0);
		strokeWeight(1);

		int C = 0;

		double x = 125;
		double y = 468;
		double xs = 38;
		double ys = 37.5;
		double num = 10;// vertical

		int BOXx = 30;
		int BOXy = 30;

		double CUT = .92;

		for (double i = x; i < x + xs * 5; i += xs) {
			for (double j = y; j < y + ys * num; j += ys) {
				boolean a = OpticalMarkReader.isBubbled((int) j, (int) i, BOXx, BOXy, CUT, images.get(0));
				boolean b = OpticalMarkReader.isBubbled((int) j, (int) i, BOXx, BOXy, CUT, current_image);
				// text("" + b,(int)i,(int)j);
				if (a != b) {
					C++;
				}

				if (b) {
					stroke(0, 0, 255);
					rect((int) i, (int) j, BOXx, BOXy);
				} else {
					stroke(255, 0, 0);
					rect((int) i, (int) j, BOXx, BOXy);
				}

			}
		}
		System.out.println((num - C) / (num));
		// delay(1000);

	}

	public void mouseReleased() {
		currentImageIndex = (currentImageIndex + 1) % images.size(); // increment
																		// current
																		// image
	}

	public void saveScores() {
		String OUT = "";

		int C = 0;

		double x = 125;
		double y = 468;
		double xs = 38;
		double ys = 37.5;
		double num = 25;// vertical

		int BOXx = 30;
		int BOXy = 30;

		double CUT = .92;

		for (PImage image : images) {
			int Question = 0;
			for (int set = 0; set < 4; set++) {
				for (double j = y; j < y + ys * num; j += ys) {
					Question++;
					boolean isCorrect = true;
					for (double i = x; i < x + xs * 5; i += xs) {

						boolean a = OpticalMarkReader.isBubbled((int) j, (int) i + 280 * set, BOXx, BOXy, CUT,
								images.get(0));
						boolean b = OpticalMarkReader.isBubbled((int) j, (int) i + 280 * set, BOXx, BOXy, CUT, image);
						if (a && !b) {
							C++;
							isCorrect = false;
							break;
						}

					}
					if (isCorrect) {
						OUT += Question + " G,";
					} else {
						OUT += Question + " X,";
					}
				}
				OUT += " ::: " + (num - C) / (num) + "/n";
			}
		}
		
		writeDataToFile("../Grades",OUT); // write data to file
	}

	public static void writeDataToFile(String filePath, String data) {
		File outFile = new File(filePath);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
			writer.write(data);
		} catch (Exception e) {

		}
	}
}
