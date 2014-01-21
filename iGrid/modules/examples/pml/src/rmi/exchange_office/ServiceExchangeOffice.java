package rmi.exchange_office;

import java.rmi.Remote;
import java.rmi.RemoteException;

import sorcer.service.Context;

@SuppressWarnings("rawtypes")
public interface ServiceExchangeOffice extends Remote {

	public Context getExchangeRate(Context from, Context to) throws RemoteException,
		ExchangeOfficeException;
	
	public Context getCurrencyFrom(Context context) throws RemoteException,
	ExchangeOfficeException;
	
	
	public Context getCurrencyTo(Context context)  throws RemoteException,
	ExchangeOfficeException;


	public Context makeExchange(Context exchangeFrom, Context exchangeTo) throws RemoteException,
		ExchangeOfficeException;

	public final static String RATE = "rate";
	
	public final static String EXCHANGE = "exchange";

	public final static String AMOUNT = "amount";

	public final static String COMMENT = "comment";
	
	public final static String CURRENCY_FROM = "comment";
	
	public final static String CURRENCY_TO = "comment";
}
