import processing.core.PImage;

/***
 * Class to perform image processing for optical mark reading
 * 
 */
public class OpticalMarkReader {

	/***
	 * Method to do optical mark reading on page image. Return an AnswerSheet
	 * object representing the page answers.
	 * 
	 * @param image
	 * @return
	 */
	public AnswerSheet processPageImage(PImage image) {
		image.filter(PImage.GRAY);
		return null;
	}

	public static int getPixelAt(int row, int col, PImage image) {

		int index = row * image.width + col;

		return image.pixels[index] & 255;
	}

	public static boolean isBubbled(int r, int c, int h, int w, double p, PImage image) {
		long sum = 0;
		for (int i = r; i < r + h; i++) {
			for (int j = c; j < c + w; j++) {
				sum += getPixelAt(i, j, image);
			}
		}
		double d = (sum / ((double) (w * h * 255)));
		return (d < p);
	}

	public static double getDarkness(int r, int c, int h, int w, double p, PImage image) {
		long sum = 0;
		for (int i = r; i < r + h; i++) {
			for (int j = c; j < c + w; j++) {
				sum += getPixelAt(i, j, image);
			}
		}
		return (sum / ((double) ((long) (w * h) * (long) (255))));

	}
}
