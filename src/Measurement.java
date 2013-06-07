
public class Measurement {

	@Override
	public String toString() {
		return Messages.getString("Measurement.0") + number + ": x = " + xVal + ", y = " + yVal + " ± " + yDel; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
	
	public String toString2() {
		return xVal + "	" + yVal + "	" + yDel; //$NON-NLS-1$ //$NON-NLS-2$
	}

	private double xVal;
	private double yVal;
	private double yDel;
	private int number;
	
	/**
	 * @param number	Numer pomiaru
	 * @param xVal		Wartosc X
	 * @param yVal		Wartosc Y
	 * @param yDel		Wartosc niepewnosci Y
	 */
	public Measurement(int number, double xVal, double yVal, double yDel) {
		this.xVal = xVal;
		this.yVal = yVal;
		this.yDel = yDel;
		this.number = number;
	}
	
	public void decreaseNumber() {
		number = number - 1;
	}
	
	public double getXVal() {
		return xVal;
	}

	public double getYVal() {
		return yVal;
	}

	public double getYDel() {
		return yDel;
	}
	
	public void setNumber(int n) {
		number = n;
	}

	public int getNumber() {
		return number;
	}
}
