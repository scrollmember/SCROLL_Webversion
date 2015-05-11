package jp.ac.tokushima_u.is.ll.service;

import java.util.List;
import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemRating;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Houbin
 */
@Service
public class ItemRatingService {

    private HibernateDao<Item, String> itemDao;
    private HibernateDao<ItemRating, String> itemRatingDao;
    private HibernateDao<Users, String> usersDao;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        itemDao = new HibernateDao<Item, String>(sessionFactory, Item.class);
        itemRatingDao = new HibernateDao<ItemRating, String>(sessionFactory, ItemRating.class);
        usersDao = new HibernateDao<Users, String>(sessionFactory, Users.class);
    }

    @Transactional(readOnly = true)
    public boolean ratingExist(String itemId) {
        Item item = itemDao.get(itemId);
        if (item == null) {
            return true;
        }
        Users user = usersDao.get(SecurityUserHolder.getCurrentUser().getId());
        if (user == null) {
            return true;
        }
        List<ItemRating> testList = this.itemRatingDao.find("from ItemRating r where r.item=? and r.user=?", item, user);
        if (testList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public void create(String itemId, Integer rate) {
        Item item = itemDao.get(itemId);
        if (item == null) {
            return;
        }
        Users user = usersDao.get(SecurityUserHolder.getCurrentUser().getId());
        if (user == null) {
            return;
        }
        List<ItemRating> testList = this.itemRatingDao.find("from ItemRating r where r.item=? and r.user=?", item, user);
        if (testList.size() > 0) {
            return;
        }
        ItemRating rating = new ItemRating();
        rating.setItem(item);
        rating.setUser(user);
        rating.setRating(new Double(rate));
        Double currentRatingSum = itemRatingDao.findUnique("select sum(r.rating) from ItemRating r where r.item=?", rating.getItem());
        if (currentRatingSum == null) {
            currentRatingSum = 0d;
        }
        Long count = itemRatingDao.findUnique("select count(r) from ItemRating r where r.item=?", rating.getItem());
        if (count == null) {
            count = 0l;
        }

        itemRatingDao.save(rating);
        double avg = (currentRatingSum + rating.getRating()) / (count + 1);
        item.setRating(avg);
        itemDao.save(item);
    }

    @Transactional(readOnly = true)
    public Long countVotesNumber(Item item) {
        Long count = itemRatingDao.findUnique("select count(r) from ItemRating r where r.item=?", item);
        return count;
    }

    @Transactional(readOnly = true)
    public Double avgRating(Item item) {
        return itemRatingDao.findUnique("select avg(r.rating) from ItemRating r where r.item=?", item);
    }
}
