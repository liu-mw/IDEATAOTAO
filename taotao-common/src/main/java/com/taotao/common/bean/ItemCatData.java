package com.taotao.common.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class ItemCatData  {

	@JsonProperty("u") // 序列化成json数据时为 u
	private String url;

	@JsonProperty("n")
	private String nname;

	@JsonProperty("i")
	private List<?> items;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNname() {
		return nname;
	}

	public void setNname(String nname) {
		this.nname = nname;
	}

	public List<?> getItems() {
		return items;
	}

	public void setItems(List<?> items) {
		this.items = items;
	}
	
	

}
