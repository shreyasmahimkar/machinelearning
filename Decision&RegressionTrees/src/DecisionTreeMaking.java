import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.TreeSet;
import java.math.RoundingMode;
import java.math.BigDecimal;

public class DecisionTreeMaking {

	public static int nooffeatures;
	public static int inputtrainingdata;
	public static String[] inputTest;
	public static String[][] datamatrix;
	public static String[][] alldatamatrix;
	/*The Number of features*/
	public static int num_attr;
	/*The number of training examples*/
	public static int trainingexamples;
	public static int testingsize;
	public static int maximumDepthOfNode = 1;
	public static String file_name;
	public static String[] lineno1;
	public static TreeNode root;
	public static LinkedList Attr_to_use = new LinkedList();
	public static String[][] threshold_per_feature;
	public static int binarySplit;	
	


	public DecisionTreeMaking(String fName, int numofAttributes,
			int examplesCases, String delim) throws IOException,
			FileNotFoundException {

		DecisionTreeMaking.trainingexamples = examplesCases;
		DecisionTreeMaking.binarySplit = 2;
		DecisionTreeMaking.file_name = fName;
		DecisionTreeMaking.num_attr = numofAttributes;

		FileInputStream fstream = new FileInputStream(fName);
		DataInputStream input = new DataInputStream(fstream);

		// Continuos variable check
		DecisionTreeMaking.lineno1 = new String[num_attr];
		DecisionTreeMaking.lineno1 = input.readLine().split(delim);

		int i, j, totallines = 0;

		for (i = 0; i < num_attr; i++)
			DecisionTreeMaking.Attr_to_use.add(new Integer(i));
		datamatrix = new String[trainingexamples][num_attr + 1];
		alldatamatrix = new String[trainingexamples][num_attr + 1];
		int notval = 0;
		while ((totallines + notval) < trainingexamples) {
			try {
				datamatrix[totallines] = (input.readLine()).split(delim);
			} catch (NullPointerException e) {
				notval++;
				continue;
			}
			if (Array.getLength(datamatrix[totallines]) != num_attr + 1
					|| (Array.get(datamatrix[totallines], num_attr).toString()
							.compareTo("?") == 0)) // number of attributes
													// provided in the line is
													// incorrect.
			{
				notval++;
				continue;
			}
			totallines++;

		}

		if (notval == trainingexamples) {
			System.out
					.println("All lines invalid - Check the supplied attribute number");
			System.exit(0); // Exit if all lines are invalid
		}
		if (notval > 0)
			System.out.println("Will Not Consider " + notval
					+ " not valid training cases");

		trainingexamples = examplesCases - notval;
		threshold_per_feature = new String[num_attr][binarySplit - 1];

	}


	private int per90acc(int[] testcases) {
		double per100 = testcases.length;
		LinkedHashMap<String, Integer> per = new LinkedHashMap<String, Integer>();
		for (int i = 0; i < Array.getLength(testcases); i++) {
			String s = datamatrix[testcases[i]][num_attr];

			if (per.get(s) == null) {
				per.put(s, 1);
			} else {
				per.put(s, per.get(s) + 1);
			}
		}

		if (per.size() == 1) {
			return 100;
		}

		for (Entry<String, Integer> e : per.entrySet()) {
			double ans = e.getValue().doubleValue() / per100;
			if (ans > 0.90) {
				return 90;
			}
		}
		return 0;

	}

	public int countUniqueValForAttr(int[] example_cases, int attr_index) 
	{
		TreeSet treeSet = new TreeSet();

		for (int i = 0; i < example_cases.length; i++) {
			if (datamatrix[example_cases[i]][attr_index].compareTo("?") != 0)
				treeSet.add(datamatrix[example_cases[i]][attr_index]);
		}
		return treeSet.size();
	}

