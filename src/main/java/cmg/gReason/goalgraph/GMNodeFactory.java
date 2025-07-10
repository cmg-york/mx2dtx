package cmg.gReason.goalgraph;

import cmg.gReason.inputs.drawio.graphelementstructure.CrossRunSet;
import cmg.gReason.inputs.drawio.graphelementstructure.Effect;
import cmg.gReason.inputs.drawio.graphelementstructure.EffectGroup;
import cmg.gReason.inputs.drawio.graphelementstructure.ElementWithFormula;
import cmg.gReason.inputs.drawio.graphelementstructure.ExportedSet;
import cmg.gReason.inputs.drawio.graphelementstructure.Goal;
import cmg.gReason.inputs.drawio.graphelementstructure.GraphElement;
import cmg.gReason.inputs.drawio.graphelementstructure.InitializationSet;
import cmg.gReason.inputs.drawio.graphelementstructure.Link;
import cmg.gReason.inputs.drawio.graphelementstructure.Precondition;
import cmg.gReason.inputs.drawio.graphelementstructure.Quality;
import cmg.gReason.inputs.drawio.graphelementstructure.Task;
import cmg.gReason.outputs.common.ErrorReporter;


/**
 * Translates a {@linkplain GraphElement} into a {@linkplain GMNode}
 */
public class GMNodeFactory {
	private ErrorReporter err;
	private IdentifierRegistry identifiers; 
	private ConditionExpressionParser parser;
	
	public GMNodeFactory(ErrorReporter e,
			IdentifierRegistry i,
			ConditionExpressionParser p) {
		this.err = e;
		this.identifiers = i;
		this.parser = p;
		
	}
	
	
	/**
	 * The main translator of a {@linkplain GraphElement} object into a {@linkplain GMNode} object
	 * @param e The {@linkplain GraphElement} object. 
	 * @return The {@linkplain GMNode} object.
	 */
	public GMNode createGMNodeFromGraphElement(GraphElement e) {
		GMNode gNode;
		
		// A first pass
		if (e instanceof Goal) {
			gNode = new GMGoal(); 
			((GMGoal) gNode).setRuns(((Goal) e).getRunNum());
			((GMGoal) gNode).setRoot(((Goal) e).isRoot());
		} else if (e instanceof Task) {
			gNode = new GMTask();
		} else if (e instanceof Quality) {
			gNode = new GMQuality();
			((GMQuality) gNode).setQRoot(((Quality) e).isQRoot());
		} else if (e instanceof EffectGroup) {
			gNode = new GMEffectGroup();
		} else if (e instanceof Effect) {
			gNode = new GMEffect();
			((GMEffect) gNode).setEffectStatus(((Effect) e).getStatus());
			//This is a clean camel label
			gNode.setLabel(e.getLabel());
			//In both statements below setTurnsX will parse the string into 
			//arraylists or hashmaps.
			//If empty, getTruePredicates uses the label.
			((GMEffect) gNode).setTurnsTrue(((Effect) e).getTurnsTrue());
			((GMEffect) gNode).setTurnsFalse(((Effect) e).getTurnsFalse());
			identifiers.addEffectPredicates(((GMEffect) gNode).getTruePredicates());
			identifiers.addEffectPredicates(((GMEffect) gNode).getFalsePredicates());
			identifiers.addEffectVariables(((GMEffect) gNode).getVariables());

		} else if (e instanceof Precondition) {
			gNode = new GMPrecondition();
		} else if (e instanceof InitializationSet) {
			gNode = new GMInitializationSet();
		} else if (e instanceof ExportedSet) {
			gNode = new GMExportedSet();
		} else if (e instanceof CrossRunSet) {
			gNode = new GMCrossRunSet();
		} else if (e instanceof Link) {
			switch (((Link) e).getType()) {
			case "orDecomp":
				gNode = new ORDecomp();
				break;
			case "andDecomp":
				gNode = new ANDDecomp();
				break;
			case "precedenceLink":
				gNode = new PRELink();
				break;
			case "negPrecedenceLink":
				gNode = new NPRLink();
				break;
			case "contributionLink":
				gNode = new Contribution();
				break;
			case "effectLink":
				gNode = new EffLink();
				break;
			case "effectGroupLink":
				gNode = new EffGroupLink();
				break;
			default:
				gNode = null;
				err.addError("Unrecognized GraphElement relationship type: " + e.getClass().getSimpleName() + "," + ((Link) e).getType(), "GoalModel::GMNodeFactory");
				break;
			}

		} else {
			gNode = null;
			err.addError("Unrecognized GraphElement type: " + e.getClass().getSimpleName(), "GoalModel::GMNodeFactory");
		}
		
		
		//PASS 2: Objects have been created. Additional considerations
		
		gNode.setId(e.getID());
		gNode.setLabel(e.getLabel());//NODE: labels have been cleaned an camelized.
		gNode.setDescription(e.getDescription());
		
		//Quality, Precondition, Unlinked boxes
		if (e instanceof ElementWithFormula) {
			((WithFormula) gNode).setFormula(((ElementWithFormula) e).getFormula());
			((WithFormula) gNode).setDtxFormula(((ElementWithFormula) e).getDtxFormula());
		}
		
		if (e instanceof Precondition) {
			String preFormula;
			if (((WithFormula) gNode).hasFormula()) {
				//It is a named formula
				preFormula = ((WithFormula) gNode).getFormula();
				//Note: the label below has been cleaned and camelized at reading
				identifiers.addIdentifier(e.getLabel(), "preconditionID");
				identifiers.addEffectPredicate(((WithFormula) gNode).getLabel());
			} else {
				//The label is the formula
				preFormula = ((WithFormula) gNode).getLabel();
			}
			((GMPrecondition) gNode).setDtxFormula(parser.parse(preFormula));
		}
		
		if (e instanceof Effect) {
			if (!( (GMEffect) gNode).useLabel()) {
				err.addWarning("Effect labeled '" + gNode.getDescription() + "' has non-empty custom properties. "
						+ "Visible label '" + gNode.getDescription() + "' cannot be used as identifier. Refer to the identifiers in turnsTrue/turnsFalse instead.", "GoalModel::GMNodeFactory");
			}
		}
		
		return(gNode);
	}

}
