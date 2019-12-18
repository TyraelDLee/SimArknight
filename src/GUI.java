import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**************************************************************************
 *                                                                        *
 *                         SimArknight ver 2.1                            *
 *                                                                        *
 *                                                                        *
 **************************************************************************/

public class GUI extends Application {
    //-- essential part definition --//
    private final Group root = new Group();
    private static final int BOARD_WIDTH = 1000;
    private static final int BOARD_HEIGHT = 600;
    private static final String imgSrc = "res/img/";
    private static final String imgTyp = ".png";
    private int prob6 = 2, prob5 = 8, prob4 = 50, prob3 = 40;
    private Logic basicLogic = new Logic();
    private FileOpe file = new FileOpe();
    private double zoomFactor = 1;
    private Number mainStageHeight = BOARD_HEIGHT, mainStageWidth = BOARD_WIDTH;
    //-- essential part definition --//

    //-- components initialized --//
    private GridPane showPane = new GridPane();
    private StatView sv = new StatView();
    private ProbView pv = new ProbView();
    private CostView cv = new CostView();
    private CustomBtn cb1 = new CustomBtn("600"), cba = new CustomBtn("6000");
    private ResetBtn resetBtn = new ResetBtn();
    private Setting setting = new Setting();
    private ChoiceBox<String> choiceUp = new ChoiceBox<>(FXCollections.observableArrayList("none","凝电之钻","强力干员(夜莺,黑up)","强力干员(伊芙利特,塞雷娅)","锁与匙的守卫者","强力干员(星熊,推进之王)",
            "「感谢庆典」纪念寻访(银灰,陈,安洁丽娜,艾雅法拉up)","强力干员(闪灵,夜莺up)","强力干员(塞雷娅,斯卡蒂up)","冰封原野","火舞之人",
            "强力干员(银灰,伊芙利特)","强力干员(能天使,推进之王)","久铸尘铁","强力干员(星熊,安洁莉娜)","深夏守夜人",
            "强力干员(斯卡蒂,闪灵up)","强力干员(能天使,伊芙利特)","强力干员(塞雷娅,艾雅法拉up)","鞘中赤红","强力干员(推进之王,安洁莉娜)",
            "龙门特别行动专员","强力干员(夜莺,银灰up)","强力干员(闪灵,塞雷娅up)","搅动潮汐之剑"));
    private String upName = "none";
    //-- components initialized --//

    class AgentTag extends StackPane {
        private final Color lv6 = new Color(.97, .79, .32, 1);
        private final Color lv5 = new Color(1, .97, .68, 1);
        private final Color lv4 = new Color(.67, .68, .88, 1);
        private final Color lv3 = new Color(.75, .75, .75, 1);
        private Rectangle tag = new Rectangle();
        private Label nameTag = new Label();
        private Label lvTag = new Label();
        private ImageView typeImg = new ImageView();
        private ImageView agentImg = new ImageView();
        private String name, type, level;

        public AgentTag(){}

        public AgentTag(Agent agent) {
            this.level = agent.getLevel() + "";
            this.name = agent.getName();
            this.type = agent.getType();
            setNameTag();
        }

        public AgentTag(String name, String type, String level) {
            this.level = level;
            this.name = name;
            this.type = type;
            setNameTag();
        }

        private void setNameTag() {
            this.tag.setVisible(true);
            this.typeImg.setImage(new Image(imgSrc + this.type + imgTyp));
            this.agentImg.setImage(new Image(imgSrc + "AgentImg/" + this.name + imgTyp));

            setTag();
            resize(0, 0);
            this.getChildren().add(tag);
            this.getChildren().add(lvTag);
            this.getChildren().add(typeImg);
            this.getChildren().add(agentImg);
        }

        private void setTag() {
            this.lvTag.setTextFill(Color.WHITE);
            if (this.level.equals("6")) {
                this.tag.setFill(lv6);
                this.lvTag.setText("★★★★★★");
            }
            if (this.level.equals("5")) {
                this.tag.setFill(lv5);
                this.lvTag.setTextFill(Color.BLACK);
                this.lvTag.setText("★★★★★");
            }
            if (this.level.equals("4")) {
                this.tag.setFill(lv4);
                this.lvTag.setText("★★★★");
            }
            if (this.level.equals("3")) {
                this.tag.setFill(lv3);
                this.lvTag.setText("★★★");
            }
        }

