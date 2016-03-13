package gui;

import java.io.File;

/**
 * Created by David on 12/03/2016.
 */
public class FileWrapper {

	static int idCounter = 0;
	private int id = idCounter++;

	private File file;

	public FileWrapper(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String toString() {
		return file.getName();
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FileWrapper that = (FileWrapper) o;

		return id == that.id;

	}

	@Override public int hashCode() {
		return id;
	}

	public int getId() {
		return id;
	}

}
