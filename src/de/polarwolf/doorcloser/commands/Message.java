package de.polarwolf.doorcloser.commands;

public enum Message {

	OK ("OK"),
	ERROR ("ERROR"),
	JAVA_EXCEPTOPN ("Java Exception Error"),
	UNKNOWN_ACTION ("Unknown action"),
	TOO_MANY_PARAMETERS ("Too many parameters"),
	HELP ("Valid actions are: "),
	INFO ("Currently running tasks: "),
	RELOAD_DONE ("Settings reloaded from configuration file"),
	READY ("The butler is now instructed to close the doors behind you"),
	FINISH ("The butler is off work now"),
	LOAD_ERROR ("Error loading configuration"),
	USAGE ("Enter \"/$ help\" for a list of available actions.");
	
	private final String messageText;
	

	private Message(String messageText) {
		this.messageText = messageText;
	}

	
	public String getMessage() {
		return messageText;
	}
	
	
	public String getMessage(String replacementText) {
		return getMessage().replace("$", replacementText);
	}

}
