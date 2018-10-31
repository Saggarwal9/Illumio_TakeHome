class TrieNode {
    TrieNode [] children = null;
    public TrieNode() {
        children = new TrieNode [256];
    }
}


public class Trie {

    TrieNode head;
    
    public Trie() {
        head = new TrieNode();
    }
    
    public void insert(String [] ip) {
        TrieNode node = head;
        for (int i = 0; i < ip.length; i++) {
            int num = Integer.parseInt(ip[i]);
            if (node.children[num] == null) {
                node.children[num] = new TrieNode();
            }
            node = node.children[num];
        }
    }
    
    public boolean search(String [] ip) {
        TrieNode node = head;
        for (int i = 0; i < ip.length; i++) {
            int num = Integer.parseInt(ip[i]);
            if (node.children[num] == null) {
               return false;
            }
            node = node.children[num];
        }
        return true;
    }
}
