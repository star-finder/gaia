package starfish.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

	public static File writeToFile(String content) throws IOException {
		File file = File.createTempFile("profile", null);
		BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsolutePath(), true));
		bw.write(content);
		bw.flush();
		bw.close();
		return file;

	}
}
