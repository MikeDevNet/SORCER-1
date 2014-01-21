/**
 * @author Mike
 *
 */
package rmi.exchange_office;

import java.rmi.RemoteException;

public class ExchangeOfficeImpl implements ExchangeOffice {

private Money exchangeFromValue;
private Money exchangeToValue;
private double exchangeRate;

public ExchangeOfficeImpl(Money from, Money to) throws RemoteException {
	exchangeFromValue = from;
	exchangeToValue = to;
}


public void makeExchange(Money exchangeFrom, Money exchangeTo)
		throws RemoteException, NegativeAmountException, ExceededException {
	checkForNegativeAmount(exchangeFrom);
	checkForNegativeAmount(exchangeTo);
	
	double tmp = exchangeFrom.amount*exchangeRate;
	if(exchangeTo.amount > 0 ){
		exchangeTo.amount += tmp;
	}
	else exchangeTo.amount = tmp;
	
	return;
}


private void checkForNegativeAmount(Money amount)
		throws NegativeAmountException {
	float cash = amount.getAmount();

	if (0 > cash) {
		throw new NegativeAmountException();
	}
}

private void checkForExceed(Money amount) throws ExceededException {
	if (amount.greaterThan(exchangeFromValue) ||
		amount.greaterThan(exchangeToValue)) {
		throw new ExceededException(false);
	}
	return;
}

private boolean checkConversionRate(Money from, Money to) {
	exchangeRate = 0.32f; // m w km
	if(exchangeRate > 1) return true;
	else return false;
}

public double getExchangeRate(Currency from, Currency to) throws RemoteException {
	return exchangeRate;
}

public Money getCurrencyFrom() throws RemoteException {
	return exchangeFromValue;
}


public Money getCurrencyTo() throws RemoteException {
	return exchangeToValue;
}

}
