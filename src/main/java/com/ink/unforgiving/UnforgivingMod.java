package com.ink.unforgiving;

import com.ink.unforgiving.command.*;
import com.ink.unforgiving.config.*;
import com.ink.unforgiving.event.*;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.*;

@Mod(modid = UnforgivingMod.MODID, version = UnforgivingMod.VERSION)
public class UnforgivingMod
{
    public static final String MODID = "inks_unforgiving";
    public static final String VERSION = "1.0";
    public static Map<UUID, String> unforgivenPlayers = new HashMap<UUID, String>();

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // Registering events
        MinecraftForge.EVENT_BUS.register(new EventRender());
        MinecraftForge.EVENT_BUS.register(new EventJoinServer());

        // Registering commands
        ClientCommandHandler.instance.registerCommand(new CommandKOS());
    }

    @EventHandler
    public void preInit(FMLInitializationEvent event) {
        // Load config
        ConfigKOS.getInstance().load();
        ConfigCache.getInstance().load();
    }
}
