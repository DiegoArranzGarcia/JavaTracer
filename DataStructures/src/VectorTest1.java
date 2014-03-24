import java.util.Vector;


public class VectorTest1 {

	Vector<Integer> integers;
	
	public VectorTest1(){
		integers = new Vector<>();
		integers.add(1);
		integers.add(30);
		integers.add(1000);
	}
	
	public static void main(String[] args){
		VectorTest1 a = new VectorTest1();
		a.imprime();
	}

	private void imprime() {
		for (int i =0;i<integers.size();i++)
			System.out.println(integers.get(i));
	}
	
}
