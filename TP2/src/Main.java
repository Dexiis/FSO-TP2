import java.awt.EventQueue;

public class Main {
	
	/*
	 * TODO: 
	 * 	Stop Avoid Obstacles
	 * 	Polimorfismo comportamentos (maquinas de estado)
	 * 	Movimentos dinamicos
	 * 	Tempo nos movimentos
	 * 	Int to byte[]
	 * 	Buffer prioritario
	 * */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI recorderWindow = new RecorderGUI();
					GUI rmWindow = new RandomMovementsGUI();
					recorderWindow.frmAd.setVisible(true);
					rmWindow.frmAd.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