        @Override
        public void resize(double width, double height) {
            super.resize(width, height);
            this.lvTag.setFont(Font.font(10 * zoomFactor));
            this.tag.setHeight(200 * zoomFactor);
            this.tag.setWidth(80 * zoomFactor);
            this.typeImg.setFitHeight(40 * zoomFactor);
            this.typeImg.setFitWidth(40 * zoomFactor);
            this.agentImg.setFitHeight(80 * zoomFactor);
            this.agentImg.setFitWidth(80 * zoomFactor);

            setMargin(lvTag, new Insets(0, 0, 150 * zoomFactor, 0));
            setMargin(agentImg, new Insets(0, 0, 40, 0));
            setMargin(typeImg, new Insets(100 * zoomFactor, 0, 0, 0));
        }

        public void setLocation(double X, double Y) {
            this.setLayoutX(X);
            this.setLayoutY(Y);
        }
    }

    static class CustomBtn extends StackPane {
        private Rectangle upperTrim = new Rectangle();
        private Rectangle mainTrim = new Rectangle();
        private Label x = new Label();
        private Label tag = new Label();
        private Color ColorUpperTrim = new Color(.5, .5, .5, .8);
        private Color ColorMainTrim1 = new Color(.84, .84, .84, .8);
        private Color ColorMainTrimA = new Color(.95, .82, .38, .8);
        private String type = "";

        CustomBtn(String type) {
            this.upperTrim.setHeight(20);
            this.upperTrim.setWidth(100);
            this.mainTrim.setHeight(30);
            this.mainTrim.setWidth(100);
            this.upperTrim.setFill(ColorUpperTrim);
            this.x.setTextFill(Color.WHITE);
            this.type = type;
            if (type.equals("600"))
                setTrim1();
            else
                setTrimA();
            ImageView stone = new ImageView();
            stone.setImage(new Image(imgSrc + "stone" + imgTyp));
            setMargin(upperTrim, new Insets(0, 0, 50, 0));
            setMargin(stone, new Insets(0, 0, 60, -60));
            setMargin(x, new Insets(0, 0, 50, 12));

            this.getChildren().add(upperTrim);
            this.getChildren().add(mainTrim);
            this.getChildren().add(stone);
            this.getChildren().add(x);
            this.getChildren().add(tag);
        }

        private void setTrim1() {
            this.x.setText("× 600");
            this.tag.setText("寻访一次");
            this.mainTrim.setFill(ColorMainTrim1);
        }

        private void setTrimA() {
            this.x.setText("× 6000");
            this.tag.setText("寻访十次");
            this.mainTrim.setFill(ColorMainTrimA);
        }

        void setLocation(double X, double Y) {
            this.setLayoutX(X);
            this.setLayoutY(Y);
        }

        void setOver(double opacity) {
            ColorUpperTrim = new Color(.5, .5, .5, opacity);
            ColorMainTrim1 = new Color(.84, .84, .84, opacity);
            ColorMainTrimA = new Color(.95, .82, .38, opacity);
            this.upperTrim.setFill(ColorUpperTrim);
            if (this.type.equals("600"))
                this.mainTrim.setFill(ColorMainTrim1);
            else
                this.mainTrim.setFill(ColorMainTrimA);
        }
    }

    static class ResetBtn extends StackPane {
        private Rectangle bckg = new Rectangle();
        private Color background = new Color(.8, .8, .8, .8);
        private Label text = new Label("重置统计");

        ResetBtn() {
            this.setAlignment(Pos.CENTER);
            this.bckg.setFill(background);
            this.bckg.setWidth(150);
            this.bckg.setHeight(25);
            this.getChildren().addAll(this.bckg, this.text);
        }

        ResetBtn(double width, String text) {
            this.setAlignment(Pos.CENTER);
            this.bckg.setFill(background);
            this.bckg.setWidth(width);
            this.bckg.setHeight(25);
            this.text.setText(text);
            this.getChildren().addAll(this.bckg, this.text);
        }

