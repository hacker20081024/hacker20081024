/*
Copyright 2024 aisi163

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the “Software”), to deal in the
Software without restriction, including without limitation the rights to use, copy,
modify, merge, publish, distribute, sublicense, and/or sell copies of the
Software,and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
*/

package github.hacker20081024.admintool;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

@EventBusSubscriber
public class ListenPlayerEvent {
    // Class: ListenPlayerEvent
    // Version: 3.1

    public static final List<String> illegal_item_name = new ArrayList<String>();
    public static final List<String> illegal_use_item = new ArrayList<String>();
    public static final List<String> illegal_use_throw_item = new ArrayList<String>();
    public static final Character[] illegal_player_name_character = {'~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '+', '=', '{', '}', '[', ']', ':', ';', '\'', '\"', '|', '\\', '<', '>', ',', '.', '?', '/'};

    static {
        illegal_item_name.add(new String("item.minecraftCommandBlock"));
        illegal_item_name.add(new String("tile.commandBlock"));
        illegal_item_name.add(new String("tile.barrier"));
        illegal_item_name.add(new String("tile.structureBlock"));
        illegal_item_name.add(new String("tile.structureVoid"));
        illegal_item_name.add(new String("tile.bedrock"));
    }

    static {
        illegal_use_item.add(new String("item.minecartTnt"));
        illegal_use_item.add(new String("item.minecartHopper"));
        illegal_use_item.add(new String("item.minecarfChest"));
        illegal_use_item.add(new String("tile.tnt"));
        illegal_use_item.add(new String("item.end_crystal"));
    }

    static {
        illegal_use_throw_item.add(new String("item.fireball"));
    }

