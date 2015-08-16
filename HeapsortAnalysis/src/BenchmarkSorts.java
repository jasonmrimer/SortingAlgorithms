import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
/*
 * The purpose of this class is to call the iterative and recursive
 * sorting methods in YourSort, maintain & calculate benchmarking statistics, and
 * display those values.
 */
public class BenchmarkSorts{
	int[] sizes;
	long[][][] results; //[size][result type][50 trials per size] results used to calculate totals
	long[][] resultData; //[size][column header] cumulative results after calculations (used to display)
	Heapsort heapsort;	//Heapsort class to run sorts from
	
	//indeces of result types for results for readability
	int IT_COUNT = 0;
	int IT_TIME = 1;
	int RE_COUNT = 2;
	int RE_TIME = 3;
	//end index
		
	//indeces of column headers for result data for readability
	int DATA_SIZE = 0;
	int IT_AVG_COUNT = 1;
	int IT_ST_DEV_COUNT = 2;
	int IT_AVG_TIME = 3;
	int IT_ST_DEV_TIME = 4;
	int RE_AVG_COUNT = 5;
	int RE_ST_DEV_COUNT = 6;
	int RE_AVG_TIME = 7;
	int RE_ST_DEV_TIME = 8;
	//end index
	
	//constructor with size array from main
	BenchmarkSorts(int[] sizes){
		this.sizes = sizes;
		this.results = new long[sizes.length][4][50];
		this.resultData = new long[sizes.length][9];
		this.heapsort = new Heapsort();
	}
	//run both sort methods and tally count/time to accumulate average
	public void runSorts(){
		long it_count_total, it_time_total, re_count_total, re_time_total;	//hold running total for average
		for (int size = 0; size < sizes.length; size++){	//loop through each array size
			//reset all general values used to calculate averages
			it_count_total = 0;	//iterative count total
			it_time_total = 0;	//iterative time total
			re_count_total = 0;	//recursive count total
			re_time_total = 0;	//recursive time total
			resultData[size][DATA_SIZE] = sizes[size];	//set the size of array in the results data to display
			for (int arrayCount = 0; arrayCount < results[size][0].length; arrayCount++){	//project requires 50 arrays of each size
				int[] currentArray = new int[sizes[size]];	//initalize the array each step to utilize garbage collector for old arrays
				currentArray = randomGenerate(currentArray);	//randomize all elements
				//iterative
				heapsort.iterativeSort(currentArray.clone());	//iterative sort
				results[size][IT_COUNT][arrayCount] = heapsort.getCount();	//record to calculate standard deviation in calculateData
				it_count_total += heapsort.getCount();	//add to total to derive average
				results[size][IT_TIME][arrayCount] = heapsort.getTime();	//record to calculate standard deviation in calculateData
				it_time_total += heapsort.getTime();	//add to total to derive average
				//recursive
				heapsort.recursiveSort(currentArray.clone());	//recursive sort
				results[size][RE_COUNT][arrayCount] = heapsort.getCount();	//record to calculate standard deviation in calculateData
				re_count_total += heapsort.getCount();	//add to total to derive average
				results[size][RE_TIME][arrayCount] = heapsort.getTime();	//record to calculate standard deviation in calculateData
				re_time_total += heapsort.getTime();	//add to total to derive average
			}
			//calculate averages per size
			resultData[size][IT_AVG_COUNT] = (long) (it_count_total / results[size][0].length);
			resultData[size][IT_AVG_TIME] = (long) (it_time_total / results[size][0].length);
			resultData[size][RE_AVG_COUNT] = (long) (re_count_total / results[size][0].length);
			resultData[size][RE_AVG_TIME] = (long) (re_time_total / results[size][0].length);
		}
	}
	//generate random array to sort
	private int[] randomGenerate(int[] array) {
		for (int index = 0; index < array.length; index++){
			array[index] = (int) (Math.random() * array.length); 
		}
		return array;
	}
	//display table of results via JFrame
	public void displayReport(){
		calculateData(results);	//generate all standard deviations; averages calculated while sorting
		JFrame frame = new JFrame("CMSV451; Project 1 Results Data");
		JPanel panel = new JPanel();
		JTable table = new JTable();
		DefaultTableModel tableModel = new DefaultTableModel();
		String[] columnNames = {	//prepare column headers according to project specifications
				"Data Size", "Average Critical Operation Count", "Standard Deviation of Count", "Average Execution Time",
				"Standard Deviation of Time", "Average Critical Operation Count", "Standard Deviation of Count",
				"Average Execution Time", "Standard Deviation of Time"
		};
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();	//right-align all columns
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		for(int column = 0; column < resultData[0].length; column++){	//create columns
			tableModel.addColumn(columnNames[column]);
			table.setModel(tableModel);
			table.getColumnModel().getColumn(column).setCellRenderer(rightRenderer);	//align columns
		}
		for (int size = 0; size < resultData.length; size++){	//loop each size 
			tableModel.addRow(new Long[]{
				resultData[size][DATA_SIZE],
				resultData[size][IT_AVG_COUNT],
				resultData[size][IT_ST_DEV_COUNT],
				resultData[size][IT_AVG_TIME],
				resultData[size][IT_ST_DEV_TIME],
				resultData[size][RE_AVG_COUNT],
				resultData[size][RE_ST_DEV_COUNT],
				resultData[size][RE_AVG_TIME],
				resultData[size][RE_ST_DEV_TIME]
			});
		}
		table.setModel(tableModel);
		for(int column = 0; column < resultData[0].length; column++){	//align columns
			table.getColumnModel().getColumn(column).setCellRenderer(rightRenderer);
		}
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(tableModel);
		panel.setLayout(new BorderLayout());
		panel.add(new JScrollPane(table), BorderLayout.CENTER);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1700, 200);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	//calculate all standard deviations using the averages calculated in the runSorts() method
	private void calculateData(long[][][] input){
		long it_st_dev_count_total, it_st_dev_time_total, re_st_dev_count_total, re_st_dev_time_total;
		for (int size = 0; size < input.length; size++){	//loop each size 
			//get the standard deviations, use general values
			//reset all general values
			it_st_dev_count_total = 0;
			it_st_dev_time_total = 0;
			re_st_dev_count_total = 0;
			re_st_dev_time_total = 0;
			for (int array = 0; array < input[size][0].length; array++){	//loop each array within size
				//add all array values
				it_st_dev_count_total += (long) Math.pow((resultData[size][IT_AVG_COUNT] - input[size][IT_COUNT][array]), 2);
				it_st_dev_time_total += (long) Math.pow((resultData[size][IT_AVG_TIME] - input[size][IT_TIME][array]), 2);
				re_st_dev_count_total += (long) Math.pow((resultData[size][RE_AVG_COUNT] - input[size][RE_COUNT][array]), 2);
				re_st_dev_time_total += (long) Math.pow((resultData[size][RE_AVG_TIME] - input[size][RE_TIME][array]), 2);
			}
			//calculate standard deviations per size
			resultData[size][IT_ST_DEV_COUNT] = (long) Math.pow((it_st_dev_count_total / input[size][0].length), 0.5);
			resultData[size][IT_ST_DEV_TIME] = (long) Math.pow((it_st_dev_time_total / input[size][0].length), 0.5);
			resultData[size][RE_ST_DEV_COUNT] = (long) Math.pow((re_st_dev_count_total / input[size][0].length), 0.5);
			resultData[size][RE_ST_DEV_TIME] = (long) Math.pow((re_st_dev_time_total / input[size][0].length), 0.5);
		}
		
	}
}