        ResetBtn(double width, ImageView imageView) {
            this.setAlignment(Pos.CENTER);
            this.bckg.setFill(background);
            this.bckg.setWidth(width);
            this.bckg.setHeight(25);
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            this.getChildren().addAll(this.bckg, imageView);
        }

        void setOver(double opc) {
            background = new Color(.8, .8, .8, opc);
            this.bckg.setFill(background);
        }

        void setLocation(double X, double Y) {
            this.setLayoutX(X);
            this.setLayoutY(Y);
        }
    }

    class Setting extends StackPane {
        ImageView set_icn = new ImageView();
        private boolean on = false;
        private TextField l6 = new TextField();
        private TextField l5 = new TextField();
        private TextField l4 = new TextField();
        private TextField l3 = new TextField();
        private GridPane grid = new GridPane();
        private ResetBtn set = new ResetBtn(100, "更新概率");
        private ResetBtn res = new ResetBtn(50, new ImageView(new Image(imgSrc + "reload" + imgTyp)));

        Setting() {
            this.l6.setPromptText("6星概率 ");
            this.l5.setPromptText("5星概率");
            this.l4.setPromptText("4星概率");
            this.l3.setPromptText("3星概率");
            GridPane buttons = new GridPane();
            buttons.setHgap(5);
            this.grid.setVgap(2);
            this.setAlignment(Pos.TOP_LEFT);
            this.set_icn.setFitHeight(25);
            this.set_icn.setFitWidth(25);
            this.set_icn.setImage(new Image(imgSrc + "setting" + imgTyp));
            this.getChildren().add(set_icn);
            this.grid.add(l6, 0, 0);
            this.grid.add(l5, 0, 1);
            this.grid.add(l4, 0, 2);
            this.grid.add(l3, 0, 3);
            buttons.add(set, 0, 0);
            buttons.add(res, 1, 0);
            this.grid.add(buttons, 0, 4);
            setMargin(this.grid, new Insets(25, 0, 0, 0));
            setMargin(this.set_icn, new Insets(0, 0, 0, 135));
            //this.getChildren().add(gp);
        }

        private void rotateAnim(ImageView imageView, boolean right) {
            RotateTransition rt = new RotateTransition(Duration.millis(250), imageView);
            if (right) {
                rt.setFromAngle(0);
                rt.setToAngle(45);
            } else {
                rt.setFromAngle(0);
                rt.setToAngle(-45);
            }
            rt.setAutoReverse(false);
            rt.play();
        }

        void onClick() {
            if (!on) {
                this.getChildren().add(grid);
                on = true;
                //System.out.println("click show");
            } else {
                this.getChildren().remove(grid);
                on = false;
                //System.out.println("click none");
            }
        }

        void hidden() {
            on = false;
            this.getChildren().remove(grid);
        }

        void setProb(boolean isReset) {
            prob6 = 0;
            prob5 = 0;
            prob4 = 0;
            prob3 = 0;
            if (l6.getText().length() > 0)
                prob6 = Integer.parseInt(l6.getText());
            if (l5.getText().length() > 0)
                prob5 = Integer.parseInt(l5.getText());
            if (l4.getText().length() > 0)
                prob4 = Integer.parseInt(l4.getText());
            if (l3.getText().length() > 0)
                prob3 = Integer.parseInt(l3.getText());

            if (isReset) {
                prob6 = 2;
                prob5 = 8;
                prob4 = 50;
                prob3 = 40;
                l6.setText("");
                l5.setText("");
                l4.setText("");
                l3.setText("");
            }

            if(prob6==23333){
                prob6 = 100;
                prob5 = 0;
                prob4 = 0;
                prob3 = 0;
                upName = "洁哥不要啊啊啊啊";
                choiceUp.setItems(FXCollections.observableArrayList(upName));
                choiceUp.getSelectionModel().select(0);
            }
//            else{
//                choiceUp.setItems(FXCollections.observableArrayList("none","凝电之钻","强力干员(夜莺,黑up)","强力干员(伊芙利特,塞雷娅)","锁与匙的守卫者","强力干员(星熊,推进之王)",
//                        "「感谢庆典」纪念寻访(银灰,陈,安洁丽娜,艾雅法拉up)","强力干员(闪灵,夜莺up)","强力干员(塞雷娅,斯卡蒂up)","冰封原野","火舞之人",
//                        "强力干员(银灰,伊芙利特)","强力干员(能天使,推进之王)","久铸尘铁","强力干员(星熊,安洁莉娜)","深夏守夜人",
//                        "强力干员(斯卡蒂,闪灵up)","强力干员(能天使,伊芙利特)","强力干员(塞雷娅,艾雅法拉up)","鞘中赤红","强力干员(推进之王,安洁莉娜)",
//                        "龙门特别行动专员","强力干员(夜莺,银灰up)","强力干员(闪灵,塞雷娅up)","搅动潮汐之剑"));
//            }
            //choiceUp.getSelectionModel().select(0);
            basicLogic.set_Prob(prob6, prob5, prob4, prob3);
            pv.setData(prob6, prob5, prob4, prob3);
        }

