package gs.dataApi.exceptions;

public class ApplicationSetupException extends Exception{
	private static final long serialVersionUID = 1L;
	
	public ApplicationSetupException() { super(); }
	public ApplicationSetupException(String message) { super(message); }
	public ApplicationSetupException(String message, Throwable cause) { super(message, cause); }
	public ApplicationSetupException(Throwable cause) { super(cause); }
}
