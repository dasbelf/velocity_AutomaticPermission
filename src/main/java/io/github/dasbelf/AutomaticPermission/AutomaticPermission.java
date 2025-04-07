package io.github.dasbelf.AutomaticPermission;

import com.google.inject.Inject;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.dasbelf.AutomaticPermission.database.DatabaseHandler;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Plugin(id = "automaticpermission", name = "AutomaticPermission", version = BuildConstants.VERSION)
public class AutomaticPermission {

    DatabaseHandler database;


    private final List<UUID> connectedPlayers = new ArrayList();

    @Getter
    private final Logger logger;

    @Getter
    private final ProxyServer proxy;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws SQLException {


        if (database.connect())
        {
            logger.info("Datenbankzugriff möglich");
        }
        else {
            logger.info("Datenbankdzugriff nicht möglich");
        }
    }

    @Inject
    public AutomaticPermission(Logger logger, ProxyServer proxy) {
        this.proxy = proxy;
        this.logger = logger;

        logger.info("AutomaticPermission has initialized successfully");
        logger.info("Please Check your config under plugins/AutomaticPermission");


        database = new DatabaseHandler("10.1.1.20", 3306, "automatic_permission","root","Secure+Password");
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

        String groupname = database.selectQuery(playerID);
        String command = "lpv user " + player.getUsername() + " parent set " + groupname;
        logger.info(command);
        proxy.getCommandManager().executeAsync(proxy.getConsoleCommandSource(), command);

    }

    private void checkIfPlayerIsWhitelisted()
    {

    }


}
