package gs.dataApi.enums;

public enum ErrorEnum {
	NO_CONTENT(204),
	INVALID_REQUEST_ERROR(400),
	REQUEST_FAILED_ERROR(402),
	APPLICATION_ERROR(500),
	NO_HANDLERS(501),
	TIMEOUT(504),
	UNKNOWN_ERROR(520);
	
	
	private int errorCode;
	
	private ErrorEnum(int code){
		this.errorCode = code;
	}
	
	public int code(){
		return errorCode;
	}
	
	public static int code(String name){
		ErrorEnum error = ErrorEnum.valueOf(name);
		
		//return corresponding code, else its default 500 (internal server error)
		if(error != null){
			return error.code();
		}else{
			return 500;
		}
	}
	public static ErrorEnum getEnum(int code){
		for(ErrorEnum error: ErrorEnum.values()){
			if(error.code() == code){
				return error;
			}
		}
		
		//return unknown error, should not come to this, 
		//must debug is unknown error is displayed to the user
		return UNKNOWN_ERROR;
	}
}
