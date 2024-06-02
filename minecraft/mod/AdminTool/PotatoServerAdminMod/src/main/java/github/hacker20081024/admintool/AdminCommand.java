package github.hacker20081024.admintool;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

class LoginAdmin extends CommandBase{

    @Override
    public String getName() {
        return "login-admin";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.login-admin.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                if (strings.length != 2) {
                    throw new CommandException("<aisi163 to " + iCommandSender.getName() + "> Command Format error,right format: /login-admin <username> <password>");
                } else {
                    String username = strings[0];
                    String password = strings[1];
                    int remaining_login_chance = LoginAdminUsernameAndPasswordManager.GetRemainingLoginChance((EntityPlayer) iCommandSender.getCommandSenderEntity());
                    if (remaining_login_chance <= 0) {
                        throw new CommandException("<aisi163 to " + iCommandSender.getName() + "> You can't login admin user,reason: repeat login too much");
                    } else {
                        if (LoginAdminUsernameAndPasswordManager.Login(username, password, (EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                            iCommandSender.sendMessage(new TextComponentString("<aisi163 to " + iCommandSender.getName() + "> Login successfully,admin welcomes to server."));
                            AdminPlayerManager.AddAdminPlayerInAdminPlayers(((EntityPlayer) iCommandSender.getCommandSenderEntity()));
                            AdminPlayerManager.AddAdminPlayerRegisterTime(((EntityPlayer) iCommandSender.getCommandSenderEntity()));
                            AdminPlayerManager.AddAdminPlayerAdminPrivilegeIssuer(((EntityPlayer) iCommandSender.getCommandSenderEntity()), username);
                            ITextComponent radio_note = new TextComponentString("[System Info] every server admin and simple player attention,server admin \"" + iCommandSender.getName() + "\" go online");
                            for (int i = 0; i < PlayerHandleManager.getPlayerHandleListLength(); i++) {
                                EntityPlayer other_player_handle = PlayerHandleManager.getPlayerHandle(i);
                                if (other_player_handle != null) {
                                    other_player_handle.sendMessage(radio_note);
                                }
                            }
                            ((EntityPlayer) iCommandSender.getCommandSenderEntity()).setGameType(GameType.CREATIVE);
                        } else {
                            if (remaining_login_chance > 0) {
                                throw new CommandException("<aisi163 to " + iCommandSender.getName() + "> Username or Password error,remaining login chance: " + (remaining_login_chance - 1));
                            }
                        }
                    }
                }
            }
        } else {
            iCommandSender.sendMessage(new TextComponentString("<aisi163 to "+iCommandSender+"> Server admin, you have already logged"));
        }
    }
}

class LogoutAdmin extends CommandBase {

    @Override
    public String getName() {
        return "logout-admin";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.logout-admin.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
         if (iCommandSender.getCommandSenderEntity() != null){
             if (AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())){
                 AdminPlayerManager.DeleteAdminRegisterTime((EntityPlayer) iCommandSender.getCommandSenderEntity());
                 AdminPlayerManager.DeleteAdminPlayerAdminPrivilegeIssuer((EntityPlayer) iCommandSender.getCommandSenderEntity());
                 AdminPlayerManager.RemoveAdminPlayerFromAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity());
                 iCommandSender.sendMessage(new TextComponentString("<aisi163 to"+iCommandSender.getName()+"> good bye, server admin"));
                 ITextComponent radio_note = new TextComponentString("[System Info] every server admin and simple player attention,server admin \"" + iCommandSender.getName() + "\" offline");
                 for (int i = 0; i < PlayerHandleManager.getPlayerHandleListLength(); i++) {
                     EntityPlayer other_player_handle = PlayerHandleManager.getPlayerHandle(i);
                     if (other_player_handle != null) {
                         other_player_handle.sendMessage(radio_note);
                     }
                 }
                 ((EntityPlayer)iCommandSender.getCommandSenderEntity()).setGameType(GameType.SURVIVAL);
             } else {
                 throw new CommandException("<aisi163 to "+iCommandSender.getName()+"> You haven't login admin user");
             }
         }
    }
}

class Admin extends CommandBase {

    @Override
    public String getName() {
        return "admin";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.admin.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"admin\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 1) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /admin <player_name>");
                } else {
                    if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Not find player \"" + strings[0] + "\"");
                    } else {
                        if (BlackListPlayerManager.IsPlayerInBlackList(strings[0])) {
                            throw new CommandException("<" + iCommandSender.getName() + "> Player \"" + strings[0] + "\" is a black list member, can't give (he/she) admin privilege");
                        } else if (AdminPlayerManager.IsPlayerInAdminPlayers(PlayerHandleManager.getPlayerHandleByPlayerName(strings[0]))) {
                            ITextComponent text_comment = new TextComponentString("<" + iCommandSender.getName() + "> Player \"" + strings[0] + "\" already have admin privilege");
                            iCommandSender.sendMessage(text_comment);
                        } else {
                            Date now_time_obj = new Date();
                            String admin_register_time = now_time_obj.toString();
                            String admin_uuid = UUID.randomUUID().toString();
                            String admin_privilege_issuer = iCommandSender.getName();
                            boolean result1 = AdminPlayerManager.AddAdminPlayerInAdminPlayers(PlayerHandleManager.getPlayerHandleByPlayerName(strings[0]));
                            boolean result2 = AdminPlayerManager.AddAdminPlayerRegisterTime(PlayerHandleManager.getPlayerHandleByPlayerName(strings[0]));
                            boolean result3 = AdminPlayerManager.AddAdminPlayerAdminPrivilegeIssuer(PlayerHandleManager.getPlayerHandleByPlayerName(strings[0]),iCommandSender.getName());
                            if (result1 && result2 && result3) {
                                ITextComponent old_admin_note1 = new TextComponentString("<" + iCommandSender.getName() + "> You gave player \"" + strings[0] + "\" admin privilege");
                                iCommandSender.sendMessage(old_admin_note1);
                                ITextComponent radio_note1 = new TextComponentString("[System Info] Congratulations player \"" + strings[0] + "\" become a new server admin");
                                int player_handle_list_length = PlayerHandleManager.getPlayerHandleListLength();
                                for (int i = 0; i < player_handle_list_length; i++) {
                                    EntityPlayer receive_message_player = PlayerHandleManager.getPlayerHandle(i);
                                    if (receive_message_player != null) {
                                        receive_message_player.sendMessage(radio_note1);
                                        if (receive_message_player.getName().equals(strings[0])) {
                                            ITextComponent new_admin_note1 = new TextComponentString("<aisi163 to " + strings[0] + "> say: Hello, my new server admin, here is your admin info:");
                                            ITextComponent new_admin_note2 = new TextComponentString("[About Your Admin Info]");
                                            ITextComponent new_admin_note3 = new TextComponentString("Admin unique identification: " + receive_message_player.getUniqueID().toString());
                                            ITextComponent new_admin_note4 = new TextComponentString("Admin register time: " + AdminPlayerManager.GetAdminPlayerRegisterTime(receive_message_player));
                                            ITextComponent new_admin_note5 = new TextComponentString("Admin Privilege Issuer: " + AdminPlayerManager.GetAdminPlayerAdminPrivilegeIssuer(receive_message_player));
                                            receive_message_player.sendMessage(new_admin_note1);
                                            receive_message_player.sendMessage(new_admin_note2);
                                            receive_message_player.sendMessage(new_admin_note3);
                                            receive_message_player.sendMessage(new_admin_note4);
                                            receive_message_player.sendMessage(new_admin_note5);
                                        }
                                    }
                                }
                            } else {
                                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"admin\" command,reason: server error");
                            }
                        }
                    }
                }
            }
        }
    }
}

class BanAdmin extends CommandBase {

    @Override
    public String getName() {
        return "ban-admin";
    }

