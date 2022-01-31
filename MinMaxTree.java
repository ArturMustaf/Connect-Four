public class MinMaxTree {
    private int numberOfChilds;
    private Node root, currentNode;

    public MinMaxTree(int c) {
        numberOfChilds = c;
        root = new Node(null, -1);
        currentNode = root;
    }

    public void setValue(int v) {
        currentNode.setValue(v);
        currentNode = currentNode.getParent();
    }

    public void canBeSet(int pos) {
        Node n = new Node(currentNode, pos);
        currentNode.setChild(pos, n);
        currentNode = n;
    }

    public void traverseUp() {
        currentNode = currentNode.getParent();
    }

    public int getBestMove() {
        setMinValue(root);
        int minValue, slot;
        if (root.getChild(3) != null) {
            minValue = root.getChild(3).getValue();
            slot = 3;
        }
        else {
            minValue = 10000;
            slot = 0;
        }

        for (int i = 0; i < numberOfChilds; i++) {
            Node child = root.getChild(i);
            if (child != null) {
                if (child.getValue() < minValue) {
                    minValue = child.getValue();
                    slot = i;
                }
            }
        }
        return slot;
   }


    private void setMaxValue(Node n) {
        if (n == null || n.hasChildren() == false)
            return;

        for (int i = 0; i < numberOfChilds; i++)
            setMinValue(n.getChild(i));

        int maxValue = 0;
        for (int i = 0; i < numberOfChilds; i++) {
            Node child = n.getChild(i);
            if (child != null) {
                int temp = child.getValue();
                if (temp > maxValue)
                    maxValue = temp;
            }
        }
        n.setValue(maxValue);
    }

   private void setMinValue(Node n) {
        if (n == null || n.hasChildren() == false)
            return;

        for (int i = 0; i < numberOfChilds; i++)
            setMaxValue(n.getChild(i));

        int minValue = 10000;
        for (int i = 0; i < numberOfChilds; i++) {
            Node child = n.getChild(i);
            if (child != null) {
                int temp = child.getValue();
                if (temp < minValue)
                    minValue = temp;
            }
        }
        n.setValue(minValue);
    }

    class Node {
        private Node parent;
        private Node[] childs;
        private boolean hasChildren;
        private int value, posInParent;

        Node(Node p, int posInP) {
            parent = p;
            posInParent = posInP;
        } 
        void setChild(int pos, Node n) {
            if (childs == null)
                childs = new Node[numberOfChilds];
            childs[pos] = n;
            hasChildren = true;
        }
        boolean hasChildren() {
            return hasChildren;
        }
        Node getChild(int pos) {
            return childs[pos];
        }
        int getPosInParent() {
            return posInParent;
        }
        Node getParent() {
            return parent;
        }
        void setValue(int v) {
            value = v;
        }
        int getValue() {
            return value;
        }
    }
}
