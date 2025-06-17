public class IncorrectExpressionTypeException extends RuntimeException {
  public IncorrectExpressionTypeException(
    Object expressionResult,
    ExpressionType expectedExpressionType
  ) {
    super (
      "No se puede usar '" + expressionResult +
      "' donde se esperaba " + getExpressionTypeName(expectedExpressionType)
    );
  }

  static private String getExpressionTypeName(ExpressionType type) {
    switch (type) {
      case SET: return "un conjunto";
      case NUMBER: return "un número";
      default: return "un tipo indefinido";
    }
  }
}
