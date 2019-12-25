import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class UploadAgentUI extends Application {
    private final Group root = new Group();
    private static final int BOARD_WIDTH = 1000;
    private static final int BOARD_HEIGHT = 600;
    private double zoomFactor = 1;
    private Number mainStageHeight = BOARD_HEIGHT, mainStageWidth = BOARD_WIDTH;
    private ChoiceBox<String> agentType = new ChoiceBox<>(FXCollections.observableArrayList("狙击","术师","先锋","近卫","重装","医疗","辅助","特种"));

    @Override
    public void start(Stage primaryStage) throws Exception {
        //-- main part initialized section --//
        primaryStage.setTitle("抽卡模拟器 干员上传");
        primaryStage.setMinHeight(420);
        primaryStage.setMinWidth(720);
        Scene mainStage = new Scene(root, mainStageWidth.doubleValue(), mainStageHeight.doubleValue());
        //-- main part initialized section --//




        //-- initialized section --//
        root.getChildren().addAll();
        primaryStage.setScene(mainStage);
        primaryStage.show();
        //-- initialized section --//
    }
}
