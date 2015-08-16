/*
 * the purpose of this class is to create an exception to throw
 * when the sort method runs unsuccessfully.
 */
public class UnsortedException extends Exception{
	public UnsortedException(int size) {
		System.out.println("Error: list of size " + size + " not sorted correctly.");
	}
}
