public class LinearRegression {
	Vector[] x;
	Vector y;
	double[] theta;

	// Constructor

	public LinearRegression(Vector[] x, Vector y) {
		this.x = x;
		this.y = y;

		theta = new double[x.length + 1];
	}

	public LinearRegression(double[][] x, double[] y) {
		this.x = new Vector[x.length];

		for (int i = 0; i < x.length; i++)
			this.x[i] = new Vector(x[i]);

		this.y = new Vector(y);

		theta = new double[x.length + 1];
	}

	public double[] getParameters() {
		return theta;
	}

	private double getX(int j, int i) {
		if (j > 0){
			//System.out.println(i);
			return x[j - 1].get(i);}
		else
			return 1; // X[0,:] = 1
	}

	private double getY(int i) {
		return y.get(i);
	}

	private double getH(int i) {
		double h = 0;

		for (int j = 0; j < theta.length; j++) {
			h += getX(j, i) * theta[j];
		}

		return h;
	}

	public double predict(double[] x) {
		double h = theta[0];

		for (int j = 0; j < x.length; j++) {
			h += x[j] * theta[j + 1];
		}

		return h;
	}

	// Gradient descent
	// ----------------

	double alpha = 0.0004;
	int iterations = 1000;
	double[] J;

	public void setLearningRate(double alpha) {
		this.alpha = alpha;
	}

	public double getLearningRate() {
		return alpha;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public double getIterations() {
		return iterations;
	}

	public double[] getJ() {
		return J;
	}

	public void gradientDescent() {
		J = new double[iterations];

		for (int i = 0; i < iterations; i++) {
			theta = updateParameters();
			J[i] = getCost();
			//System.out.println(Math.sqrt(J[i]*2));
		}
		
	}

	public double getCost() {
		int m = y.size();
		double cost = 0;
		double d;

		for (int i = 0; i < m; i++) {
			d = getH(i) - getY(i);
			cost += d * d;
		}

		return cost / (2 * m);
	}

	private double[] updateParameters() {
		double[] t = new double[theta.length];
		int m = y.size();
		double s;
		int i, j;

		for (j = 0; j < t.length; j++) {

			s = 0;

			for (i = 0; i < m; i++) {
				s += (getH(i) - getY(i)) * getX(j, i);
			}

			t[j] = theta[j] - (alpha / m) * s;
		}

		return t;
	}

	// Normal equation
	// ---------------

	public void normalEquation() {
		int m = this.y.size();
		Matrix X = new Matrix(m, theta.length); // m x p
		Matrix Y = this.y; // 1 x m
		Matrix Xt;
		Matrix Yt;
		Matrix result;

		// Design matrix

		for (int i = 0; i < m; i++)
			for (int j = 0; j < theta.length; j++)
				X.set(i, j, getX(j, i));

		Xt = X.transpose(); // p x m
		Yt = Y.transpose(); // m x 1

		// Normal equation

		result = Xt.multiply(X).inverse().multiply(Xt).multiply(Yt);

		// Theta parameters

		for (int p = 0; p < theta.length; p++)
			theta[p] = result.get(p, 0);
	}

}