package com.ink.unforgiving.configs;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public abstract class Config {
  protected File config_file;
  protected Configuration config;
  
  protected Config() {
  }
  
  public void load() {
    // Load config files
    config_file = new File("config" + file_name() + ".cfg");
    String abs = config_file.getAbsolutePath();
    System.out.println("Loading file at: " + abs);
    if (!config_file.exists()) {
      // Create config file
      try {
        config_file.createNewFile();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    config = new Configuration(config_file);
    config.load();
    
    reload();
    save();
  }
  
  public abstract String file_name();
  
  public abstract void reload();
  
  public void save() {
    // Save config
    config.save();
  }
}
