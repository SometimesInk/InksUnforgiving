package com.ink.unforgiving.config;

import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.common.Loader;

import java.io.File;

public abstract class Config {
    protected File configFile;
    protected Configuration config;

    protected Config() {
    }

    public void load() {
        // Load config files
        configFile = new File(Loader.instance().getConfigDir(),  fileName() + ".cfg");

        System.out.println("Loading file at: " + configFile.getAbsolutePath());

        // Check for null file
        if(!configFile.exists()) {
            // Create config file
            try {
                configFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        config = new Configuration(configFile);
        config.load();

        reload();
        save();
    }

    /**
     * Get the file name of the config file
     *
     * <p> This method should return the name of the file without the extension
     * </p>
     * @return the file name
     */
    public abstract String fileName();

    public abstract void reload();

    public void save() {
        // Save config
        config.save();
    }
}
