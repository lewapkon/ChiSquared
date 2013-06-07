
public class Result {

	@Override
	public String toString() {
		return "a = " + round(a, places, false) + " ± " + round(delA, places, true) + "\nb = " + round(b, places, false) + " ± " + round(delB, places, true) + "\nchi^2 = " + round(chi2, places, false) + "\nP = " + round(1 - p, places, false);
	}

	private double s;
	private double sx;
	private double sy;
	private double sxx;
	private double sxy;
	private double a;
	private double b;
	private double sigma2;
	private double chi2;
	private double delA;
	private double delB;
	private double p;
	private Measurement[] measurements;
	private int places;

	/**
	 * Calculates a and b coefficients for the best matching function. Also calculates probability if the system of points is fortuitous.
	 * @param array	Array of objects of type <code>Pomiar</code>.
	 */
	public Result(Measurement[] array) {
		measurements = array;
		places = Integer.parseInt(AppFrame.places.getText());
		s = calculateS();
		sx = calculateSX();
		sy = calculateSY();
		sxx = calculateSXX();
		sxy = calculateSXY();
		a = calculateA();
		b = calculateB();
		sigma2 = calculateSigma2();
		chi2 = calculateChi2();
		delA = calculateDelA();
		delB = calculateDelB();
		p = calculateP(chi2, measurements.length - 3);
	}

	/**
	 * Calculates probability if the system of points is fortuitous.
	 * @param 	x	Chi squared.
	 * @param 	n	Degrees of freedom
	 * @return	Probability that the system of points is fortuitous.
	 */
	private double calculateP(double x, int df) {

		double a = 0, y = 0, s = 0;
		double e = 0, c = 0, z = 0;
		Boolean even = false;                     /* True if df is an even number */

		final double LOG_SQRT_PI = 0.5723649429247000870717135; /* log(sqrt(pi)) */
		final double I_SQRT_PI = 0.5641895835477562869480795;   /* 1 / sqrt(pi) */

		if (x <= 0.0 || df < 1) {
			return 1.0;
		}

		a = 0.5 * x;
		if (x % 2 == 0)	even = true;
		if (df > 1) {
			y = ex(-a);
		}
		s = (even ? y : (2.0 * poz(-Math.sqrt(x))));
		if (df > 2) {
			x = 0.5 * (df - 1.0);
			z = (even ? 1.0 : 0.5);
			if (a > 20.0) {
				e = (even ? 0.0 : LOG_SQRT_PI);
				c = Math.log(a);
				while (z <= x) {
					e = Math.log(z) + e;
					s += ex(c * z - a - e);
					z += 1.0;
				}
				return s;
			} else {
				e = (even ? 1.0 : (I_SQRT_PI / Math.sqrt(a)));
				c = 0.0;
				while (z <= x) {
					e = e * (a / z);
					c = c + e;
					z += 1.0;
				}
				return c * y + s;
			}
		} else {
			return s;
		}
	}

	private double ex(double x) {
		return (x < -20.0) ? 0.0 : Math.exp(x);
	}

	private double poz(double z) {
		double y, x, w;
		final double Z_MAX = 6.0;              /* Maximum meaningful z value */

		if (z == 0.0) {
			x = 0.0;
		} else {
			y = 0.5 * Math.abs(z);
			if (y >= (Z_MAX * 0.5)) {
				x = 1.0;
			} else if (y < 1.0) {
				w = y * y;
				x = ((((((((0.000124818987 * w
						- 0.001075204047) * w + 0.005198775019) * w
						- 0.019198292004) * w + 0.059054035642) * w
						- 0.151968751364) * w + 0.319152932694) * w
						- 0.531923007300) * w + 0.797884560593) * y * 2.0;
			} else {
				y -= 2.0;
				x = (((((((((((((-0.000045255659 * y
						+ 0.000152529290) * y - 0.000019538132) * y
						- 0.000676904986) * y + 0.001390604284) * y
						- 0.000794620820) * y - 0.002034254874) * y
						+ 0.006549791214) * y - 0.010557625006) * y
						+ 0.011630447319) * y - 0.009279453341) * y
						+ 0.005353579108) * y - 0.002141268741) * y
						+ 0.000535310849) * y + 0.999936657524;
			}
		}
		return (z > 0.0) ? ((x + 1.0) * 0.5) : ((1.0 - x) * 0.5);
	}

