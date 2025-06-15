public class PrintSetInstruction implements Instruction {
  public PrintSetInstruction(SetExpression setExpression, ConsoleController consoleController) {
    this.setExpression = setExpression;
    this.consoleController = consoleController;
  }

  @Override
  public void run() {
    consoleController.println(setExpression.evaluate().toString());
  }

  private SetExpression setExpression;
  private ConsoleController consoleController;
}
