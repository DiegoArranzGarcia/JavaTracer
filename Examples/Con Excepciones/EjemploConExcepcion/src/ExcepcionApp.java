

public class ExcepcionApp {
    public static void main(String[] args) {

        String str1="12";
	    String str2="0";
        String respuesta;
	    
        int numerador, denominador, cociente;
            numerador=Integer.parseInt(str1);
            denominador=Integer.parseInt(str2);
            cociente=numerador/denominador;
            respuesta=String.valueOf(cociente);
	    
        System.out.println(respuesta);

     
    }
}