	/**
	 * @return	Calculates uncertainty of 'b' coefficient.
	 */
	private double calculateDelB() {
		return Math.sqrt(sigma2 * (sxx / ((s * sxx) - Math.pow(sx, 2))));
	}

	/**
	 * @return	Calculates uncertainty of 'a' coefficient.
	 */
	private double calculateDelA() {
		return Math.sqrt(sigma2 * (s / ((s * sxx) - Math.pow(sx, 2))));
	}

	private double calculateChi2() {
		double value = 0;
		for (int i = 0; i < measurements.length; i++) {
			value += Math.pow(measurements[i].getYVal() - b - (a * measurements[i].getXVal()), 2) / Math.pow(measurements[i].getYDel(), 2);
		}
		return value;
	}

	private double calculateSigma2() {
		double value = 0;
		for (int i = 0; i < measurements.length; i++) {
			value += Math.pow(measurements[i].getYVal() - b - (a * measurements[i].getXVal()), 2);
		}
		return value * 1 / (measurements.length - 2);
	}

	/**
	 * @return	Calculates 'b' coefficient.
	 */
	private double calculateB() {
		return ((sxx * sy) - (sx * sxy)) / ((s * sxx) - Math.pow(sx, 2));
	}

	/**
	 * @return	Calculates 'a' coefficient.
	 */
	private double calculateA() {
		return ((s * sxy) - (sx * sy)) / ((s * sxx) - Math.pow(sx, 2));
	}

	private double calculateSXY() {
		double value = 0;
		for (int i = 0; i < measurements.length; i++) {
			value += (measurements[i].getXVal() * measurements[i].getYVal()) / Math.pow(measurements[i].getYDel(), 2);
		}
		return value;
	}

	private double calculateSXX() {
		double value = 0;
		for (int i = 0; i < measurements.length; i++) {
			value += Math.pow(measurements[i].getXVal(), 2) / Math.pow(measurements[i].getYDel(), 2);
		}
		return value;
	}

	private double calculateSY() {
		double value = 0;
		for (int i = 0; i < measurements.length; i++) {
			value += measurements[i].getYVal() / Math.pow(measurements[i].getYDel(), 2);
		}
		return value;
	}

	private double calculateSX() {
		double value = 0;
		for (int i = 0; i < measurements.length; i++) {
			value += measurements[i].getXVal() / Math.pow(measurements[i].getYDel(), 2);
		}
		return value;
	}

	private double calculateS() {
		double value = 0;
		for (int i = 0; i < measurements.length; i++) {
			value += 1 / Math.pow(measurements[i].getYDel(), 2);
		}
		return value;
	}

	/**
	 * @return	'A' coefficient.
	 */
	public double getA() {
		return a;
	}

	/**
	 * @return 'B' coefficient.
	 */
	public double getB() {
		return b;
	}

	/**
	 * @return	Chi squared.
	 */
	public double getChi2() {
		return chi2;
	}

	/**
	 * @return Uncertainty of 'a' coefficient.
	 */
	public double getDelA() {
		return delA;
	}

	/**
	 * @return	Uncertainty of 'b' coefficient.
	 */
	public double getDelB() {
		return delB;
	}
	
	/**
	 * @return	Array of <code>Pomiar</code> type.
	 */
	public Measurement[] getPomiary() {
		return measurements;
	}
	
	/**
	 * Rounds numbers depending if its a normal number or uncertainty.
	 * @param val			Number to round
	 * @param places		How much places after comma should returned number have
	 * @param uncertainty	Is it an uncertainty
	 * @return				Number rounded to given places
	 */
	static private double round(double val, int places, boolean uncertainty)
	{            
		long factor = (long) Math.pow(10,places);
		val = val * factor;
		long tmp;
		if (uncertainty)
			tmp = (long) Math.ceil(val);
		else
			tmp = (long) Math.round(val);
		return (double) tmp / factor;
	}
}
