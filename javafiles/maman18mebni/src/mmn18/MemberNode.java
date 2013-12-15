package mmn18;

public class MemberNode
{
    private Member member;
    private MemberNode left,right,parent;
    private int color;

    public MemberNode(Member _member)
    {
        member=_member;
        left=null;
        right=null;
        parent=null;
        color=0;
    }

    /*
     * Returns the parent of the node
     * @return parent
     */
    public MemberNode getParent()
    {
        return parent;
    }

    /*
     * Returns the left child of the node
     * @return left child
     */
    public MemberNode getLeft()
    {
        return left;
    }

    /*
     * Returns the right child of the node
     * @return right child
     */
    public MemberNode getRight()
    {
        return right;
    }

    /*
     * Returns the member object of the node
     * @return member object
     */
    public Member getmember()
    {
        return member;
    }

    /*
     * Returns the color of the node
     * @return 1 if the color is red, otherwise 0 if it's black
     */
    public int getColor()
    {
        return color;
    }

    /*
     * Returns the grand parent of the node
     * @return grand parent
     */
    public MemberNode getGrandPa()
    {
    	if(parent!=null&&parent.getParent()!=null){
    		return parent.getParent();
    	}
    	return null;
    }

    /*
     * Replaces the parent of the node with a new one
     * @param _parent the new parent
     */
    public void setParent(MemberNode _parent)
    {
        parent=_parent;
    }

    /*
     * Replaces the right child of the node with a new one
     * @param _right the new right child
     */
    public void setRight(MemberNode _right)
    {
        right=_right;
    }

    /*
     * Replaces the left child of the node with a new one
     * @param _left the new left child
     */
    public void setLeft(MemberNode _left)
    {
        left=_left;
    }
    
    /*
     * Replaces the color of the node with a new one
     * @param _color the new color
     */
    public void setColor(int _color)
    {
        color=_color;
    }
    
    /*
     * Replaces the member object of the node with a new one
     * @param _member the new member object
     */
    public void setmember(Member _member)
    {
        member=_member;
    }


}
