package com.Whodundid.hotkeys.subModSpecific;

import org.lwjgl.input.Keyboard;
import com.Whodundid.debug.DebugFunctions;
import com.Whodundid.debug.IDebugCommand;
import com.Whodundid.enhancedChat.gameChat.ModdedChat;
import com.Whodundid.hotkeys.HotKeyManager;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import com.Whodundid.hotkeys.control.hotKeyUtil.SubModHotKeys;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.miscUtil.ChatBuilder;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.EnumChatFormatting;

//Oct 16, 2018
//Jan 10, 2019
//Last edited: Feb 17, 2019
//Edit note: removed 'openInGameMenu' key as it's action was being overshadowed by the EventListener.
//First Added: Nov 16, 2017
//Author: Hunter Bragg

public class Global_HotKeys extends SubModHotKeys {
	
	static int speed = 1;
	
	public Global_HotKeys(HotKeyManager manIn) {
		man = manIn;
		subModName = "global";
	}
	
	@Override public void addKeys() {
		keys.add(new HotKey("openChat", new KeyComboAction(mc.gameSettings.keyBindChat.getKeyCode()), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Opens the chat gui or an EnhancedChat window if EnhancedChat is enabled."); }
			@Override public void executeHotKeyAction() {
				if (RegisteredSubMods.getMod(SubModType.ENHANCEDCHAT).isEnabled()) { mc.displayGuiScreen(new ModdedChat()); }
				else { mc.displayGuiScreen(new GuiChat()); }
			}
		});
		
		keys.add(new HotKey("openCommandChat", new KeyComboAction(Keyboard.KEY_SLASH), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Opens the chat gui and adds a '/' at the beginning"); }
			@Override public void executeHotKeyAction() {
				if (RegisteredSubMods.getMod(SubModType.ENHANCEDCHAT).isEnabled()) { mc.displayGuiScreen(new ModdedChat("/")); }
				else { mc.displayGuiScreen(new GuiChat("/")); }
			}
		});
		
		keys.add(new HotKey("baseDebug", new KeyComboAction(Keyboard.KEY_GRAVE), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Runs debug function: 0."); }
			@Override public void executeHotKeyAction() {
				DebugFunctions.runDebugFunction(IDebugCommand.DEBUG_0);
			}
		});
	}
}