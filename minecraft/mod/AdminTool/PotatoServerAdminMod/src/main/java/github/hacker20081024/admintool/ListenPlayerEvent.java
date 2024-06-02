package github.hacker20081024.admintool;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber
public class ListenPlayerEvent {
    public static final List<String> illegal_item_name = new ArrayList<String>();

    static {
        illegal_item_name.add(new String("item.minecraftCommandBlock"));
        illegal_item_name.add(new String("tile.commandBlock"));
        illegal_item_name.add(new String("tile.barrier"));
        illegal_item_name.add(new String("tile.structureBlock"));
        illegal_item_name.add(new String("tile.structureVoid"));
        illegal_item_name.add(new String("tile.bedrock"));
    }

    public static String encipherPlayerIpAddress(String player_ip_address) {
        String encipher_player_ip_address = "";
        for (int i = 0; i < player_ip_address.length(); i++) {
            if (i <= 1){
                encipher_player_ip_address += player_ip_address.charAt(i);
            } else {
                encipher_player_ip_address += "*";
            }
        };
        return encipher_player_ip_address;
    }
    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        EntityPlayerMP client_player_handle = (EntityPlayerMP) event.player;
        client_player_handle.getEntityData().setInteger("Message_Send_Timer",10000);
        client_player_handle.getEntityData().setInteger("In_The_Prohibited_Message_Send_Timer",350);
        client_player_handle.getEntityData().setInteger("Admin_Radio_In_The_Prohibited_Message_Send_Timer",350);
        String player_name      = client_player_handle.getName();
        String player_uuid      = client_player_handle.getUniqueID().toString();
        String player_ipaddress = client_player_handle.getPlayerIP();
        PlayerIPManager.addPlayerIP(player_name,player_ipaddress);
        PlayerHandleManager.addPlayerHandle(event.player);
        String encipher_player_ip_address = encipherPlayerIpAddress(player_ipaddress);
        boolean is_player_admin = AdminPlayerManager.IsPlayerInAdminPlayers(event.player);
        ITextComponent welcome_note1 = new TextComponentString("Welcome player \""+ player_name +"\" join the aisi163 Potato Server");
        ITextComponent welcome_note2 = new TextComponentString("[About Your Info]");
        ITextComponent welcome_note3 = new TextComponentString("Your session unique identification: "+player_uuid);
        ITextComponent welcome_note4 = new TextComponentString("Your IP address: "+encipher_player_ip_address);
        ITextComponent welcome_note5 = new TextComponentString("Your privilege: " +  (is_player_admin ? "Server Admin" : "Simple Player"));
        client_player_handle.sendMessage(welcome_note1);
        client_player_handle.sendMessage(welcome_note2);
        client_player_handle.sendMessage(welcome_note3);
        client_player_handle.sendMessage(welcome_note4);
        client_player_handle.sendMessage(welcome_note5);
        if (is_player_admin) {
            ITextComponent admin_info1 = new TextComponentString("[About Admin Info]");
            ITextComponent admin_info2 = new TextComponentString("Admin unique identification: "+client_player_handle.getUniqueID());
            ITextComponent admin_info3 = new TextComponentString("Admin register time: "+AdminPlayerManager.GetAdminPlayerRegisterTime(client_player_handle));
            ITextComponent admin_info4 = new TextComponentString("Admin Privilege Issuer: "+AdminPlayerManager.GetAdminPlayerAdminPrivilegeIssuer(client_player_handle));
            client_player_handle.sendMessage(admin_info1);
            client_player_handle.sendMessage(admin_info2);
            client_player_handle.sendMessage(admin_info3);
            client_player_handle.sendMessage(admin_info4);
            // If player is admin, set the game mode of admin to CREATIVE
            client_player_handle.setGameType(GameType.CREATIVE);
        } else {
            // If player is a simple player, set the game of the simple player to SURVIVAL | ADVENTURE
            if (!BlackListPlayerManager.IsPlayerInBlackList(player_name)) {
                // not in black list
                client_player_handle.setGameType(GameType.SURVIVAL);
            } else {
                // in block list
                client_player_handle.setGameType(GameType.ADVENTURE);
                client_player_handle.sendMessage(new TextComponentString("<aisi163 to "+client_player_handle.getName()+"> You are on black list!!!"));
                client_player_handle.sendMessage(new TextComponentString("<aisi163 to "+client_player_handle.getName()+"> Our server don't welcome you!!!"));
            }
        }
        int player_handle_list_length = PlayerHandleManager.getPlayerHandleListLength();
        for (int i = 0; i < player_handle_list_length; i++) {
            EntityPlayer receive_message_player = PlayerHandleManager.getPlayerHandle(i);
            if (receive_message_player != null){
                receive_message_player.sendMessage(new TextComponentString(""));
                if (AdminPlayerManager.IsPlayerInAdminPlayers(receive_message_player)) {
                    ITextComponent admin_user_note  = new TextComponentString("Player \""+player_name+"\" join the game");
                    ITextComponent admin_user_note1 = new TextComponentString("[About Player]");
                    ITextComponent admin_user_note2 = new TextComponentString("Player session unique identification: "+player_uuid);
                    ITextComponent admin_user_note3 = new TextComponentString("Player IP: "+player_ipaddress);
                    ITextComponent admin_user_note4 = new TextComponentString("Player Pos: [x="+client_player_handle.getPosition().getX()+",y="+client_player_handle.getPosition().getY()+",z="+client_player_handle.getPosition().getZ()+"]");
                    ITextComponent admin_user_note5 = new TextComponentString("Player World: " + client_player_handle.getEntityWorld().getWorldType().getName());
                    receive_message_player.sendMessage(admin_user_note);
                    receive_message_player.sendMessage(admin_user_note1);
                    receive_message_player.sendMessage(admin_user_note2);
                    receive_message_player.sendMessage(admin_user_note3);
                    receive_message_player.sendMessage(admin_user_note4);
                    receive_message_player.sendMessage(admin_user_note5);
                } else {
                    ITextComponent simple_user_note = new TextComponentString("Player \""+ player_name +"\" join the game, (he/she) is: "+(is_player_admin ? "[Server Admin]" : "[Simple Player]"));
                    receive_message_player.sendMessage(simple_user_note);
                }
                if (BlackListPlayerManager.IsPlayerInBlackList(receive_message_player.getName())){
                    ITextComponent simple_user_note = new TextComponentString("[Warning!!!] Players and Admins attention, player \""+ player_name + "\" is black list member");
                    receive_message_player.sendMessage(simple_user_note);
                }
            } else {
                // ...
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void onPlayerLogout(PlayerLoggedOutEvent event){
        EntityPlayer player = event.player;
        String player_name = player.getName();
        if (AdminPlayerManager.IsPlayerInAdminPlayers(player)){
            AdminPlayerManager.DeleteAdminRegisterTime(player);
            AdminPlayerManager.DeleteAdminPlayerAdminPrivilegeIssuer(player);
            AdminPlayerManager.RemoveAdminPlayerFromAdminPlayers(player);
        }
        PlayerHandleManager.removePlayerHandle(player);
        int player_handle_length = PlayerHandleManager.getPlayerHandleListLength();
        for (int i = 0; i < player_handle_length;i++){
            EntityPlayer receive_message_player = PlayerHandleManager.getPlayerHandle(i);
            if (receive_message_player != null){
                receive_message_player.sendMessage(new TextComponentString(""));
                ITextComponent left_note = new TextComponentString("Player \""+ player_name +"\" left the game");
            } else {
                // ...
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void ListenPlayerJumpEvent(LivingUpdateEvent event){
       Entity living_entity = event.getEntity();
       if (PlayerHandleManager.IsPlayerInGame(living_entity.getName())){
           if (living_entity instanceof EntityPlayer){
               EntityPlayer player_handle = (EntityPlayer) living_entity;
               boolean can_jump_boolean = PlayerHandleManager.player_can_jumps.get(player_handle).booleanValue();
               if (!can_jump_boolean){
                   double motion_Y = player_handle.motionY;
                   if (motion_Y > 0){ // Player_jump
                       player_handle.setPositionAndUpdate(player_handle.getPosition().getY(), PlayerHandleManager.player_Y_Pos.get(player_handle).intValue(), player_handle.getPosition().getZ());
                       ITextComponent note = new TextComponentString("<"+player_handle.getName()+"> You can't jump");
                       player_handle.sendMessage(note);
                   } else {
                       PlayerHandleManager.player_Y_Pos.replace(player_handle,new Integer(player_handle.getPosition().getY()));
                   }
               } else {
                   PlayerHandleManager.player_Y_Pos.replace(player_handle,new Integer(player_handle.getPosition().getY()));
               }
           }
       }
    };

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void ListenFixedPlayerMove(LivingUpdateEvent event) {
        Entity living_entity = event.getEntity();
        if (PlayerHandleManager.IsPlayerInGame(living_entity.getName())) {
            if (living_entity instanceof EntityPlayer) {
                // Player move in game
                EntityPlayer player_for_move = (EntityPlayer) living_entity;
                String player_name = player_for_move.getName();
                if (FixedPlayerListManager.IsPlayerInFixedPlayerList(player_name)) {
                    if (player_for_move.getEntityData().getInteger("Message_Send_Timer") >= 350) {
                        player_for_move.getEntityData().setInteger("Message_Send_Timer", 0);
                        ITextComponent note = new TextComponentString("<aisi163 to " + player_name + "> You can't move,because the server admin \"" + FixedPlayerListManager.GetWhoFixedPlayerByFixedPlayerName(player_name) + "\" prohibited your move privilege in game");
                        player_for_move.sendMessage(note);
                    } else {
                        player_for_move.getEntityData().setInteger("Message_Send_Timer", player_for_move.getEntityData().getInteger("Message_Send_Timer") + 1);
                    }
                    player_for_move.setPositionAndUpdate(player_for_move.getPosition().getX(),player_for_move.getPosition().getY(),player_for_move.getPosition().getZ());
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
    public static void ListenSimplePlayerMoveToProhibitedArea(LivingUpdateEvent event){
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
                if (result || !AdminPlayerManager.IsPlayerInAdminPlayers(player_handle)) {
                    player_handle.setGameType(GameType.ADVENTURE);
                    DamageSource damage_source = new DamageSource("Server");
                    player_handle.attackEntityFrom(damage_source, 1);
                    player_handle.setHealth(10);
                    ITextComponent in_prohibit_area_player_note = new TextComponentString("<aisi163 To " + player_handle.getName() + "> Player \"" + player_handle.getName() + "\" what are you doing? here is prohibited area,except server admin,other players can't in here,far away from this area now!!!");
                    if (player_handle.getEntityData().getInteger("In_The_Prohibited_Message_Send_Timer") >= 350) {
                        player_handle.getEntityData().setInteger("In_The_Prohibited_Message_Send_Timer", 0);
                        player_handle.sendMessage(in_prohibit_area_player_note);
                    } else {
                        player_handle.getEntityData().setInteger("In_The_Prohibited_Message_Send_Timer", player_handle.getEntityData().getInteger("In_The_Prohibited_Message_Send_Timer") + 1);
                    }
                    ITextComponent admin_note = new TextComponentString("<aisi163 to all of the server admins>: Every server admins attention!!! player \"" + player_handle.getName() + "\" to the prohibit area, [Player Position: (X=" + player_position_X + ",Y=" + player_position_Y + ",Z=" + player_position_Z + ")]");
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

    public static boolean IsSimplePlayerPlaceBlockInIllegalBlock(String item_name){
        for (int i = 0; i < illegal_item_name.size();i++){
            if (illegal_item_name.get(i).compareTo(item_name) == 0){
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void ListenPlayerInterfaceEvent(PlayerInteractEvent.RightClickBlock player_interface_event){
           // 当玩家放置方块的时候,获取玩家主手上面的物品
           ItemStack held_on_main_hand_item = player_interface_event.getEntityPlayer().getHeldItemMainhand();
           // 获取玩家主手上的物品的初始化名称
           String item_default_name = held_on_main_hand_item.getItem().getUnlocalizedName();
           if (item_default_name.compareTo("tile.tnt") == 0){
               // 如果玩家要通过TNT物品放置TNT方块
               player_interface_event.getEntityPlayer().sendMessage(new TextComponentString("<aisi163 to "+player_interface_event.getEntityPlayer().getName()+"> You can't place TNT"));
               player_interface_event.setCanceled(true);
           }
           if (IsSimplePlayerPlaceBlockInIllegalBlock(item_default_name) && !AdminPlayerManager.IsPlayerInAdminPlayers(player_interface_event.getEntityPlayer())){
               DamageSource damage_source = new DamageSource("server");
               player_interface_event.getEntityPlayer().attackEntityFrom(damage_source,1.0F);
               World player_associated_world_handle = player_interface_event.getWorld();
               BlockPos player_position = player_interface_event.getEntityPlayer().getPosition();
               player_associated_world_handle.setBlockState(new BlockPos(player_interface_event.getPos().getX()+1,player_interface_event.getPos().getY(),player_interface_event.getPos().getZ()),Blocks.BEDROCK.getDefaultState());
               player_associated_world_handle.setBlockState(new BlockPos(player_interface_event.getPos().getX()-1,player_interface_event.getPos().getY(),player_interface_event.getPos().getZ()),Blocks.BEDROCK.getDefaultState());
               player_associated_world_handle.setBlockState(new BlockPos(player_interface_event.getPos().getX(),player_interface_event.getPos().getY(),player_interface_event.getPos().getZ()+1),Blocks.BEDROCK.getDefaultState());
               player_associated_world_handle.setBlockState(new BlockPos(player_interface_event.getPos().getX(),player_interface_event.getPos().getY(),player_interface_event.getPos().getZ()-1),Blocks.BEDROCK.getDefaultState());
               player_associated_world_handle.setBlockState(new BlockPos(player_interface_event.getPos().getX(),player_interface_event.getPos().getY()+1,player_interface_event.getPos().getZ()),Blocks.BEDROCK.getDefaultState());
               player_associated_world_handle.setBlockState(new BlockPos(player_interface_event.getPos().getX(),player_interface_event.getPos().getY()-1,player_interface_event.getPos().getZ()),Blocks.BEDROCK.getDefaultState());
               if (FixedPlayerListManager.who_fixed_player_dict.containsKey(player_interface_event.getEntityPlayer().getName())){
                   FixedPlayerListManager.who_fixed_player_dict.remove(player_interface_event.getEntityPlayer().getName());
               }
               if (FixedPlayerListManager.fixed_player_name.contains(player_interface_event.getEntityPlayer().getName())){
                   FixedPlayerListManager.fixed_player_name.remove(player_interface_event.getEntityPlayer().getName());
               }
               FixedPlayerListManager.who_fixed_player_dict.put(player_interface_event.getEntityPlayer().getName(),"System Protected Program");
               FixedPlayerListManager.fixed_player_name.add(player_interface_event.getEntityPlayer().getName());
               player_interface_event.getEntityPlayer().sendMessage(new TextComponentString("<aisi163 to "+player_interface_event.getEntityPlayer().getName()+"> Player \""+player_interface_event.getEntityPlayer().getName()+"\" don't place illegal block!!!"));
               ITextComponent radio_note = new TextComponentString("[System Info] Player \""+player_interface_event.getEntityPlayer().getName()+"\" place illegal block !!!");
               ITextComponent admin_note = new TextComponentString("<aisi163 to every admin> say: every server admin attention, player \""+player_interface_event.getEntityPlayer().getName()+"\" place illegal block in (X="+player_position.getX()+",Y="+player_position.getY()+",Z="+player_position.getZ()+")");
               for (int i = 0; i < PlayerHandleManager.getPlayerHandleListLength();i++){
                   EntityPlayer player_handle = PlayerHandleManager.getPlayerHandle(i);
                   if (player_handle != null){
                       player_handle.sendMessage(radio_note);
                       if (AdminPlayerManager.IsPlayerInAdminPlayers(player_handle)){
                           player_handle.sendMessage(admin_note);
                       }
                   }
               }
           }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void ListenPlayerAttackOtherEntity(PlayerInteractEvent.LeftClickBlock player_dig_block_event){
        // 监听玩家左键点击方块事件(如果玩家在游戏内部对着一个方块左键点击了一下,那么这个玩家就是要挖掘某一个方块)
        String player_name = player_dig_block_event.getEntityPlayer().getName();
        if (PlayerHandleManager.IsPlayerInGame(player_name)){
            if (!PlayerHandleManager.player_can_dig_block.get(player_dig_block_event.getEntityPlayer())){
                player_dig_block_event.setCanceled(true);
                player_dig_block_event.getEntityPlayer().sendMessage(new TextComponentString("<aisi163 to "+player_dig_block_event.getEntityPlayer().getName()+"> You can't dig block in the game, because the server admin prohibited your dig block privilege !!!"));
            }
            if (CantBreakAreaManager.IsBreakBlockPosInCantBreakArea(player_dig_block_event.getPos()) || !AdminPlayerManager.IsPlayerInAdminPlayers(player_dig_block_event.getEntityPlayer())){
                player_dig_block_event.setCanceled(true);
                player_dig_block_event.getEntityPlayer().sendMessage(new TextComponentString("<aisi163 to "+player_dig_block_event.getEntityPlayer().getName()+"> Simple User, You can't break the block in the can't break area!!!"));
            }
        }
    }
}