    @Override
    public java.lang.String getUsage(ICommandSender iCommandSender) {
        return "command.ban-admin.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer)iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"ban-admin\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 1 && strings.length != 2) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /ban-admin <player_name> [ban_reason]");
                } else {
                    if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Not find player \"" + strings[0] + "\"");
                    } else {
                        if (!AdminPlayerManager.IsPlayerInAdminPlayers(PlayerHandleManager.getPlayerHandleByPlayerName(strings[0]))) {
                            throw new CommandException("<" + iCommandSender.getName() + "> Player \"" + strings[0] + "\" haven't admin privilege");
                        } else if (strings[0].equals("aisi163")) {
                            // if other server admin want to ban me
                            throw new CommandException("<aisi163 to " + iCommandSender.getName() + "> say: Server Admin What are you doing, you want to ban me???");
                        } else {
                            boolean result1 = AdminPlayerManager.DeleteAdminRegisterTime(PlayerHandleManager.getPlayerHandleByPlayerName(strings[0]));
                            boolean result2 = AdminPlayerManager.DeleteAdminPlayerAdminPrivilegeIssuer(PlayerHandleManager.getPlayerHandleByPlayerName(strings[0]));
                            boolean result3 = AdminPlayerManager.RemoveAdminPlayerFromAdminPlayers(PlayerHandleManager.getPlayerHandleByPlayerName(strings[0]));
                            ITextComponent commander_sender_note = new TextComponentString("<" + iCommandSender.getName() + "> You revoke the admin privilege of player \"" + strings[0] + "\"");
                            iCommandSender.sendMessage(commander_sender_note);
                            if (result1 && result2 && result3) {
                                TextComponentString radio_note = new TextComponentString("[System Info] Revoke the admin privilege of player \"" + strings[0] + " \" ");
                                int player_handle_list_length = PlayerHandleManager.getPlayerHandleListLength();
                                for (int i = 0; i < player_handle_list_length; i++) {
                                    EntityPlayer receive_message_player = PlayerHandleManager.getPlayerHandle(i);
                                    if (receive_message_player != null) {
                                        receive_message_player.sendMessage(radio_note);
                                        if (receive_message_player.getName().equals(strings[0])) {
                                            TextComponentString revoke_note = new TextComponentString("<" + strings[0] + ">" + " Other admin revoke your admin privilege, you are not admin now!!!");
                                            TextComponentString revoke_reason = new TextComponentString("<" + strings[0] + ">" + " Revoke Reason: " + (strings.length == 2 ? strings[1] : "(Don't know)"));
                                            receive_message_player.sendMessage(revoke_note);
                                            receive_message_player.sendMessage(revoke_reason);
                                        }
                                    }
                                }
                            } else {
                                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"ban-admin\" command,reason: server error");
                            }
                        }
                    }
                }
            }
        }
    }
}

class Privilege extends CommandBase {

    @Override
    public String getName() {
        return "privilege";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.privilege.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer)iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"privilege\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 1) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format Error,right format: /privilege <player_name>");
                } else {
                    if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Not find player \"" + strings[0] + "\"");
                    } else {
                        String player_privilege = AdminPlayerManager.IsPlayerInAdminPlayers(PlayerHandleManager.getPlayerHandleByPlayerName(strings[0])) ? "Server Admin" : "Simple Player";
                        ITextComponent note = new TextComponentString("<" + iCommandSender.getName() + "> The privilege of player \"" + strings[0] + "\" is [" + player_privilege + "]");
                        iCommandSender.sendMessage(note);
                    }
                }
            }
        }
    }
}

class Ping extends CommandBase {

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.ping.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"ping\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 1) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /ping <player_name>");
                } else {
                    if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Not Find Player \"" + strings[0] + "\"");
                    } else {
                        EntityPlayerMP player_handle = (EntityPlayerMP) PlayerHandleManager.getPlayerHandleByPlayerName(strings[0]);
                        if (player_handle == null) {
                            throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"ping\" command,reason: server error");
                        } else {
                            String player_IP_address = player_handle.getPlayerIP();
                            ITextComponent note = new TextComponentString("<" + iCommandSender.getName() + "> The IP address of player \"" + strings[0] + "\" is [" + player_IP_address + "]");
                            iCommandSender.sendMessage(note);
                        }
                    }
                }
            }
        }
    }
}

class ListAll extends CommandBase{

    @Override
    public String getName() {
        return "list-all";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.list-all.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"list-all\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 0) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command Format error,right format: /list-all");
                } else {
                    List<String> name_list = new ArrayList<String>();
                    int player_handle_number = PlayerHandleManager.getPlayerHandleListLength();
                    for (int i = 0; i < player_handle_number; i++) {
                        EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                        if (player_handle != null) {
                            name_list.add(player_handle.getName());
                        }
                    }
                    ITextComponent note1 = new TextComponentString("<" + iCommandSender.getName() + "> Number of all players: " + name_list.size() + "/" + minecraftServer.getMaxPlayers());
                    String player_list = "<" + iCommandSender.getName() + "> All players List: ";
                    for (int name_index = 0; name_index < name_list.size(); name_index++) {
                        player_list += name_list.get(name_index);
                        if (name_index != name_list.size() - 1) {
                            player_list += ",";
                        }
                    }
                    ;
                    if (name_list.size() == 0) {
                        player_list += "(None)";
                    }
                    ITextComponent note2 = new TextComponentString(player_list);
                    iCommandSender.sendMessage(note1);
                    iCommandSender.sendMessage(note2);
                }
            }
        }
    }
}

class ListPlayer extends CommandBase{

    @Override
    public String getName() {
        return "list-player";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.list-player.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"list-player\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 0) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command Format error,right format: /list-player");
                } else {
                    List<String> name_list = new ArrayList<String>();
                    int player_handle_number = PlayerHandleManager.getPlayerHandleListLength();
                    for (int i = 0; i < player_handle_number; i++) {
                        EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                        if (player_handle != null) {
                            if (!AdminPlayerManager.IsPlayerInAdminPlayers(player_handle)) {
                                name_list.add(player_handle.getName());
                            }
                        }
                    }
                    String player_list = "<" + iCommandSender.getName() + "> Simple players list: ";
                    for (int name_index = 0; name_index < name_list.size(); name_index++) {
                        player_list += name_list.get(name_index);
                        if (name_index != name_list.size() - 1) {
                            player_list += ",";
                        }
                    }
                    if (name_list.size() == 0) {
                        player_list += "(None)";
                    }
                    ITextComponent note1 = new TextComponentString(player_list);
                    iCommandSender.sendMessage(note1);
                }
            }
        }
    }
}

class ListAdmin extends CommandBase{

    @Override
    public String getName() {
        return "list-admin";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.list-admin.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"list-admin\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 0) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command Format error,right format: /list-admin");
                } else {
                    List<String> name_list = new ArrayList<String>();
                    int player_handle_number = PlayerHandleManager.getPlayerHandleListLength();
                    for (int i = 0; i < player_handle_number; i++) {
                        EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                        if (player_handle != null) {
                            if (AdminPlayerManager.IsPlayerInAdminPlayers(player_handle)) {
                                name_list.add(player_handle.getName());
                            }
                        }
                    }
                    String player_list = "<" + iCommandSender.getName() + "> Server Admins list: ";
                    for (int name_index = 0; name_index < name_list.size(); name_index++) {
                        player_list += name_list.get(name_index);
                        if (name_index != name_list.size() - 1) {
                            player_list += ",";
                        }
                    }
                    ;
                    if (name_list.size() == 0) {
                        player_list += "(None)";
                    }
                    ITextComponent note1 = new TextComponentString(player_list);
                    iCommandSender.sendMessage(note1);
                }
            }
        }
    }
}

class GPS extends CommandBase{

    @Override
    public String getName() {
        return "gps";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.gps.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"gps\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 1) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /gps <player_name>");
                } else {
                    if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Not Find Player \"" + strings[0] + "\"");
                    } else {
                        EntityPlayer player_handle = PlayerHandleManager.getPlayerHandleByPlayerName(iCommandSender.getName());
                        if (player_handle != null) {
                            BlockPos player_position = player_handle.getPosition();
                            World player_in_world = player_handle.getEntityWorld();
                            WorldType player_in_world_type = player_in_world.getWorldType();
                            String player_in_world_name = player_in_world_type.getName();
                            ITextComponent note = new TextComponentString("<" + iCommandSender.getName() + "> Found player at [x=" + player_position.getX() + ",y=" + player_position.getY() + ",z=" + player_position.getZ() + "] in [" + player_in_world_name + " World]");
                            iCommandSender.sendMessage(note);
                        } else {
                            throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"gps\" command,reason: server error");
                        }
                    }
                }
            }
        }
    }
};

class FixedPlayer extends CommandBase{

