import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

public class RecorderGUI extends GUI {
	
	public RecorderGUI() {
	}

	@Override
	protected void addedFeatures() {
		JLabel lblFile = new JLabel("File");
		lblFile.setFont(new Font("Arial", Font.PLAIN, 18));
		lblFile.setBounds(10, 320, 33, 29);
		frmAd.getContentPane().add(lblFile);

		JButton btnRecord = new JButton("Start Recording");
		btnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(record.isRecording()) {
					btnRecord.setText("Start Recording");
					record.stopRecording();
				} else {
					btnRecord.setText("Stop Recording");
					record.startRecording();
				}
			}
		});
		btnRecord.setFont(new Font("Arial", Font.PLAIN, 16));
		btnRecord.setBounds(150, 420, 200, 35);
		frmAd.getContentPane().add(btnRecord);

		JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Chama a função do robotController que coloca o array de movimentos
				// guardado no data para o buffer.
			}
		});

		// TODO adicionar barra de introdução de ficheiros

		btnPlay.setFont(new Font("Arial", Font.PLAIN, 16));
		btnPlay.setBounds(350, 420, 100, 35);
		frmAd.getContentPane().add(btnPlay);
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFont(new Font("Arial", Font.PLAIN, 16));
		fileChooser.setBounds(150, 320, 200, 200);
		fileChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser.showSaveDialog(null);
			}
		});
		frmAd.getContentPane().add(fileChooser);
		
	}
	
}
