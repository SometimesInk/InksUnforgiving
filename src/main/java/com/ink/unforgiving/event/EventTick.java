package com.ink.unforgiving.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class EventTick {

    @SubscribeEvent
    public void render(RenderGameOverlayEvent event) {
        List<EntityPlayer> entities = Minecraft.getMinecraft().theWorld.playerEntities;

    }
}