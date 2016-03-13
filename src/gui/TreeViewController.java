package gui;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by David on 12/03/2016.
 */
public class TreeViewController implements Controller {

	private TreeView treeView;
	private Button refreshButton;
	private Button chooseRootButton;

	private MainWindowController mainWindowController;

	private DirectoryNavigator dirNav = new DirectoryNavigator();

	// Initialise to my picture dir. If it doesn't exist you can select a folder from the "select root" button
	private File rootFile = new File("E:\\MyPictures");

	{
		if (!rootFile.exists() || !rootFile.isDirectory()) {
			rootFile = null;
		}
	}

	private FileWrapper currentFileWrapper;

	public TreeViewController(Button fb, TreeView tv, Button chooseRoot, MainWindowController mwc) {
		this.treeView = tv;
		this.treeView.setFocusTraversable(false);
		this.refreshButton = fb;
		this.chooseRootButton = chooseRoot;
		mainWindowController = mwc;
	}

	private TreeItem<FileWrapper> getNodes(File parent, TreeItem<FileWrapper> parentNode) {
		TreeItem<FileWrapper> root = new TreeItem<>(new FileWrapper(parent));
		root.setExpanded(false);
		File[] files = parent.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				root.getChildren().add(getNodes(f, root));
			} else if (dirNav.isImage(f)) {
				root.getChildren().add(new TreeItem<>(new FileWrapper(f)));
			}
		}
		return root;
	}

	public void nextPressed() {
		FileWrapper f = dirNav.next();
		if (f != null) {
			mainWindowController.imageFileSelected(f.getFile());
		}
	}

	public void previousPressed() {
		FileWrapper f = dirNav.previous();
		if (f != null) {
			mainWindowController.imageFileSelected(f.getFile());
		}
	}

	private void initListeners() {
		chooseRootButton.setOnAction(e -> {
			DirectoryChooser dc = new DirectoryChooser();
			File f = dc.showDialog(new Stage());
			if (f != null && f.isDirectory()) {
				rootFile = f;
				updateTreeView();
			}
		});
		treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				currentFileWrapper = ((TreeItem<FileWrapper>) newVal).getValue();
				File f = currentFileWrapper.getFile();
				if (f.isDirectory()) {
					File[] files = f.listFiles();
					dirNav.setFiles(files);
					FileWrapper fw = dirNav.next();
					if (fw != null) {
						mainWindowController.imageFileSelected(fw.getFile());
					}
				} else {
					mainWindowController.imageFileSelected(f);
					dirNav.fileSelected(f);
				}
			}
		});
		refreshButton.setOnAction(e -> updateTreeView());
	}

	private void updateTreeView() {
		treeView.setRoot(null);
		if (rootFile != null) {
			MainWindowController.e.submit(() -> {
				TreeItem<FileWrapper> root = getNodes(rootFile, null);
				root.setExpanded(true);
				Platform.runLater(() -> treeView.setRoot(root));
			});
		}
	}


	@Override public Pane init() {
		initListeners();
		updateTreeView();
		return null;
	}

	@Override public void update(MainWindowController mainWindowController) {

	}
}
