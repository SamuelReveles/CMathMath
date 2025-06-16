import java.util.Map;

public class IncrementInstruction implements Instruction {
    private final String id;
    private final Map<String, Complex> variables;
    private final int delta;

    public IncrementInstruction(String id, Map<String, Complex> variables, int delta) {
        this.id = id;
        this.variables = variables;
        this.delta = delta;
    }

    @Override
    public void run() {
        Complex current = variables.get(id);
        if (current == null) current = Complex.fromReal(0);
        variables.put(id, current.add(Complex.fromReal(delta)));
    }

    public void runWithVars(Map<String, Complex> vars) {
        Complex current = vars.get(id);
        if (current == null) current = Complex.fromReal(0);
        vars.put(id, current.add(Complex.fromReal(delta)));
    }
}