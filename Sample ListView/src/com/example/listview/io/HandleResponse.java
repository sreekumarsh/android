package com.example.listview.io;

/**
 * Interface to pass data from the service thread to the UI thread
 * @author Sreekumar
 *
 */
public interface HandleResponse {
	/**
	 * Pass the {@code BaseResponse} Object to UI thread
	 * @param responseData - BaseResponse
	 */
	public void handleResponse(BaseResponse responseData);
}
