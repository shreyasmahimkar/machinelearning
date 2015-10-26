import java.util.ListIterator;


	public class TreeNode // Tree data structure for decision tree and regression tree.
	{
		TreeNode child[];
		TreeNode parent;
		String parentLabel,max_class_label;
		int split_attr;
		int[] example_cases;
		int depth_node; // Depth of the Node in process.
		
		TreeNode(int[] instances) { //Parameterized constructor 
			int size = instances.length;
			example_cases = new int[size];
			for (int i = 0; i < size; i++)
				example_cases[i] = instances[i];
		}

		void getSplitAttributeMSE() { // Used for Regression trees (Variance)
			Double min = Double.MAX_VALUE;
			double val;
			int finalAttribute = -1, attr;
			ListIterator lIt = DecisionTreeMaking.Attr_to_use.listIterator();
			double[] values = null; // Here will go all the values in that
									// specific attribute
			while (lIt.hasNext()) {
				attr = new Integer(lIt.next().toString()).intValue();
				val = Util.calculateVarianceForallAttrs(example_cases, attr);
				if (val < min) {
					min = val;
					finalAttribute = attr;
				}
			}
			split_attr = finalAttribute;
		}

		/**
		 * Used for calculating the best Split attribute for 
		 */
		void getSplitAttribute() // Using Gain Calculate all best split attribute 
		{
			float max = Long.MIN_VALUE;
			float val;
			int finalattr = -1, attr;
			ListIterator lIt = DecisionTreeMaking.Attr_to_use.listIterator();

			while (lIt.hasNext()) {
				attr = new Integer(lIt.next().toString()).intValue();
				val = DecisionTreeMaking.calculate_Information_Gain(example_cases, attr);
				if (val > max) {
					max = val;
					finalattr = attr;
				}
			}

			split_attr = finalattr;
		}

	}