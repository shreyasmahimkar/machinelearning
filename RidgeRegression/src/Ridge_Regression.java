public class Ridge_Regression {
	Vector[] x;
	Vector y;
	double[] theta;
	double alpha = 0.0004;
	int iterations = 1000;
	double[] J;
	// Constructor

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public Ridge_Regression(double[][] x, double[] y) {
		this.x = new Vector[x.length];

		for (int i = 0; i < x.length; i++)
			this.x[i] = new Vector(x[i]);

		this.y = new Vector(y);

		theta = new double[x.length];
	}
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public Ridge_Regression(Vector[] x, Vector y) {
		this.x = x;
		this.y = y;

		theta = new double[x.length];
	}
	
	/**
	 * 
	 * @param i
	 * @return
	 */
	private double getHypothesis(int i) {
		double h = 0;

		for (int j = 0; j < theta.length; j++) {
			h += getX(j, i) * theta[j];
		}

		return h;
	}
/**
 * 
 * @param j
 * @param i
 * @return
 */

	private double getX(int j, int i) {
			return x[j].get(i);
			
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	private double getY(int i) {
		return y.get(i);
	}

	/**
	 * 
	 * @param x
	 * @return
	 */
	public double predict(double[] x) {
		double h = 0;

		for (int j = 0; j < x.length; j++) {
			h += x[j] * theta[j];
		}

		return h;
	}

	/**
	 * 
	 * @param alpha
	 */
	public void setLearningRate(double alpha) {
		this.alpha = alpha;
	}


	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	/**
	 * 
	 * @return
	 */
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
			d = getHypothesis(i) - getY(i);
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
				s += (getHypothesis(i) - getY(i)) * getX(j, i);
			}

			t[j] = theta[j] - (alpha / m) * s;
		}

		return t;
	}

	public void normalEquation(double lambda) {
		int m = this.y.size();
		Matrix A = new Matrix(m, theta.length); // m x p
		Matrix B = this.y; // 1 x m
		Matrix At;
		Matrix Bt;
		Matrix I = new Matrix(theta.length, theta.length, "I");
		Matrix lambda_m;
		Matrix answer;

		// Design matrix

		for (int i = 0; i < m; i++)
			for (int j = 0; j < theta.length; j++)
				A.set(i, j, getX(j, i));

		// p x m
		At = A.transpose(); 
		// m x 1
		Bt = B.transpose(); 

		// Normal equation
		lambda_m = I.multiply(lambda);
		

		answer = At.multiply(A).add(lambda_m).inverse().multiply(At).multiply(Bt);

		// Theta parameters
		for (int p = 0; p < theta.length; p++)
			theta[p] = answer.get(p, 0);
	}

}