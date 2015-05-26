public class SeamCarver {
	private Picture picture;

	// private double[][] energyMatrix;

	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture) {
		if (picture == null) {
			throw new NullPointerException();
		}
		this.picture = new Picture(picture);
		// energyMatrix = buildEnergyMatrix();
	}

	// current picture
	public Picture picture() {
		return picture;
	}

	// width of current picture
	public int width() {
		return picture().width();
	}

	// height of current picture
	public int height() {
		return picture().height();
	}

	// energy of pixel at column x and row y
	public double energy(int x, int y) {
		if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1) {
			throw new IndexOutOfBoundsException();
		}
		// energy of pixels at the border
		if (x == 0 || x == width() - 1) {
			return 195075.0;
		}
		if (y == 0 || y == height() - 1) {
			return 195075.0;
		}
		return xGrad(x, y) + yGrad(x, y);
	}

	// compute the square of x-gradient
	private double xGrad(int x, int y) {
		// absolute value in differences of red, green and blue components
		// between pixel (x + 1, y) and pixel (x − 1, y)
		double redX = Math.abs((double) picture().get(x + 1, y).getRed()
				- (double) picture().get(x - 1, y).getRed());
		double greenX = Math.abs((double) picture().get(x + 1, y).getGreen()
				- (double) picture().get(x - 1, y).getGreen());
		double blueX = Math.abs((double) picture().get(x + 1, y).getBlue()
				- (double) picture().get(x - 1, y).getBlue());
		return Math.pow(redX, 2) + Math.pow(greenX, 2) + Math.pow(blueX, 2);
	}

	// compute the square of y-gradient
	private double yGrad(int x, int y) {
		// absolute value in differences of red, green and blue components
		// between pixel (x, y + 1) and pixel (x, y - 1)
		double redY = Math.abs((double) picture().get(x, y + 1).getRed()
				- (double) picture().get(x, y - 1).getRed());
		double greenY = Math.abs((double) picture().get(x, y + 1).getGreen()
				- (double) picture().get(x, y - 1).getGreen());
		double blueY = Math.abs((double) picture().get(x, y + 1).getBlue()
				- (double) picture().get(x, y - 1).getBlue());
		return Math.pow(redY, 2) + Math.pow(greenY, 2) + Math.pow(blueY, 2);
	}

	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {
		double[][] transposedEnergyMatrix = buildTransposeMatrix();
		return findVerticalSeamInMatrix(transposedEnergyMatrix);
	}

	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		double[][] energyMatrix = buildEnergyMatrix();
		return findVerticalSeamInMatrix(energyMatrix);
	}

	private double[][] buildTransposeMatrix() {
		double[][] transposedEnergyMatrix = new double[width()][height()];
		for (int i = 0; i < width(); i++) {
			for (int j = 0; j < height(); j++) {
				transposedEnergyMatrix[i][j] = energy(i, j);
			}
		}
		return transposedEnergyMatrix;
	}

	private double[][] buildEnergyMatrix() {
		double[][] energyMatrix = new double[height()][width()];
		for (int i = 0; i < height(); i++) {
			for (int j = 0; j < width(); j++) {
				energyMatrix[i][j] = energy(j, i);
			}
		}
		return energyMatrix;
	}

	private int[] findVerticalSeamInMatrix(double[][] energyMatrix) {
		final int W = energyMatrix[0].length;
		final int H = energyMatrix.length;
		final double sourceEnergy = 195075.0;

		// array data structures for putting energy of each pixel, energy needed
		// to get to each pixel, last pixel in the lowest energy path and the
		// seam respectively
		double[][] energyTo = new double[H][W];
		int[][] edgeTo = new int[H][W];
		int[] seam = new int[H];

		// initialization
		for (int i = 0; i < H; i++) {
			for (int j = 0; j < W; j++) {
				if (i == 0) {
					energyTo[i][j] = sourceEnergy; // first row is the source
				} else {
					energyTo[i][j] = Double.POSITIVE_INFINITY;
				}
			}
		}

		// relax the energy of each pixel
		for (int i = 0; i < H - 1; i++) {
			for (int j = 0; j < W; j++) {
				for (int k = j - 1; k < j + 2; k++) {
					if (k >= 0 && k < W) {
						if (energyTo[i][j] + energyMatrix[i + 1][k] < energyTo[i + 1][k]) {
							// relax pixel (i+1, k)
							energyTo[i + 1][k] = energyTo[i][j]
									+ energyMatrix[i + 1][k];
							edgeTo[i + 1][k] = j;
						}
					}
				}
			}
		}

		// find the pixel with the lowest energy to get to in the last row
		double min = energyTo[H - 1][0];
		for (int i = 0; i < W; i++) {
			if (energyTo[H - 1][i] < min) {
				min = energyTo[H - 1][i];
				seam[H - 1] = i;
			}
		}

		// trace backward to get the lowest energy path
		for (int i = H - 2; i >= 0; i--) {
			seam[i] = edgeTo[i + 1][seam[i + 1]];
		}

		return seam;
	}

	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		final int H = height(), W = width();

		if (seam == null) {
			throw new NullPointerException();
		}
		if (seam.length != W || H <= 1) {
			throw new IllegalArgumentException();
		}

		Picture newPicture = new Picture(W, H - 1);
		for (int i = 0; i < W; i++) {
			for (int j = 0; j < H - 1; j++) {
				if (j < seam[i]) {
					newPicture.set(i, j, picture.get(i, j));
				} else {
					newPicture.set(i, j, picture.get(i, j + 1));
				}
			}
		}
		picture = newPicture;
	}

	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
		final int H = height(), W = width();

		if (seam == null) {
			throw new NullPointerException();
		}
		if (seam.length != H || W <= 1) {
			throw new IllegalArgumentException();
		}

		Picture newPicture = new Picture(W - 1, H);
		for (int i = 0; i < H; i++) {
			for (int j = 0; j < W - 1; j++) {
				if (j < seam[i]) {
					newPicture.set(j, i, picture.get(j, i));
				} else {
					newPicture.set(j, i, picture.get(j + 1, i));
				}
			}
		}
		picture = newPicture;
	}

	private boolean isValidSeam(int[] seam) {
		for (int i = 0; i < seam.length; i++) {
			if (i + 1 < seam.length && Math.abs(seam[i] - seam[i + 1]) > 1) {
				return false;
			}
		}
		return true;
	}

}
