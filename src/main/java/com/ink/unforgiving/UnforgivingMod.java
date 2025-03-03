package com.ink.unforgiving;

import com.ink.unforgiving.commands.*;
import com.ink.unforgiving.configs.*;
import com.ink.unforgiving.events.*;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = UnforgivingMod.MODID, version = UnforgivingMod.VERSION)
public class UnforgivingMod
{
    public static final String MODID = "unforgiving";
    public static final String VERSION = "2.0";

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
        ConfigKOS.get_instance().load();
        ConfigCache.get_instance().load();
    }
}
