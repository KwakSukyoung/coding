package spell;

public class Trie implements ITrie{
    private int numNode;
    private int numUnique;
    private Node root;

    public Trie() {
        numNode = 1;
        numUnique = 0;
        root = new Node();
    }

    @Override
    public void add(String word) {
        if(find(word)!=null){
            find(word).incrementValue();
        }
        else{
            Node curr = (Node) root;
            for(int i =0; i < word.length();++i){
                Node[] children = (Node[]) curr.getChildren();
                if(children[word.charAt(i)-'a']==null){
                    children[word.charAt(i)-'a'] = new Node();
                    numNode++;
                }
                curr = children[word.charAt(i)-'a'];
                if(i==word.length()-1){
                    numUnique++;
                    curr.incrementValue();
                }
            }
        }
    }

    @Override
    public INode find(String word) {
        Node curr = (Node) root;
        for(int i =0; i < word.length();++i){
            Node[] children = (Node[]) curr.getChildren();
            if(children[word.charAt(i)-'a']==null){
                return null;
            }
            curr = children[word.charAt(i)-'a'];
        }
        if(curr.getValue()==0){
            return null;
        }
        return curr;
    }

    @Override
    public int getWordCount() {
        return numUnique;
    }

    @Override
    public int getNodeCount() {
        return numNode;
    }

    @Override
    public int hashCode() {
        int n;
        int k = 1;
        Node curr = (Node) root;
        Node[] children = (Node[]) curr.getChildren();
        for(int i =0;i < children.length;++i){
            if(children[i]!=null){
                k = i+ children[i].getValue();
            }
        }
        n = k*numNode*numUnique;
        return n;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }
        if(obj == null){
            return false;
        }
        if(obj.getClass()!= this.getClass()){
            return false;
        }
        Trie other = (Trie) obj;
        if(other.getWordCount()!= this.getWordCount()){
            return false;
        }
        if(other.getNodeCount() != this.getNodeCount()){
            return false;
        }
        return equalsHelper(other.root, this.root);
    }

    private boolean equalsHelper(Node a, Node b) {
        if(a.getValue()!= b.getValue()){
            return false;
        }
        for(int i=0; i< 26;++i){
            if(a.getChildren()[i]==null^b.getChildren()[i]==null){
                return false;
            }
            if(a.getChildren()[i]!=null&&b.getChildren()[i]!=null){
                if(!equalsHelper((Node)a.getChildren()[i], (Node)b.getChildren()[i])){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder curr = new StringBuilder();
        StringBuilder output = new StringBuilder();
        stringHelper(root, curr, output);
        return output.toString();
    }

    private void stringHelper(Node n, StringBuilder curr, StringBuilder output) {
        if(n.getValue()>0){
            output.append(curr.toString()+"\n");
        }
        for(int i=0;i < 26;++i){
            Node child = (Node) n.getChildren()[i];
            if(child!=null){
                curr.append((char)('a'+i));
                stringHelper(child, curr, output);
                curr.deleteCharAt(curr.length()-1);
            }
        }
    }
}
