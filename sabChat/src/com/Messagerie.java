package edu.UTEP.android;

import android.widget.EditText;

public class Messagerie {

	private Long id;
	
	private String msgrecu;
	
	private String msgenvoyer;

	public Messagerie(){}
	
	public Messagerie(EditText msgrecu, String msgenvoyer) {
		super();
		this.msgrecu = this.msgrecu;
		this.msgenvoyer = this.msgenvoyer;
	}

	@Override
	public String toString() {
		return "Messagerie [id=" + id + ", msgrecu=" + msgrecu + ", msgenvoyer=" + msgenvoyer
				+ "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMsgrecu() {
		return msgrecu;
	}

	public void setMsgrecu(String msgrecu) {
		this.msgrecu = msgrecu;
	}

	public String getMsgenvoyer() {
		return msgenvoyer;
	}

	public void setMsgenvoyer(String msgenvoyer) {
		this.msgenvoyer = msgenvoyer;
	}
	
	
	
}
