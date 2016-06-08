package tree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
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
        
        try
        {
            JarFile jar = new JarFile("IndexTree.jar");
            root.setCenter(createDisplayPane(collectResources(jar)));
            root.setBottom(createButtonPane(primaryStage));
        }
        catch(NoSuchElementException | IOException ex)
        {
            displayAlert(ex.getMessage());
        }
        
        Scene scene = new Scene(root, 700, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Text File Search");
        primaryStage.show();
    }
    
    /*
    Collects all resources in JarFile while excluding directories.  Returns
    List bound to String
    */
    private List<String> collectResources(JarFile jar)
    {
        if (jar != null)
        {
            Enumeration<JarEntry> entries = jar.entries();
            List<String> resourcePaths = new ArrayList<>();
            while (entries.hasMoreElements())
            {
                JarEntry entry = entries.nextElement();
                String resourcePath = entry.getName();
                String fileType = resourcePath.substring(resourcePath.length() - 3, resourcePath.length());
                if (fileType.equals("txt"))
                {
                    resourcePaths.add(resourcePath);
                    System.out.println(resourcePath);
                }
            }
            return resourcePaths;            
        }
        else{
            throw new NoSuchElementException();
        }
    }
    
    /*
    Iterates through a collection of resource paths, trims them, and passes the
    trimmed path to a method that is called when the hyperlink is clicked
    */
    private Node createDisplayPane(List<String> resourcePaths)
    {
        TreeItem rootItem = new TreeItem("Text Files"); //root of TreeView
        rootItem.setExpanded(true);
        for (String resourcePath : resourcePaths)
        {
            int indexSlash = resourcePath.indexOf("/");
            String trimmedPath = resourcePath.substring(indexSlash + 1, resourcePath.length());
            indexSlash = trimmedPath.indexOf("/");
            String name = trimmedPath.substring(indexSlash + 1, trimmedPath.length());
            Hyperlink hlResource = new Hyperlink();
            hlResource.setText(name);
            hlResource.setOnAction(e ->
            {
                textArea.clear();
                try
                {
                    showWordCount(trimmedPath);
                }
                catch(FileNotFoundException ex)
                {
                    displayAlert(ex.getMessage());
                }
            });
            
            Hyperlink hlFull = new Hyperlink();
            hlFull.setText("[Full Text]");
            hlFull.setOnAction(e ->
            {
                textArea.clear();
                showFullText(trimmedPath);
            });
            
            HBox hb = new HBox(10);
            hb.setAlignment(Pos.CENTER_LEFT);
            hb.getChildren().addAll(hlResource, hlFull);
            
            TreeItem file = new TreeItem(hb);
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
    
    /*
    Displays full text of txt document
    */
    private void showFullText(String resource)
    {
        InputStream inStream = DemoBST.class.getResourceAsStream(resource);
        StringBuilder sb = new StringBuilder();
        Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(inStream))); 
        while (scanner.hasNext())
        {
            sb.append(scanner.nextLine());
            sb.append("\n");
        }
        scanner.close();
        textArea.appendText(sb.toString());        
    }
    
    /*
    Creates a new BinaryIndexSearchTree.  Creates a new InputStream of the 
    resource. Uses a BufferedReader to scan the stream.  Adds word and the line
    number it occured on to tree.  Calls the toString() method on the tree and
    appends the return value to the TextArea
    */
    private void showWordCount(String resource) throws FileNotFoundException
    {
        textArea.setText("WORD,  # OF OCCURENCES,  [LINE NUMBERS]\n\n");
        BinaryIndexSearchTree<String> bst = new BinaryIndexSearchTree<>();
        int lineNum = 1;
        InputStream inStream = DemoBST.class.getResourceAsStream(resource);

        try (BufferedReader scanner = new BufferedReader(new InputStreamReader(inStream))) 
        {
            String line = scanner.readLine();
            while (line != null)
            {
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
                line = scanner.readLine();
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
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
    
        
    private void displayAlert(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();        
    }
    
    public static void main(String[] args)
    {
        Application.launch(args);      
    }
    

}

