package inputsystem;

import java.io.File;

public class FileParserFactory {

	public FileParser getFileType(String extension, File file) {

		if (extension.equals("txt")) {
			return new TxtParser(file);
		} else if (extension.equals("xml")) {
			return new XmlParser(file);
		}
		return null;
	}
}
