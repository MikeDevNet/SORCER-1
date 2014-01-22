package rmi.exchange_office;

public class ExceededException extends Exception {
	public boolean exchangeSucceeded;

	public ExceededException(boolean exchangeSucceeded) {
		this.exchangeSucceeded = exchangeSucceeded;
	}

	public boolean isExchangeSucceeded() {
		return exchangeSucceeded;
	}
}
