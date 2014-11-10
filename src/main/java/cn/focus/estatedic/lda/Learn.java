package cn.focus.estatedic.lda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
 

public class Learn {

	public static void load(double[] alpha, double[][] beta, int nclass,
			int nlex) {
		File fp1 = new File("alpha");
		File fp2 = new File("beta");
		BufferedReader reader1 = null, reader2 = null;

		try {
			reader1 = new BufferedReader(new FileReader(fp1));
			reader2 = new BufferedReader(new FileReader(fp2));
			String temp = null;
			/*
			 * read alpha
			 */
			while ((temp = reader1.readLine()) != null) {
				String[] d = temp.split(" ");
				for (int i = 0; i < nclass; i++)
					alpha[i] = Double.parseDouble(d[i]);
			}
			/*
			 * read beta
			 */
			int j = 0;
			while ((temp = reader2.readLine()) != null) {
				String[] d = temp.split(" ");
				for (int i = 0; i < nclass; i++)
					beta[j][i] = Double.parseDouble(d[i]);
				j++;
			}
			System.out.println(j);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				reader1.close();
				reader2.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void save(double[] alpha, double[][] beta, int nclass,
			int nlex) {
		File fp1 = new File("alpha_java");
		File fp2 = new File("beta_java");
		BufferedWriter writer1 = null, writer2 = null;

		try {
			writer1 = new BufferedWriter(new FileWriter(fp1));
			writer2 = new BufferedWriter(new FileWriter(fp2));
			String temp = null;
			/*
			 * write alpha
			 */

			for (int i = 0; i < nclass; i++)
				writer1.write(String.valueOf(alpha[i]) + " ");
			writer1.write("\n");

			/*
			 * read beta
			 */
			for (int i = 0; i < nlex; i++) {
				for (int j = 0; j < nclass; j++)
					writer2.write(String.valueOf(beta[i][j]) + " ");
				writer2.write("\n");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer1.close();
				writer2.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public static void lda_learn(final Document[] data, double[] alpha,
			double[][] beta, int nclass, int nlex, int dlenmax, int emmax,
			int demmax, double epsilon) {
		double[] gamma, nt, pnt, ap;
		double[][] gammas, betas, q;
		double z, ppl, pppl = 0;
		Document[] dp;
		int i, j, t, n;
		long start;
		long elapsed;
		/*
		 * randomize a seed
		 */
		// 需要随机初始化种子
		// Math.random();

		n = data.length - 1;

		/*
		 * initialize parameters
		 */
		for (i = 0; i < nclass; i++)
			alpha[i] = Math.random();
		for (i = 0, z = 0; i < nclass; i++)
			z += alpha[i];
		for (i = 0; i < nclass; i++) {
			alpha[i] = alpha[i] / z;
		}
		/*
		 * sort alpha initially
		 */
		Arrays.sort(alpha);// from low to up?
		for (i = 0; i < nclass; i++) {

			System.out.println(alpha[i]);
		}

		for (j = 0; j < nclass; j++) {
			for (i = 0, z = 0; i < nlex; i++) {
				beta[i][j] = Math.random() * 10;
				z += beta[i][j];
			}
			for (i = 0; i < nlex; i++) {
				beta[i][j] = beta[i][j] / z;
			}
		}

		/*
		 * initialize posteriors
		 */

		// load(alpha,beta,nclass,nlex);
		// save(alpha,beta,nclass,nlex);

		if ((gammas = Dmatrix.dmatrix(n, nclass)) == null) {
			System.err.print("lda_learn:: cannot allocate gammas.\n");
			return;
		}

		if ((betas = Dmatrix.dmatrix(nlex, nclass)) == null) {
			System.err.print("lda_learn:: cannot allocate betas.\n");
			return;
		}

		/*
		 * initialize buffers
		 */
		if ((q = Dmatrix.dmatrix(dlenmax, nclass)) == null) {
			System.err.print("lda_learn:: cannot allocate q.\n");
			return;
		}
		if ((gamma = new double[nclass]) == null) {
			System.err.print("lda_learn:: cannot allocate gamma.\n");
			return;
		}
		if ((ap = new double[nclass]) == null) {
			System.err.print("lda_learn:: cannot allocate ap.\n");
			return;
		}
		if ((nt = new double[nclass]) == null) {
			System.err.print("lda_learn:: cannot allocate nt.\n");
			return;
		}
		if ((pnt = new double[nclass]) == null) {
			System.err.print("lda_learn:: cannot allocate pnt.\n");
			return;
		}
		System.out.println("Number of documents          = " + n);
		System.out.println("Number of words              = " + nlex);
		System.out.println("Number of latent classes     = " + nclass);
		System.out.println("Number of outer EM iteration = " + emmax);
		System.out.println("Number of inner EM iteration = " + demmax);
		System.out.println("Convergence threshold        = " + epsilon);

		/*
		 * learn main
		 */
		start = System.currentTimeMillis();
		for (t = 0; t < emmax; t++) {
			System.out.print("iteration  " + (t + 1) + " ");
			System.out.flush();

			/*
			 * 
			 * VB-E step
			 */
			/* iterate for data */
			// System.out.print(data.length)
			for (i = 0; i < data.length; i++)
				if (data[i].len > 0) {
					// System.out.println(data[i].id[0]);
					Vbem.vbem(data[i], gamma, q, nt, pnt, ap, alpha, beta,
							data[i].len, nclass, demmax);
					// System.out.println(gamma[0]);
					accum_gammas(gammas, gamma, i, nclass);
					accum_betas(betas, q, nclass, data[i]);

				} else {

					// System.err.println("document length -1!"+i);
				}
			/*
			 * 
			 * VB-M step
			 */
			/* Newtown-Raphson for alpha */
			Newton.newton_alpha(alpha, gammas, n, nclass, 0);
			// System.out.println("alpha:"+alpha[0]+" gammas:"+gammas[0][0]);
			/* MLE for beta */
			Util.normalize_matrix_col(beta, betas, nlex, nclass);
			/* clean buffer */
			for (i = 0; i < nlex; i++)
				for (j = 0; j < nclass; j++)
					betas[i][j] = 0;
			/*
			 * converge?
			 */
			// System.out.println("beta:"+beta[0][0]);
			ppl = Likelihood.lda_ppl(data, beta, gammas, n, nclass);

			elapsed = System.currentTimeMillis() - start;
			System.out.println("PPL= " + ppl);
			System.out.flush();
			// /...
			if ((t > 1) && (Math.abs((ppl - pppl) / pppl) < epsilon)) {
				if (t < 5) {
					Dmatrix.free_dmatrix(gammas, n);
					Dmatrix.free_dmatrix(betas, nlex);
					Dmatrix.free_dmatrix(q, dlenmax);
					gamma = null;
					ap = null;
					nt = null;
					pnt = null;
					System.gc();
					System.out.print("\nearly convergence. restarting..\n");
					lda_learn(data, alpha, beta, nclass, nlex, dlenmax, emmax,
							demmax, epsilon);
					return;
				} else {
					System.out.println("\nconverged" + Util.rtime(elapsed));
					break;
				}

			}
			pppl = ppl;
			/*
			 * ETA
			 */
			System.out.print("ETA:"
					+ Util.rtime(elapsed * ((double) emmax / (t + 1) - 1))
					+ " (" + (int) ((double) elapsed / (t + 1) + 0.5)
					+ " sec/step)\r");

		}
		
		Writer.write_matrix(new File("model.gammas"), gammas, n, nclass);
		Dmatrix.free_dmatrix(gammas, n);
		Dmatrix.free_dmatrix(betas, nlex);
		Dmatrix.free_dmatrix(q, dlenmax);
		gamma = null;
		ap = null;
		nt = null;
		pnt = null;
		System.gc();
		return;
	}

	public static void accum_gammas(double[][] gammas, double[] gamma, int n,
			int K) {
		/* gammas(n,:) = gamma for Newton-Raphson of alpha */
		int k;
		for (k = 0; k < K; k++)
			gammas[n][k] = gamma[k];
		return;
	}

	public static void accum_betas(double[][] betas, double[][] q, int K,
			Document dp) {
		int i, k;
		int n = dp.len;

		for (i = 0; i < n; i++)
			for (k = 0; k < K; k++)
				betas[dp.id[i]][k] += q[i][k] * dp.cnt[i];
	}

}
