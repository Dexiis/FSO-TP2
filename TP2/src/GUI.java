
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JSpinner;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class GUI implements ILogger {

	private JFrame frmAd;
	private JTextField textRadius;
	private JTextField textAngle;
	private JTextField textDistance;
	private JTextField textRobotName;
	private JCheckBox chckbxOnOff;
	private JSpinner spinnerNumber;
	private JRadioButton rdbtnRandomMovements;
	private JTextArea textArea;

	private final Thread robotControllerThread;

	private final Controller robotController;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmAd.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void logMessage(String message) {
		SwingUtilities.invokeLater(() -> textArea.append(message));
	}

	public GUI() {
		this.robotController = new Controller(this);
		this.robotControllerThread = new Thread(robotController);
		robotControllerThread.start();
		initialize();
		robotController.updateActionNumber(Integer.parseInt(spinnerNumber.getValue().toString()));
		robotController.updateDistance(Integer.parseInt(textDistance.getText()));
		robotController.updateRadius(Integer.parseInt(textRadius.getText()));
		robotController.updateAngle(Integer.parseInt(textAngle.getText()));
	}

	private void initialize() {
		frmAd = new JFrame();
		frmAd.setTitle("GUI Trabalho Prático 2");
		frmAd.setBounds(100, 100, 700, 800);
		frmAd.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmAd.getContentPane().setLayout(null);
		frmAd.setResizable(false);

		frmAd.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				robotController.turnOffRobot();

				frmAd.dispose();
				System.exit(0);
			}
		});

		JLabel lblRadius = new JLabel("Radius");
		lblRadius.setFont(new Font("Arial", Font.PLAIN, 18));
		lblRadius.setBounds(10, 10, 59, 29);
		frmAd.getContentPane().add(lblRadius);

		JLabel lblAngle = new JLabel("Angle");
		lblAngle.setFont(new Font("Arial", Font.PLAIN, 18));
		lblAngle.setBounds(150, 10, 48, 29);
		frmAd.getContentPane().add(lblAngle);

		JLabel lblDistance = new JLabel("Distance");
		lblDistance.setFont(new Font("Arial", Font.PLAIN, 18));
		lblDistance.setBounds(300, 10, 71, 29);
		frmAd.getContentPane().add(lblDistance);

		JLabel lblRobot = new JLabel("Robot");
		lblRobot.setFont(new Font("Arial", Font.PLAIN, 18));
		lblRobot.setBounds(540, 10, 59, 29);
		frmAd.getContentPane().add(lblRobot);

		JLabel lblNumber = new JLabel("Number");
		lblNumber.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNumber.setBounds(390, 250, 71, 29);
		frmAd.getContentPane().add(lblNumber);

		JLabel lblLogger = new JLabel("Logger");
		lblLogger.setFont(new Font("Arial", Font.PLAIN, 18));
		lblLogger.setBounds(10, 480, 77, 29);
		frmAd.getContentPane().add(lblLogger);

		textRadius = new JTextField();

		textRadius.setHorizontalAlignment(SwingConstants.CENTER);
		textRadius.setFont(new Font("Arial", Font.PLAIN, 18));
		textRadius.setText("30");
		textRadius.setBounds(70, 10, 50, 30);
		frmAd.getContentPane().add(textRadius);
		textRadius.setColumns(10);

		textAngle = new JTextField();

		textAngle.setHorizontalAlignment(SwingConstants.CENTER);
		textAngle.setFont(new Font("Arial", Font.PLAIN, 18));
		textAngle.setText("90");
		textAngle.setBounds(200, 11, 50, 30);
		frmAd.getContentPane().add(textAngle);
		textAngle.setColumns(10);

		textDistance = new JTextField();

		textDistance.setText("50");
		textDistance.setHorizontalAlignment(SwingConstants.CENTER);
		textDistance.setFont(new Font("Arial", Font.PLAIN, 18));
		textDistance.setColumns(10);
		textDistance.setBounds(375, 11, 50, 30);
		frmAd.getContentPane().add(textDistance);

		textRobotName = new JTextField();

		textRobotName.setText("EVA");
		textRobotName.setHorizontalAlignment(SwingConstants.CENTER);
		textRobotName.setFont(new Font("Arial", Font.PLAIN, 18));
		textRobotName.setColumns(10);
		textRobotName.setBounds(600, 11, 100, 30);
		frmAd.getContentPane().add(textRobotName);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 510, 680, 250);
		frmAd.getContentPane().add(scrollPane);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

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

		chckbxOnOff = new JCheckBox("Turn On");
		chckbxOnOff.setFont(new Font("Arial", Font.PLAIN, 18));
		chckbxOnOff.setBounds(600, 50, 95, 28);
		frmAd.getContentPane().add(chckbxOnOff);
		chckbxOnOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxOnOff.isSelected())
					robotController.turnOnRobot(textRobotName.getText());
				else
					robotController.turnOffRobot();
			}
		});

		JButton btnFoward = new JButton("^");
		btnFoward.setForeground(new Color(0, 0, 0));
		btnFoward.setBackground(new Color(128, 255, 128));
		btnFoward.setFont(new Font("Arial", Font.BOLD, 22));
		btnFoward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				robotController.updateDistance(Integer.parseInt(textDistance.getText()));

				robotController.bufferMoveForward();
			}
		});
		btnFoward.setBounds(275, 100, 150, 40);
		frmAd.getContentPane().add(btnFoward);

		JButton btnStop = new JButton("STOP");
		btnStop.setBackground(new Color(217, 0, 5));
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				robotController.clearBuffer();
				robotController.stopMovement();
				robotController.stopRandomMovements();
				rdbtnRandomMovements.setSelected(false);
			}
		});
		btnStop.setFont(new Font("Arial", Font.BOLD, 18));
		btnStop.setBounds(275, 140, 150, 40);
		frmAd.getContentPane().add(btnStop);

		JButton btnLeft = new JButton("<");
		btnLeft.setBackground(new Color(255, 255, 0));
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				robotController.updateRadius(Integer.parseInt(textRadius.getText()));
				robotController.updateAngle(Integer.parseInt(textAngle.getText()));

				robotController.bufferMoveLeftCurve();
			}
		});
		btnLeft.setFont(new Font("Arial", Font.BOLD, 18));
		btnLeft.setBounds(125, 140, 150, 40);
		frmAd.getContentPane().add(btnLeft);

		JButton btnRight = new JButton(">");
		btnRight.setBackground(new Color(0, 128, 192));
		btnRight.setFont(new Font("Arial", Font.BOLD, 18));
		btnRight.setBounds(425, 140, 150, 40);
		frmAd.getContentPane().add(btnRight);
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				robotController.updateRadius(Integer.parseInt(textRadius.getText()));
				robotController.updateAngle(Integer.parseInt(textAngle.getText()));

				robotController.bufferMoveRightCurve();
			}
		});

		JButton btnBackwards = new JButton("v");
		btnBackwards.setBackground(new Color(192, 192, 192));
		btnBackwards.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				robotController.updateDistance(Integer.parseInt(textDistance.getText()));

				robotController.bufferMoveBackwards();
			}
		});
		btnBackwards.setFont(new Font("Arial", Font.BOLD, 18));
		btnBackwards.setBounds(275, 180, 150, 40);
		frmAd.getContentPane().add(btnBackwards);

		JButton btnSquare = new JButton("Square");
		btnSquare.setFont(new Font("Arial", Font.PLAIN, 16));
		btnSquare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textDistance.setText("20");
				robotController.updateDistance(20);
				textAngle.setText("90");
				robotController.updateAngle(90);
				textRadius.setText("0");
				robotController.updateRadius(0);

				robotController.squareMovement();
			}
		});
		btnSquare.setBounds(10, 250, 100, 35);
		frmAd.getContentPane().add(btnSquare);

		JLabel lblFile = new JLabel("File");
		lblFile.setFont(new Font("Arial", Font.PLAIN, 18));
		lblFile.setBounds(10, 320, 33, 29);
		frmAd.getContentPane().add(lblFile);

		JButton btnRecord = new JButton("Record");
		btnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Clica uma vez para ativar (cor do botão vermelho). Clicar denovo para
				// desligar (cor neutra atual).
				// caso seja apenas para os movimentos da GUI então chamar a função na GUI que
				// armazena todos os movimentos clicacos num array. Quando o botão é clicado
				// para parar de dar record, então envia o array para o data.

				// Quando ligado desativa o botão de play e a barra para introduzir ficheiros
			}
		});
		btnRecord.setFont(new Font("Arial", Font.PLAIN, 16));
		btnRecord.setBounds(250, 420, 100, 35);
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
	}
}