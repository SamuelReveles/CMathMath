import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

public class UI extends Application {

    private static final String[] KEYWORDS = {
      "dado", "equivale", "si", "si no", "pero si", "f", "mientras", "desde", "con", "avanzar", "tal que",
      "conjunto", "PI", "E", "imprimir"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS).replace(" ", "\\s") + ")\\b";
    private static final String NUMBER_PATTERN = "\\b\\d+(\\.\\d+)?\\b";
    private static final String IMAGINARY_PATTERN = "\\b-?\\d+(\\.\\d+)?i\\b";
    private static final String DEGREES_PATTERN = "(?<!\\S)-?\\d+(\\.\\d+)?°(?!\\S)";
    private static final String OPERATOR_PATTERN = "[+\\-*/=<>!&|%^~]+";
    private static final String BRACE_PATTERN = "[\\[\\]{}()]";
    private static final String COMMENT_PATTERN = "//.*";
    private static final String COMMA_PATTERN = ",";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String ID_PATTERN = "\\b(?!(" + String.join("|", KEYWORDS).replace(" ", "\\s")
            + ")\\b)[a-zA-Z_][a-zA-Z_0-9]*\\b";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<IMAGINARY>" + IMAGINARY_PATTERN + ")"
                    + "|(?<DEGREES>" + DEGREES_PATTERN + ")"
                    + "|(?<NUMBER>" + NUMBER_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<OPERATOR>" + OPERATOR_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
                    + "|(?<ID>" + ID_PATTERN + ")"
                    + "|(?<COMMA>" + COMMA_PATTERN + ")");

    private CodeArea codeEditor;
    private TextArea consoleOutput;
    private VBox consoleBox;
    private ConsoleController consoleController;

    @Override
    public void start(Stage stage) {
        HBox fileBar = new HBox(0);
        fileBar.getStyleClass().add("top-bar");
        fileBar.setPadding(new Insets(5, 5, 5, 5));
        fileBar.setAlignment(Pos.CENTER_LEFT);

        Button importBtn = new Button("Importar");
        importBtn.getStyleClass().add("file-btn");
        importBtn.setStyle("-fx-font-size: 12px; -fx-padding: 6 10 6 10;");
        importBtn.setOnAction(e -> importCode(stage));

        Button exportBtn = new Button("Exportar");
        exportBtn.getStyleClass().add("file-btn");
        exportBtn.setStyle("-fx-font-size: 12px; -fx-padding: 6 10 6 10;");
        exportBtn.setOnAction(e -> exportCode(stage));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button runBtn = new Button("Ejecutar");
        runBtn.getStyleClass().add("run-btn");
        runBtn.setStyle("-fx-font-size: 13px; -fx-padding: 6 18 6 18;");
        runBtn.setOnAction(e -> compileCode());

        fileBar.getChildren().addAll(importBtn, exportBtn, spacer, runBtn);

        // --- Editor de código con RichTextFX ---
        codeEditor = new CodeArea();
        codeEditor.setParagraphGraphicFactory(LineNumberFactory.get(codeEditor));
        codeEditor.getStyleClass().add("code-editor");
        codeEditor.setWrapText(true);
        codeEditor.textProperty().addListener((obs, oldText, newText) -> {
            codeEditor.setStyleSpans(0, keyHighlighting(newText, PATTERN));
        });

        VBox editorBox = new VBox(codeEditor);
        VBox.setVgrow(codeEditor, Priority.ALWAYS);

        // --- Consola de salida flotante ---
        consoleOutput = new TextArea();
        consoleOutput.setEditable(false);
        consoleOutput.setFont(Font.font("Consolas", 14));
        consoleOutput.getStyleClass().add("console-output");
        consoleOutput.setPromptText("Salida de la consola...");
        consoleOutput.setWrapText(true);

        // Se crea un controlador de consola que servirá para que
        // el parser imprima en ella.
        consoleController = new ConsoleController(consoleOutput);

        Label consoleLabel = new Label("Consola:");
        consoleLabel.getStyleClass().add("console-label");

        Button closeConsoleBtn = new Button("✕");
        closeConsoleBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #00e8c6; -fx-font-size: 14px; -fx-padding: 2 8 2 8;");
        closeConsoleBtn.setOnAction(e -> consoleBox.setVisible(false));

        HBox consoleHeader = new HBox(consoleLabel, new Region(), closeConsoleBtn);
        HBox.setHgrow(consoleHeader.getChildren().get(1), Priority.ALWAYS);

        consoleBox = new VBox(consoleHeader, consoleOutput);
        consoleBox.setSpacing(2);
        consoleBox.setPadding(new Insets(10, 10, 10, 10));
        consoleBox.setMaxHeight(220);
        consoleBox.setMinHeight(120);
        consoleBox.setVisible(false);

        // Mostrar consola al compilar
        runBtn.setOnAction(e -> {
            consoleBox.setVisible(true);
            compileCode();
        });

        // --- Layout principal ---
        StackPane stack = new StackPane(editorBox);
        stack.getChildren().add(consoleBox);
        StackPane.setAlignment(consoleBox, Pos.BOTTOM_CENTER);

        VBox root = new VBox(fileBar, stack);
        VBox.setVgrow(stack, Priority.ALWAYS);
        root.setPadding(new Insets(0, 0, 0, 0));
        root.getStyleClass().add("main-root");

        Scene mainScene = new Scene(root);
        mainScene.getStylesheets().addAll("styles/topbar.css", "styles/editor.css");

        stage.setTitle("CMathMath IDE");
        stage.setMaximized(true);
        stage.setScene(mainScene);
        stage.show();

        // Inicializar resaltado al inicio
        codeEditor.replaceText("");
    }

    private StyleSpans<Collection<String>> keyHighlighting(String text, Pattern pattern) {
        Matcher keywordMatcher = pattern.matcher(text);
        int lastKeyword = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        while (keywordMatcher.find()) {
            String styleClass = keywordMatcher.group("KEYWORD") != null ? "keyword"
                    : keywordMatcher.group("NUMBER") != null ? "number"
                            : keywordMatcher.group("IMAGINARY") != null ? "imaginary"
                                    : keywordMatcher.group("DEGREES") != null ? "degrees"
                                            : keywordMatcher.group("STRING") != null ? "string"
                                                    : keywordMatcher.group("ID") != null ? "id"
                                                            : keywordMatcher.group("OPERATOR") != null ? "operator"
                                                                    : keywordMatcher.group("BRACE") != null ? "brace"
                                                                    : keywordMatcher.group("COMMENT") != null ? "comment"
                                                                    : keywordMatcher.group("BRACE") != null ? "brace"
                                                                                    : keywordMatcher
                                                                                            .group("COMMA") != null
                                                                                                    ? "comma"
                                                                                                    : null;
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), keywordMatcher.start() - lastKeyword);
            spansBuilder.add(Collections.singleton(styleClass), keywordMatcher.end() - keywordMatcher.start());
            lastKeyword = keywordMatcher.end();
        }

        spansBuilder.add(Collections.emptyList(), text.length() - lastKeyword);
        return spansBuilder.create();

    }

    // --- Funciones de botones ---
    private void importCode(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar archivo de código");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos .cmm", "*.cmm"));
        var file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                String content = Files.readString(file.toPath());
                codeEditor.replaceText(content);
            } catch (IOException ex) {
                showConsole();
                consoleOutput.appendText("Error al importar archivo.\n");
            }
        }
    }

    private void exportCode(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar código");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos .cmm", "*.cmm"));
        var file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                String path = file.toPath().toString();
                if (!path.toLowerCase().endsWith(".cmm")) {
                    file = new File(path + ".cmm");
                }
                Files.writeString(file.toPath(), codeEditor.getText());
                showConsole();
                consoleOutput.appendText("Código exportado correctamente.\n");
            } catch (IOException ex) {
                showConsole();
                consoleOutput.appendText("Error al exportar archivo.\n");
            }
        }
    }

    private void compileCode() {
        String code = codeEditor.getText();

        // La consola se reiniciará en cada compilación.
        consoleOutput.clear();

        if (code.isBlank()) {
            consoleOutput.appendText("No hay código para ejecutar.\n");
            return;
        }

        // Este reader permite pasar el texto de código al parser.
        StringReader stringReader = new StringReader(code);

        // Se construye el parser con un lexer que lee el código y
        // con el consoleController local para mostrar resultados en consola.
        CMathMathParser parser = new CMathMathParser(new CMathMathLexer(stringReader), consoleController);

        // Proceso principal de parseo.
        try {
            parser.parse();
        }
        // Cualquier error al parsear será informado en la misma consola.
        catch (Exception e) {
            consoleOutput.appendText("Error: " + e.getMessage());
        }
    }

    private void showConsole() {
        if (!consoleBox.isVisible()) {
            consoleBox.setVisible(true);
        }
    }
}
