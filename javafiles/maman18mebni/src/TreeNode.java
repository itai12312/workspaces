public class TreeNode<T>{
int color;
TreeNode<T> parent,left,right;
String key;
T info;
public TreeNode(String key1,T info1) {
	this.key = key1;
	this.info=info1;
	//this.personid = personid;
}
public TreeNode(TreeNode<T> copy){
	this.color=copy.color;
	this.parent=copy.parent;
	this.left=copy.left;
	this.right=copy.right;
	this.key=copy.key;
	this.info=copy.info;
}
public void copy(TreeNode<T> copy){
	this.color=copy.color;
	this.parent=copy.parent;
	this.left=copy.left;
	this.right=copy.right;
	this.key=copy.key;
	this.info=copy.info;
}
public int getColor() {
	return color;
}
public void setColor(int color) {
	this.color = color;
}
public TreeNode<T> getParent() {
	return parent;
}
public void setParent(TreeNode<T> parent) {
	this.parent = parent;
}
public TreeNode<T> getLeft() {
	return left;
}
public void setLeft(TreeNode<T> left) {
	this.left = left;
}
public TreeNode<T> getRight() {
	return right;
}
public void setRight(TreeNode<T> right) {
	this.right = right;
}
public String getKey() {
	return key;
}
public void setKey(String key) {
	this.key = key;
}
public T getInfo() {
	return info;
}
public void setInfo(T info) {
	this.info = info;
}
}