
public class ArrayTest1 {
	
	private int array[];
	
	public ArrayTest1(){
		initialize();
	}
	
	public static void main(String args[]){
		ArrayTest1 test = new ArrayTest1();
		test.print();
		test.delete();
	}

	private void initialize() {
		array = new int[10];
		for (int i=0;i<array.length;i++){
			array[i] = i;
		}
	}

	private void print() {
		for (int i=0;i<array.length;i++){
			System.out.println(array[i]);
		}
	}

	private void delete() {
		array = null;
	}

}
