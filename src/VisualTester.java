import java.awt.image.BufferedImage;
import java.io.File;
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

		text(mouseX + "   " + mouseY, mouseX, mouseY);
		if (OpticalMarkReader.isBubbled(mouseY, mouseX, 20, 20, .8, current_image)) {
			fill(255, 0, 0);
			rect(mouseX, mouseY, 22, 22);
		}

		noFill();
		stroke(0, 0, 0);
		strokeWeight(1);

		double C = 0;

		double x = 125;
		double y = 468;
		double xs = 38;
		double ys = 37.5;
		double num = 10;

		double CUT = .95;

		for (double i = x; i < x + xs * 5; i += xs) {
			for (double j = y; j < y + ys * num; j += ys) {
				boolean a = OpticalMarkReader.isBubbled((int) i, (int) j, (int) xs, (int) ys, CUT, images.get(0));
				boolean b = OpticalMarkReader.isBubbled((int) i, (int) j, (int) xs, (int) ys, CUT, current_image);
				System.out.println(a + "\t" + b);

				stroke(255, 0, 0);
				text(" " + (float)OpticalMarkReader.getDarkness((int) i, (int) j, (int) xs, (int) ys, CUT, current_image),
						(float) i, (float) j);

				if (a != b) {
					C++;
					stroke(0, 0, 0);
					rect((int) i, (int) j, (int) xs, (int) ys);
				} else {
					stroke(255, 0, 0);
					rect((int) i, (int) j, (int) xs, (int) ys);
				}

			}
		}
		System.out.println((num - C) / (num));
	}

	public void mouseReleased() {
		currentImageIndex = (currentImageIndex + 1) % images.size(); // increment
																		// current
																		// image
	}
}
