package computerPlayer;

public class NeuralNetException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NeuralNetException(String message) {
		super(message);
	}

	public NeuralNetException(Throwable cause) {
		super(cause);
	}

	public NeuralNetException(String message, Throwable cause) {
		super(message, cause);
	}

	public NeuralNetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
