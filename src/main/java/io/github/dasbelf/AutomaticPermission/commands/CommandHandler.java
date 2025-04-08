package io.github.dasbelf.AutomaticPermission.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import io.github.dasbelf.AutomaticPermission.AutomaticPermission;
import io.github.dasbelf.AutomaticPermission.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.slf4j.Logger;

public class CommandHandler implements SimpleCommand
{

    private final Logger logger;

    public CommandHandler(Logger logger)
    {
        this.logger = logger;
    }



    @Override
    public void execute (final Invocation invocation)
    {
        CommandSource source = invocation.source();

        String[] args = invocation.arguments();
        String[] args_copy = args;




        for (String arg : args_copy) {
            logger.info(arg);
        }

        if (args.length >= 1)
        {

            switch (args[0])
            {
                case "reload": source.sendMessage(Component.text("This should reload the complete plugin.", NamedTextColor.RED)); break;
                case "user":
                {
                    SubCommandUser subCommandUser = new SubCommandUser(this);
                    break;
                }
                case "group":
                {
                    source.sendMessage(Component.text("AutoPermission: This command isnt implemented yet.", NamedTextColor.RED));
                    break;
                }
                default:
                    source.sendMessage(Component.text("AutoPermission: This command does not exist.", NamedTextColor.RED));
            }

        }
        else
        {
            source.sendMessage(Component.text("AutoPermission: This command isnt complete.", NamedTextColor.RED));
        }

    }






}
