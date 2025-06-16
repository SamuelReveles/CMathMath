public class PrintInstruction implements Instruction {
  private Expression expression;
  private String string;
  private ConsoleController consoleController;

  public PrintInstruction(Expression expression, ConsoleController consoleController) {
    this.expression = expression;
    this.consoleController = consoleController;
    this.string = null;
  }

  public PrintInstruction(String string, ConsoleController consoleController) {
    this.string = string;
    this.consoleController = consoleController;
    this.expression = null;
  }

  public PrintInstruction(String string, Expression expression, ConsoleController consoleController) {
    this.string = string;
    this.consoleController = consoleController;
    this.expression = expression;
  }

  @Override
  public void run() {
    if (expression == null) {
      consoleController.println(string);
    } else if (string == null) {
      consoleController.println(expression.evaluate().toString());
    } else {
      consoleController.println(string + expression.evaluate().toString());
    }
  }
}