    @Override
    public String getName() {
        return "fixed-player";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.fixed-player.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"fixed-player\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 1 && strings.length != 2) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command Format error,right format: /fixed-player <player_name> [reason]");
                } else {
                    if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Not Find Player \"" + strings[0] + "\"");
                    } else if (strings[0].equals("aisi163") && !iCommandSender.getName().equals("aisi163")) {
                        throw new CommandException("<aisi163 to " + iCommandSender.getName() + "> Server Admin,are you kidding me??? you want to fixed me???");
                    } else {
                        if (FixedPlayerListManager.IsPlayerInFixedPlayerList(strings[0])) {
                            throw new CommandException("<" + iCommandSender.getName() + "> Player \"" + strings[0] + "\" had already been prohibited move in game");
                        } else {
                            boolean result1 = FixedPlayerListManager.AddPlayerInFixedPlayerList(strings[0]);
                            boolean result2 = FixedPlayerListManager.AddWhoFixedPlayer(strings[0], iCommandSender.getName());
                            if (result1 && result2) {
                                ITextComponent command_sender_note = new TextComponentString("<" + iCommandSender.getName() + "> You prohibited the move privilege of player \"" + strings[0] + "\" in the game");
                                ITextComponent radio_note = new TextComponentString("[System Information] every players and admins attention,player \"" + strings[0] + "\" was prohibited move in the game by \"" + iCommandSender.getName() + "\" admin");
                                ITextComponent fixed_move_player_note1 = new TextComponentString("<" + strings[0] + "> The Server Admin \"" + iCommandSender.getName() + "\" prohibited your move privilege in the game !!!");
                                ITextComponent fixed_move_player_nte2 = new TextComponentString("<" + strings[0] + "> Prohibited Reason: " + (strings.length == 2 ? strings[1] : "(don't know)"));
                                int player_handle_number = PlayerHandleManager.getPlayerHandleListLength();
                                for (int i = 0; i < player_handle_number; i++) {
                                    EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                                    if (player_handle != null) {
                                        if (player_handle.getName().equals(iCommandSender.getName())) {
                                            player_handle.sendMessage(command_sender_note);
                                        }
                                        if (player_handle.getName().equals(strings[0])) {
                                            player_handle.sendMessage(fixed_move_player_note1);
                                            player_handle.sendMessage(fixed_move_player_nte2);
                                        }
                                        player_handle.sendMessage(radio_note);
                                    }
                                }
                            } else {
                                throw new CommandException("<" + iCommandSender.getName() + "> You can't execute \"fixed-player\" command,reason: Server Error");
                            }
                        }
                    }
                }
            }
        }
    }
}

class UnFixedPlayer extends CommandBase {

    @Override
    public String getName() {
        return "unfixed-player";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.unfixed-player.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"unfixed-player\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 1) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /unfixed-player <player_name>");
                } else {
                    if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Not find player \"" + strings[0] + "\"");
                    } else {
                        if (!FixedPlayerListManager.IsPlayerInFixedPlayerList(strings[0])) {
                            throw new CommandException("<" + iCommandSender.getName() + "> Player \"" + strings[0] + "\" haven't been prohibit move privilege");
                        } else {
                            boolean result1 = FixedPlayerListManager.DeleteWhoFixedPlayer(strings[0]);
                            boolean result2 = FixedPlayerListManager.DeletePlayerFromFixedPlayerList(strings[0]);
                            if (result1 && result2) {
                                ITextComponent command_sender_note = new TextComponentString("<" + iCommandSender.getName() + "> Player \"" + strings[0] + "\" can move!!!");
                                ITextComponent radio_note = new TextComponentString("[System Information] every players and admins attention,player \"" + strings[0] + "\" can move now !!!");
                                ITextComponent unfixed_player = new TextComponentString("<aisi163 to " + strings[0] + "> You can move now,because the server admin \"" + iCommandSender.getName() + "\" won't prohibit your move privilege in the game");
                                int player_handle_number = PlayerHandleManager.getPlayerHandleListLength();
                                for (int i = 0; i < player_handle_number; i++) {
                                    EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                                    if (player_handle != null) {
                                        if (player_handle.getName().equals(iCommandSender.getName())) {
                                            player_handle.sendMessage(command_sender_note);
                                        }
                                        if (player_handle.getName().equals(strings[0])) {
                                            player_handle.sendMessage(unfixed_player);
                                        }
                                        player_handle.sendMessage(radio_note);
                                    }
                                }
                            } else {
                                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"unfixed-player\" command,reason: Server Error");
                            }
                        }
                    }
                }
            }
        }
    }
}

class ListFixedPlayer extends CommandBase{

    @Override
    public String getName() {
        return "list-fixed-player";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.list-fixed-player.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"list-fixed-player\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 0) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /list-fixed-player");
                } else {
                    String fixed_player_list = "Fixed Players List: ";
                    int fixed_player_length = FixedPlayerListManager.GetFixedPlayerLength();
                    for (int i = 0; i < fixed_player_length; i++) {
                        String player_name = FixedPlayerListManager.GetFixedPlayerName(i);
                        if (!player_name.isEmpty()) {
                            fixed_player_list += player_name;
                            if (i != fixed_player_length - 1) {
                                fixed_player_list += ',';
                            }
                        }
                    }
                    if (fixed_player_length == 0) {
                        fixed_player_list += "(None)";
                    }
                    ITextComponent note = new TextComponentString("<" + iCommandSender.getName() + "> " + fixed_player_list);
                    iCommandSender.sendMessage(note);
                }
            }
        }
    }
}

class ListUnFixedPlayer extends CommandBase {

    @Override
    public String getName() {
        return "list-unfixed-player";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.list-unfixed-player.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"list-unfixed-player\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 0) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /list-unfixed-player");
                } else {
                    String unfixed_player_list = "<" + iCommandSender.getName() + "> Unfixed Player List: ";
                    List<String> unfixed_player_name_list = new ArrayList<String>();
                    int player_handle_number = PlayerHandleManager.getPlayerHandleListLength();
                    for (int i = 0; i < player_handle_number; i++) {
                        EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                        if (player_handle != null) {
                            if (!FixedPlayerListManager.IsPlayerInFixedPlayerList(player_handle.getName())) {
                                unfixed_player_name_list.add(player_handle.getName());
                            }
                        }
                    }
                    for (int i = 0; i < unfixed_player_name_list.size(); i++) {
                        unfixed_player_list += unfixed_player_name_list.get(i);
                        if (i != unfixed_player_name_list.size() - 1) {
                            unfixed_player_list += ',';
                        }
                    }
                    if (unfixed_player_name_list.size() == 0) {
                        unfixed_player_list += "(None)";
                    }
                    ITextComponent note = new TextComponentString(unfixed_player_list);
                    iCommandSender.sendMessage(note);
                }
            }
        }
    }
}

class AddProhibitArea extends CommandBase {

    public static boolean IsNumberString(String str){
        for (int i = 0; i < str.length(); i++){
            if (i == 0) {
                if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != '-'){
                    return false;
                }
            } else {
                if (!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public java.lang.String getName() {
        return "add-prohibit-area";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.add-prohibit-area.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"add-prohibit-are\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 7) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /add-prohibit-area <x1> <y1> <z1> <x2> <y2> <z2> <prohibit_area_name>");
                } else {
                    String prohibit_area_x1_str = strings[0];
                    String prohibit_area_y1_str = strings[1];
                    String prohibit_area_z1_str = strings[2];
                    String prohibit_area_x2_str = strings[3];
                    String prohibit_area_y2_str = strings[4];
                    String prohibit_area_z2_str = strings[5];
                    String prohibit_area_prohibit_name = strings[6];
                    if (!IsNumberString(prohibit_area_x1_str) && !IsNumberString(prohibit_area_y1_str) && !IsNumberString(prohibit_area_z1_str) &&
                            !IsNumberString(prohibit_area_x2_str) && !IsNumberString(prohibit_area_y2_str) && !IsNumberString(prohibit_area_z2_str)) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Command format error, reason: <x1>,<y1>,<z1>,<x2>,<y2>,<z2> must be an integer");
                    } else if (ProhibitAreaManager.IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_prohibit_name)) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Prohibited area name already existed");
                    } else {
                        Integer prohibit_area_x1_int = new Integer(prohibit_area_x1_str);
                        Integer prohibit_area_y1_int = new Integer(prohibit_area_y1_str);
                        Integer prohibit_area_z1_int = new Integer(prohibit_area_z1_str);
                        Integer prohibit_area_x2_int = new Integer(prohibit_area_x2_str);
                        Integer prohibit_area_y2_int = new Integer(prohibit_area_y2_str);
                        Integer prohibit_area_z2_int = new Integer(prohibit_area_z2_str);
                        boolean result1 = ProhibitAreaManager.addProhibitAreaName(prohibit_area_prohibit_name);
                        boolean result2 = ProhibitAreaManager.SetProhibitAreaX1(prohibit_area_prohibit_name, prohibit_area_x1_int);
                        boolean result3 = ProhibitAreaManager.SetProhibitAreaY1(prohibit_area_prohibit_name, prohibit_area_y1_int);
                        boolean result4 = ProhibitAreaManager.SetProhibitAreaZ1(prohibit_area_prohibit_name, prohibit_area_z1_int);
                        boolean result5 = ProhibitAreaManager.SetProhibitAreaX2(prohibit_area_prohibit_name, prohibit_area_x2_int);
                        boolean result6 = ProhibitAreaManager.SetProhibitAreaY2(prohibit_area_prohibit_name, prohibit_area_y2_int);
                        boolean result7 = ProhibitAreaManager.SetProhibitAreaZ2(prohibit_area_prohibit_name, prohibit_area_z2_int);
                        if (result1 && result2 && result3 && result4 && result5 && result6 && result7) {
                            ITextComponent commander_sender_note = new TextComponentString("<" + iCommandSender.getName() + "> You create a new prohibited area named \"" + prohibit_area_prohibit_name + "\"");
                            ITextComponent radio_note = new TextComponentString("[System Information] All players and admins attention, server admin \"" + iCommandSender.getName() + "\" create a new prohibit area in game, prohibit area range [From (X=" + prohibit_area_x1_str + ",Y=" + prohibit_area_y1_str + ",Z=" + prohibit_area_z1_str + ") To (X=" + prohibit_area_x2_str + ",Y=" + prohibit_area_y2_str + ",Z=" + prohibit_area_z2_str + ")], Simple Player haven't privilege to this prohibited area");
                            int player_handle_number = PlayerHandleManager.getPlayerHandleListLength();
                            for (int i = 0; i < player_handle_number; i++) {
                                EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                                if (player_handle != null) {
                                    if (player_handle.getName().equals(iCommandSender.getName())) {
                                        player_handle.sendMessage(commander_sender_note);
                                    }
                                    player_handle.sendMessage(radio_note);
                                }
                            }
                        } else {
                            throw new CommandException("<" + iCommandSender.getName() + "> You can't execute \"add-prohibit-area\" command,reason: Server Error");
                        }
                    }
                }
            }
        }
    }
}

