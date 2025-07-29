package cmg.gReason.goalgraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import cmg.gReason.inputs.drawio.ExportedSetParser;
import cmg.gReason.inputs.drawio.InitializationParser;
import cmg.gReason.outputs.common.ErrorReporter;

public class Validator {
	IdentifierRegistry identifiers;
	ErrorReporter err;
	GoalModel goalModel;
	
	
	public Validator(IdentifierRegistry identifiers, ErrorReporter err, GoalModel model) {
		super();
		this.identifiers = identifiers;
		this.err = err;
		this.goalModel = model;
	}

	public boolean isStrictlyNumeric(String s) {
	    return s != null && s.matches("-?\\d+(\\.\\d+)?");
	}

	public void predicateValidation() {
		
		//Predicate/variable appears in a precondition but in no effect. (Error)
		identifiers.validate();
		
		//Initializations, Exported Sets, Cross Must Mention existing predicates.
		GMInitializationSet init = null;
		GMExportedSet export = null;
		GMCrossRunSet cross = null;
		
		for (GMNode g : this.goalModel.getGoalModel()) {
			if (g instanceof GMInitializationSet) {
				init = (GMInitializationSet) g;
			}

			if (g instanceof GMCrossRunSet) {
				cross = (GMCrossRunSet) g;
			}
			
			if (g instanceof GMExportedSet) {
				export = (GMExportedSet) g;
			}
		}
		
		if (init != null) {
			InitializationParser p = new InitializationParser();
			p.parse(init.getLabel());

			List<String> allowedInits = Arrays.asList("predicateID");
			for (String prop : p.getPropositions()) {
				if (!(identifiers.contains(prop))) {
					err.addError("Unknown predicate in initialization '" + prop + "'", "GoalModel::predicateValidation()");
				} else if (!allowedInits.contains(identifiers.getIdentifierTypeBreak(prop))) {
					err.addError("Initialization identifier '" + prop + "' of invalid type. Types allowed: predicates.", "GoalModel::predicateValidation()");
				}
			}

			allowedInits = Arrays.asList("variableID","qualID");
			for (Entry<String, String> entry : p.getVariables().entrySet()) {
				if (!identifiers.contains(entry.getKey())) {
					err.addError("Unknown variable or quality in initialization '" + entry.getKey() + "'", "GoalModel::predicateValidation()");
				} else {
					if (!allowedInits.contains(identifiers.getIdentifierTypeBreak(entry.getKey()))) {
						err.addError("Initialization identifier '" + entry.getKey() + "' of invalid type. Types allowed: variables, qualities.", "GoalModel::predicateValidation()");
					}
					if (!(isStrictlyNumeric(entry.getValue()))) {
						err.addError("Continuous initialization identifier '" + entry.getKey() + "' has illegal value type '" + entry.getValue() + "'. Numeric expected.", "GoalModel::generalValidation()");					
					}
				}
			}
			
		} else {
			err.addWarning("No initialization element added.", "GoalModel::predicateValidation()");
		}
		
		if (cross != null) {
			ArrayList<String> crossList = new ArrayList<>(Arrays.asList(cross.getLabel().split("\\s*,\\s*")));
			List<String> allowedCrosses = Arrays.asList("predicateID","variableID","qualID","conditionID");
			for (String prop : crossList) {
				if (!(identifiers.contains(prop))) {
					err.addError("Unknown element in cross run set '" + prop + "'", "GoalModel::predicateValidation()");
				} else if (!allowedCrosses.contains(identifiers.getIdentifierTypeBreak(prop))) {
					err.addError("Cross run element '" + prop + "' is of invalid type. Allowed types: predicate, variable, quality", "GoalModel::generalValidation()");
				}
			}

		} else {
			err.addWarning("No cross run set has been added.", "GoalModel::predicateValidation()");
		}
		
		
		if (export != null) {
			ExportedSetParser p = new ExportedSetParser();
			p.parseExportedSetLabel(export.getLabel());
			//identifier must be: predicate, goal, task, variable, quality
			List<String> allowedPropExports = Arrays.asList("predicateID","taskID","goalID","conditionID");
			List<String> allowedRelExports = Arrays.asList("variableID","qualID");

			for (String prop : p.getPropositions()) {
				if (!(identifiers.contains(prop))) {
					err.addError("Unknown propositional element in exported set '" + prop + "'. Continuous elements must be of the form 'identifier(minVal,maxVal)'", "GoalModel::generalValidation()");
				} else if (!allowedPropExports.contains(identifiers.getIdentifierTypeBreak(prop))) {
					err.addError("Propositional element in exported set '" + prop + "' - invalid type. Allowed: predicate, task, goal. Continuous elements must be of the form 'identifier(minVal,maxVal)'", "GoalModel::generalValidation()");
				}
			}
			
			for (Entry<String, List<String>> entry : p.getRelations().entrySet()) {
				if (!(identifiers.contains(entry.getKey()))) {
					err.addError("Unknown relational element in exported set '" + entry.getKey() + entry.getValue() + "'", "GoalModel::generalValidation()");
				} else {
					if (!allowedRelExports.contains(identifiers.getIdentifierTypeBreak(entry.getKey()))) {
						err.addError("Relational element in exported set '" + entry.getKey() + entry.getValue() + "' - invalid type. Allowed: quality, variable.", "GoalModel::generalValidation()");
					}
					if (entry.getValue().size()!=2) {
						err.addError("Relational element in  exported set '" + entry.getKey() + entry.getValue() + "': invalid number of parameters. Must be 2 as in identifier(minVal,maxVal)", "GoalModel::generalValidation()");				
					} else {
						if (!isStrictlyNumeric(entry.getValue().get(0))) {
							err.addError("Relational element in  exported set '" + entry.getKey() + entry.getValue() + "': invalid parameter type '" +  entry.getValue().get(0) + "'", "GoalModel::generalValidation()");				
						}
						if (!isStrictlyNumeric(entry.getValue().get(1))) {
							err.addError("Relational element in  exported set '" + entry.getKey() + entry.getValue() + "': invalid parameter type '" +  entry.getValue().get(1) + "'", "GoalModel::generalValidation()");				
						}
					}
				}
			}
		} else {
			err.addWarning("No exported set has been added.", "GoalModel::generalValidation()");
		}
	}
	
	
	public void generalValidation() {
		
		Set<String> seen = new HashSet<>();
		Set<String> duplicates = new HashSet<>();
		boolean hasInitialization = false;
		boolean hasExportedSet = false;
		boolean hasCrossRun = false;
		boolean rootIsMultiRun = false;
		
		for (GMNode node : goalModel.getGoalModel()) {
		    String label = node.getLabel();
		    
		    boolean allowDuplicates = (node instanceof GMPrecondition) || (node instanceof GMInitializationSet)
		    		|| (node instanceof GMCrossRunSet) || (node instanceof GMExportedSet);
		    
		    if (!seen.add(label)) {
		    	if (!allowDuplicates) {
		    		duplicates.add(label);  // Note: Set will ignore repeated duplicates
		    	}
		    }

		    if (node instanceof GMGoal) {
		    	if (((GMGoal) node).andChildren.isEmpty() && ((GMGoal) node).orChildren.isEmpty()) {
		    		err.addError("Goal '" + node.getLabel() + "' has no children.", "GoalModel::generalValidation()");
		    	}
		    } else if (node instanceof GMTask) {
		    	if (((GMTask) node).effects.isEmpty()) {
		    		err.addError("Task '" + node.getLabel() + "' has no effects.", "GoalModel::generalValidation()");
		    	}
		    	
		    	if (((GMTask) node).getParent() == null) {
		    		err.addError("Task '" + node.getLabel() + "' has no parent.", "GoalModel::generalValidation()");
		    	}
		    } else if (node instanceof GMQuality) {
		    	if (!((GMQuality) node).hasDtxFormula() && !((GMQuality) node).hasFormula() && ((GMQuality) node).inContr.isEmpty()) {
		    		err.addError("Quality '" + node.getLabel() + "' has no formula nor incoming contribution links.", "GoalModel::generalValidation()");
		    	}
		    } else if (node instanceof GMPrecondition) {
		    	if (((GMPrecondition) node).outPre.isEmpty() && ((GMPrecondition) node).outNegPre.isEmpty()) {
		    		err.addWarning("Precondition with label '" + (node.getLabel().length() > 50 ? node.getLabel().substring(0, 50) : node.getLabel()) + "' has no outgoing pre or npr link.", "GoalModel::generalValidation()");
		    	}

		    } else if (node instanceof GMInitializationSet) {
		    	hasInitialization = true;
		    } else if (node instanceof GMCrossRunSet) {
		    	hasCrossRun = true;
		    } else if (node instanceof GMExportedSet) {
		    	hasExportedSet = true;
		    }
		}

		if (!duplicates.isEmpty()) {
			err.addWarning("Duplicate labels: " + duplicates, "GoalModel::generalValidation()");
		}
		
		if (!((GMGoal) this.goalModel.getRoot()).getRuns().matches("[1-9][0-9]*")) {
			err.addError("Root goal '" + this.goalModel.getRoot().getLabel() + "'. Number of runs must be a positive natural number (found" + ((GMGoal) this.goalModel.getRoot()).getRuns() + ").", "GoalModel::generalValidation()");
		} else {
			int rns = Integer.parseInt(((GMGoal) this.goalModel.getRoot()).getRuns());
			if (rns > 1) {
				rootIsMultiRun = true;
			}
		}
		
		if (!hasInitialization) {
			err.addWarning("Initialization element missing.", "GoalModel::generalValidation()");
		}
		if (!hasExportedSet) {
			err.addWarning("Exported set missing.", "GoalModel::generalValidation()");
		}
		
		if (!hasCrossRun && rootIsMultiRun) {
			err.addWarning("CrossRun set is missing for multi-run root.", "GoalModel::generalValidation()");		
		}

	}
	
