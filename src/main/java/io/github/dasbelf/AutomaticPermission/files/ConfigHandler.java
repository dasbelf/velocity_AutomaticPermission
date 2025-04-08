package io.github.dasbelf.AutomaticPermission.files;

import io.github.dasbelf.AutomaticPermission.AutomaticPermission;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class ConfigHandler
{

    private final Path configFile;
    public String ip;
    public int port;
    public String database;
    public String table_group;
    public String username;
    public String password;


    public ConfigHandler(Path dataDirectory, AutomaticPermission automaticPermission)
    {
        this.configFile = dataDirectory.resolve("config.yml");
        automaticPermission.storage_method = init();
    }

    public String init()
    {
        try
        {
            //Ordner anlegen, falls nicht vorhanden
            if (Files.notExists(configFile.getParent()) )
            {
                Files.createDirectories(configFile.getParent());
            }

            if (Files.notExists(configFile))
            {
                Files.createFile(configFile);

                // Optional: Standardinhalt reinschreiben
                String defaultConfig = "Storage-Method: json\n#Option 1: json, Option 2: sql" +
                        "\nMySql Config: \n  host: 127.0.0.1\n  port: 3306\n" +
                        "  database: automatic_permission\n  table_group: auto_perm_group\n" +
                        "  user: example\n  # Needs full rights on database\n  password: example\n";
                Files.writeString(configFile, defaultConfig);
            }

            // SnakeYAML-Parser erstellen
            Yaml yaml = new Yaml();
            FileReader reader = new FileReader(configFile.toFile());

            // YAML-Datei in eine Map laden
            Map<String, Object> config = yaml.load(reader);


            Object mysqlConfigObj = config.get("MySql Config");

            Map<String, Object> mysqlConfig = Map.of();


            // Überprüfen, ob "MySql Config" eine Map ist
            if (mysqlConfigObj instanceof Map)
            {
                 mysqlConfig = (Map<String, Object>) mysqlConfigObj;
            }

            // Werte aus der Map auslesen
            if ("sql".equals(config.get("Storage-Method")))
            {
                this.ip = (String) mysqlConfig.get("host");
                this.port = (Integer) mysqlConfig.get("port");

                this.database = (String) mysqlConfig.get("database");
                this.table_group = (String) mysqlConfig.get("table_group");
                this.username = (String) mysqlConfig.get("user");;
                this.password = (String) mysqlConfig.get("password");;
                return "sql";
            }
            else
            {
                return "json";
            }


        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "json";
        }
    }
}