class ListProhibitArea extends CommandBase {

    public boolean IsNumberString(String str){
        return AddProhibitArea.IsNumberString(str);
    }

    @Override
    public String getName() {
        return "list-prohibit-area";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.list-prohibit-area.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"list-prohibit-area\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 0 && strings.length != 1) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /list-prohibit-area [page_number]");
                } else {
                    iCommandSender.sendMessage(new TextComponentString(""));
                    if (strings.length == 0) {
                        ITextComponent title_note = new TextComponentString("Prohibited Area List (Page: 1/" + ((int) (ProhibitAreaManager.getProhibitAreaNameLength() / 5) + 1) + ")");
                        ITextComponent split_line = new TextComponentString("=================================================");
                        iCommandSender.sendMessage(title_note);
                        iCommandSender.sendMessage(split_line);
                        for (int i = 0; i < 5; i++) {
                            if (i >= ProhibitAreaManager.getProhibitAreaNameLength()) {
                                if (i == 0) {
                                    iCommandSender.sendMessage(new TextComponentString("(None)"));
                                }
                                break;
                            } else {
                                String prohibited_area_name = ProhibitAreaManager.getProhibitAreaName(i);
                                int X1 = (ProhibitAreaManager.getProhibitAreaX1(prohibited_area_name) != null ? ProhibitAreaManager.getProhibitAreaX1(prohibited_area_name).intValue() : 0);
                                int Y1 = (ProhibitAreaManager.getProhibitAreaY1(prohibited_area_name) != null ? ProhibitAreaManager.getProhibitAreaY1(prohibited_area_name).intValue() : 0);
                                int Z1 = (ProhibitAreaManager.getProhibitAreaZ1(prohibited_area_name) != null ? ProhibitAreaManager.getProhibitAreaZ1(prohibited_area_name).intValue() : 0);
                                int X2 = (ProhibitAreaManager.getProhibitAreaX2(prohibited_area_name) != null ? ProhibitAreaManager.getProhibitAreaX2(prohibited_area_name).intValue() : 0);
                                int Y2 = (ProhibitAreaManager.getProhibitAreaY2(prohibited_area_name) != null ? ProhibitAreaManager.getProhibitAreaY2(prohibited_area_name).intValue() : 0);
                                int Z2 = (ProhibitAreaManager.getProhibitAreaZ2(prohibited_area_name) != null ? ProhibitAreaManager.getProhibitAreaZ2(prohibited_area_name).intValue() : 0);
                                ITextComponent line2 = new TextComponentString(prohibited_area_name + " " + "[Range: From (X=" + X1 + ",Y=" + Y1 + ",Z=" + Z1 + ") To (X=" + X2 + ",Y=" + Y2 + ",Z=" + Z2 + ")]");
                                iCommandSender.sendMessage(line2);
                            }
                        }
                        iCommandSender.sendMessage(split_line);
                    } else {
                        if (!IsNumberString(strings[0])) {
                            throw new CommandException("Command format error,reason: [page] must be an integer");
                        } else {
                            int page = Integer.parseInt(strings[0]) - 1;
                            if (page < 0) {
                                iCommandSender.sendMessage(new TextComponentString("(None)"));
                            } else if (page >= (int) (ProhibitAreaManager.getProhibitAreaNameLength() / 5) + 1) {
                                throw new CommandException("Command format error,reason: [page] too much, right range 1-" + ((int) (ProhibitAreaManager.getProhibitAreaNameLength() / 5) + 1));
                            } else {
                                ITextComponent title_note = new TextComponentString("Prohibited Area List (Page: " + (page + 1) + "/" + ((int) (ProhibitAreaManager.getProhibitAreaNameLength() / 5) + 1) + ")");
                                ITextComponent split_line = new TextComponentString("=================================================");
                                iCommandSender.sendMessage(title_note);
                                iCommandSender.sendMessage(split_line);
                                for (int i = page * 5; i < (page + 1) * 5; i++) {
                                    if (i >= ProhibitAreaManager.getProhibitAreaNameLength()) {
                                        if (i == 0) {
                                            iCommandSender.sendMessage(new TextComponentString("(None)"));
                                            break;
                                        }
                                    } else {
                                        String prohibited_area_name = ProhibitAreaManager.getProhibitAreaName(i);
                                        int X1 = (ProhibitAreaManager.getProhibitAreaX1(prohibited_area_name) != null ? ProhibitAreaManager.getProhibitAreaX1(prohibited_area_name).intValue() : 0);
                                        int Y1 = (ProhibitAreaManager.getProhibitAreaY1(prohibited_area_name) != null ? ProhibitAreaManager.getProhibitAreaY1(prohibited_area_name).intValue() : 0);
                                        int Z1 = (ProhibitAreaManager.getProhibitAreaZ1(prohibited_area_name) != null ? ProhibitAreaManager.getProhibitAreaZ1(prohibited_area_name).intValue() : 0);
                                        int X2 = (ProhibitAreaManager.getProhibitAreaX2(prohibited_area_name) != null ? ProhibitAreaManager.getProhibitAreaX2(prohibited_area_name).intValue() : 0);
                                        int Y2 = (ProhibitAreaManager.getProhibitAreaY2(prohibited_area_name) != null ? ProhibitAreaManager.getProhibitAreaY2(prohibited_area_name).intValue() : 0);
                                        int Z2 = (ProhibitAreaManager.getProhibitAreaZ2(prohibited_area_name) != null ? ProhibitAreaManager.getProhibitAreaZ2(prohibited_area_name).intValue() : 0);
                                        ITextComponent line2 = new TextComponentString(prohibited_area_name + " " + "[Range: From (X=" + X1 + ",Y=" + Y1 + ",Z=" + Z1 + ") To (X=" + X2 + ",Y=" + Y2 + ",Z=" + Z2 + ")]");
                                        iCommandSender.sendMessage(line2);
                                    }
                                }
                                iCommandSender.sendMessage(split_line);
                            }
                        }
                    }
                }
            }
        }
    }
}

class RemoveProhibitArea extends CommandBase {

