package codeGadgetGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileOutput {
	private ArrayList<CdgWrapper> cdgWrappers;
	private String outputPath;
    private Integer count = 1;
	public FileOutput(ArrayList<CdgWrapper> _cdgwrappers, String _outputPath) {
		this.cdgWrappers = _cdgwrappers;
		this.outputPath = _outputPath;
	}
	
	private static ArrayList<String> readFile(File fin) throws IOException {
	    FileInputStream fis = new FileInputStream(fin);
	    ArrayList<String> lines = new ArrayList<String>();
	    //Construct BufferedReader from InputStreamReader
	    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
	    String line = null;
	    while ((line = br.readLine()) != null) {
	        lines.add(line);
	    }
	    br.close();
	    
	    return lines;
	}
	
	public void output() throws Exception{
		PrintWriter resultOut = new PrintWriter(this.outputPath,"UTF-8");
		for(CdgWrapper cdg : cdgWrappers) {
			String header = count + " " + cdg.getFilePath() + " " + cdg.getRule() + " " + cdg.getApiLine();
			resultOut.println(header);
			File fin = new File(cdg.getFilePath());
			ArrayList<String> fileLines = this.readFile(fin);
			for(Integer i : cdg.getCdgLineList()) {
				resultOut.println(fileLines.get(i-1).trim());
			}
			resultOut.println("---------------------------------");
			this.count++;
		}
		resultOut.close();
	}
}
