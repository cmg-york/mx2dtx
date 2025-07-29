package ca.yorku.cmg.istardt.reader.inputs.drawio;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ca.yorku.cmg.istardt.reader.goalgraph.GoalModel;
import ca.yorku.cmg.istardt.reader.inputs.drawio.graphelementstructure.GraphElement;
import ca.yorku.cmg.istardt.reader.outputs.common.ErrorReporter;

public class DrawIOReader {

	private String inFile = "";
	private ErrorReporter err = null;

	
	public DrawIOReader(ErrorReporter err) {
		this.err = err;
	}
	
	
	public GoalModel readXML() {
		GoalModel g = readFile();
		if (err.hasErrors()) {
			err.printAll();
			System.exit(-1);
		}
		g = generateGoalGraph(g);
		if (err.hasErrors()) {
			err.printAll();
			System.exit(-1);
		}
		err.printAll();
		return(g);
	}


	/**
	 * Reads the XML file from draw.io and creates a list of elements within a
	 * {@link GoalModel} object.
	 */
	public GoalModel readFile() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		GraphElementFactory cf = new GraphElementFactory(err);
		GoalModel m = null;
		
		try {
			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = (Document) db.parse(new File(inFile));
			m = new GoalModel(err);
			
			System.out.println("Reading mxGraph spec...");

			NodeList list = doc.getElementsByTagName("object");
			for (int temp = 0; temp < list.getLength(); temp++) {
				Node node = list.item(temp);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					GraphElement c;
					c = cf.constructElement(element);
					if (c!=null) {
						m.addElement(c);	
					}
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		if (err.hasErrors()) {
			err.printAll();
			System.exit(0);
		}
		return(m);
	}
	
	public GoalModel generateGoalGraph(GoalModel m) {
		System.out.println("Creating and validating model...");
		try {
			m.createGoalGraph();
		} catch (Exception e) {
			e.printStackTrace();
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
		else {
			this.inFile = inFile;
			System.out.println("Opening file for translation: " + inFile);
		}
	}

}
