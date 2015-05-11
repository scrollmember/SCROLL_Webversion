
package jp.ac.tokushima_u.is.ll.controller;

import jp.ac.tokushima_u.is.ll.service.PropertyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    private PropertyService propertyService;

    @RequestMapping("/apk")
    public String downloadApk(ModelMap model) {
        model.addAttribute("urlApkLearningLog", propertyService.getUrlApkLearningLog());
        model.addAttribute("urlApkNavigator", propertyService.getUrlApkNavigator());
        model.addAttribute("systemUrl", propertyService.getSystemUrl());
        return "download/apk";
    }
}
