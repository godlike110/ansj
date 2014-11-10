package cn.focus.estatedic.lda;


public class Vbem {

	public static void vbem(Document d, double[] gamma, double[][] q,
			double[] nt, double[] pnt, double[] ap, double[] alpha,
			double[][] beta, int L, int K, int emmax) {
		int j, k, l;
		double z;
		for (k = 0; k < K; k++)
			nt[k] = (double) L / K;
		for (j = 0; j < emmax; j++) {
			/* vb-estep */
			for (k = 0; k < K; k++)
				ap[k] = Math.exp(Gamma.psi(alpha[k] + nt[k]));

			/* accumulate q */

			for (l = 0; l < L; l++)
				for (k = 0; k < K; k++)
					q[l][k] = beta[d.id[l]][k] * ap[k];
			/* normalize q */
			for (l = 0; l < L; l++) {
				z = 0;
				for (k = 0; k < K; k++)
					z += q[l][k];
				for (k = 0; k < K; k++)
					q[l][k] /= z;
			}
			/* vb-mstep */
			for (k = 0; k < K; k++) {
				z = 0;
				for (l = 0; l < L; l++)
					z += q[l][k] * d.cnt[l];
				nt[k] = z;
			}
			/* converge? */
			if ((j > 0) && Util.converged(nt, pnt, K, 1.0e-2))
				break;
			for (k = 0; k < K; k++)
				pnt[k] = nt[k];

		}
		for (k = 0; k < K; k++)
			gamma[k] = alpha[k] + nt[k];

		return;

	}

}
