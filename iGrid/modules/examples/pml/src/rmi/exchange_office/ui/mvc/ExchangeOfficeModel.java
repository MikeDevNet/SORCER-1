package rmi.exchange_office.ui.mvc;

import java.util.Observable;

import sorcer.account.provider.Money;

public class ExchangeOfficeModel extends Observable {

	private Money balance;

	private Money withdrawalAmount;

	private Money depositAmount;

	final static String DEPOSIT = "$ Deposit";

	final static String WITHDRAW = "$ Withdraw";

	final static String BALANCE = "Balance";

	public ExchangeOfficeModel() {
		this.balance = new Money(0);
	}

	public ExchangeOfficeModel(Money balance) {
		this.balance = balance;
	}

	public void setBalance(Money balance) {
		this.balance = balance;
		setChanged();
		notifyObservers(BALANCE);
	}

	public Money getBalance() {
		return balance;
	}

	public Money getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(Money depositAmount) {
		this.depositAmount = depositAmount;
		setChanged();
		notifyObservers(DEPOSIT);
	}

	public Money getWithdrawalAmount() {
		return withdrawalAmount;
	}

	public void setWithdrawalAmount(Money withdrawalAmount) {
		this.withdrawalAmount = withdrawalAmount;
		setChanged();
		notifyObservers(WITHDRAW);
	}
}