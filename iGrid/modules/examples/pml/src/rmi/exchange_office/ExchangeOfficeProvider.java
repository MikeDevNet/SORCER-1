package rmi.exchange_office;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import sorcer.core.SorcerConstants;
import sorcer.core.provider.ServiceTasker;
import sorcer.service.Context;
import sorcer.util.Log;

import com.sun.jini.start.LifeCycle;

@SuppressWarnings("rawtypes")
public class ExchangeOfficeProvider extends ServiceTasker implements ExchangeOffice,
		ServiceExchangeOffice, SorcerConstants {

	private static Logger logger = Log.getTestLog();
	
	private Money exchangeFromValue;
	private Money exchangeToValue;
	private double exchangeRate;

	/**
	 * Constructs an instance of the SORCER account provider implementing
	 * SorcerAccount and Account. This constructor is required by Jini 2 life
	 * cycle management.
	 * 
	 * @param args
	 * @param lifeCycle
	 * @throws Exception
	 */
	public ExchangeOfficeProvider(String[] args, LifeCycle lifeCycle) throws Exception {
		super(args, lifeCycle);
		exchangeFromValue = new Money(Double.parseDouble(getProperty("exchangeFrom")),getProperty("currencyFrom"));
		exchangeToValue = new Money(Double.parseDouble(getProperty("exchangeTo")),getProperty("currencyTo"));
	}

	public Context getExchangeRate(Context from, Context to)
			throws RemoteException, ExchangeOfficeException {
		return process(from, to, ServiceExchangeOffice.RATE);
	}
	
	public Context getCurrencyFrom(Context context) throws RemoteException,
		ExchangeOfficeException {
		return process(context , ServiceExchangeOffice.CURRENCY_FROM);
	}
	
	public Context getCurrencyTo(Context context)  throws RemoteException,
		ExchangeOfficeException {
		return process(context , ServiceExchangeOffice.CURRENCY_TO);
	}

	public Context makeExchange(Context exchangeFrom, Context exchangeTo)
			throws RemoteException, ExchangeOfficeException {
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
			context.putValue(selector + ServiceExchangeOffice.EXCHANGE + CPS + ServiceExchangeOffice.AMOUNT, result);
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
				amount = (Money) contextTo.getValue(ServiceExchangeOffice.EXCHANGE + CPS
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
					ServiceExchangeOffice.EXCHANGE + CPS + ServiceExchangeOffice.AMOUNT, result);
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
		double cash = amount.getAmount();

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
