package com.ink.unforgiving.configs;

import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.common.Loader;

import java.io.File;

public abstract class Config {
    protected File config_file;
    protected Configuration config;

    protected Config() {
    }

    public void load() {
        // Load config files
        config_file = new File(Loader.instance().getConfigDir(),  file_name() + ".cfg");

        System.out.println("Loading file at: " + config_file.getAbsolutePath());

        // Check for null file
        if(!config_file.exists()) {
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
