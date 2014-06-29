package com.prueba;

import java.util.Random;

public class MergeSort {

	public int[] array;

	public MergeSort() {
		this.array = new int[8];
		randomizeArray(this.array);
	}

	public MergeSort(int[] array) {
		this.array = array;
		randomizeArray(this.array);
	}

	public int[] merge() {
		return mergesort(this.array);
	}

	public int[] mergesort(int[] L) {
		int n = L.length;

		if (n > 1) {
			int m = (int) (Math.ceil(n / 2.0));
			int[] L1 = new int[m];
			int[] L2 = new int[n - m];

			for (int i = 0; i < m; i++) {
				L1[i] = L[i];
			}
			for (int i = m; i < n; i++) {
				L2[i - m] = L[i];
			}
			L = merge(mergesort(L1), mergesort(L2));
		}
		return L;
	}

	public int[] merge(int[] L1, int[] L2) {
		int[] L = new int[L1.length + L2.length];
		int i = 0;
		int j = 0;
		while (i < L1.length && j < L2.length) {
			if (L1[i] < L2[j]) {
				L[i + j] = L1[i];
				i++;
			} else {
				L[i + j] = L2[j];
				j++;
			}
		}
		
		for (int index=i;index<L1.length;index++){
			 L[i+j] = L1[index];
			 i++;
		}
		
		for (int index=j;index<L2.length;index++){
			 L[i+j] = L2[index];
			 j++;
		}
		
		return L;
	}

	private void randomizeArray(int[] array) {
		Random ran = new Random();
		int x;
		for (int i = 0; i < array.length; i++) {
			x = (int) (ran.nextDouble() * 10000);
			array[i] = x;
		}
	}

}