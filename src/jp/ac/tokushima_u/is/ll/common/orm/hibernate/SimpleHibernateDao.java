/**
 * Copyright (c) 2005-2009 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: SimpleHibernateDao.java 624 2009-11-05 13:31:47Z calvinxiu $
 */
package jp.ac.tokushima_u.is.ll.common.orm.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.ac.tokushima_u.is.ll.util.ReflectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Hibernate DAO汎用抽象クラス。
 * 
 * Service層で直接使える。また拡張泛型DAOサブクラスでも使える。
 * Spring2.5のPetlinc例を参考して,HibernateTemplateを使わずHibernateのAPIを使っています.
 * 
 * @param <T> DAO　DAOの対象データのテープ
 * @param <PK> 主キーのテープ
 * 
 * @author calvin
 */
@SuppressWarnings("unchecked")
public class SimpleHibernateDao<T, PK extends Serializable> {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected SessionFactory sessionFactory;
    protected Class<T> entityClass;

    /**
     * DAOのサブクラス用のコンストラクタ
     * 通过子类的泛型定义取得对象类型Class.
     * eg.
     * public class UserDao extends SimpleHibernateDao<User, Long>
     */
    public SimpleHibernateDao() {
        this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
    }

    /**
     * Dao層を省略して、Service層で直接使うためのコンストラクタ
     * eg.
     * SimpleHibernateDao<User, Long> userDao = new SimpleHibernateDao<User, Long>(sessionFactory, User.class);
     */
    public SimpleHibernateDao(final SessionFactory sessionFactory, final Class<T> entityClass) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
    }

    /**
     * sessionFactoryを取得.
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Annotation　@Autowired　を通してSessionFactoryを取得する、複数のSesionFactoryがある場合この関数をOverride
     */
    @Autowired
    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 現在のSessionを取得
     */
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 新規登録また更新
     */
    public void save(final T entity) {
        Assert.notNull(entity, "entity should not be null");
        getSession().saveOrUpdate(entity);
        logger.debug("save entity: {}", entity);
    }

    /**
     * 削除
     *
     * @param entity オブジェクトは必ずsessionの中のオブジェクトまたはidを含めているtransientオブジェクト.
     */
    public void delete(final T entity) {
        Assert.notNull(entity, "entity should not be null");
        getSession().delete(entity);
        logger.debug("delete entity: {}", entity);
    }

    /**
     * idで削除
     */
    public void delete(final PK id) {
        Assert.notNull(id, "id should not be null");
        delete(get(id));
        logger.debug("delete entity {},id is {}", entityClass.getSimpleName(), id);
    }

    /**
     * idでデータを取得
     */
    public T get(final PK id) {
        Assert.notNull(id, "id should not be null");
        return (T) getSession().load(entityClass, id);
    }

    /**
     *	すべてのデータを取得.
     */
    public List<T> getAll() {
        return find();
    }

    /**
     *	すべてのデータを取得,ソートをサポートしている.
     */
    public List<T> getAll(String orderBy, boolean isAsc) {
        Criteria c = createCriteria();
        if (isAsc) {
            c.addOrder(Order.asc(orderBy));
        } else {
            c.addOrder(Order.desc(orderBy));
        }
        return c.list();
    }

    /**
     * 属性でデータリストを取得,マッチング方法はeq.
     */
    public List<T> findBy(final String propertyName, final Object value) {
        Assert.hasText(propertyName, "propertyName不能为空");
        Criterion criterion = Restrictions.eq(propertyName, value);
        return find(criterion);
    }

    /**
     * 属性でユニークなデータを取得,マッチング方法はeq.
     */
    public T findUniqueBy(final String propertyName, final Object value) {
        Assert.hasText(propertyName, "propertyName不能为空");
        Criterion criterion = Restrictions.eq(propertyName, value);
        return (T) createCriteria(criterion).uniqueResult();
    }

    /**
     * idリストでデータリストを取得.
     */
    public List<T> findByIds(List<PK> ids) {
        return find(Restrictions.in(getIdName(), ids));
    }

    /**
     * HQLでデータリストを取得.
     *
     * @param values HQLの引数、順番で設定する.
     */
    public <X> List<X> find(final String hql, final Object... values) {
        return createQuery(hql, values).list();
    }

    /**
     * HQLでデータリストを取得.
     *
     * @param values 命名引数,名称で設定する.
     */
    public <X> List<X> find(final String hql, final Map<String, Object> values) {
        return createQuery(hql, values).list();
    }

    /**
     * HQLでユニークなデータを取得.
     *
     * @param values HQL引数,順番で設定する.
     */
    public <X> X findUnique(final String hql, final Object... values) {
        return (X) createQuery(hql, values).uniqueResult();
    }

    /**
     * HQLでユニークなデータを取得.
     *
     * @param values 命名引数,名称で設定する.
     */
    public <X> X findUnique(final String hql, final Map<String, Object> values) {
        return (X) createQuery(hql, values).uniqueResult();
    }

    /**
     * HQLでユニークなデータを取得バッチ更新・削除.
     */
    public int batchExecute(final String hql, final Object... values) {
        return createQuery(hql, values).executeUpdate();
    }

    /**
     * HQLでユニークなデータを取得バッチ更新・削除.
     * @return 変更したレコード数.
     */
    public int batchExecute(final String hql, final Map<String, Object> values) {
        return createQuery(hql, values).executeUpdate();
    }

    /**
     * HQLと引数リストでQueryを生成
     *
     * このクラスのfind()関数の戻り値のテープはすべてTです、Tではない場合はこの関数をつかう.
     *
     * @param values HQL引数,順番で設定する.
     */
    public Query createQuery(final String queryString, final Object... values) {
        Assert.hasText(queryString, "queryString should not be null");
        Query query = getSession().createQuery(queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query;
    }

    /**
     * HQLと引数リストでQueryを生成
     *
     * @param values 命名引数,名称で設定する.
     */
    public Query createQuery(final String queryString, final Map<String, Object> values) {
        Assert.hasText(queryString, "queryString should not be null");
        Query query = getSession().createQuery(queryString);
        if (values != null) {
            query.setProperties(values);
        }
        return query;
    }

    /**
     * Criteriaでデータリストを取得.
     *
     * @param criterions Criterion.
     */
    public List<T> find(final Criterion... criterions) {
        return createCriteria(criterions).list();
    }

    public List<T> find(DetachedCriteria detachedCriteria) {
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        return criteria.list();
    }

    /**
     * Criteriaでユニークなデータを取得.
     *
     * @param criterions Criterion.
     */
    public T findUnique(final Criterion... criterions) {
        return (T) createCriteria(criterions).uniqueResult();
    }

    /**
     * CriterionでCriteriaを生成.
     *
     * このクラスのfind()関数の戻り値のテープはすべてTです、Tではない場合はこの関数をつかう.
     *
     * @param criterions Criterion.
     */
    public Criteria createCriteria(final Criterion... criterions) {
        Criteria criteria = getSession().createCriteria(entityClass);
        for (Criterion c : criterions) {
            criteria.add(c);
        }
        return criteria;
    }

    /**
     * 対象データを初期化
     * load()で取得したデータはデータのProxyです, View層に渡す前に初期化が必要だ.
     * entityの一般的な属性だけを初期化する,関連している属性および集合などを初期化しない.
     * 関連属性をしたい時、他の関数を使います、例えば:
     * Hibernate.initialize(user.getRoles())，Userの一般的な属性と関連している集合を初期化.
     * Hibernate.initialize(user.getDescription())，Userの一般的な属性とLazy　LoadのDescription属性を初期化する.
     */
    public void initEntity(T entity) {
        Hibernate.initialize(entity);
    }

    /**
     * @see #initEntity(Object)
     */
    public void initEntity(List<T> entityList) {
        for (T entity : entityList) {
            Hibernate.initialize(entity);
        }
    }

    /**
     * Queryにdistinct transformerを追加.
     */
    public Query distinct(Query query) {
        query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return query;
    }

    /**
     * Criteriaにdistinct transformerを追加.
     */
    public Criteria distinct(Criteria criteria) {
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria;
    }

    /**
     * Setで重複データを含めているリストをユニーク化
     * 主にHQL/Criteriaに関連集合のPreloadのために重複しているレコードを残っていると共にしdistinct使えない場合.
     */
    public <X> List<X> distinct(@SuppressWarnings("rawtypes") List list) {
        Set<X> set = new LinkedHashSet<X>(list);
        return new ArrayList<X>(set);
    }

    /**
     * 対象データの主キー名を取得.
     */
    public String getIdName() {
        ClassMetadata meta = getSessionFactory().getClassMetadata(entityClass);
        return meta.getIdentifierPropertyName();
    }
}
