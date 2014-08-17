package com.example.listview.models;

public class FeedResponseModel {
	private String title;
	private ListItemModel[] rows;
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the rows
	 */
	public ListItemModel[] getRows() {
		return rows;
	}
	/**
	 * @param rows the rows to set
	 */
	public void setRows(ListItemModel[] rows) {
		this.rows = rows;
	}

}
