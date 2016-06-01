
/*
Class to allow data to be added to BinaryIndexTree.
*/
public class BinaryIndexSearchTree<E extends Comparable<E>> extends BinaryIndexTree<E>
{
    protected boolean addReturn;

    /*
    Public method for interfacing with binary search tree.  Calls private add
    method.  Starts at root of tree
    */
    public boolean add(E word, int lineNum) 
    {
        root = add(root, word, lineNum);
        return addReturn;
    }
    
    /*
    Private method for adding new node, updating existing node, finding node
    in tree
    */
    private Node<E> add(Node<E> localRoot, E word, int lineNum) 
    {
        if (localRoot == null) //adds new node to tree
        {
            addReturn = true;
            return new Node<>(word, lineNum);
        } 
        else if (word.compareTo(localRoot.data) == 0) //updates existing node
        {
            addReturn = false;
            localRoot.numOccurences++;
            localRoot.lineNumbers.add(lineNum);
            return localRoot;
        } 
        else if (word.compareTo(localRoot.data) < 0) //moves to left node of local node
        {
            localRoot.left = add(localRoot.left, word, lineNum);
            return localRoot;
        } 
        else //moves to right node of local node
        {
            localRoot.right = add(localRoot.right, word, lineNum);
            return localRoot;
        }
    }
}