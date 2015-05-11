package jp.ac.tokushima_u.is.ll.exception;

/**
 *
 * @author lemonrain
 */
public class NotFoundException extends Exception {
	private static final long serialVersionUID = -7895799828574371561L;

	public NotFoundException(String mes){
        super(mes);
    }

    public NotFoundException(){
        super();
    }
}
