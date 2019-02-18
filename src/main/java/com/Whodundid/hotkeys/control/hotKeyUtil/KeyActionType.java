package com.Whodundid.hotkeys.control.hotKeyUtil;

//Sep 30, 2018
//Last edited: Feb 18, 2019
//Edit note: finally added key category activator/deactivator keys.
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public enum KeyActionType {
	COMMAND(true),
	COMMAND_PLAYER_HANDHELD_CONDITION(true),
	SCRIPT(true),
	MOD_ACTIVATOR(true),
	MOD_DEACTIVATOR(true),
	CATEGORY_ACTIVATOR(true),
	CATEGORY_DEACTIVATOR(true),
	GUI_OPENER(true),
	MC_KEYBIND_MODIFIER(false),
	DEBUG(true),
	BUILTINCODE(false),
	UNDEFINED(false);
	
	private boolean canUserCreate;
	
	private KeyActionType(boolean canUserCreateIn) {
		canUserCreate = canUserCreateIn;
	}
	
	public boolean canUserCreate() { return canUserCreate; }
	
	public static String getStringFromType(KeyActionType typeIn) {
		switch (typeIn) {
		case BUILTINCODE: return "Built In Hotkey";
		case COMMAND: return "Command";
		case COMMAND_PLAYER_HANDHELD_CONDITION: return "Command with Item Test";
		case DEBUG: return "Debug Command Runner";
		case GUI_OPENER: return "Gui Opener";
		case MC_KEYBIND_MODIFIER: return "KeyBind Modifier";
		case MOD_ACTIVATOR: return "EnhancedMC Mod Activator";
		case MOD_DEACTIVATOR: return "EnhancedMC Mod Deactivator";
		case CATEGORY_ACTIVATOR: return "Key Category Activator";
		case CATEGORY_DEACTIVATOR: return "Key Category Deactivator";
		case SCRIPT: return "Script Runner";
		default: return "Undefined";
		}
	}
	
	public static KeyActionType getActionTypeFromString(String typeIn) {
		try {
			return valueOf(typeIn);
		} catch (IllegalArgumentException e) {}
		
		switch (typeIn.toLowerCase()) {
		case "code": return BUILTINCODE;
		case "command": return COMMAND;
		case "command item test": return COMMAND_PLAYER_HANDHELD_CONDITION;
		case "debug": return DEBUG;
		case "gui opener": return GUI_OPENER;
		case "keybind modifier": return MC_KEYBIND_MODIFIER;
		case "mod activator": return MOD_ACTIVATOR;
		case "mod deactivator": return MOD_DEACTIVATOR;
		case "category activator": return CATEGORY_ACTIVATOR;
		case "category deactivator": return CATEGORY_DEACTIVATOR;
		case "script": return SCRIPT;
		default: return KeyActionType.UNDEFINED;
		}
	}
}
