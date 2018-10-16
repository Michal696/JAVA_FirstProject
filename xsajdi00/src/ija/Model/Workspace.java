
package ija.Model;

import javafx.scene.shape.Line;
import java.util.ArrayList;
import java.util.List;
import java.lang.*;

public class Workspace {

    public List<Block> blocks = new ArrayList<>();
    public List<Link> links = new ArrayList<>();

    private int blockIdEffectOff = -1;

    public Block getBlock(int id) throws NullPointerException {
        for (Block b : blocks) {
            if (b.getID() == id) {
                return b;
            }
        }
        throw new NullPointerException();
    }

    public void addBlock(Block b) {
        try{
            this.getBlock(b.getID());
        }catch (Exception e){
            blocks.add(b);
        }
    }
    public void deleteBlock(Block b){
        blocks.remove(b);
    }

    public Link getLink(int linkID) {
        for (Link l : links) {
            if(l.getLinkID() == linkID){
                return l;
            }
        }
        throw new RuntimeException();
    }

    public void addLink(Link l, int y2) {
        try{
            this.getLink(l.getLinkID());
        }catch (Exception e){
            Position pos = getBlock(l.getLeftBlockID()).getPosition();
            Position endPos = getBlock(l.getRightBlockID()).getPosition();
            l.line = new Line(pos.getX()+97,pos.getY()+50,endPos.getX()+3,endPos.getY()+21+y2);
            l.line.setId(Integer.toString(l.getLinkID()));
            l.line.setStrokeWidth(5);
            links.add(l);


        }
    }
    public void deleteLink(Link l){
        links.remove(l);
    }

    public void clearLists(){
        blocks.clear();
        links.clear();
    }


    //expecting all values
    public void highLightBlock(int blockID){
       if(blockIdEffectOff != -1){
           getBlock(blockIdEffectOff).highLightBlockOff();
       }
        getBlock(blockID).highLightBlockOn();
       blockIdEffectOff = blockID;

    }
    public void resetAllBlocks(){
        for (Block b: blocks) {
            if(b.getType() != 0){
                b.hasData = false;
                b.hasPortA = false;
                b.hasPortB = false;
                b.highLightBlockOff();
            }
            if(b.getType() == -1){
                b.label.setText("Refreshed");
            }
        }
    }
    public void resetAllLinksUsages(){
        for (Link l: links) {
            l.reUse();
        }
    }
    public void resetSceneCalculations(){
        resetAllBlocks();
        resetAllLinksUsages();
    }
    public double performBlock(int blockID) throws RuntimeException, IllegalAccessException {

        double portA = getBlock(blockID).getPortA();
        double portB = getBlock(blockID).getPortB();
        int type = getBlock(blockID).getType();
        if (type == 1){ //+
            return getBlock(blockID).updateD(portA + portB);
        }
        if (type == 2){ //-
            return getBlock(blockID).updateD(portA - portB);
        }
        if (type == 3){ //*
            return getBlock(blockID).updateD(portA * portB);
        }
        if (type == 4){ ///
            if (portB == 0.0) {
                throw new RuntimeException();//div 0
            }
            return getBlock(blockID).updateD(portA / portB);
        }
        if(type == 5){
            return getBlock(blockID).updateD(Math.pow(portA,portB));
        }
        throw new IllegalAccessException(); // wrong type

    }


}
