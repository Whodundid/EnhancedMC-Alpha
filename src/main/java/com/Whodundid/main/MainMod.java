package com.Whodundid.main;

import com.Whodundid.clearVisuals.ClearVisuals;
import com.Whodundid.commands.*;
import com.Whodundid.enhancedChat.EnhancedChat;
import com.Whodundid.hotkeys.HotKeyManager;
import com.Whodundid.main.global.GlobalSettings;
import com.Whodundid.main.global.SettingsGui;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.util.EventListener;
import com.Whodundid.main.util.miscUtil.CursorHelper;
import com.Whodundid.main.util.miscUtil.EFontRenderer;
import com.Whodundid.main.util.miscUtil.Resources;
import com.Whodundid.main.util.networking.NetworkHandler;
import com.Whodundid.nameHistory.NameHistory;
import com.Whodundid.pingDrawer.Ping;
import com.Whodundid.scripts.ScriptManager;
import com.Whodundid.sls.SkinSwitcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

//Jan 10, 2019
//Last edited: Feb 18, 2019
//Edit note: added 'isInitialized' boolean flag which indicates if all of EnhancedMC's dependancies have fully loaded.
//First Added: Nov 16, 2017
//Author: Hunter Bragg

@Mod(modid = MainMod.MODID, version = MainMod.VERSION, name = MainMod.NAME)
public class MainMod {
	public static final String MODID = "enhancedmc";
	public static final String VERSION = "alpha_1.0";
	public static final String NAME = "EnhancedMC";
	public static final Minecraft mc = Minecraft.getMinecraft();
	public static final KeyBinding openSettingsGui = new KeyBinding("Opens the global mod settings gui", Keyboard.KEY_P, "MainMod");
	public static EFontRenderer fontRenderer;
	public static EnhancedInGameGui enhancedMCGui;
	static boolean isInitialized = false;
	
	static {
		//register EventListener
		MinecraftForge.EVENT_BUS.register(new EventListener());
		
		//register sub mods
		RegisteredSubMods.registerSubMod(new EnhancedChat());
		RegisteredSubMods.registerSubMod(new ClearVisuals());
		RegisteredSubMods.registerSubMod(new HotKeyManager());
		RegisteredSubMods.registerSubMod(new NameHistory());
		RegisteredSubMods.registerSubMod(new Ping());
		RegisteredSubMods.registerSubMod(new ScriptManager());
		RegisteredSubMods.registerSubMod(new SkinSwitcher());
	}
	
	@EventHandler 
    private void preInit(FMLPreInitializationEvent event) {
		GlobalSettings.loadConfig();
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.preInit(); }
	}
	
	@EventHandler
    private void init(FMLInitializationEvent event) {
    	//register keybinds
    	ClientRegistry.registerKeyBinding(openSettingsGui);
    	
    	//initialize client resources
    	new Resources();
    	new NetworkHandler();
    	CursorHelper.init();
    	fontRenderer = new EFontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);
		if (mc.gameSettings.language != null) {
            fontRenderer.setUnicodeFlag(mc.isUnicode());
            fontRenderer.setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
        }
		((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(fontRenderer);
    	enhancedMCGui = new EnhancedInGameGui(mc);
    	mc.ingameGUI = enhancedMCGui;
    	
    	//register commands
    	ClientCommandHandler h = ClientCommandHandler.instance;
        h.registerCommand(new CMD_CheckNameHistory());
        h.registerCommand(new CMD_debug());
        
        //initialize sub mods
    	for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.init(); }
    	
    	isInitialized = true;
	}
	
	@EventHandler
	private void postInit(FMLPostInitializationEvent e) {
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) {
			m.postInit();
			if (m.hasConfig() && !m.getConfig().loadConfig()) { m.getConfig().saveConfig(); }
		}
	}
	
	public static void checkOpenSettings() {
		if (openSettingsGui.isPressed()) { mc.displayGuiScreen(new SettingsGui()); }
	}
	
	public static Minecraft getMC() { return mc; }
	public static EFontRenderer getFontRenderer() { return fontRenderer; }
	public static EnhancedInGameGui getInGameGui() { return enhancedMCGui; }
	public static boolean isInitialized() { return isInitialized; }
	protected static void createdByHunterBragg() {}
}