        void setLocation(double X, double Y) {
            this.setLayoutX(X);
            this.setLayoutY(Y);
        }
    }

    static class StatView extends GridPane {
        private Label total = new Label("寻访次数: 0");
        private Label last6 = new Label("距离上次抽到6星次数: 0");
        private Label numl3 = new Label("3星干员: 0");
        private Label numl4 = new Label("4星干员: 0");
        private Label numl5 = new Label("5星干员: 0");
        private Label numl6 = new Label("6星干员: 0");

        StatView() {
            this.setAlignment(Pos.CENTER);
            this.getChildren().add(total);
            this.getChildren().add(numl3);
            this.getChildren().add(numl4);
            this.getChildren().add(numl5);
            this.getChildren().add(numl6);
            this.getChildren().add(last6);

            setMargin(total, new Insets(0, 0, 60, 0));
            setMargin(numl3, new Insets(0, 0, 30, 0));
            setMargin(numl4, new Insets(0, 0, 0, 0));
            setMargin(numl5, new Insets(30, 0, 0, 0));
            setMargin(numl6, new Insets(60, 0, 0, 0));
            setMargin(last6, new Insets(90, 0, 0, 0));
        }

        void setData(int total, int last6, int numl3, int numl4, int numl5, int numl6) {
            this.total.setText("寻访次数: " + total);
            this.last6.setText("距离上次抽到6星次数: " + last6);
            this.numl3.setText("3星干员: " + numl3);
            this.numl4.setText("4星干员: " + numl4);
            this.numl5.setText("5星干员: " + numl5);
            this.numl6.setText("6星干员: " + numl6);
        }

        void setLocation(double X, double Y) {
            this.setLayoutX(X);
            this.setLayoutY(Y);
        }
    }

    static class ProbView extends GridPane {
        private Label prob3 = new Label();
        private Label prob4 = new Label();
        private Label prob5 = new Label();
        private Label prob6 = new Label();
        private Label numl3 = new Label("3星干员概率: ");
        private Label numl4 = new Label("4星干员概率: ");
        private Label numl5 = new Label("5星干员概率: ");
        private Label numl6 = new Label("6星干员概率: ");

        ProbView() {
            this.setHgap(0);
            this.setVgap(-3);
            this.add(numl3, 0, 0);
            this.add(prob3, 1, 0);

            this.add(numl4, 0, 1);
            this.add(prob4, 1, 1);

            this.add(numl5, 0, 2);
            this.add(prob5, 1, 2);

            this.add(numl6, 0, 3);
            this.add(prob6, 1, 3);

        }

