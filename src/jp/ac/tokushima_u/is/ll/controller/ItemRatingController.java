package jp.ac.tokushima_u.is.ll.controller;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.ItemRatingService;
import jp.ac.tokushima_u.is.ll.service.ItemService;
import jp.ac.tokushima_u.is.ll.service.UserService;
import jp.ac.tokushima_u.is.ll.util.Constants;
import jp.ac.tokushima_u.is.ll.visualization.ReviewHistoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Houbin
 */
@Controller
@RequestMapping("/itemrating")
public class ItemRatingController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemRatingService itemRatingService;
    @Autowired
	private ReviewHistoryService reviewHistoryService;

    @RequestMapping(value = "/{id}", method=RequestMethod.POST)
    public String rating(@PathVariable String id, Integer rate, ModelMap model) {
        if (rate == null || rate < 1 || rate > 5) {
            return null;
        }
        Item item = itemService.findById(id);
        if (item == null) {
            return null;
        }
        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        if (user == null) {
            return null;
        }
        itemRatingService.create(id, rate);

        model.put("votes", itemRatingService.countVotesNumber(item));
        model.put("avg", itemRatingService.avgRating(item));
        return "item/rating";
    }
    
    @RequestMapping(value = "/{id}/exper", method=RequestMethod.POST)
    public String experience(@PathVariable String id, Integer exper, ModelMap model) {
        if (exper == null || exper < 1 ) {
            return null;
        }
        Item item = itemService.findById(id);
        if (item == null) {
            return null;
        }
        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        if (user == null) {
            return null;
        }
        reviewHistoryService.updateItemState(item, user, Constants.ExperiencedState, null);
        

        model.put("votes", itemRatingService.countVotesNumber(item));
        model.put("avg", itemRatingService.avgRating(item));
        return "item/rating";
    }
}
