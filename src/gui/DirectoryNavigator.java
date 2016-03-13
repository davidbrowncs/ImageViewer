package gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by David on 13/03/2016.
 */
public class DirectoryNavigator {

	private ArrayList<FileWrapper> files = new ArrayList<>();
	private ListIterator<FileWrapper> it = files.listIterator();

	private static final String IMAGE_PATTERN =
			"([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
	private Pattern pattern = Pattern.compile(IMAGE_PATTERN);

	public boolean hasNext() {
		return it.hasNext();
	}

	public boolean hasPrevious() {
		return it.hasPrevious();
	}

	public FileWrapper next() {
		FileWrapper f;
		boolean reInitialised = false;
		do {
			if (it.hasNext()) {
				f = it.next();
			} else if (reInitialised) {
				return null;
			} else {
				it = this.files.listIterator();
				reInitialised = true;
				if (it.hasNext()) {
					f = it.next();
				} else {
					return null;
				}
			}
		} while (!isImage(f.getFile()));
		return f;
	}

	public FileWrapper previous() {
		FileWrapper f;
		boolean reInitialised = false;
		do {
			if (it.hasPrevious()) {
				f = it.previous();
			} else if (reInitialised) {
				return null;
			} else {
				if (!this.files.isEmpty()) {
					it = this.files.listIterator(this.files.size());
					reInitialised = true;
					f = it.previous();
				} else {
					return null;
				}
			}
		} while (!isImage(f.getFile()));
		return f;
	}

	public boolean isImage(File file) {
		Matcher matcher = pattern.matcher(file.getName());
		return matcher.matches();
	}

	public void fileSelected(File f) {
		setFiles(f.getParentFile().listFiles());
		it = this.files.listIterator();
		File curr;
		// Get to the right position
		while (it.hasNext()) {
			curr = it.next().getFile();
			if (curr.equals(f)) {
				break;
			}
		}
	}

	public void setFiles(List<FileWrapper> files) {
		this.files.clear();
		this.files.addAll(files);
		it = this.files.listIterator();
	}

	public void setFiles(FileWrapper[] files) {
		setFiles(Arrays.asList(files));
	}

	public void setFiles(File[] files) {
		List<FileWrapper> list = new ArrayList<>();
		for (File f : files) {
			list.add(new FileWrapper(f));
		}
		setFiles(list);
	}


}
