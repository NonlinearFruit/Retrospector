/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector;

import insidefx.undecorator.UndecoratorScene;
import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import retrospector.fxml.core.CoreController;
import retrospector.fxml.server.XmlService;
import retrospector.model.DataManager;

/**
 *
 * @author nonfrt
 */
public class Retrospector extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static final String SPLASH_IMAGE
            = "/retrospector/res/splash.png";
    public static final String LOGO_IMAGE
            = "res/logo-128.png";
    
    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private Stage mainStage;
    private static final int SPLASH_WIDTH = 400;
    private static final int SPLASH_HEIGHT = 400;

    @Override
    public void init() {
        ImageView splash = new ImageView(new Image(
                SPLASH_IMAGE,
                SPLASH_WIDTH,
                SPLASH_HEIGHT,
                true,
                true
        ));
        loadProgress = new ProgressBar(0);
        loadProgress.setPrefWidth(SPLASH_WIDTH);
        progressText = new Label("Initializing . . .");
        progressText.setBackground(new Background(new BackgroundFill(Color.GHOSTWHITE,new CornerRadii(5),new Insets(0))));
        splashLayout = new VBox();
        splashLayout.getChildren().addAll(splash, loadProgress, progressText);
        progressText.setAlignment(Pos.CENTER);
        splashLayout.setBackground(Background.EMPTY);
    }

    @Override
    public void start(final Stage initStage) throws Exception {
        final Task<Parent> task = new Task<Parent>() {
            @Override
            protected Parent call() throws InterruptedException,IOException {
                updateProgress(0,7);
                updateMessage("Loading Database . . .");
                DataManager.startDB();
                updateProgress(1,7);
                
                updateMessage("Loading Core . . .");
                FXMLLoader ldr = new FXMLLoader(getClass().getResource("/retrospector/fxml/core/Core.fxml"));
                    Parent root = ldr.load();
                    CoreController core = ldr.getController();
                updateProgress(2,7);
                
                updateMessage("Loading Search . . .");
                FXMLLoader searchldr = new FXMLLoader(getClass().getResource("/retrospector/fxml/search/SearchTab.fxml"));
                    searchldr.load();
                    core.setSearchController(searchldr);
                updateProgress(3,7);
                
                updateMessage("Loading Media . . .");
                FXMLLoader medialdr = new FXMLLoader(getClass().getResource("/retrospector/fxml/media/MediaTab.fxml"));
                    medialdr.load();
                    core.setMediaController(medialdr);
                updateProgress(4,7);
                
                updateMessage("Loading Chart . . .");
                FXMLLoader statldr = new FXMLLoader(getClass().getResource("/retrospector/fxml/chart/StatsTab.fxml"));
                    statldr.load();
                    core.setStatsController(statldr);
                updateProgress(5,7);
                
                updateMessage("Loading List . . .");
                FXMLLoader listldr = new FXMLLoader(getClass().getResource("/retrospector/fxml/list/ListsTab.fxml"));
                    listldr.load();
                    core.setListController(listldr);
                updateProgress(6,7);
                
                updateMessage("Loading Achievements . . .");
                FXMLLoader achieveldr = new FXMLLoader(getClass().getResource("/retrospector/fxml/achievements/AchievementTab.fxml"));
                    achieveldr.load();
                    core.setAchieveController(achieveldr);
                updateProgress(7,7);

                return root;
            }
        };

        showSplash(
                initStage,
                task,
                () -> showMainStage(task.valueProperty().get())
        );
        new Thread(task).start();
    }

    private void showMainStage(Parent root) {
        mainStage = new Stage();
//        mainStage = new Stage(StageStyle.UNDECORATED);
//        Scene scene = new Scene(root, 1300, 800);
        UndecoratorScene.setClassicDecoration();
        UndecoratorScene undecoratorScene = new UndecoratorScene(mainStage, (Region) root);
        mainStage.setScene(undecoratorScene);
        mainStage.setTitle("Retrospector");
        mainStage.getIcons().add(new Image(Retrospector.class.getResourceAsStream( LOGO_IMAGE ))); 
        mainStage.show();
    }

    private void showSplash(
            final Stage initStage,
            Task<?> task,
            InitCompletionHandler initCompletionHandler
    ) {
        progressText.textProperty().bind(task.messageProperty());
        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            } else if (newState == Worker.State.FAILED) {
                task.getException().printStackTrace(System.err);
            }
        });

        Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.setAlwaysOnTop(true);
        initStage.initStyle(StageStyle.TRANSPARENT);
        initStage.show();
    }

    public interface InitCompletionHandler {

        void complete();
    }
    
    @Override
    public void stop() {
        XmlService.killServer();
    }
}
