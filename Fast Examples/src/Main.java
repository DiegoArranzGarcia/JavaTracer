
public class Main {

	public static void main(String args[]){
		int[] array = {1,23,3,13,12,3123};
		Person p1 = new Person(1, "", "b");
		Person p2 = new Person(1, "", "b",p1);
		p1.bestFriend = p2;
		prueba(1,p1,p2);
	}
	
	public static Person prueba(int i,Person p1,Person p2){
		i = 0;
		return p2.bestFriend;
	}
}
