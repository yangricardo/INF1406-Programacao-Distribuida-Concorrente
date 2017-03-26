package fileLog;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LogControl {

	static BufferedWriter bw = null;
	static FileWriter fw = null;
	
	public static void createFile(String nameFile){
		try {
			fw = new FileWriter(nameFile+".txt");
			bw = new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void writeMessage(String nameFile,String message){
		try {
			fw = new FileWriter(nameFile+".txt",true);
			bw = new BufferedWriter(fw);
			bw.write(message+"\n");
			System.out.println(message);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
	}
}
