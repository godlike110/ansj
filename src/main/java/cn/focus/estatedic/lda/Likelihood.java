package cn.focus.estatedic.lda;

 

public class Likelihood {

	public static double lda_ppl(Document[] data, double[][] beta,
			double[][] gammas, int m, int nclass) {
		double n = 0;
		int j;
		for (int i = 0; i < data.length; i++)
			if (data[i].len != -1) {
				for (j = 0; j < data[i].len; j++)
					n += data[i].cnt[j];
			}

		return Math.exp(-lda_lik(data, beta, gammas, m, nclass) / n);
	}

	public static double lda_lik(Document[] data, double[][] beta,
			double[][] gammas, int m, int nclass) {
		double[][] egammas;
		double z, lik;
		int i, j, k;
		int n;
		lik = 0;

		if ((egammas = Dmatrix.dmatrix(m, nclass)) == null) {
			System.err.print("lda_likelihood:: cannot allocate egammas.\n");
			System.exit(1);
		}

		Util.normalize_matrix_row(egammas, gammas, m, nclass);

		for (i = 0; i < data.length; i++)
			if (data[i].len != -1) {
				n = data[i].len;
				for (j = 0; j < n; j++) {
					for (k = 0, z = 0; k < nclass; k++)
						z += beta[data[i].id[j]][k] * egammas[i][k];
					lik += data[i].cnt[j] * Math.log(z);
				}
			}

		Dmatrix.free_dmatrix(egammas, m);
		return lik;

	}

}
