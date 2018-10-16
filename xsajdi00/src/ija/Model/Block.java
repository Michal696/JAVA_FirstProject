
package ija.Model;

import java.util.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Block extends Rectangle{

    public int id;
    public int type;   //-1textOut,0textIn,1+,2-,3*,4/,5^
    public Data data = new Data();
    public Position position = new Position();
    public boolean hasData;
    public boolean hasPortA;
    public boolean hasPortB;

    public double portA;
    public double portB;


    public boolean hasPortAconnected;
    public boolean hasPortBconnected;
    public int portAlinkID;
    public int portBlinkID;
    public List<Integer> portOutLinkListId = new ArrayList<>();



    public Rectangle rectangle;
    public Rectangle portA_rec;
    public Rectangle portB_rec;
    public Rectangle portData_rec;
    public Rectangle onClickRectangle;
    public Label label;
    public TextField textField;

    public Block (int id, int type, double x, double y){ //open file
        this.id = id;
        this.type = type;
        setPosition(x,y);
        hasData = false;
        hasPortA = false;
        hasPortB = false;
        hasPortAconnected = false;
        hasPortBconnected = false;
        if(type > 0) { //6
            portA_rec = new Rectangle(position.getX() + 2, position.getY() + 10, 20, 20);
            portB_rec = new Rectangle(position.getX() + 2, position.getY() + 70, 20, 20);
            portA_rec.setFill(Color.GREEN);
            portB_rec.setFill(Color.GREEN);
            portData_rec = new Rectangle(position.getX() + 78, position.getY() + 40, 20, 20);
            portData_rec.setFill(Color.GREEN);
            rectangle = new Rectangle(position.getX(), position.getY(), 100, 100);
            rectangle.setFill(Color.RED);
            if(type == 1){
                label = new Label("a+b");
            }
            if(type == 2) {
                label = new Label("a-b");
            }
            if(type == 3) {
                label = new Label("a*b");
            }
            if(type == 4) {
                label = new Label("a/b");
            }
            if(type == 5) {
                label = new Label("a^b");
            }
            label.setLayoutX(position.getX() + 35);
            label.setLayoutY(position.getY() + 40);
            onClickRectangle = new Rectangle(position.getX(), position.getY(), 100, 100);
            onClickRectangle.setFill(Color.TRANSPARENT);
            onClickRectangle.setStroke(Color.BLACK);
            onClickRectangle.setId(Integer.toString(id));
            onClickRectangle.setStrokeWidth(2);
            //        System.out.print(onClickRectangle.getId()+"\n");
        }
        if(type == 0){ //4
            rectangle = new Rectangle(position.getX(), position.getY(), 100, 100);
            rectangle.setFill(Color.RED);
            portData_rec = new Rectangle(position.getX() + 78, position.getY() + 40, 20, 20);
            portData_rec.setFill(Color.GREEN);

            textField = new TextField("0");
            data.updateData(Double.parseDouble("0"));
            setHasValueOn();
            textField.setLayoutX(position.getX());
            textField.setLayoutY(position.getX());
            textField.setMaxWidth(100);

            textField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable,
                                    String oldValue, String newValue) {
                    boolean parse;
                    parse = true;

                    try {Double.parseDouble(newValue);}
                    catch(Exception e){
                        parse = false;
                        setHasValueOff();
                    }
                    if(parse){
                        data.updateData(Double.parseDouble(newValue));
                        setHasValueOn();
                        System.out.println("Parsed");
                        System.out.println(data.getValue());
                    }
                    System.out.println(" Text Changed to  " + newValue + "\n");
                }
            });

            onClickRectangle = new Rectangle(position.getX(), position.getY(), 100, 100);
            onClickRectangle.setFill(Color.TRANSPARENT);
            onClickRectangle.setStroke(Color.BLACK);
            onClickRectangle.setId(Integer.toString(id));
            onClickRectangle.setStrokeWidth(2);
            //        System.out.print(onClickRectangle.getId()+"\n");
        }
        if(type == -1){//4
            rectangle = new Rectangle(position.getX(), position.getY(), 100, 100);
            rectangle.setFill(Color.RED);
            portData_rec = new Rectangle(position.getX() + 2, position.getY() + 40, 20, 20);
            portData_rec.setFill(Color.GREEN);
            label = new Label("Waiting...");
            label.setLayoutX(position.getX() + 25);
            label.setLayoutY(position.getY() + 40);
            onClickRectangle = new Rectangle(position.getX(), position.getY(), 100, 100);
            onClickRectangle.setFill(Color.TRANSPARENT);
            onClickRectangle.setStroke(Color.BLACK);
            onClickRectangle.setId(Integer.toString(id));
            onClickRectangle.setStrokeWidth(2);
            //        System.out.print(onClickRectangle.getId()+"\n");
        }

    }
    public Block(int id, int type) {
        this.id = id;
        this.type = type;
        setPosition(100,100);
        hasData = false;
        hasPortA = false;
        hasPortB = false;
        hasPortAconnected = false;
        hasPortBconnected = false;
        if(type > 0) { //6
            portA_rec = new Rectangle(position.getX() + 2, position.getY() + 10, 20, 20);
            portB_rec = new Rectangle(position.getX() + 2, position.getY() + 70, 20, 20);
            portA_rec.setFill(Color.GREEN);
            portB_rec.setFill(Color.GREEN);
            portData_rec = new Rectangle(position.getX() + 78, position.getY() + 40, 20, 20);
            portData_rec.setFill(Color.GREEN);
            rectangle = new Rectangle(position.getX(), position.getY(), 100, 100);
            rectangle.setFill(Color.RED);
            if(type == 1){
                label = new Label("a+b");
            }
            if(type == 2) {
                label = new Label("a-b");
            }
            if(type == 3) {
                label = new Label("a*b");
            }
            if(type == 4) {
                label = new Label("a/b");
            }
            if(type == 5) {
                label = new Label("a^b");
            }
            label.setLayoutX(position.getX() + 35);
            label.setLayoutY(position.getY() + 40);
            onClickRectangle = new Rectangle(position.getX(), position.getY(), 100, 100);
            onClickRectangle.setFill(Color.TRANSPARENT);
            onClickRectangle.setStroke(Color.BLACK);
            onClickRectangle.setId(Integer.toString(id));
            onClickRectangle.setStrokeWidth(2);
            //        System.out.print(onClickRectangle.getId()+"\n");
        }
        if(type == 0){ //4
            rectangle = new Rectangle(position.getX(), position.getY(), 100, 100);
            rectangle.setFill(Color.RED);
            portData_rec = new Rectangle(position.getX() + 78, position.getY() + 40, 20, 20);
            portData_rec.setFill(Color.GREEN);

            textField = new TextField("0");
            data.updateData(Double.parseDouble("0"));
            setHasValueOn();
            textField.setLayoutX(position.getX());
            textField.setLayoutY(position.getY());
            textField.setMaxWidth(100);

            textField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable,
                                    String oldValue, String newValue) {
                    boolean parse;
                    parse = true;

                    try {Double.parseDouble(newValue);}
                    catch(Exception e){
                        parse = false;
                        setHasValueOff();
                    }
                    if(parse){
                        data.updateData(Double.parseDouble(newValue));
                        setHasValueOn();
                        System.out.println("Parsed");
                        System.out.println(data.getValue());
                    }
                    System.out.println(" Text Changed to  " + newValue + "\n");
                }
            });

            onClickRectangle = new Rectangle(position.getX(), position.getY(), 100, 100);
            onClickRectangle.setFill(Color.TRANSPARENT);
            onClickRectangle.setStroke(Color.BLACK);
            onClickRectangle.setId(Integer.toString(id));
            onClickRectangle.setStrokeWidth(2);
            //        System.out.print(onClickRectangle.getId()+"\n");
        }
        if(type == -1){//4
            rectangle = new Rectangle(position.getX(), position.getY(), 100, 100);
            rectangle.setFill(Color.RED);
            portData_rec = new Rectangle(position.getX() + 2, position.getY() + 40, 20, 20);
            portData_rec.setFill(Color.GREEN);
            label = new Label("Waiting...");
            label.setLayoutX(position.getX() + 25);
            label.setLayoutY(position.getY() + 40);
            onClickRectangle = new Rectangle(position.getX(), position.getY(), 100, 100);
            onClickRectangle.setFill(Color.TRANSPARENT);
            onClickRectangle.setStroke(Color.BLACK);
            onClickRectangle.setId(Integer.toString(id));
            onClickRectangle.setStrokeWidth(2);
            //        System.out.print(onClickRectangle.getId()+"\n");
        }




    }

    public int getID() { return id; }

    public double getPortA(){return portA;}
    public double getPortB(){return portB;}
    public boolean isHasPortA(){return hasPortA;}
    public boolean isHasPortB(){return hasPortB;}
    public void setPortA(double value){ portA = value; hasPortA = true;}
    public void setPortB(double value){ portB = value; hasPortB = true;}

    public double getBlockValue() {
        return data.getValue();
    }

    public boolean hasData(){return hasData;}

    public void setHasValueOn(){
        hasData = true;
    }

    public void setHasValueOff(){
        hasData = false;
    }


    public double updateD(double value) {
        data.updateData(value);
        setHasValueOn();
        return data.getValue();
    }

    public int getType() {
        return type;
    }

    public void setType(int type){
        this.type = type;
    }

    void printState() {
        System.out.println("Sensor id " + id);
            data.printState();
        System.out.println("--");
    }

    // new
    public void setPosition(Position p) {
        this.position = p;
    }


    public void setPosition(double x, double y) {
        this.position.setX(x);
        this.position.setY(y);
    }

    public void highLightBlockOn(){
        onClickRectangle.setStrokeWidth(5);
    }
    public void highLightBlockOff(){
        onClickRectangle.setStrokeWidth(2);
    }


    // new
    public Position getPosition() {
        return this.position;
    }


}

