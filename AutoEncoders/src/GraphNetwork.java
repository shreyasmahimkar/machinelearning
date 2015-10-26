public class GraphNetwork {
	protected Layer_Info output_vertices;
	protected Layer_Info hidden_vertices;
	protected int input_length;
	protected int output_length;
	protected int hidden_lenght;
	private double output_nodes[];

	private double Sigmoid(final double x) {
		return (1 / (1 + Math.exp(-x)));
	}

	// Constructor to initialize Graph structure/ Nueral Network Structure
	public GraphNetwork(final int input_length, final int hidden_length,
			final int output_length) throws Exception {
		this.input_length = input_length;
		this.hidden_lenght = hidden_length;
		this.output_length = output_length;
		this.hidden_vertices = new Layer_Info(hidden_length, input_length);
		this.output_vertices = new Layer_Info(output_length, hidden_length);
		this.output_nodes = new double[output_length];
	}

	public double BackpropAlgo(double train_examples[], double actual_op[],
			double eta, double alpha) throws Exception {

		double delta_op[] = new double[output_length], delta_in[] = new double[hidden_lenght];
		int i, j;
		double err, sum, mean_sq_err;
		output_nodes = BackPropAlgotest(train_examples);
		for (i = 0, mean_sq_err = 0; i < output_length; i++) {
			delta_op[i] = (err = (actual_op[i] - output_nodes[i]))
					* (output_nodes[i]) * (1 - output_nodes[i]);
			mean_sq_err += Math.pow(err, 2);
		}
		for (i = 0; i < hidden_lenght; i++) {
			for (j = 0, sum = 0; j < output_length; j++) {
				sum += delta_op[j] * (output_vertices.nodes[j].w[i]);
			}
			delta_in[i] = sum * (hidden_vertices.nodes[i].op)
					* (1 - (hidden_vertices.nodes[i].op));
		}
		for (i = 0; i < output_length; i++) {
			output_vertices.nodes[i].b_delta = (alpha * output_vertices.nodes[i].b_delta)
					+ (eta * delta_op[i]);
			output_vertices.nodes[i].bias += output_vertices.nodes[i].b_delta;
			for (j = 0; j < hidden_lenght; j++) {
				output_vertices.nodes[i].del[j] = (alpha * (output_vertices.nodes[i].del[j]))
						+ (eta * delta_op[i] * (hidden_vertices.nodes[j].op));
				output_vertices.nodes[i].w[j] += output_vertices.nodes[i].del[j];
			}
		}
		for (i = 0; i < hidden_lenght; i++) {
			hidden_vertices.nodes[i].b_delta = (alpha * hidden_vertices.nodes[i].b_delta)
					+ (eta * delta_in[i]);
			hidden_vertices.nodes[i].bias += hidden_vertices.nodes[i].b_delta;
			for (j = 0; j < input_length; j++) {
				hidden_vertices.nodes[i].del[j] = (alpha * (hidden_vertices.nodes[i].del[j]))
						+ (eta * delta_in[i] * train_examples[j]);
				hidden_vertices.nodes[i].w[j] += hidden_vertices.nodes[i].del[j];
			}
		}
		// Returning the mean squared error
		return mean_sq_err;
	}

	public double[] BackPropAlgotest(double test_examples[]) throws Exception {
		int i, j;
		double sum, answer[] = new double[output_length];
		for (i = 0; i < hidden_lenght; i++) {
			for (j = 0, sum = hidden_vertices.nodes[i].bias; j < input_length; j++) {
				sum += ((hidden_vertices.nodes[i].w[j]) * (test_examples[j]));
			}
			hidden_vertices.nodes[i].op = Sigmoid(sum);
		}

		for (i = 0; i < output_length; i++) {
			for (j = 0, sum = output_vertices.nodes[i].bias; j < hidden_lenght; j++) {
				sum += ((output_vertices.nodes[i].w[j]) * (hidden_vertices.nodes[j].op));
			}
			output_vertices.nodes[i].op = Sigmoid(sum);
			answer[i] = Sigmoid(sum);
		}
		return answer;
	}

	protected class Neuron_Info {
		double w[], del[], op, bias, b_delta;

		public Neuron_Info(final int input_length) {
			bias = Math.random() * (Math.random() > .5 ? 1 : -1);
			w = new double[input_length];
			del = new double[input_length];
			for (int i = 0; i < input_length; i++) {
				w[i] = Math.random() * (Math.random() > .5 ? 1 : -1);
				del[i] = 0;
			}
		}
	}

	protected class Layer_Info {
		protected int layer_length, input_length;
		protected Neuron_Info nodes[];

		public Layer_Info(final int size, final int input_size) {
			this.layer_length = size;
			this.input_length = input_size;
			nodes = new Neuron_Info[size];
			for (int i = 0; i < size; i++) {
				nodes[i] = new Neuron_Info(input_size);
			}
		}
	}
}