	/**
	 *  Returns an hashmap of the instances in example cases
	 * @param example_cases
	 * @param attr_index
	 * @return
	 */
	private HashMap<String, Integer> get_node_instances(int[] example_cases,
			int attr_index)
	{
		HashMap<String, Integer> instances_Map = new HashMap();
		int value;

		for (int i = 0; i < example_cases.length; i++) {
			if (datamatrix[example_cases[i]][attr_index].compareTo("?") == 0)
				continue;
			if (instances_Map.containsKey(datamatrix[example_cases[i]][attr_index])) {
				value = instances_Map.remove(datamatrix[example_cases[i]][attr_index]);
				instances_Map.put(datamatrix[example_cases[i]][attr_index],
						new Integer(value + 1));
			} else
				instances_Map.put(datamatrix[example_cases[i]][attr_index],
						new Integer(1));
		}

		return instances_Map;
	}

	private String max_Class_Label_Calc(TreeNode N) {
		HashMap<String, Integer> instances_Map = get_node_instances(N.example_cases,
				num_attr);

		Iterator iter = instances_Map.keySet().iterator();
		Integer max = Integer.MIN_VALUE, value;
		String ans = "";
		String class_label;
		while (iter.hasNext()) {
			class_label = iter.next().toString();
			value = instances_Map.get(class_label);

			if (value.compareTo(max) > 0) {
				max = value;
				ans = class_label;
			}
		}
		return ans;
	}
	/**
	 *  Calculate the Entropy of the input examples
	 * @param example_cases
	 * @return
	 */
	private static float calculate_Entropy(int[] example_cases)
	{
		HashMap classMap = new HashMap();
		String class_Label;
		int value;
		BigDecimal final_entropy = BigDecimal.ZERO;
		BigDecimal fraction_pqk;

		for (int i = 0; i < example_cases.length; i++) {
			class_Label = datamatrix[example_cases[i]][num_attr];

			if (classMap.containsKey(class_Label)) {
				value = new Integer(classMap.get(class_Label).toString()).intValue();
				classMap.remove(class_Label);
				classMap.put(class_Label, new Integer(value + 1));
			} else
				classMap.put(class_Label, new Integer("1"));
		}
		Collection hColl = classMap.values();
		Iterator hIter = hColl.iterator();

		while (hIter.hasNext()) {
			fraction_pqk = new BigDecimal(hIter.next().toString()).divide(
					new BigDecimal(Array.getLength(example_cases)), 10,
					RoundingMode.HALF_DOWN);
			fraction_pqk = fraction_pqk.multiply(new BigDecimal(Math.log(fraction_pqk
					.doubleValue())));
			final_entropy = final_entropy.add(fraction_pqk.negate());
		}
		return final_entropy.floatValue();
	}
	/**
	 * Calculate Information gain over an attribute to split on the training examples
	 * @param training_examples
	 * @param attr_index
	 * @return
	 */
	public static float calculate_Information_Gain(int[] training_examples, int attr_index)
	{
		HashMap classmap = new HashMap();
		String class_Label, ind;
		double finalanswer = 0;
		int i;

		for (i = 0; i < training_examples.length; i++) {
			class_Label = datamatrix[training_examples[i]][attr_index];

			if (class_Label.compareTo("?") == 0) continue;

			if (classmap.containsKey(class_Label)) {
				ind = classmap.get(class_Label).toString();
				ind = ind.concat(";" + String.valueOf(i));
				classmap.put(class_Label, ind);
			}

			else
				classmap.put(class_Label, String.valueOf(i));
		}

		String[] tokens;
		int[] all_child_instances;
		

		Collection hColl = classmap.values();
		Iterator hIter = hColl.iterator();
		while (hIter.hasNext()) {
			tokens = hIter.next().toString().split(";");
			all_child_instances = new int[Array.getLength(tokens)];

			for (i = 0; i < tokens.length; i++)
				all_child_instances[i] = new Integer(tokens[i]).intValue();

			finalanswer += calculate_Entropy(all_child_instances);
		}

		finalanswer -= calculate_Entropy(training_examples);
		return (float) - finalanswer;

	}

