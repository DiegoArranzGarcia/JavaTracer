import java.util.ArrayList;


public class ArrayListTest1 {

	ArrayList<Integer> integers;
	
	public ArrayListTest1(){
		integers = new ArrayList<>();
		integers.add(1);
		integers.add(30);
		integers.add(1000);
	}
	
	public static void main(String[] args){
		ArrayListTest1 a = new ArrayListTest1();
		a.imprime();
	}

	private void imprime() {
		for (int i =0;i<integers.size();i++)
			System.out.println(integers.get(i));
	}
	
}
