/*
 * The purpose of this class is to run the main method.
 */
public class SortMain {
	public static void main(String args[]){
		int[] sizes = {10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000};	//Input desired test numbers here
		BenchmarkSorts benchmark = new BenchmarkSorts(sizes);
		benchmark.runSorts();
		benchmark.displayReport();
	}
}