	/**
	 * parse the decision tree on the given set of test examples 
	 * @param testexamples
	 * @return
	 */
	private String TestRegressionTree(String testexamples) 
	{
		String[] tokens = new String[num_attr];
		tokens = testexamples.split(",");

		for (int i = 0; i < num_attr; i++) {
			if (tokens[i] == null) {
				System.out.println("Invalid Testing String");
				return null;
			}

			if (lineno1[i].compareTo("c") == 0)
				tokens[i] = makeFeatureBinary(tokens[i], i);
		}

		if (root == null) {
			System.out.println("Perform the training");
			return null;
		}

		TreeNode temp = root;

		while (temp.child != null) {
			for (int i = 0; i < Array.getLength(temp.child); i++) {
				if (temp.child[i].parentLabel
						.compareTo(tokens[temp.split_attr]) == 0) {
					temp = temp.child[i];
					break;
				}
			}
		}

		double[] housesprice = new double[temp.example_cases.length];
		for (int k=0; k<temp.example_cases.length;k++){
				housesprice[k] = Double.parseDouble(datamatrix[temp.example_cases[k]][num_attr].trim());
		}
		
		double d = Util.mean(housesprice);
		return Double.toString(d);

	}

	/**
	 * Output the class label on the input test cases using decision tree
	 * @param testexamples
	 * @return
	 */
	private String TestDecisionTree(String testexamples) 
	{
		String[] tokens = new String[num_attr];
		tokens = testexamples.split(",");

		for (int i = 0; i < num_attr; i++) {
			if (tokens[i] == null) {
				System.out.println("Invalid Testing String");
				return null;
			}

			if (lineno1[i].compareTo("c") == 0)
				tokens[i] = makeFeatureBinary(tokens[i], i);
		}

		if (root == null) {
			System.out.println("Perform the training");
			return null;
		}
		TreeNode temp = root;
		boolean isfound = false;
		

		while (temp.child != null) {
			isfound = false;
			for (int i = 0; i < Array.getLength(temp.child); i++) {
				if (temp.child[i].parentLabel
						.compareTo(tokens[temp.split_attr]) == 0) {
					temp = temp.child[i];
					isfound = true;
					break;
				}
			}

			if (!isfound) {
				return max_Class_Label_Calc(temp);
			}
		}

		HashMap<String, Integer> classmap = get_node_instances(temp.example_cases,
				num_attr);

		Iterator<String> instIter = classmap.keySet().iterator();
		int max = 0, value;
		String label, ans = "";

		while (instIter.hasNext()) {
			label = instIter.next();
			value = classmap.get(label).intValue();

			if (value > max) {
				max = value;
				ans = label;
			}
		}

		return ans;

	}

	private String makeFeatureBinary(String value, int attrIndex) {
		for (int i = 0; i < binarySplit - 1; i++) {

			if (value.compareTo(threshold_per_feature[attrIndex][i]) <= 0)
				return new Integer(i + 1).toString();
		}

		return new Integer(binarySplit).toString();
	}

	private String makeRFeatureBinary(String value, int attrIndex) {
		for (int i = 0; i < binarySplit - 1; i++) {

			if (Double.parseDouble(value) <= Double
					.parseDouble(threshold_per_feature[attrIndex][i]))
				return new Integer(i + 1).toString();
		}

		return new Integer(binarySplit).toString();
	}

	public String calculate_Threshold_for_attr(int[] currentcases, int attrIndex,
			int splitVal) {
		ArrayList<BigDecimal> specificFeatureValues = new ArrayList<BigDecimal>();
		BigDecimal val;

		for (int i = 0; i < currentcases.length; i++) {
			if (datamatrix[currentcases[i]][attrIndex].compareTo("?") == 0)
				continue;
			try {
				val = new BigDecimal(
						datamatrix[currentcases[i]][attrIndex].trim());
				specificFeatureValues.add(val);
			} catch (NumberFormatException e) {
				System.out
						.println("Real Numbers for Continous values");
				System.exit(0);
			}

		}

		Collections.sort(specificFeatureValues);

		BigDecimal threshold_for_attr = specificFeatureValues.get(((splitVal + 1) * (specificFeatureValues.size() - 1))
				/ binarySplit);
		return threshold_for_attr.toString();

	}


