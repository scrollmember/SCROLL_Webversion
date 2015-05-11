package jp.ac.tokushima_u.is.ll.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class FileFinder {
	public List<File> find(File dir, String fileName) {
		File[] strList;
		int i = 0;
		strList = dir.listFiles();
		List<File> result = new ArrayList<File>();
		for (i = 0; i < strList.length; i++) {
			File truefile = strList[i];
			if (!truefile.isDirectory()) {
				if(fileName.toUpperCase().equals(truefile.getName().toUpperCase())){
					result.add(truefile);
				}
			} else{
				List<File> test = find(truefile, fileName);
				if(test!=null) result.addAll(test);
			}
		}
		return result;
	}
	
	public static void main(String[] args) throws IOException{
		File zipFile = new File("D:\\temp\\9AAC4074-CE62-85F3-BC54-7D59D4D949E4.zip");
		LLZip llzip = new LLZip();
		File unzipDir = new File("d:\\temp", FilenameUtils.getBaseName(zipFile.getName()));
		llzip.unZip(unzipDir.getPath(), zipFile.getPath());
		FileFinder finder = new FileFinder();
		List<File> csvFiles = finder.find(unzipDir, "sensor.csv");
		if(csvFiles.size()<1) throw new FileNotFoundException("sensor.csv");
		File csvFile = csvFiles.get(0);
		File tmpPath = csvFile.getParentFile();
		FileUtils.moveDirectory(tmpPath, new File("d:/pacalldata", UUID.randomUUID().toString()));
		FileUtils.deleteDirectory(unzipDir);
	}
}
