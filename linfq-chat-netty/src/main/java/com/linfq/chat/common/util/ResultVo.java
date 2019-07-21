package com.linfq.chat.common.util;

/**
 * ResultVo.
 *
 * @author linfq
 * @date 2019/7/21 19:28
 */
public class ResultVo {

	/**
	 * 响应业务状态
	 */
	private Integer status;
	/**
	 * 响应消息
	 */
	private String msg;
	/**
	 * 响应中的数据
	 */
	private Object data;
	/**
	 * 不使用
	 */
	private String ok;

	public static ResultVo build(Integer status, String msg, Object data) {
		return new ResultVo(status, msg, data);
	}

	public static ResultVo ok(Object data) {
		return new ResultVo(data);
	}

	public static ResultVo ok() {
		return new ResultVo(null);
	}

	public static ResultVo errorMsg(String msg) {
		return new ResultVo(500, msg, null);
	}

	public static ResultVo errorMap(Object data) {
		return new ResultVo(501, "error", data);
	}

	public static ResultVo errorTokenMsg(String msg) {
		return new ResultVo(502, msg, null);
	}

	public static ResultVo errorException(String msg) {
		return new ResultVo(555, msg, null);
	}

	public ResultVo() {

	}

//    public static LeeJSONResult build(Integer status, String msg) {
//        return new LeeJSONResult(status, msg, null);
//    }

	public ResultVo(Integer status, String msg, Object data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public ResultVo(Object data) {
		this.status = 200;
		this.msg = "OK";
		this.data = data;
	}

	public Boolean isOK() {
		return this.status == 200;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getOk() {
		return ok;
	}

	public void setOk(String ok) {
		this.ok = ok;
	}
}
