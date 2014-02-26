
public class MainArguments {

	public static void main(String[] args){
		
		if (args.length==2)
		
			System.out.println("Buenos días " + args[0] + ", son las : " + args[1]);
		
		else
			
			System.out.println("Debes usar dos argumentos :(");
		
	}
	
}
