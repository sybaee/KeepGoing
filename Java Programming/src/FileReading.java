import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileReading {
	private int[] instatus = new int[8];
	private String ch;
	private String[] temp = new String[9];
	File file = new File("C:\\Users\\user\\Desktop\\Status.txt");

	public FileReading(){
		try {
			FileReader filereader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(filereader);
			String sLine = "";
			while ((sLine = bufReader.readLine()) != null) {
				temp = sLine.split(" ");
			}
			bufReader.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			System.out.println(e);
		}
		
		try {
			for(int i=0;i<8;i++) instatus[i] = Integer.parseInt(temp[i]);
		} catch(NumberFormatException e) {
			System.out.println(e);
		}
		ch = temp[8];
	}
	
	public int[] getstatus() { return this.instatus; }
	public String getch() { return this.ch; }
}
