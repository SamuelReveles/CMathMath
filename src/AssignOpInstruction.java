import java.util.Map;

public class AssignOpInstruction implements Instruction {
    private final String id;
    private final ComplexExpression expr;
    private final Map<String, Complex> variables;
    private final String op;

    public AssignOpInstruction(String id, ComplexExpression expr, Map<String, Complex> variables, String op) {
        this.id = id;
        this.expr = expr;
        this.variables = variables;
        this.op = op;
    }

    @Override
    public void run() {
        Complex current = variables.get(id);
        Complex value = expr.evaluate();
        if (current == null) current = Complex.fromReal(0);
        switch (op) {
            case "+": variables.put(id, current.add(value)); break;
            case "-": variables.put(id, current.substract(value)); break;
            case "*": variables.put(id, current.multiply(value)); break;
            case "/": variables.put(id, current.divide(value)); break;
            case "^": variables.put(id, current.pow(value)); break;
        }
    }

    public void runWithVars(Map<String, Complex> vars) {
        Complex current = vars.get(id);
        Complex value = expr.evaluate();
        if (current == null) current = Complex.fromReal(0);
        switch (op) {
            case "+": vars.put(id, current.add(value)); break;
            case "-": vars.put(id, current.substract(value)); break;
            case "*": vars.put(id, current.multiply(value)); break;
            case "/": vars.put(id, current.divide(value)); break;
            case "^": vars.put(id, current.pow(value)); break;
        }
    }
}