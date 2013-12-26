package com.prueba;
import java.util.Random;

public class MergeSort{

	 public int[] array;
	 
	 public MergeSort(){
		 this.array = new int[10];
		 randomizeArray(this.array);
	 }
	 
	 public MergeSort(int[] array){
		 this.array = array;
		 randomizeArray(this.array);
	 }
	
	 public int[] merge(){
		 return mergesort(this.array);
	 }
	 
     public int[] mergesort(int[] L) {
        int n = L.length;
 
        if (n > 1){
            int m = (int) (Math.ceil(n/2.0));
            int [] L1 = new int[m];
            int [] L2 = new int[n-m];
 
            for (int i = 0; i < m; i++){
                L1[i] = L[i];
            }
            for (int i = m; i < n; i++){
                L2[i-m] = L[i];
            }
            L = merge(mergesort(L1), mergesort(L2));
        }
        return L;
    }
 
    public int[] delete(int [] l){
        int [] L = new int[l.length-1];
        for(int i = 1; i < l.length; i++){
            L[i-1] = l[i];
        }
        return L;
    }
 
    public int[] merge(int[] L1, int[] L2) {
         int[] L = new int[L1.length+L2.length];
         int i = 0;
         while ((L1.length != 0) && (L2.length != 0)) {
             if (L1[0] < L2[0]){
                 L[i++] = L1[0];
                 L1 = delete(L1);
                 if (L1.length == 0){
                     while (L2.length != 0) {
                         L[i++] = L2[0];
                         L2 = delete(L2);
                     }
                 }
             }
             else{
                 L[i++] = L2[0];
                 L2 = delete(L2);
                 if (L2.length == 0) {
                    while (L1.length != 0) {
                         L[i++] = L1[0];
                         L1 = delete(L1);
                    }
                 }
             }
         }
         return L;
    }
 
    private void randomizeArray(int[] array){
        Random ran = new Random();
        int x;
        for(int i = 0; i < array.length; i++){
            x = (int)(ran.nextDouble()*10000);
            array[i] = x;
        }
    }

}