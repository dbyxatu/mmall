package dby.common;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;



/**
 * 
 * Title: ServerResponse Description:
 * 
 * @author 董博源
 * @date 2019/11/25 10:47:18
 */
/**
 * 对null值的属性不进行json拼接
 * 保证序列化json的时候，如果是null的对象，key也会消失
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6378903045469378769L;
	private int status;
	private String msg;
	private T data;

	private ServerResponse(int status) {
		this.status = status;
	}

	private ServerResponse(int status, T data) {
		this.status = status;
		this.data = data;
	}

	private ServerResponse(int status, String msg, T data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	private ServerResponse(int status, String msg) {
		this.status = status;
		this.msg = msg;
	}


	/**
	 * 在序列化后，不在JSON中显示该字段
	 * 即使之不在json序列化结果当中
	 * @return
	 */
	@JsonIgnore
	public boolean isSuccess() {
		return this.status == ResponseCode.SUCESS.getCode();
	}

	public int getStatus() {
		return status;
	}

	public T getData() {
		return data;
	}

	public String getMsg() {
		return msg;
	}

	/**
	 * 正确返回
	 * 
	 * @return
	 */
	public static <T> ServerResponse<T> createBySuccess() {
		return new ServerResponse<>(ResponseCode.SUCESS.getCode());
	}

	public static <T> ServerResponse<T> createBySuccessStatusMessage(String msg) {
		return new ServerResponse<>(ResponseCode.SUCESS.getCode(), msg);
	}

	public static <T> ServerResponse<T> createBySuccessStatusData(T data) {
		return new ServerResponse<>(ResponseCode.SUCESS.getCode(), data);
	}

	public static <T> ServerResponse<T> createBySuccessStatusMessageData(String msg, T data) {
		return new ServerResponse<>(ResponseCode.SUCESS.getCode(), msg, data);
	}

	/**
	 * 错误返回
	 * 
	 * @return
	 */
	public static <T> ServerResponse<T> createByError() {
		return new ServerResponse<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
	}

	public static <T> ServerResponse<T> createByErrorMessage(String errorMessage) {
		return new ServerResponse<>(ResponseCode.ERROR.getCode(), errorMessage);
	}

	public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
		return new ServerResponse<>(errorCode, errorMessage);
	}
}