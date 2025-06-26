package cmg.gReason.outputs.common;

public class Diagnostic {
    public enum Kind { ERROR, WARNING, INFO }

    private final Kind kind;
    private final String message;
    private final String source;


    public Diagnostic(Kind kind, String message, String source) {
        this.kind = kind;
        this.message = message;
        this.source = source;
    }

    public Kind getKind() { return kind; }

    @Override
    public String toString() {
    	String msgStr;
    	if (kind == Kind.ERROR) {
    		msgStr = ColorPrinter.paintRed("[" + kind.toString() + "   | " + source + "]: ");
    	} else if (kind == Kind.WARNING) {
    		msgStr = ColorPrinter.paintYellow("[" + kind.toString() + " | " + source + "]: ");
    	} else {
    		msgStr = ColorPrinter.paintBlue("[" + kind.toString() + "    | " + source + "]: ");
    	}
        return  msgStr + message;
    }
}
