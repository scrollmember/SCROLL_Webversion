/**
 * Copyright (c) 2005-2009 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: Page.java 613 2009-11-01 17:09:33Z calvinxiu $
 */
package jp.ac.tokushima_u.is.ll.common.orm;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 与具体ORM实现无关的分页参数及查询结果封装.
 * 注意所有序号从1开始.
 * 
 * ORM実装と関係ないページ引数および検索結果のカプセル化
 * 注意：すべての順番は1からです。
 * 
 * @param <T> Page中记录的类型. Pageのレコードのテープ
 * 
 * @author calvin
 */
public class Page<T> {
	//-- 公共变量--//
	//-- 共用変数--//
	public static final String ASC = "asc";
	public static final String DESC = "desc";

	//-- 分页参数 --//
	//-- ページのパラメータ --// 
	protected int pageNo = 1;
	protected int pageSize = 1;
	protected String orderBy = null;
	protected String order = null;
	protected boolean autoCount = true;

	//-- 返回结果  --//
	//-- 戻り値 --//
	protected List<T> result = Collections.emptyList();
	protected long totalCount = -1;

	//-- 构造函数  --//
	//-- コンストラクタ --//
	public Page() {
	}

	public Page(int pageSize) {
		this.pageSize = pageSize;
	}

	//-- 访问查询参数函数　--//
	//-- 検索用のパラメータ --//
	/**
	 * 获得当前页的页号,序号从1开始,默认为1.
	 * 現在ページのページ番号を取得する。番号は1からです、デフォルト値は1です。
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
	 * 現在ページのページ番号を設定する、番号は1からです。<1の場合は自動的に1にする。
	 */
	public void setPageNo(final int pageNo) {
		this.pageNo = pageNo;

		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}

	public Page<T> pageNo(final int thePageNo) {
		setPageNo(thePageNo);
		return this;
	}

	/**
	 * 获得每页的记录数量,默认为1.
	 * ページサイズを取得、デフォルト値は1です。
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页的记录数量,低于1时自动调整为1.
	 * ページサイズを設定する、<1の場合は1にする。
	 */
	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;

		if (pageSize < 1) {
			this.pageSize = 1;
		}
	}

	public Page<T> pageSize(final int thePageSize) {
		setPageSize(thePageSize);
		return this;
	}

	/**
	 * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
	 * pageNoとpageSizeより現在ページの1番目のレコードは全レコードの中におる位置を取得、番号は1からです。
	 */
	public int getFirst() {
		return ((pageNo - 1) * pageSize) + 1;
	}

	/**
	 * 获得排序字段,无默认值.多个排序字段时用','分隔.
	 * ソート順を取得する。複数がある場合、','で区切。
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 设置排序字段,多个排序字段时用','分隔.
	 * ソート順を設定する。複数がある場合、','で区切。
	 */
	public void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}

	public Page<T> orderBy(final String theOrderBy) {
		setOrderBy(theOrderBy);
		return this;
	}

	/**
	 * 获得排序方向.
	 * 昇順か降順か
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * 设置排序方式向.
	 * 昇順か降順か
	 * 
	 * @param order 可选值为desc或asc,多个排序字段时用','分隔.
	 */
	public void setOrder(final String order) {
		//检查order字符串的合法值
		String[] orders = StringUtils.split(StringUtils.lowerCase(order), ',');
		for (String orderStr : orders) {
			if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr))
				throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
		}

		this.order = StringUtils.lowerCase(order);
	}

	public Page<T> order(final String theOrder) {
		setOrder(theOrder);
		return this;
	}

	/**
	 * 是否已设置排序字段,无默认值.
	 * ソード順を設定しているかどうか、デフォルト値はなし
	 */
	public boolean isOrderBySetted() {
		return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order));
	}

	/**
	 * 查询对象时是否自动另外执行count查询获取总记录数, 默认为false.
	 * 検索するとき自動的にレコードの総数を計算するかどうか
	 */
	public boolean isAutoCount() {
		return autoCount;
	}

	/**
	 * 查询对象时是否自动另外执行count查询获取总记录数.
	 * 検索するとき自動的にレコードの総数を計算するかどうか
	 */
	public void setAutoCount(final boolean autoCount) {
		this.autoCount = autoCount;
	}

	public Page<T> autoCount(final boolean theAutoCount) {
		setAutoCount(theAutoCount);
		return this;
	}

	//-- 访问查询结果函数 --//

	/**
	 * 取得页内的记录列表.
	 * ページ内のレコードを取得
	 */
	public List<T> getResult() {
		return result;
	}

	/**
	 * 设置页内的记录列表.
	 * ページ内のレコードを設定
	 */
	public void setResult(final List<T> result) {
		this.result = result;
	}

	/**
	 * 取得总记录数, 默认值为-1.
	 * レコードの総数を取得
	 */
	public long getTotalCount() {
		return totalCount;
	}

	/**
	 * 设置总记录数.
	 * レコードの総数を設定
	 */
	public void setTotalCount(final long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 根据pageSize与totalCount计算总页数, 默认值为-1.
	 * pageSizeとtotalCountより全ページ数を計算する、デフォルト値は-1.
	 */
	public long getTotalPages() {
		if (totalCount < 0)
			return -1;

		long count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	/**
	 * 是否还有下一页.
	 * 次のページがあるかどうか
	 */
	public boolean isHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}

	/**
	 * 取得下页的页号, 序号从1开始.
	 * 当前页为尾页时仍返回尾页序号.
	 * 次のページの番号を取得、番号は1からです。
	 * 現在のページは最後ページだったら、 最後ページの番号を戻す。
	 */
	public int getNextPage() {
		if (isHasNext())
			return pageNo + 1;
		else
			return pageNo;
	}

	/**
	 * 是否还有上一页.
	 * 前のページがあるかどうか
	 */
	public boolean isHasPre() {
		return (pageNo - 1 >= 1);
	}

	/**
	 * 取得上页的页号, 序号从1开始.
	 * 当前页为首页时返回首页序号.
	 * 前のページの番号を取得、番号は1からです。
	 * 現在のページは最初ページだったら、最初ページの番号を戻す
	 */
	public int getPrePage() {
		if (isHasPre())
			return pageNo - 1;
		else
			return pageNo;
	}
}
