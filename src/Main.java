import gui.MainWindowController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by David on 12/03/2016.
 */
public class Main extends Application {
	@Override public void start(Stage primaryStage) throws Exception {
		Scene p = new MainWindowController().init();
		primaryStage.setTitle("Image Viewer");
		primaryStage.setScene(p);
		primaryStage.setMaximized(true);
		primaryStage.setOnCloseRequest(e -> System.exit(0));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