    public static String encipherPlayerIpAddress(String player_ip_address) {
        // This code never run in minecraft client
        String encipher_player_ip_address = "";
        for (int i = 0; i < player_ip_address.length(); i++) {
            if (i <= 1) {
                encipher_player_ip_address += player_ip_address.charAt(i);
            } else {
                encipher_player_ip_address += "*";
            }
        }
        ;
        return encipher_player_ip_address;
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void onPlayerLoggedIn(PlayerLoggedInEvent event) throws IOException {
        // This code never run in minecraft client
        EntityPlayerMP client_player_handle = (EntityPlayerMP) event.player;
        client_player_handle.getEntityData().setInteger("Message_Send_Timer", 10000);
        client_player_handle.getEntityData().setInteger("In_The_Prohibited_Message_Send_Timer", 350);
        client_player_handle.getEntityData().setInteger("Admin_Radio_In_The_Prohibited_Message_Send_Timer", 350);
        String player_name = client_player_handle.getName();
        String player_uuid = client_player_handle.getUniqueID().toString();
        String player_ipaddress = client_player_handle.getPlayerIP();
        if (BanPlayerManager.ban_player_list.contains(player_name)) {
            if (BanPlayerManager.ban_player_reason.containsKey(player_name)) {
                Style ban_reason_style = new Style();
                ban_reason_style.setColor(TextFormatting.RED);
                ban_reason_style.setBold(true);
                ITextComponent ban_reason = new TextComponentString("You can't join the game,the server admin banned you. Reason: " + BanPlayerManager.ban_player_reason.get(player_name));
                ban_reason.setStyle(ban_reason_style);
                client_player_handle.connection.disconnect(ban_reason);
            } else {
                Style ban_reason_style = new Style();
                ban_reason_style.setColor(TextFormatting.RED);
                ban_reason_style.setBold(true);
                ITextComponent ban_reason = new TextComponentString("You can't join the game,the server admin banned you. Reason: (Don't Know)");
                ban_reason.setStyle(ban_reason_style);
                client_player_handle.connection.disconnect(ban_reason);
            }
            return;
        }
        if (BanPlayerManager.ban_ip_list.contains(player_ipaddress)) {
            if (BanPlayerManager.ban_ip_reason.containsKey(player_ipaddress)) {
                Style ban_ip_reason_style = new Style();
                ban_ip_reason_style.setColor(TextFormatting.RED);
                ban_ip_reason_style.setBold(true);
                ITextComponent ban_ip_reason = new TextComponentString("You can't join the game,the server admin banned your IP [" + player_ipaddress + "]. Reason: " + BanPlayerManager.ban_ip_reason.get(player_ipaddress));
                ban_ip_reason.setStyle(ban_ip_reason_style);
                client_player_handle.connection.disconnect(ban_ip_reason);
            } else {
                Style ban_ip_reason_style = new Style();
                ban_ip_reason_style.setColor(TextFormatting.RED);
                ban_ip_reason_style.setBold(true);
                ITextComponent ban_ip_reason = new TextComponentString("You can't join the game,the server admin banned your IP [" + player_ipaddress + "]. Reason: (Don't Know)");
                ban_ip_reason.setStyle(ban_ip_reason_style);
                client_player_handle.connection.disconnect(ban_ip_reason);
            }
            return;
        }
        for (Character illegal_character : illegal_player_name_character) {
            if (player_name.indexOf(illegal_character) != -1) {
                Style cant_join_reason_note_style = new Style();
                cant_join_reason_note_style.setColor(TextFormatting.RED);
                cant_join_reason_note_style.setBold(true);
                ITextComponent cant_join_reason_note = new TextComponentString("You can't join the game,reason: your player name \"" + player_name + "\" exists illegal character");
                cant_join_reason_note.setStyle(cant_join_reason_note_style);
                client_player_handle.connection.disconnect(cant_join_reason_note);
                return;
            }
        }
        PlayerIPManager.addPlayerIP(player_name, player_ipaddress);
        PlayerHandleManager.addPlayerHandle(event.player);
        String encipher_player_ip_address = encipherPlayerIpAddress(player_ipaddress);
        try {
            PlayerJoinGameLogManager.WriteLog(player_name, player_ipaddress, player_uuid);
        } catch (IOException e){
            System.out.println("System Error");
        }
        boolean is_player_admin = AdminPlayerManager.IsPlayerInAdminPlayers(event.player);
        Style welcome_note1_style = new Style();
        welcome_note1_style.setBold(true);
        welcome_note1_style.setColor(TextFormatting.GREEN);
        ITextComponent welcome_note1 = new TextComponentString("Welcome player \"" + player_name + "\" join the aisi163 Potato Server");
        welcome_note1.setStyle(welcome_note1_style);
        Style welcome_note2_style = new Style();
        welcome_note2_style.setBold(true);
        welcome_note2_style.setColor(TextFormatting.BLUE);
        ITextComponent welcome_note2 = new TextComponentString("[About Your Info]");
        welcome_note2.setStyle(welcome_note2_style);
        Style player_info_note_style = new Style();
        player_info_note_style.setColor(TextFormatting.BLUE);
        ITextComponent welcome_note3 = new TextComponentString("Your session unique identification: " + player_uuid);
        welcome_note3.setStyle(welcome_note2_style);
        ITextComponent welcome_note4 = new TextComponentString("Your IP address: " + encipher_player_ip_address);
        welcome_note4.setStyle(welcome_note2_style);
        ITextComponent welcome_note5 = new TextComponentString("Your privilege: " + (is_player_admin ? "Server Admin" : "Simple Player"));
        welcome_note5.setStyle(welcome_note2_style);
        ITextComponent welcome_note6 = new TextComponentString("[Other Info]");
        Style about_server_info = new Style();
        about_server_info.setBold(true);
        about_server_info.setColor(TextFormatting.BLUE);
        welcome_note6.setStyle(about_server_info);
        ITextComponent welcome_note7 = new TextComponentString("Player QQ group: 964817020");
        welcome_note7.setStyle(welcome_note2_style);
        ITextComponent welcome_note8 = new TextComponentString("History Mod Download Link: https://github.com/hacker20081024/hacker20081024/tree/main/minecraft/mod/server_mod");
        welcome_note8.setStyle(welcome_note2_style);
        ITextComponent welcome_note9 = new TextComponentString("Now Using Mod Download Link: https://github.com/hacker20081024/hacker20081024/tree/main/minecraft/mod/server_mod/now_use");
        welcome_note9.setStyle(welcome_note2_style);
        ITextComponent welcome_note10 = new TextComponentString("Server Copyright: (c) 2024, Mod Copyright: __MIT__");
        welcome_note10.setStyle(welcome_note2_style);
        client_player_handle.sendMessage(welcome_note1);
        client_player_handle.sendMessage(welcome_note2);
        client_player_handle.sendMessage(welcome_note3);
        client_player_handle.sendMessage(welcome_note4);
        client_player_handle.sendMessage(welcome_note5);
        client_player_handle.sendMessage(welcome_note6);
        client_player_handle.sendMessage(welcome_note7);
        client_player_handle.sendMessage(welcome_note8);
        client_player_handle.sendMessage(welcome_note9);
        client_player_handle.sendMessage(welcome_note10);
        if (is_player_admin) {
            ITextComponent admin_info1 = new TextComponentString("[About Admin Info]");
            ITextComponent admin_info2 = new TextComponentString("Admin unique identification: " + client_player_handle.getUniqueID());
            ITextComponent admin_info3 = new TextComponentString("Admin register time: " + AdminPlayerManager.GetAdminPlayerRegisterTime(client_player_handle));
            ITextComponent admin_info4 = new TextComponentString("Admin Privilege Issuer: " + AdminPlayerManager.GetAdminPlayerAdminPrivilegeIssuer(client_player_handle));
            client_player_handle.sendMessage(admin_info1);
            client_player_handle.sendMessage(admin_info2);
            client_player_handle.sendMessage(admin_info3);
            client_player_handle.sendMessage(admin_info4);
            // If player is admin, set the game mode of admin to CREATIVE
            client_player_handle.setGameType(GameType.CREATIVE);
        } else {
            // If player is a simple player, set the game of the simple player to SURVIVAL
            client_player_handle.setGameType(GameType.SURVIVAL);
        }
        int player_handle_list_length = PlayerHandleManager.getPlayerHandleListLength();
        for (int i = 0; i < player_handle_list_length; i++) {
            EntityPlayer receive_message_player = PlayerHandleManager.getPlayerHandle(i);
            if (receive_message_player != null) {
                receive_message_player.sendMessage(new TextComponentString(""));
                if (AdminPlayerManager.IsPlayerInAdminPlayers(receive_message_player)) {
                    Style join_style = new Style();
                    join_style.setBold(true);
                    join_style.setColor(TextFormatting.YELLOW);
                    ITextComponent admin_user_note = new TextComponentString("Player \"" + player_name + "\" join the game");
                    admin_user_note.setStyle(join_style);
                    Style about_player_style = new Style();
                    about_player_style.setBold(true);
                    about_player_style.setColor(TextFormatting.BLUE);
                    ITextComponent admin_user_note1 = new TextComponentString("[About Player]");
                    admin_user_note1.setStyle(about_player_style);
                    Style information_style = new Style();
                    information_style.setColor(TextFormatting.BLUE);
                    ITextComponent admin_user_note2 = new TextComponentString("Player session unique identification: " + player_uuid);
                    admin_user_note2.setStyle(information_style);
                    ITextComponent admin_user_note3 = new TextComponentString("Player IP: " + player_ipaddress);
                    admin_user_note3.setStyle(information_style);
                    ITextComponent admin_user_note4 = new TextComponentString("Player Pos: [x=" + client_player_handle.getPosition().getX() + ",y=" + client_player_handle.getPosition().getY() + ",z=" + client_player_handle.getPosition().getZ() + "]");
                    admin_user_note4.setStyle(information_style);
                    ITextComponent admin_user_note5 = new TextComponentString("Player World: " + client_player_handle.getEntityWorld().getWorldType().getName());
                    admin_user_note5.setStyle(information_style);
                    receive_message_player.sendMessage(admin_user_note);
                    receive_message_player.sendMessage(admin_user_note1);
                    receive_message_player.sendMessage(admin_user_note2);
                    receive_message_player.sendMessage(admin_user_note3);
                    receive_message_player.sendMessage(admin_user_note4);
                    receive_message_player.sendMessage(admin_user_note5);
                } else {
                    ITextComponent simple_user_note = new TextComponentString("Player \"" + player_name + "\" join the game, (he/she) is: " + (is_player_admin ? "[Server Admin]" : "[Simple Player]"));
                    Style join_style = new Style();
                    join_style.setBold(true);
                    join_style.setColor(TextFormatting.YELLOW);
                    simple_user_note.setStyle(join_style);
                    receive_message_player.sendMessage(simple_user_note);
                }
            } else {
                // ...
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void onPlayerLogout(PlayerLoggedOutEvent event) {
        // This code never run in minecraft client
        EntityPlayer player = event.player;
        try {
            PlayerLeftGameLogManager.WriteLogin(player.getName(), ((EntityPlayerMP) player).getPlayerIP(), player.getUniqueID().toString());
        } catch (IOException e){
            System.out.println("System Error");
        }
        String player_name = player.getName();
        if (AdminPlayerManager.IsPlayerInAdminPlayers(player)) {
            AdminPlayerManager.DeleteAdminRegisterTime(player);
            AdminPlayerManager.DeleteAdminPlayerAdminPrivilegeIssuer(player);
            AdminPlayerManager.RemoveAdminPlayerFromAdminPlayers(player);
            LoginAdminUsernameAndPasswordManager.player_login_count.remove(player_name);
        }
        PlayerHandleManager.removePlayerHandle(player);
        int player_handle_length = PlayerHandleManager.getPlayerHandleListLength();
        for (int i = 0; i < player_handle_length; i++) {
            EntityPlayer receive_message_player = PlayerHandleManager.getPlayerHandle(i);
            if (receive_message_player != null) {
                receive_message_player.sendMessage(new TextComponentString(""));
                Style left_note_style = new Style();
                left_note_style.setColor(TextFormatting.YELLOW);
                ITextComponent left_note = new TextComponentString("Player \"" + player_name + "\" left the game, goodbye.");
                left_note.setStyle(left_note_style);
                receive_message_player.sendMessage(left_note);
            } else {
                // ...
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void ListenPlayerJumpEvent(LivingUpdateEvent event) {
        // This code never run in minecraft client
        Entity living_entity = event.getEntity();
        if (PlayerHandleManager.IsPlayerInGame(living_entity.getName())) {
            if (living_entity instanceof EntityPlayer) {
                EntityPlayer player_handle = (EntityPlayer) living_entity;
                boolean can_jump_boolean = PlayerHandleManager.player_can_jumps.get(player_handle).booleanValue();
                if (!can_jump_boolean) {
                    double motion_Y = player_handle.motionY;
                    if (motion_Y > 0) { // Player_jump
                        player_handle.setPositionAndUpdate(player_handle.getPosition().getY(), PlayerHandleManager.player_Y_Pos.get(player_handle).intValue(), player_handle.getPosition().getZ());
                        Style note_style = new Style();
                        note_style.setColor(TextFormatting.RED);
                        ITextComponent note = new TextComponentString("<" + player_handle.getName() + "> You can't jump");
                        note.setStyle(note_style);
                        player_handle.sendMessage(note);
                    } else {
                        PlayerHandleManager.player_Y_Pos.replace(player_handle, new Integer(player_handle.getPosition().getY()));
                    }
                } else {
                    PlayerHandleManager.player_Y_Pos.replace(player_handle, new Integer(player_handle.getPosition().getY()));
                }
            }
        }
    }

    ;

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void ListenFixedPlayerMove(LivingUpdateEvent event) {
        // This code never run in minecraft client
        Entity living_entity = event.getEntity();
        if (PlayerHandleManager.IsPlayerInGame(living_entity.getName())) {
            if (living_entity instanceof EntityPlayer) {
                // Player move in game
                EntityPlayer player_for_move = (EntityPlayer) living_entity;
                String player_name = player_for_move.getName();
                if (FixedPlayerListManager.IsPlayerInFixedPlayerList(player_name)) {
                    if (player_for_move.getEntityData().getInteger("Message_Send_Timer") >= 350) {
                        player_for_move.getEntityData().setInteger("Message_Send_Timer", 0);
                        Style note_style = new Style();
                        note_style.setColor(TextFormatting.RED);
                        ITextComponent note = new TextComponentString("<aisi163 to " + player_name + "> You can't move,because the server admin \"" + FixedPlayerListManager.GetWhoFixedPlayerByFixedPlayerName(player_name) + "\" prohibited your move privilege in game");
                        note.setStyle(note_style);
                        player_for_move.sendMessage(note);
                    } else {
                        player_for_move.getEntityData().setInteger("Message_Send_Timer", player_for_move.getEntityData().getInteger("Message_Send_Timer") + 1);
                    }
                    player_for_move.setPositionAndUpdate(player_for_move.getPosition().getX(), player_for_move.getPosition().getY(), player_for_move.getPosition().getZ());
                } else {
                    if (PlayerHandleManager.player_AI_Move_Speed.get(player_for_move).floatValue() == 0.0F) {
                        PotionEffect speed_effect = new PotionEffect(MobEffects.SPEED, 30, 0, false, true);
                        player_for_move.addPotionEffect(speed_effect);
                    } else if (PlayerHandleManager.player_AI_Move_Speed.get(player_for_move).floatValue() == 0.1F) {
                        PotionEffect speed_effect = new PotionEffect(MobEffects.SPEED, 30, 1, false, false);
                        player_for_move.addPotionEffect(speed_effect);
                    } else if (PlayerHandleManager.player_AI_Move_Speed.get(player_for_move).floatValue() == 0.2F) {
                        PotionEffect speed_effect = new PotionEffect(MobEffects.SPEED, 30, 2, false, false);
                        player_for_move.addPotionEffect(speed_effect);
                    } else if (PlayerHandleManager.player_AI_Move_Speed.get(player_for_move).floatValue() == 0.3F) {
                        PotionEffect speed_effect = new PotionEffect(MobEffects.SPEED, 30, 3, false, false);
                        player_for_move.addPotionEffect(speed_effect);
                    } else if (PlayerHandleManager.player_AI_Move_Speed.get(player_for_move).floatValue() == 0.4F) {
                        PotionEffect speed_effect = new PotionEffect(MobEffects.SPEED, 30, 4, false, false);
                        player_for_move.addPotionEffect(speed_effect);
                    } else if (PlayerHandleManager.player_AI_Move_Speed.get(player_for_move).floatValue() == 0.5F) {
                        PotionEffect speed_effect = new PotionEffect(MobEffects.SPEED, 30, 5, false, false);
                        player_for_move.addPotionEffect(speed_effect);
                    } else if (PlayerHandleManager.player_AI_Move_Speed.get(player_for_move).floatValue() == 0.6F) {
                        PotionEffect speed_effect = new PotionEffect(MobEffects.SPEED, 30, 6, false, true);
                        player_for_move.addPotionEffect(speed_effect);
                    } else if (PlayerHandleManager.player_AI_Move_Speed.get(player_for_move).floatValue() == 0.7F) {
                        PotionEffect speed_effect = new PotionEffect(MobEffects.SPEED, 30, 7, false, true);
                        player_for_move.addPotionEffect(speed_effect);
                    } else if (PlayerHandleManager.player_AI_Move_Speed.get(player_for_move).floatValue() == 0.8F) {
                        PotionEffect speed_effect = new PotionEffect(MobEffects.SPEED, 30, 8, false, true);
                        player_for_move.addPotionEffect(speed_effect);
                    } else if (PlayerHandleManager.player_AI_Move_Speed.get(player_for_move).floatValue() == 0.9F) {
                        PotionEffect speed_effect = new PotionEffect(MobEffects.SPEED, 30, 9, false, true);
                        player_for_move.addPotionEffect(speed_effect);
                    } else if (PlayerHandleManager.player_AI_Move_Speed.get(player_for_move).floatValue() == 1.0F) {
                        PotionEffect speed_effect = new PotionEffect(MobEffects.SPEED, 30, 10, false, true);
                        player_for_move.addPotionEffect(speed_effect);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void ListenSimplePlayerMoveToProhibitedArea(LivingUpdateEvent event) {
        // This code never run in minecraft client
        Entity living_entity = event.getEntity();
        if (PlayerHandleManager.IsPlayerInGame(living_entity.getName())) {
            if (living_entity instanceof EntityPlayer) {
                // Player move in game
                EntityPlayer player_handle = (EntityPlayer) living_entity;
                BlockPos player_position = player_handle.getPosition();
                int player_position_X = player_position.getX();
                int player_position_Y = player_position.getY();
                int player_position_Z = player_position.getZ();
                boolean result = ProhibitAreaManager.IsPlayerInProhibitArea(player_position_X, player_position_Y, player_position_Z);
                if (result && !AdminPlayerManager.IsPlayerInAdminPlayers(player_handle)) {
                    player_handle.setGameType(GameType.ADVENTURE);
                    DamageSource damage_source = new DamageSource("Server");
                    player_handle.attackEntityFrom(damage_source, 1);
                    player_handle.setHealth(10);
                    Style in_prohibit_area_player_note_style = new Style();
                    in_prohibit_area_player_note_style.setColor(TextFormatting.RED);
                    in_prohibit_area_player_note_style.setBold(true);
                    in_prohibit_area_player_note_style.setUnderlined(true);
                    ITextComponent in_prohibit_area_player_note = new TextComponentString("<aisi163 To " + player_handle.getName() + "> Player \"" + player_handle.getName() + "\" what are you doing? here is prohibited area,except server admin,other players can't in here,far away from this area now!!!");
                    in_prohibit_area_player_note.setStyle(in_prohibit_area_player_note_style);
                    if (player_handle.getEntityData().getInteger("In_The_Prohibited_Message_Send_Timer") >= 350) {
                        player_handle.getEntityData().setInteger("In_The_Prohibited_Message_Send_Timer", 0);
                        player_handle.sendMessage(in_prohibit_area_player_note);
                    } else {
                        player_handle.getEntityData().setInteger("In_The_Prohibited_Message_Send_Timer", player_handle.getEntityData().getInteger("In_The_Prohibited_Message_Send_Timer") + 1);
                    }
                    Style admin_note_style = new Style();
                    admin_note_style.setColor(TextFormatting.RED);
                    admin_note_style.setBold(true);
                    admin_note_style.setUnderlined(true);
                    ITextComponent admin_note = new TextComponentString("<aisi163 to all of the server admins>: Every server admins attention!!! player \"" + player_handle.getName() + "\" to the prohibit area, [Player Position: (X=" + player_position_X + ",Y=" + player_position_Y + ",Z=" + player_position_Z + ")]");
                    admin_note.setStyle(admin_note_style);
                    int player_handle_length = PlayerHandleManager.getPlayerHandleListLength();
                    for (int i = 0; i < player_handle_length; i++) {
                        EntityPlayer other_player_handle = PlayerHandleManager.getPlayerHandle(i);
                        if (other_player_handle != null && AdminPlayerManager.IsPlayerInAdminPlayers(other_player_handle)) {
                            if (other_player_handle.getEntityData().getInteger("Admin_Radio_In_The_Prohibited_Message_Send_Timer") >= 350) {
                                other_player_handle.getEntityData().setInteger("Admin_Radio_In_The_Prohibited_Message_Send_Timer", 0);
                                other_player_handle.sendMessage(admin_note);
                            } else {
                                other_player_handle.getEntityData().setInteger("Admin_Radio_In_The_Prohibited_Message_Send_Timer", other_player_handle.getEntityData().getInteger("Admin_Radio_In_The_Prohibited_Message_Send_Timer") + 1);
                            }
                        }
                    }
                    player_handle.jump();
                }
            }
        }
    }

    public static boolean IsSimplePlayerPlaceBlockInIllegalBlock(String item_name) {
        // This code never run in minecraft client
        for (int i = 0; i < illegal_item_name.size(); i++) {
            if (illegal_item_name.get(i).compareTo(item_name) == 0) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void ListenPlayerInterfaceEvent(PlayerInteractEvent.RightClickBlock player_interface_event) {
        // This code never run in minecraft client
        // 当玩家放置方块的时候,获取玩家主手上面的物品
        ItemStack held_on_main_hand_item = player_interface_event.getEntityPlayer().getHeldItemMainhand();
        // 获取玩家主手上的物品的初始化名称
        String item_default_name = held_on_main_hand_item.getItem().getUnlocalizedName();
        if (item_default_name.compareTo("tile.tnt") == 0) {
            // 如果玩家要通过TNT物品放置TNT方块
            Style player_put_tnt_style = new Style();
            player_put_tnt_style.setColor(TextFormatting.RED);
            player_put_tnt_style.setBold(true);
            TextComponentString player_put_tnt_refuse_note = new TextComponentString("<aisi163 to " + player_interface_event.getEntityPlayer().getName() + "> You can't place TNT");
            player_put_tnt_refuse_note.setStyle(player_put_tnt_style);
            player_interface_event.getEntityPlayer().sendMessage(player_put_tnt_refuse_note);
            player_interface_event.setCanceled(true);
        }
        if (IsSimplePlayerPlaceBlockInIllegalBlock(item_default_name) && !AdminPlayerManager.IsPlayerInAdminPlayers(player_interface_event.getEntityPlayer())) {
            DamageSource damage_source = new DamageSource("server");
            player_interface_event.getEntityPlayer().attackEntityFrom(damage_source, 1.0F);
            World player_associated_world_handle = player_interface_event.getWorld();
            BlockPos player_position = player_interface_event.getEntityPlayer().getPosition();
            player_associated_world_handle.setBlockState(new BlockPos(player_interface_event.getPos().getX() + 1, player_interface_event.getPos().getY(), player_interface_event.getPos().getZ()), Blocks.BEDROCK.getDefaultState());
            player_associated_world_handle.setBlockState(new BlockPos(player_interface_event.getPos().getX() - 1, player_interface_event.getPos().getY(), player_interface_event.getPos().getZ()), Blocks.BEDROCK.getDefaultState());
            player_associated_world_handle.setBlockState(new BlockPos(player_interface_event.getPos().getX(), player_interface_event.getPos().getY(), player_interface_event.getPos().getZ() + 1), Blocks.BEDROCK.getDefaultState());
            player_associated_world_handle.setBlockState(new BlockPos(player_interface_event.getPos().getX(), player_interface_event.getPos().getY(), player_interface_event.getPos().getZ() - 1), Blocks.BEDROCK.getDefaultState());
            player_associated_world_handle.setBlockState(new BlockPos(player_interface_event.getPos().getX(), player_interface_event.getPos().getY() + 1, player_interface_event.getPos().getZ()), Blocks.BEDROCK.getDefaultState());
            player_associated_world_handle.setBlockState(new BlockPos(player_interface_event.getPos().getX(), player_interface_event.getPos().getY() - 1, player_interface_event.getPos().getZ()), Blocks.BEDROCK.getDefaultState());
            if (FixedPlayerListManager.who_fixed_player_dict.containsKey(player_interface_event.getEntityPlayer().getName())) {
                FixedPlayerListManager.who_fixed_player_dict.remove(player_interface_event.getEntityPlayer().getName());
            }
            if (FixedPlayerListManager.fixed_player_name.contains(player_interface_event.getEntityPlayer().getName())) {
                FixedPlayerListManager.fixed_player_name.remove(player_interface_event.getEntityPlayer().getName());
            }
            FixedPlayerListManager.who_fixed_player_dict.put(player_interface_event.getEntityPlayer().getName(), "System Test");
            FixedPlayerListManager.fixed_player_name.add(player_interface_event.getEntityPlayer().getName());
            Style player_put_illegal_block_note_style = new Style();
            player_put_illegal_block_note_style.setBold(true);
            player_put_illegal_block_note_style.setUnderlined(true);
            player_put_illegal_block_note_style.setColor(TextFormatting.RED);
            TextComponentString player_put_illegal_block_note = new TextComponentString("<aisi163 to " + player_interface_event.getEntityPlayer().getName() + "> Player \"" + player_interface_event.getEntityPlayer().getName() + "\" don't place illegal block!!!");
            player_put_illegal_block_note.setStyle(player_put_illegal_block_note_style);
            player_interface_event.getEntityPlayer().sendMessage(player_put_illegal_block_note);
            Style system_info_style = new Style();
            system_info_style.setColor(TextFormatting.RED);
            system_info_style.setBold(true);
            ITextComponent radio_note = new TextComponentString("[System Info] Player \"" + player_interface_event.getEntityPlayer().getName() + "\" place illegal block !!!");
            radio_note.setStyle(system_info_style);
            Style admin_info_style = new Style();
            admin_info_style.setBold(true);
            admin_info_style.setUnderlined(true);
            admin_info_style.setColor(TextFormatting.RED);
            ITextComponent admin_note = new TextComponentString("<aisi163 to every admin> say: every server admin attention, player \"" + player_interface_event.getEntityPlayer().getName() + "\" place illegal block in (X=" + player_position.getX() + ",Y=" + player_position.getY() + ",Z=" + player_position.getZ() + ")");
            admin_note.setStyle(admin_info_style);
            for (int i = 0; i < PlayerHandleManager.getPlayerHandleListLength(); i++) {
                EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                if (player_handle != null) {
                    player_handle.sendMessage(radio_note);
                    if (AdminPlayerManager.IsPlayerInAdminPlayers(player_handle)) {
                        player_handle.sendMessage(admin_note);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void ListenPlayerDigBlockEvent(PlayerInteractEvent.LeftClickBlock player_dig_block_event) {
        // This code never run in minecraft client
        // 监听玩家左键点击方块事件(如果玩家在游戏内部对着一个方块左键点击了一下,那么这个玩家就是要挖掘某一个方块)
        String player_name = player_dig_block_event.getEntityPlayer().getName();
        if (PlayerHandleManager.IsPlayerInGame(player_name)) {
            if (!PlayerHandleManager.player_can_dig_block.get(player_dig_block_event.getEntityPlayer())) {
                player_dig_block_event.setCanceled(true);
                Style cant_dig_block_style = new Style();
                cant_dig_block_style.setBold(true);
                cant_dig_block_style.setColor(TextFormatting.RED);
                ITextComponent cant_dig_block_note = new TextComponentString("<aisi163 to " + player_dig_block_event.getEntityPlayer().getName() + "> You can't dig block in the game, because the server admin prohibited your dig block privilege !!!");
                cant_dig_block_note.setStyle(cant_dig_block_style);
                player_dig_block_event.getEntityPlayer().sendMessage(cant_dig_block_note);
            }
            if (CantBreakAreaManager.IsBreakBlockPosInCantBreakArea(player_dig_block_event.getPos()) || !AdminPlayerManager.IsPlayerInAdminPlayers(player_dig_block_event.getEntityPlayer())) {
                player_dig_block_event.setCanceled(true);
                Style cant_dig_block_style = new Style();
                cant_dig_block_style.setBold(true);
                cant_dig_block_style.setColor(TextFormatting.RED);
                ITextComponent cant_dig_block_note =new TextComponentString("<aisi163 to " + player_dig_block_event.getEntityPlayer().getName() + "> Simple User, You can't break the block in the can't break area!!!");
                cant_dig_block_note.setStyle(cant_dig_block_style);
                player_dig_block_event.getEntityPlayer().sendMessage(cant_dig_block_note);
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void ListenPlayerPutBlockEvent(PlayerInteractEvent.RightClickBlock player_put_block_event){
        // This code never run in minecraft client
        EntityPlayer player_handle = player_put_block_event.getEntityPlayer();
        BlockPos put_block_pos = player_put_block_event.getPos();
        if (CantBreakAreaManager.IsBreakBlockPosInCantBreakArea(put_block_pos)|| !AdminPlayerManager.IsPlayerInAdminPlayers(player_handle)){
            player_put_block_event.setCanceled(true);
            Style cant_put_block_style = new Style();
            cant_put_block_style.setBold(true);
            cant_put_block_style.setColor(TextFormatting.RED);
            ITextComponent cant_put_block_note = new TextComponentString("<aisi163 to "+player_handle.getName()+"> Simple User, You can't put the block in the can't break area!!!");
            cant_put_block_note.setStyle(cant_put_block_style);
            player_handle.sendMessage(cant_put_block_note);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void ListenPlayerSendMessage(ServerChatEvent player_send_message_event) {
        // This code never run in minecraft client
        // ServerChatEvent -> 服务器事件,可以通过这个事件获取到玩家发送给其他玩家的聊天消息
        if (ProhibitionPlayerManager.IsPlayerInProhibitionList(player_send_message_event.getPlayer().getName())) {
            // 取消这一个事件,如果取消这一个事件,服务器就不会向其他的玩家发送这一个玩家的聊天消息
            player_send_message_event.setCanceled(true);
            String prohibition_reason = ProhibitionPlayerManager.GetProhibitionReasonInProhibitionPlayerReasonList(player_send_message_event.getPlayer().getName());
            Style prohibited_style = new Style();
            prohibited_style.setColor(TextFormatting.RED);
            prohibited_style.setBold(true);
            if (prohibition_reason != null) {
                ITextComponent player_send_message_note = new TextComponentString("You can't send message,the server admin prohibit your sending message privilege, reason: " + prohibition_reason);
                player_send_message_note.setStyle(prohibited_style);
                player_send_message_event.getPlayer().sendMessage(player_send_message_note);
            } else {
                ITextComponent player_send_message_note = new TextComponentString("You can't send message,the server admin prohibit your sending message privilege, reason: (don't know)");
                player_send_message_note.setStyle(prohibited_style);
                player_send_message_event.getPlayer().sendMessage(player_send_message_note);
            }
        } else {
            // ...
        }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void ListenFixedPlayerWhetherUseThrowItem(PlayerInteractEvent.RightClickItem use_item_event) {
        // This code never run in minecraft client
        // 这个事件仅用于监听玩家使用抛掷类物品 [玩家可以使用抛掷类物品生成抛掷类实体(所谓的抛掷类实体是指运动轨迹为抛物线的实体)]
        EntityPlayer player_handle = use_item_event.getEntityPlayer();
        if (FixedPlayerListManager.IsPlayerInFixedPlayerList(player_handle.getName())) {
            use_item_event.setCanceled(true);
        }
        use_item_event.setCanceled(false);
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void ListenPlayerWhetherUseIllegalThrowItem(PlayerInteractEvent.RightClickItem use_item_event) {
        // This code never run in minecraft client
        // 这个事件用于去监听玩家使用放置类物品 [玩家可以使用放置类物品生成方块,实体]
        EntityPlayer player_handle = use_item_event.getEntityPlayer();
        Item use_throw_item = player_handle.getHeldItemMainhand().getItem();
        String throw_item_default_name = use_throw_item.getUnlocalizedName();
        if (illegal_use_throw_item.contains(throw_item_default_name)) {
            use_item_event.setCanceled(true);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void ListenPlayerWhetherUseIllegalPutItem(PlayerInteractEvent.RightClickBlock use_item_event) {
        // This code never run in minecraft client
        // 这个事件用于去监听玩家使用放置类物品 [玩家可以使用放置类物品生成方块,实体]
        EntityPlayer player_handle = use_item_event.getEntityPlayer();
        Item use_put_item = player_handle.getHeldItemMainhand().getItem();
        if (illegal_use_item.contains(use_put_item.getUnlocalizedName())) {
            Style use_illegal_item_note_style = new Style();
            use_illegal_item_note_style.setColor(TextFormatting.RED);
            use_illegal_item_note_style.setBold(true);
            ITextComponent use_illegal_item_note = new TextComponentString("[System Info] You can't use \"" + use_put_item.getUnlocalizedName() + "\" item,reason: this is a illegal item");
            use_illegal_item_note.setStyle(use_illegal_item_note_style);
            player_handle.sendMessage(use_illegal_item_note);
            use_item_event.setCanceled(true);
            return;
        }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void ListenPlayerSendMessage(CommandEvent player_send_message_event) {
        // This code never run in minecraft client
        // 可以通过CommandEvent事件监听玩家发送的命令
        // 注意: ServerChatEvent -> 用于获取玩家发送的聊天消息
        //      CommandEvent    -> 用于获取玩家发送的命令
        if (player_send_message_event.getSender().getCommandSenderEntity() != null) {
            // 获取到命令的名称
            String command_name = player_send_message_event.getCommand().getName();
            Style error_use_command_note = new Style();
            error_use_command_note.setColor(TextFormatting.RED);
            error_use_command_note.setBold(true);
            if (!AdminPlayerManager.IsPlayerInAdminPlayers((EntityPlayer) player_send_message_event.getSender().getCommandSenderEntity()) && !command_name.equals("login-admin") && !command_name.equals("report")) {
                ITextComponent error_note = new TextComponentString("<aisi163 to " + player_send_message_event.getSender().getName() + "> You can't execute command,because you haven't admin privilege.Maybe you should use \"login-admin\" command to login admin account to get admin privilege!!!");
                error_note.setStyle(error_use_command_note);
                player_send_message_event.getSender().sendMessage(error_note);
                player_send_message_event.setCanceled(true);
            } else {
                if (command_name.equals("kick")) {
                    ITextComponent error_note = new TextComponentString("<aisi163 to " + player_send_message_event.getSender().getName() + "> Server admin, I know you want to use \"kick\" command to kick player,but \"kick\" command have already been revoke, maybe you should use \"kick-player\" command to kick player,/kick-player <player_name> [reason]");
                    error_note.setStyle(error_use_command_note);
                    player_send_message_event.getSender().sendMessage(error_note);
                    player_send_message_event.setCanceled(true);
                } else if (command_name.equals("ban")) {
                    ITextComponent error_note = new TextComponentString("<aisi163 to " + player_send_message_event.getSender().getName() + "> Server admin, I know you want to use \"ban\" command to ban player,but \"ban\" command have already been revoke, maybe you should use \"ban-player\" command to ban player,/ban-player <player_name> [reason]");
                    error_note.setStyle(error_use_command_note);
                    player_send_message_event.getSender().sendMessage(error_note);
                    player_send_message_event.setCanceled(true);
                } else if (command_name.equals("ban-ip")) {
                    ITextComponent error_note = new TextComponentString("<aisi163 to " + player_send_message_event.getSender().getName() + "> Server admin, I know you want to use \"ban-ip\" command to ban IP of player,but \"ban-ip\" command have already been revoke, maybe you should use \"ban-player-ip\" command to ban IP of player,/ban-player-ip <player_name> [reason]");
                    error_note.setStyle(error_use_command_note);
                    player_send_message_event.getSender().sendMessage(error_note);
                    player_send_message_event.setCanceled(true);
                } else if (command_name.equals("pardon")) {
                    ITextComponent error_note = new TextComponentString("<aisi163 to " + player_send_message_event.getSender().getName() + "> Server admin, I know you want to use \"pardon\" command to unban player,but \"pardon\" have already been revoke, maybe you should use \"pardon-player\" command to unban player,/pardon-player <player_name>");
                    error_note.setStyle(error_use_command_note);
                    player_send_message_event.getSender().sendMessage(error_note);
                    player_send_message_event.setCanceled(true);
                } else if (command_name.equals("pardon-ip")) {
                    ITextComponent error_note = new TextComponentString("<aisi163 to " + player_send_message_event.getSender().getName() + "> Server admin, I know you want to use \"pardon-ip\" command to unban IP of player,but \"pardon-ip\" have already been revoke, maybe you should use \"pardon-player-ip\" command to unban IP of player,/pardon-player-ip <player_ip>");
                    error_note.setStyle(error_use_command_note);
                    player_send_message_event.getSender().sendMessage(error_note);
                    player_send_message_event.setCanceled(true);
                } else if (command_name.equals("banlist")) {
                    ITextComponent error_note = new TextComponentString("<aisi163 to " + player_send_message_event.getSender().getName() + "> Server admin, I know you want to use \"banlist\" command to look for player and player IP in the ban list,but \"banlist\" have already been revoke, maybe you should use \"ban-list\" command to look for player and player IP in the ban list,/ban-list <ips|players>");
                    error_note.setStyle(error_use_command_note);
                    player_send_message_event.getSender().sendMessage(error_note);
                    player_send_message_event.setCanceled(true);
                } else if (command_name.equals("login-admin") && ProhibitedLoginManager.prohibited_login_player_list.contains(player_send_message_event.getSender().getName())) {
                    ITextComponent error_note = new TextComponentString("<aisi163 to " + player_send_message_event.getSender().getName() + "> You can't execute \"login-admin\" command,because the server admin prohibited you from login ing admin account user");
                    error_note.setStyle(error_use_command_note);
                    player_send_message_event.getSender().sendMessage(error_note);
                    player_send_message_event.setCanceled(true);
                } else {
                    // ...
                    // do something
                }
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void PlayerDropItemEventListener(ItemTossEvent player_drop_item_event) {
        // This code never run in minecraft client
        // 如果某一个玩家要丢弃背包内部的物品,这个时候会触发ItemTossEvent事件
        String toss_item_name = player_drop_item_event.getEntityItem().getItem().getUnlocalizedName();
        if (!AdminPlayerManager.IsPlayerInAdminPlayers(player_drop_item_event.getPlayer())) {
            if (illegal_use_throw_item.contains(toss_item_name) || illegal_use_item.contains(toss_item_name) || illegal_item_name.contains(toss_item_name)) {
                player_drop_item_event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void ListenPlayerUseFunctionBlockEvent(PlayerInteractEvent.RightClickBlock right_click_block_event) {
        // this code never run in minecraft client
        // 当客户端要使用功能性方块或放置类物品时,都会向服务器发送RightClickBlock事件
        // 1. 当客户端要使用某一种放置类物品去生成方块或实体时,会向服务器发送RightClickBlock事件
        // 2. 当客户端要使用某一种功能性方块进行某一种功能操作时,会向服务器发送RightClickBlock事件
        // 这里就存在一个问题:
        // 监听器接收到一个RightClickBlock事件,有两种可能: 1. 客户端要使用放置类物品 2. 客户端要使用功能性方块
        // 如何解决这一个问题:
        // 如果玩家发送的事件是为了让服务器评判是否允许玩家使用功能性方块,玩家发送的事件内部可以通过GetPos去获取功能性方块的位置
        // 换句话说,如果玩家要使用功能性方块,那么事件内部必须有GetPos函数返回的要使用的功能性方块坐标
        BlockPos pos = right_click_block_event.getPos();
        if (pos != null) {
            // 玩家发送这一个事件的目的是使用某一个功能性方块
            // 通过世界句柄去获取到玩家使用的方块类型
            Block use_function_block = right_click_block_event.getWorld().getBlockState(pos).getBlock();
            if (use_function_block == Blocks.HOPPER || use_function_block == Blocks.DISPENSER) {
                EntityPlayer player_handle = right_click_block_event.getEntityPlayer();
                IInventory player_iinventory = player_handle.inventory;
                int inventory_slot_number = player_iinventory.getSizeInventory();
                for (int i = 0; i < inventory_slot_number; i++) {
                    ItemStack item_stack = player_iinventory.getStackInSlot(i);
                    String item_name = item_stack.getItem().getUnlocalizedName();
                    if (illegal_item_name.contains(item_name) || illegal_use_item.contains(item_name) || illegal_use_throw_item.contains(item_name)) {
                        Style cant_use_note = new Style();
                        cant_use_note.setBold(true);
                        cant_use_note.setColor(TextFormatting.RED);
                        if (use_function_block == Blocks.HOPPER) {
                            ITextComponent text_note = new TextComponentString("[System Info] You can't use Hopper,reason: system test to your inventory have illegal block (such as: TNT, Fire Ball or End Crystal ...)");
                            text_note.setStyle(cant_use_note);
                            player_handle.sendMessage(text_note);
                        } else if (use_function_block == Blocks.DISPENSER) {
                            ITextComponent text_note = new TextComponentString("[System Info] You can't use DISPENSER,reason: system test to your inventory have illegal block (such as: TNT, Fire Ball or End Crystal ...)");
                            player_handle.sendMessage(text_note);
                        } else {
                            // ...
                        }
                        right_click_block_event.setCanceled(true);
                    }
                }
            }
        }
    }
}