package spell;

public class Node implements INode{
    private int value;
    private Node[] node;

    public Node() {
        value =0;
        setNode(new Node[26]);
    }

    private void setNode(Node[] nodes) {
        node = nodes;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void incrementValue() {
        value++;
    }

    @Override
    public INode[] getChildren() {
        return node;
    }
}
