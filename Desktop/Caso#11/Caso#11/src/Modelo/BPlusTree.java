/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;
import java.util.ArrayList;

/*
 * Clase para el manejo de un árbol B+
 */

public class BPlusTree{
    class Node{
        public int mNumKeys = 0;/*Número de claves permitido*/
        public ArrayList[] mKeys = new ArrayList[2 * T - 1];/*Vector de claves*/
        public Object[] mObjects = new Object[2 * T - 1]; /*Objetos dentro del árbol B+*/
        public Node[] mChildNodes = new Node[2 * T];/*Vector de páginas hijas*/
        public boolean mIsLeafNode;/*Determina si una página es hoja o no lo es*/
        public Node mNextNode;/*Página siguiente para hacer el enlace respectivo*/
    }
    private Node mRootNode;/*Página raíz dentro del árbol B+*/
    private static final int T = 512;

    /*
     * Constructor del árbol B+
     */
    public BPlusTree() {
        mRootNode = new Node();
        mRootNode.mIsLeafNode = true;/*La página raíaz es una página hoja*/
    }
    
    /*
     * Método para adicionar un dato (objeto) al árbol B+
     */

    public void add(ArrayList<String> key, Object object){
        Node rootNode = mRootNode;
        if (rootNode.mNumKeys == (2 * T - 1)){
            Node newRootNode = new Node();
            mRootNode = newRootNode;
            newRootNode.mIsLeafNode = false;
            mRootNode.mChildNodes[0] = rootNode;
            splitChildNode(newRootNode, 0, rootNode);
            insertIntoNonFullNode(newRootNode, key, object);
        }else{
            insertIntoNonFullNode(rootNode, key, object);
        }
    }
    
    /*
     * Método para dividir una página cuando se encuentra completa
     */

    
    void splitChildNode(Node parentNode, int i, Node node){
        Node newNode = new Node();
        newNode.mIsLeafNode = node.mIsLeafNode;
        newNode.mNumKeys = T;
        for (int j = 0; j < T; j++){
            newNode.mKeys[j] = node.mKeys[j + T - 1];
            newNode.mObjects[j] = node.mObjects[j + T - 1];
        }if (!newNode.mIsLeafNode){
            for(int j = 0; j < T + 1; j++){
                newNode.mChildNodes[j] = node.mChildNodes[j + T - 1];
            }for(int j = T; j <= node.mNumKeys; j++){
                node.mChildNodes[j] = null;
            }
        }else{
            newNode.mNextNode = node.mNextNode;
            node.mNextNode = newNode;
        }for(int j = T - 1; j < node.mNumKeys; j++){
            node.mKeys[j] = null;
            node.mObjects[j] = null;
        }
        node.mNumKeys = T - 1;
        for(int j = parentNode.mNumKeys; j >= i + 1; j--){
            parentNode.mChildNodes[j + 1] = parentNode.mChildNodes[j];
        }
        parentNode.mChildNodes[i + 1] = newNode;
        for(int j = parentNode.mNumKeys - 1; j >= i; j--){
            parentNode.mKeys[j + 1] = parentNode.mKeys[j];
            parentNode.mObjects[j + 1] = parentNode.mObjects[j];
        }
        parentNode.mKeys[i] = newNode.mKeys[0];
        parentNode.mObjects[i] = newNode.mObjects[0];
        parentNode.mNumKeys++;
    }

    /*
     * Método para insertar un dato en una página no llena
     */
    
    void insertIntoNonFullNode(Node node, ArrayList<String> key, Object object){
        int i = node.mNumKeys - 1;
        if (node.mIsLeafNode){
            while (i >= 0 && key.get(0).compareTo((String) node.mKeys[i].get(0)) < 0 ){
                node.mKeys[i + 1] = node.mKeys[i];
                node.mObjects[i + 1] = node.mObjects[i];
                i--;
            }
            i++;
            node.mKeys[i] = key;
            node.mObjects[i] = object;
            node.mNumKeys++;
        }else{
            while(i >= 0 && key.get(0).compareTo((String) node.mKeys[i].get(0)) < 0){
                i--;
            }
            i++;
            if(node.mChildNodes[i].mNumKeys == (2 * T - 1)){
                splitChildNode(node, i, node.mChildNodes[i]);
                if(key.get(0).compareTo((String) node.mKeys[i].get(0)) > 0){
                    i++;
                }
            }
            insertIntoNonFullNode(node.mChildNodes[i], key, object);
        }
    }

    /*
     * Método que hace una búsqueda recursiva de una clave dentro del árbol B+
     */
    
    public Object search(Node node, ArrayList<String> key){
        int i = 0;
        while (i < node.mNumKeys && key.get(0).compareTo((String) node.mKeys[i].get(0)) > 0){
            i++;
        }if(i < node.mNumKeys && key == node.mKeys[i]){
            return node.mObjects[i];
        }if (node.mIsLeafNode){
            return null;
        }else{
            return search(node.mChildNodes[i], key);
        }
    }
    
    /*
     * Método para hacer una búsqueda de una clave dentro del árbol B+
     */

    public Object search(ArrayList<String> key) {
        return search(mRootNode, key);
    }

    /*
     * Método de búsqueda de una clave dentro del arból B+ no recursivo
     */
    
    public Object search2(Node node, ArrayList<String> key){
        while(node != null){
            int i = 0;
            while(i < node.mNumKeys && key.get(0).compareTo((String) node.mKeys[i].get(0)) > 0){
                i++;
            }if(i < node.mNumKeys && key == node.mKeys[i]){
                return node.mObjects[i];
            }if(node.mIsLeafNode){
                return null;}
            else{
                node = node.mChildNodes[i];
            }
        }
        return null;
    }
    
    /*
     * Método que busca un objeto en un árbol B+ según su clave
     */

    public Object search2(ArrayList<String> key) {
        return search2(mRootNode, key);
    }

    /*
     * Método que implementa la impresión como String del árbol B+
     */
    
    @Override
    public String toString() {
        String string = "";
        Node node = mRootNode;
        while (!node.mIsLeafNode) {
            node = node.mChildNodes[0];
        }
        while (node != null) {
            for (int i = 0; i < node.mNumKeys; i++) {
                string += node.mObjects[i] + ", ";
            }
            node = node.mNextNode;
        }
        return string;
    }

    /*
     * Método que imprime un árbol B+ entre dos rangos de claves dados
     */
    
    public String toString(ArrayList<String> fromKey, ArrayList<String> toKey) {
        String string = "";
        Node node = getLeafNodeForKey(fromKey);
        while (node != null) {
            for (int j = 0; j < node.mNumKeys; j++) {
                string += node.mObjects[j] + ", ";
                if (node.mKeys[j] == toKey) {
                    return string;
                }
            }
            node = node.mNextNode;
        }
        return string;
    }
    
    /*
     * Método que obtiene una hoja para una clave dentro del árbol B+
     */

    Node getLeafNodeForKey(ArrayList<String> key) {
        Node node = mRootNode;
        while (node != null) {
            int i = 0;
            while (i < node.mNumKeys && key.get(0).compareTo((String) node.mKeys[i].get(0)) > 0) {
                i++;
            }
            if (i < node.mNumKeys && key == node.mKeys[i]) {
                node = node.mChildNodes[i + 1];
                while (!node.mIsLeafNode) {
                    node = node.mChildNodes[0];
                }
                return node;
            }
            if (node.mIsLeafNode) {
                return null;
            } else {
                node = node.mChildNodes[i];
            }
        }
        return null;
    }
}
