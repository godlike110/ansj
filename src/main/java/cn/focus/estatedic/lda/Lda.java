package cn.focus.estatedic.lda;

import gnu.getopt.Getopt;

import java.io.File;
 

public class Lda {

	public static String LDA_COPYRIGHT = "this code is coded by gga with reference a vision of C by Blei.\n";
	public static int CLASS_DEFAULT = 50;
	public static int EMMAX_DEFAULT = 100;
	public static int DEMMAX_DEFAULT = 20;
	public static double EPSILON_DEFAULT = 1.0e-4;

	public static void usage() {
		System.out.print(LDA_COPYRIGHT);
		System.out
				.print("usage: lda -N classes [-I emmax] [-D demmax] [-E epsilon] train model\n");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Document[] data;
		double[] alpha;
		double beta[][];
		// FILE ap[],bp[];
		File ap, bp;

		int c;
		fakeInt nlex = new fakeInt(), dlenmax = new fakeInt();
		int nclass = CLASS_DEFAULT;
		int emmax = EMMAX_DEFAULT;
		int demmax = DEMMAX_DEFAULT;
		double epsilon = EPSILON_DEFAULT;
		Getopt g = new Getopt("Lda", args, "N:I:D:E:h");
		usage();
		while ((c = g.getopt()) != -1) {
			String optarg = g.getOptarg();
			switch (c) {
			case 'N':
				nclass = Integer.parseInt(optarg);
				break;
			case 'I':
				emmax = Integer.parseInt(optarg);
				break;
			case 'D':
				demmax = Integer.parseInt(optarg);
				break;
			case 'E':
				epsilon = Integer.parseInt(optarg);
				break;
			case 'h':
				usage();
				break;
			default:
				usage();
				break;
			}

		}
		int optind = g.getOptind();
		System.out.println(optind);
		for (int i = 0; i < args.length; i++)
			System.out.println(args[i]);
		if (!(args.length - optind == 2))
			usage();
		System.out.println("OK");

		ap = new File(args[optind + 1] + ".alpha");
		bp = new File(args[optind + 1] + ".beta");

		/*
		 * open data
		 */
		// System.out.println(args[optind]);
		System.out.println(System.getProperty("user.dir"));
		if ((data = Feature.feature_matrix(args[optind], nlex, dlenmax)) == null) {
			System.err.print("lda:: cannot open training data.\n");
			System.exit(1);
		}
		// System.out.println(nlex.getdata());
		/*
		 * allocate parameters
		 */
		if ((alpha = new double[nclass]) == null) {
			System.err.print("lda:: cannot allocate alpha.\n");
			System.exit(1);
		}

		if ((beta = Dmatrix.dmatrix(nlex.getdata(), nclass)) == null) {
			System.err.print("lda:: cannot allocate beta.\n");
			System.exit(1);
		}

		/*
		 * open model outputs
		 */

		if (ap == null || bp == null) {
			System.out.print("lda:: cannot open model outputs.\n");
			System.exit(1);
		}

		Learn.lda_learn(data, alpha, beta, nclass, nlex.getdata(),
				dlenmax.getdata(), emmax, demmax, epsilon);
		Writer.lda_write(ap, bp, alpha, beta, nclass, nlex.getdata());

		Feature.free_feature_matrix(data);
		Dmatrix.free_dmatrix(beta, nlex.getdata());
		alpha = null;
		System.gc();

		// fclose(ap);

	}

}