        void setData(double prob6, double prob5, double prob4, double prob3) {
            this.prob3.setText(prob3 + "%");
            this.prob4.setText(prob4 + "%");
            this.prob5.setText(prob5 + "%");
            this.prob6.setText(prob6 + "%");
            this.prob3.setTextFill(Color.BLACK);
            this.prob4.setTextFill(Color.BLACK);
            this.prob5.setTextFill(Color.BLACK);
            this.prob6.setTextFill(Color.BLACK);
            if (prob3 != 40.0)
                this.prob3.setTextFill(Color.RED);
            if (prob4 != 50.0)
                this.prob4.setTextFill(Color.RED);
            if (prob5 != 8.0)
                this.prob5.setTextFill(Color.RED);
            if (prob6 != 2.0)
                this.prob6.setTextFill(Color.RED);

        }

        void setLocation(double X, double Y) {
            this.setLayoutX(X);
            this.setLayoutY(Y);
        }
    }

    static class CostView extends GridPane {
        private Label costY = new Label("× 0");
        private Label costS = new Label("× 0");
        private Label costR = new Label("× 0");
        private Label title = new Label("一共消耗: ");
        private ImageView stone = new ImageView();
        private ImageView yuanshi = new ImageView();
        private ImageView yuan = new ImageView();

        public CostView() {

            this.setHgap(2);
            this.setVgap(2);
            this.stone.setImage(new Image(imgSrc + "stone" + imgTyp));
            this.yuanshi.setImage(new Image(imgSrc + "yuanshiS" + imgTyp));
            this.yuan.setImage(new Image(imgSrc + "yen" + imgTyp));
            this.stone.setFitHeight(18);
            this.stone.setFitWidth(18);
            this.yuanshi.setFitHeight(20);
            this.yuanshi.setFitWidth(18);
            this.yuan.setFitHeight(18);
            this.yuan.setFitWidth(18);

            this.add(this.stone, 0, 1);
            this.add(this.costS, 1, 1);

            this.add(this.yuanshi, 0, 2);
            this.add(this.costY, 1, 2);

            this.add(this.yuan, 0, 3);
            this.add(this.costR, 1, 3);
        }

        public void setData(double hechengyu, double yuanshi, double yen) {
            this.costS.setText("× " + (int) hechengyu + "");
            this.costY.setText("× " + yuanshi + "");
            this.costR.setText("× " + (int) yen + "");
        }

        public void setLocation(double X, double Y) {
            this.setLayoutX(X);
            this.setLayoutY(Y);
        }
    }

    private void animation(AgentTag agentTag, int idx) {
        FadeTransition ft = new FadeTransition(Duration.millis(500), agentTag);
        ft.setFromValue(0.5f);
        ft.setToValue(1.0f);
        ft.setAutoReverse(false);
        TranslateTransition tt = new TranslateTransition(Duration.millis(500), agentTag);
        if (idx == 0)
            tt.setFromY(-20);
        else
            tt.setFromY(20);
        tt.setToY(0);
        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().addAll(ft, tt);
        pt.setCycleCount(1);
        pt.play();
    }

    private void moveUp(AgentTag agentTag, boolean In) {
        TranslateTransition up = new TranslateTransition(Duration.millis(200), agentTag);
        if (In) {
            up.setFromY(0);
            up.setToY(-10);
        } else {
            up.setFromY(-10);
            up.setToY(0);
        }

        up.setAutoReverse(false);
        up.play();
    }


    private void drawPool(int time) {
        showPane.getChildren().clear();
        for (int i = 0; i < time; i++) {
            AgentTag agentTag = new AgentTag();
            if(upName.equals("none") || upName.equals("")){
                agentTag = new AgentTag(basicLogic.roll_lv());//--insert uo here!!--//
            }else{
                agentTag = new AgentTag(basicLogic.roll_lv(upName));
            }
            AgentTag finalAgentTag = agentTag;
            agentTag.setOnMouseEntered(event -> {
                moveUp(finalAgentTag, true);
            });
            AgentTag finalAgentTag1 = agentTag;
            agentTag.setOnMouseExited(event -> {
                moveUp(finalAgentTag1, false);
            });
            if (time > 1)
                animation(agentTag, i % 2);
            else {
                Random animR = new Random();
                int idx = animR.nextInt(2);
                //System.out.println(idx);
                animation(agentTag, idx);
            }
            showPane.add(agentTag, i, 0);
            double[] stat = basicLogic.getStat();
            sv.setData((int) stat[4], (int) stat[5], (int) stat[0],
                    (int) stat[1], (int) stat[2], (int) stat[3]);
            pv.setData(stat[6], stat[7], stat[8], stat[9]);
            System.out.println(stat[6]+" "+stat[7]+" "+stat[8]+" "+stat[9]);
            cv.setData(basicLogic.getCost()[0], basicLogic.getCost()[1], basicLogic.getCost()[2]);
        }
    }

