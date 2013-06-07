import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;


public class AppFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_WIDTH = 300;
	private static final int DEFAULT_HEIGHT = 470;
	private final static JTextField xVal = new JTextField();
	private final static JTextField yVal = new JTextField();
	private final static JTextField yDel = new JTextField();
	public final static JTextField places = new JTextField("3"); //$NON-NLS-1$
	private final static JTextArea result = new JTextArea(Messages.getString("AppFrame.0"));	 //$NON-NLS-1$
	private static Boolean editing = false;
	private Measurement selectedItem;
	
	public AppFrame() {
		
		setTitle(Messages.getString("AppFrame.2")); //$NON-NLS-1$
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(6,2));
		
		panel.add(new JLabel("X: ", SwingConstants.RIGHT)); //$NON-NLS-1$
		panel.add(xVal);
		
		panel.add(new JLabel("Y: ", SwingConstants.RIGHT)); //$NON-NLS-1$
		panel.add(yVal);
		
		panel.add(new JLabel("ΔY: ", SwingConstants.RIGHT)); //$NON-NLS-1$
		panel.add(yDel);
		
		panel.add(new JLabel(Messages.getString("AppFrame.6"), SwingConstants.RIGHT)); //$NON-NLS-1$
		panel.add(places);
		
		final JButton add = new JButton(Messages.getString("AppFrame.7")); //$NON-NLS-1$
		JButton calculate = new JButton(Messages.getString("AppFrame.8")); //$NON-NLS-1$
		final JButton usun = new JButton(Messages.getString("AppFrame.9")); //$NON-NLS-1$
		JButton wykres = new JButton(Messages.getString("AppFrame.10")); //$NON-NLS-1$
		usun.setEnabled(false);
		
		panel.add(add);
		panel.add(calculate);
		
		panel.add(usun);
		panel.add(wykres);
		
		add(panel, BorderLayout.NORTH);
		
		result.setBackground(null);
		result.setForeground(null);
		result.setBorder(null);
		result.setEditable(false);
		add(result, BorderLayout.CENTER);
		
		final PointsList measurements = new PointsList();
		final JList<Measurement> list = new JList<Measurement>(measurements);
		JScrollPane scrollBar = new JScrollPane(list);
		add(scrollBar, BorderLayout.SOUTH);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu(Messages.getString("AppFrame.11")); //$NON-NLS-1$
		JMenuItem importTxt = new JMenuItem(Messages.getString("AppFrame.12")); //$NON-NLS-1$
		JMenuItem exportTxt = new JMenuItem(Messages.getString("AppFrame.13")); //$NON-NLS-1$
		JMenuItem help = new JMenuItem(Messages.getString("AppFrame.14")); //$NON-NLS-1$
		JMenuItem exit = new JMenuItem(Messages.getString("AppFrame.15")); //$NON-NLS-1$
		menu.add(importTxt);
		menu.add(exportTxt);
		menu.add(help);
		menu.add(exit);
		menuBar.add(menu);
		
		setJMenuBar(menuBar);
		
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (!(Double.isNaN(Double.parseDouble((xVal.getText()).replace(",","."))) || Double.isNaN(Double.parseDouble((yVal.getText()).replace(",","."))) || Double.isNaN(Double.parseDouble((yDel.getText()).replace(",","."))))) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
						if (!editing) {
							measurements.addElement(new Measurement(measurements.size() + 1, Double.parseDouble((xVal.getText()).replace(",",".")), Double.parseDouble((yVal.getText()).replace(",",".")), Double.parseDouble((yDel.getText()).replace(",",".")))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
							usun.setEnabled(true);
						} else {
							int place = selectedItem.getNumber() - 1;
							measurements.remove(place);
							measurements.add(place, new Measurement(place + 1, Double.parseDouble((xVal.getText()).replace(",",".")), Double.parseDouble((yVal.getText()).replace(",",".")), Double.parseDouble((yDel.getText()).replace(",",".")))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
							editing = false;
							add.setText(Messages.getString("AppFrame.34")); //$NON-NLS-1$
						}

						xVal.setText(""); //$NON-NLS-1$
						yVal.setText(""); //$NON-NLS-1$
						yDel.setText(""); //$NON-NLS-1$
					}
				}
				catch (NumberFormatException err) {
					JOptionPane.showMessageDialog(null, Messages.getString("AppFrame.38")); //$NON-NLS-1$
				}
			}
		});
		
		list.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 2) {
					
					editing = true;
					selectedItem = (Measurement) list.getSelectedValue();
					xVal.setText(Double.toString(selectedItem.getXVal()));
					yVal.setText(Double.toString(selectedItem.getYVal()));
					yDel.setText(Double.toString(selectedItem.getYDel()));
					
					add.setText(Messages.getString("AppFrame.39")); //$NON-NLS-1$
				}
			}
		});
		
		calculate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (measurements.size() >= 3) {
					Measurement[] array = measurements.toArray();
					Result result = new Result(array);
					AppFrame.result.setText(Messages.getString("AppFrame.40") + result); //$NON-NLS-1$
				}
				else {
					JOptionPane.showMessageDialog(null, Messages.getString("AppFrame.41")); //$NON-NLS-1$
				}
			}
		});

		usun.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (list.getSelectedIndex() != -1) {
					
					if (editing) {
						editing = false;
						add.setText(Messages.getString("AppFrame.42")); //$NON-NLS-1$
					}
					
					int[] selected = list.getSelectedIndices();
					for(int i = 0; i < selected.length; i++) {
						
						measurements.remove(selected[i]-i);
						for (int j = 0; j < measurements.size(); j++) {

							measurements.get(j).setNumber(j + 1);
						}
						
					}
					xVal.setText(""); //$NON-NLS-1$
					yVal.setText(""); //$NON-NLS-1$
					yDel.setText(""); //$NON-NLS-1$
					
					if (measurements.isEmpty()) {
						usun.setEnabled(false);
					}
				}
				else {
					JOptionPane.showMessageDialog(null, Messages.getString("AppFrame.46")); //$NON-NLS-1$
				}

			}
		});

		wykres.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (measurements.size() >= 3) {
					Measurement[] array = measurements.toArray();
					Result wynik = new Result(array);
					AppFrame.result.setText(Messages.getString("AppFrame.47") + wynik); //$NON-NLS-1$
					JFrame frame = new JFrame(Messages.getString("AppFrame.48")); //$NON-NLS-1$
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					frame.setLocation(screenSize.width / 2, screenSize.height/2);
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.getContentPane().add(new AxesTitlesPanel(frame, measurements, wynik.getA(), wynik.getB()));
					frame.pack();
					frame.setVisible(true);
				}
				else {
					JOptionPane.showMessageDialog(null, Messages.getString("AppFrame.49")); //$NON-NLS-1$
				}
			}
		});
		
		help.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFrame frame = new JFrame(Messages.getString("AppFrame.50")); //$NON-NLS-1$
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				frame.setLocation(screenSize.width / 2, screenSize.height/2);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setSize(500, 430);
				frame.setResizable(false);
				frame.getContentPane().add(new JLabel(Messages.getString("AppFrame.51") + //$NON-NLS-1$
						Messages.getString("AppFrame.52") + //$NON-NLS-1$
						Messages.getString("AppFrame.53") + //$NON-NLS-1$
						Messages.getString("AppFrame.54") + //$NON-NLS-1$
						Messages.getString("AppFrame.55") + //$NON-NLS-1$
						Messages.getString("AppFrame.56") + //$NON-NLS-1$
						Messages.getString("AppFrame.57") + //$NON-NLS-1$
						Messages.getString("AppFrame.58") + //$NON-NLS-1$
						Messages.getString("AppFrame.59") + //$NON-NLS-1$
						Messages.getString("AppFrame.60") + //$NON-NLS-1$
						Messages.getString("AppFrame.61") + //$NON-NLS-1$
						Messages.getString("AppFrame.62") + //$NON-NLS-1$
						Messages.getString("AppFrame.63") + //$NON-NLS-1$
						Messages.getString("AppFrame.64") + //$NON-NLS-1$
						Messages.getString("AppFrame.65"))); //$NON-NLS-1$
				frame.setVisible(true);
			}
		});
		
		importTxt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT", "txt"); //$NON-NLS-1$ //$NON-NLS-2$
				fileChooser.setFileFilter(filter);
				int result = fileChooser.showOpenDialog(AppFrame.this);
				if (result == JFileChooser.APPROVE_OPTION) {

					File file = fileChooser.getSelectedFile();
					try (BufferedReader in = new BufferedReader(new FileReader(file))) {

						String line = null;
						String[] array = null;
						while ((line = in.readLine()) != null) {
							
							array = line.split("	"); //$NON-NLS-1$
							measurements.addElement(new Measurement(measurements.size() + 1, Double.parseDouble(array[0].replace(",",".")), Double.parseDouble(array[1].replace(",",".")), Double.parseDouble(array[2].replace(",",".")))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
							usun.setEnabled(true);
						}
						in.close();
					} catch (IOException e1) {

						e1.printStackTrace();
					}
				}
			}
		});
		
		exportTxt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT", "txt"); //$NON-NLS-1$ //$NON-NLS-2$
				fileChooser.setFileFilter(filter);
				int result = fileChooser.showDialog(AppFrame.this, "Create file"); //$NON-NLS-1$
				if (result == JFileChooser.APPROVE_OPTION) {
					
					try {
						File file = fileChooser.getSelectedFile();
						FileWriter fileWriter = new FileWriter(file);
						PrintWriter printWriter = new PrintWriter(fileWriter);
						for (int i = 0; i < measurements.size(); i++) {
							
							printWriter.println(measurements.get(i).toString2());
						}
						printWriter.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
	}
}
