package json;
import java.util.*;
import java.util.stream.*;

public class Utility {
	private static Utility uniqueInstance;
	
	public Utility() {}
	
	//Singleton - Pattern (Creational)
	public static Utility getInstance() {
		if (uniqueInstance == null)
		{ 
			uniqueInstance = new Utility();
		}
		return uniqueInstance;
	}

	////////////////////////////////////////////////Mathematics///////////////////////////////////////////
	//log base 2
	public int log2(int N)
    {
        // calculate log2 N indirectly using log() method
        int result = (int)(Math.log(N) / Math.log(2));
  
        return result;
    }
	
	//Compute Standard deviation
	public double computeSD(Map<String, Double> m) {
		ArrayList<Double> x = new ArrayList<Double>();
		for (Double k : m.values())
			x.add(k);
		double total = x.stream().mapToDouble(y -> y).sum();
		double mean = total/x.size();
		double sum = x.stream().mapToDouble(y -> Math.pow(y-mean, 2)).sum();

		return Math.sqrt(sum/x.size());
	}
	
	////////////////////////////////////////////////Sorted ///////////////////////////////////////////
	//Order follow by values
	public HashMap<String, Integer> intSorted(HashMap<String, Integer> m){
		return m.entrySet()
				.stream()
				.sorted(Map.Entry.<String, Integer>comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
	
}
