package seal.custom.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import com.google.gson.Gson;


public class SSocket {
	private String host;
	private int port;
	private Socket socket = null;
	private DataOutputStream out = null;
	private DataInputStream in = null;
	public SSocket(String host, int port) {
		this.host = host;
		this.port = port;
	}
	public SSocket() {
		this.host = "";
		this.port = 0;
	}
	public void io(Socket socket) {		
		if(this.socket != null) {
			return;
		}
		try {
			this.socket = socket;
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			initReceive();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void io() {
		if(this.socket != null) {
			return;
		}
		try {
			this.socket = new Socket(host, port);
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			initReceive();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void initReceive() {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				try{
					while(socket != null && in != null) {
						String data = in.readUTF();
						Gson gson = new Gson();
						EmitPacket packet = gson.fromJson(data, EmitPacket.class);
						on(packet.getEvent(), packet.getData());
					}
				}catch(Exception e) {
					close(false);
					//e.printStackTrace();
				}
			}
		}).start();
	}
	public void emit(String event, String data) {
		EmitPacket packet = new EmitPacket(event, data);
		try {
			if(this.out != null) {
				this.out.writeUTF(toString(packet));
				this.out.flush();
			}
		} catch (Exception e) {
			close(false);
			e.printStackTrace();
		}		
	}
	public void close(boolean disconnect) {	
		if(disconnect) {
			try {
				emit("disconnect", null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		try {
			socket.close();
			in = null;
			out = null;
			socket = null;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		System.out.println("Client disconnect!");
	}
	public void emit(String event, Object object) {		
		emit(event, toString(object));
	}
	public String toString(Object object) {
		Gson gson = new Gson();
		String data = gson.toJson(object);
		return data;
	}
	public void on(String event, String data) {
		
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
