/**
 * Copyright (c) 2005-2009 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: HibernateDao.java 577 2009-10-20 15:44:24Z calvinxiu $
 */
package jp.ac.tokushima_u.is.ll.common.orm.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.common.orm.Page;
import jp.ac.tokushima_u.is.ll.common.orm.PropertyFilter;
import jp.ac.tokushima_u.is.ll.common.orm.PropertyFilter.MatchType;
import jp.ac.tokushima_u.is.ll.util.ReflectionUtils;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.springframework.util.Assert;

/**
 * SpringSide拡張機能をカプセル化したHibernate DAO汎用抽象クラス。
 * 拡張機能にはページ検索、属性の条件で検索を包括している。
 * Service層で直接使える。また拡張泛型DAOサブクラスでも使える。
 * 
 * @param <T> DAOの対象データのテープ
 * @param <PK> 主キーのテープ
 * 
 * @author calvin
 */
@SuppressWarnings("rawtypes")
public class HibernateDao<T, PK extends Serializable> extends SimpleHibernateDao<T, PK> {

    /**
     * DAOのサブクラス用のコンストラクタ
     * サブクラスの汎用定義を通して対象データのテープのクラスを取得
     * 
     * eg.
     * public class UserDao extends HibernateDao<User, Long>{
     * }
     */
    public HibernateDao() {
        super();
    }

    /**
     * Dao層を省略して、Service層で直接使うためのコンストラクタ
     * 
     * eg.
     * HibernateDao<User, Long> userDao = new HibernateDao<User, Long>(sessionFactory, User.class);
     */
    public HibernateDao(final SessionFactory sessionFactory, final Class<T> entityClass) {
        super(sessionFactory, entityClass);
    }

    //-- ページクエリ関数 --//
    /**
     * すべてのデータを取得する
     */
    public Page<T> getAll(final Page<T> page) {
        return findPage(page);
    }

    /**
     * HQLで検索
     *
     * @param page 分页参数.不支持其中的orderBy参数.  ページ引数、orderByをサポートしない
     * @param hql Hibernate HQL
     * @param values HQL中の引数の値、順番で設定する
     *
     * @return 分页查询结果, 附带结果列表及所有查询时的参数.
     */
    @SuppressWarnings("unchecked")
    public Page<T> findPage(final Page<T> page, final String hql, final Object... values) {
        Assert.notNull(page, "page should not be null");

        Query q = createQuery(hql, values);

        if (page.isAutoCount()) {
            long totalCount = countHqlResult(hql, values);
            page.setTotalCount(totalCount);
        }

        setPageParameter(q, page);
		List result = q.list();
        page.setResult(result);
        return page;
    }

    /**
     * HQLでページクエリ
     *
     * @param page ページ引数
     * @param hql HQL
     * @param values HQLの命名引数、名称で設定する。
     *
     * @return 検索結果.結果のリストと検索パラメータも含める
     */
    @SuppressWarnings("unchecked")
    public Page<T> findPage(final Page<T> page, final String hql, final Map<String, Object> values) {
        Assert.notNull(page, "page should not be null");

        Query q = createQuery(hql, values);

        if (page.isAutoCount()) {
            long totalCount = countHqlResult(hql, values);
            page.setTotalCount(totalCount);
        }

        setPageParameter(q, page);

        List result = q.list();
        page.setResult(result);
        return page;
    }