    @Override
    public String getName() {
        return "remove-prohibit-area";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.remove-prohibit-area.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"remove-prohibit-area\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 1) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /remove-prohibit-area <prohibit_area_name>");
                } else {
                    String prohibited_area_name = strings[0];
                    if (!ProhibitAreaManager.IsProhibitAreaNameInProhibitAreaNameList(prohibited_area_name)) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Not find prohibited area name \"" + prohibited_area_name + "\"");
                    } else {
                        int X1 = (ProhibitAreaManager.getProhibitAreaX1(prohibited_area_name) != null ? ProhibitAreaManager.getProhibitAreaX1(strings[0]).intValue() : 0);
                        int Y1 = (ProhibitAreaManager.getProhibitAreaY1(prohibited_area_name) != null ? ProhibitAreaManager.getProhibitAreaY1(strings[0]).intValue() : 0);
                        int Z1 = (ProhibitAreaManager.getProhibitAreaZ1(prohibited_area_name) != null ? ProhibitAreaManager.getProhibitAreaZ1(strings[0]).intValue() : 0);
                        int X2 = (ProhibitAreaManager.getProhibitAreaX2(prohibited_area_name) != null ? ProhibitAreaManager.getProhibitAreaX2(strings[0]).intValue() : 0);
                        int Y2 = (ProhibitAreaManager.getProhibitAreaY2(prohibited_area_name) != null ? ProhibitAreaManager.getProhibitAreaY2(strings[0]).intValue() : 0);
                        int Z2 = (ProhibitAreaManager.getProhibitAreaZ2(prohibited_area_name) != null ? ProhibitAreaManager.getProhibitAreaZ2(strings[0]).intValue() : 0);
                        boolean result1 = ProhibitAreaManager.RemoveProhibitX1(prohibited_area_name);
                        boolean result2 = ProhibitAreaManager.RemoveProhibitY1(prohibited_area_name);
                        boolean result3 = ProhibitAreaManager.RemoveProhibitZ1(prohibited_area_name);
                        boolean result4 = ProhibitAreaManager.RemoveProhibitX2(prohibited_area_name);
                        boolean result5 = ProhibitAreaManager.RemoveProhibitY2(prohibited_area_name);
                        boolean result6 = ProhibitAreaManager.RemoveProhibitZ2(prohibited_area_name);
                        boolean result7 = ProhibitAreaManager.RemoveProhibitAreaName(strings[0]);
                        if (result1 && result2 && result3 && result4 && result5 && result6 && result7) {
                            ITextComponent commander_sender_note = new TextComponentString("<" + iCommandSender.getName() + "> You removed a prohibited area named \"" + strings[0] + "\"");
                            ITextComponent radio_note = new TextComponentString("[System Information] All players and admins attention, server admin \"" + iCommandSender.getName() + "\" removed a prohibited area named \"" + strings[0] + "\"" + ",prohibit area range [From (X=" + X1 + ",Y=" + Y1 + ",Z=" + Z1 + ") To (X=" + X2 + ",Y=" + Y2 + ",Z=" + Z2 + ")]");
                            int player_handle_number = PlayerHandleManager.getPlayerHandleListLength();
                            for (int i = 0; i < player_handle_number; i++) {
                                EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                                if (player_handle != null) {
                                    if (player_handle.getName().equals(iCommandSender.getName())) {
                                        player_handle.sendMessage(commander_sender_note);
                                    }
                                    player_handle.sendMessage(radio_note);
                                }
                            }
                        } else {
                            throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"remove-prohibit-area\" command,reason: Server Error");
                        }
                    }
                }
            }
        }
    }
}

class GetSpeed extends CommandBase {

