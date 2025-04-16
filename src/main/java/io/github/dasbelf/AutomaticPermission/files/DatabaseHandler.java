package io.github.dasbelf.AutomaticPermission.files;

import java.sql.Connection;

public class DatabaseHandler implements StorageHandler
{
    private final String url;
    private final String base_url;
    private final String user;
    private final String password;
    private Connection connection;
    private final String table_group;


    public DatabaseHandler(String ip, int port, String database, String table_group, String user, String password)
    {
        this.base_url = "jdbc:mysql://" + ip + ":" + port + "?useSSL=false&autoReconnect=true";
        this.url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true";
        this.table_group = table_group;
        this.user = user;
        this.password = password;
    }

    public void init()
    {

    }

}
