package de.polarwolf.doorcloser.commands;

public enum Action {

	HELP ("help"),
	DEBUGENABLE ("debugenable"),
	DEBUGDISABLE ("debugdisable"),
	DUMP ("dump"),
	INFO ("info"),
	RELOAD ("reload");

	private final String command;


	private Action(String command) {
		this.command = command;
	}


	public String getCommand() {
		return command;
	}

}
