package one.oktw;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {
    private boolean hackOnlineMode = true;
    private boolean hackEarlySend = false;
    private boolean hackMessageChain = true;
    private String disconnectMessage = "This server requires you to connect with Velocity.";
    private String secret = "";

    public static ModConfig load(Path configPath) {
        try {
            ModConfig config;
            
            if (Files.exists(configPath)) {
                config = new Toml().read(configPath.toFile()).to(ModConfig.class);
            } else {
                config = new ModConfig();
            }
            
            File configFile = configPath.toFile();
            try {
                new TomlWriter().write(config, configFile);
            } catch (IOException e) {
                LogManager.getLogger().error("Init config failed.", e);
            }
            
            String envDisconnectMessage = System.getenv("FABRIC_PROXY_MESSAGE");
            if (envDisconnectMessage!=null) {
                config.disconnectMessage = envDisconnectMessage;
            }
            
            String envSecret = System.getenv("FABRIC_PROXY_SECRET");
            if (envSecret!=null) {
                config.secret = envSecret;
            } else {
                String envSecretFile = System.getenv("FABRIC_PROXY_SECRET_FILE");
                if (envSecretFile!=null) {
                    try {
                        config.secret = Files.readString(Path.of(envSecretFile));
                    } catch (IOException e) {
                        LogManager.getLogger().error("Unable to read secret file {}: {}", envSecretFile, e);
                    }
                }
            }
            
            return config;
            
        } catch (Exception e) {
            LogManager.getLogger().error("Error while loading config!", e);
        }
        
        return new ModConfig();
    }
    
    public String getAbortedMessage() {
        return disconnectMessage;
    }
    
    public boolean getHackOnlineMode() {
        return hackOnlineMode;
    }
    
    public boolean getHackEarlySend() {
        return hackEarlySend;
    }
    
    public boolean getHackMessageChain() {
        return hackMessageChain;
    }
    
    public String getSecret() {
        return secret;
    }
}
