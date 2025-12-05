import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RandomMovementsGUI extends GUI {
	
	public RandomMovementsGUI() {
		super();
		robotController.updateActionNumber(Integer.parseInt(spinnerNumber.getValue().toString()));
	}
	
	@Override
	protected void addedFeatures() {
		JLabel lblNumber = new JLabel("Number");
		lblNumber.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNumber.setBounds(390, 250, 71, 29);
		frmAd.getContentPane().add(lblNumber);
		
		spinnerNumber = new JSpinner();
		spinnerNumber.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				robotController.updateActionNumber(Integer.parseInt(spinnerNumber.getValue().toString()));
			}
		});
		spinnerNumber.setModel(new SpinnerNumberModel(Integer.valueOf(5), null, null, Integer.valueOf(1)));

		spinnerNumber.setFont(new Font("Arial", Font.PLAIN, 18));
		spinnerNumber.setBounds(460, 250, 42, 29);
		frmAd.getContentPane().add(spinnerNumber);
		
		rdbtnRandomMovements = new JRadioButton("Random Movements");
		rdbtnRandomMovements.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				robotController.updateActionNumber(Integer.parseInt(spinnerNumber.getValue().toString()));
				if (rdbtnRandomMovements.isSelected()) {
					robotController.startRandomMovements();
				} else {
					robotController.stopRandomMovements();
				}
			}
		});
		rdbtnRandomMovements.setFont(new Font("Arial", Font.PLAIN, 18));
		rdbtnRandomMovements.setBounds(505, 255, 189, 20);
		frmAd.getContentPane().add(rdbtnRandomMovements);
	}
	
}
