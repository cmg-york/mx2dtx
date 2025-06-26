package cmg.gReason.outputs.common;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class OutputManager {
	
	private boolean replace;
	private String outFile = "";
	private Translator t = null;

	public OutputManager() {

	}
	
	public OutputManager(String outFile) {
		this.outFile = outFile;
	}
	
	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}

	public void setTranslator(Translator trans) {
		this.t = trans;
	}
	
	public boolean outFileEmpty() {
		return (outFile.equals(""));
	}
	
	/**
	 * To be called only if there is an output file set. Will throw error if not.
	 */
	public void saveOutput() {
		Writer fW;
		try {
			File myObj = new File(outFile);
			if (myObj.createNewFile()) {
				fW = new FileWriter(outFile, false);
				writeSpecToFile(fW);
				this.replace = false;
			} else {
				this.replace = true;
				fW = new FileWriter(outFile, false);
				writeSpecToFile(fW);
			}
		} catch (IOException e) {
			System.err.println("Error creating output file.");
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	private void writeSpecToFile(Writer fW) throws Exception {
		
		if (t==null)
			throw new Exception("Translator object not set.");
		
		try {
			fW.write(t.getSpec());
			fW.close();
			if (this.replace)
				System.out.println("Spec file updated: " + this.outFile);
			else
				System.out.println("Spec file created: " + this.outFile);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
}
