
public class ArrayTest2 {
	
	private int array[];
	
	public static void main(String args[]){
		ArrayTest2 test = new ArrayTest2();
		test.initialize();
		test.changeArray();
		test.print();
	}

	private void initialize() {
		array = new int[10];
		for (int i=0;i<array.length;i++){
			array[i] = i;
		}
	}
	
	private void changeArray() {
		array = new int[5];
		for (int i=0;i<array.length;i++){
			array[i] = i*i;
		}
	}

	private void print() {
		for (int i=0;i<array.length;i++){
			System.out.println(array[i]);
		}
	}

}