public class Vector extends Matrix {
	public static double EPSILON = 1e-6;

	// Constructors

	public Vector(int size) {
		super(1, size);
	}

	public Vector(Vector original) {
		super(original);
	}

	public Vector(double[] data) {
		super(data);
	}

	// Accessors & mutators

	public double get(int i) {
		return super.get(0, i);
	}

	public void set(int i, double value) {
		super.set(0, i, value);
	}

	// Arithmetic

	public double magnitude() {
		return Math.sqrt(magnitude2());
	}

	public double magnitude2() {
		double result = 0;

		for (int i = 0; i < size(); i++)
			result += get(i) * get(i);

		return result;
	}

	public double dotProduct(Vector other) {
		double result = 0;

		for (int i = 0; i < size(); i++)
			result += this.get(i) * other.get(i);

		return result;
	}

	public double projection(Vector other) {
		double length = other.magnitude();

		if (length > 0)
			return dotProduct(other) / length;
		else
			return 0;
	}

	public double distance(Vector other) {
		return Math.sqrt(distance2(other));
	}

	public double distance2(Vector other) {
		double diff;
		double result = 0;

		for (int i = 0; i < size(); i++) {
			diff = this.get(i) - other.get(i);
			result += diff * diff;
		}

		return result;
	}

	public double angle(Vector other) {
		double a = this.magnitude();
		double b = other.magnitude();
		double p = this.dotProduct(other);

		if ((a > 0) && (b > 0)) {

			if (Math.abs(p - a * b) < EPSILON)
				return 0;
			else
				return Math.acos(p / (a * b));

		} else
			return 0;
	}

	// Basic statistics

	public double min() {
		double min = get(0);

		for (int i = 1; i < size(); i++)
			if (get(i) < min)
				min = get(i);

		return min;
	}

	public double max() {
		double max = get(0);

		for (int i = 1; i < size(); i++)
			if (get(i) > max)
				max = get(i);

		return max;
	}

	public double sum() {
		double sum = 0;

		for (int i = 0; i < size(); i++)
			sum += get(i);

		return sum;
	}

	public double sum2() {
		double sum2 = 0;

		for (int i = 0; i < size(); i++)
			sum2 += get(i) * get(i);

		return sum2;
	}

	public double average() {
		return sum() / size();
	}

	public double variance() {
		double avg = average();

		return sum2() / size() - avg * avg;
	}

	public double deviation() {
		return Math.sqrt(variance());
	}

	public double absoluteDeviation() {
		double avg = average();
		double dev = 0;

		for (int i = 0; i < size(); i++)
			dev += Math.abs(get(i) - avg);

		return dev / size();
	}

	// Overriden Object methods

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {

			return true;

		} else if (obj instanceof Vector) {

			Vector other = (Vector) obj;

			for (int i = 0; i < size(); i++)
				if (this.get(i) != other.get(i))
					return false;

			return true;

		} else {

			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	// Standard output

	@Override
	public String toString() {
		return toString("[", "]", ",");
	}

	public String toStringSummary() {
		return "[n=" + size() + " min=" + min() + " max=" + max() + " avg="
				+ average() + " dev=" + deviation() + "]";
	}

	public String toString(String prefix, String suffix, String delimiter) {
		int i, n;
		StringBuffer buffer = new StringBuffer();

		n = size();

		buffer.append(prefix);
		buffer.append(get(0));

		for (i = 1; i < n; i++) {
			buffer.append(delimiter);
			buffer.append(get(i));
		}

		buffer.append(suffix);

		return buffer.toString();
	}

}