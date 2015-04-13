import org.apache.commons.math3.*; //TODO Reduce Import to include only the needed packages (streamlining)

import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class saniPOC {
	private int tu = -1;
	private int tg = -1;
	private int n = -1;
	
	private float[][] tuTg = new float[8][50];
	private float[][] tTg = new float[8][50];

	public static void main(String[] args) {
		saniPOC sani = new saniPOC();
		
		sani.loadArrays();
		sani.input();
		
        if (sani.verifyInput()){
        	System.out.println("starting sani calculation with Tu = " + sani.tu + ", Tg = " + sani.tg);
        	sani.n = sani.calculateN();
        	System.out.println("\tN is " + sani.n);
        }

	}
	
	public int loadArrays() {
		System.out.println("Loading stored arrays TuTg and tTg ...");
		final String[] files = new String[2];
		files[0] = "./lib/T_Tg.csv";
		files[1] = "./lib/Tu_Tg.csv";
		for (String file : files){
			try{
				Path p = Paths.get(file);
				String line = null;
				InputStream in = Files.newInputStream(p);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				for (int i = 0; i<8; i++) {
					line = reader.readLine();
					String[] splits = line.split(",");
					for (int j = 0; j<50; j++){
						float value = 0;
						if (splits[j].equals("Inf")) {
							value = Float.parseFloat("NaN");
						}
						else {
							value = Float.parseFloat(splits[j]);
						}
						if (file == files[0]) {
							tTg[i][j] = value;
						}
						else {
							tuTg[i][j] = value;
						}
					}
					
				}
			}
			catch (IOException x) {
				System.err.println(x);
				return 0;
			}
		}
		return 1;
	}
	
	public void input() {
		final Scanner scanner = new Scanner( System.in );
        System.out.print("Enter Value for Tu: ");
        tu = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Value for Tg: ");
        tg = Integer.parseInt(scanner.nextLine());
        scanner.close();
	}
	
	public boolean verifyInput() {
		if (tu <= 0 || tg <= 0) {
			System.out.println("Values must be greater than 0");
			return false;
		}
		else {
			float v = tu / (float)tg;
			if ( v > 0.64173) {
				System.out.println("Tu/Tg too great --> N would be greater than 8");
				return false;
			}
			else if (v < 0.001) {
				System.out.println("Tu/Tg too small --> N would be less than 1");
				return false;
				
			}
			else {
				return true;	
			}
		}
	}
	
	public int calculateN() {
		int n = -1;
		float v = tu / (float)tg;
		if (v <= 0.103638) {
			n = 2;
		}
		else if (v <= 0.218017) {
			n = 3;
		}
		else if (v <= 0.319357) {
			n = 4;
		}
		else if (v <= 0.410303) {
			n = 5;
		}
		else if (v <= 0.4933) {
			n = 6;
		}
		else if (v <= 0.5700) {
			n = 7;
		}
		else if (v <= 0.64173) {
			n = 8;
		}
		else {
			n = 10;
		}
		return n;
	}

}
