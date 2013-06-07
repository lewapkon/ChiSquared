import javax.swing.DefaultListModel;


@SuppressWarnings("serial")
public class PointsList extends DefaultListModel<Measurement> {

	public Measurement[] array;
	
	public PointsList() {
		array = toArray();
	}

	/**
	 * @param lista	List of type <code>PointsList</code>.
	 * @return		List of type <code>Pomiar[]</code>.
	 */
	public Measurement[] toArray() {
		Measurement[] array = new Measurement[this.size()];
		for (int i = 0; i < this.size(); i++) {
			array[i] = this.get(i);
		}
		this.array = array;
		return array;
	}
	
	public double maxXVal() {
		double max = array[0].getXVal();
		for (int i = 1; i < array.length; i++) {
			if (array[i].getXVal() > max)	max = array[i].getXVal();
		}
		return max;
	}
	
	public double maxYVal() {
		double max = array[0].getYVal();
		for (int i = 1; i < array.length; i++) {
			if (array[i].getYVal() > max)	max = array[i].getYVal();
		}
		return max;
	}
	
	public double minXVal() {
		double min = array[0].getXVal();
		for (int i = 1; i < array.length; i++) {
			if (array[i].getXVal() < min)	min = array[i].getXVal();
		}
		return min;
	}
	
	public double minYVal() {
		double min = array[0].getYVal();
		for (int i = 1; i < array.length; i++) {
			if (array[i].getYVal() < min)	min = array[i].getYVal();
		}
		return min;
	}
	
	public double maxYDel () {
		double max = array[0].getYDel() + array[0].getYVal();
		for (int i = 1; i < array.length; i++) {
			if (array[i].getYDel() + array[i].getYVal() > max)	max = array[i].getYDel() + array[i].getYVal();
		}
		return max;
	}
	
	public double minYDel () {
		double min = array[0].getYVal() - array[0].getYDel();
		for (int i = 1; i < array.length; i++) {
			if (array[i].getYVal() - array[i].getYDel() < min)	min = array[i].getYVal() - array[i].getYDel();
		}
		return min;
	}
}
