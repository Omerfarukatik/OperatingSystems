import java.util.Scanner;

public class MatrixCarpimi {
	static int[][] matrisA;
	static int[][] matrisB;
	static int[][] matrisC;
	static int SATIR_A, SUTUN_A, SATIR_B, SUTUN_B;
	static int adim_i = 0;

	static class matrixCarp implements Runnable {
		int i;

		matrixCarp(int i) {
			this.i = i;
		}

		@Override
		public void run() {
			for (int j = 0; j < SUTUN_B; j++) {
				for (int k = 0; k < SUTUN_A; k++) {
					matrisC[i][j] += matrisA[i][k] * matrisB[k][j];
				}
			}
		}
	}

	public static void main(String[] args) {
		Scanner tarayici = new Scanner(System.in);

		System.out.print("Matris A için satır sayısını girin: ");
		SATIR_A = tarayici.nextInt();
		System.out.print("Matris A için sütun sayısını girin: ");
		SUTUN_A = tarayici.nextInt();
		System.out.print("Matris B için satır sayısını  girin: ");
		SATIR_B = tarayici.nextInt();
		System.out.print("Matris B için sütun sayısını girin: ");
		SUTUN_B = tarayici.nextInt();

		if (SUTUN_A != SATIR_B) {
			System.out.println("Verilen boyutlarla matris çarpımı mümkün değil.");
			tarayici.close();
			System.exit(1);

		}

		matrisA = new int[SATIR_A][SUTUN_A];
		matrisB = new int[SATIR_B][SUTUN_B];
		matrisC = new int[SATIR_A][SUTUN_B];

		System.out.println("Matris A için değerleri girin:");
		for (int i = 0; i < SATIR_A; i++) {
			for (int j = 0; j < SUTUN_A; j++) {
				matrisA[i][j] = tarayici.nextInt();
			}
		}

		System.out.println("Matris B için değerleri girin:");
		for (int i = 0; i < SATIR_B; i++) {
			for (int j = 0; j < SUTUN_B; j++) {
				matrisB[i][j] = tarayici.nextInt();
			}
		}

		System.out.println("Matris A");
		for (int i = 0; i < SATIR_A; i++) {
			for (int j = 0; j < SUTUN_A; j++) {
				System.out.print(matrisA[i][j] + " ");
			}
			System.out.println();
		}

		System.out.println("Matris B");
		for (int i = 0; i < SATIR_B; i++) {
			for (int j = 0; j < SUTUN_B; j++) {
				System.out.print(matrisB[i][j] + " ");
			}
			System.out.println();
		}

		Thread[] isParcaciklari = new Thread[SATIR_A];

		for (int i = 0; i < SATIR_A; i++) {
			isParcaciklari[i] = new Thread(new matrixCarp(adim_i++));
			isParcaciklari[i].start();
		}

		for (int i = 0; i < SATIR_A; i++) {
			try {
				isParcaciklari[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("A ve B'nin Çarpımı");
		for (int i = 0; i < SATIR_A; i++) {
			for (int j = 0; j < SUTUN_B; j++) {
				System.out.print(matrisC[i][j] + " ");
			}
			System.out.println();
		}
		tarayici.close();
	}
}
