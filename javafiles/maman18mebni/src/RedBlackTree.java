public class RedBlackTree<T>{
	
	private void leftrotate(TreeNode<T> x,TreeNode<T> root){
	TreeNode<T> y=x.getRight();
	x.setRight(y.getLeft());
	if(y.getLeft()!=null)
		y.getLeft().setParent(x);
	y.setParent(x.getParent());
	if(x.getParent()==null)
		root=y;
	else if(x==x.getParent().getLeft())
		x.getParent().setLeft(y);
	else
		x.getParent().setRight(y);
	y.setLeft(x);
	x.setParent(y);
	}
	
	private void rightrotate(TreeNode<T> x,TreeNode<T> root){
		TreeNode<T> y=x.getLeft();
		x.setLeft(y.getRight());
		if(y.getRight()!=null)
			y.getRight().setParent(x);
		y.setParent(x.getParent());
		if(x.getParent()==null)
			root=y;
		else if(x==x.getParent().getRight())
			x.getParent().setRight(y);
		else
			x.getParent().setLeft(y);
		y.setRight(x);
		x.setParent(y);
		}

	private void insert(TreeNode<T> z,TreeNode<T> root){
		TreeNode<T> y=null;
		TreeNode<T> x=root;
		while(x!=null){
			y.copy(x);
			if(x.getKey().compareTo(z.getKey())>0)
				x.copy(x.left);
			else
				x.copy(x.right);
				
		}
		z.setParent(y);
		
	}
}