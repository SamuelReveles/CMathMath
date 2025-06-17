import java.util.Map;
import java.util.List;

public class IterationInstruction implements Instruction {
  public IterationInstruction(String id, Expression end, Expression step, List<Instruction> body, Map<String, Object> variables) {
    this.id = id;
    this.end = end;
    this.step = step;
    this.body = body;
    this.variables = variables;
  }
  
  @Override
  public void run() {
    Object iteradorObj = variables.get(id);
    if (iteradorObj == null) {
      System.err.println("Variable iterador no declarada: " + id);
      iteradorObj = Complex.fromReal(0);
    }
    if (!(iteradorObj instanceof Complex))
      throw new IncorrectExpressionTypeException(iteradorObj, ExpressionType.NUMBER);
    Complex iterador = (Complex) iteradorObj;

    Object finObj = end.evaluate();
    if (!(finObj instanceof Complex))
      throw new IncorrectExpressionTypeException(finObj, ExpressionType.NUMBER);
    Complex fin = (Complex) finObj;

    Object pasoObj = step.evaluate();
    if (!(pasoObj instanceof Complex))
      throw new IncorrectExpressionTypeException(pasoObj, ExpressionType.NUMBER);
    Complex paso = (Complex) pasoObj;

    if (iterador.isLessThan(fin)) {
      while (iterador.isLessThan(fin) || iterador.equals(fin)) {
        variables.put(id, iterador);
        for (Instruction in : body) {
          in.run();
        }
        iterador = iterador.add(paso);
      }
    } else {
      while (iterador.isGreaterThan(fin) || iterador.equals(fin)) {
        variables.put(id, iterador);
        for (Instruction in : body) {
          in.run();
        }
        iterador = iterador.substract(paso);
      }
    }
  }

  private String id;
  private Expression end;
  private Expression step;
  private List<Instruction> body;
  private Map<String, Object> variables;
}
