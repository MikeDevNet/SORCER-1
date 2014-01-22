/**
 * @author Mike
 *
 */
package rmi.exchange_office;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Currency implements Serializable {

	public List<String> codes;
	
	protected String code;

	public Currency() {
		this.code = "PLN";
	}

	public Currency(String code) {
		this.code = code;
		codes = new ArrayList<String>();
	}
	
	public String getCode() {
		return code;
	}

	public boolean isCodeSupported(Currency newCode) {
		if (codes.contains(code)) {
			return true;
		}
		return false;
	}

	public boolean equals(Object object) {
		if (object instanceof Currency) {
			Currency newCurrency = (Currency) object;

			return (code == newCurrency.getCode());
		}
		return false;
	}

	public String toString() {
		return code;
	}

	public String value() {
		return "" + code;
	}
}
