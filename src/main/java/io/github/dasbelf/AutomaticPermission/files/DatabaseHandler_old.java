package io.github.dasbelf.AutomaticPermission.files;

import java.sql.*;
import java.util.UUID;

public class DatabaseHandler_old
{
    private final String url;
    private final String base_url;
    private final String user;
    private final String password;
    private Connection connection;
    private final String table_group;

    public DatabaseHandler_old(String ip, int port, String database, String table_group, String user, String password)
    {
        this.base_url = "jdbc:mysql://" + ip + ":" + port + "?useSSL=false&autoReconnect=true";
        this.url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true";
        this.table_group = table_group;
        this.user = user;
        this.password = password;
    }


    public boolean connect()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            return true;
        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean initConnect()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(base_url, user, password);
            return true;
        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean disconnect()
    {
        if (connection != null)
        {
            try
            {
                connection.close();
                return true;
            } catch (SQLException e) {

                e.printStackTrace();
                return false;
            }
        }
        return false;
    }


    public boolean init()
    {
        String sql = "CREATE DATABASE IF NOT EXISTS automatic_permission;";

        try
        {
            initConnect();
            Statement stmt = connection.createStatement();
            stmt.execute(sql);

            connect();
            stmt = connection.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS auto_perm_group(\n" +
                    "id int AUTO_INCREMENT PRIMARY KEY,\n" +
                    "uuid varchar(255) NOT NULL,\n" +
                    "luckperms_group varchar(255) DEFAULT 'default'\n" +
                    ");";
            stmt.execute(sql);
            disconnect();
            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            disconnect();
            return false;

        }


    }

    public String querySelectGroup(UUID userID) {
        String sql = "SELECT uuid, luckperms_group FROM ? WHERE uuid = ?";

        try
        {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, table_group);
            ps.setString(2, userID.toString());

            try (ResultSet res = ps.executeQuery()) {
                // Überprüfen, ob Ergebnisse vorhanden sind
                if (res.next()) {
                    // Gebe die "luckperms_group"-Spalte zurück
                    return res.getString("luckperms_group");
                }
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();

        }
        return "default";

    }

    public Connection getConnection()
    {
        return connection;
    }
}

