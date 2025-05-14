package cmg.gReason;

import java.io.File;

import cmg.gReason.inputs.drawio.BooleanExpressionParser;
import cmg.gReason.inputs.drawio.ConditionExpressionParser;
import cmg.gReason.inputs.drawio.DrawIOReader;
import cmg.gReason.inputs.drawio.IdentifierRegistry;

public class g2dt {
	
	static String inputFile = "";
	static String outputFile ="";
	static boolean printUsage = false;
	
	
	private static String printUsage() {
		String s = "";
		s = "Usage: dtg [-options]\n" + 
				"where options are:\n" + 
				"    -f filename \t draw.io XML file \n" + 
				"    -o filename \t iStarDT-X XML file \n" + 
				"    -h \t\t\t prints this help \n";
		return(s);
	}
	
	private static void processArgs(String[] args) throws Exception {
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
       		//throw new Exception("No input file");
       		throw new Exception("No input file.");
       }

       if (inputFile.isEmpty()) {
    	   printUsage = true;
    	   throw new Exception("No input file.");
       }
       
       File inpF = new File(inputFile);
       if (!inpF.exists()) throw new Exception("Input file does not exist:" + inputFile);
	}
	
	

	public static void main(String[] args) {
	  
	  DrawIOReader app = new DrawIOReader();
	  
	  try {
		  processArgs(args);
		  app.setInFile(inputFile);
		  app.setOutFile(outputFile);
	  } catch (Exception e1) {
		  System.err.println(e1.getMessage());
		  if (printUsage)
		  	System.out.println(printUsage());
          try {
        	System.err.println("Error reading file: trying default test file."); 
        	app.setInFile("./src/main/resources/OragnizeTravelNew.drawio");
		  	} catch (Exception e) {
		  		System.out.println("Current directory:" + System.getProperty("user.dir"));
		  	}
	  }

	  //ConditionExpressionParser c = new ConditionExpressionParser(new IdentifierRegistry());
	  //System.out.println(c.parse("bookRefundableTickets OR PREV(nonRefTixFailed) AND (cost GT 50) AND (privacy LT 0.5) AND headAvailable OR flightsExist"));
	  
	  app.translateToIstarDTX();
	  
	}
}
