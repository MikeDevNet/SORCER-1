package rmi.exchange_office;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.logging.Logger;
import java.net.UnknownHostException;
import java.rmi.Remote;

import net.jini.admin.Administrable;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import sorcer.core.SorcerConstants;
import sorcer.core.provider.Provider;
import sorcer.core.proxy.Partnership;
import sorcer.core.proxy.RemotePartner;
import sorcer.service.Context;
import sorcer.service.Exertion;
import sorcer.service.ExertionException;
import sorcer.util.Log;

@SuppressWarnings("rawtypes")
public class ServiceExchangeOfficeImpl implements ExchangeOffice,
		ServiceExchangeOffice, SorcerConstants {

	private static Logger logger = Log.getTestLog();
	
	private Money exchangeFromValue;
	private Money exchangeToValue;
	private double exchangeRate;
	
	private Provider partner;
	private Administrable admin;
	
	public Remote getInner() throws RemoteException {
		return (Remote) partner;
	}
	
	public Exertion service(Exertion exertion, Transaction transaction)
			throws RemoteException, ExertionException, TransactionException {
		return partner.service(exertion, null);
	}

	public Object getAdmin() throws RemoteException {
		return admin;
	}

	public void setInner(Object provider) {
		partner = (Provider) provider;
	}

	public void setAdmin(Object admin) {
		this.admin = (Administrable) admin;
	}

	/**
	 * Constructs an instance of the SORCER account provider implementing
	 * SorcerAccount and Account. This constructor is required by Jini 2 life
	 * cycle management.
	 * 
	 * @param args
	 * @param lifeCycle
	 * @throws Exception
	 */
	public ServiceExchangeOfficeImpl(Money startingFromValue, Money startingToValue) throws RemoteException {
		exchangeFromValue = startingFromValue;
		exchangeToValue = startingToValue;
	}

	public Context getExchangeRate(Context from, Context to)
			throws RemoteException {
		return process(from, to, ServiceExchangeOffice.RATE);
	}
	
	public Context getCurrencyFrom(Context context) throws RemoteException{
		return process(context , ServiceExchangeOffice.CURRENCY_FROM);
	}
	
	public Context getCurrencyTo(Context context)  throws RemoteException {
		return process(context , ServiceExchangeOffice.CURRENCY_TO);
	}

	public Context makeExchange(Context exchangeFrom, Context exchangeTo)
			throws RemoteException {
		return process(exchangeFrom, exchangeTo, ServiceExchangeOffice.EXCHANGE);
	}
	
	private Context process(Context context, String selector)
			throws RemoteException, ExchangeOfficeException {
		try {
			logger.info("input context: \n" + context);

			Money result = null, amount = null;
			if (selector.equals(ServiceExchangeOffice.CURRENCY_TO)) {
				result = getCurrencyTo();
			} else if (selector.equals(ServiceExchangeOffice.CURRENCY_FROM)) {
				result = getCurrencyFrom();
			} 
			// set return value
			if (context.getReturnPath() != null) {
				context.setReturnValue(result);
			}
			logger.info(selector + " result: \n" + result);
			String outputMessage = "processed by " + getHostname();
			context.putValue(selector + CPS +
					ServiceExchangeOffice.BALANCE + CPS + ServiceExchangeOffice.AMOUNT, result);
			context.putValue(ServiceExchangeOffice.COMMENT, outputMessage);

		} catch (Exception ex) {
			throw new ExchangeOfficeException(ex);
		}
		return context;
	}
	
	private Context process(Context contextFrom, Context contextTo, String selector)
			throws RemoteException, ExchangeOfficeException {
		try {
			logger.info("input context: \n" + contextFrom + " + " +contextTo);

			Money result = null, amount = null;
			if (selector.equals(ServiceExchangeOffice.EXCHANGE)) {
				amount = (Money) contextTo.getValue(ServiceExchangeOffice.DEPOSIT + CPS
						+ ServiceExchangeOffice.AMOUNT);
				makeExchange(amount, amount);
				result = getCurrencyTo();
			} 
			// set return value
			if (contextTo.getReturnPath() != null) {
				contextTo.setReturnValue(result);
			}
			logger.info(selector + " result: \n" + result);
			String outputMessage = "processed by " + getHostname();
			contextFrom.putValue(selector + CPS +
					ServiceExchangeOffice.BALANCE + CPS + ServiceExchangeOffice.AMOUNT, result);
			contextTo.putValue(ServiceExchangeOffice.COMMENT, outputMessage);

		} catch (Exception ex) {
			throw new ExchangeOfficeException(ex);
		}
		return contextTo;
	}
	
	public double getExchangeRate(Currency from, Currency to)
			throws RemoteException {
		return exchangeRate;
	}


	public Money getCurrencyFrom() throws RemoteException {
		return exchangeFromValue;
	}


	public Money getCurrencyTo() throws RemoteException {
		return exchangeToValue;
	}

	public void makeExchange(Money exchangeFrom, Money exchangeTo)
			throws RemoteException, NegativeAmountException, ExceededException {
		checkForNegativeAmount(exchangeFrom);
		checkForNegativeAmount(exchangeTo);
		makeExchange(exchangeFrom, exchangeTo);
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

	/**
	 * Returns name of the local host.
	 * 
	 * @return local host name
	 * @throws UnknownHostException
	 */
	private String getHostname() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName();
	}
}
