package jp.ac.tokushima_u.is.ll.ws.service.converter;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.ws.service.model.ItemModel;

/**
 *
 * @author Houbin
 */
public class ItemConverter {

    public static ItemModel convert(Item item) {
        ItemModel model = new ItemModel();
        model.setAuthorNickname(item.getAuthor().getNickname());
        model.setBarcode(item.getBarcode());
        model.setCreateTime(item.getCreateTime());
        model.setEnTitle(item.getTitleByCode("en"));
        model.setDisabled(item.getDisabled());
        model.setId(item.getId());
        model.setItemLat(item.getItemLat());
        model.setItemLng(item.getItemLng());
        model.setItemZoom(item.getItemZoom());
        model.setJpTitle(item.getTitleByCode("ja"));
        model.setNote(item.getNote());
        model.setPlace(item.getPlace());
        model.setQrcode(item.getQrcode());
        model.setRfid(item.getRfid());
        model.setUpdateTime(item.getUpdateTime());
        model.setZhTitle(item.getTitleByCode("zh"));
        return model;
    }
}
