package cmg.gReason.inputs.drawio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cmg.gReason.goalgraph.GoalModel;
import cmg.gReason.outputs.istardtx.dtxTranslator;

/**
 * 
 */
public class DrawIOReader {

	private String inFile = "";


	/**
	 * Reads the XML file from draw.io and creates a list of elements within a
	 * {@link GoalModel} object.
	 */
	public GoalModel readXML() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		GraphElementFactory cf = new GraphElementFactory();
		GoalModel m = null;
		
		try {
			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = (Document) db.parse(new File(inFile));
			m = new GoalModel();
			
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
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Creating goal graph...");
		try {
			m.createGoalGraph();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return(m);
	}


	

	/**
	 * 
	 * FILE MANAGMENT 
	 * 
	 */
	

	
	public void setInFile(String inFile) throws Exception {
		File f = new File(inFile);
		if (!f.exists() || f.isDirectory())
			throw new Exception("Input file not found:" + inFile);
		this.inFile = inFile;
	}



	
}
