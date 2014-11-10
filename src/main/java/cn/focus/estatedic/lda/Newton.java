package cn.focus.estatedic.lda;

public class Newton {
	public static final int MAX_NEWTON_ITERATION = 20;
	public static final int MAX_RECURSION_LIMIT = 20;

	public static void newton_alpha(double[] alpha, double[][] gammas, int M,
			int K, int level) {
		int i, j, t;
		double[] g, h, pg, palpha;
		double z, sh, hgz;
		double psg, spg, gs;
		double alpha0, palpha0;

		/* allocate arrays */
		if ((g = new double[K]) == null) {
			System.err.print("newton:: cannot allocate g.\n");
			return;
		}
		if ((h = new double[K]) == null) {
			System.err.print("newton:: cannot allocate h.\n");
			return;
		}
		if ((pg = new double[K]) == null) {
			System.err.print("newton:: cannot allocate pg.\n");
			return;
		}
		if ((palpha = new double[K]) == null) {
			System.err.print("newton:: cannot allocate palpha.\n");
			return;
		}

		/* initialize */
		if (level == 0) {
			for (i = 0; i < K; i++) {
				for (j = 0, z = 0; j < M; j++)
					z += gammas[j][i];
				alpha[i] = z / (M * K);
			}
		} else {
			for (i = 0; i < K; i++) {
				for (j = 0, z = 0; j < M; j++)
					z += gammas[j][i];
				alpha[i] = z / (M * K * Math.pow(10, level));
			}
		}

		psg = 0;
		for (i = 0; i < M; i++) {
			for (j = 0, gs = 0; j < K; j++)
				gs += gammas[i][j];
			psg += Gamma.psi(gs);
		}
		for (i = 0; i < K; i++) {
			for (j = 0, spg = 0; j < M; j++)
				spg += Gamma.psi(gammas[j][i]);
			pg[i] = spg - psg;
		}

		/* main iteration */
		for (t = 0; t < MAX_NEWTON_ITERATION; t++) {
			for (i = 0, alpha0 = 0; i < K; i++)
				alpha0 += alpha[i];
			palpha0 = Gamma.psi(alpha0);

			for (i = 0; i < K; i++)
				g[i] = M * (palpha0 - Gamma.psi(alpha[i])) + pg[i];
			for (i = 0; i < K; i++)
				h[i] = -1 / Gamma.ppsi(alpha[i]);
			for (i = 0, sh = 0; i < K; i++)
				sh += h[i];

			for (i = 0, hgz = 0; i < K; i++)
				hgz += g[i] * h[i];
			hgz /= (1 / Gamma.ppsi(alpha0) + sh);

			for (i = 0; i < K; i++)
				alpha[i] = alpha[i] - h[i] * (g[i] - hgz) / M;

			for (i = 0; i < K; i++)
				if (alpha[i] < 0) {
					if (level >= MAX_RECURSION_LIMIT) {
						System.err
								.print("newton:: maximum recursion limit reached.\n");
						System.exit(1);
					} else {
						g = null;
						h = null;
						pg = null;
						palpha = null;
						System.gc();
						// recursive
						newton_alpha(alpha, gammas, M, K, 1 + level);
						return;
					}
				}

			if ((t > 0) && Util.converged(alpha, palpha, K, 1.0e-4)) {
				g = null;
				h = null;
				pg = null;
				palpha = null;
				System.gc();
				return;
			} else
				for (i = 0; i < K; i++)
					palpha[i] = alpha[i];

		}
		System.err.println("newton:: maximum iteration reached. t = " + t);

		return;

	}

}
