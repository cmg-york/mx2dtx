package ca.yorku.cmg.istardt.reader.outputs.istardtx;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.io.TempDir;

import ca.yorku.cmg.istardt.reader.goalgraph.GoalModel;
import ca.yorku.cmg.istardt.reader.inputs.drawio.DrawIOReader;
import ca.yorku.cmg.istardt.reader.outputs.common.ErrorReporter;
import ca.yorku.cmg.istardt.reader.outputs.common.Translator;
import ca.yorku.cmg.istardt.reader.outputs.istardtx.dtxTranslator;

class EndToEndDebugTest {

    @BeforeEach
    void setUp() {
        // Code here runs before each @Test
    	System.out.println("\n\n == mx2dtx new test ==");
    }

	@ParameterizedTest(name = "{0}")
	@MethodSource("discoverCases")
	void translates_drawio_to_expected_dtx_auto(String base, @TempDir Path tmp) throws Exception {
		Path resourceDir = Paths.get(EndToEndDebugTest.class.getResource("/").toURI());

		Path input = resourceDir.resolve(base + ".drawio");
		Path expected = resourceDir.resolve(base + "-Auth.istardtx");
		Path actual = tmp.resolve(base + ".istardtx");

		translate(input.toString(), actual.toString());
		assertLinesMatch(Files.readAllLines(expected), Files.readAllLines(actual));
	}


	private static void translate(String inFile, String outFile) throws Exception {
		ErrorReporter err = new ErrorReporter();
		DrawIOReader reader = new DrawIOReader(err);
		GoalModel model = new GoalModel(err);
		Translator writer = new dtxTranslator();

		// Read from the XML
		reader.setInFile(inFile);
		model = reader.readXML();

		// Produce the translation
		writer.setOutFile(outFile);
		writer.setModel(model);
		writer.translate(false);
	}

	static List<String> discoverCases() throws IOException, URISyntaxException {
		Path resourceDir = Paths.get(EndToEndDebugTest.class.getResource("/").toURI());

		// list all files in resource
		try (Stream<Path> files = Files.list(resourceDir)) {
			return files
					// keep .drawio files
					.filter(file -> file.getFileName().toString().endsWith(".drawio"))
					// strip off the .drawio extension to get base name
					.map(file -> {
						String filename = file.getFileName().toString();
						return filename.substring(0, filename.length() - ".drawio".length());
					})
					// keep files with a matching -Auth.istardtx
					.filter(base -> Files.exists(resourceDir.resolve(base + "-Auth.istardtx")))
					// sort alphabetically
					.sorted()
					.toList();
		}
	}

	/*
	 * Additional tests - under construction
	 */


	@Disabled
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

	}



	@Disabled
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
