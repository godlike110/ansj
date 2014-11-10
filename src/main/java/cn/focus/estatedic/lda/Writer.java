package cn.focus.estatedic.lda;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {

	public static void lda_write(File ap, File bp, double[] alpha,
			double[][] beta, int nclass, int nlex) {
		System.out.print("writing model..\n");
		System.out.flush();
		write_vector(ap, alpha, nclass);
		write_matrix(bp, beta, nlex, nclass);
		System.out.print("done.\n");
		System.out.flush();
	}

	public static void write_vector(File fp, double[] vector, int n) {
		int i;
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(fp));
			for (i = 0; i < n; i++)
				writer.write(vector[i] + "\t");
			writer.write("\n");

		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void write_matrix(File fp, double[][] matrix, int rows,
			int cols) {
		int i, j;
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(fp));
			// ps:the output format
			for (i = 0; i < rows; i++){
				for (j = 0; j < cols; j++)
					writer.write(matrix[i][j] + "\t");
					writer.write("\n");
			}
			writer.write("\n");
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
