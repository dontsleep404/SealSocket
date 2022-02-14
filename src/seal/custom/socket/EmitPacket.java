package seal.custom.socket;

public class EmitPacket {
	private String event;
	private String data;
	
	public EmitPacket(String event, String data) {
		this.event = event;
		this.data = data;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}	
}
