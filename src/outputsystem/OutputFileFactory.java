package outputsystem;

public class OutputFileFactory {

	public OutputFile setFileType(String extension) {

		if (extension.equals("txt")) {
			return new TxtReportWriter();
		} else if (extension.equals("xml")) {
			return new XmlReportWriter();
		}
		return null;
	}

}
