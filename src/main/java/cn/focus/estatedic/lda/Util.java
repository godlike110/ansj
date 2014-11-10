package cn.focus.estatedic.lda;

public class Util {

	public static boolean converged(double[] u, double[] v, int n,
			double threshold) {
		/* return 1 if |a - b|/|a| < threshold */
		double us = 0;
		double ds = 0;
		double d;
		int i;

		for (i = 0; i < n; i++)
			us += u[i] * u[i];

		for (i = 0; i < n; i++) {
			d = u[i] - v[i];
			ds += d * d;
		}

		if (Math.sqrt(ds / us) < threshold)
			return true;
		else
			return false;

	}

	public static void normalize_matrix_col(double[][] dst, double[][] src,
			int rows, int cols) {
		/* column-wise normalize from src -> dst */
		double z;
		int i, j;

		for (j = 0; j < cols; j++) {
			for (i = 0, z = 0; i < rows; i++)
				z += src[i][j];
			for (i = 0; i < rows; i++)
				dst[i][j] = src[i][j] / z;
		}
	}

	public static void normalize_matrix_row(double[][] dst, double[][] src,
			int rows, int cols) {
		/* row-wise normalize from src -> dst */
		int i, j;
		double z;

		for (i = 0; i < rows; i++) {
			for (j = 0, z = 0; j < cols; j++)
				z += src[i][j];
			for (j = 0; j < cols; j++)
				dst[i][j] = src[i][j] / z;
		}
	}

	public static String rtime(double t) {
		int hour, min, sec;
		t /= 1000;
		hour = (int) Math.floor((long) t / 60 / 60);
		min = (int) Math.floor(((long) t % (60 * 60)) / 60);
		sec = (int) Math.floor((long) t % 60);

		return String.valueOf(hour) + ":" + String.valueOf(min) + ":"
				+ String.valueOf(sec);
	}

}
