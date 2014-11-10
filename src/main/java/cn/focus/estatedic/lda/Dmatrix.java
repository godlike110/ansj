package cn.focus.estatedic.lda;

import java.io.*;

public class Dmatrix {

	public static double[][] dmatrix(int rows, int cols) {
		double[][] matrix;
		int i;
		matrix = new double[rows][cols];
		return matrix;
	}

	public static void free_dmatrix(double[][] matrix, int rows) {
		matrix = null;
		System.gc();
	}

}