    /**
     * Criteriaでページクエリ.
     *
     * @param page ページ引数.
     * @param criterions 複数のCriterion.
     *
     * @return 検索結果.結果のリストと検索パラメータも含める
     */
    @SuppressWarnings("unchecked")
    public Page<T> findPage(final Page<T> page, final Criterion... criterions) {
        Assert.notNull(page, "page should not be null");

        Criteria c = createCriteria(criterions);

        if (page.isAutoCount()) {
            long totalCount = countCriteriaResult(c);
            page.setTotalCount(totalCount);
        }

        setPageParameter(c, page);
        List result = c.list();
        page.setResult(result);
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<T> findPage(final Page<T> page, final DetachedCriteria detachedCriteria) {
        Assert.notNull(page, "page should not be null");

        Criteria c = detachedCriteria.getExecutableCriteria(getSession());

        if (page.isAutoCount()) {
            long totalCount = countCriteriaResult(c);
            page.setTotalCount(totalCount);
        }

        setPageParameter(c, page);
        List result = c.list();
        page.setResult(result);
        return page;
    }

    /**
     * ページ引数をQueryに設定する、補助関数
     */
    protected Query setPageParameter(final Query q, final Page<T> page) {
        //注意：実際にHibernateのfirstResultの番号は0からです。
        q.setFirstResult(page.getFirst() - 1);
        q.setMaxResults(page.getPageSize());
        return q;
    }

    /**
     * ページ引数をCriteriaに設定する、補助関数
     */
    protected Criteria setPageParameter(final Criteria c, final Page<T> page) {
        //注意：実際にHibernateのfirstResultの番号は0からです。
        c.setFirstResult(page.getFirst() - 1);
        c.setMaxResults(page.getPageSize());

        if (page.isOrderBySetted()) {
            String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
            String[] orderArray = StringUtils.split(page.getOrder(), ',');

            Assert.isTrue(orderByArray.length == orderArray.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");

            for (int i = 0; i < orderByArray.length; i++) {
                if (Page.ASC.equals(orderArray[i])) {
                    c.addOrder(Order.asc(orderByArray[i]));
                } else {
                    c.addOrder(Order.desc(orderByArray[i]));
                }
            }
        }
        return c;
    }

    /**
     * count検索を実行して今回のHQLで検索したレコードの総数を取得する。
     *
     *　この関数は簡単なHQLだけを処理できる、複雑なHQLは自分でcountを書いてください。
     */
    protected long countHqlResult(final String hql, final Object... values) {
        String fromHql = hql;
        //selectとorder byはcount検索に影響するため,簡単な削除を行う。
        fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
        fromHql = StringUtils.substringBefore(fromHql, "order by");

        String countHql = "select count(*) " + fromHql;

        try {
            Long count = findUnique(countHql, values);
            return count;
        } catch (Exception e) {
            throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
        }
    }

    /**
     * count検索を実行して今回のHQLで検索したレコードの総数を取得する。
     *
     * この関数は簡単なHQLだけを処理できる、複雑なHQLは自分でcountを書いてください。
     */
    protected long countHqlResult(final String hql, final Map<String, Object> values) {
        String fromHql = hql;
        //selectとorder byはcount検索に影響するため,簡単な削除を行う。
        fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
        fromHql = StringUtils.substringBefore(fromHql, "order by");

        String countHql = "select count(*) " + fromHql;

        try {
            Long count = findUnique(countHql, values);
            return count;
        } catch (Exception e) {
            throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
        }
    }

    /**
     * count検索を実行して今回のCriteriaで検索したレコードの総数を取得する。
     */
    @SuppressWarnings("unchecked")
    protected long countCriteriaResult(final Criteria c) {
        CriteriaImpl impl = (CriteriaImpl) c;

        // まずProjection、ResultTransformer、OrderByを取得して、NULLにした後Countを実行する
        Projection projection = impl.getProjection();
        ResultTransformer transformer = impl.getResultTransformer();

        List<CriteriaImpl.OrderEntry> orderEntries = null;
        try {
            orderEntries = (List) ReflectionUtils.getFieldValue(impl, "orderEntries");
            ReflectionUtils.setFieldValue(impl, "orderEntries", new ArrayList());
        } catch (Exception e) {
            logger.error("Exception:{}", e.getMessage());
        }

        // Countを実行する
        long totalCount = (Long) c.setProjection(Projections.rowCount()).uniqueResult();

        // この前抽出したProjectionとResultTransformerとOrderByで再び設定する
        c.setProjection(projection);

        if (projection == null) {
            c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        if (transformer != null) {
            c.setResultTransformer(transformer);
        }
        try {
            ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
        } catch (Exception e) {
            logger.error("Exception:{}", e.getMessage());
        }

        return totalCount;
    }

    //-- 属性フィルタの条件(PropertyFilter)で検索 --//
    /**
     * 属性でレコードリストを検索する.
     *
     * @param matchType マッチング,サポートしている値はPropertyFilterのMatcheType enumを参照してください.
     */
    public List<T> findBy(final String propertyName, final Object value, final MatchType matchType) {
        Criterion criterion = buildPropertyFilterCriterion(propertyName, value, matchType);
        return find(criterion);
    }

    /**
     * 属性フィルタ条件リストで検索結果リストを検索
     */
    public List<T> find(List<PropertyFilter> filters) {
        Criterion[] criterions = buildPropertyFilterCriterions(filters);
        return find(criterions);
    }

    /**
     * 属性フィルタ条件ページで検索結果リストを検索
     */
    public Page<T> findPage(final Page<T> page, final List<PropertyFilter> filters) {
        Criterion[] criterions = buildPropertyFilterCriterions(filters);
        return findPage(page, criterions);
    }

    /**
     * 属性フィルタ条件リストでCriterion[]を作成、辅助函数.
     */
    protected Criterion[] buildPropertyFilterCriterions(final List<PropertyFilter> filters) {
        List<Criterion> criterionList = new ArrayList<Criterion>();
        for (PropertyFilter filter : filters) {
            if (!filter.isMultiProperty()) { //比較する必要がある属性は一個だけある場合.
                Criterion criterion = buildPropertyFilterCriterion(filter.getPropertyName(), filter.getPropertyValue(),
                        filter.getMatchType());
                criterionList.add(criterion);
            } else {//複数の比較する必要がある属性がある場合、or処理を行う.
                Disjunction disjunction = Restrictions.disjunction();
                for (String param : filter.getPropertyNames()) {
                    Criterion criterion = buildPropertyFilterCriterion(param, filter.getPropertyValue(), filter.getMatchType());
                    disjunction.add(criterion);
                }
                criterionList.add(disjunction);
            }
        }
        return criterionList.toArray(new Criterion[criterionList.size()]);
    }

    /**
     * 属性条件引数でCriterion作成,補助関数.
     */
    protected Criterion buildPropertyFilterCriterion(final String propertyName, final Object propertyValue,
            final MatchType matchType) {
        Assert.hasText(propertyName, "propertyName should not be null");
        Criterion criterion = null;
        try {

            //MatchTypeでcriterionを生成
            if (MatchType.EQ.equals(matchType)) {
                criterion = Restrictions.eq(propertyName, propertyValue);
            } else if (MatchType.LIKE.equals(matchType)) {
                criterion = Restrictions.like(propertyName, (String) propertyValue, MatchMode.ANYWHERE);
            } else if (MatchType.LE.equals(matchType)) {
                criterion = Restrictions.le(propertyName, propertyValue);
            } else if (MatchType.LT.equals(matchType)) {
                criterion = Restrictions.lt(propertyName, propertyValue);
            } else if (MatchType.GE.equals(matchType)) {
                criterion = Restrictions.ge(propertyName, propertyValue);
            } else if (MatchType.GT.equals(matchType)) {
                criterion = Restrictions.gt(propertyName, propertyValue);
            }
        } catch (Exception e) {
            throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
        }
        return criterion;
    }

    /**
     * 対象データの属性はデータベースにUniqueかどうか判断する.
     *
     *　対象データを変更する時、もし変更しようとする属性の値(value)は元の値(orgValue)と同じの場合、比較しない
     */
    public boolean isPropertyUnique(final String propertyName, final Object newValue, final Object oldValue) {
        if (newValue == null || newValue.equals(oldValue)) {
            return true;
        }
        Object object = findUniqueBy(propertyName, newValue);
        return (object == null);
    }
}
