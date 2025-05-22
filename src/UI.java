import java.io.IOException;
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

    private static final Pattern KEYWORD_PATTERN = Pattern.compile(
        "\\b(" + String.join("|", KEYWORDS).replace(" ", "\\s") + ")\\b"
    );

    private static final Pattern IMAGINARY_PATTERN = Pattern.compile("\\b-?\\d+(\\.\\d+)?i\\b");

    private CodeArea codeEditor;
    private TextArea consoleOutput;
    private VBox consoleBox;

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

        Button exeBtn = new Button("Generar EXE");
        exeBtn.getStyleClass().add("file-btn");
        exeBtn.setStyle("-fx-font-size: 12px; -fx-padding: 6 10 6 10;");
        exeBtn.setOnAction(e -> generateExe());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button runBtn = new Button("Ejecutar");
        runBtn.getStyleClass().add("run-btn");
        runBtn.setStyle("-fx-font-size: 13px; -fx-padding: 6 18 6 18;");
        runBtn.setOnAction(e -> compileCode());

        fileBar.getChildren().addAll(importBtn, exportBtn, exeBtn, spacer, runBtn);

        // --- Editor de código con RichTextFX ---
        codeEditor = new CodeArea();
        codeEditor.setParagraphGraphicFactory(LineNumberFactory.get(codeEditor));
        codeEditor.getStyleClass().add("code-editor");
        codeEditor.setWrapText(true);
        codeEditor.textProperty().addListener((obs, oldText, newText) -> {
            codeEditor.setStyleSpans(0, computeHighlighting(newText));
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

    // --- Resaltado de palabras clave ---
    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher keywordMatcher = KEYWORD_PATTERN.matcher(text);
        Matcher imaginaryMatcher = IMAGINARY_PATTERN.matcher(text);

        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        // Guardar los rangos de los imaginarios
        java.util.List<int[]> imaginaryRanges = new java.util.ArrayList<>();
        while (imaginaryMatcher.find()) {
            imaginaryRanges.add(new int[]{imaginaryMatcher.start(), imaginaryMatcher.end()});
        }

        int idx = 0;
        while (keywordMatcher.find()) {
            // Añadir estilos para imaginarios antes de la palabra clave
            while (idx < imaginaryRanges.size() && imaginaryRanges.get(idx)[0] < keywordMatcher.start()) {
                int[] range = imaginaryRanges.get(idx);
                if (range[0] > lastKwEnd)
                    spansBuilder.add(Collections.emptyList(), range[0] - lastKwEnd);
                spansBuilder.add(Collections.singleton("imaginary"), range[1] - range[0]);
                lastKwEnd = range[1];
                idx++;
            }
            if (keywordMatcher.start() > lastKwEnd)
                spansBuilder.add(Collections.emptyList(), keywordMatcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton("keyword"), keywordMatcher.end() - keywordMatcher.start());
            lastKwEnd = keywordMatcher.end();
        }
        // Añadir estilos para imaginarios después de la última palabra clave
        while (idx < imaginaryRanges.size()) {
            int[] range = imaginaryRanges.get(idx);
            if (range[0] > lastKwEnd)
                spansBuilder.add(Collections.emptyList(), range[0] - lastKwEnd);
            spansBuilder.add(Collections.singleton("imaginary"), range[1] - range[0]);
            lastKwEnd = range[1];
            idx++;
        }
        if (text.length() > lastKwEnd)
            spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);

        return spansBuilder.create();
    }

    // --- Funciones de botones ---
    private void importCode(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar archivo de código");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Archivos de texto", "*.txt", "*.cmm"),
            new FileChooser.ExtensionFilter("Todos los archivos", "*.*"));
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
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Archivos de texto", "*.txt", "*.cmm"),
            new FileChooser.ExtensionFilter("Todos los archivos", "*.*"));
        var file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                Files.writeString(file.toPath(), codeEditor.getText());
                showConsole();
                consoleOutput.appendText("Código exportado correctamente.\n");
            } catch (IOException ex) {
                showConsole();
                consoleOutput.appendText("Error al exportar archivo.\n");
            }
        }
    }

    private void generateExe() {
        showConsole();
        consoleOutput.appendText("Función para generar EXE no implementada.\n");
    }

    private void compileCode() {
        String code = codeEditor.getText();
        if (code.isBlank()) {
            consoleOutput.appendText("No hay código para ejecutar.\n");
            return;
        }
        // Aquí iría la lógica real de compilación/ejecución
        consoleOutput.appendText("Ejecutando...\n");
        // Simulación de salida
        consoleOutput.appendText("Ejecución finalizada.\n");
    }

    private void showConsole() {
        if (!consoleBox.isVisible()) {
            consoleBox.setVisible(true);
        }
    }
}
