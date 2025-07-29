package ca.yorku.cmg.istardt.reader.outputs.common;

import java.util.ArrayList;
import java.util.List;

public class ErrorReporter {
    private final List<Diagnostic> diagnostics = new ArrayList<>();
    private final List<Diagnostic> infos = new ArrayList<>();
    
    public void addError(String message, String source) {
        diagnostics.add(new Diagnostic(Diagnostic.Kind.ERROR, message, source));
    }

    public void addInfo(String message, String source) {
        infos.add(new Diagnostic(Diagnostic.Kind.INFO, message, source));
    }

    public void addWarning(String message, String source) {
        diagnostics.add(new Diagnostic(Diagnostic.Kind.WARNING, message, source));
    }

    public boolean hasErrors() {
        return diagnostics.stream().anyMatch(d -> d.getKind() == Diagnostic.Kind.ERROR);
    }

    public boolean hasWarnings() {
        return diagnostics.stream().anyMatch(d -> d.getKind() == Diagnostic.Kind.WARNING);
    }

    public boolean hasInfo() {
        return infos.stream().anyMatch(d -> d.getKind() == Diagnostic.Kind.INFO);
    }
    
    public void printAll() {
    	infos.forEach(System.out::println);
    	diagnostics.forEach(System.err::println);
    	infos.clear();
    	diagnostics.clear();
    }

	public boolean hasAnything() {
		return (hasErrors() || hasWarnings() || hasInfo());
	}

}
