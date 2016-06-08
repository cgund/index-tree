package tree;


import java.util.ArrayList;
import java.util.List;


/*Class to encapsulate binary tree of type E objects.  */
public class BinaryIndexTree<E> 
{
    protected Node<E> root;  //root of tree

    public BinaryIndexTree() 
    {
        root = null; //constructor sets root to null
    }

    /*
    Executes a traversal of tree, returns string representation of nodes
    within tree
    */
    @Override
    public String toString() 
    {
        StringBuilder sb = new StringBuilder();
        inOrderTraverse(root, sb);
        return sb.toString();
    }

    /*
    Traverses left subtree, root node, and then right subtree.  Reference to a 
    string builder passed with every recursive call
    */
    private void inOrderTraverse(Node<E> node, StringBuilder sb) 
    {
        if (node != null) 
        {
            inOrderTraverse(node.left, sb);
            sb.append(node.toString());
            sb.append("\n");
            inOrderTraverse(node.right, sb);
        } 
    }
    
    /* Class to encapsulate tree node*/
    protected static class Node<E> 
    {
        public E data; //stores word
        public int numOccurences; // stores number of occurences of word
        public List<Integer> lineNumbers; //stores list of line numbers word appears on
        public Node<E> left;
        public Node<E> right;

        /*
        Constructor of node initializes number of occurences to 1 and adds line
        number of first occurence.  Left and right child nodes are null
        */
        public Node(E data, int lineNum) 
        {
            this.data = data;
            numOccurences = 1;
            lineNumbers = new ArrayList<>();
            lineNumbers.add(lineNum);
            left = null;
            right = null;
        }

        @Override
        public String toString() 
        {
            String output = data.toString() + ", " + numOccurences + ", " + 
                    lineNumbers.toString();
            return output;
        }
    }
}

