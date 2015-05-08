import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/*
 * @author:		Richard Gut / Patrik Studer FHNW
 * 
 */

public class JFormattedDoubleTextField extends JTextField implements
		FocusListener {
	private static final long serialVersionUID = 1L;
	private JFormattedDoubleTextField txtField = this;
	private double minValue = -Double.MAX_VALUE, maxValue = Double.MAX_VALUE,
			value;
	private DecimalFormat formatter = null;
	private int digits = 0;
	private boolean edited = false;
	private boolean errorDisplayed = false;
	private InputVerifier verifyer;

	public JFormattedDoubleTextField(int col) {
		super(col);
		init();
	}

	public JFormattedDoubleTextField(DecimalFormat formatter, int col) {
		super(col);
		this.formatter = formatter;
		init();
	}

	public JFormattedDoubleTextField(int digits, int col) {
		super(col);
		if (digits < 3 || digits > 16) {
			throw new IllegalArgumentException();
		}
		this.digits = digits;
		init();
	}

	private void init() {
		verifyer = (new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				double v = 0.0;
				try {
					v = Double.parseDouble(txtField.getText());
				} catch (NumberFormatException e) {
					errorMsg();
					return false;
				}
				if (v > maxValue || v < minValue) {
					errorMsg();
					return false;
				} else {
					if (edited)
						value = v;
					edited = false;
					if (txtField.formatter != null) {
						setText(txtField.formatter.format(value));
					} else if (digits != 0) {
						setText(toStringENG(value, digits));
					}
					return true;
				}
			}
		});

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() != KeyEvent.VK_ENTER)
					edited = true;
				char caracter = e.getKeyChar();
				if (caracter == 'd' || caracter == 'f') {
					e.consume();
					return;
				}
				int offs = txtField.getCaretPosition();
				String tfText = txtField.getText().substring(0, offs)
						+ caracter + txtField.getText().substring(offs);
				try {
					if (caracter == '-' || caracter == '+' || caracter == 'e') {
						Double.parseDouble(tfText.trim() + "1");
					} else {
						Double.parseDouble(tfText.trim());
					}
				} catch (Exception ex) {
					e.consume();
				}
			}
		});
		addFocusListener(this);
	}

	public void setValue(double value) {
		this.value = value;
		edited = false;
		if (formatter != null) {
			setText(formatter.format(value));
		} else if (digits != 0) {
			setText(toStringENG(value, digits));
		}
	}

	public double doubleValue() {
		return value;
	}

	public void setRange(double minValue, double maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		setToolTipText("" + minValue + " \u2264 value \u2264 " + maxValue);
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
		setToolTipText("" + minValue + " \u2264 value");
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
		setToolTipText("value \u2264 " + maxValue);
	}

	@Override
	public void focusGained(FocusEvent e) {
		selectAll();
	}

	@Override
	public void focusLost(FocusEvent e) {

		verifyer.verify(this);
	}

	@Override
	protected void fireActionPerformed() {
		if (verifyer.verify(this))
			super.fireActionPerformed();
	}

	private String toStringENG(double val, int digits) {
		if (val == 0)
			return String.format("%1." + (digits - 1) + "f", 0.0);

		int exp = (int) Math.floor(Math.log10(Math.abs(val)));
		int engExp = (int) Math.floor(exp / 3.0) * 3;
		int preFix = (((exp + 330) % 3) + 1);
		double engVal = val / Math.pow(10.0, engExp);

		String stFormatter = "%" + preFix + "." + (digits - preFix) + "f";

		if (engExp != 0) {
			stFormatter += "E%d";
		}
		return String.format(stFormatter, engVal, engExp);
	}

	private void errorMsg() {
		if (errorDisplayed)
			return;
		errorDisplayed = true;
		final Color color = getForeground();
		setForeground(Color.red);
		setText("Invalide Value!");
		requestFocus();
		javax.swing.Timer timer = new javax.swing.Timer(1500,
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						setForeground(color);
						if (formatter != null) {
							setText(formatter.format(value));
						} else {
							setText(toStringENG(value, digits));
						}
						selectAll();
						edited = false;
						errorDisplayed = false;
					}
				});
		timer.setRepeats(false);
		timer.start();
	}
}
