import java.util.Scanner;


public class Test2 {

	public static void main(String[] args) {
		System.out.print("A dormir:");
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		
		System.out.print("Voy a leer:");
		
		Scanner scanner = new Scanner(System.in);
		String linea1 = scanner.nextLine();
		String linea2 = scanner.nextLine();
		
		System.err.println("Error " + linea1);
		System.out.println("Salida " + linea2);
	}
	
}
