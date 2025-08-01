package ca.yorku.cmg.istardt.reader.inputs.drawio;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.Disabled;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.yorku.cmg.istardt.reader.goalgraph.GoalModel;
import ca.yorku.cmg.istardt.reader.inputs.drawio.GraphElementFactory;
import ca.yorku.cmg.istardt.reader.inputs.drawio.graphelementstructure.GraphElement;
import ca.yorku.cmg.istardt.reader.outputs.common.ErrorReporter;

class GraphElementFactoryNewTest {

	@Disabled
	void test() {
		ErrorReporter err = new ErrorReporter();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		GraphElementFactory cf = new GraphElementFactory(err);
		GoalModel m = null;
		
		try {
			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			String inFile = "src/test/resources/Order.drawio"; 
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = (Document) db.parse(new File(inFile));
			m = new GoalModel(err);
			
			System.out.println("Reading XML file...");

			NodeList list = doc.getElementsByTagName("object");
			for (int temp = 0; temp < list.getLength(); temp++) {
				Node node = list.item(temp);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					GraphElement c;
					try {
						c = cf.constructElement(element);
						m.addElement(c);
					} catch (Exception e) {
						System.err.println(e.getMessage());
					}
				}
			}
			m.debugPrintGraphElementList();
			m.createGoalGraph();
			m.debugGoalModelFlat();
			
			if (err.hasAnything()) {
				err.printAll();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
