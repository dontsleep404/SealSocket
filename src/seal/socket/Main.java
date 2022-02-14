package seal.socket;

import seal.custom.socket.*;

public class Main {
	public static void main(String[] args) throws Exception{
		SServer server = new SServer(25565) {
			public void on(SSocket client, String event, String data) {
				emit(event, data + " " + client.hashCode(), client);
				System.out.println(data + " " + client.hashCode());
			}
		};
		server.listen();
	}
}
