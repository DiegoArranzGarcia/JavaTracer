
import java.util.HashMap;


public class HashMapTest1 {

	HashMap<Integer,Integer> integers;
	
	public HashMapTest1(){
		integers = new HashMap<>();
		integers.put(0,100);
		integers.put(1,200);
		integers.put(2,300);
	}
	
	public static void main(String[] args){
		HashMapTest1 a = new HashMapTest1();
		a.imprime();
	}

	private void imprime() {
		for (int i =0;i<integers.size();i++)
			System.out.println(integers.get(i));
	}
	
}
