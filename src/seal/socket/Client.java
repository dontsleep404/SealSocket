package seal.socket;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import seal.custom.socket.SSocket;
public class Client {
	static SSocket client;
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(500,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTextPane text = new JTextPane();
		frame.add(text, BorderLayout.CENTER);
		JTextField txt = new JTextField();
		txt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					client.emit("msg", txt.getText());
					txt.setText("");
				}
			}
		});
		frame.add(txt, BorderLayout.NORTH);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);		
		
		client = new SSocket("localhost", 25565) {
			@Override
			public void on(String event, String data) {
				text.setText(text.getText() + "\n" + event + " " + data);
			}
		};		
		client.io();
	}
}