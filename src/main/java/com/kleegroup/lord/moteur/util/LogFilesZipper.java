package com.kleegroup.lord.moteur.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import com.kleegroup.lord.moteur.reader.CsvReaderAdapter;

/**
 *Zip les fichier de log dauns un fichier d�sign�.
 *
 *voir < a href=http://www.java-tips.org/java-se-tips/java.util.zip/how-to-create-a-zip-file.html>
 *source du code </a> 
 *
 */
public class LogFilesZipper {
	private static org.apache.log4j.Logger logAppli = Logger.getLogger(CsvReaderAdapter.class);

	/**
	 * @param zipFilePath le path d�sir� du fichier de log
	 * @param filenames les fichiers � zipper
	 * @throws IOException si une erreur de lecture a lieu
	 */
	public static void zip(String zipFilePath, List<String> filenames) throws IOException {
		zip(new File(zipFilePath), filenames);
	}

	/**
	 * @param outputZip le File qui repr�sente le fichier de zip
	 * @param filenames les fichiers � zipper
	 * @throws IOException si une erreur de lecture a lieu
	 */
	public static void zip(File outputZip, List<String> filenames) throws IOException {
		// Create a buffer for reading the files
		byte[] buf = new byte[1024];

		// Create the ZIP file
		ZipOutputStream out = null;
		List<String> fileList = getValidFiles(filenames);
		if (fileList.isEmpty()) {
			return;
		}
		try {
			out = new ZipOutputStream(new FileOutputStream(outputZip));

			// Compress the files
			for (String filename : fileList) {
				File f = new File(filename);

				try (FileInputStream in = new FileInputStream(filename)) {
					// Add ZIP entry to output stream.
					out.putNextEntry(new ZipEntry(f.getName()));

					// Transfer bytes from the file to the ZIP file
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}

					// Complete the entry
					out.closeEntry();
				} catch (IOException ex) {
					logAppli.error(ex);
				}
			}
		} catch (IOException ex) {
			logAppli.error(ex);
		} finally {

			// Complete the ZIP file
			if (out != null) {
				out.close();
			}
		}
	}

	private static List<String> getValidFiles(List<String> filenames) {
		List<String> res = new ArrayList<>();
		for (int i = 0; i < filenames.size(); i++) {
			File aTester = new File(filenames.get(i));
			if (aTester.exists() && aTester.canRead()) {
				res.add(aTester.getAbsolutePath());
			}
		}
		return res;
	}
}
