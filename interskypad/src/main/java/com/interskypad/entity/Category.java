package com.interskypad.entity;

import java.util.ArrayList;

/**
 * Element类
 * @author carrey
 *
 */
public class Category {
	/** 文字内容 */
	public String contentText = "";
	/** 在tree中的层级 */
	public int level = -1;
	/** 元素的id */
	public String id = "";
	/** 父元素的id */
	public String parendId = "";
	/** 是否有子元素 */
	public ArrayList<Category> childs = new ArrayList<Category>();
	/** item是否展开 */
	public boolean isExpanded = false;

	public boolean isSelected = false;

	public Category()
	{

	};

	public Category(String contentText, int level, String id, String parendId,boolean isExpanded) {
		this.contentText = contentText;
		this.level = level;
		this.id = id;
		this.parendId = parendId;
		this.isExpanded = isExpanded;
		this.isSelected = false;
	}

	public Category(String contentText, String parendId,String id) {
		this.contentText = contentText;
		this.id = id;
		this.parendId = parendId;
		this.isSelected = false;
	}
	public Category(String contentText, int level, String parendId,String id) {
		this.contentText = contentText;
		this.level = level;
		this.id = id;
		this.parendId = parendId;
	}
}

