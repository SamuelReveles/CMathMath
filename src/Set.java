import java.util.List;
import java.util.HashSet;

public class Set {
  public Set(List<Expression> expressions) {
    elements = new HashSet<>();
    expressions.forEach(e -> {
      Object obj = e.evaluate();

      if (!(obj instanceof Complex))
        throw new IncorrectExpressionTypeException(obj, ExpressionType.NUMBER);
      
      elements.add((Complex) obj);
    });
  }

  public Set(Set other) {
    this.elements = new HashSet<>(other.elements);
  }

  public Set union(Set other) {
    Set result = new Set(this);
    result.elements.addAll(other.elements);
    return result;
  }

  public Set intersect(Set other) {
    Set result = new Set(this);
    result.elements.retainAll(other.elements);
    return result;
  }

  @Override
  public String toString() {
    if (this.elements.isEmpty()) {
      return "{ }";
    }

    StringBuilder builder = new StringBuilder();
    elements.forEach(e -> builder.append(e.toString() + ", "));
    return "{ " + builder.substring(0, builder.length() - 2) + " }";
  }

  private java.util.Set<Complex> elements;
}
