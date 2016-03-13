package gui;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by David on 12/03/2016.
 */
public class MainWindowController {

	public static final ExecutorService e = Executors.newCachedThreadPool();

	@FXML private Button refreshButton;
	@FXML private Button chooseRootButton;
	@FXML private TreeView treeView;
	@FXML private SplitPane splitPane;

	@FXML private ImageView imageView;
	@FXML private BorderPane imagePane;

	@FXML private Pane imageViewBackgroundPane;

	@FXML private Button previousButton;
	@FXML private Button fullScreenButton;
	@FXML private Button nextButton;

	@FXML private AnchorPane splitPaneParentLeft;

	private TreeViewController treeViewController;
	private Parent parent;

	private Image currImg;
	private double currImgPrefWidth;
	private double currImgPrefHeight;

	@FXML private Button fullScreenCloseButton;
	@FXML private ImageView fullScreenImageView;
	@FXML private BorderPane fullScreenImageWrapper;
	private Stage imageStage = null;

	private EventHandler<KeyEvent> handler = event -> {
		switch (event.getCode()) {
			case LEFT: {
				treeViewController.previousPressed();
			}
			break;
			case RIGHT: {
				treeViewController.nextPressed();
			}
			break;
		}
	};

	public void imageFileSelected(File file) {
		e.submit(() -> {
			currImg = new Image(file.toURI().toString());
			currImgPrefWidth = currImg.getWidth();
			currImgPrefHeight = currImg.getHeight();
			if (imageStage == null) {
				setImage();
			} else {
				Platform.runLater(() -> fullScreenImageView.setImage(currImg));
			}
		});
	}

	private void setImage() {
		Platform.runLater(() -> {
			double width = imagePane.getWidth();
			double height = imagePane.getHeight();
			double w = Math.min(width, currImgPrefWidth);
			double h = Math.min(height, currImgPrefHeight);
			imageView.setFitWidth(w);
			imageView.setFitHeight(h);
			imageView.setImage(currImg);
		});
	}

	private void initValues() {
		imageView.setFitWidth(imagePane.getWidth());
		imageView.setFitHeight(imagePane.getHeight());
		imagePane.widthProperty().addListener(e -> imageView.setFitWidth(Math.min(imagePane.getWidth(), currImgPrefWidth)));
		imagePane.heightProperty().addListener(e -> imageView.setFitHeight(Math.min(imagePane.getHeight(), currImgPrefHeight)));
		imageView.setPreserveRatio(true);
		imageView.setCache(true);
	}

	private void initKeyListener(Scene scene) {
		scene.addEventFilter(KeyEvent.KEY_PRESSED, handler);
	}

	private void initListeners() {
		previousButton.setOnAction(e -> treeViewController.previousPressed());
		nextButton.setOnAction(e -> treeViewController.nextPressed());
		fullScreenButton.setOnAction(e -> {
			if (imageStage == null && currImg != null) {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("FullScreenImage.fxml"));
					loader.setController(this);
					Pane p = loader.load();
					Scene sc = new Scene(p);

					final Runnable close = () -> {
						imageStage.close();
						imageStage = null;
						setImage();
					};

					sc.addEventFilter(KeyEvent.KEY_PRESSED, handler);
					sc.addEventFilter(KeyEvent.KEY_PRESSED, e1 -> {
						if (e1.getCode() == KeyCode.ESCAPE) {
							close.run();
						}
					});

					fullScreenCloseButton.setOnAction(e1 -> {
						close.run();
					});

					fullScreenImageView.setImage(currImg);
					fullScreenImageWrapper.widthProperty().addListener(e2 -> fullScreenImageView.setFitWidth
							(fullScreenImageWrapper.getWidth()));
					fullScreenImageWrapper.heightProperty().addListener(e2 -> fullScreenImageView.setFitHeight
							(fullScreenImageWrapper.getHeight()));

					imageStage = new Stage(StageStyle.UNDECORATED);
					imageStage.setOnCloseRequest(e1 -> imageStage = null);
					imageStage.setScene(sc);
					imageStage.setMaximized(true);

					imageStage.show();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	public Scene init() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
		loader.setController(this);
		try {
			parent = loader.load();
			initValues();
			initListeners();
			Scene sc = new Scene(parent);
			treeViewController = new TreeViewController(refreshButton, treeView, chooseRootButton, this);
			treeViewController.init();
			initKeyListener(sc);
			/* Wat */
			Platform.runLater(() -> splitPane.setDividerPosition(0, 0.15d));
			return sc;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
