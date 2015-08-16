import java.util.ArrayList;
import java.util.Date;
import java.util.Arrays;
import java.util.Stack;
/*
 * the purpose of this class is to sort the arrays passed from
 * SortInterface via Heap Sort iteratively and recursively.
 */
public class Heapsort implements SortInterface{

	private Stack<Integer> recursiveHeapStack;
	private Stack<Integer> iterativeHeapStack;
	private int count;
	private long time;

	
	//ITERATION
	
	@Override //iterate through the elements to create leaves of a heap tree then sort, re-heapify, repeat...
	public void iterativeSort(int[] list) {
		time = java.lang.System.currentTimeMillis();
		count = 0;
		iterativeHeapStack = arrayToHeap(list); //convert array to stack for easier management upon sorting
		iterativeHeapify(iterativeHeapStack); //create heap
		for (int index = 0; index < list.length; index++){ //sort heap into array
			list[index] = iterativeHeapStack.remove(0); //remove root node
			iterativeHeapify(iterativeHeapStack); //re-heapify to maintain tree
		}
		try { //check the newly sorted list
			check(list);
		} catch (UnsortedException e) {
			e.printStackTrace();
		}
		time = (long) Math.abs(time - java.lang.System.currentTimeMillis());	//calculate elapsed time
	}
	//following algorithm inspired by: http://en.wikibooks.org/wiki/Algorithm_Implementation/Sorting/Heapsort
	//iterative heapify method with stack - create min-heap
	private void iterativeHeapify(Stack<Integer> heapStack){
		int childNode, parentNode, tempValue;	//set variables used within loop
		for (int node = 0; node < heapStack.size(); node++){ //loop through every node
			childNode = node;
            while (childNode > 0){	//until the childNode is the root node or more
    			count++;
                parentNode = (childNode - 1) / 2;	//set parent to the heap-node above child
                if (Integer.compare(heapStack.get(childNode), heapStack.get(parentNode)) > 0){	//swap min-heap violations
                	swap(heapStack, childNode, parentNode);
                    childNode = parentNode; //move up the tree from child to parent
                }
                else break;
            }
        }  
	}
	
	//RECURSION
	
	@Override
	public void recursiveSort(int[] list) {
		time = java.lang.System.currentTimeMillis();
		count = 0;
		recursiveHeapStack = arrayToHeap(list);
		for(int index = recursiveHeapStack.size() / 2; index > -1; index--){	//create heap
			maxHeapify(recursiveHeapStack, index);
		}
		for(int index = 0; index < list.length; index++){	//convert heap to list
			list[index] = extractMax(recursiveHeapStack);
		}
		try { //check the newly sorted list
			check(list);
		} catch (UnsortedException e) {
			e.printStackTrace();
		}
		time = (long) Math.abs(time - java.lang.System.currentTimeMillis());	//calculate elapsed time
	}
	//recurive heapify method with stack - create max-heap
	//inspired by http://ocw.mit.edu/courses/electrical-engineering-and-computer-science/6-006-introduction-to-algorithms-fall-2011/lecture-videos/MIT6_006F11_lec04.pdf
	private void maxHeapify(Stack<Integer> stack, int node){
		count++;
		int left = 2 * node + 1;
		int right = (node + 1) * 2;
		int max = node;
		if (left <= stack.size() - 1 && stack.get(left) > stack.get(node)){ //check child exists and if greater than parent
			max = left;
		}
		else {
			max = node;
		}
		if (right <= stack.size() - 1 && stack.get(right) > stack.get(max)){ //check child exists and if greater than parent
			max = right;
		}
		if (max != node){
			swap(stack, node, max);
			maxHeapify(stack, max);
		}
	}
	//extract the max then re-heapify
	private int extractMax(Stack<Integer> heap){
		int max = heap.get(0);
		swap(heap, 0, heap.size() - 1);
		heap.pop();
		maxHeapify(heap, 0);
		return max;
	}
		
	//MISCELLANEOUS

	//convert arrays to heaps for easy management during step two: sorting (add/remove)
	private Stack<Integer> arrayToHeap(int[] list) {
		Stack<Integer> outStack = new Stack<Integer>();
		for (int index = 0; index < list.length; index++){
			outStack.add(list[index]);
		}
		return outStack;
	}
	private static void swap(Stack<Integer> stack, int i, int j){
		int temp = stack.get(i);
		stack.set(i, stack.get(j));
		stack.set(j, temp);
	}
	@Override
	public int getCount() {
		return count;
	}

	@Override
	public long getTime() {
		return time;
	}	
	//check each sorted list for errors and throw exception
	private void check(int[] list) throws UnsortedException{
		for(int index = 0; index < list.length - 1; index++){
			if (list[index] < list[index + 1]){
				throw new UnsortedException(list.length);
			}
		}
	}
}
