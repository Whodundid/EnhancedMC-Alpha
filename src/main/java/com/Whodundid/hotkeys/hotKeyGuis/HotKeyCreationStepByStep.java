package com.Whodundid.hotkeys.hotKeyGuis;

import com.Whodundid.debug.IDebugCommand;
import com.Whodundid.hotkeys.HotKeyManager;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiTextArea;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiTextField;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.scripts.scriptBase.Script;
import net.minecraft.client.settings.KeyBinding;

//Last edited: Feb 14, 2019
//First Added: Feb 14, 2019
//Author: Hunter Bragg

public class HotKeyCreationStepByStep extends EnhancedGui {
	
	HotKeyManager man = (HotKeyManager) RegisteredSubMods.getMod(SubModType.HOTKEYS);
	HotKey creationKey;
	KeyEntryTextField keyEntry;
	EGuiTextArea selectionList;
	EGuiTextField nameEntry, commandEntry, argEntry;
	EGuiButton cancelButton, nextButton, createButton, backButton;
	String keyName = "";
	boolean enabledVal = true;
	KeyActionType selectedHotKeyType;
	Class selectedGui;
	Script selectedScript;
	IDebugCommand selectedDebug;
	SubModType selectedMod;
	KeyBinding selectedKeyBind;
	int currentScreen = 0;
	
	public HotKeyCreationStepByStep() { super(); }
	public HotKeyCreationStepByStep(EnhancedGui oldGui) { super(oldGui); }
	public HotKeyCreationStepByStep(int posX, int posY) { super(posX, posY); }
	public HotKeyCreationStepByStep(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	
	@Override
	public void initGui() {
		super.initGui();
		loadScreen(currentScreen);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object == nextButton) {
			
		}
	}
	
	private void loadScreen(int screenNumIn) {
		switch (screenNumIn) {
		case 0:
		case 1:
		case 2:
		case 3:
		default: break;
		}
	}
	
	private void loadSelectionList() {
		
	}
	
	private void hideAllObjects() {
		
	}
	
	private void createKey() {
		
	}
}
