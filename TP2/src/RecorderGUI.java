import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class RecorderGUI extends GUI {
	
	File selectedFile;
	
	private final PlayMovements player = new PlayMovements(this);
	
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
				if(selectedFile == null) 
					textArea.append("No file chosen.\n");
				else {
					player.loadFile(selectedFile);
					player.playMovements();
				}
			}
		});

		// TODO adicionar barra de introdução de ficheiros

		btnPlay.setFont(new Font("Arial", Font.PLAIN, 16));
		btnPlay.setBounds(350, 420, 200, 35);
		frmAd.getContentPane().add(btnPlay);
		
		JTextArea filePath = new JTextArea();
		filePath.setBounds(150, 370, 340, 35);
		frmAd.getContentPane().add(filePath);
		
		JButton btnSelectFile = new JButton("...");
		btnSelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int response = fileChooser.showOpenDialog(null);
				
				if(response == JFileChooser.APPROVE_OPTION) {
					selectedFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
					filePath.setText(selectedFile.toString());
				}
			}
		});
		btnSelectFile.setFont(new Font("Arial", Font.PLAIN, 16));
		btnSelectFile.setBounds(500, 370, 50, 35);
		frmAd.getContentPane().add(btnSelectFile);
	}
	
}
