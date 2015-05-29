package View;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class Console extends JTextField {
	private static final long serialVersionUID = 1L;
	
	public final static int error = 0;
	public final static int warning = 1;
	public final static int success = 9001; //It's over 9000!!
	
	private boolean displayingError = false;
	
	public Console(){
		super();
		super.setEditable(false);
		super.setHighlighter(null);
	}
	
	public void log(String msg){
		setText(msg);
	}
	
	public void log(String msg, int msgTyp){
		final Color color = getBackground();
		final Color textColor = getForeground();
		Color msgColor;
		
		switch (msgTyp){
		case error:
			msgColor = Color.red;
			break;
		case warning:
			msgColor = Color.orange;
			break;
		case success:
			msgColor = Color.green.darker();
			break;
		default:
			msgColor = color;
		}
		
		if (!displayingError){
			setBackground(msgColor);
			setForeground(Color.white);
			log(msg);
			
			javax.swing.Timer timer = new javax.swing.Timer(3000,
					new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							setBackground(color);
							setForeground(textColor);
						}
					});
			
			timer.setRepeats(false);
			timer.start();
		}
	}

}
