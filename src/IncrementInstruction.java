import java.util.Map;

public class IncrementInstruction implements Instruction {
    private final String id;
    private final Map<String, Object> variables;
    private final int delta;

    public IncrementInstruction(String id, Map<String, Object> variables, int delta) {
        this.id = id;
        this.variables = variables;
        this.delta = delta;
    }

    @Override
    public void run() {
        Object variable = variables.get(id);
        if (variable == null) variable = Complex.fromReal(0);
        
        if (!(variable instanceof Complex))
            throw new IncorrectExpressionTypeException(variable, ExpressionType.NUMBER);

        Complex current = (Complex) variable;
        variables.put(id, current.add(Complex.fromReal(delta)));
    }
}