    private void reset() {
        basicLogic.reSet();
        //basicLogic = new Logic();
        double[] stat = basicLogic.getStat();
        sv.setData((int) stat[4], (int) stat[5], (int) stat[0],
                (int) stat[1], (int) stat[2], (int) stat[3]);
        pv.setData(stat[6], stat[7], stat[8], stat[9]);
        cv.setData(basicLogic.getCost()[0], basicLogic.getCost()[1], basicLogic.getCost()[2]);

        showPane.getChildren().clear();
    }



    private void setZoomFactor(Number mainStageWidth, Number mainStageHeight) {
        double width = mainStageWidth.doubleValue() / BOARD_WIDTH, height = mainStageHeight.doubleValue() / BOARD_HEIGHT;
        if (width < height)
            zoomFactor = Math.round(width * 100.0) / 100.0;
        else
            zoomFactor = Math.round(height * 100.0) / 100.0;
    }

    @Override
    public void start(Stage primaryStage) {
        //Height min = 420, Width min = 720
        //-- main part initialized section --//
        primaryStage.setTitle("抽卡模拟器");
        primaryStage.setMinHeight(420);
        primaryStage.setMinWidth(720);
        Scene mainStage = new Scene(root, mainStageWidth.doubleValue(), mainStageHeight.doubleValue());
        mainStage.setFill(Color.rgb(200, 200, 200, 0.3));
        //-- main part initialized section --//

        //-- resize listener --//
        mainStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            mainStageHeight = newValue;
            setZoomFactor(mainStageWidth, mainStageHeight);
            resize();
        });
        mainStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            mainStageWidth = newValue;
            showPane.setMinWidth(mainStageWidth.intValue());
            setZoomFactor(mainStageWidth, mainStageHeight);
            resize();
        });

        //-- resize listener --//

        //-- test section --//
        //basicLogic.set_Prob(100, 0, 0, 0);
        //-- test section --//

        //-- initialized section --//
        resize();
        pv.setData(basicLogic.getStat()[6], basicLogic.getStat()[7], basicLogic.getStat()[8], basicLogic.getStat()[9]);

        showPane.setAlignment(Pos.CENTER);
        showPane.setBackground(new Background(new BackgroundFill(Color.rgb(200, 200, 200, 0.8), null, null)));
        showPane.setHgap(0);
        showPane.setVgap(6);
        //-- initialized section --//

        //-- listener section --//
        cb1.setOnMouseClicked(event -> drawPool(1));
        cb1.setOnMouseEntered(event -> cb1.setOver(1.0));
        cb1.setOnMouseExited(event -> cb1.setOver(0.8));

        cba.setOnMouseEntered(event -> cba.setOver(1.0));
        cba.setOnMouseExited(event -> cba.setOver(0.8));
        cba.setOnMouseClicked(event -> drawPool(10));

        resetBtn.setOnMouseEntered(event -> resetBtn.setOver(1.0));
        resetBtn.setOnMouseExited(event -> resetBtn.setOver(0.8));
        resetBtn.setOnMouseClicked(event -> {
            reset();
            returnList();
        });

        setting.set_icn.setOnMouseClicked(event -> setting.onClick());
        setting.set_icn.setOnMouseEntered(event -> setting.rotateAnim(setting.set_icn, true));
        setting.set_icn.setOnMouseExited(event -> setting.rotateAnim(setting.set_icn, false));
        setting.set.setOnMouseClicked(event -> setting.setProb(false));
        setting.set.setOnMouseEntered(event -> setting.set.setOver(1));
        setting.set.setOnMouseExited(event -> setting.set.setOver(0.8));
        setting.res.setOnMouseClicked(event -> {
            setting.setProb(true);
            if(upName.equals("洁哥不要啊啊啊啊"))returnList();
        });
        setting.res.setOnMouseEntered(event -> setting.res.setOver(1));
        setting.res.setOnMouseExited(event -> setting.res.setOver(0.8));

        mainStage.setOnMouseClicked(event -> {
            if (!(event.getX() > mainStage.getWidth() - 100 && event.getY() < 100))
                setting.hidden();
        });
        choiceUp.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String old_str, String new_str) {
                upName = choiceUp.getSelectionModel().getSelectedItem();
            }
        });
        //-- listener section --//

        //-- initialized section --//
        root.getChildren().addAll(cb1, cba, showPane, sv, pv, cv, resetBtn, setting, choiceUp);
        primaryStage.setScene(mainStage);
        primaryStage.show();
        //-- initialized section --//
    }

    private void resize() {
        sv.setLocation(15, mainStageHeight.intValue() - 180);
        pv.setLocation(mainStageWidth.intValue() - 200, mainStageHeight.intValue() - 180);
        cv.setLocation(mainStageWidth.intValue() - 200, mainStageHeight.intValue() - 100);
        cb1.setLocation((mainStageWidth.doubleValue() / 2 - 100) - 50, mainStageHeight.doubleValue() - 150);
        cba.setLocation(mainStageWidth.doubleValue() / 2 + 50, mainStageHeight.doubleValue() - 150);
        resetBtn.setLocation(15, mainStageHeight.doubleValue() - 65);
        setting.setLocation(mainStageWidth.doubleValue() - 170, 20);
        showPane.setMinWidth(mainStageWidth.intValue());
        showPane.setMinHeight(250 * zoomFactor);
        showPane.setLayoutY(100 * zoomFactor);
        choiceUp.setMinSize(200,20);
        choiceUp.setPrefSize(300*zoomFactor,20*zoomFactor);
        choiceUp.setLayoutX(mainStageWidth.doubleValue()/2 - choiceUp.getPrefWidth()/2);
        choiceUp.setLayoutY(20);
        choiceUp.getSelectionModel().select(0);
        choiceUp.setStyle(String.format("-fx-font-size: %f ", 10 * zoomFactor));
        if (mainStageHeight.doubleValue() >= 650 && mainStageWidth.doubleValue() <= 900)
            showPane.setLayoutY((mainStageHeight.doubleValue() - showPane.getMinHeight()) / 2 - 100);
        for (Node node : showPane.getChildren())
            node.resize(0, 0);
        if (mainStageHeight.doubleValue() <= 440) {
            sv.setLocation(15, mainStageHeight.intValue() - 150);
            pv.setLocation(mainStageWidth.intValue() - 200, mainStageHeight.intValue() - 150);
            cv.setLocation(mainStageWidth.intValue() - 200, mainStageHeight.intValue() - 80);
            resetBtn.setLocation(15, mainStageHeight.doubleValue() - 40);
        }
    }

    private void returnList(){
        choiceUp.setItems(FXCollections.observableArrayList("none","凝电之钻","强力干员(夜莺,黑up)","强力干员(伊芙利特,塞雷娅)","锁与匙的守卫者","强力干员(星熊,推进之王)",
                "「感谢庆典」纪念寻访(银灰,陈,安洁丽娜,艾雅法拉up)","强力干员(闪灵,夜莺up)","强力干员(塞雷娅,斯卡蒂up)","冰封原野","火舞之人",
                "强力干员(银灰,伊芙利特)","强力干员(能天使,推进之王)","久铸尘铁","强力干员(星熊,安洁莉娜)","深夏守夜人",
                "强力干员(斯卡蒂,闪灵up)","强力干员(能天使,伊芙利特)","强力干员(塞雷娅,艾雅法拉up)","鞘中赤红","强力干员(推进之王,安洁莉娜)",
                "龙门特别行动专员","强力干员(夜莺,银灰up)","强力干员(闪灵,塞雷娅up)","搅动潮汐之剑"));
        choiceUp.getSelectionModel().select(0);
    }
}
//TODO: add the selection bar for choose the up rules.