    @Override
    public String getName() {
        return "get-speed";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.get-speed.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"get-speed\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 1) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /get-speed <player_name>");
                } else {
                    if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Not find player \"" + strings[0] + "\"");
                    } else {
                        int player_handle_number = PlayerHandleManager.getPlayerHandleListLength();
                        for (int i = 0; i < player_handle_number; i++) {
                            EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                            if (player_handle != null) {
                                if (player_handle.getName().equals(strings[0])) {
                                    Float AI_Move_Speed = PlayerHandleManager.player_AI_Move_Speed.get(player_handle);
                                    ITextComponent note = new TextComponentString("Player \"" + player_handle.getName() + "\" move speed: " + AI_Move_Speed);
                                    iCommandSender.sendMessage(note);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class OriginalSpeed extends CommandBase{

    public static final Float Game_original_AI_Move_Speed = new Float(0.1F);
    @Override
    public String getName() {
        return "original-speed";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.original-speed.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"get-speed\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 1) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /get-speed <player_name>");
                } else {
                    if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Not find player \"" + strings[0] + "\"");
                    } else if (strings[0].equals("aisi163") && !iCommandSender.getName().equals("aisi163")) {
                        throw new CommandException("<aisi163 to " + iCommandSender.getName() + "> Server admin,what are you doing??? you want to set my move speed???");
                    } else {
                        int player_handle_number = PlayerHandleManager.getPlayerHandleListLength();
                        for (int i = 0; i < player_handle_number; i++) {
                            EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                            if (player_handle != null) {
                                if (player_handle.getName().equals(strings[0])) {
                                    PlayerHandleManager.player_AI_Move_Speed.replace(player_handle, Game_original_AI_Move_Speed);
                                    float AI_Move_Speed = PlayerHandleManager.player_AI_Move_Speed.get(player_handle);
                                    ITextComponent note = new TextComponentString("Set player \"" + player_handle.getName() + "\" move speed: " + AI_Move_Speed);
                                    iCommandSender.sendMessage(note);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class SetSpeed extends CommandBase{

    @Override
    public String getName() {
        return "set-speed";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.set-speed.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"set-speed\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 2) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /set-speed <player_name> <speed>");
                } else {
                    if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Not find player \"" + strings[0] + "\"");
                    } else if (strings[0].equals("aisi163") && !iCommandSender.getName().equals("aisi163")) {
                        throw new CommandException("<aisi163 to " + iCommandSender.getName() + "> Server admin,what are you doing??? you want to set my move speed???");
                    } else {
                        int player_handle_number = PlayerHandleManager.getPlayerHandleListLength();
                        for (int i = 0; i < player_handle_number; i++) {
                            EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                            if (player_handle != null) {
                                if (player_handle.getName().equals(strings[0])) {
                                    Float player_AI_Move_Speed = new Float(strings[1]);
                                    if (player_AI_Move_Speed.isNaN()) {
                                        throw new CommandException("<" + iCommandSender.getName() + "> Command format error,reason: speed parameter type error,speed parameter must be a float");
                                    } else {
                                        if (player_AI_Move_Speed.floatValue() < 0.0F || player_AI_Move_Speed.floatValue() > 1.0F) {
                                            throw new CommandException("<" + iCommandSender.getName() + "> Command format error,reason: speed parameter range error, right range: 0.0F - 1.0F");
                                        } else {
                                            // 注意: 如果要更改玩家的移速,Mojang提供的setAIMoveSpeed函数去设置玩家的移速,因为
                                            // 这个函数已经被Mojang写死了,请使用其他的方案,(类如: 为玩家添加迅捷药水效果)
                                            // player_handle.setAIMoveSpeed(0.2F);
                                            PlayerHandleManager.player_AI_Move_Speed.replace(player_handle, player_AI_Move_Speed);
                                            float now_player_AI_Move_Speed = PlayerHandleManager.player_AI_Move_Speed.get(player_handle);
                                            ITextComponent note = new TextComponentString("Set player \"" + player_handle.getName() + "\" move speed: " + now_player_AI_Move_Speed);
                                            iCommandSender.sendMessage(note);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class ModeSU extends CommandBase{

    @Override
    public String getName() {
        return "mode-su";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.mode-su.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"mode-su\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 1) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /mode-su <player_name>");
                } else {
                    if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Not find player \"" + strings[0] + "\"");
                    } else if (strings[0].equals("aisi163") && !iCommandSender.getName().equals("aisi163")) {
                        throw new CommandException("<aisi163 to " + iCommandSender.getName() + "> Server Admin, what are you doing??? you want to set my game mode???");
                    } else {
                        int player_handle_length = PlayerHandleManager.getPlayerHandleListLength();
                        for (int i = 0; i < player_handle_length; i++) {
                            EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                            if (player_handle != null) {
                                if (player_handle.getName().equals(strings[0])) {
                                    player_handle.setGameType(GameType.SURVIVAL);
                                    ITextComponent note = new TextComponentString("<" + iCommandSender.getName() + "> Change the game mode of player \"" + strings[0] + "\" to SURVIVAL");
                                    player_handle.sendMessage(new TextComponentString("<" + player_handle.getName() + "> Your game mode is set to SURVIVAL"));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class ModeA extends CommandBase{

    @Override
    public String getName() {
        return "mode-a";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.mode-a.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"mode-a\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 1) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /mode-a <player_name>");
                } else {
                    if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Not find player \"" + strings[0] + "\"");
                    } else if (strings[0].equals("aisi163") && !iCommandSender.getName().equals("aisi163")) {
                        throw new CommandException("<aisi163 to " + iCommandSender.getName() + "> Server Admin, what are you doing??? you want to set my game mode???");
                    } else {
                        int player_handle_length = PlayerHandleManager.getPlayerHandleListLength();
                        for (int i = 0; i < player_handle_length; i++) {
                            EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                            if (player_handle != null) {
                                if (player_handle.getName().equals(strings[0])) {
                                    player_handle.setGameType(GameType.ADVENTURE);
                                    ITextComponent note = new TextComponentString("<" + iCommandSender.getName() + "> Change the game mode of player \"" + strings[0] + "\" to ADVENTURE");
                                    player_handle.sendMessage(new TextComponentString("<" + player_handle.getName() + "> Your game mode is set to ADVENTURE"));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class ModeC extends CommandBase{

    @Override
    public String getName() {
        return "mode-c";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.mode-c.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"mode-c\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 1) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /mode-c <player_name>");
                } else if (strings[0].equals("aisi163") && !iCommandSender.getName().equals("aisi163")) {
                    throw new CommandException("<aisi163 to " + iCommandSender.getName() + "> Server Admin, what are you doing??? you want to set my game mode???");
                } else {
                    if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Not find player \"" + strings[0] + "\"");
                    } else {
                        int player_handle_length = PlayerHandleManager.getPlayerHandleListLength();
                        for (int i = 0; i < player_handle_length; i++) {
                            EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                            if (player_handle != null) {
                                if (player_handle.getName().equals(strings[0])) {
                                    player_handle.setGameType(GameType.CREATIVE);
                                    ITextComponent note = new TextComponentString("<" + iCommandSender.getName() + "> Change the game mode of player \"" + strings[0] + "\" to CREATIVE");
                                    player_handle.sendMessage(new TextComponentString("<" + player_handle.getName() + "> Your game mode is set to CREATIVE"));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class ModeSP extends CommandBase{

    @Override
    public String getName() {
        return "mode-sp";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.mode-sp.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> you can't execute \"mode-sp\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 1) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /mode-sp <player_name>");
                } else {
                    if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Not find player \"" + strings[0] + "\"");
                    } else if (strings[0].equals("aisi163") && !iCommandSender.getName().equals("aisi163")) {
                        throw new CommandException("<aisi163 to " + iCommandSender.getName() + "> Server Admin, what are you doing??? you want to set my game mode???");
                    } else {
                        int player_handle_length = PlayerHandleManager.getPlayerHandleListLength();
                        for (int i = 0; i < player_handle_length; i++) {
                            EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                            if (player_handle != null) {
                                if (player_handle.getName().equals(strings[0])) {
                                    player_handle.setGameType(GameType.SPECTATOR);
                                    ITextComponent note = new TextComponentString("<" + iCommandSender.getName() + "> Change the game mode of player \"" + strings[0] + "\" to SPECTATOR");
                                    player_handle.sendMessage(new TextComponentString("<" + player_handle.getName() + "> Your game mode is set to SPECTATOR"));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class Mode extends CommandBase{

    @Override
    public String getName() {
        return "default-mode";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.default-mode.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> You can't execute \"mode\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 1) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command Format Error,right format: /mode <player_name>");
                } else {
                    if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Not find player \"" + strings[0] + "\"");
                    } else {
                        int player_handle_length = PlayerHandleManager.getPlayerHandleListLength();
                        for (int i = 0; i < player_handle_length; i++) {
                            EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                            if (player_handle != null) {
                                if (player_handle.getName().equals(strings[0])) {
                                    iCommandSender.sendMessage(new TextComponentString("<" + iCommandSender.getName() + "> Player \"" + strings[0] + "\" default game mode: " + minecraftServer.getGameType().getName()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class CanJump extends CommandBase{

    @Override
    public String getName() {
        return "can-jump";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.can-jump.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> You can't execute \"can-jump\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 1 && strings.length != 2) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /can-jump <player_name> [true|false]");
                } else {
                    if (strings.length == 1) {
                        if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                            throw new CommandException("<" + iCommandSender.getName() + "> Not find player \"" + strings[0] + "\"");
                        } else {
                            EntityPlayer player_handle = PlayerHandleManager.getPlayerHandleByPlayerName(strings[0]);
                            if (player_handle != null) {
                                boolean can_jump_boolean = PlayerHandleManager.player_can_jumps.get(player_handle).booleanValue();
                                if (can_jump_boolean) {
                                    ITextComponent note = new TextComponentString("<" + iCommandSender.getName() + "> Player \"" + strings[0] + "\" can jump in the game [true]");
                                    player_handle.sendMessage(note);
                                } else {
                                    ITextComponent note = new TextComponentString("<" + iCommandSender.getName() + "> Player \"" + strings[0] + "\" can't jump in the game [false]");
                                    player_handle.sendMessage(note);
                                }
                            } else {
                                throw new CommandException("<" + iCommandSender.getName() + "> You can't execute \"can-jump\" command,reason: Server Error");
                            }
                        }
                    } else {
                        if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                            throw new CommandException("<" + iCommandSender.getName() + "> Not find player \"" + strings[0] + "\"");
                        } else if (strings[0].equals("aisi163") && !iCommandSender.getName().equals("aisi163")) {
                            throw new CommandException("<" + iCommandSender.getName() + "> Admin Server, what are you doing??? you want to prohibit my jump privilege in game???");
                        } else {
                            if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                                throw new CommandException("<" + iCommandSender.getName() + "> Not find player \"" + strings[0] + "\"");
                            } else {
                                EntityPlayer player_handle = PlayerHandleManager.getPlayerHandleByPlayerName(strings[0]);
                                if (player_handle != null) {
                                    if (strings[1].equals("true")) {
                                        PlayerHandleManager.player_can_jumps.replace(player_handle, new Boolean(true));
                                        ;
                                        ITextComponent note = new TextComponentString("<" + iCommandSender.getName() + "> Player \"" + strings[0] + "\" can jump in the game");
                                        iCommandSender.sendMessage(note);
                                        ITextComponent jumped_note = new TextComponentString("<aisi163 to " + strings[0] + "> You can jump in the game");
                                        player_handle.sendMessage(jumped_note);
                                        ITextComponent radio_note = new TextComponentString("[System Info] Player \"" + strings[0] + "\" can jump in the game");
                                        int player_handle_length = PlayerHandleManager.getPlayerHandleListLength();
                                        for (int i = 0; i < player_handle_length; i++) {
                                            EntityPlayer other_player_handle = PlayerHandleManager.getPlayerHandle(i);
                                            if (other_player_handle != null) {
                                                other_player_handle.sendMessage(radio_note);
                                            }
                                        }
                                        player_handle.setJumping(true);
                                    } else if (strings[1].equals("false")) {
                                        PlayerHandleManager.player_can_jumps.replace(player_handle, new Boolean(false));
                                        ITextComponent note = new TextComponentString("<" + iCommandSender.getName() + "> Player \"" + strings[0] + "\" can't jump in the game");
                                        iCommandSender.sendMessage(note);
                                        ITextComponent jumped_note = new TextComponentString("<aisi163 to " + strings[0] + "> You can't jump in the game");
                                        player_handle.sendMessage(jumped_note);
                                        ITextComponent radio_note = new TextComponentString("[System Info] Player \"" + strings[0] + "\" can't jump in the game");
                                        int player_handle_length = PlayerHandleManager.getPlayerHandleListLength();
                                        for (int i = 0; i < player_handle_length; i++) {
                                            EntityPlayer other_player_handle = PlayerHandleManager.getPlayerHandle(i);
                                            if (other_player_handle != null) {
                                                other_player_handle.sendMessage(radio_note);
                                            }
                                        }
                                        player_handle.setJumping(false);
                                    } else {
                                        throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right: /can-jump <player_name> [true|false]");
                                    }
                                } else {
                                    throw new CommandException("<" + iCommandSender.getName() + "> You can't execute \"can-jump\" command,reason: Server Error");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class CanDigBlock extends CommandBase{

    @Override
    public String getName() {
        return "can-dig-block";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.can-dig-block.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> You can't execute \"can-dig-block\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 1 && strings.length != 2) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command Format error,right format: /can-dig-block <player_name> [true|false]");
                } else {
                    if (strings.length == 1) {
                        if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                            throw new CommandException("<" + iCommandSender.getName() + "> Not find player \"" + strings[0] + "\"");
                        } else {
                            EntityPlayer player_handle = PlayerHandleManager.getPlayerHandleByPlayerName(strings[0]);
                            if (player_handle != null) {
                                Boolean can_dig_block = PlayerHandleManager.player_can_dig_block.get(player_handle);
                                if (can_dig_block.booleanValue()) {
                                    iCommandSender.sendMessage(new TextComponentString("<" + iCommandSender.getName() + "> Player \"" + strings[0] + "\" can dig block"));
                                } else {
                                    iCommandSender.sendMessage(new TextComponentString("<" + iCommandSender.getName() + "> Player \"" + strings[0] + "\" can't dig block"));
                                }
                            } else {
                                throw new CommandException("<" + iCommandSender.getName() + "> You can't execute \"can-dig-block\" command, reason: Server Error");
                            }
                        }
                    } else {
                        if (!PlayerHandleManager.IsPlayerInGame(strings[0])) {
                            throw new CommandException("<" + iCommandSender.getName() + "> Not find player \"" + strings[0] + "\"");
                        } else if (!iCommandSender.getName().equals("aisi163") && strings[0].equals("aisi163")) {
                            throw new CommandException("<aisi163 to " + iCommandSender.getName() + "> Server Admin, what are you doing??? you want to prohibit my dig block privilege in the game???");
                        } else {
                            EntityPlayer player_handle = PlayerHandleManager.getPlayerHandleByPlayerName(strings[0]);
                            if (player_handle != null) {
                                if (strings[1].compareTo("true") == 0) {
                                    PlayerHandleManager.player_can_dig_block.replace(player_handle, new Boolean(true));
                                    iCommandSender.sendMessage(new TextComponentString("<" + iCommandSender.getName() + "> Player \"" + strings[0] + "\" can dig block"));
                                    player_handle.sendMessage(new TextComponentString("<aisi163 to " + iCommandSender.getName() + "> You can't dig the block in the game,because the server admin \"" + iCommandSender.getName() + "\" prohibited your dig block privilege !!!"));
                                    ITextComponent radio_note = new TextComponentString("[System Info] every server admins and simple player attention, player \"" + strings[0] + "\" can't dig the block in the game !!!");
                                    for (int i = 0; i < PlayerHandleManager.getPlayerHandleListLength(); i++) {
                                        EntityPlayer other_player_handle = PlayerHandleManager.getPlayerHandle(i);
                                        if (other_player_handle != null) {
                                            other_player_handle.sendMessage(radio_note);
                                        }
                                    }
                                } else if (strings[1].compareTo("false") == 0) {
                                    PlayerHandleManager.player_can_dig_block.replace(player_handle, new Boolean(false));
                                    iCommandSender.sendMessage(new TextComponentString("<" + iCommandSender.getName() + "> Player \"" + strings[0] + "\" can't dig block"));
                                    player_handle.sendMessage(new TextComponentString("<aisi163 to " + iCommandSender.getName() + "> You can dig the block in the game,because the server admin \"" + iCommandSender.getName() + "\" give you dig block privilege !!!"));
                                    ITextComponent radio_note = new TextComponentString("[System Info] every server admins and simple player attention, the player \"" + strings[0] + "\" can dig the block in the game !!!");
                                    for (int i = 0; i < PlayerHandleManager.getPlayerHandleListLength(); i++) {
                                        EntityPlayer other_player_handle = PlayerHandleManager.getPlayerHandle(i);
                                        if (other_player_handle != null) {
                                            other_player_handle.sendMessage(radio_note);
                                        }
                                    }
                                } else {
                                    throw new CommandException("<" + iCommandSender.getName() + "> Command Format Error,right format: /can-dig-block <player_name> [true|false]");
                                }
                            } else {
                                throw new CommandException("<" + iCommandSender.getName() + "> You can't execute \"can-dig-block\" command, reason: Server Error");
                            }
                        }
                    }
                }
            }
        }
    }
}

class AddCantBreakArea extends CommandBase{

    @Override
    public String getName() {
        return "add-cant-break-area";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.add-cant-break-area.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> You can't execute \"add-cant-break-area\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 7) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /add-cant-break-area <x1> <y1> <z1> <x2> <y2> <z2> <cant-break-area>");
                } else {
                    String cant_break_area_name = strings[6];
                    if (CantBreakAreaManager.IsCantBreakAreaNameInCantBreakAreaList(cant_break_area_name)) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Can't break area \"" + cant_break_area_name + "\" have already existed");
                    } else {
                        String associated_X1 = strings[0];
                        String associated_Y1 = strings[1];
                        String associated_Z1 = strings[2];
                        String associated_X2 = strings[3];
                        String associated_Y2 = strings[4];
                        String associated_Z2 = strings[5];
                        if (AddProhibitArea.IsNumberString(associated_X1) && AddProhibitArea.IsNumberString(associated_Y1) && AddProhibitArea.IsNumberString(associated_Z1) &&
                                AddProhibitArea.IsNumberString(associated_X2) && AddProhibitArea.IsNumberString(associated_Y2) && AddProhibitArea.IsNumberString(associated_Z2)) {
                            Integer int_X1 = new Integer(associated_X1);
                            Integer int_Y1 = new Integer(associated_Y1);
                            Integer int_Z1 = new Integer(associated_Z1);
                            Integer int_X2 = new Integer(associated_X2);
                            Integer int_Y2 = new Integer(associated_Y2);
                            Integer int_Z2 = new Integer(associated_Z2);
                            boolean result1 = CantBreakAreaManager.AddCantBreakAreaList(cant_break_area_name);
                            boolean result2 = CantBreakAreaManager.AddCantBreakAreaAssociatedX1(cant_break_area_name, int_X1);
                            boolean result3 = CantBreakAreaManager.AddCantBreakAreaAssociatedY1(cant_break_area_name, int_Y1);
                            boolean result4 = CantBreakAreaManager.AddCantBreakAreaAssociatedZ1(cant_break_area_name, int_Z1);
                            boolean result5 = CantBreakAreaManager.AddCantBreakAreaAssociatedX2(cant_break_area_name, int_X2);
                            boolean result6 = CantBreakAreaManager.AddCantBreakAreaAssociatedY2(cant_break_area_name, int_Y2);
                            boolean result7 = CantBreakAreaManager.AddCantBreakAreaAssociatedZ2(cant_break_area_name, int_Z2);
                            if (result1 && result2 && result3 && result4 && result5 && result6 && result7) {
                                iCommandSender.sendMessage(new TextComponentString("<" + iCommandSender.getName() + "> You create a new can't break area named \"" + cant_break_area_name + "\" Pos: [From (X=" + int_X1 + ",Y=" + int_Y1 + ",Z=" + int_Z1 + ") To (X=" + int_X2 + ",Y=" + int_Y2 + ",Z=" + int_Z2 + ")]"));
                                ITextComponent radio_note = new TextComponentString("[System Info] every server admin and simple player attention,the server admin \"" + iCommandSender.getName() + "\" create a new can't break area named \"" + cant_break_area_name + "\" Pos: [From (X=" + int_X1 + ",Y=" + int_Y1 + ",Z=" + int_Z1 + ") To (X=" + int_X2 + ",Y=" + int_Y2 + ",Z=" + int_Z2 + ")], simple player can't break all of the blocks in this area");
                                for (int i = 0; i < PlayerHandleManager.getPlayerHandleListLength(); i++) {
                                    EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                                    if (player_handle != null) {
                                        player_handle.sendMessage(radio_note);
                                    }
                                }
                            } else {
                                throw new CommandException("<" + iCommandSender.getName() + "> You can't execute \"add-cant-break-area\" command,reason: Server Error");
                            }
                        } else {
                            throw new CommandException("<" + iCommandSender.getName() + "> Command format error, reason: <x1> <y1> <z1> <x2> <y2> <z2> must be an integer");
                        }
                    }
                }
            }
        }
    }
}

class ListCantBreakArea extends CommandBase {

    @Override
    public String getName() {
        return "list-cant-break-area";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.list-cant-break-area.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> You can't execute \"list-cant-break-area\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 0 && strings.length != 1) {
                    throw new CommandException("<" + iCommandSender.getName() + "> Command format error,right format: /list-cant-break-area [page]");
                } else {
                    if (strings.length == 0) {
                        iCommandSender.sendMessage(new TextComponentString("Can't Break Area List (1/" + ((int) (CantBreakAreaManager.GetCantBreakAreaLength() / 5) + 1) + ")"));
                        ITextComponent split_line = new TextComponentString("=================================================");
                        iCommandSender.sendMessage(split_line);
                        for (int i = 0; i < 5; i++) {
                            if (i < CantBreakAreaManager.GetCantBreakAreaLength()) {
                                ITextComponent line = new TextComponentString(CantBreakAreaManager.GetCantBreakAreaName(i) + " "
                                        + "Pos: [From (X=" + CantBreakAreaManager.GetCantBreakAreaAssociatedX1(CantBreakAreaManager.GetCantBreakAreaName(i)) +
                                        ",Y=" + CantBreakAreaManager.GetCantBreakAreaAssociatedY1(CantBreakAreaManager.GetCantBreakAreaName(i)) +
                                        ",Z=" + CantBreakAreaManager.GetCantBreakAreaAssociatedZ1(CantBreakAreaManager.GetCantBreakAreaName(i)) +
                                        ") To (X=" + CantBreakAreaManager.GetCantBreakAreaAssociatedX2(CantBreakAreaManager.GetCantBreakAreaName(i)) +
                                        ",Y=" + CantBreakAreaManager.GetCantBreakAreaAssociatedY2(CantBreakAreaManager.GetCantBreakAreaName(i)) +
                                        ",Z=" + CantBreakAreaManager.GetCantBreakAreaAssociatedZ2(CantBreakAreaManager.GetCantBreakAreaName(i)) + ")]");
                                iCommandSender.sendMessage(line);
                            } else {
                                break;
                            }
                        }
                        if (CantBreakAreaManager.GetCantBreakAreaLength() == 0) {
                            iCommandSender.sendMessage(new TextComponentString("(None)"));
                        }
                        iCommandSender.sendMessage(split_line);
                        iCommandSender.sendMessage(new TextComponentString(""));
                    } else {
                        String page = strings[0];
                        if (!AddProhibitArea.IsNumberString(page)) {
                            throw new CommandException("<" + iCommandSender.getName() + "> Command format error,reason: [page] must be an integer");
                        } else {
                            Integer page_number = new Integer(page);
                            if (1 <= page_number && page_number <= (int) ((CantBreakAreaManager.GetCantBreakAreaLength() / 5) + 1)) {
                                iCommandSender.sendMessage(new TextComponentString("Can't Break Area List (1/" + ((int) (CantBreakAreaManager.GetCantBreakAreaLength() / 5) + 1) + ")"));
                                ITextComponent split_line = new TextComponentString("=================================================");
                                iCommandSender.sendMessage(split_line);
                                for (int i = (page_number - 1) * 5; i < page_number * 5; i++) {
                                    if (i < CantBreakAreaManager.GetCantBreakAreaLength()) {
                                        ITextComponent line = new TextComponentString(CantBreakAreaManager.GetCantBreakAreaName(i) + " "
                                                + "Pos: [From (X=" + CantBreakAreaManager.GetCantBreakAreaAssociatedX1(CantBreakAreaManager.GetCantBreakAreaName(i)) +
                                                ",Y=" + CantBreakAreaManager.GetCantBreakAreaAssociatedY1(CantBreakAreaManager.GetCantBreakAreaName(i)) +
                                                ",Z=" + CantBreakAreaManager.GetCantBreakAreaAssociatedZ1(CantBreakAreaManager.GetCantBreakAreaName(i)) +
                                                ") To (X=" + CantBreakAreaManager.GetCantBreakAreaAssociatedX2(CantBreakAreaManager.GetCantBreakAreaName(i)) +
                                                ",Y=" + CantBreakAreaManager.GetCantBreakAreaAssociatedY2(CantBreakAreaManager.GetCantBreakAreaName(i)) +
                                                ",Z=" + CantBreakAreaManager.GetCantBreakAreaAssociatedZ2(CantBreakAreaManager.GetCantBreakAreaName(i)) + ")]");
                                        iCommandSender.sendMessage(line);
                                    } else {
                                        break;
                                    }
                                }
                                if (CantBreakAreaManager.GetCantBreakAreaLength() == 0) {
                                    iCommandSender.sendMessage(new TextComponentString("(None)"));
                                }
                                iCommandSender.sendMessage(split_line);
                                iCommandSender.sendMessage(new TextComponentString(""));
                            } else {
                                throw new CommandException("<" + iCommandSender.getName() + "> Command format error,reason: page range must in 1-" + (int) ((CantBreakAreaManager.GetCantBreakAreaLength() / 5) + 1));
                            }
                        }
                    }
                }
            }
        }
    }
}

class RemoveCantBreakArea extends CommandBase {

    @Override
    public String getName() {
        return "remove-cant-break-area";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "command.remove-cant-break-area.usage";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender.getCommandSenderEntity() != null) {
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) iCommandSender.getCommandSenderEntity())) {
                throw new CommandException("<" + iCommandSender.getName() + "> You can't execute \"remove-cant-break-area\" command,reason: you haven't admin privilege");
            } else {
                if (strings.length != 1) {
                    throw new CommandException("Command format error,right format: /remove-cant-break-area <cant-break-area>");
                } else {
                    if (!CantBreakAreaManager.IsCantBreakAreaNameInCantBreakAreaList(strings[0])) {
                        throw new CommandException("<" + iCommandSender.getName() + "> Not find can't break area \"" + strings[0] + "\"");
                    } else {
                        Integer X1 = CantBreakAreaManager.GetCantBreakAreaAssociatedX1(strings[0]);
                        Integer Y1 = CantBreakAreaManager.GetCantBreakAreaAssociatedY1(strings[0]);
                        Integer Z1 = CantBreakAreaManager.GetCantBreakAreaAssociatedZ1(strings[0]);
                        Integer X2 = CantBreakAreaManager.GetCantBreakAreaAssociatedX2(strings[0]);
                        Integer Y2 = CantBreakAreaManager.GetCantBreakAreaAssociatedY2(strings[0]);
                        Integer Z2 = CantBreakAreaManager.GetCantBreakAreaAssociatedZ2(strings[0]);
                        boolean result1 = CantBreakAreaManager.RemoveCantBreakAreaAssociatedX1(strings[0]);
                        boolean result2 = CantBreakAreaManager.RemoveCantBreakAreaAssociatedY1(strings[0]);
                        boolean result3 = CantBreakAreaManager.RemoveCantBreakAreaAssociatedZ1(strings[0]);
                        boolean result4 = CantBreakAreaManager.RemoveCantBreakAreaAssociatedX2(strings[0]);
                        boolean result5 = CantBreakAreaManager.RemoveCantBreakAreaAssociatedY2(strings[0]);
                        boolean result6 = CantBreakAreaManager.RemoveCantBreakAreaAssociatedZ2(strings[0]);
                        boolean result7 = CantBreakAreaManager.RemoveCantBreakAreaList(strings[0]);
                        if (X1 != null && Y1 != null && Z1 != null && X2 != null && Y2 != null && Z2 != null && result1 && result2 && result3 && result4 && result5 && result6 && result7) {
                            iCommandSender.sendMessage(new TextComponentString("<" + iCommandSender.getName() + "> You removed a can't break area named \"" + strings[0] + "\""));
                            ITextComponent radio_note = new TextComponentString("[System Info] every server admin and simple player attention, server admin \"" + strings[0] + "\" remove a can't break area named \"" + strings[0] + "\" Pos: [From (X=" + X1 + ",Y=" + Y1 + ",Z=" + Z1 + ") To (X=" + X2 + ",Y=" + Y1 + ",Z=" + Z1 + ")]");
                            for (int i = 0; i < PlayerHandleManager.getPlayerHandleListLength(); i++) {
                                EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                                if (player_handle != null) {
                                    player_handle.sendMessage(radio_note);
                                }
                            }
                        } else {
                            throw new CommandException("<" + iCommandSender.getName() + "> You can't execute \"add-cant-break-area\" command,reason: Server Error");
                        }
                    }
                }
            }
        }
    }
}

public class AdminCommand {
    public static final LoginAdmin login_admin = new LoginAdmin();
    public static final LogoutAdmin logout_admin = new LogoutAdmin();
    public static final Admin admin = new Admin();
    public static final BanAdmin ban_admin = new BanAdmin();
    public static final Privilege privilege = new Privilege();
    public static final Ping ping = new Ping();
    public static final ListAll list_all = new ListAll();
    public static final ListPlayer list_player =new ListPlayer();
    public static final ListAdmin list_admin = new ListAdmin();
    public static final GPS gps = new GPS();
    public static final FixedPlayer fixed_player = new FixedPlayer();
    public static final UnFixedPlayer unfixed_player= new UnFixedPlayer();
    public static final ListFixedPlayer list_fixed_player =  new ListFixedPlayer();
    public static final ListUnFixedPlayer list_unFixed_player = new ListUnFixedPlayer();
    public static final AddProhibitArea add_prohibit_area = new AddProhibitArea();
    public static final RemoveProhibitArea remove_prohibit_area = new RemoveProhibitArea();
    public static final ListProhibitArea list_prohibit_area = new ListProhibitArea();
    public static final GetSpeed get_speed = new GetSpeed();
    public static final OriginalSpeed original_speed = new OriginalSpeed();
    public static final SetSpeed set_speed = new SetSpeed();
    public static final ModeA mode_a = new ModeA();
    public static final ModeC mode_c = new ModeC();
    public static final ModeSU mode_su = new ModeSU();
    public static final ModeSP mode_sp = new ModeSP();
    public static final Mode mode = new Mode();
    public static final CanJump can_jump = new CanJump();
    public static final CanDigBlock can_dig_block = new CanDigBlock();
    public static final AddCantBreakArea add_cant_break_area = new AddCantBreakArea();
    public static final RemoveCantBreakArea remove_cant_break_area = new RemoveCantBreakArea();
    public static final ListCantBreakArea list_cant_break_area = new ListCantBreakArea();
}
