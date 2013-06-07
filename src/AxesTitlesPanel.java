import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class AxesTitlesPanel extends JPanel {

	public AxesTitlesPanel(final JFrame frame, final PointsList pomiary, final double a, final double b) {
		
		final JTextField title = new JTextField(Messages.getString("AxesTitlesPanel.0")); //$NON-NLS-1$
		final JTextField axisX = new JTextField(Messages.getString("AxesTitlesPanel.1")); //$NON-NLS-1$
		final JTextField axisY = new JTextField(Messages.getString("AxesTitlesPanel.2")); //$NON-NLS-1$
		JButton plot = new JButton(Messages.getString("AxesTitlesPanel.3")); //$NON-NLS-1$

		setLayout(new GridLayout(4, 1));
		
		add(title);
		add(axisX);
		add(axisY);
		add(plot);
		
		plot.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				frame.dispose();
				JFrame frame = new JFrame(Messages.getString("AxesTitlesPanel.4")); //$NON-NLS-1$
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.getContentPane().add(new PlotPanel(pomiary, a, b, axisX.getText(), axisY.getText(), title.getText()));
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
}
