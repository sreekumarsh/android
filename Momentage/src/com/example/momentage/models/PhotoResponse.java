package com.example.momentage.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.example.momentage.models.sys.Meta;
import com.example.momentage.models.sys.Result;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PhotoResponse {

	private Error error;
	private Result result;
	private Meta meta;

	/**
	 * @return the error
	 */
	public Error getError() {
		return error;
	}

	/**
	 * @param error
	 *            the error to set
	 */
	public void setError(Error error) {
		this.error = error;
	}

	/**
	 * @return the result
	 */
	public Result getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(Result result) {
		this.result = result;
	}

	/**
	 * @return the meta
	 */
	public Meta getMeta() {
		return meta;
	}

	/**
	 * @param meta
	 *            the meta to set
	 */
	public void setMeta(Meta meta) {
		this.meta = meta;
	}

}
