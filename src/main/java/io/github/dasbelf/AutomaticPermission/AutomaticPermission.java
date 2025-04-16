package io.github.dasbelf.AutomaticPermission;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.dasbelf.AutomaticPermission.commands.CommandHandler;
import io.github.dasbelf.AutomaticPermission.files.*;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Plugin(id = "automaticpermission", name = "AutomaticPermission", version = BuildConstants.VERSION)
public class AutomaticPermission {






    private final Path dataDirectory;

    public String storage_method;
    DatabaseHandler_old database;
    ConfigHandler config;
    StorageHandler storageHandler;


    private final List<UUID> connectedPlayers = new ArrayList();

    public List<String> listedGroups = new ArrayList();
    public List<String> listedUsers = new ArrayList();


    @Getter
    private final Logger logger;

    @Getter
    private final ProxyServer proxy;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws SQLException {


        if (storage_method.equals("sql"))
        {
            if (this.database.init())
            {
                logger.info("Datenbankzugriff möglich");
            }
            else {
                logger.info("Datenbankdzugriff nicht möglich, ggf. Rechte des Nutzers prüfen");
            }
        }
        else
        {
            logger.info("Daten werden in .json gespeichert!");
        }

        CommandManager commandManager = proxy.getCommandManager();

        //SimpleCommand simpleCommand =

        commandManager.register(commandManager.metaBuilder("automaticpermission")
                        .aliases("autoperm")
                        .aliases("ap")
                        .build(), new CommandHandler(this.logger));


    }

    @Inject
    public AutomaticPermission(Logger logger, ProxyServer proxy, @DataDirectory Path dataDirectory) {
        this.proxy = proxy;
        this.logger = logger;
        this.dataDirectory = dataDirectory;



        logger.info("AutomaticPermission has initialized successfully");
        logger.info("Please Check your config under plugins/AutomaticPermission");
        //logger.info(String.valueOf(this.dataDirectory));


        this.config = new ConfigHandler(this.dataDirectory, this);

        logger.info("IP: " + config.ip);

        if (storage_method.equals("sql"))
        {
            storageHandler = new DatabaseHandler(config.ip, config.port, config.database, config.table_group, config.username, config.password);
        }
        else if (storage_method.equals("json"))
        {
            storageHandler = new FileHandler();
        }
        else
        {
            throw new ExceptionInInitializerError("Wrong storage Method, please check your config!");
        }
    }


    @Subscribe
    public void onServerPreConnectionEvent(ServerPreConnectEvent event)
    {
        Player player = event.getPlayer();


        if (!connectedPlayers.contains(player.getUniqueId()))
        {
            checkIfPlayerIsWhitelisted();
            checkPlayerGroup(player.getUniqueId(), player);
            //command lpv user dasbelf parent set developer
        }

    }

    private void checkPlayerGroup(UUID playerID, Player player)
    {
        database.connect();

        String groupname = database.querySelectGroup(playerID);
        String command = "lpv user " + player.getUsername() + " parent set " + groupname;
        logger.info(command);
        proxy.getCommandManager().executeAsync(proxy.getConsoleCommandSource(), command);

    }

    private void checkIfPlayerIsWhitelisted()
    {

    }


}
