package jp.ac.tokushima_u.is.ll.util;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

public class LLZip {
	public void unZip(String destDir, String sourceZip) {
			try {
				Project proj = new Project();
				Expand expand = new Expand();
				expand.setProject(proj);
				expand.setSrc(new File(sourceZip));
				expand.setOverwrite(false);
				File f = new File(destDir);
				expand.setDest(f);
				expand.execute();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
	}

	public void zip(String sourceFile, String targetZip) {
		Project proj = new Project();
		Zip zip = new Zip();
		zip.setProject(proj);
		zip.setDestFile(new File(targetZip));
		FileSet fileSet = new FileSet();
		fileSet.setProject(proj);
		fileSet.setDir(new File(sourceFile));
		// fileSet.setIncludes("**/*.java"); //包括哪些文件或文件夹,只压缩目录中的所有java文件
		// fileSet.setExcludes("**/*.java"); //排除哪些文件或文件夹,压缩所有的文件，排除java文件
		zip.addFileset(fileSet);
		zip.execute();
	}
}
