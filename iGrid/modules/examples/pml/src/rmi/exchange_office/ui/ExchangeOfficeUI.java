package rmi.exchange_office.ui;

/**
 * @author Micha³ Kowalski
 */

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rmi.exchange_office.*;

import net.jini.core.lookup.ServiceItem;
import net.jini.lookup.entry.UIDescriptor;
import net.jini.lookup.ui.MainUI;
import rmi.exchange_office.ExchangeOffice;
import sorcer.core.provider.ServiceProvider;
import sorcer.ui.serviceui.UIComponentFactory;
import sorcer.ui.serviceui.UIDescriptorFactory;
import sorcer.util.Sorcer;

public class ExchangeOfficeUI extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField exchangeFromTextField;

	private JTextField exchangeToTextField;

	private ExchangeOffice exchange;
	private Money exchangeFrom;
	private Money exchangeTo;
	
	private boolean fromToDirection;

	private ServiceItem item;

	private final static Logger logger = Logger.getLogger(ExchangeOfficeUI.class
			.getName());

	public ExchangeOfficeUI(Object provider) {
		super();
		fromToDirection = true;
		getAccessibleContext().setAccessibleName("Exchange Office Tester");
		item = (ServiceItem) provider;

		if (item.service instanceof ExchangeOffice) {
			exchange = (ExchangeOffice) item.service;
			createUI();
		}
	}

	protected void createUI() {
		setLayout(new BorderLayout());
		add(buildAccountPanel(), BorderLayout.CENTER);
		resetExchangeFromField();
		resetExchangeToField();
	}

	private void resetExchangeFromField() {
		try {
			exchangeFrom = exchange.getCurrencyFrom();
			exchangeFromTextField.setText(exchangeFrom.value());
			//exchangeFromTextField.setText(exchangeFrom.value());
			
		} catch (Exception e) {
			logger.info("Error occurred while getting start currency amount");
			logger.throwing(getClass().getName(), "resetExchangeFromField", e);
		}
	}
	
	private void resetExchangeToField() {
		try {
			exchangeTo = exchange.getCurrencyTo();
			exchangeToTextField.setText(exchangeTo.value());
			//exchangeFromTextField.setText(exchangeTo.value());
		} catch (Exception e) {
			logger.info("Error occurred while getting end currency amount");
			logger.throwing(getClass().getName(), "resetExchangeToField", e);
		}
	}

	private JPanel buildAccountPanel() {
		JPanel panel = new JPanel();
		JPanel actionPanel = new JPanel(new GridLayout(3, 3));

		actionPanel.add(new JLabel("Konwerter walut"));
		
		actionPanel.add(new JLabel("Kwota w "+exchangeFrom.getCurrencyCode()));
		exchangeFromTextField = new JTextField();
		exchangeFromTextField.setEnabled(true);
		actionPanel.add(exchangeFromTextField);
		actionPanel.add(new JLabel(exchangeFrom.getCurrencyCode()));
		
		actionPanel.add(new JLabel("Kwota w "+exchangeTo.getCurrencyCode()));
		exchangeToTextField = new JTextField();
		exchangeToTextField.setEnabled(true);
		actionPanel.add(exchangeToTextField);
		actionPanel.add(new JLabel(exchangeTo.getCurrencyCode()));

		JButton exchangeButton = new JButton("Exchange");
		exchangeButton.addActionListener(new ExchangeAction());
		actionPanel.add(exchangeButton);

		panel.add(actionPanel);
		return panel;
	}

	private Money readTextField(JTextField moneyField) {
		try {
			double actualValue = new Double(moneyField.getText());
			return new Money(actualValue);
		} 
		catch (Exception e) {
			logger.info("Field doesn't contain a valid value");
		}
		return null;
	}

	private class ExchangeAction implements ActionListener {
		double result = 0;
		
		public void actionPerformed(ActionEvent event) {
			try {
				if(fromToDirection){
					Money exchangeAmount = readTextField(exchangeFromTextField);
					result = exchangeAmount.exchange(exchangeFrom, exchangeAmount);
					exchangeToTextField.setText(""+result);
					exchangeFromTextField.setText("");
					resetExchangeFromField();
				}
				else {
					Money exchangeAmount = readTextField(exchangeToTextField);
					result = exchangeAmount.exchange(exchangeAmount, exchangeTo);
					exchangeFromTextField.setText(""+result);
					exchangeToTextField.setText("");
					resetExchangeToField();
				}
			} catch (Exception exception) {
				logger.info("Couldn't talk to exchanger. Error was \n"
						+ exception);
				logger.throwing(getClass().getName(), "actionPerformed",
						exception);
			}
		}
	}

	/**
	 * Returns a service UI descriptor for this service. Usally this method is
	 * used as an entry in provider configuration files when smart proxies are
	 * deployed with a standard off the shelf {@link ServiceProvider}.
	 * 
	 * @return service UI descriptor
	 */
	public static UIDescriptor getUIDescriptor() {
		UIDescriptor uiDesc = null;
		try {
			uiDesc = UIDescriptorFactory.getUIDescriptor(MainUI.ROLE,
					new UIComponentFactory(new URL[] { new URL(Sorcer
							.getWebsterUrl()
							+ "/ExchangeOffice-ui.jar") }, ExchangeOfficeUI.class.getName()));
		} catch (Exception ex) {
			logger.throwing(ExchangeOfficeUI.class.getName(), "getUIDescriptor", ex);
		}
		return uiDesc;
	}
}
