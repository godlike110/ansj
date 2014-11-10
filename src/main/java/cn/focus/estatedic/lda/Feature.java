package cn.focus.estatedic.lda;

import java.io.*;

public class Feature {

	public static int BUFSIZE = 65536;

	public static Document[] feature_matrix(String file_name, fakeInt maxid,
			fakeInt maxlen) {
		Document[] d;
		int n, m;
		//System.out.println(System.getProperty("user.dir"));
		//System.out.println(file_name);
		File fp = new File(file_name);
		//System.out.println(fp == null);
		BufferedReader reader = null;

		String line = null;
		maxid.setdata(-1);
		maxlen.setdata(0);

		m = file_lines(fp);

		System.out.println(m);
		d = new Document[m + 1];
		for (int i = 0; i < d.length; i++) {
			d[i] = new Document();
			// System.out.print(i+" " );
		}
		if (d == null) {
			System.err.print("d allocation error!\n");
			return null;
		}
		System.out.println(m);
		d[m].len = -1;

		n = 0;

		try {
			reader = new BufferedReader(new FileReader(fp));
			while ((line = reader.readLine()) != null) {
				int i, len;

				if (isspaces(line))
					continue;

				// split
				//System.out.println(line);
				String[] field = line.split("\t");
				len = field.length;

				// allocate
				if (len > maxlen.getdata())
					maxlen.setdata(len);
				if (!(len > 0)) {
					System.err.println("feature_matrix: suspicious line:"
							+ line);
					System.exit(1);
				}
				d[n].id = new int[len];
				d[n].cnt = new double[len];
				d[n].len = len;
				if ((d[n].id == null) || d[n].cnt == null) {
					System.err.println("feature_matrix: d allocation error");
					return null;
				}
				//System.out.println(len);
				for (i = 0; i < len; i++) {
					//System.out.println(field[i]);
					String[] ar = field[i].split(":");
					//System.out.println("s:"+ar[0]);
					if (ar[0] != "") {
						
						d[n].id[i] = Integer.valueOf(ar[0]) - 1;
						if (d[n].id[i] >= maxid.getdata())
							maxid.setdata(d[n].id[i] + 1);
						d[n].cnt[i] = Double.valueOf(ar[1]);
						
					} else
						break;
				}
				n++;
			}

		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}

		}

		return d;
	}

	public static int file_lines(File fp) {
		int n = 0;
		BufferedReader reader = null;
		String tempstring = null;
		try {
			reader = new BufferedReader(new FileReader(fp));
			while ((tempstring = reader.readLine()) != null) {
				if (!isspaces(tempstring))
					n++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return n;
	}

	public static boolean isspaces(String s) {
		int n = s.length();
		int i = 0;
		byte[] temp = s.getBytes();
		while (i < n) {
			if (!(temp[i] == ' '))
				return false;
			i++;
		}
		return true;
	}

	public static void free_feature_matrix(Document[] matrix) {
		matrix = null;
		System.gc();

	}

}
