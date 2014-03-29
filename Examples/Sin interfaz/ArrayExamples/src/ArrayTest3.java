
public class ArrayTest3 {
	
	private int array[];
	
	public static void main(String args[]){
		ArrayTest3 test = new ArrayTest3();
		test.initialize();
		test.changeComponent();
		test.print();
	}

	private void initialize() {
		array = new int[10];
		for (int i=0;i<array.length;i++){
			array[i] = i;
		}
	}
	
	private void changeComponent() {
		array[2] = 0;
	}

	private void print() {
		for (int i=0;i<array.length;i++){
			System.out.println(array[i]);
		}
	}

}