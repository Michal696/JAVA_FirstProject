
package ija;
import ija.Model.*;

import javafx.scene.shape.Line;
import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.input.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainWindow extends Application
{
    Workspace w = new Workspace();
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    static int newBlockId = 0;
    int actualID = -1;
    Group root = new Group();

    boolean canConnect = false;
    int lastID = -1;
    int newLinkID;

    boolean move = true;
    boolean connect = false;
    boolean remove = false;

    Button button_new = new Button("New");
    Button button_open = new Button("Open");
    Button button_save = new Button("Save");
    Button button_close = new Button("Close");

    Button button_in = new Button("Input");
    Button button_add = new Button("Addition");
    Button button_sub = new Button("Substraction");
    Button button_mul = new Button("Multiplication");
    Button button_div = new Button("Division");
    Button button_exp = new Button("Exponentiation");
    Button button_out = new Button("Output");

    Button button_calc = new Button("Calculate");
    Button button_step = new Button("Step");
    Button button_reset = new Button("Reset");
    Button button_move = new Button("Move");
    Button button_connect = new Button("Connect");
    Button button_remove = new Button("Remove");
    Label labelValue = new Label(" Value of line: x");

    HBox hBox = new HBox();
    HBox hBox2 = new HBox();
    HBox hBox3 = new HBox();
    VBox vBox = new VBox();

    String block_scheme = "";
    Desktop desktop = Desktop.getDesktop();

    @Override
    public void start(Stage primaryStage) {

        add_buttons();

        button_save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                block_scheme = "";
                int countOfOutLinks;
                for (Block item : w.blocks) {
                    countOfOutLinks = 0;
                    for (Integer i: item.portOutLinkListId) {
                        countOfOutLinks = countOfOutLinks + 1;
                    }
                    block_scheme = block_scheme +
                            String.valueOf(item.getID()) + "$" +
                            String.valueOf(item.getType()) + "$" +
                            String.valueOf(item.hasPortAconnected) + "$" +
                            String.valueOf(item.hasPortBconnected) + "$" +
                            String.valueOf(item.portAlinkID) + "$" +
                            String.valueOf(item.portBlinkID) + "$" +
                            String.valueOf(countOfOutLinks);

                    for (Integer i: item.portOutLinkListId) {
                        block_scheme = block_scheme + "$" +
                                String.valueOf(i);
                    }
                    block_scheme = block_scheme + "$" +
                                String.valueOf(item.rectangle.getTranslateX()) + "$" +
                                String.valueOf(item.rectangle.getTranslateY()) + "$" +
                                String.valueOf(item.portData_rec.getTranslateX()) + "$" +
                                String.valueOf(item.portData_rec.getTranslateY()) + "$" +
                                String.valueOf(item.onClickRectangle.getTranslateX()) + "$" +
                                String.valueOf(item.onClickRectangle.getTranslateY());

                    if (item.getType() == 0){ // data
                        block_scheme = block_scheme + "$" +
                                String.valueOf(item.getBlockValue()) + "$" +
                                String.valueOf(item.textField.getTranslateX()) + "$" +
                                String.valueOf(item.textField.getTranslateY());
                    }
                    else{
                        if (item.getType() == -1){
                            block_scheme = block_scheme + "$" +
                                    String.valueOf(item.label.getTranslateX()) + "$" +
                                    String.valueOf(item.label.getTranslateY());
                        }else{//type > 0
                            block_scheme = block_scheme + "$" +
                                    String.valueOf(item.portA_rec.getTranslateX()) + "$" +
                                    String.valueOf(item.portA_rec.getTranslateY()) + "$" +
                                    String.valueOf(item.portB_rec.getTranslateX()) + "$" +
                                    String.valueOf(item.portB_rec.getTranslateY()) + "$" +
                                    String.valueOf(item.label.getTranslateX()) + "$" +
                                    String.valueOf(item.label.getTranslateY());


                        }

                    }
                    block_scheme = block_scheme + "@";//end of block
                }
                block_scheme = block_scheme + "%@";
                for (Link itemL : w.links) {
                    block_scheme = block_scheme +
                            String.valueOf(itemL.getLinkID()) + "$" +
                            String.valueOf(itemL.getLeftBlockID()) + "$" +
                            String.valueOf(itemL.getRightBlockID()) + "$" +
                            String.valueOf(itemL.line.getStartX()) + "$" +
                            String.valueOf(itemL.line.getStartY()) + "$" +
                            String.valueOf(itemL.line.getEndX()) + "$" +
                            String.valueOf(itemL.line.getEndY()) + "@";
                }

                FileChooser fileChooser = new FileChooser();

                //Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);

                //Show save file dialog
                File file = fileChooser.showSaveDialog(primaryStage);

                if (file != null) {
                    SaveFile(block_scheme, file);
                }
            }
        });

        button_open.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();

                //Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);

                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    root.getChildren().clear();
                    w.clearLists();
                    root.getChildren().add(vBox);
                    labelValue.setText(" Value of line: x");
                    newBlockId = 0;
                    move = true;
                    connect = false;
                    remove = false;
                    readFile(file);
                }
            }
        });

        primaryStage.setMaximized(true);

        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(root, 900,350));
        primaryStage.setTitle("BlockScene");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void SaveFile(String content, File file){
        try {
            FileWriter fileWriter = null;

            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void readFile(File file) {
        String[] split_text;
        BufferedReader bufferedReader = null;
        boolean flag = false;
        int countOfOutLinks;
        try {

            bufferedReader = new BufferedReader(new FileReader(file));
            String fileText = bufferedReader.readLine();
            String line = fileText;
            int max = line.length() - line.replace("@", "").length();
            String[] splitFileText= fileText.split("@");
            System.out.print("\n"+String.valueOf(max));
            System.out.print("\n"+fileText+"\n");
            String text;
            int type;
            int i;
            int j = 0;
            while ( j < max) {
                text = splitFileText[j];
                j = j+1;
                i = 0;
                if (text.equals("%")) {
                    flag = true;
                    continue;
                }
                split_text = text.split("\\$");
                if (flag == false) { //block
                    newBlockId = Integer.parseInt(split_text[0]);
                    type = Integer.parseInt(split_text[1]);
                    w.addBlock(new Block(newBlockId, type));

                    w.getBlock(newBlockId).onClickRectangle.setOnMousePressed(rectangleOnMousePressedEventHandler);
                    w.getBlock(newBlockId).onClickRectangle.setOnMouseDragged(rectangleOnMouseDraggedEventHandler);
                    w.getBlock(newBlockId).hasPortAconnected = Boolean.valueOf(split_text[2]);
                    w.getBlock(newBlockId).hasPortBconnected = Boolean.valueOf(split_text[3]);
                    w.getBlock(newBlockId).portAlinkID = Integer.parseInt(split_text[4]);
                    w.getBlock(newBlockId).portBlinkID = Integer.parseInt(split_text[5]);
                    countOfOutLinks = Integer.parseInt(split_text[6]);
                    while(i < countOfOutLinks){
                        i = i + 1;
                        w.getBlock(newBlockId).portOutLinkListId.add(Integer.parseInt(split_text[i+6]));
                    }
                    w.getBlock(newBlockId).rectangle.setTranslateX(Double.valueOf(split_text[i+7]));
                    w.getBlock(newBlockId).rectangle.setTranslateY(Double.valueOf(split_text[i+8]));

                    w.getBlock(newBlockId).portData_rec.setTranslateX(Double.valueOf(split_text[i+9]));
                    w.getBlock(newBlockId).portData_rec.setTranslateY(Double.valueOf(split_text[i+10]));

                    w.getBlock(newBlockId).onClickRectangle.setTranslateX(Double.valueOf(split_text[i+11]));
                    w.getBlock(newBlockId).onClickRectangle.setTranslateY(Double.valueOf(split_text[i+12]));

                    if (type == 0) { //i+13
                        w.getBlock(newBlockId).updateD(Double.valueOf(split_text[i+13]));
                        w.getBlock(newBlockId).textField.setText(split_text[i+13]);
                        w.getBlock(newBlockId).textField.setTranslateX(Double.valueOf(split_text[i+14]));
                        w.getBlock(newBlockId).textField.setTranslateY(Double.valueOf(split_text[i+15]));


                    } else {
                        if (type == -1){
                            w.getBlock(newBlockId).label.setTranslateX(Double.valueOf(split_text[i+13]));
                            w.getBlock(newBlockId).label.setTranslateY(Double.valueOf(split_text[i+14]));
                        }else{ // type > 0
                            w.getBlock(newBlockId).portA_rec.setTranslateX(Double.valueOf(split_text[i+13]));
                            w.getBlock(newBlockId).portA_rec.setTranslateY(Double.valueOf(split_text[i+14]));
                            w.getBlock(newBlockId).portB_rec.setTranslateX(Double.valueOf(split_text[i+15]));
                            w.getBlock(newBlockId).portB_rec.setTranslateY(Double.valueOf(split_text[i+16]));
                            w.getBlock(newBlockId).label.setTranslateX(Double.valueOf(split_text[i+17]));
                            w.getBlock(newBlockId).label.setTranslateY(Double.valueOf(split_text[i+18]));
                        }
                    }

                    root.getChildren().add(w.getBlock(newBlockId).rectangle);
                    if (type > 0){
                        root.getChildren().add(w.getBlock(newBlockId).portA_rec);
                        root.getChildren().add(w.getBlock(newBlockId).portB_rec);
                    }
                    root.getChildren().add(w.getBlock(newBlockId).portData_rec);
                    if (type == 0){
                        root.getChildren().add(w.getBlock(newBlockId).onClickRectangle);
                        root.getChildren().add(w.getBlock(newBlockId).textField);
                    }else {
                        root.getChildren().add(w.getBlock(newBlockId).label);
                        root.getChildren().add(w.getBlock(newBlockId).onClickRectangle);
                    }
                }
                else { // link
                    newLinkID = Integer.parseInt(split_text[0]);
                    Link link = new Link(newLinkID,Integer.parseInt(split_text[1]),Integer.parseInt(split_text[2]));

                    Position pos = w.getBlock(link.getLeftBlockID()).getPosition();
                    Position endPos = w.getBlock(link.getRightBlockID()).getPosition();
                    link.line = new Line(pos.getX()+97,pos.getY()+50,endPos.getX()+3,endPos.getY()+21+29);


                    link.line.setStartX(0+Double.valueOf(split_text[3]));
                    link.line.setStartY(0+Double.valueOf(split_text[4]));
                    link.line.setEndX(0+Double.valueOf(split_text[5]));
                    link.line.setEndY(0+Double.valueOf(split_text[6]));

                    link.line.setId(Integer.toString(newLinkID));
                    link.line.setStrokeWidth(5);
                    w.links.add(link);
                    w.getLink(newLinkID).line.setOnMouseMoved(linkOnMouseHoverEventHandler);
                    root.getChildren().add(w.getLink(newLinkID).line);
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }



    private void add_buttons() {
        hBox.getChildren().addAll(button_new, button_open, button_save, button_close, labelValue);
        hBox2.getChildren().addAll(button_in, button_add, button_sub, button_mul, button_div, button_exp, button_out);
        hBox3.getChildren().addAll(button_calc, button_step, button_reset, button_move, button_connect, button_remove);

        vBox.getChildren().addAll(hBox, hBox2, hBox3);
        root.getChildren().add(vBox);

        button_reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                w.resetSceneCalculations();
            }
        });

        button_calc.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                move = false;
                connect = false;
                remove = false;
                if(actualID != -1)
                    w.getBlock(actualID).onClickRectangle.setStrokeWidth(2);
                w.resetSceneCalculations();
                // ^all values to default^ -> block,link
                int id1;
                int id2;
                double value;
                boolean effectiveRun = true;
                while(effectiveRun) {
                    effectiveRun = false;
                    for (Link l: w.links) {
                        id1 = l.getLeftBlockID();
                        id2 = l.getRightBlockID();
                        if (w.getBlock(id1).hasData()) {
                            if(l.notUsed()) {
                                l.gotUsed();
                                effectiveRun = true;
                                value = w.getBlock(id1).getBlockValue();
                                if (w.getBlock(id2).isHasPortA()) {
                                    w.getBlock(id2).setPortB(value);
                                    try {
                                        w.performBlock(id2);
                                    } catch (RuntimeException e) {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Error Dialog");
                                        alert.setHeaderText("Error");
                                        alert.setContentText("Division by zero");
                                        alert.showAndWait();
                                        return;
                                    } catch (Exception e){
                                        System.out.println("Exception occurred - performBlock");
                                        return;
                                    }
                                }
                                w.getBlock(id2).setPortA(value);
                                if(w.getBlock(id2).getType() == -1){
                                    w.getBlock(id2).updateD(w.getBlock(id2).getPortA());
                                    w.getBlock(id2).label.setText(Double.toString(w.getBlock(id2).getBlockValue()));
                                }
                            }
                        }
                    }
                }
                for (Block b: w.blocks) {
                    value = b.getBlockValue();
                    System.out.print(b.getID()+ ":" + value + "\n");
                }
            }
        });

        button_step.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                move = false;
                connect = false;
                remove = false;
                if(actualID != -1)
                    w.getBlock(actualID).onClickRectangle.setStrokeWidth(2);
                // ^all values to default^ -> block,link
                int id1;
                int id2;
                double value;


                for (Link l: w.links) {
                    id1 = l.getLeftBlockID();
                    id2 = l.getRightBlockID();
                    if (w.getBlock(id1).hasData()) {
                        if(l.notUsed()) {
                            l.gotUsed();
                            value = w.getBlock(id1).getBlockValue();
                            if (w.getBlock(id2).isHasPortA()) {
                                w.getBlock(id2).setPortB(value);
                                try {
                                    w.performBlock(id2);
                                } catch (RuntimeException e) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Error Dialog");
                                    alert.setHeaderText("Error");
                                    alert.setContentText("Division by zero");
                                    alert.showAndWait();
                                } catch (Exception e){
                                    System.out.println("Exception occurred - performBlock");
                                    return;
                                }
                                //performed
                                w.highLightBlock(id2);
                                break;
                            }else{
                                w.getBlock(id2).setPortA(value);
                                if(w.getBlock(id2).getType() == -1){
                                    w.getBlock(id2).updateD(w.getBlock(id2).getPortA());
                                    w.getBlock(id2).label.setText(Double.toString(w.getBlock(id2).getBlockValue()));
                                    w.highLightBlock(id2);
                                    break;
                                }
                            }
                        }
                    }
                }

                // debug
                for (Block b: w.blocks) {
                    value = b.getBlockValue();
                    System.out.print(b.getID()+ ":" + value + "\n");
                }
            }
        });
        
        button_in.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setRect(0);
                //if(lastID != -1)
                    //w.getBlock(lastID).onClickRectangle.setStrokeWidth(2);
            }
        });

        button_add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setRect(1);
                //if(lastID != -1)
                    //w.getBlock(lastID).onClickRectangle.setStrokeWidth(2);
            }
        });

        button_sub.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setRect(2);
                //if(lastID != -1)
                    //w.getBlock(lastID).onClickRectangle.setStrokeWidth(2);
            }});

        button_mul.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setRect(3);
                //if(lastID != -1)
                    //w.getBlock(lastID).onClickRectangle.setStrokeWidth(2);
            }
        });

        button_div.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setRect(4);
                //if(lastID != -1)
                    //w.getBlock(lastID).onClickRectangle.setStrokeWidth(2);
            }
        });

        button_exp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setRect(5);
                //if(lastID != -1)
                    //w.getBlock(lastID).onClickRectangle.setStrokeWidth(2);
            }
        });

        button_out.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setRect(-1);
                //if(lastID != -1)
                    //w.getBlock(lastID).onClickRectangle.setStrokeWidth(2);
            }
        });

        button_move.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                move = true;
                connect = false;
                remove = false;
                //if(lastID != -1)
                    //w.getBlock(lastID).onClickRectangle.setStrokeWidth(2);
            }
        });

        button_connect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                move = false;
                connect = true;
                remove = false;
                //if(lastID != -1)
                    //w.getBlock(lastID).onClickRectangle.setStrokeWidth(2);
            }
        });

        button_remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                move = false;
                connect = false;
                remove = true;
            }

        });

        button_new.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                root.getChildren().clear();
                w.clearLists();
                root.getChildren().add(vBox);
                labelValue.setText(" Value of line: x");
                newBlockId = 0;
                move = true;
                connect = false;
                remove = false;
            }
        });

        button_close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });

    }

    private void setRect(int c) {
        newBlockId++;
        w.addBlock(new Block(newBlockId, c));

        w.getBlock(newBlockId).onClickRectangle.setCursor(Cursor.HAND);
        w.getBlock(newBlockId).onClickRectangle.setOnMousePressed(rectangleOnMousePressedEventHandler);
        w.getBlock(newBlockId).onClickRectangle.setOnMouseDragged(rectangleOnMouseDraggedEventHandler);

        root.getChildren().add(w.getBlock(newBlockId).rectangle);
        if (c > 0){
            root.getChildren().add(w.getBlock(newBlockId).portA_rec);
            root.getChildren().add(w.getBlock(newBlockId).portB_rec);
        }
        root.getChildren().add(w.getBlock(newBlockId).portData_rec);
        if (c == 0){
            root.getChildren().add(w.getBlock(newBlockId).onClickRectangle);
            root.getChildren().add(w.getBlock(newBlockId).textField);
        }else {
            root.getChildren().add(w.getBlock(newBlockId).label);
            root.getChildren().add(w.getBlock(newBlockId).onClickRectangle);
        }

    }

    EventHandler<MouseEvent> linkOnMouseHoverEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    actualID = Integer.parseInt(((Line) (t.getSource())).getId());
                    double value = w.getBlock(w.getLink(actualID).getLeftBlockID()).getBlockValue();
                    labelValue.setText(" Value of line: "+String.valueOf(value));
                }
            };
    EventHandler<MouseEvent> rectangleOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    actualID = Integer.parseInt(((Rectangle) (t.getSource())).getId());
                    int tmpLinkId;
                    int tmpBlockId;
                    Link tmpLink;
                    Block tmpBlock;
                    int tmpId;
                    if (move == true) {
                        orgSceneX = t.getSceneX();
                        orgSceneY = t.getSceneY();
                        orgTranslateX = ((Rectangle) (t.getSource())).getTranslateX();
                        orgTranslateY = ((Rectangle) (t.getSource())).getTranslateY();
                    }
                    if (remove == true) {
                        try {
                            actualID = Integer.parseInt(((Rectangle) (t.getSource())).getId());
                            int block_type = w.getBlock(actualID).getType();
                            root.getChildren().remove(w.getBlock(actualID).rectangle);
                            if (block_type > 0) {
                                root.getChildren().remove(w.getBlock(actualID).portA_rec);
                                root.getChildren().remove(w.getBlock(actualID).portB_rec);
                                if (w.getBlock(actualID).hasPortAconnected) {
                                    tmpLinkId = w.getBlock(actualID).portAlinkID;
                                    tmpLink = w.getLink(tmpLinkId);
                                    tmpBlockId = tmpLink.getLeftBlockID();
                                    tmpBlock = w.getBlock(tmpBlockId);
                                    tmpBlock.portOutLinkListId.remove((Integer) tmpLinkId);
                                    w.links.remove(tmpLink);
                                    root.getChildren().remove(tmpLink.line);
                                }
                                if (w.getBlock(actualID).hasPortBconnected) {
                                    tmpLinkId = w.getBlock(actualID).portBlinkID;
                                    tmpLink = w.getLink(tmpLinkId);
                                    tmpBlockId = tmpLink.getLeftBlockID();
                                    tmpBlock = w.getBlock(tmpBlockId);
                                    tmpBlock.portOutLinkListId.remove((Integer) tmpLinkId);
                                    w.links.remove(tmpLink);
                                    root.getChildren().remove(tmpLink.line);
                                }
                                for (int ID : w.getBlock(actualID).portOutLinkListId) {
                                    tmpLink = w.getLink(ID);
                                    tmpBlockId = tmpLink.getRightBlockID();
                                    tmpBlock = w.getBlock(tmpBlockId);

                                    if (tmpBlock.hasPortAconnected)
                                        if (tmpBlock.portAlinkID == ID) {
                                            tmpBlock.portAlinkID = -1;
                                            tmpBlock.hasPortAconnected = false;
                                        }
                                    if (tmpBlock.hasPortBconnected)
                                        if (tmpBlock.portBlinkID == ID) {
                                            tmpBlock.portBlinkID = -1;
                                            tmpBlock.hasPortBconnected = false;
                                        }

                                    root.getChildren().remove(tmpLink.line);
                                    w.links.remove(tmpLink);
                                }
                            }
                            root.getChildren().remove(w.getBlock(actualID).portData_rec);
                            root.getChildren().remove(w.getBlock(actualID).label);
                            root.getChildren().remove(w.getBlock(actualID).onClickRectangle);
                            if (block_type == 0) {
                                for (int ID : w.getBlock(actualID).portOutLinkListId) {
                                    tmpLink = w.getLink(ID);
                                    tmpBlockId = tmpLink.getRightBlockID();
                                    tmpBlock = w.getBlock(tmpBlockId);
                                    if (tmpBlock.hasPortAconnected)
                                        if (tmpBlock.portAlinkID == ID) {
                                            tmpBlock.portAlinkID = -1;
                                            tmpBlock.hasPortAconnected = false;
                                        }
                                    if (tmpBlock.hasPortBconnected)
                                        if (tmpBlock.portBlinkID == ID) {
                                            tmpBlock.portBlinkID = -1;
                                            tmpBlock.hasPortBconnected = false;
                                        }

                                    root.getChildren().remove(tmpLink.line);
                                    w.links.remove(tmpLink);
                                }
                                root.getChildren().remove(w.getBlock(actualID).onClickRectangle);
                                root.getChildren().remove(w.getBlock(actualID).textField);
                            } else {
                                if (w.getBlock(actualID).hasPortAconnected) {
                                    tmpLinkId = w.getBlock(actualID).portAlinkID;
                                    tmpLink = w.getLink(tmpLinkId);
                                    tmpBlockId = tmpLink.getLeftBlockID();
                                    tmpBlock = w.getBlock(tmpBlockId);
                                    tmpBlock.portOutLinkListId.remove((Integer) tmpLinkId);
                                    w.links.remove(tmpLink);
                                    root.getChildren().remove(tmpLink.line);
                                }
                                root.getChildren().remove(w.getBlock(actualID).label);
                                root.getChildren().remove(w.getBlock(actualID).onClickRectangle);
                            }

                            w.deleteBlock(w.getBlock(actualID));
                        }catch(Exception e){

                        }
                    }
                    if (connect){
                        if(lastID != -1){
                            w.getBlock(lastID).highLightBlockOff();
                        }
                        actualID = Integer.parseInt(((Rectangle) (t.getSource())).getId());
                        int block_type = w.getBlock(actualID).getType();

                        if (actualID == lastID){
                            canConnect = false;
                        }
                        if(block_type == 0){
                            canConnect = false;
                        }
                        if(lastID == -1){
                            canConnect = false;
                        }

                        if(w.getBlock(actualID).hasPortAconnected && w.getBlock(actualID).hasPortBconnected){
                            canConnect = false;
                        }
                        if(block_type == -1 && w.getBlock(actualID).hasPortAconnected) {
                            canConnect = false;
                        }
                        if((lastID != -1) && (w.getBlock(lastID).getType() == -1)) {
                                canConnect = false;

                        }
                        if(canConnect){
                            //setLink();
                            newLinkID++;
                            int yAdjustment=0;
                            if(w.getBlock(actualID).hasPortAconnected){
                                yAdjustment = 59;
                                w.getBlock(actualID).hasPortBconnected = true;
                                w.getBlock(actualID).portBlinkID = newLinkID;
                            }else{
                                if(block_type == -1){
                                    yAdjustment = 28;
                                }
                                w.getBlock(actualID).hasPortAconnected = true;
                                w.getBlock(actualID).portAlinkID = newLinkID;
                            }

                            w.addLink(new Link(newLinkID,lastID,actualID),yAdjustment);
                            w.getLink(newLinkID).line.setOnMouseMoved(linkOnMouseHoverEventHandler);

                            w.getBlock(lastID).portOutLinkListId.add(newLinkID);

                            canConnect = false;
                            //turnEffectOff(lastID);
                            w.getBlock(lastID).onClickRectangle.setStrokeWidth(2);
                            lastID = -1;
                            root.getChildren().add(w.getLink(newLinkID).line);
                        }
                        else{

                            if (actualID == lastID){
                                canConnect = false;
                                //turnEffectOff(lastID);
                                w.getBlock(lastID).onClickRectangle.setStrokeWidth(2);
                            }else{
                                canConnect = true;
                                //turnEffectOn(actualID);
                                w.getBlock(actualID).onClickRectangle.setStrokeWidth(5);
                            }
                            lastID = actualID;
                        }


                    }


                }
            };

    EventHandler<MouseEvent> rectangleOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    if (move == true) {
                        double offsetX = t.getSceneX() - orgSceneX;
                        double offsetY = t.getSceneY() - orgSceneY;
                        double newTranslateX = orgTranslateX + offsetX;
                        double newTranslateY = orgTranslateY + offsetY;

                        ((Rectangle) (t.getSource())).setTranslateX(newTranslateX);
                        ((Rectangle) (t.getSource())).setTranslateY(newTranslateY);
                        actualID = Integer.parseInt(((Rectangle) (t.getSource())).getId());
                        moveBlockToPosition(actualID,newTranslateX, newTranslateY);
                    }
                }
            };

    private void moveBlockToPosition(int blockID,double x, double y){
        w.getBlock(blockID).rectangle.setTranslateX(x);
        w.getBlock(blockID).rectangle.setTranslateY(y);
        if (w.getBlock(blockID).getType() > 0) {
            w.getBlock(blockID).portA_rec.setTranslateX(x);
            w.getBlock(blockID).portA_rec.setTranslateY(y);
            w.getBlock(blockID).portB_rec.setTranslateX(x);
            w.getBlock(blockID).portB_rec.setTranslateY(y);
        }
        w.getBlock(blockID).portData_rec.setTranslateX(x);
        w.getBlock(blockID).portData_rec.setTranslateY(y);
        if(w.getBlock(blockID).getType() != 0) {
            w.getBlock(blockID).label.setTranslateX(x);
            w.getBlock(blockID).label.setTranslateY(y);
        }
        if(w.getBlock(blockID).getType() == 0){
            w.getBlock(blockID).textField.setTranslateX(x);
            w.getBlock(blockID).textField.setTranslateY(y);
        }
        if(w.getBlock(blockID).hasPortAconnected) {
            if(w.getBlock(blockID).getType() == -1){
                w.getLink(w.getBlock(blockID).portAlinkID).line.setEndX(x+103);
                w.getLink(w.getBlock(blockID).portAlinkID).line.setEndY(y+149);
            }else {
                w.getLink(w.getBlock(blockID).portAlinkID).line.setEndX(x + 103);
                w.getLink(w.getBlock(blockID).portAlinkID).line.setEndY(y + 121);
            }
        }
        if(w.getBlock(blockID).hasPortBconnected){
            w.getLink(w.getBlock(blockID).portBlinkID).line.setEndX(x+103);
            w.getLink(w.getBlock(blockID).portBlinkID).line.setEndY(y+180);
        }
        for (int linkID :w.getBlock(blockID).portOutLinkListId) {
            w.getLink(linkID).line.setStartX(x+197);
            w.getLink(linkID).line.setStartY(y+150);
        }

        w.getBlock(blockID).setPosition(x+100,y+100);
    }

}
