import java.util.Scanner;


public class Test1 {

	public static void main(String[] args) {
		
		System.out.print("Voy a leer:");
		
		Scanner scanner = new Scanner(System.in);
		String linea1 = scanner.nextLine();
		
		System.err.println("Error " + linea1);
		
	}
	
}
