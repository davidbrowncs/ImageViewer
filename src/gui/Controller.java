package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Created by David on 12/03/2016.
 */
public interface Controller {
	Pane init();

	void update(MainWindowController mainWindowController);

	static Pane loadFromFXML(String path, Controller c) {
		FXMLLoader loader = new FXMLLoader(c.getClass().getResource(path));
		loader.setController(c);
		try {
			return loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