	// create a Regression Tree for the specified threshold
	public void generateRegressionTree(float threshold)
			throws InterruptedException {
		int[] examples = new int[trainingexamples];
		int i, j;
		for (i = 0; i < trainingexamples; i++){
			examples[i] = i;
		}

		/* Construct the tree using Stack data structure that stores only the
		 indexes at each tree node from
		 the data matrix*/
		TreeNode node = new TreeNode(examples);
		root = node;
		root.depth_node = 1; // start depth
		root.parent = null; // No parent for first iteration
		Iterator<String> tempIter1, tempIter2;
		HashMap<String, Integer> N1Map;
		
		String max_label = "";
		String label;
		ArrayList N1list;
		int N1size;
		int[] tempArray;
		
		Stack<TreeNode> index_Stack = new Stack<TreeNode>();

		index_Stack.push(node);


		while (!index_Stack.isEmpty() && Attr_to_use.size() > 0) {
			TreeNode N1 = index_Stack.pop();
			// For splitting on the same attribute duplicate the data
			for (int l = 0; l < trainingexamples; l++) {
				for (int m = 0; m < num_attr + 1; m++) {
					alldatamatrix[l][m] = datamatrix[l][m];
				}
			}
			int[] currentcases = N1.example_cases;

			for (i = 0; i < num_attr; i++) {
				for (j = 0; j < binarySplit - 1; j++)

					threshold_per_feature[i][j] = calculate_Threshold_for_attr(currentcases, i, j);
			}

			for (i = 0; i < N1.example_cases.length; i++) {
				for (j = 0; j < num_attr; j++) {
					datamatrix[currentcases[i]][j] = makeRFeatureBinary(
							datamatrix[currentcases[i]][j], j);
				}
			}

			N1.getSplitAttributeMSE();
			N1Map = get_node_instances(N1.example_cases, N1.split_attr);
			N1size = N1Map.size();

			tempIter2 = N1Map.keySet().iterator();
			int max = 0, val;
			String key;

			if (N1size == 1) {
				Attr_to_use.remove(new Integer(N1.split_attr));
				index_Stack.push(N1);
				for (int l = 0; l < trainingexamples; l++) {
					for (int m = 0; m < num_attr + 1; m++) {
						datamatrix[l][m] = alldatamatrix[l][m];
					}

				}
				continue;
			}

			if (N1.example_cases.length <= threshold) {
				N1.child = null;
				continue;
			}

			while (tempIter2.hasNext()) {
				key = tempIter2.next();
				val = N1Map.get(key);
				if (max < val) {
					max = val;
					max_label = key;
				}
			}

			N1.child = new TreeNode[N1size]; // Create as many child
														// nodes as there are
														// categories in the
														// splitAttribute.
			tempIter1 = N1Map.keySet().iterator();

			for (i = 0; i < N1size; i++) {
				N1list = new ArrayList();
				label = tempIter1.next();

				for (j = 0; j < Array.getLength(N1.example_cases); j++) {
					if (datamatrix[N1.example_cases[j]][N1.split_attr]
							.compareTo("?") == 0) {
						if (label.compareTo(max_label) == 0)
							N1list.add(N1.example_cases[j]);
						continue;
					}

					if (datamatrix[N1.example_cases[j]][N1.split_attr]
							.compareTo(label) == 0)
						N1list.add(N1.example_cases[j]);
				}

				tempArray = new int[N1list.size()];

				for (j = 0; j < N1list.size(); j++)
					tempArray[j] = new Integer(N1list.get(j).toString())
							.intValue();

				TreeNode N2 = new TreeNode(tempArray);
				N2.parentLabel = label;

				// System.out.println(label);
				N2.depth_node = N1.depth_node + 1;
				if (N2.depth_node > maximumDepthOfNode)
					maximumDepthOfNode = N2.depth_node;
				N1.child[i] = N2;
				N2.parent = N1;
				index_Stack.push(N2);
			}
			/*Copy back the full data back for selecting other split attribute*/
			for (int l = 0; l < trainingexamples; l++) {
				for (int m = 0; m < num_attr + 1; m++) {
					datamatrix[l][m] = alldatamatrix[l][m];
				}

			}
		}
	}

