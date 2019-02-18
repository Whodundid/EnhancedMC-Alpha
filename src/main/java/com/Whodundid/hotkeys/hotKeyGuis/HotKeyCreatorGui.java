package com.Whodundid.hotkeys.hotKeyGuis;

import com.Whodundid.debug.IDebugCommand;
import com.Whodundid.hotkeys.HotKeyManager;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.CommandHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.DebugHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.GuiOpenerHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.HandHeldCommandHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.KeyCategoryActivatorHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.KeyCategoryDeactivatorHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ModActivatorHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ModDeactivatorHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ScriptHotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.*;
import com.Whodundid.hotkeys.keySaveLoad.HotKeyBuilder;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.*;
import com.Whodundid.main.util.enhancedGui.guiObjects.*;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiDialogueBox.DialogueBoxTypes;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.miscUtil.Resources;
import com.Whodundid.main.util.miscUtil.EUtil;
import com.Whodundid.scripts.scriptBase.Script;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;

//Jan 9, 2019
//Last edited: Feb 17, 2019
//Edit note: Added support for key categories
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public class HotKeyCreatorGui extends EnhancedGui {
	
	HotKeyManager man = (HotKeyManager) RegisteredSubMods.getMod(SubModType.HOTKEYS);
	EGuiButton create, back, setEnabled, selectType, selectArg1, stepByStep;
	EGuiButton select, cancelSel;
	EGuiTextField keyNameEntry, mainArgEntry, secondaryArgEntry, categoryEntry, descriptionEntry;
	KeyEntryTextField keysEntry;
	EGuiDropDownList trueFalseList;
	EGuiTextArea selectionList;
	InnerEnhancedGui selectionGui;
	EGuiDialogueBox msgBox;
	HotKey key;
	KeyComboAction keys;
	KeyActionType selectedHotKeyType;
	Class selectedGui;
	Script selectedScript;
	IDebugCommand selectedDebug;
	SubModType selectedMod;
	KeyBinding selectedKeyBind;
	boolean enabledVal = true;
	String originalKeyName = "";
	
	public HotKeyCreatorGui() { super(); }
	public HotKeyCreatorGui(HotKey keyIn) { super(); key = keyIn; }
	public HotKeyCreatorGui(EnhancedGui oldGui) { super(oldGui); }
	public HotKeyCreatorGui(EnhancedGui oldGui, HotKey keyIn) { super(oldGui); key = keyIn; }
	public HotKeyCreatorGui(int posX, int posY) { super(posX, posY); }
	public HotKeyCreatorGui(int posX, int posY, HotKey keyIn) { super(posX, posY); key = keyIn; }
	public HotKeyCreatorGui(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	public HotKeyCreatorGui(int posX, int posY, EnhancedGui oldGui, HotKey keyIn) { super(posX, posY, oldGui); key = keyIn; }
	
	protected enum SelectionType { Type, Gui, Script, Debug, Mod, Keybind; }
	
	@Override
	public void initGui() {
		centerGuiWithDimensions(256, 324);
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		//trueFalseList = new EGuiDropDownList(this, wPos - 91, hPos + 37, 17).setFixedWidth(true, 70);
		
		keyNameEntry = new EGuiTextField(this, startX + 23, startY + 38, width - 48, 13).setTextWhenEmpty("enter a key name");
		selectType = new EGuiButton(this, startX + 23, startY + 76, 170, 17, "Select a type");
		keysEntry = new KeyEntryTextField(this, startX + 23, startY + 114, width - 48, 13);
		selectArg1 = new EGuiButton(this, startX + 23, startY + 149, 140, 17, "Arg1");
		mainArgEntry = new EGuiTextField(this, startX + 23, startY + 152, width - 48, 13).setMaxStringLength(100);
		secondaryArgEntry = new EGuiTextField(this, startX + 23, startY + 190, width - 48, 13).setMaxStringLength(200);
		categoryEntry = new EGuiTextField(this, startX + 23, startY + 228, width - 48, 13).setMaxStringLength(20).setTextWhenEmpty("enter a category name");
		descriptionEntry = new EGuiTextField(this, startX + 23, startY + 266, width - 48, 13).setMaxStringLength(200).setTextWhenEmpty("enter a hotkey description");
		
		create = new EGuiButton(this, startX + 9, endY - 28, 53, 20, key != null ? "Edit" : "Create");
		setEnabled = new EGuiButton(this, startX + 101, endY - 28, 53, 20, key != null ? key.isEnabled() ? "Enabled" : "Disabled" : "Enabled");
		back = new EGuiButton(this, startX + 194, endY - 28, 53, 20, "Back");
		
		setEnabled.setDisplayStringColor(key != null ? key.isEnabled() ? 0x55ff55 : 0xff5555 : 0x55ff55);
		keysEntry.setTextWhenEmpty("enter keys");
		
		//trueFalseList.addListEntry(new DropDownListEntry<Boolean>(Boolean.TRUE.toString(), Boolean.TRUE));
		//trueFalseList.addListEntry(new DropDownListEntry<Boolean>(Boolean.FALSE.toString(), Boolean.FALSE));
		
		setEnabled.setVisible(true);
		
		//addObject(trueFalseList);
		addObject(keyNameEntry, keysEntry, mainArgEntry, secondaryArgEntry, categoryEntry, descriptionEntry, setEnabled);
		addObject(create, selectType, selectArg1, back);
		
		if (key != null) { loadKeyValues(key); }
		else { updateVisibleObjects(); }
	}

	@Override
	public void drawObject(int mX, int mY, float ticks) {
		drawDefaultBackground();
		
		drawCenteredStringWithShadow("Hotkey Creation", wPos, startY + 8, 0xb2b2b2);
		
		//draw container
		drawRect(startX + 9, startY + 20, endX - 9, endY - 35, 0xff000000);
		drawRect(startX + 10, startY + 21, endX - 10, endY - 36, 0xff2D2D2D);
		
		//draw separation lines
		drawRect(startX + 10, startY + 58, endX - 9, startY + 59, 0xff000000);
		drawRect(startX + 10, startY + 96, endX - 9, startY + 97, 0xff000000);
		drawRect(startX + 10, startY + 134, endX - 9, startY + 135, 0xff000000);
		drawRect(startX + 10, startY + 172, endX - 9, startY + 173, 0xff000000);
		drawRect(startX + 10, startY + 210, endX - 9, startY + 211, 0xff000000);
		drawRect(startX + 10, startY + 248, endX - 9, startY + 249, 0xff000000);
		
		//draw separation titles
		drawBuilder(mX, mY, ticks);
	}
	
	private void drawBuilder(int mX, int mY, float ticks) {
		drawStringWithShadow("Name:", startX + 13, startY + 24, 0xffd800);
		drawStringWithShadow("Type:", startX + 13, startY + 62, 0xffd800);
		drawStringWithShadow("Keys:", startX + 13, startY + 100, 0xffd800);
		
		int firstArgColor = 0xb2b2b2, secondArgColor = 0xb2b2b2;
		String firstArgName = "Key Data Entry", secondArgName = "Extra Data Entry";
		if (selectedHotKeyType != null) {
			switch (selectedHotKeyType) {
			case COMMAND: firstArgName = "Command:"; firstArgColor = 0xffd800; break;
			case COMMAND_PLAYER_HANDHELD_CONDITION: firstArgName = "Command:"; secondArgName = "Test Item ID:"; firstArgColor = 0xffd800; secondArgColor = 0xffd800; break;
			case DEBUG: firstArgName = "Debug Command:"; firstArgColor = 0xffd800; break;
			case GUI_OPENER: firstArgName = "Gui to be opened:"; firstArgColor = 0xffd800; break;
			case MC_KEYBIND_MODIFIER: firstArgName = "MC KeyBind:"; secondArgName = "New KeyBind Value: (true / false)"; firstArgColor = 0xffd800; secondArgColor = 0xffd800; break;
			case CATEGORY_ACTIVATOR: firstArgName = "Hotkey category to activate:"; firstArgColor = 0xffd800; break;
			case CATEGORY_DEACTIVATOR: firstArgName = "Hotkey category to deactivate:"; firstArgColor = 0xffd800; break;
			case MOD_ACTIVATOR: firstArgName = "Mod to activate:"; firstArgColor = 0xffd800; break;
			case MOD_DEACTIVATOR: firstArgName = "Mod to deactivate:"; firstArgColor = 0xffd800; break;
			case SCRIPT: firstArgName = "Script:"; secondArgName = "Script Arguments:"; firstArgColor = 0xffd800; secondArgColor = 0xffd800;break;
			default: break;
			}
		}
		
		drawStringWithShadow(firstArgName, startX + 13, startY + 138, firstArgColor);
		drawStringWithShadow(secondArgName, startX + 13, startY + 176, secondArgColor);
		drawStringWithShadow("Category:", startX + 13, startY + 214, 0xffd800);
		drawStringWithShadow("Description:", startX + 13, startY + 252, 0xffd800);
	}
	
	private void loadKeyValues(HotKey keyIn) {
		originalKeyName = keyIn.getKeyName();
		keyNameEntry.setText(keyIn.getKeyName());
		enabledVal = keyIn.isEnabled();
		selectedHotKeyType = keyIn.getHotKeyType();
		selectType.setDisplayString(KeyActionType.getStringFromType(selectedHotKeyType));
		keysEntry.setText(EUtil.keysToString(keyIn.getKeyCombo().getKeys()));
		keys = new KeyComboAction(keysEntry.getKeys());
		
		mainArgEntry.setVisible(false);
		secondaryArgEntry.setVisible(false);
		selectArg1.setVisible(false);
		
		switch (selectedHotKeyType) {
		case COMMAND:
			mainArgEntry.setText(((CommandHotKey) keyIn).getCommand()).setVisible(true);
			break;
		case COMMAND_PLAYER_HANDHELD_CONDITION:
			mainArgEntry.setText(((GuiOpenerHotKey) keyIn).getGuiDisplayName()).setVisible(true);
			secondaryArgEntry.setText(((HandHeldCommandHotKey) keyIn).getItemID() + "").setVisible(true);
			break;
		case DEBUG:
			selectArg1.setDisplayString(IDebugCommand.getDebugCommandName(((DebugHotKey) keyIn).getDebugFunction())).setVisible(true);
			selectedDebug = ((DebugHotKey) keyIn).getDebugFunction();
			break;
		case GUI_OPENER:
			selectArg1.setDisplayString(((GuiOpenerHotKey) keyIn).getGuiDisplayName()).setVisible(true);
			selectedGui = ((GuiOpenerHotKey) keyIn).getGui();
			break;
		case CATEGORY_ACTIVATOR:
			selectArg1.setDisplayString(((KeyCategoryActivatorHotKey) keyIn).getCategoryName()).setVisible(true);
			break;
		case CATEGORY_DEACTIVATOR:
			selectArg1.setDisplayString(((KeyCategoryDeactivatorHotKey) keyIn).getCategoryName()).setVisible(true);
			break;
		case MOD_ACTIVATOR:
			selectArg1.setDisplayString(SubModType.getModName(((ModActivatorHotKey) keyIn).getSubMod())).setVisible(true);
			selectedMod = ((ModActivatorHotKey) keyIn).getSubMod();
			break;
		case MOD_DEACTIVATOR:
			selectArg1.setDisplayString(SubModType.getModName(((ModDeactivatorHotKey) keyIn).getSubMod())).setVisible(true);
			selectedMod = ((ModDeactivatorHotKey) keyIn).getSubMod();
			break;
		case SCRIPT:
			selectArg1.setDisplayString(((ScriptHotKey) keyIn).getScript().getScriptName()).setVisible(true);
			secondaryArgEntry.setText(((ScriptHotKey) keyIn).getScriptArgs() + "").setVisible(true);
			selectedScript = ((ScriptHotKey) keyIn).getScript();
			break;
		default: break;
		}
		descriptionEntry.setText(keyIn.getKeyDescription());
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(selectType)) {
			if (selectedHotKeyType != KeyActionType.BUILTINCODE) {
				openSelectionGui(SelectionType.Type);
			} else {
				createErrorDialogue("Edit Error", "Cannot modify a Built In hotkey's key type!");
			}
		}
		if (object.equals(selectArg1)) {
			switch (selectedHotKeyType) {
			case DEBUG: openSelectionGui(SelectionType.Debug); break;
			case GUI_OPENER: openSelectionGui(SelectionType.Gui); break;
			case MC_KEYBIND_MODIFIER: openSelectionGui(SelectionType.Keybind); break;
			case MOD_ACTIVATOR:
			case MOD_DEACTIVATOR: openSelectionGui(SelectionType.Mod); break;
			case SCRIPT: openSelectionGui(SelectionType.Script); break;
			default: break;
			}
		}
		if (object.equals(setEnabled)) {
			enabledVal = !enabledVal;
			setEnabled.setDisplayString(enabledVal ? "Enabled" : "Disabled");
			setEnabled.setDisplayStringColor(enabledVal ? 0x55ff55 : 0xff5555);
		}
		if (object == back) {
			closeGui();
		}
		if (object.equals(create)) {
			try {
				if (!keyNameEntry.getText().isEmpty() && keyNameEntry.getText().equals("enter a key name")) { throw new MissingHotKeyArgumentException("Key name not defined!"); }
				//editing
				if (key != null) {
					if (keys == null) { throw new MissingHotKeyArgumentException("No keys defined!"); }
					switch (key.getHotKeyType()) {
					case COMMAND:
						if (!mainArgEntry.getText().isEmpty() && mainArgEntry.getText().equals("enter command")) {
							throw new MissingHotKeyArgumentException("Command not set!");
						}
						((CommandHotKey) key).setCommand(mainArgEntry.getText());
						break;
					case COMMAND_PLAYER_HANDHELD_CONDITION:
						if (!mainArgEntry.getText().isEmpty() && mainArgEntry.getText().equals("enter command")) {
							throw new MissingHotKeyArgumentException("Command not set!");
						}
						
						try {
							if (!secondaryArgEntry.getText().isEmpty() && secondaryArgEntry.getText().equals("enter an item id to test for")) {
								throw new MissingHotKeyArgumentException("Item id not specified!");
							}
							Integer.parseInt(secondaryArgEntry.getText());
						} catch (NumberFormatException e) {
							throw new MissingHotKeyArgumentException("Could not parse item id!");
						} catch (MissingHotKeyArgumentException e) {
							throw e;
						} catch (Exception e) { e.printStackTrace(); }
						
						((HandHeldCommandHotKey) key).setCommand(mainArgEntry.getText());
						((HandHeldCommandHotKey) key).setItemID(Integer.parseInt(secondaryArgEntry.getText()));
						break;
					case DEBUG:
						if (selectedDebug == null) { throw new MissingHotKeyArgumentException("DebugCommand not selected!"); }
						((DebugHotKey) key).setDebugFunction(selectedDebug);
						break;
					case GUI_OPENER:
						if (selectedGui == null) { throw new MissingHotKeyArgumentException("No gui not selected!"); }
						((GuiOpenerHotKey) key).setGui(selectedGui);
						break;
					case CATEGORY_ACTIVATOR:
						if (!categoryEntry.getText().isEmpty() && categoryEntry.getText().equals("enter a category name")) { throw new MissingHotKeyArgumentException("Category name not entered!"); }
						((KeyCategoryActivatorHotKey) key).setKeyCategory(categoryEntry.getText());
						break;
					case CATEGORY_DEACTIVATOR:
						if (!categoryEntry.getText().isEmpty() && categoryEntry.getText().equals("enter a category name")) { throw new MissingHotKeyArgumentException("Category name not entered!"); }
						((KeyCategoryDeactivatorHotKey) key).setKeyCategory(categoryEntry.getText());
						break;
					case MOD_ACTIVATOR:
						if (selectedMod == null) { throw new MissingHotKeyArgumentException("SubMod not selected!"); }
						((ModActivatorHotKey) key).setSubMod(selectedMod);
						break;
					case MOD_DEACTIVATOR:
						if (selectedMod == null) { throw new MissingHotKeyArgumentException("SubMod not selected!"); }
						((ModDeactivatorHotKey) key).setSubMod(selectedMod);
						break;
					case SCRIPT:
						if (selectedScript == null) { throw new MissingHotKeyArgumentException("Script not selected!"); }
						((ScriptHotKey) key).setScript(selectedScript);
						((ScriptHotKey) key).setScriptArgs(secondaryArgEntry.getText().split(","));
						break;
					default: break;
					}
					msgBox = new EGuiDialogueBox(this, wPos - 150, hPos - 48, 300, 75, DialogueBoxTypes.custom) {
						{ okButton = new EGuiButton(this, midX - 25, midY + 10, 50, 20, "Ok"); addObject(okButton); }
						@Override
						public void actionPerformed(IEnhancedActionObject object) {
							if (object == okButton) {
								man.saveHotKeys();
								closeGui();
							}
						}
					};
					msgBox.setMessage("Success! Hotkey: " + originalKeyName + " edited.").setMessageColor(0x55ff55);
					msgBox.setDisplayString("Hotkey Edit");
					addObject(msgBox);
				}
				//creating
				else {
					if (keysEntry.getKeys().length == 0) { throw new MissingHotKeyArgumentException("No keys defined!"); }
					HotKeyBuilder builder = man.getKeyBuilder();
					if (selectedHotKeyType != null) {						
						switch (selectedHotKeyType) {
						case COMMAND:
							if (!mainArgEntry.getText().isEmpty() && mainArgEntry.getText().equals("enter command")) {
								throw new MissingHotKeyArgumentException("Command not set!");
							}
							builder.setBuilderCommand(mainArgEntry.getText()); 
							break;
						case COMMAND_PLAYER_HANDHELD_CONDITION:
							if (!mainArgEntry.getText().isEmpty() && mainArgEntry.getText().equals("enter command")) {
								throw new MissingHotKeyArgumentException("Command not set!");
							}
							
							try {
								if (!secondaryArgEntry.getText().isEmpty() && secondaryArgEntry.getText().equals("enter an item id to test for")) {
									throw new MissingHotKeyArgumentException("Item id not specified!");
								}
								Integer.parseInt(secondaryArgEntry.getText());
							} catch (NumberFormatException e) {
								throw new MissingHotKeyArgumentException("Could not parse item id!");
							} catch (MissingHotKeyArgumentException e) {
								throw e;
							} catch (Exception e) { e.printStackTrace(); }
							
							builder.setBuilderCommandAndItemArgs(mainArgEntry.getText(), Integer.parseInt(secondaryArgEntry.getText()));
							break;
						case DEBUG:
							if (selectedDebug == null) { throw new MissingHotKeyArgumentException("DebugCommand not selected!"); }
							builder.setBuilderDebugCommand(selectedDebug);
							break;
						case GUI_OPENER:
							if (selectedGui == null) { throw new MissingHotKeyArgumentException("No gui not selected!"); }
							builder.setBuilderGuiToBeOpened(selectedGui);
							break;
						case MC_KEYBIND_MODIFIER:
							if (selectedKeyBind == null) { throw new MissingHotKeyArgumentException("KeyBind not selected!"); }
							builder.setBuilderKeyBindingIn(selectedKeyBind, (Boolean) trueFalseList.getSelectedEntry().getEntryObject());
							break;
						case CATEGORY_ACTIVATOR:
						case CATEGORY_DEACTIVATOR:
							if (!categoryEntry.getText().isEmpty() && categoryEntry.getText().equals("enter a category name")) { throw new MissingHotKeyArgumentException("Category name not entered!"); }
							break;
						case MOD_ACTIVATOR:
						case MOD_DEACTIVATOR:
							if (selectedMod == null) { throw new MissingHotKeyArgumentException("SubMod not selected!"); }
							builder.setBuilderSubMod(selectedMod); 
							break;
						case SCRIPT:
							if (selectedScript == null) { throw new MissingHotKeyArgumentException("Script not selected!"); }
							builder.setBuilderScriptToBeRun(selectedScript, secondaryArgEntry.getText().split(","));
							break;
						default: break;
						}
						
						builder.setBuilderKeys(new KeyComboAction(keysEntry.getKeys()));
						String keyDesc = descriptionEntry.getText().equals("enter a hotkey description") ? "No description set." : descriptionEntry.getText();
						String category = categoryEntry.getText().equals("enter a category name") ? null : categoryEntry.getText();
						
						if (builder.buildHotKey(keyNameEntry.getText(), category, keyDesc, enabledVal, selectedHotKeyType)) {
							msgBox = new EGuiDialogueBox(this, wPos - 150, hPos - 48, 300, 75, DialogueBoxTypes.ok);
							msgBox.setMessage("Success! Hotkey: " + keyNameEntry.getText() + " created.").setMessageColor(0x55ff55);
							msgBox.setDisplayString("Hotkey Creation");
							addObject(msgBox);
							clearEntryData();
						}
					} else {
						createErrorDialogue("Creation Error", "No key type selected!");
					}
				}
			} catch (MissingHotKeyArgumentException e) {
				createErrorDialogue("Creation Error", e.getMessage());
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	public void clearEntryData() {
		keyNameEntry.setText("");
		keysEntry.setText("");
		mainArgEntry.setText("");
		secondaryArgEntry.setText("");
		descriptionEntry.setText("");
	}
	
	protected void openSelectionGui(SelectionType typeIn) {
		selectionGui = new InnerEnhancedGui(this, startX + ((width - 200) / 2), startY + ((height - 230) / 2) + 8, 200, 230) {
			@Override
			public void initGui() {
				requestFocus();
				setFocusLockObject(selectionGui);
				
				header = new EGuiHeader(this);
				header.setDisplayString("Select a " + typeIn.toString());
				
				select = new EGuiButton(this, startX + 10, endY - 28, 80, 20, "Select");
				cancelSel = new EGuiButton(this, startX + 110, endY - 28, 80, 20, "Cancel");
				
				selectionList = new EGuiTextArea(this, startX + 10, startY + 10, 180, 185, false).setDrawLineNumbers(true);
				
				addObject(header, select, cancelSel, selectionList);
				
				switch (typeIn) {
				case Debug: for (IDebugCommand c : IDebugCommand.values()) { selectionList.addTextLine(EnumChatFormatting.GREEN + IDebugCommand.getDebugCommandName(c), 0xffffff, c); } break;
				case Gui: for (Class c : Resources.guis) { selectionList.addTextLine(EnumChatFormatting.GREEN + c.getSimpleName(), 0xffffff, c); } break;
				case Keybind: for (KeyBinding k : mc.gameSettings.keyBindings) { selectionList.addTextLine(EnumChatFormatting.GREEN + k.getKeyDescription(), 0xffffff, k); } break;
				case Mod: for (SubModType m : SubModType.values()) { selectionList.addTextLine(EnumChatFormatting.GREEN + SubModType.getModName(m), 0xffffff, m); } break;
				case Script: break;
				case Type:
					for (KeyActionType t : KeyActionType.values()) {
						if (t.canUserCreate()) { selectionList.addTextLine(EnumChatFormatting.GREEN + KeyActionType.getStringFromType(t), 0xffffff, t); }
					}
					break;
				default: break;
				}
				
				if (!selectionList.getTextDocument().isEmpty()) {
					if (selectedHotKeyType != null) { selectionList.setSelectedLine(selectionList.getLineWithObject(selectedHotKeyType)); }
					else { selectionList.setSelectedLine(selectionList.getTextLineWithLineNumber(1)); }
				}
				
				bringToFront();
			}
			@Override
			public void drawObject(int mXIn, int mYIn, float ticks) {
				drawDefaultBackground();
				super.drawObject(mXIn, mYIn, ticks);
			}
			@Override
			public void actionPerformed(IEnhancedActionObject object) {
				if (object.equals(select)) {
					if (selectionList.getCurrentLine() != null && selectionList.getCurrentLine().getStoredObj() != null) {
						Object o = selectionList.getCurrentLine().getStoredObj();
						switch (typeIn) {
						case Debug: selectedDebug = (IDebugCommand) o; selectArg1.setDisplayString(IDebugCommand.getDebugCommandName(selectedDebug)).setVisible(true); break;
						case Gui: selectedGui = (Class) o; selectArg1.setDisplayString(selectedGui.getSimpleName()).setVisible(true); break;
						case Keybind: selectedKeyBind = (KeyBinding) o; selectArg1.setDisplayString(selectedKeyBind.getKeyDescription()).setVisible(true); break;
						case Mod: selectedMod = (SubModType) o; selectArg1.setDisplayString(SubModType.getModName(selectedMod)).setVisible(true); break;
						case Script: selectedScript = (Script) o; selectArg1.setDisplayString(selectedScript.getScriptName()).setVisible(true); break;
						case Type:
							selectedHotKeyType = (KeyActionType) o;
							selectType.setDisplayString(KeyActionType.getStringFromType(selectedHotKeyType)).setVisible(true);
							updateVisibleObjects(selectedHotKeyType);
							break;
						default: break;
						}
					}
					close();
				}
				if (object.equals(cancelSel)) { close(); }
			}
		};
		
		addObject(selectionGui);
	}
	
	public void updateVisibleObjects() { updateVisibleObjects(null); }
	public void updateVisibleObjects(KeyActionType typeIn) {
		try {
			mainArgEntry.setVisible(false);
			secondaryArgEntry.setVisible(false);
			selectArg1.setVisible(false);
			
			KeyActionType testType;
			testType = selectedHotKeyType != null ? typeIn : selectedHotKeyType;
			
			if (testType != null) {
				switch (testType) {
				case COMMAND: mainArgEntry.setTextWhenEmpty("enter command").setVisible(true); break;
				case COMMAND_PLAYER_HANDHELD_CONDITION:
					mainArgEntry.setTextWhenEmpty("enter command").setVisible(true);
					secondaryArgEntry.setTextWhenEmpty("enter an item id to test for").setVisible(true);
					break;
				case GUI_OPENER: selectArg1.setDisplayString("Select a Gui").setVisible(true); break;
				case SCRIPT:
					selectArg1.setDisplayString("Select a Script").setVisible(true);
					secondaryArgEntry.setTextWhenEmpty("enter script arguments").setVisible(true);
					break;
				case DEBUG: selectArg1.setDisplayString("Select a Debug Command").setVisible(true); break;
				case CATEGORY_ACTIVATOR:
				case CATEGORY_DEACTIVATOR: mainArgEntry.setTextWhenEmpty("enter category name").setVisible(true); break;
				case MOD_ACTIVATOR:
				case MOD_DEACTIVATOR: selectArg1.setDisplayString("Select a Mod").setVisible(true); break;
				case MC_KEYBIND_MODIFIER: selectArg1.setDisplayString("Select a KeyBind").setVisible(true); trueFalseList.setVisible(true); break;
				default: break;
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private void createErrorDialogue(String errorName, String errorMessage) {
		msgBox = new EGuiDialogueBox(this, wPos - 150, hPos - 48, 300, 75, DialogueBoxTypes.ok);
		msgBox.setMessage("Error: " + errorMessage).setMessageColor(0xff5555);
		msgBox.setDisplayString(errorName);
		addObject(msgBox);
	}
}
