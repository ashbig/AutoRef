package flex.process;

public class Protocol {
	private int id;
	private String processcode;
	private String processname;

	public Protocol (int id, String processcode, String processname) {
		this.id = id;
		this.processcode = processcode;
		this.processname = processname;
	}

	public int getId() {
		return id;
	}

	public String getProcesscode() {
		return processcode;
	}
}