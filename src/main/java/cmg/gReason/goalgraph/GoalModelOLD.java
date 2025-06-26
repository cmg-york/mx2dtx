package cmg.gReason.goalgraph;

import java.util.ArrayList;
import java.util.Iterator;

import cmg.gReason.inputs.drawio.GraphElementOLD;


/**
 * 
 * A goal model object containing a tree of {@linkplain GMNode} objects.
 * @author Sotirios Liaskos
 *
 */
public class GoalModelOLD {
	ArrayList<GraphElementOLD> elements = new ArrayList<GraphElementOLD>();
	ArrayList<GMNode> goalModel = new ArrayList<GMNode>();

	private GMNode root;
	private GMNode qroot;
	
	
	/**
	 * Reads the list of {@linkplain GraphElement} objects (which are primarily relationships) and generates a goal model based on {@linkplain GMNode} objects.
	 * @throws Exception in a variety of situations in which the goal model is invalid.
	 */
	public void createGoalGraph() throws Exception {

		for (GraphElementOLD c:elements) {
			if (c.getCategory().equals("relationship")) {
				
				//Create the node
				GMNode gmn = new GMNode();
				GMNode gmnOrigin;
				GMNode gmnTarget;
				
				gmn.setId(c.getID());
				gmn.setLabel(c.getLabel());
				gmn.setType(c.getType());
				
				//Handle origin and destination
				String strOrigin = c.getOriginID();
				gmnOrigin = findNodeByID(strOrigin);
				if (gmnOrigin == null) {
					gmnOrigin = new GMNode();
					GraphElementOLD elOrigin = findElementByID(strOrigin);
					gmnOrigin.setId(elOrigin.getID());
					gmnOrigin.setLabel(elOrigin.getLabel());
					gmnOrigin.setType(elOrigin.getType());
					gmnOrigin.setEffectStatus(elOrigin.getStatus());
					gmnOrigin.setRuns(elOrigin.getRunNum());
					goalModel.add(gmnOrigin);
				}

				String strTarget = c.getTargetID();
				gmnTarget = findNodeByID(strTarget);
				if (gmnTarget == null) {
					gmnTarget = new GMNode();
					GraphElementOLD elTarget = findElementByID(strTarget);
					gmnTarget.setId(elTarget.getID());
					gmnTarget.setLabel(elTarget.getLabel());
					gmnTarget.setType(elTarget.getType());
					gmnTarget.setEffectStatus(elTarget.getStatus());
					gmnTarget.setRuns(elTarget.getRunNum());
					goalModel.add(gmnTarget);
				}
				
				//gmn, gmnOrigin, gmnTarget
				switch(gmn.getType()) {
				case "orDecomp":
					gmnTarget.addORChild(gmnOrigin);
					gmnOrigin.setParent(gmnTarget);
					break;
				case "andDecomp":
					gmnTarget.addANDChild(gmnOrigin);
					gmnOrigin.setParent(gmnTarget);
					break;
				case "precedenceLink":
					gmnOrigin.addOutPrecedence(gmnTarget);
					gmnTarget.addInPrecedence(gmnOrigin);
					break;
				case "negPrecedenceLink":
					gmnOrigin.addOutNegPrecedence(gmnTarget);
					gmnTarget.addInNegPrecedence(gmnOrigin);
					break;
				case "contributionLink":
					gmnOrigin.addOutContribution(new Contribution(gmnOrigin,gmnTarget,gmn.getLabel()));
					gmnTarget.addInContribution(new Contribution(gmnOrigin,gmnTarget,gmn.getLabel()));
					break;
				case "effectLink":
					gmnOrigin.setOutEffectLink(gmnTarget); //gmnTarget is an effect group
					gmnTarget.setInEffectLink(gmnOrigin); //gmnOrigin is a task
					break;
				case "effectGroupLink":
					gmnOrigin.addEffect(gmnTarget);
					//System.out.println("Adding effect group" + gmnTarget.getId() + " to " + gmnOrigin.getLabel());
					gmnTarget.setParent(gmnOrigin);
					gmnTarget.setInWeight(Float.parseFloat(gmn.getLabel()));
					break;
				}
			} else if (c.getType().equals("initialization")) {
				GMNode ini = new GMNode();
				ini.setLabel(c.getLabel());
				ini.setId(c.getID());
				ini.setType(c.getType());
				goalModel.add(ini);
			} else if (c.getType().equals("crossrun")) {
				GMNode  crs = new GMNode();
				crs.setLabel(c.getLabel());
				crs.setId(c.getID());
				crs.setType(c.getType());
				goalModel.add(crs);
			} else if (c.getType().equals("export")) {
				GMNode  exr = new GMNode();
				exr.setLabel(c.getLabel());
				exr.setId(c.getID());
				exr.setType(c.getType());
				goalModel.add(exr);
			} else if (c.getType().equals("quality")) {
				GMNode  qua = new GMNode();
				qua.setLabel(c.getLabel());
				qua.setId(c.getID());
				qua.setType(c.getType());
				qua.setDtxFormula(c.getDtxFormula());
				qua.setFormula(c.getFormula());
				qua.setQRoot(c.isQRoot());
				goalModel.add(qua);
			} else if (c.getType().equals("precondition")) {
				GMNode  cond = new GMNode();
				cond.setLabel(c.getLabel());
				cond.setId(c.getID());
				cond.setType(c.getType());
				cond.setDtxFormula(c.getDtxFormula());
				cond.setFormula(c.getFormula());
				goalModel.add(cond);

			}
		}//loop 
		
		//Bypass effectGroups
		byPassEffectGroups();
	
		try {
			findRoot();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	
	/**
	 * Removes the effect group element, such that, in the end, tasks are directly connected to the effects. 
	 */
	private void byPassEffectGroups() {
		Iterator<GMNode> itr = goalModel.iterator();
		
		while (itr.hasNext()) {
			GMNode n = itr.next();
			if (n.getType().equals("effectGroup")) {
				n.getInEffectLink().setEffects(n.getEffects());
				for (GMNode o:n.getEffects())
					o.setParent(n.getInEffectLink());
				itr.remove();
			}
		}
	}
	
	
	/**
	 * Find and set the roots of the goal decomposition and quality subgraphs.  
	 * @throws Exception if multiple roots are found.
	 */
	private void findRoot() throws Exception {
		ArrayList<String> roots = new ArrayList<String>();
		ArrayList<String> qRoots = new ArrayList<String>();
		
		for (GMNode n:goalModel) {
			if (n.getType().equals("goal") && (n.getParent()==null) ) {
				this.root = n;
				roots.add(n.getLabel());
			}
			if (n.getType().equals("quality") && n.isQRoot()) {
				System.out.println(n.getCamelLabel() + " is root");
				this.qroot = n;
				qRoots.add(n.getLabel());
			}
		}
		
		if (roots.size() > 1)
			throw new Exception("Error: Too many root goals, 1 expected, " + roots.size() + " found: " + roots);

		if (qRoots.size() > 1) {
			throw new Exception("Error: Too many quality root goals, 1 expected, " + qRoots.size() + " found: " + qRoots + ".");
		} else if (qRoots.size() == 0) {
			for (GMNode n:goalModel) {
				if (n.getType().equals("quality") && n.getOutgoingContributions().isEmpty()) {
					this.qroot = n;
					qRoots.add(n.getLabel());
				}
			}
			if (qRoots.size() >1)
				throw new Exception("Error: Too many quality root goals, 1 expected, " + qRoots.size() + " found: " + qRoots + ". Pick one manually but adding property root = true.");
		}
	
	}
	
	
	
	/**
	 * 
	 * 
	 * M I S C   G E T T E R S,   S E T T E R S,   A N D   H E L P E R S 
	 *  
	 * 
	 */
	
	
	public GMNode findNodeByID(String ID) {
		GMNode n = null;
		for (GMNode m:goalModel) {
			if (m.getId().equals(ID))
				n = m; 
		}
		return (n);
	}
	
	public GraphElementOLD findElementByID(String ID) {
		GraphElementOLD n = null;
		for (GraphElementOLD m:elements) {
			if (m.getID().equals(ID))
				n = m; 
		}
		return (n);
	}
	
	public void addElement(GraphElementOLD e) {
		elements.add(e);
	}
	
	public ArrayList<GMNode> getGoalModel() {
		return goalModel;
	}
	
	
	/**
	 * Returns the root of the qualities graph.
	 * @return The root of the qualities graph.
	 */
	public GMNode getQRoot() {
		return qroot;
	}

	/**
	 * Returns the root of the hardgoal graph.
	 * @return The root of the hardgoal graph.
	 */
	public GMNode getRoot() {
		return root;
	}


	/**
	 * 
	 * 
	 * D E B U G I N G 
	 *  
	 * 
	 */
	
	
	public void debugPrintGraphElementList() {
		for (GraphElementOLD c:elements) {
	        System.out.println("-----");
	        System.out.println("Object Id : " + c.getID());
	        System.out.println("Type : " + c.getType());
	        System.out.println("Label : " + c.getLabel());
	        System.out.println("Category : " + c.getCategory());
	        System.out.println("Origin : " + c.getOriginID());
	        System.out.println("Target : " + c.getTargetID());
	        System.out.println("Status : " + c.getStatus());
	        System.out.println();
		}
	}
	
	public void debugGoalModelFlat() {
		for (GMNode c:goalModel) {
			System.out.println(c.getLabel() + " (" + c.getType() + ") ID:" + c.getId());
			System.out.println(" --> AND Children: " + GMNode.debugPrintList(c.getANDChildren(),","));
			System.out.println(" --> OR Children: " + GMNode.debugPrintList(c.getORChildren(),","));
			System.out.println(" --> Effects: " + GMNode.debugPrintList(c.getEffects(),","));
			System.out.println(" --> InPrecedences: " + GMNode.debugPrintList(c.getIncompingPrecedences(),","));
			System.out.println(" --> OutPrecedences: " + GMNode.debugPrintList(c.getOutgoingPrecedences(),","));
			System.out.println(" --> InContributions: " + GMNode.debugPrintList(c.getIncompingContributions(),","));
			System.out.println(" --> OutContributions: " + GMNode.debugPrintList(c.getOutgoingContributions(),","));
			System.out.println(" --> InWeight: " + Float.toString(c.getInWeight()));
			System.out.println(" --> Status: " + c.getEffectStatus());
			if (c.getParent()!= null)
				System.out.println(" --> Parent: " + c.getParent().getLabel());
			else 
				System.out.println(" --> Parent: none");
		}
	}
	
}
