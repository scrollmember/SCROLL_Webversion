package jp.ac.tokushima_u.is.ll.controller.pacall;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import jp.ac.tokushima_u.is.ll.service.pacall.ShowpicService;
import jp.ac.tokushima_u.is.ll.util.FileContentTypeUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pacall/showpic")
public class ShowpicController {
	
	@Autowired
	private ShowpicService showpicService;
	
	@RequestMapping("/{filename}")
	public void show(@PathVariable String filename, HttpServletResponse response) throws IOException{
		if(filename == null){
			return;
		}
		if(StringUtils.isBlank(FilenameUtils.getExtension(filename))){
			filename+=".jpg";
		}
		String basename = FilenameUtils.getBaseName(filename);
		String[] params = basename.split("_");
		if(params == null || params.length!=3){
			return;
		}
		String sensepicId = params[0];
		int width = Integer.valueOf(params[1]);
		int height = Integer.valueOf(params[2]);
		
		File outputFile = showpicService.prepareFile(sensepicId, width, height);
		if(outputFile == null) return;
		response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");
		response.setHeader("Cache-Control", "max-age=259200");
		response.setContentType(FileContentTypeUtil.getMimeType(outputFile));
		FileUtils.copyFile(outputFile, response.getOutputStream());
		return;
	}
}
