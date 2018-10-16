
package ija.Model;
import javafx.scene.shape.Line;

public class Link extends Line{
    private int linkID;
    private int leftBlockID;
    private int rightBlockID;
    private boolean notUsed;

    public Line line;

    public Link(int linkID, int leftBlockID, int rightBlockID){
        this.linkID = linkID;
        this.leftBlockID = leftBlockID;
        this.rightBlockID = rightBlockID;
        this.notUsed = true;
    }


    public void gotUsed(){notUsed = false;}
    public boolean notUsed(){return notUsed;}
    protected void reUse(){notUsed = true;}

    public int getLinkID() {
        return linkID;
    }

    public int getLeftBlockID() {
        return leftBlockID;
    }

    public int getRightBlockID() {
        return rightBlockID;
    }

}
