package sample;

public class Node {
    
    private String _name;
    private String _type;
    private int _port;
    
    public Node(String nodeType, String nodeName, int port){
        this._name = nodeName;
        this._type = nodeType;
        this._port = port;
        
        //createNode();
    }


}
