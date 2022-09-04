package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

public class Controller {

    private PrefixTree<String>        prefixTree;
    private Map<Character, StackPane> nodes;
    @FXML
    private TreeView<String> treeView;

    @FXML
    void initialize() {
        prefixTree = new PrefixTree();
        nodes = new HashMap<>();
        //   loadDictionary();
        insertDefValues();
        dfsWithoutRecursion();
    }

    private void loadDictionary(List<String> dictonary) {
        prefixTree.getRoot().getChildren().clear();
        dictonary.forEach(it -> {
            prefixTree.insert(it);
        });
    }

    private void insertDefValues() {
        prefixTree.insert("luk");
        prefixTree.insert("rek");
        prefixTree.insert("sek");
        prefixTree.insert("brek");
        prefixTree.insert("marek");
        prefixTree.insert("bor");
        prefixTree.insert("dar");
        prefixTree.insert("zmar");
        prefixTree.insert("magor");
        prefixTree.insert("tabor");
    }

    @FXML
    void onAddWordClicked(ActionEvent event) {
        Dialog<String> addWordDialog = new Dialog<>();
        addWordDialog.setTitle("Add word");
        addWordDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField wordTextField = new TextField();

        grid.add(new Label("Word:"), 0, 0);
        grid.add(wordTextField, 1, 0);
        addWordDialog.getDialogPane().setContent(grid);
        Optional<String> result = addWordDialog.showAndWait();
        if (result.isPresent()) {
            prefixTree.insert(wordTextField.getText());
            updateView();
        }
    }

    @FXML
    void onDeleteWordClicked(ActionEvent event) {
        Dialog<String> deleteWordDialog = new Dialog<>();
        deleteWordDialog.setTitle("Delete word");
        deleteWordDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField wordTextField = new TextField();

        grid.add(new Label("Word:"), 0, 0);
        grid.add(wordTextField, 1, 0);
        deleteWordDialog.getDialogPane().setContent(grid);
        Optional<String> result = deleteWordDialog.showAndWait();
        if (result.isPresent()) {
            prefixTree.delete(wordTextField.getText());
            updateView();
        }
    }

    public void dfsWithoutRecursion() {
        Stack<Pair<PrefixTree<String>.TreeNode, TreeItem<String>>> stack = new Stack<>();
        TreeItem<String> currentItemView = new TreeItem<>(prefixTree.getRoot().getKey().toString());
        stack.push(new Pair(prefixTree.getRoot(), currentItemView));
        currentItemView.setExpanded(true);
        treeView.setRoot(currentItemView);
        while (!stack.isEmpty()) {
            Pair<PrefixTree<String>.TreeNode, TreeItem<String>> current = stack.pop();
            PrefixTree<String>.TreeNode currentNode = current.getKey();
            currentItemView = current.getValue();
            if (!currentNode.isVisited()) {
                currentNode.setVisited(true);
                for (Map.Entry<Character, PrefixTree<String>.TreeNode> entry : currentNode.getChildren().entrySet()) {
                    if (!entry.getValue().isVisited()) {
                        TreeItem<String> tmp = new TreeItem<>(entry.getKey().toString());
                        currentItemView.getChildren().add(tmp);
                        stack.push(new Pair(entry.getValue(), tmp));
                    }
                }
            }
        }
    }

    @FXML
    void onSearchWordClicked(ActionEvent event) {
        Dialog<String> searchWordDialog = new Dialog<>();
        searchWordDialog.setTitle("Search word");
        searchWordDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField wordTextField = new TextField();
        TextField resultField = new TextField();
        resultField.setDisable(true);
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            final boolean result = prefixTree.wordSearch(wordTextField.getText());
            resultField.setText(String.valueOf(result));
        });

        grid.add(new Label("Word:"), 0, 0);
        grid.add(wordTextField, 1, 0);
        grid.add(new Label("Result:"), 0, 1);
        grid.add(resultField, 1, 1);
        grid.add(searchButton, 0, 2);
        searchWordDialog.getDialogPane().setContent(grid);
        searchWordDialog.showAndWait();
    }

    @FXML
    void onSearchPrefixClicked(ActionEvent event) {
        Dialog<String> searchWordDialog = new Dialog<>();
        searchWordDialog.setTitle("Search word");
        searchWordDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField wordTextField = new TextField();
        TextArea resultField = new TextArea();
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            final List<String> result = prefixTree.prefixSearch(wordTextField.getText()).stream().sorted().collect(Collectors.toList());
            resultField.setText(String.join("\n", result));
        });

        grid.add(new Label("Prefix:"), 0, 0);
        grid.add(wordTextField, 1, 0);
        grid.add(new Label("Result:"), 0, 1);
        grid.add(resultField, 1, 1);
        grid.add(searchButton, 0, 2);
        searchWordDialog.getDialogPane().setContent(grid);
        searchWordDialog.showAndWait();
    }

    @FXML
    void onImportFile(ActionEvent event) {
        FileChooser file = new FileChooser();
        file.setTitle("Open File");
        File selectedFile = file.showOpenDialog(null);
        if(selectedFile != null){
            loadDictionary(FileService.readWords(selectedFile.getAbsolutePath()));
            updateView();
        }
    }
    public void updateView() {
        treeView.getRoot().getChildren().clear();
        treeView.setRoot(null);
        prefixTree.reset();
        dfsWithoutRecursion();
        treeView.refresh();
    }

    //
    private StackPane getNode(Double x, Double y, String text) {
        StackPane stack = new StackPane();
        stack.setLayoutX(x);
        stack.setLayoutY(y);
        final Circle vertex = new Circle(15);
        vertex.setFill(Color.RED);
        final Text vertexLabel = new Text(text);
        vertexLabel.setFill(Color.WHITE);
        stack.getChildren().addAll(vertex, vertexLabel);
        return stack;
    }

}
