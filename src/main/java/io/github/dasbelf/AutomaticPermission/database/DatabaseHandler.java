package io.github.dasbelf.AutomaticPermission.database;

import java.sql.*;
import java.util.UUID;

public class DatabaseHandler
{
        private final String url;
        private final String user;
        private final String password;
        private Connection connection;

        public DatabaseHandler(String ip, int port, String database, String user, String password)
        {
            this.url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true";
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

    public String selectQuery(UUID userID) {
        String sql = "SELECT uuid, luckperms_group FROM auto_perm_whitelist WHERE uuid = ?";

        try
        {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, userID.toString());

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

