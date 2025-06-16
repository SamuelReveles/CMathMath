import java.util.Map;

public class DeclareInstruction implements Instruction {
  public DeclareInstruction(String id, ComplexExpression expression, Map<String, Complex> variables) {
    this.id = id;
    this.expression = expression;
    this.variables = variables;
  }

  @Override
  public void run() {
    variables.put(id, expression.evaluate());
  }

  private String id;
  private ComplexExpression expression;
  private Map<String, Complex> variables;
}