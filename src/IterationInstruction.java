import java.util.Map;
import java.util.List;

public class IterationInstruction implements Instruction {
  public IterationInstruction(String id, Expression end, Expression step, List<Instruction> body, Map<String, Complex> variables) {
    this.id = id;
    this.end = end;
    this.step = step;
    this.body = body;
    this.variables = variables;
  }
  
  public void run() {
    Complex iterador = variables.get(id);
    if (iterador == null) {
      System.err.println("Variable iterador no declarada: " + id);
      iterador = Complex.fromReal(0);
    }

    Complex fin = end.evaluate();
    Complex paso = step.evaluate();

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
  private Map<String, Complex> variables;
}
