package com.ink.unforgiving.event;

import com.ink.unforgiving.config.ConfigCache;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventJoinServer {
    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event) {
        if(event.entity instanceof EntityPlayer && Minecraft.getMinecraft().thePlayer != null) {
            ConfigCache.getInstance().reloadPlayers();
        }
    }
}
