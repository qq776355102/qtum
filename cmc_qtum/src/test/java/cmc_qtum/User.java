package cmc_qtum;

public class User {

	private int id;
	
	public String uname;
	
	public String address;



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}



	public User(int id, String uname, String address) {
		super();
		this.id = id;
		this.uname = uname;
		this.address = address;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
