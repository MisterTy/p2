package Auxillary;
// Source: http://www.pcs.cnu.edu/~bbradie/cpp/interp.C

public class SplineNAK {

	public static void tridiagonal(int n, double[] c, double[] a, double[] b,
			double[] r) {
		int i;

		for (i = 0; i < n - 1; i++) {
			b[i] /= a[i];
			a[i + 1] -= c[i] * b[i];
		}

		r[0] /= a[0];
		for (i = 1; i < n; i++)
			r[i] = (r[i] - c[i - 1] * r[i - 1]) / a[i];

		for (i = n - 2; i >= 0; i--)
			r[i] -= r[i + 1] * b[i];

	}

	/**
	 * <pre>
	 *     PURPOSE:
	 *          determine the coefficients for the 'not-a-knot'
	 *          cubic spline for a given set of data
	 * 
	 * 
	 *     CALLING SEQUENCE:
	 *          cubic_nak ( n, x, f, b, c, d );
	 * 
	 * 
	 *     INPUTS:
	 *          n	number of interpolating points
	 *          x	array containing interpolating points
	 *          f	array containing function values to
	 * 			    be interpolated;  f[i] is the function
	 * 			    value corresponding to x[i]
	 *          b	array of size at least n; contents will
	 * 			    be overwritten
	 *          c	array of size at least n; contents will
	 * 			    be overwritten
	 *          d	array of size at least n; contents will
	 * 			    be overwritten
	 * 
	 *     OUTPUTS:
	 *          b	coefficients of linear terms in cubic 
	 * 			    spline
	 * 	  	    c	coefficients of quadratic terms in
	 * 			    cubic spline
	 * 	        d	coefficients of cubic terms in cubic
	 * 			    spline
	 * 
	 *     REMARK:
	 *          remember that the constant terms in the cubic spline
	 *          are given by the function values being interpolated;
	 *          i.e., the contents of the f array are the constant
	 *          terms
	 * 
	 *          to evaluate the cubic spline, use the routine
	 *          'spline_eval'
	 * 
	 * </pre>
	 * 
	 * @param n
	 * @param x
	 * @param f
	 * @param b
	 * @param c
	 * @param d
	 */
	public static void cubic_nak(int n, double[] x, double[] f, double[] b,
			double[] c, double[] d)

	{
		int i;

		double[] h = new double[n];
		double[] dl = new double[n];
		double[] dd = new double[n];
		double[] du = new double[n];

		for (i = 0; i < n - 1; i++)
			h[i] = x[i + 1] - x[i];
		for (i = 0; i < n - 3; i++)
			dl[i] = du[i] = h[i + 1];

		for (i = 0; i < n - 2; i++) {
			dd[i] = 2.0 * (h[i] + h[i + 1]);
			c[i] = (3.0 / h[i + 1]) * (f[i + 2] - f[i + 1]) - (3.0 / h[i])
					* (f[i + 1] - f[i]);
		}
		dd[0] += (h[0] + h[0] * h[0] / h[1]);
		dd[n - 3] += (h[n - 2] + h[n - 2] * h[n - 2] / h[n - 3]);
		du[0] -= (h[0] * h[0] / h[1]);
		dl[n - 4] -= (h[n - 2] * h[n - 2] / h[n - 3]);

		tridiagonal(n - 2, dl, dd, du, c);

		for (i = n - 3; i >= 0; i--)
			c[i + 1] = c[i];
		c[0] = (1.0 + h[0] / h[1]) * c[1] - h[0] / h[1] * c[2];
		c[n - 1] = (1.0 + h[n - 2] / h[n - 3]) * c[n - 2] - h[n - 2] / h[n - 3]
				* c[n - 3];
		for (i = 0; i < n - 1; i++) {
			d[i] = (c[i + 1] - c[i]) / (3.0 * h[i]);
			b[i] = (f[i + 1] - f[i]) / h[i] - h[i] * (c[i + 1] + 2.0 * c[i])
					/ 3.0;
		}

	}

	/**
	 * <pre>
	 * 	     PURPOSE:
	 * 	          evaluate a cubic spline at a single value of 
	 * 	          the independent variable given the coefficients of
	 * 	          the cubic spline interpolant (obtained from 
	 * 	          'cubic_nak' or 'cubic_clamped')
	 * 
	 * 
	 * 	     CALLING SEQUENCE:
	 * 	          y = spline_eval ( n, x, f, b, c, d, t );
	 * 	          spline_eval ( n, x, f, b, c, d, t );
	 * 
	 * 
	 * 	     INPUTS:
	 * 	          n		number of interpolating points
	 * 	          x		array containing interpolating points
	 * 	          f		array containing the constant terms from 
	 * 				the cubic spline (obtained from 'cubic_nak'
	 * 				or 'cubic_clamped')
	 * 	          b		array containing the coefficients of the
	 * 				linear terms from the cubic spline 
	 * 				(obtained from 'cubic_nak' or 'cubic_clamped')
	 * 	          c		array containing the coefficients of the
	 * 				quadratic terms from the cubic spline 
	 * 				(obtained from 'cubic_nak' or 'cubic_clamped')
	 * 	          d		array containing the coefficients of the
	 * 				cubic terms from the cubic spline 
	 * 				(obtained from 'cubic_nak' or 'cubic_clamped')
	 * 	          t		value of independent variable at which
	 * 				the interpolating polynomial is to be
	 * 				evaluated
	 * 
	 * 
	 * 	     OUTPUTS:
	 * 	          y		value of cubic spline at the specified 
	 * 				value of the independent variable
	 * 
	 * </pre>
	 * 
	 * @param n
	 * @param x
	 * @param f
	 * @param b
	 * @param c
	 * @param d
	 * @param t
	 * @return
	 */
	public static double spline_eval(int n, double[] x, double[] f, double[] b,
			double[] c, double[] d, double t) {
		int i;
		boolean found;

		i = 1;
		found = false;
		while (!found && (i < n - 1)) {
			if (t < x[i])
				found = true;
			else
				i++;
		}
		t = f[i - 1]
				+ (t - x[i - 1])
				* (b[i - 1] + (t - x[i - 1])
						* (c[i - 1] + (t - x[i - 1]) * d[i - 1]));
		return (t);
	}
}

