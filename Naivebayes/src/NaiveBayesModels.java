import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class NaiveBayesModels {
	public List<String> V;
	public double N;
	public double[] prior;
	public double[][] condprob;
	public HashMap<String, Integer> termmapping = new LinkedHashMap<String, Integer>();

	public void trainMultinomial(HashMap<String, String> Classes,
			HashMap<String, String> Documents) {
		N = Documents.size();
		double Nc = 0;
		prior = new double[Utils.Classes.size()];
		condprob = new double[V.size()][Utils.Classes.size()];
		for (Entry<String, String> c : Classes.entrySet()) {
			Nc = Utils.countDocumentsinClasses.get(c.getKey());
			prior[Integer.parseInt(c.getKey()) - 1] = Nc / N;
			HashMap<String, Integer> text_C = getconcatTextofAllDocsInClass(c.getKey());
			double Tct = 0;
			double Tct_dash = 0;
			for (String term : V) {
				Tct_dash += !text_C.containsKey(term) ? 0
						: (text_C.get(term) + 1);
			}
			double alltermsinTextc = Tct_dash;
			for (String term : V) {
				Tct = text_C.containsKey(term) ? text_C.get(term) : 0;
				Tct_dash = alltermsinTextc + V.size();
				condprob[termmapping.get(term)][Integer.parseInt(c.getKey()) - 1] = (Tct + 1)
						/ Tct_dash;
				//System.out.println();
			}
		}

	}

	public HashMap<String, Integer> getconcatTextofAllDocsInClass(String classs) {
		HashMap<String, Integer> text_C = new HashMap<String, Integer>();
		HashMap<String, Integer> inner = new HashMap<String, Integer>();
		for (Entry<String, String> doc : Utils.doc_class.entrySet()) {
			if (doc.getValue().equalsIgnoreCase(classs)) {
				inner = Utils.doc_term_train.get(doc.getKey());
				if (inner != null) {// if that document belongs to that class
					for (String innerkey : inner.keySet()) {
						if (!text_C.containsKey(innerkey)) {
							text_C.put(innerkey, inner.get(innerkey));
						} else {
							text_C.put(innerkey,
									inner.get(innerkey) + text_C.get(innerkey));
						}
					}
				}
			}
		}
		return text_C;
	}

	public void trainBernoulli(HashMap<String, String> Classes,
			HashMap<String, String> Documents) {
		N = Documents.size();
		double Nc = 0;
		prior = new double[Utils.Classes.size()];
		condprob = new double[V.size()][Utils.Classes.size()];
		for (Entry<String, String> c : Classes.entrySet()) {
			Nc = Utils.countDocumentsinClasses.get(c.getKey());
			prior[Integer.parseInt(c.getKey()) - 1] = Nc / N;
			double Nct = 0;
			for (String term : V) {
				Nct = countDocsInClassContainingTerm(term, c.getKey());
				condprob[termmapping.get(term)][Integer.parseInt(c.getKey()) - 1] = (Nct + 1)
						/ (Nc + 2);
				//System.out.println();
			}
		}
	}

	public String applyMultinomial(HashMap<String, String> Classes, String docId) {
		//List of trems and freq
		HashMap<String, Integer> Vd = extractTermsfromDoc(docId);
		double[] score = new double[Utils.Classes.size()];
		for (Entry<String, String> c : Classes.entrySet()) {
			int pos = Integer.parseInt(c.getKey()) - 1;
			score[pos] = Math.log(prior[pos]);
			for (String term : Vd.keySet()) {
				score[pos] += termmapping.containsKey(term)? Vd.get(term)*Math.log(condprob[termmapping.get(term)][pos]) : 0;
			}

		}
		double max = Double.NEGATIVE_INFINITY;
		int maxclass = 0;
		for (int i = 0; i < score.length; i++) {
			if (score[i] > max) {
				max = score[i];
				maxclass = i;
			}
		}
		return Integer.toString(maxclass + 1);

	}

	public String applyBernoulli(HashMap<String, String> Classes, String docId) {
		HashMap<String, Integer> Vd = extractTermsfromDoc(docId);
		double[] score = new double[Utils.Classes.size()];
		for (Entry<String, String> c : Classes.entrySet()) {
			int pos = Integer.parseInt(c.getKey()) - 1;
			score[pos] = Math.log(prior[pos]);
			for (String term : V) {
				if (Vd.containsKey(term)) {
					score[pos] += Math
							.log(condprob[termmapping.get(term)][pos]);
				} else {
					score[pos] += Math
							.log(1 - condprob[termmapping.get(term)][pos]);
				}
			}
		}
		double max = Double.NEGATIVE_INFINITY;
		int maxclass = 0;
		for (int i = 0; i < score.length; i++) {
			if (score[i] > max) {
				max = score[i];
				maxclass = i;
			}
		}
		return Integer.toString(maxclass + 1);
	}

	public double calculatePreictionAcc(HashMap<String, String> predictedClasses) {
		double count = 0;
		//key docId : val class
		for (Entry<String, String> pred : predictedClasses.entrySet()) {
			if (pred.getValue().equalsIgnoreCase(
					Utils.actual_class.get(pred.getKey()))) {
				count++;
			}
		}
		double acc = count / Utils.actual_class.size() * 100;
		System.out.println(acc);
		return acc;
	}

	public HashMap<String, Integer> extractTermsfromDoc(String docId) {
		return Utils.doc_term_test.get(docId);
	}

	public int countDocsInClassContainingTerm(String term, String classs) {
		int count = 0;
		if (!Utils.term_doc.containsKey(term)) {
			return 0;
		} else {
			List<String> docs = Utils.term_doc.get(term);
			for (String d : docs) {
				if (Utils.doc_class.get(d).equalsIgnoreCase(classs)) {
					count++;
				}
			}
		}

		return count;
	}

	public void extractVocabulary(List<String> fullvocab, int sizeV, String all) {
		if (all.equalsIgnoreCase("all")) {
			V = fullvocab;
			for (int i = 0; i < fullvocab.size(); i++) {
				termmapping.put(fullvocab.get(i), i);
			}
		} else {
			V = new ArrayList<String>();
			for (int i = 0; i < sizeV; i++) {
				V.add(fullvocab.get(i));
				termmapping.put(fullvocab.get(i), i);
			}
		}

	}
}
