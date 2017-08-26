package com.hit.memoryunits;

import java.io.Serializable;

public class Page<T> extends Object implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3339336743990959909L;
	private T content;
	private 	Long pageID;
	
	
	public Page(Long id,  T content) {
			this.content = content;
			this.pageID = id;
	}
	
	public Long getPageId(){
		return this.pageID;
	}
	
	public void setPageId(Long pageId) {
		this.pageID = pageId;
	}
	
	public T getContent() {
		return this.content;
	}
	
	public void setContent(T content) {
		this.content = content;
	}
	
	public int hashCode() {
		return Long.hashCode(this.pageID);
	}
	
	public boolean equals(Object obj) {
		if(!(obj instanceof Page<?>))
			return false;
		return (this.pageID == ((Page<?>)obj).pageID? true:false);
	}
	
	public String toString(){
		return "Content is: " + content;
	}
}
