

public class ExcepcionApp {
    public static void main(String[] args) {

        String str1="12";
	    String str2="0";
        String respuesta;
	    int numerador, denominador, cociente;
        try{
            numerador=Integer.parseInt(str1);
            denominador=Integer.parseInt(str2);
            cociente=numerador/denominador;
            respuesta=String.valueOf(cociente);
        }catch(NumberFormatException ex){
            respuesta="Se han introducido caracteres no num�ricos";
        }catch(ArithmeticException ex){
            respuesta="Divisi�n entre cero";
        }
        System.out.println(respuesta);

     
    }
}