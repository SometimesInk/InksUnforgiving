package com.ink.unforgiving.commands;

import net.minecraft.command.ICommandSender;

public abstract class Subcommand {
  protected abstract String get_name();
  
  public abstract void execute(ICommandSender sender, String[] args);
}
