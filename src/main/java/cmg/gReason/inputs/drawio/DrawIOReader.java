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
import cmg.gReason.outputs.istardtx.Translator;

public class DrawIOReader {

	private GoalModel m = new GoalModel();
	private Translator t;

	private String inFile = "";
	private String outFile = "";
	private boolean replace;

	
	
	
	/**
	 * The root call. Handles all translation and saving to a file.
	 */
	public void translateToIstarDTX() {

		readXML();

		System.out.println("Creating goal graph...");
		try {
			m.createGoalGraph();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		System.out.println("Translating...");
		
		
		t = new Translator(m);
		t.translate();
		
		System.out.println("Outputing...");
		if (outFile.equals("")) {
			System.out.println(t.getSpec());
		} else {
			saveOutput(outFile);
		}
	}
	
	
	


	/**
	 * Reads the XML file from draw.io and creates a list of elements within a
	 * {@link GoalModel} object.
	 */
	private void readXML() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		GraphElementFactory cf = new GraphElementFactory();

		try {
			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = (Document) db.parse(new File(inFile));

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
	}



	public void saveOutput(String path) {
		// Scanner kb = new Scanner(System.in);
		Writer fW;
		try {
			File myObj = new File(path);
			if (myObj.createNewFile()) {
				fW = new FileWriter(path, false);
				writeSpecToFile(fW);
				this.replace = false;
			} else {
				// System.out.println("Output file already exists. Replace? (y/n)");
				// char c = kb.next().charAt(0);
				// if ((c == 'y') || (c == 'Y')) {
				this.replace = true;
				fW = new FileWriter(path, false);
				writeSpecToFile(fW);
				// } else if ((c == 'n') || (c == 'N')) {
				// System.out.println("Process interrupted. No changes made.");
				// } else {
				// System.out.println("Unknown output. No changes made.");
				// }
				// kb.close();
			}
		} catch (IOException e) {
			System.err.println("Error creating output file.");
			e.printStackTrace();
		}
	}

	private void writeSpecToFile(Writer fW) {
		try {
			fW.write(t.getSpec());
			fW.close();
			if (this.replace)
				System.out.println("Spec file updated: " + this.outFile);
			else
				System.out.println("Spec file created: " + this.outFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void setInFile(String inFile) throws Exception {
		File f = new File(inFile);
		if (!f.exists() || f.isDirectory())
			throw new Exception("Input file not found:" + inFile);
		this.inFile = inFile;
	}

	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}

	
}
