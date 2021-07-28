package de.polarwolf.doorcloser.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParameter;
import de.polarwolf.doorcloser.api.DoorCloserAPI;
import de.polarwolf.doorcloser.exception.DoorCloserException;
import de.polarwolf.doorcloser.main.Main;
import de.polarwolf.doorcloser.materials.Material;

public class DoorCloserCommand extends Command {

	protected final Main main;
	protected final DoorCloserAPI doorCloserAPI;

	
	public DoorCloserCommand(Main main, DoorCloserAPI doorCloserAPI) {
		super(Main.COMMAND_NAME, "DoorCloser administration", Message.USAGE.getMessage(Main.COMMAND_NAME));
		this.main = main;
		this.doorCloserAPI = doorCloserAPI;

		this.setPermission("doorcloser.admin");
        this.commandParameters.clear();
        
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newEnum("action", new CommandEnum("string", listActions()))
        });
        
	}
	
	
	public List<String> listActions() {
		List<String> names = new ArrayList<>();
		for (Action myAction : Action.values()) {
			names.add(myAction.getCommand());
		}
		return names;
	}

	
	protected void cmdHelp(CommandSender sender) {
		String s = String.join(" ", listActions());
		sender.sendMessage(Message.HELP.getMessage() + s);
	}
	
	
	protected void cmdDump(CommandSender sender) {
		Set<String> names = new TreeSet<>();
		for (Material myMaterial : doorCloserAPI.getOpenablesInScope()) {
			names.add(myMaterial.getName());
		}
		String s = String.join(", ", names);
		sender.sendMessage(s);
		
	}
	
	
	protected void cmdInfo(CommandSender sender) {
		sender.sendMessage(Message.INFO.getMessage() + Integer.toString(doorCloserAPI.getTaskCount()));
	}
	
	
	protected void cmdReload(CommandSender sender) throws DoorCloserException {
		doorCloserAPI.reload();
		sender.sendMessage(Message.RELOAD_DONE.getMessage());
	}
	

	protected void cmdDebugEnable() {
		doorCloserAPI.setDebug(true);
	}


	protected void cmdDebugDisable() {
		doorCloserAPI.setDebug(false);
	}


	protected void dispatchCommand(CommandSender sender, Action action) throws DoorCloserException {
		switch (action) {
			case HELP:			cmdHelp(sender);
								break;
			case DEBUGENABLE:	cmdDebugEnable();
								break;
			case DEBUGDISABLE:	cmdDebugDisable();
								break;
			case DUMP:			cmdDump(sender);
								break;
			case INFO:			cmdInfo(sender);
								break;
			case RELOAD:		cmdReload(sender);
								break;
			default: sender.sendMessage(Message.ERROR.getMessage());
		}
	}


	public Action findAction(String actionName) {
		for (Action myAction : Action.values()) {
			if (myAction.getCommand().equalsIgnoreCase(actionName)) {
				return myAction;
			}
		}
		return null;
	}
	

	public boolean handleCommand(CommandSender sender, String[] args) throws DoorCloserException {
        if (!this.testPermission(sender)) {
        	return true;
        }
        
		if (args.length==0) {
			sender.sendMessage(getUsage());
			return true;
		}

		String actionName=args[0].toLowerCase();
		Action action = findAction(actionName);
		if (action == null) {
			sender.sendMessage(Message.UNKNOWN_ACTION.getMessage());
			return true;
		}
		
		if (args.length > 1) {
			sender.sendMessage(Message.TOO_MANY_PARAMETERS.getMessage());
			return true;
		}			

		dispatchCommand (sender, action);
		return true;
	}
	

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		try {
			return handleCommand(sender, args);
		} catch (DoorCloserException e) {
			main.getLogger().warning(Message.ERROR.getMessage()+ " " + e.getMessage());
			sender.sendMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			sender.sendMessage(Message.JAVA_EXCEPTOPN.getMessage());
		}

		return true;
	}

}
