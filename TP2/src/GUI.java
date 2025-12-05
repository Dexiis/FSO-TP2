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

public abstract class GUI implements ILogger {

	protected JFrame frmAd;
	protected JTextField textRadius;
	protected JTextField textAngle;
	protected JTextField textDistance;
	protected JTextField textRobotName;
	protected JCheckBox chckbxOnOff;
	protected JSpinner spinnerNumber;
	protected JTextArea textArea;
	protected JRadioButton rdbtnRandomMovements;

	protected final Thread robotControllerThread;

	protected final Controller robotController;
	protected final Record record = new Record();

	@Override
	public void logMessage(String message) {
		SwingUtilities.invokeLater(() -> textArea.append(message));
	}

	public GUI() {
		this.robotController = new Controller(this, this.record);
		this.robotControllerThread = new Thread(robotController);
		robotControllerThread.start();
		initialize();
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
		
		addedFeatures();
		
	}
	
	protected abstract void addedFeatures();
}