import java.util.HashSet;
import java.util.Iterator;


public class SetTest1 {

	HashSet<Integer> integers;
	
	public SetTest1(){
		integers = new HashSet<>();
		integers.add(1);
		integers.add(30);
		integers.add(1000);
	}
	
	public static void main(String[] args){
		SetTest1 a = new SetTest1();
		a.imprime();
	}

	private void imprime() {
		Iterator<Integer> iterator = integers.iterator();
		while(iterator.hasNext())
			System.out.println(iterator.next());
	}
	
}
