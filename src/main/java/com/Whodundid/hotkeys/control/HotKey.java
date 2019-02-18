package com.Whodundid.hotkeys.control;

import com.Whodundid.hotkeys.HotKeyManager;
import com.Whodundid.hotkeys.control.hotKeyUtil.HotkeyCategory;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import net.minecraft.client.Minecraft;

//Last edited: 9-30-18
//First Added: 9-14-18
//Author: Hunter Bragg

public abstract class HotKey {
	
	protected Minecraft mc = MainMod.getMC();
	protected HotKeyManager man = (HotKeyManager) RegisteredSubMods.getMod(SubModType.HOTKEYS);
	protected String keyName = "";
	protected String description = "No description set.";
	protected KeyComboAction keyCombo;
	protected HotkeyCategory category;
	protected boolean isEnabled = true;
	protected boolean builtIn = false;
	protected String builtInSubModType = "";
	protected KeyActionType hotKeyType;
	
	protected HotKey() {}
	public HotKey(String keyNameIn, KeyComboAction keyCodeIn, KeyActionType typeIn) { this(keyNameIn, keyCodeIn, true, typeIn, null); }
	public HotKey(String keyNameIn, KeyComboAction keyCodeIn, KeyActionType typeIn, String builtInSubModTypeIn) { this(keyNameIn, keyCodeIn, true, typeIn, builtInSubModTypeIn); }
	public HotKey(String keyNameIn, KeyComboAction keyCodeIn, boolean builtInVal, KeyActionType typeIn) { this(keyNameIn, keyCodeIn, builtInVal, typeIn, null); }
	public HotKey(String keyNameIn, KeyComboAction keyCodeIn, boolean builtInVal, KeyActionType typeIn, String builtInSubModTypeIn) { this(keyNameIn, keyCodeIn, builtInVal, typeIn, null, builtInSubModTypeIn); }
	public HotKey(String keyNameIn, KeyComboAction keyCodeIn, boolean builtInVal, KeyActionType typeIn, String categoryNameIn, String builtInSubModTypeIn) {
		keyName = keyNameIn;
		keyCombo = keyCodeIn;
		builtIn = builtInVal;
		hotKeyType = typeIn;
		builtInSubModType = builtInSubModTypeIn;
		if (categoryNameIn != null) { category = new HotkeyCategory(categoryNameIn); }
	}
	
	//getters
	public String getKeyName() { return keyName; }
	public String getKeyDescription() { return description; }
	public KeyComboAction getKeyCombo() { return keyCombo; }
	public KeyActionType getHotKeyType() { return hotKeyType; }
	public HotkeyCategory getKeyCategory() { return category; }
	public String getBuiltInSubModType() { return isBuiltIn() ? builtInSubModType : "not submod key"; }
	
	//setters
	public void setEnabled(boolean enable) { isEnabled = enable; }
	public void setKeyCategory(String categoryNameIn) { category = new HotkeyCategory(categoryNameIn); }
	public void setKeyDescription(String descriptionIn) { description = descriptionIn; }
	
	//general
	public boolean isEnabled() { return isEnabled; }
	public boolean isBuiltIn() { return builtIn; }
	
	public String getHotKeyStatistics() {
		String returnStats = keyName + "; ";
		returnStats += hotKeyType.toString() + "; ";
		for (int i : keyCombo.hotKeyCodes) { returnStats += (i + " "); }
		returnStats += "; ";
		returnStats += String.valueOf(isEnabled) + "; ";
		if (category != null) { returnStats += category.getCategoryName(); }
		returnStats += String.valueOf(builtIn);
		return returnStats;
	}
	
	public abstract void executeHotKeyAction();
}
