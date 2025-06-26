package cmg.gReason.outputs.istardtx;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import cmg.gReason.goalgraph.GoalModel;
import cmg.gReason.inputs.drawio.DrawIOReader;
import cmg.gReason.outputs.common.Translator;

class EndToEndDebugTest {

	@Test
	void test() {
		System.out.println("THIS IS DA TEST");
		mx2dtx mainClass = new mx2dtx();
		DrawIOReader reader = new DrawIOReader();
		Translator writer = new dtxTranslator();
		GoalModel model = new GoalModel();

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
	}

}
