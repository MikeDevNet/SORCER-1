package rmi.exchange_office;

public class ExchangeOfficeException extends Exception {
	public boolean exchangeSucceeded;

	public ExchangeOfficeException(Exception cause) {
		super(cause);
	}
}
