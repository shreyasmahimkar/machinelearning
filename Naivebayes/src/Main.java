import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.jfree.ui.RefineryUtilities;

public class Main {
	
	static HashMap<Integer, Double> bernoulliplot = new HashMap<Integer, Double>();
	static HashMap<Integer, Double> multinomialplot = new HashMap<Integer, Double>();
	
	//(ClassId <predictedval,actualval>
	static HashMap<String, ActPred> bernoullibarplot = new LinkedHashMap<String, ActPred>();
	static HashMap<String, ActPred> multinomialbarplot = new LinkedHashMap<String, ActPred>();

	public static void main(String[] args) throws IOException {
		Utils.readfile("train.data");// Count documents in this phase only
		Utils.readClasses("train.map");
		Utils.countdocsinclass("train.label");
		Utils.createTermsinDocTrain("train.data");
		Utils.actualDocumentClasses("test.label");
		Utils.createTermsinDocTest("test.data");
		/*Utils.readfile("tp.data");// Count documents in this phase only
		Utils.readClasses("tp.map");
		Utils.countdocsinclass("tp.label");
		Utils.createTermsinDocTrain("tp.data");
		Utils.actualDocumentClasses("tptest.label");
		Utils.createTermsinDocTest("tptest.data");*/
		
		List<String> fullVocab = Utils.readVocabulary("word_freq.txt");
		List<Integer> vocabsize = setvocabsizes();
		System.out.println("-----------Bernoulli Model---------");
		runBernoulliModel(fullVocab, vocabsize);
		
		System.out.println("-----------Multinomial Model---------");
		runMultinomialModel(fullVocab, vocabsize);
		plotBernoulliAndMultinomial();
		BarChart.PlotBarGraphBernoulliMult();
	}
	
	public static void plotBernoulliAndMultinomial(){
		XYSeriesPlot demo = new XYSeriesPlot("D", bernoulliplot,multinomialplot);
        demo.pack();
 		RefineryUtilities.centerFrameOnScreen(demo);
 		demo.setVisible(true);
	}
	

	public static void runMultinomialModel(List<String> fullVocab,List<Integer> vocabsize) throws IOException {
		for (Integer vs : vocabsize) {
			NaiveBayesModels nb = new NaiveBayesModels();
			nb.extractVocabulary(fullVocab, vs, "limited");
			nb.trainMultinomial(Utils.Classes, Utils.Documents);
			HashMap<String, String> predictedclass = new LinkedHashMap<String, String>();
			for (String docId : Utils.doc_term_test.keySet()) {
				predictedclass.put(docId, nb.applyMultinomial(Utils.Classes, docId));
			}
			
			double acc = nb.calculatePreictionAcc(predictedclass);
			multinomialplot.put(vs, acc);
		}
		/* For full vocbulary */
		NaiveBayesModels nb = new NaiveBayesModels();
		nb.extractVocabulary(fullVocab, 1, "all");
		nb.trainMultinomial(Utils.Classes, Utils.Documents);
		HashMap<String, String> predictedclass = new HashMap<String, String>();
		for (String docId : Utils.doc_term_test.keySet()) {
			predictedclass.put(docId, nb.applyMultinomial(Utils.Classes, docId));
		}
		//For bar graphs
		for (String docId : Utils.actual_class.keySet()) {
			String classId = Utils.actual_class.get(docId);
			ActPred ap = new ActPred();
			if(!multinomialbarplot.containsKey(classId)){
				multinomialbarplot.put(classId, ap);
			}else{
				ap = multinomialbarplot.get(classId);
			}
			
			if(predictedclass.get(docId).equalsIgnoreCase(Utils.actual_class.get(docId))){
				ap.actual += 1;
				ap.predicted +=1;
			}else{
				ap.actual += 1;
			}
			multinomialbarplot.put(classId, ap);
			
		}
		multinomialplot.put(fullVocab.size(), nb.calculatePreictionAcc(predictedclass));
	
	}

	public static void runBernoulliModel(List<String> fullVocab,List<Integer> vocabsize) {
		for (Integer vs : vocabsize) {
			NaiveBayesModels bm = new NaiveBayesModels();
			bm.extractVocabulary(fullVocab, vs, "limited");
			bm.trainBernoulli(Utils.Classes, Utils.Documents);
			HashMap<String, String> predictedclass = new LinkedHashMap<String, String>();
			for (String docId : Utils.actual_class.keySet()) {
				String classId = bm.applyBernoulli(Utils.Classes, docId);
				predictedclass.put(docId, classId);
			}
			
			double acc = bm.calculatePreictionAcc(predictedclass);
			bernoulliplot.put(vs, acc);
		
		}
		/* For full vocbulary */
		NaiveBayesModels bm = new NaiveBayesModels();
		bm.extractVocabulary(fullVocab, 1, "all");
		bm.trainBernoulli(Utils.Classes, Utils.Documents);
		HashMap<String, String> predictedclass = new HashMap<String, String>();
		for (String docId : Utils.actual_class.keySet()) {
			predictedclass.put(docId, bm.applyBernoulli(Utils.Classes, docId));
		}
		//For Plotting bar graphs
		for (String docId : Utils.actual_class.keySet()) {
			String classId = Utils.actual_class.get(docId);
			ActPred ap = new ActPred();
			if(!bernoullibarplot.containsKey(classId)){
				bernoullibarplot.put(classId, ap);
			}else{
				ap = bernoullibarplot.get(classId);
			}
			
			if(predictedclass.get(docId).equalsIgnoreCase(Utils.actual_class.get(docId))){
				ap.actual += 1;
				ap.predicted +=1;
			}else{
				ap.actual += 1;
			}
			bernoullibarplot.put(classId, ap);
			
		}
		bernoulliplot.put(fullVocab.size(),bm.calculatePreictionAcc(predictedclass));
	}

	public static List<Integer> setvocabsizes() {
		List<Integer> vocabsize = new ArrayList<Integer>();
		//vocabsize.add(200);
		vocabsize.add(100);
		vocabsize.add(500);
		vocabsize.add(1000);
		vocabsize.add(2500);
		vocabsize.add(5000);
		vocabsize.add(7500);
		vocabsize.add(10000);
		vocabsize.add(12500);
		vocabsize.add(25000);
		vocabsize.add(50000);

		return vocabsize;
	}
}
