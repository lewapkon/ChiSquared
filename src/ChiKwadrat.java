import java.awt.EventQueue;

import javax.swing.JFrame;

public class ChiKwadrat {
	public static void main(String[] args) {
		
		try
	    {
	        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
	        {
	            if ("Nimbus".equals(info.getName()))
	            {
	                javax.swing.UIManager.setLookAndFeel(info.getClassName());
	                break;
	            }
	        }
	    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex)
	    {
	        java.util.logging.Logger.getLogger(AppFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	    }
		
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				AppFrame frame = new AppFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.setResizable(false);
			}
		});
	}
}