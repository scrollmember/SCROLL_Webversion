package jp.ac.tokushima_u.is.ll.controller;

import jp.ac.tokushima_u.is.ll.common.orm.Page;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.form.ItemSearchCondForm;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.GoogleMapService;
import jp.ac.tokushima_u.is.ll.service.ItemService;
import jp.ac.tokushima_u.is.ll.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private GoogleMapService googleMapService;

    @ModelAttribute("googleMapApi")
    public String googleApiKey() {
        return googleMapService.getGoogleMapApi();
    }

    @RequestMapping
    public String index(ModelMap model) {
		Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());

        ItemSearchCondForm form = new ItemSearchCondForm();
        form.setUserId(user.getId());
        form.setIncludeRelog(true);
        Page<Item> itemPage = itemService.searchItemPageByCond(form);
        model.addAttribute("myitems", itemPage);


        ItemSearchCondForm form2 = new ItemSearchCondForm();
        form2.setAnsweruserId(user.getId());
        Page<Item> answerPage = itemService.searchItemPageByCond(form2);
        model.addAttribute("answeritems", answerPage);
        model.addAttribute("user", user);


		// ■wakebe 経験値の加算(仮)
		userService.addExperiencePoint(user.getId(), 0);

		// ■wakebe 次のレベルまでの経験値取得
		model.addAttribute("nextExperiencePoint", this.userService.getNextExperiencePoint(user.getId()));

		// ■wakebe 現在の合計経験値取得
		model.addAttribute("nowExperiencePoint", this.userService.getNowExperiencePoint(user.getId()));

        return "member/index";
    }
}
