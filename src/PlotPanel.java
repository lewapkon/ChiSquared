import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


@SuppressWarnings("serial")
public class PlotPanel extends JPanel implements KeyListener {
	
	private static final int PREF_W = 800;
	private static final int PREF_H = 650;
	
	private Measurement[] measurements;
	private PointsList list;
	private final int PAD1 = 60;
	private final int PAD2 = 40;
	private double xMin;
	private double xMax;
	private double yMin;
	private double yMax;
	private double a;
	private double b;
	private String axisX;
	private String axisY;
	private String title;
	private double splitX = 8;
	private double splitY = 7;
	
	public PlotPanel(PointsList lista, double a, double b, String axisX, String axisY, String title) {
		measurements = lista.array;
		this.list = lista;
		this.a = a;
		this.b = b;
		this.title = title;
		this.axisX = axisX;
		this.axisY = axisY;
		setFocusable(true);
		setBackground(Color.white);
		addKeyListener(this);
	}
	
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Font font = g2.getFont();
		FontRenderContext frc = g2.getFontRenderContext();
		LineMetrics lm = font.getLineMetrics("0", frc);
		double width; 	// String's width.
		double height; 	// String's height.
		int w = getWidth();
		int h = getHeight();
		
		xMin = list.minXVal();
		xMax = list.maxXVal();
		yMin = list.minYDel();
		yMax = list.maxYDel();
		
		// This is used to make kind of a padding inside the plot.
		double magnify = 0.05;
		xMin = xMin - Math.abs(xMin) * magnify;
		xMax = xMax + Math.abs(xMax) * magnify;
		yMin = yMin - Math.abs(yMin) * magnify;
		yMax = yMax + Math.abs(yMax) * magnify;
		
		double xScale = (w - PAD1 - PAD2) / (xMax - xMin);
		double yScale = (h - PAD1 - PAD2) / (yMax - yMin);
		
		
		Point2D.Double origin = new Point2D.Double(PAD1, h - PAD2); // Axes origin.
		
		// Draw abscissa.
		g2.draw(new Line2D.Double(PAD1, origin.y, w - PAD2, origin.y));
		// Mirror.
		g2.draw(new Line2D.Double(PAD1, PAD1, w - PAD2, PAD1));
		
		// Draw ordinate.
		g2.draw(new Line2D.Double(origin.x, PAD1, origin.x, h - PAD2));
		// Mirror.
		g2.draw(new Line2D.Double(w - PAD2, PAD1, w - PAD2, h - PAD2));
		
		// Mark origin.
		//g2.setPaint(Color.red);
		//g2.fill(new Ellipse2D.Double(origin.x-2, origin.y-2, 4, 4));
		
		// Draw units on abscissa.

		Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10000, new float[]{2}, 1000);
		Stroke normal = g2.getStroke();
		double deltaX = (xMax - xMin) / splitX ;
		double shift;
		String number;
		for (int i = 0; i < splitX + 1; i++) {
			g2.setStroke(normal);
			g2.setPaint(Color.black);
			number = String.format("%.2f", xMin + deltaX * i);
			width = font.getStringBounds(number, frc).getWidth();
			height = font.getStringBounds(number, frc).getHeight();
			shift = PAD1 + i * deltaX * xScale;
			g2.draw(new Line2D.Double(shift, origin.y, shift, origin.y - 5));
			g2.drawString(number, (int)shift - (int)(width / 2), (int)origin.y + (int)lm.getAscent() + 3);
			
			// Draw dashed line.
			g2.setStroke(dashed);
			g2.setPaint(Color.gray);
			if (i != 0 && i != splitX)
				g2.draw(new Line2D.Double(shift, origin.y - 5, shift, PAD1));
		}
		
		// Draw units on ordinate.
		double deltaY = (yMax - yMin) / splitY;
		for (int i = 0; i < splitY + 1; i++) {
			g2.setStroke(normal);
			g2.setPaint(Color.black);
			number = String.format("%.2f", yMax - deltaY * i);
			width = font.getStringBounds(number, frc).getWidth();
			height = font.getStringBounds(number, frc).getHeight();
			shift = PAD1 + i * deltaY * yScale;
			g2.draw(new Line2D.Double(origin.x, shift, origin.x + 5, shift));
			g2.drawString(number, (int)origin.x - (int)width - 1, (int)shift);
			
			// Draw dashed line.
			g2.setStroke(dashed);
			g2.setPaint(Color.gray);
			if (i != 0 && i != splitY)
				g2.draw(new Line2D.Double(origin.x + 5, shift, w - PAD2, shift));
		}
		g2.setStroke(normal);
		
		// Draw linear regression line.
		g2.setPaint(Color.green);
		g2.draw(new Line2D.Double(	PAD1, 		h - PAD2 - ((xMin * a + b - yMin) * yScale),
									w - PAD2, 	h - PAD2 - ((xMax * a + b - yMin) * yScale)));
		
		// Plot data.
		for (int i = 0; i < measurements.length; i++) {
			
			// Calculations for point.
			double x = PAD1 + xScale * (measurements[i].getXVal() - xMin);
			double y = h - PAD2 - yScale * (measurements[i].getYVal() - yMin);
			
			// Calculations for y error bar.
			double y1 = y - yScale * measurements[i].getYDel();
			double y2 = y + yScale * measurements[i].getYDel();
			
			// Draw y error bar.
			g2.setPaint(Color.red);
			g2.draw(new Line2D.Double(x, y1, x, y2));
			g2.draw(new Line2D.Double(x - 3, y1, x + 3, y1));
			g2.draw(new Line2D.Double(x - 3, y2, x + 3, y2));
			
			// Draw a point.
			g2.setPaint(Color.blue);
			g2.fill(new Ellipse2D.Double(x - 2, y - 2, 4, 4));
			g2.drawString(Integer.toString(i + 1), (float)x - 7, (float)y - 3);
		}
		
		Font biggerFont = new Font(null, Font.BOLD, 20);
		g2.setFont(biggerFont);
		g2.setPaint(Color.black);
		// Draw plot's title.
		width = biggerFont.getStringBounds(title, frc).getWidth();
		height = biggerFont.getStringBounds(title, frc).getHeight();
		g2.drawString(title, (w / 2) - (int)(width / 2), (int)height + lm.getAscent());
		
		// Draw X axis title.
		g2.setFont(font);
		width = font.getStringBounds(axisX, frc).getWidth();
		height = font.getStringBounds(axisX, frc).getHeight();
		g2.drawString(axisX, (w / 2) - (int)(width / 2), h - (int)(height / 2));
		
		// Draw Y axis title.
		AffineTransform fontAT = new AffineTransform();
		fontAT.rotate(1.5*Math.PI);
		Font derivedFont = font.deriveFont(fontAT);
		g2.setFont(derivedFont);
		width = derivedFont.getStringBounds(axisY, frc).getWidth();
		height = derivedFont.getStringBounds(axisY, frc).getHeight();
		g2.drawString(axisY, lm.getAscent() + 1, (h / 2) + (int)(width / 2));
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PREF_W, PREF_H);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent evt) {
		Character c = evt.getKeyChar();
		Character s = 's';
		if (c.equals(s)) {
			// Copy screenshot to clipboard.
			BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			paint(img.getGraphics());

			JFileChooser filechooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"PNG Images", "png");
			filechooser.setFileFilter(filter);
			int result = filechooser.showSaveDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File saveFile = filechooser.getSelectedFile();
				try {
					ImageIO.write(img, "png", saveFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
