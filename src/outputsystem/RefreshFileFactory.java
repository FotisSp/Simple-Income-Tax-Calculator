package outputsystem;

import java.io.File;

public class RefreshFileFactory {

	public RefreshFile getFileType(String extension, File file) {

		if (extension.equals("txt")) {
			return new TxtWriter(file);
		} else if (extension.equals("xml")) {
			return new XmlWriter(file);
		}
		return null;
	}
}
