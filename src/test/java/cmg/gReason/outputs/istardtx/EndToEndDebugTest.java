package cmg.gReason.outputs.istardtx;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import cmg.gReason.goalgraph.GoalModel;
import cmg.gReason.inputs.drawio.DrawIOReader;
import cmg.gReason.outputs.common.ErrorReporter;
import cmg.gReason.outputs.common.Translator;

class EndToEndDebugTest {

	@Test
	void test_Order() throws IOException {
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
			writer.translate(false);
					
		} catch (Exception e) {
			System.err.println("[mx2dtx] Error: " + e.getMessage());
			System.exit(1);
		}
		
        Path actual = Path.of("src/test/resources/Order.istardtx");
        Path expected = Path.of("src/test/resources/Order-Authoritative.istardtx");

        List<String> actualLines = Files.readAllLines(actual);
        List<String> expectedLines = Files.readAllLines(expected);

        assertEquals(expectedLines, actualLines, "Files do not match!");
		
	}

	@Test
	void test_Build() throws IOException {

		ErrorReporter err = new ErrorReporter();
		DrawIOReader reader = new DrawIOReader(err);
		GoalModel model = new GoalModel(err);
		Translator writer = new dtxTranslator();


		try {
			//Read from the XML
			String inputFile = "src/test/resources/Build.drawio"; 
			
			reader.setInFile(inputFile);
			model = reader.readXML();

			//Produce the translation
			
			String outputFile = "src/test/resources/Build.istardtx";
			writer.setOutFile(outputFile);
			writer.setModel(model);
			writer.translate(false);
					
		} catch (Exception e) {
			System.err.println("[mx2dtx] Error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
        Path actual = Path.of("src/test/resources/Build.istardtx");
        Path expected = Path.of("src/test/resources/Build-Authoritative.istardtx");

        List<String> actualLines = Files.readAllLines(actual);
        List<String> expectedLines = Files.readAllLines(expected);

        assertEquals(expectedLines, actualLines, "Files do not match!");

	}

	@Test
	void test_Travel() throws IOException {

		ErrorReporter err = new ErrorReporter();
		DrawIOReader reader = new DrawIOReader(err);
		GoalModel model = new GoalModel(err);
		Translator writer = new dtxTranslator();


		try {
			//Read from the XML
			String inputFile = "src/test/resources/OrganizeTravel.drawio"; 
			
			reader.setInFile(inputFile);
			model = reader.readXML();

			//Produce the translation
			
			String outputFile = "src/test/resources/OrganizeTravel.istardtx";
			writer.setOutFile(outputFile);
			writer.setModel(model);
			writer.translate(false);
					
		} catch (Exception e) {
			System.err.println("[mx2dtx] Error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
        Path actual = Path.of("src/test/resources/OrganizeTravel.istardtx");
        Path expected = Path.of("src/test/resources/OrganizeTravel-Authoritative.istardtx");

        List<String> actualLines = Files.readAllLines(actual);
        List<String> expectedLines = Files.readAllLines(expected);

        assertEquals(expectedLines, actualLines, "Files do not match!");
	}

	

	@Test
	void test_OrderVer2() throws IOException {

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
			writer.translate(false);
					
		} catch (Exception e) {
			System.err.println("[mx2dtx] Error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
        Path actual = Path.of("src/test/resources/OrderVer2.istardtx");
        Path expected = Path.of("src/test/resources/OrderVer2-Authoritative.istardtx");

        List<String> actualLines = Files.readAllLines(actual);
        List<String> expectedLines = Files.readAllLines(expected);

        assertEquals(expectedLines, actualLines, "Files do not match!");

	}

	
	@Tag("this")
	@Test
	void test_Heating() throws IOException {

		ErrorReporter err = new ErrorReporter();
		DrawIOReader reader = new DrawIOReader(err);
		GoalModel model = new GoalModel(err);
		Translator writer = new dtxTranslator();


		try {
			//Read from the XML
			String inputFile = "src/test/resources/Heating.drawio"; 
			
			reader.setInFile(inputFile);
			model = reader.readXML();

			//Produce the translation
			
			String outputFile = "src/test/resources/Heating.istardtx";
			writer.setOutFile(outputFile);
			writer.setModel(model);
			writer.translate(false);
					
		} catch (Exception e) {
			System.err.println("[mx2dtx] Error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

	
	@Tag("this")
	@Test
	void test_SpecPrep() throws IOException {

		ErrorReporter err = new ErrorReporter();
		DrawIOReader reader = new DrawIOReader(err);
		GoalModel model = new GoalModel(err);
		Translator writer = new dtxTranslator();


		try {
			//Read from the XML
			String inputFile = "src/test/resources/SpecPreparation.drawio"; 
			
			reader.setInFile(inputFile);
			model = reader.readXML();

			//Produce the translation
			
			String outputFile = "src/test/resources/SpecPreparation.istardtx";
			writer.setOutFile(outputFile);
			writer.setModel(model);
			writer.translate(false);
					
		} catch (Exception e) {
			System.err.println("[mx2dtx] Error: " + e.getMessage());
			e.printStackTrace();
			//System.exit(1);
		}
	}
	
}
