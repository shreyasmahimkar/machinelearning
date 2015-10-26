public class Matrix implements java.io.Serializable {
	private int row_inst;
	private int column_inst;
	private double datos[][];

	public Matrix(int rows, int columns) {
		this(rows, columns, 0);
	}

	public Matrix(int rows, int columns, double value) {
		int i, j;

		row_inst = rows;
		column_inst = columns;
		datos = new double[row_inst][column_inst];

		for (i = 0; i < row_inst; i++)
			for (j = 0; j < column_inst; j++)
				datos[i][j] = value;
	}
	
	public Matrix(int rows, int columns, String identity) {
		int i, j;
		int iden = 1;
		row_inst = rows;
		column_inst = columns;
		datos = new double[row_inst][column_inst];

		for (i = 0; i < row_inst; i++)
			for (j = 0; j < column_inst; j++)
				if (i == j){
					datos[i][j] = iden;
				}
	}

	public Matrix(Matrix origen) {
		int i, j;

		row_inst = origen.row_inst;
		column_inst = origen.column_inst;
		datos = new double[row_inst][column_inst];

		for (i = 0; i < row_inst; i++)
			for (j = 0; j < column_inst; j++)
				datos[i][j] = origen.datos[i][j];
	}

	public Matrix(double[][] data) {
		this.row_inst = data.length;
		this.column_inst = data[0].length;
		this.datos = data;
	}

	protected Matrix(double[] vector) {
		this.row_inst = 1;
		this.column_inst = vector.length;

		this.datos = new double[row_inst][];
		this.datos[0] = vector;
	}

	public final int size() {
		return row_inst * column_inst;
	}

	public final int rows() {
		return row_inst;
	}

	public final int columns() {
		return column_inst;
	}

	public final double get(int i, int j) {
		return datos[i][j];
	}

	public void set(int i, int j, double v) {
		datos[i][j] = v;
	}

	
	public Matrix transpose() {
		int i, j;
		Matrix t = new Matrix(column_inst, row_inst);

		for (i = 0; i < column_inst; i++)
			for (j = 0; j < row_inst; j++)
				t.datos[i][j] = this.datos[j][i];

		return t;
	}


	public Matrix add(Matrix other) {
		int i, j;
		Matrix suma = null;

		if (this.row_inst == other.row_inst && this.column_inst == other.column_inst) {

			suma = new Matrix(row_inst, column_inst);

			for (i = 0; i < row_inst; i++)
				for (j = 0; j < column_inst; j++)
					suma.datos[i][j] = this.datos[i][j] + other.datos[i][j];
		}

		return suma;
	}
	
	public Matrix multiply(Matrix other) {
		int i, j, k;
		Matrix result = null;

		if (this.column_inst == other.row_inst) {

			result = new Matrix(this.row_inst, other.column_inst);

			for (i = 0; i < this.row_inst; i++)
				for (j = 0; j < other.column_inst; j++) {

					result.datos[i][j] = 0;

					for (k = 0; k < this.column_inst; k++)
						result.datos[i][j] += this.datos[i][k]
								* other.datos[k][j];
				}
		}

		return result;
	}

	public Matrix multiply(double constant) {
		int i, j;
		Matrix result = new Matrix(this.row_inst, this.column_inst);

		for (i = 0; i < this.row_inst; i++)
			for (j = 0; j < this.column_inst; j++)
				result.datos[i][j] = constant * this.datos[i][j];

		return result;
	}

	private int LU(Matrix A, Matrix P) {
		int i, j, k, n;
		int maxi;
		double tmp;
		double c, c1;
		int p;

		n = A.column_inst;

		for (i = 0; i < n; i++)
			P.datos[i][0] = i;

		p = 0;

		for (k = 0; k < n; k++) {

			for (i = k, maxi = k, c = 0; i < n; i++) {

				c1 = Math.abs(A.datos[(int) P.datos[i][0]][k]);

				if (c1 > c) {
					c = c1;
					maxi = i;
				}
			}

			if (k != maxi) {
				p++;
				tmp = P.datos[k][0];
				P.datos[k][0] = P.datos[maxi][0];
				P.datos[maxi][0] = tmp;
			}

			if (A.datos[(int) P.datos[k][0]][k] == 0.0)
				return -1;

			for (i = k + 1; i < n; i++) {

				A.datos[(int) P.datos[i][0]][k] /= A.datos[(int) P.datos[k][0]][k];

				for (j = k + 1; j < n; j++)
					A.datos[(int) P.datos[i][0]][j] -= A.datos[(int) P.datos[i][0]][k]
							* A.datos[(int) P.datos[k][0]][j];
			}
		}

		return p;
	}


	private void backwardsSubstitution(Matrix A, Matrix B, Matrix X, Matrix P,
			int xcol) {
		int i, j, k, n;
		double sum;

		n = A.column_inst;

		for (k = 0; k < n; k++)
			for (i = k + 1; i < n; i++)
				B.datos[(int) P.datos[i][0]][0] -= A.datos[(int) P.datos[i][0]][k]
						* B.datos[(int) P.datos[k][0]][0];

		X.datos[n - 1][xcol] = B.datos[(int) P.datos[n - 1][0]][0]
				/ A.datos[(int) P.datos[n - 1][0]][n - 1];

		for (k = n - 2; k >= 0; k--) {

			sum = 0;

			for (j = k + 1; j < n; j++)
				sum += A.datos[(int) P.datos[k][0]][j] * X.datos[j][xcol];

			X.datos[k][xcol] = (B.datos[(int) P.datos[k][0]][0] - sum)
					/ A.datos[(int) P.datos[k][0]][k];
		}
	}


	public Matrix inverse() {
		int i;
		int n = row_inst;
		int p;
		Matrix A = new Matrix(this);
		Matrix B = new Matrix(n, 1);
		Matrix P = new Matrix(n, 1);
		Matrix C = null;

		p = LU(A, P);
		if (p != -1) {
			C = new Matrix(n, n);
			for (i = 0; i < n; i++) {
				B.zero();
				B.datos[i][0] = 1;
				backwardsSubstitution(A, B, C, P, i);
			}
		}
		A = null;
		B = null;
		P = null;
		return C;
	}

	private void zero() {
		int i, j;
		for (i = 0; i < row_inst; i++)
			for (j = 0; j < column_inst; j++)
				datos[i][j] = 0;
	}



		@Override
	public boolean equals(Object obj) {
		Matrix other;

		if (this == obj) {

			return true;

		} else if (obj instanceof Matrix) {

			other = (Matrix) obj;

			if (this.columns() != other.columns())
				return false;

			if (this.rows() != other.rows())
				return false;

			for (int i = 0; i < rows(); i++)
				for (int j = 0; j < rows(); j++)
					if (this.get(i, j) != other.get(i, j))
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

	@Override
	public String toString() {
		return toString("[", "]\n", " ");
	}

	public String toString(String rowPrefix, String rowSuffix, String delimiter) {
		int i, j;
		StringBuffer buffer = new StringBuffer();
		for (i = 0; i < row_inst; i++) {
			buffer.append(rowPrefix);
			buffer.append(datos[i][0]);
			for (j = 1; j < column_inst; j++) {
				buffer.append(delimiter);
				buffer.append(datos[i][j]);
			}
			buffer.append(rowSuffix);
		}
		return buffer.toString();
	}
}
