
public class MergeSortLauncher {
	
	public static void main(String args[]){
		MergeSort mergeSort = new MergeSort();
		int[] result = mergeSort.merge();
		printArray(result);
	}
	
   public static void printArray(int[] array){
        for(int i = 0; i < array.length; i++){
            System.out.println(array[i]);
        }
    }
}