	// Create decision tree for a specified threshold
	public void createDecisionTree(float threshold) throws InterruptedException {
		
		int[] examples = new int[trainingexamples];
		int i, j;
		for (i = 0; i < trainingexamples; i++){
			examples[i] = i;
		}

		/* Construct the tree using Stack data structure that stores only the
		 indexes at each tree node from
		 the data matrix*/
		TreeNode node = new TreeNode(examples);
		root = node;
		root.depth_node = 1; // start depth
		root.parent = null; // No parent for first iteration
		root.max_class_label = max_Class_Label_Calc(root);
		Iterator<String> tempIter1, tempIter2;
		HashMap<String, Integer> N1Map;
		
		String maxlabel = "";

		ArrayList N1List;
		int N1size;
		String label;
		int[] tempArray;
		
		Stack<TreeNode> index_Stack = new Stack<TreeNode>();

		index_Stack.push(node);

		while (!index_Stack.isEmpty() && Attr_to_use.size() > 0) {
			TreeNode N1 = index_Stack.pop();

			// If all the output class of the leaf note are the same don't
			// expand the leaf node
			if (countUniqueValForAttr(N1.example_cases, num_attr) == 1) {
				N1.child = null;
				continue;
			}

			if (per90acc(N1.example_cases) > 0) {
				N1.child = null;
				continue;
			}

			for (int l = 0; l < trainingexamples; l++) {
				for (int m = 0; m < num_attr + 1; m++) {
					alldatamatrix[l][m] = datamatrix[l][m];
				}
			}

			int[] currentcases = N1.example_cases;

			for (i = 0; i < num_attr; i++) {
				for (j = 0; j < binarySplit - 1; j++)
					threshold_per_feature[i][j] = calculate_Threshold_for_attr(currentcases, i, j);
			}

			for (i = 0; i < N1.example_cases.length; i++) {
				for (j = 0; j < num_attr; j++) {
					datamatrix[currentcases[i]][j] = makeRFeatureBinary(
							datamatrix[currentcases[i]][j], j);
				}
			}

			N1.getSplitAttribute();
			// System.out.println(tempNode.splitAttribute+":: split attribute");
			N1Map = get_node_instances(N1.example_cases, N1.split_attr);
			N1size = N1Map.size();
			tempIter2 = N1Map.keySet().iterator();
			int max = 0, value;
			String key;
			if (N1size == 1) {
				Attr_to_use.remove(new Integer(N1.split_attr));
				index_Stack.push(N1);
				for (int l = 0; l < trainingexamples; l++) {
					for (int m = 0; m < num_attr + 1; m++) {
						datamatrix[l][m] = alldatamatrix[l][m];
					}
				}
				continue;
			}

			if (N1.example_cases.length <= threshold ) {
				N1.child = null;
				continue;
			}

			while (tempIter2.hasNext()) {
				key = tempIter2.next();
				value = N1Map.get(key);
				if (max < value) {
					max = value;
					maxlabel = key;
				}
			}
			/*2 child nodes get creates as its a binary decision tree*/
			N1.child = new TreeNode[N1size]; 
			tempIter1 = N1Map.keySet().iterator();

			for (i = 0; i < N1size; i++) {
				N1List = new ArrayList();
				label = tempIter1.next();

				for (j = 0; j < Array.getLength(N1.example_cases); j++) {
					if (datamatrix[N1.example_cases[j]][N1.split_attr]
							.compareTo("?") == 0) {
						if (label.compareTo(maxlabel) == 0)
							N1List.add(N1.example_cases[j]);
						continue;
					}

					if (datamatrix[N1.example_cases[j]][N1.split_attr]
							.compareTo(label) == 0)
						N1List.add(N1.example_cases[j]);
				}

				tempArray = new int[N1List.size()];

				for (j = 0; j < N1List.size(); j++)
					tempArray[j] = new Integer(N1List.get(j).toString())
							.intValue();

				TreeNode N2 = new TreeNode(tempArray);
				N2.parentLabel = label;
				N2.max_class_label = max_Class_Label_Calc(N2);

				// System.out.println(label);
				N2.depth_node = N1.depth_node + 1;
				if (N2.depth_node > maximumDepthOfNode)
					maximumDepthOfNode = N2.depth_node;
				N1.child[i] = N2;
				N2.parent = N1;
				index_Stack.push(N2);
			}

			/*Copy back the full data back for selecting other split attribute*/
			for (int l = 0; l < trainingexamples; l++) {
				for (int m = 0; m < num_attr + 1; m++) {
					datamatrix[l][m] = alldatamatrix[l][m];
				}
			}
		}
	}


	
	public void findRAccuracy(String fTestfileName, int testingSize, String delim, int nmin)
			throws IOException, FileNotFoundException {
		
		this.testingsize = testingSize;

		FileInputStream fstreamTest = new FileInputStream(fTestfileName);
		DataInputStream input = new DataInputStream(fstreamTest);

		int line_num_Count = 0, correctmatch = 0, skiplines = 0;
		inputTest = new String[testingSize];

		String tempInput;

		while (line_num_Count < testingSize) {
			inputTest[line_num_Count] = input.readLine();

			/*If the test data is invalid or class data then skip */
			if (Array.get(inputTest[line_num_Count].split(delim), num_attr)
					.toString().compareTo("?") == 0) {
				skiplines++;
				line_num_Count++;
				continue;
			}

			//System.out.print(Array.get(inputTest[lineCount].split(delim),num_attr).toString()+ " :: ");
			//System.out.println(getROutput(inputTest[lineCount]));
			double actual = Double.parseDouble(Array.get(inputTest[line_num_Count].split(delim),num_attr).toString());
			double predicted = Double.parseDouble(TestRegressionTree(inputTest[line_num_Count]));
			double sub = actual - predicted;
			ArrayList<Double> errors = new ArrayList<Double>();
			if (TestDT.MSE.get(nmin) == null){
				errors.add(sub*sub);
				TestDT.MSE.put(nmin, errors);
			}else{
				errors = TestDT.MSE.get(nmin);
				errors.add(sub*sub);
				TestDT.MSE.put(nmin, errors);
			}

			line_num_Count++;
		}
	}

	public float calculate_accuracy(String fTestfileName, int testingSize, String delim)
			throws IOException, FileNotFoundException {
		this.testingsize = testingSize;

		FileInputStream fstreamTest = new FileInputStream(fTestfileName);
		DataInputStream input = new DataInputStream(fstreamTest);

		int line_num_Count = 0, correctmatch = 0, skiplines = 0;
		inputTest = new String[testingSize];

		String tempInput;

		while (line_num_Count < testingSize) {
			inputTest[line_num_Count] = input.readLine();

			/*If the test data is invalid or class data then skip */
			if (Array.get(inputTest[line_num_Count].split(delim), num_attr)
					.toString().compareTo("?") == 0) {
				skiplines++;
				line_num_Count++;
				continue;
			}

			if ((Array.get(inputTest[line_num_Count].split(delim), num_attr)
					.toString()).compareTo(TestDecisionTree(inputTest[line_num_Count])) == 0)
				correctmatch++;

			line_num_Count++;
		}

		return (float) correctmatch / (float) (testingSize - skiplines);

	}


}
