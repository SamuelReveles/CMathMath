public class PrintInstruction implements Instruction {
  public PrintInstruction(Expression expression, ConsoleController consoleController) {
    this.expression = expression;
    this.consoleController = consoleController;
  }

  @Override
  public void run() {
    consoleController.println(expression.evaluate().toString());
  }

  private Expression expression;
  private ConsoleController consoleController;
}
