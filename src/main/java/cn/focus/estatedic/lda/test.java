package cn.focus.estatedic.lda;

 

public class test {
	public final static int y = 10;
	public final Document q = new Document();

	public static class fakeInt {
		private int data;

		public fakeInt(int x) {
			data = x;
		}

		public int getdata() {
			return data;
		}

		public void setdata(int x) {
			data = x;
		}
	}

	public static void main(String[] args) {
		fakeInt i = new fakeInt(10);
		fakeInt j = new fakeInt(20);
		exange(i, j);
		Document[] q = new Document[10];
		Document p = new Document();
		System.out.println(q == null);
		for (int l = 0; l < q.length; l++)
			q[l] = new Document();
		q[1].len = 17;
		System.out.println(q[1].len);

		// y=3;

		System.out.println("结果为:\n" + i.getdata());
		System.out.println(j.getdata());
		Double[][] x;
		if ((x = new Double[10][10]) == null)
			System.out.print("no");
		else
			System.out.println("OK" + x[0][0]);
		x[1][0] = 1.0;
		System.out.println(x[1][0]);
		make(x[1]);
		System.out.println(x[1][0]);
		double t = -1.0 / 0.0;
		double[][] td = new double[2][2];
		td[1][1] = 342;
		System.out.println(td[1][1]);

		System.out.println(-t);
		System.out.println(Double.isInfinite(t));
		System.out.println(Math.random());
		System.out.println(Double.isNaN(t));
		System.out.println(Math.pow(10, 2));
	}

	public static void make(final Double[] x) {
		x[0] = 1.6;
	}

	public static void exange(fakeInt i, fakeInt j) {
		fakeInt t = new fakeInt(i.getdata());
		i.setdata(j.getdata());
		j.setdata(t.getdata());
		// i = new fakeInt(j.getdata());
		// j = new fakeInt(t.getdata());
		Integer a;
		a = 10;

	}
}
/*
 * public class test {
 * 
 * 
 * public static void change(Integer a ) { System.out.println(a); a= new
 * Integer(20); System.out.println(a); } public static void main(String[] args)
 * { // TODO Auto-generated method stub Integer a=new Integer(100);
 * System.out.println(a); change(a); System.out.println(a);
 * 
 * }
 * 
 * }
 */
