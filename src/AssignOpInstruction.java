import java.util.Map;

public class AssignOpInstruction implements Instruction {
    private final String id;
    private final Expression expr;
    private final Map<String, Object> variables;
    private final String op;

    public AssignOpInstruction(String id, Expression expr, Map<String, Object> variables, String op) {
        this.id = id;
        this.expr = expr;
        this.variables = variables;
        this.op = op;
    }

    @Override
    public void run() {
        Object variable = variables.get(id);
        Object valueObj = expr.evaluate();

        if (variable == null) variable = Complex.fromReal(0);

        if (!(variable instanceof Complex))
            throw new IncorrectExpressionTypeException(variable, ExpressionType.NUMBER);
        if (!(valueObj instanceof Complex))
            throw new IncorrectExpressionTypeException(valueObj, ExpressionType.NUMBER);
        
        Complex current = (Complex) variable;
        Complex value = (Complex) valueObj;

        switch (op) {
            case "+": variables.put(id, current.add(value)); break;
            case "-": variables.put(id, current.substract(value)); break;
            case "*": variables.put(id, current.multiply(value)); break;
            case "/": variables.put(id, current.divide(value)); break;
            case "^": variables.put(id, current.pow(value)); break;
        }
    }
}