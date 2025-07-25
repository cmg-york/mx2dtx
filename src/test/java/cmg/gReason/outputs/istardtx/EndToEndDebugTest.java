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
			String inputFile = "src/test/resources/1.Order.drawio"; 
			
			reader.setInFile(inputFile);
			model = reader.readXML();

			//Produce the translation
			
			String outputFile = "src/test/resources/1.Order.istardtx";
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
	void test_OrderState() throws IOException {
		ErrorReporter err = new ErrorReporter();
		DrawIOReader reader = new DrawIOReader(err);
		GoalModel model = new GoalModel(err);
		Translator writer = new dtxTranslator();


		try {
			//Read from the XML
			String inputFile = "src/test/resources/OrderState.drawio"; 
			
			reader.setInFile(inputFile);
			model = reader.readXML();

			//Produce the translation
			
			String outputFile = "src/test/resources/OrderState.istardtx";
			writer.setOutFile(outputFile);
			writer.setModel(model);
			writer.translate(false);
					
		} catch (Exception e) {
			System.err.println("[mx2dtx] Error: " + e.getMessage());
			System.exit(1);
		}
		
	}
	
	@Test
	void test_Build_1R_Discrete() throws IOException {

		ErrorReporter err = new ErrorReporter();
		DrawIOReader reader = new DrawIOReader(err);
		GoalModel model = new GoalModel(err);
		Translator writer = new dtxTranslator();

		try {
			//Read from the XML
			String inputFile = "src/test/resources/2.1.Build_1R_Discrete.drawio"; 
			
			reader.setInFile(inputFile);
			model = reader.readXML();

			//Produce the translation
			
			String outputFile = "src/test/resources/2.1.Build_1R_Discrete.istardtx";
			writer.setOutFile(outputFile);
			writer.setModel(model);
			writer.translate(false);
					
		} catch (Exception e) {
			System.err.println("[mx2dtx] Error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
        //Path actual = Path.of("src/test/resources/Build.istardtx");
        //Path expected = Path.of("src/test/resources/Build-Authoritative.istardtx");

        //List<String> actualLines = Files.readAllLines(actual);
        //List<String> expectedLines = Files.readAllLines(expected);

        //assertEquals(expectedLines, actualLines, "Files do not match!");

	}

	@Test
	void test_Build_3R_Discrete() throws IOException {

		ErrorReporter err = new ErrorReporter();
		DrawIOReader reader = new DrawIOReader(err);
		GoalModel model = new GoalModel(err);
		Translator writer = new dtxTranslator();

		try {
			//Read from the XML
			String inputFile = "src/test/resources/2.2.Build_3R_Discrete.drawio"; 
			
			reader.setInFile(inputFile);
			model = reader.readXML();

			//Produce the translation
			String outputFile = "src/test/resources/2.2.Build_3R_Discrete.istardtx";
			writer.setOutFile(outputFile);
			writer.setModel(model);
			writer.translate(false);
					
		} catch (Exception e) {
			System.err.println("[mx2dtx] Error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
        //Path actual = Path.of("src/test/resources/Build.istardtx");
        //Path expected = Path.of("src/test/resources/Build-Authoritative.istardtx");

        //List<String> actualLines = Files.readAllLines(actual);
        //List<String> expectedLines = Files.readAllLines(expected);

        //assertEquals(expectedLines, actualLines, "Files do not match!");

	}

	
	@Test
	void test_Build_3R_Discrete_2() throws IOException {

		ErrorReporter err = new ErrorReporter();
		DrawIOReader reader = new DrawIOReader(err);
		GoalModel model = new GoalModel(err);
		Translator writer = new dtxTranslator();

		try {
			//Read from the XML
			String inputFile = "src/test/resources/2.3.Build_3R_Discrete_2.drawio"; 
			
			reader.setInFile(inputFile);
			model = reader.readXML();

			//Produce the translation
			String outputFile = "src/test/resources/2.3.Build_3R_Discrete_2.istardtx";
			writer.setOutFile(outputFile);
			writer.setModel(model);
			writer.translate(false);
					
		} catch (Exception e) {
			System.err.println("[mx2dtx] Error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
        //Path actual = Path.of("src/test/resources/Build.istardtx");
        //Path expected = Path.of("src/test/resources/Build-Authoritative.istardtx");

        //List<String> actualLines = Files.readAllLines(actual);
        //List<String> expectedLines = Files.readAllLines(expected);

        //assertEquals(expectedLines, actualLines, "Files do not match!");

	}
	
	
	@Test
	void test_Build_1R_Mixed() throws IOException {

		ErrorReporter err = new ErrorReporter();
		DrawIOReader reader = new DrawIOReader(err);
		GoalModel model = new GoalModel(err);
		Translator writer = new dtxTranslator();

		try {
			//Read from the XML
			String inputFile = "src/test/resources/2.4.Build_1R_Mixed.drawio"; 
			
			reader.setInFile(inputFile);
			model = reader.readXML();

			//Produce the translation
			String outputFile = "src/test/resources/2.4.Build_1R_Mixed.istardtx";
			writer.setOutFile(outputFile);
			writer.setModel(model);
			writer.translate(false);
					
		} catch (Exception e) {
			System.err.println("[mx2dtx] Error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
        //Path actual = Path.of("src/test/resources/Build.istardtx");
        //Path expected = Path.of("src/test/resources/Build-Authoritative.istardtx");

        //List<String> actualLines = Files.readAllLines(actual);
        //List<String> expectedLines = Files.readAllLines(expected);

        //assertEquals(expectedLines, actualLines, "Files do not match!");

	}

	
	@Test
	void test_Build_5R_Mixed() throws IOException {

		ErrorReporter err = new ErrorReporter();
		DrawIOReader reader = new DrawIOReader(err);
		GoalModel model = new GoalModel(err);
		Translator writer = new dtxTranslator();

		try {
			//Read from the XML
			String inputFile = "src/test/resources/2.5.Build_5R_Mixed.drawio"; 
			
			reader.setInFile(inputFile);
			model = reader.readXML();

			//Produce the translation
			String outputFile = "src/test/resources/2.5.Build_5R_Mixed.istardtx";
			writer.setOutFile(outputFile);
			writer.setModel(model);
			writer.translate(false);
					
		} catch (Exception e) {
			System.err.println("[mx2dtx] Error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
        //Path actual = Path.of("src/test/resources/Build.istardtx");
        //Path expected = Path.of("src/test/resources/Build-Authoritative.istardtx");

        //List<String> actualLines = Files.readAllLines(actual);
        //List<String> expectedLines = Files.readAllLines(expected);

        //assertEquals(expectedLines, actualLines, "Files do not match!");

	}

	@Test
	void test_Heating_1R_mixed() throws IOException {

		ErrorReporter err = new ErrorReporter();
		DrawIOReader reader = new DrawIOReader(err);
		GoalModel model = new GoalModel(err);
		Translator writer = new dtxTranslator();


		try {
			//Read from the XML
			String inputFile = "src/test/resources/3.1.Heating_1R_Mixed.drawio"; 
			
			reader.setInFile(inputFile);
			model = reader.readXML();

			//Produce the translation
			
			String outputFile = "src/test/resources/3.1.Heating_1R_Mixed.istardtx";
			writer.setOutFile(outputFile);
			writer.setModel(model);
			writer.translate(false);
					
		} catch (Exception e) {
			System.err.println("[mx2dtx] Error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	

	@Test
	void test_Travel() throws IOException {

		ErrorReporter err = new ErrorReporter();
		DrawIOReader reader = new DrawIOReader(err);
		GoalModel model = new GoalModel(err);
		Translator writer = new dtxTranslator();


		try {
			//Read from the XML
			String inputFile = "src/test/resources/4.1.OrganizeTravel.drawio"; 
			
			reader.setInFile(inputFile);
			model = reader.readXML();

			//Produce the translation
			
			String outputFile = "src/test/resources/4.1.OrganizeTravel.istardtx";
			writer.setOutFile(outputFile);
			writer.setModel(model);
			writer.translate(false);
					
		} catch (Exception e) {
			System.err.println("[mx2dtx] Error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		/*
        Path actual = Path.of("src/test/resources/OrganizeTravel.istardtx");
        Path expected = Path.of("src/test/resources/OrganizeTravel-Authoritative.istardtx");

        List<String> actualLines = Files.readAllLines(actual);
        List<String> expectedLines = Files.readAllLines(expected);

        assertEquals(expectedLines, actualLines, "Files do not match!");
        */
	}

	
	@Disabled
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
		
//        Path actual = Path.of("src/test/resources/OrderVer2.istardtx");
//        Path expected = Path.of("src/test/resources/OrderVer2-Authoritative.istardtx");
//
//        List<String> actualLines = Files.readAllLines(actual);
//        List<String> expectedLines = Files.readAllLines(expected);
//
//        assertEquals(expectedLines, actualLines, "Files do not match!");

	}



	@Disabled
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
