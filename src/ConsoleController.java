import javafx.scene.control.TextArea;

public class ConsoleController {
  public ConsoleController(TextArea consoleArea) {
    this.consoleArea = consoleArea;
  }

  public void print(String s) {
    consoleArea.appendText(s);
  }

  public void println(String s) {
    consoleArea.appendText(s + "\n");
  }

  private TextArea consoleArea;
}
