
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/*
Class to demo BinaryIndexTree
*/
public class DemoBST extends Application
{
    private final Insets DEFAULT_INSETS = new Insets(10, 10, 10, 10);
    private final TextArea textArea = new TextArea();
     
    @Override
    public void start(Stage primaryStage)
    {
        BorderPane root = new BorderPane();
        
        root.setCenter(createDisplayPane(collectFiles("txt_files")));
        root.setBottom(createButtonPane(primaryStage));
        
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Text File Search");
        primaryStage.show();
    }
    
    /*
    Collects all files in a folder while excluding directories.  Returns
    List bound to File
    */
    private List<File> collectFiles(String folder)
    {
        try
        {
            List<File> textFiles = Files.walk(Paths.get(folder)) 
                 .filter(Files::isRegularFile)
                 .map(Path::toFile)
                 .collect(Collectors.toList());   
            return textFiles;
        }
        catch(IOException ex)
        {
            displayAlert(ex.getMessage());
        }
        return null;
    }
    
    private Node createDisplayPane(List<File> textFiles)
    {
        TreeItem rootItem = new TreeItem("Files"); //root of TreeView
        rootItem.setExpanded(true);
        for (File textFile : textFiles)
        {
            Hyperlink hlFile = new Hyperlink();
            hlFile.setText(textFile.getName());
            hlFile.setOnAction(e ->
            {
                textArea.clear();
                try
                {
                    processFile(textFile);
                }
                catch(FileNotFoundException ex)
                {
                    displayAlert(ex.getMessage());
                }
            }); 
            TreeItem file = new TreeItem(hlFile);
            rootItem.getChildren().add(file); //adds node to root
        }
        TreeView treeView = new TreeView(rootItem);
        
        textArea.setEditable(false);
        
        HBox hbDisplay = new HBox(10);
        hbDisplay.setAlignment(Pos.CENTER);
        hbDisplay.setPadding(DEFAULT_INSETS);
        hbDisplay.getChildren().addAll(treeView, textArea);
        return hbDisplay;
    }
    
    private void displayAlert(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();        
    }
    
    private void processFile(File file) throws FileNotFoundException
    {
        textArea.setText("WORD,  # OF OCCURENCES,  [LINE NUMBERS]\n\n");
        BinaryIndexSearchTree<String> bst = new BinaryIndexSearchTree<>();
        int lineNum = 1;
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)))) 
        {
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                String[] words = line.split(" ");
                for (String word: words)
                {
                    word = word.toLowerCase();                   
                    //Replaces all non lowercase alpha chars at start and end of word
                    word = word.replaceAll("^[^a-z]+|[^a-z]+$", "");
                    /*If word does not equal any number of occurences of space
                    or is not empty, then add word and line number to tree*/
                    if (!word.equals("\\s*") && !word.isEmpty())
                    {
                        bst.add(word, lineNum);
                    }
                }
                lineNum++;
            }
        }
        textArea.appendText(bst.toString());
    }
    
    private Node createButtonPane(Stage primaryStage)
    {
        Button btnClose = new Button("Close");
        btnClose.setOnAction(e ->
        {
            primaryStage.close();
        });
        
        HBox hbButton = new HBox(10);
        hbButton.setAlignment(Pos.CENTER);
        hbButton.setPadding(DEFAULT_INSETS);
        hbButton.getChildren().add(btnClose); 
        return hbButton;
    }
    
    public static void main(String[] args)
    {
        Application.launch(args);      
    }
    

}

