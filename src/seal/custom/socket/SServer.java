package seal.custom.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;

public class SServer {
	private ServerSocket server = null;
	private ArrayList<SSocket> clients;	
	private int port = 25565;
	public SServer(int port) {
		this.port = port;
		this.clients = new ArrayList<>();
	}
	public void listen() {
		try {
			this.server = new ServerSocket(port);			
			initAccept();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	public void on(SSocket client, String event, String data) {
		
	}
	public void initAccept() {
		new Thread(new Runnable() {
			public void run() {
				try {
					while(server != null && !server.isClosed()) {
						Socket client = server.accept();
						SSocket sSocket = new SSocket() {
							public void close(boolean disconnect) {
								SServer.this.on(this, "disconnect", "disconnect");
								clients.remove(this);
								super.close(disconnect);
							}
							public void on(String event, String data) {		
								SServer.this.on(this, event, data);
							}
						};	
						try {
							on(sSocket, "connect", "connect");				
							sSocket.io(client);
						} catch (Exception e) {
							sSocket.close(false);
							clients.remove(sSocket);
							e.printStackTrace();
						}						
						clients.add(sSocket);
					}
				} catch (Exception e) {
					close();
					e.printStackTrace();
				}
			}
		}).start();
	}
	public void emit(String event, Object object, SSocket nosend) {		
		String data = toString(object);
		emit(event, data, nosend);
	}
	public void emit(String event, String data, SSocket nosend) {
		try {
			for(SSocket client : clients) {
				if(client == nosend) continue;
				client.emit(event, data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	public void emit(String event, Object object) {		
		String data = toString(object);
		emit(event, data);
	}
	public String toString(Object object) {
		Gson gson = new Gson();
		String data = gson.toJson(object);
		return data;
	}
	public void emit(String event, String data) {
		try {
			for(SSocket client : clients) {
				client.emit(event, data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	public void close() {
		try {
			server.close();
			server = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