	public void validateRelationship(GMNode origin, GMNode target, GMNode relationship) {
		List<Class<?>> allowedTypes;
		if ((relationship instanceof PRELink) || (relationship instanceof NPRLink)) {
	        allowedTypes = List.of(GMPrecondition.class, GMGoal.class, GMTask.class);
	        if (!allowedTypes.stream().anyMatch(c -> c.isInstance(origin))) {
	        	err.addError("Element '" + origin.getLabel() + "' cannot be origin of PRE or NPR link. Allowed types: Precondition, Goal, Task.", "GoalModel::validateRelationship(GMNode,GMNode,GMNode)");
	        }
	        allowedTypes = List.of(GMGoal.class, GMTask.class, GMEffect.class);
	        if (!allowedTypes.stream().anyMatch(c -> c.isInstance(target))) {
	        	err.addError("Element '" + target.getLabel() + "' cannot be targeted of PRE or NPR link. Allowed types: Goal, Task, Effect.", "GoalModel::validateRelationship(GMNode,GMNode,GMNode)");
	        }
		}

		if ((relationship instanceof Contribution)) {
	        allowedTypes = List.of(GMPrecondition.class, GMGoal.class, GMTask.class, GMEffect.class, GMQuality.class);
	        if (!allowedTypes.stream().anyMatch(c -> c.isInstance(origin))) {
	        	err.addError("Element '" + origin.getLabel() + "' cannot be origin of a contribution link. Allowed types: Precondition, Goal, Task, Effect, Quality.", "GoalModel::validateRelationship(GMNode,GMNode,GMNode)");
	        }
	        allowedTypes = List.of(GMQuality.class);
	        if (!allowedTypes.stream().anyMatch(c -> c.isInstance(target))) {
	        	err.addError("Element '" + target.getLabel() + "' cannot be targeted of a contribution link. Allowed types: Quality", "GoalModel::validateRelationship(GMNode,GMNode,GMNode)");
	        }
		}

		if ((relationship instanceof ANDDecomp) || (relationship instanceof ORDecomp)) {
		        allowedTypes = List.of(GMGoal.class, GMTask.class);
	        if (!allowedTypes.stream().anyMatch(c -> c.isInstance(origin))) {
	        	err.addError("Element '" + origin.getLabel() + "' cannot be origin of a refinement link. Allowed types: Goal, Task", "GoalModel::validateRelationship(GMNode,GMNode,GMNode)");
	        }
	        allowedTypes = List.of(GMGoal.class);
	        if (!allowedTypes.stream().anyMatch(c -> c.isInstance(target))) {
	        	err.addError("Element '" + target.getLabel() + "' cannot be targeted of a refinement link. Allowed types: Goal", "GoalModel::validateRelationship(GMNode,GMNode,GMNode)");
	        }
		}

		if ((relationship instanceof EffGroupLink)) {
			allowedTypes = List.of(GMEffectGroup.class);
			if (!allowedTypes.stream().anyMatch(c -> c.isInstance(origin))) {
				err.addError("Element '" + origin.getLabel() + "' cannot be the origin of an effect link. Allowed types: Effect Group (disk)", "GoalModel::validateRelationship(GMNode,GMNode,GMNode)");
			}
			allowedTypes = List.of(GMEffect.class);
			if (!allowedTypes.stream().anyMatch(c -> c.isInstance(target))) {
				err.addError("Element '" + target.getLabel() + "' cannot be the target of an effect link. Allowed types: Effect", "GoalModel::validateRelationship(GMNode,GMNode,GMNode)");
			}
		}
		
		if ((relationship instanceof EffLink)) {
			allowedTypes = List.of(GMTask.class);
			if (!allowedTypes.stream().anyMatch(c -> c.isInstance(origin))) {
				err.addError("Element '" + origin.getLabel() + "' cannot be the origin of an effect group link. Allowed types: Task.", "GoalModel::validateRelationship(GMNode,GMNode,GMNode)");
			}
			allowedTypes = List.of(GMEffectGroup.class);
			if (!allowedTypes.stream().anyMatch(c -> c.isInstance(target))) {
				err.addError("Element '" + target.getLabel() + "' cannot be the target of an effect group link. Allowed types: Effect Group (disk)", "GoalModel::validateRelationship(GMNode,GMNode,GMNode)");
			}
		}
		
	}
}
