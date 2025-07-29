package ca.yorku.cmg.istardt.reader.outputs.istardtx;

import java.io.File;

import ca.yorku.cmg.istardt.reader.goalgraph.GoalModel;
import ca.yorku.cmg.istardt.reader.inputs.drawio.DrawIOReader;
import ca.yorku.cmg.istardt.reader.outputs.common.ErrorReporter;
import ca.yorku.cmg.istardt.reader.outputs.common.Translator;

public class mx2dtx {

	static String inputFile = "";
	static String outputFile ="";
	static boolean printUsage = false;

	
	public static void main(String[] args) {

		ErrorReporter err = new ErrorReporter();
		mx2dtx mainClass = new mx2dtx();
		GoalModel model = new GoalModel(err);
		DrawIOReader reader = new DrawIOReader(err);
		Translator writer = new dtxTranslator();
		

		try {

			
			mainClass.processArgs(args);
			
			System.out.println("\n\n == mx2dtx == \n");
			
			//Read from the XML
			reader.setInFile(inputFile);
			model = reader.readXML();
			
			//Produce the translation
			writer.setOutFile(outputFile);
			writer.setModel(model);
			writer.translate(true);
			
			if (err.hasAnything()) {
				err.printAll();
			}

		} catch (Exception e) {
			System.err.println("[mx2dtx] Error: " + e.getMessage());
			//e.printStackTrace();
			if (printUsage) {
				System.out.println(mainClass.printUsage());
			}
			System.exit(1);
		}
	}
	
	private String printUsage() {
		String s = "";
		s = "Usage: mx2dtx [-options]\n" + 
				"where options are:\n" + 
				"    -f filename \t draw.io XML file \n" + 
				"    -o filename \t iStarDT-X XML file \n" + 
				"    -h \t\t\t prints this help \n";
		return(s);
	}

	private void processArgs(String[] args) throws Exception {
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.startsWith("-")) {
				char option = arg.charAt(1);
				switch (option) {
				case 'f':
					if (i + 1 < args.length) {
						inputFile = args[i + 1];
						i++;
					} else {
						printUsage = true;
						throw new Exception("Option -f requires a file name.");
					}
					break;
				case 'o':
					if (i + 1 < args.length) {
						outputFile = args[i + 1];
						i++;
					} else {
						printUsage = true;
						throw new Exception("Option -o requires a file name.");
					}
					break;
				case 'h':
					System.out.println(printUsage());
					System.exit(0);
				default:
					printUsage = true;
					throw new Exception("Unknown option: " + option);
				}
			} else {
				printUsage = true;
				throw new Exception("Invalid argument: " + arg);
			}
		}

		if (args.length == 0) {
			printUsage = true;
			throw new Exception("No arguments provided. Use -h for help.");
		}

		if (inputFile.isEmpty()) {
			printUsage = true;
			throw new Exception("No input file specified. Use -f to specify input file.");
		}

		File inpF = new File(inputFile);
		if (!inpF.exists()) {
			throw new Exception("Input file does not exist: " + inputFile);
		}
	}




}
