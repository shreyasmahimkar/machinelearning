import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Utils {
	
	
	static HashMap<String, Integer> wordfreq = new LinkedHashMap<String, Integer>();
	static HashMap<String, String> Documents = new HashMap<String, String>();
	static HashMap<String, String> Classes = new LinkedHashMap<String, String>();
	static HashMap<String, Integer> countDocumentsinClasses = new HashMap<String, Integer>();
	static HashMap<String, List<String>> term_doc = new HashMap<String, List<String>>();
	static HashMap<String, String> doc_class = new LinkedHashMap<String, String>();
	static HashMap<String, HashMap<String, Integer>> doc_term_train = new LinkedHashMap<String, HashMap<String,Integer>>();
	static HashMap<String, HashMap<String, Integer>> doc_term_test = new LinkedHashMap<String, HashMap<String,Integer>>();
	static HashMap<String, String> actual_class = new LinkedHashMap<String, String>();
	
	public static void actualDocumentClasses(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String sCurrentLine;
		int docId = 0;
		while ((sCurrentLine = br.readLine()) != null) {
			docId +=1;
			actual_class.put(Integer.toString(docId), sCurrentLine.trim());
		}
		
	}
	
	public static void createTermsinDocTrain(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String sCurrentLine;
		String [] tokens;
		while ((sCurrentLine = br.readLine()) != null) {
			tokens = sCurrentLine.split(" ");
			
			HashMap<String,Integer> inner = new HashMap<String,Integer>();
			if (!doc_term_train.containsKey(tokens[0])){
				inner.put(tokens[1],Integer.parseInt(tokens[2]));
				doc_term_train.put(tokens[0], inner );	
			}else{
				inner = doc_term_train.get(tokens[0]);
				inner.put(tokens[1],Integer.parseInt(tokens[2]));
				doc_term_train.put(tokens[0], inner);
			}
		}
		
		
	}
	
	public static void createTermsinDocTest(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String sCurrentLine;
		String [] tokens;
		while ((sCurrentLine = br.readLine()) != null) {
			tokens = sCurrentLine.split(" ");
			
			HashMap<String,Integer> inner = new HashMap<String,Integer>();
			if (!doc_term_test.containsKey(tokens[0])){
				inner.put(tokens[1],Integer.parseInt(tokens[2]));
				doc_term_test.put(tokens[0], inner );	
			}else{
				inner = doc_term_test.get(tokens[0]);
				inner.put(tokens[1],Integer.parseInt(tokens[2]));
				doc_term_test.put(tokens[0], inner);
			}
		}
		
		
	}
	
	
	
	public static void readfile(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String sCurrentLine;
		String [] tokens;
		while ((sCurrentLine = br.readLine()) != null) {
			tokens = sCurrentLine.split(" ");	
			Documents.put(tokens[0], "");
			
			List<String> inner = new ArrayList<String>();
			if (!term_doc.containsKey(tokens[1])){
				inner.add(tokens[0]);
				term_doc.put(tokens[1], inner );	
			}else{
				inner = term_doc.get(tokens[1]);
				inner.add(tokens[0]);
				term_doc.put(tokens[1], inner);
			}
			
			if (!wordfreq.containsKey(tokens[1])){
				wordfreq.put(tokens[1], Integer.parseInt(tokens[2]));
			}else{
				wordfreq.put(tokens[1], Integer.parseInt(tokens[2]) + wordfreq.get(tokens[1]));
			}
		}
		wordfreq = sortByValues(wordfreq);
		//System.out.println(wordfreq);
		writetofile("word_freq.txt", wordfreq);
	}
	
	public static void countdocsinclass(String filename) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String sCurrentLine;
		int docId = 0;
		while ((sCurrentLine = br.readLine()) != null) {
			docId = docId+1;
			doc_class.put(Integer.toString(docId), sCurrentLine.trim());
			if(countDocumentsinClasses.containsKey(sCurrentLine.trim())){
				countDocumentsinClasses.put(sCurrentLine.trim(), countDocumentsinClasses.get(sCurrentLine.trim()) +1);
			}else{
				countDocumentsinClasses.put(sCurrentLine.trim(), 1);
			}
		}
		
	}
	
	public static void readClasses(String filename) throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String sCurrentLine;
		String [] tokens;
		while ((sCurrentLine = br.readLine()) != null) {
			tokens = sCurrentLine.split(" ");
			Classes.put(tokens[1], tokens[0]);
		}
		
	}
	
	public static List<String> readVocabulary(String fileName) throws IOException{
		List<String> vocab = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String sCurrentLine;
		String [] tokens;
		while ((sCurrentLine = br.readLine()) != null) {
			tokens = sCurrentLine.split(" ");
			vocab.add(tokens[0]);
		}
		
		return vocab;
	}

	public static void writetofile(String fname, HashMap<String, Integer> wf) throws IOException{
		File file = new File(fname);
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		} else {
			file.delete();
			file.createNewFile();
		}
		
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		StringBuilder st = null;
		
		for (Map.Entry rec : wf.entrySet()){
			st = new StringBuilder();
			st.append(rec.getKey());
			st.append(" ");
			st.append(rec.getValue());
			st.append("\n");
			bw.write(st.toString());
		}
		bw.close();
	}
	
	public static HashMap<String, Integer> sortByValues(HashMap<String, Integer> map) {
		List list = new LinkedList(map.entrySet());
		// Defined Custom Comparator here
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue())
						.compareTo(((Map.Entry) (o1)).getValue());
			}
		});

		// Here I am copying the sorted list in HashMap
		// using LinkedHashMap to preserve the insertion order
		HashMap sortedHashMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		}
		return sortedHashMap;
	}
}
