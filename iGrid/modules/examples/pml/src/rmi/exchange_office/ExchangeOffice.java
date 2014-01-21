/**
 * @author Mike
 *
 */
package rmi.exchange_office;

import java.rmi.*;

public interface ExchangeOffice extends Remote {

	public double getExchangeRate(Currency from, Currency to) 
        	throws RemoteException;
        
    public Money getCurrencyFrom() 
    	throws RemoteException;
    
    public Money getCurrencyTo() 
    	throws RemoteException;
    
    public void makeExchange(Money exchangeFrom, Money exchangeTo) 
    	throws RemoteException,NegativeAmountException, ExceededException;
}