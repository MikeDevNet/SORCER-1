/**
 * @author Mike
 *
 */
package rmi.exchange_office;

import java.io.Serializable;

public class Money implements Serializable {
	
	protected double amount;
	protected Currency curr;

	public Money(double amount) {
		this.amount = amount;
		curr = new Currency();
	}

	public Money(int amount) {
		this.amount = amount;
		this.curr = new Currency();
	}
	
	public Money(double amount, String code ) {
		this.amount = amount;
		this.curr = new Currency(code);
	}

	public double getAmount() {
		return amount;
	}
	
	public String getCurrencyCode() {
		return curr.code;
	}
	
	public double exchange(Money from, Money to) {
		return 0;
	}

	public void add(Money otherMoney) {
		amount += otherMoney.getAmount();
	}

	public void subtract(Money otherMoney) {
		amount -= otherMoney.getAmount();
	}
	
	public void multiply(Money otherMoney) {
		amount *= otherMoney.getAmount();
	}
	
	public void divide(Money otherMoney) {
		amount /= otherMoney.getAmount();
	}

	public boolean greaterThan(Money otherMoney) {
		if (amount > otherMoney.getAmount()) {
			return true;
		}
		return false;
	}

	public boolean equals(Object object) {
		if (object instanceof Money) {
			Money otherMoney = (Money) object;

			return (amount == otherMoney.getAmount());
		}
		return false;
	}

	public String toString() {
		return amount + " " + curr.code;
	}

	public String value() {
		return "" + amount;
	}
}
