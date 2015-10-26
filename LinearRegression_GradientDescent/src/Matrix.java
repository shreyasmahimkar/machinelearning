public class Matrix implements java.io.Serializable {
	private int filas;
	private int columnas;
	private double datos[][];

	// Constructors

	/** @pre rows>0 && columns>0 */
	public Matrix(int rows, int columns) {
		this(rows, columns, 0);
	}

	/** @pre rows>0 && columns>0 */
	public Matrix(int rows, int columns, double value) {
		int i, j;

		filas = rows;
		columnas = columns;
		datos = new double[filas][columnas];

		for (i = 0; i < filas; i++)
			for (j = 0; j < columnas; j++)
				datos[i][j] = value;
	}

	// Constructor de copia

	public Matrix(Matrix origen) {
		int i, j;

		filas = origen.filas;
		columnas = origen.columnas;
		datos = new double[filas][columnas];

		for (i = 0; i < filas; i++)
			for (j = 0; j < columnas; j++)
				datos[i][j] = origen.datos[i][j];
	}

	public Matrix(double[][] data) {
		this.filas = data.length;
		this.columnas = data[0].length;
		this.datos = data;
	}

	protected Matrix(double[] vector) {
		this.filas = 1;
		this.columnas = vector.length;

		this.datos = new double[filas][];
		this.datos[0] = vector;
	}

	// Acceso a las variables de instancia

	public final int size() {
		return filas * columnas;
	}

	public final int rows() {
		return filas;
	}

	public final int columns() {
		return columnas;
	}

	public final double get(int i, int j) {
		return datos[i][j];
	}

	public void set(int i, int j, double v) {
		datos[i][j] = v;
	}

	/* ------------------------ */
	/* OPERACIONES CON MATRICES */
	/* ------------------------ */

	// Traspuesta de una matriz

	public Matrix transpose() {
		int i, j;
		Matrix t = new Matrix(columnas, filas);

		for (i = 0; i < columnas; i++)
			for (j = 0; j < filas; j++)
				t.datos[i][j] = this.datos[j][i];

		return t;
	}

	// Devuelve el resultado de eliminar de A la fila i y la columna j

	/**
	 * @pre (filas>1) && (i>=0) && (i<filas) && (columnas>1) && (j>=0) &&
	 *      (j<columnas)
	 */
	public Matrix submatrix(int i, int j) {
		int x, y, xS, yS;
		Matrix S = new Matrix(filas - 1, columnas - 1);

		for (x = xS = 0; x < filas; x++)
			if (x != i) {
				for (y = yS = 0; y < columnas; y++)
					if (y != j) {
						S.datos[xS][yS] = this.datos[x][y];
						yS++;
					}
				xS++;
			}

		return S;
	}

	// Suma de matrices: A+B

	public Matrix add(Matrix other) {
		int i, j;
		Matrix suma = null;

		if (this.filas == other.filas && this.columnas == other.columnas) {

			suma = new Matrix(filas, columnas);

			for (i = 0; i < filas; i++)
				for (j = 0; j < columnas; j++)
					suma.datos[i][j] = this.datos[i][j] + other.datos[i][j];
		}

		return suma;
	}

	public Matrix add(double constant) {
		int i, j;
		Matrix suma = new Matrix(filas, columnas);

		for (i = 0; i < filas; i++)
			for (j = 0; j < columnas; j++)
				suma.datos[i][j] = this.datos[i][j] + constant;

		return suma;
	}

	// Resta de matrices: A-B

	public Matrix substract(Matrix other) {
		int i, j;
		Matrix resta = null;

		if (this.filas == other.filas && this.columnas == other.columnas) {

			resta = new Matrix(filas, columnas);

			for (i = 0; i < filas; i++)
				for (j = 0; j < columnas; j++)
					resta.datos[i][j] = this.datos[i][j] - other.datos[i][j];
		}

		return resta;
	}

	// Multiplicación de matrices: A*B

	public Matrix multiply(Matrix other) {
		int i, j, k;
		Matrix result = null;

		if (this.columnas == other.filas) {

			result = new Matrix(this.filas, other.columnas);

			for (i = 0; i < this.filas; i++)
				for (j = 0; j < other.columnas; j++) {

					result.datos[i][j] = 0;

					for (k = 0; k < this.columnas; k++)
						result.datos[i][j] += this.datos[i][k]
								* other.datos[k][j];
				}
		}

		return result;
	}

	public Matrix multiply(double constant) {
		int i, j;
		Matrix result = new Matrix(this.filas, this.columnas);

		for (i = 0; i < this.filas; i++)
			for (j = 0; j < this.columnas; j++)
				result.datos[i][j] = constant * this.datos[i][j];

		return result;
	}

	public Matrix divide(double constant) {
		int i, j;
		Matrix result = new Matrix(this.filas, this.columnas);

		for (i = 0; i < this.filas; i++)
			for (j = 0; j < this.columnas; j++)
				result.datos[i][j] = this.datos[i][j] / constant;

		return result;
	}

	// Suma de los coeficientes de la diagonal (Traza)

	public double trace() {
		int i;
		double result = 0;

		if (filas == columnas)
			for (i = 0; i < filas; i++)
				result += datos[i][i];

		return result;
	}

	// Producto de los coeficientes de la diagonal

	public double diagonalProduct() {
		int i;
		double result = 1;

		if (filas == columnas)
			for (i = 0; i < filas; i++)
				result *= datos[i][i];

		return result;
	}

	// Descomposición LU con pivoteo parcial
	// Entradas: A = Matriz cuadrada (n x n)
	// P = Vector de permutación (n x 1) */
	// Salida: Número de permutaciones realizadas ( -1 ---> Matriz singular )
	// A ---> Matriz LU

	private int LU(Matrix A, Matrix P) {
		int i, j, k, n;
		int maxi;
		double tmp;
		double c, c1;
		int p;

		n = A.columnas;

		for (i = 0; i < n; i++)
			P.datos[i][0] = i;

		p = 0;

		for (k = 0; k < n; k++) {

			// Pivoteo parcial

			for (i = k, maxi = k, c = 0; i < n; i++) {

				c1 = Math.abs(A.datos[(int) P.datos[i][0]][k]);

				if (c1 > c) {
					c = c1;
					maxi = i;
				}
			}

			// Intercambio de filas y actualización del vector P

			if (k != maxi) {
				p++;
				tmp = P.datos[k][0];
				P.datos[k][0] = P.datos[maxi][0];
				P.datos[maxi][0] = tmp;
			}

			// Matriz singular

			if (A.datos[(int) P.datos[k][0]][k] == 0.0)
				return -1;

			for (i = k + 1; i < n; i++) {

				// Calcula m(i,j)

				A.datos[(int) P.datos[i][0]][k] /= A.datos[(int) P.datos[k][0]][k];

				// Eliminación

				for (j = k + 1; j < n; j++)
					A.datos[(int) P.datos[i][0]][j] -= A.datos[(int) P.datos[i][0]][k]
							* A.datos[(int) P.datos[k][0]][j];
			}
		}

		return p;
	}

	/* ----------------------------------------------------------------------- */
	/* Substitución hacia atrás */
	/* Entrada: A = Matriz LU cuadrada n x n */
	/* B = Matriz n x 1 ( se sobreescribe) */
	/* X = Resultado de AX=B */
	/* P = Permutación (tras llamar a Descomposicion LU) */
	/* x = Columna de x en la que poner el resultado */

	private void backwardsSubstitution(Matrix A, Matrix B, Matrix X, Matrix P,
			int xcol) {
		int i, j, k, n;
		double sum;

		n = A.columnas;

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

	/* ----------------------------------------------------------------------- */
	/* Sistema de ecuaciones lineales AX = c */
	/* ----------------------------------------------------------------------- */

	public Matrix system(Matrix c) {
		Matrix A = new Matrix(this);
		Matrix B = new Matrix(c);
		Matrix X = new Matrix(filas, 1);
		Matrix P = new Matrix(filas, 1);

		LU(A, P);
		backwardsSubstitution(A, B, X, P, 0);

		A = null;
		B = null;
		P = null;

		return X;
	}

	/* ----------------------------------------------------------------------- */
	/* Inversa de una matriz */
	/* ----------------------------------------------------------------------- */

	public Matrix inverse() {
		int i;
		int n = filas;
		int p;
		Matrix A = new Matrix(this);
		Matrix B = new Matrix(n, 1);
		Matrix P = new Matrix(n, 1);
		Matrix C = null;

		// Descomposición LU

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

	// Anula todos los coeficientes de una matriz

	private void zero() {
		int i, j;

		for (i = 0; i < filas; i++)
			for (j = 0; j < columnas; j++)
				datos[i][j] = 0;
	}

	/* ----------------------------------------------------------------------- */
	/* Determinante de una matriz A */
	/* ----------------------------------------------------------------------- */

	private static final double sign[] = { 1.0, -1.0 };

	public double determinant() {
		Matrix A, P;
		int i, j, n;
		double result;

		n = filas;
		A = new Matrix(this);
		P = new Matrix(n, 1);

		// Descomposición LU

		i = LU(A, P);

		switch (i) {

		// Matriz singular

		case -1:
			result = 0.0;
			break;

		// |A| = |L||U||P|
		// |L| = 1,
		// |U| = multiplication of the diagonal
		// |P| = +-1

		default:
			result = 1.0;

			for (j = 0; j < n; j++)
				result *= A.datos[(int) P.datos[j][0]][j];

			result *= sign[i % 2];
			break;
		}

		A = null;
		P = null;

		return result;
	}

	/* ----------------------------------------------------------------------- */
	/* Menor de Aij */

	public double minor(int i, int j) {
		Matrix S = this.submatrix(i, j);

		return S.determinant();
	}

	/* ----------------------------------------------------------------------- */
	/* Cofactor de Aij */

	public double cofactor(int i, int j) {
		return sign[(i + j) % 2] * datos[i][j] * minor(i, j);
	}

	/* ----------------------------------------------------------------------- */
	/* Matriz simétrica n x n de Toeplitz a partir de un vector n x 1 */

	public Matrix toeplitz(Matrix R) {
		int i, j, n;
		Matrix T;

		n = R.filas;
		T = new Matrix(n, n);

		for (i = 0; i < n; i++)
			for (j = 0; j < n; j++)
				T.datos[i][j] = R.datos[Math.abs(i - j)][0];

		return T;
	}

	/* ----------------------------------------------------------------------- */
	/* Algoritmo de Levinson-Durbin */
	/*                                                                         */
	/* Resolución de un sistema de ecuaciones lineales de la forma: */
	/*                                                                         */
	/* | v0 v1 v2 .. vn-1 | | a1 | | v1 | */
	/* | v1 v0 v1 .. vn-2 | | a2 | | v2 | */
	/* | v2 v1 v0 .. vn-3 | | a3 | = | .. | */
	/* | ... | | .. | | .. | */
	/* | vn-1 vn-2 .. v0 | | an | | vn | */
	/*                                                                         */
	/* - A es una matriz simtrica de Toeplitz */
	/* - R = Matriz (v0, v1, ... vn) (dim (n+1) x 1) */
	/* - Devuelve x (de Ax = B) */
	/* ----------------------------------------------------------------------- */

	public Matrix LevinsonDurbin(Matrix R) {
		int i, i1, j, ji, p;
		Matrix W, E, K, A, X;

		p = R.filas - 1;
		W = new Matrix(p + 2, 1);
		E = new Matrix(p + 2, 1);
		K = new Matrix(p + 2, 1);
		A = new Matrix(p + 2, p + 2);

		W.datos[0][0] = R.datos[1][0];
		E.datos[0][0] = R.datos[0][0];

		for (i = 1; i <= p; i++) {
			K.datos[i][0] = W.datos[i - 1][0] / E.datos[i - 1][0];
			E.datos[i][0] = E.datos[i - 1][0]
					* (1.0 - K.datos[i][0] * K.datos[i][0]);
			A.datos[i][i] = -K.datos[i][0];

			i1 = i - 1;

			if (i1 >= 1)
				for (j = 1; j <= i1; j++) {
					ji = i - j;
					A.datos[j][i] = A.datos[j][i1] - K.datos[i][0]
							* A.datos[ji][i1];
				}

			if (i != p) {
				W.datos[i][0] = R.datos[i + 1][0];
				for (j = 1; j <= i; j++)
					W.datos[i][0] += A.datos[j][i] * R.datos[i - j + 1][0];
			}
		}

		X = new Matrix(p, 1);

		for (i = 0; i < p; i++)
			X.datos[i][0] = -A.datos[i + 1][p];

		A = null;
		W = null;
		K = null;
		E = null;

		return X;
	}

	/* ----------------------------------------------------------------------- */
	/* Algoritmo de Levinson-Durbin: */
	/* Resuelve el sistema de ecuaciones Ax=B de la forma */
	/*                                                                         */
	/* | v0 v1 v2 .. vn-1 | | a1 | | v1 | */
	/* | v1 v0 v1 .. vn-2 | | a2 | | v2 | */
	/* | v2 v1 v0 .. vn-3 | | a3 | = | .. | */
	/* | ... | | .. | | .. | */
	/* | vn-1 vn-2 .. v0 | | an | | vn | */
	/*                                                                         */
	/* ----------------------------------------------------------------------- */

	public Matrix LevinsonDurbinSystem(Matrix A, Matrix B) {
		Matrix R, X;
		int i, n;

		n = A.filas;
		R = new Matrix(n + 1, 1);

		for (i = 0; i < n; i++)
			R.datos[i][0] = A.datos[i][0];

		R.datos[n][0] = B.datos[n - 1][0];

		X = LevinsonDurbin(R);

		R = null;

		return X;
	}

	// Matrix equality

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

	/* --------------- */
	/* Salida estándar */
	/* --------------- */

	@Override
	public String toString() {
		return toString("[", "]\n", " ");
	}

	public String toString(String rowPrefix, String rowSuffix, String delimiter) {
		int i, j;
		StringBuffer buffer = new StringBuffer();

		for (i = 0; i < filas; i++) {

			buffer.append(rowPrefix);
			buffer.append(datos[i][0]);

			for (j = 1; j < columnas; j++) {
				buffer.append(delimiter);
				buffer.append(datos[i][j]);
			}

			buffer.append(rowSuffix);
		}

		return buffer.toString();
	}

}
