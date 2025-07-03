package cmg.gReason.outputs.istardtx;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import cmg.gReason.goalgraph.GoalModel;
import cmg.gReason.inputs.drawio.DrawIOReader;
import cmg.gReason.outputs.common.ErrorReporter;
import cmg.gReason.outputs.common.Translator;

class EndToEndDebugTest {

	@Disabled
	@Test
	void test1() throws IOException {
		mx2dtx mainClass = new mx2dtx();
		ErrorReporter err = new ErrorReporter();
		DrawIOReader reader = new DrawIOReader(err);
		GoalModel model = new GoalModel(err);
		Translator writer = new dtxTranslator();


		try {
			//Read from the XML
			String inputFile = "src/test/resources/Order.drawio"; 
			
			reader.setInFile(inputFile);
			model = reader.readXML();

			//Produce the translation
			
			String outputFile = "src/test/resources/Order.istardtx";
			writer.setOutFile(outputFile);
			writer.setModel(model);
			writer.translate();
					
		} catch (Exception e) {
			System.err.println("[mx2dtx] Error: " + e.getMessage());
			System.exit(1);
		}
		
        Path actual = Path.of("src/test/resources/Order.istardtx");
        Path expected = Path.of("src/test/resources/Order-Authoritative.istardtx");

        List<String> actualLines = Files.readAllLines(actual);
        List<String> expectedLines = Files.readAllLines(expected);

        //assertEquals(expectedLines, actualLines, "Files do not match!");
		
	}

	
	@Test
	void test2() throws IOException {
		mx2dtx mainClass = new mx2dtx();
		ErrorReporter err = new ErrorReporter();
		DrawIOReader reader = new DrawIOReader(err);
		GoalModel model = new GoalModel(err);
		Translator writer = new dtxTranslator();


		try {
			//Read from the XML
			String inputFile = "src/test/resources/OrderVer2.drawio"; 
			
			reader.setInFile(inputFile);
			model = reader.readXML();

			//Produce the translation
			
			String outputFile = "src/test/resources/OrderVer2.istardtx";
			writer.setOutFile(outputFile);
			writer.setModel(model);
			writer.translate();
					
		} catch (Exception e) {
			System.err.println("[mx2dtx] Error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
		/*
        Path actual = Path.of("src/test/resources/OrderVer2.istardtx");
        Path expected = Path.of("src/test/resources/OrderVer2-Authoritative.istardtx");

        List<String> actualLines = Files.readAllLines(actual);
        List<String> expectedLines = Files.readAllLines(expected);

        assertEquals(expectedLines, actualLines, "Files do not match!");
		*/
	}
	
}
