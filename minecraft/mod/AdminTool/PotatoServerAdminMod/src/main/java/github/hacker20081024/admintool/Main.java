package github.hacker20081024.admintool;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.Int;

import java.util.*;


@Mod(modid = Main.MOD_ID, name = Main.MOD_NAME, version = Main.MOD_VERSION)
public class Main {
    public static final String MOD_ID = "github-hacker20081024-admintool";
    public static final String MOD_NAME = "Admin Tool";
    public static final String MOD_VERSION = "1.0";

    @EventHandler
    @SideOnly(Side.SERVER)
    public static void RegisterAdminCommand(FMLServerStartingEvent event) {
        // Register Command
        event.registerServerCommand(AdminCommand.login_admin);
        event.registerServerCommand(AdminCommand.logout_admin);
        event.registerServerCommand(AdminCommand.admin);
        event.registerServerCommand(AdminCommand.ban_admin);
        event.registerServerCommand(AdminCommand.privilege);
        event.registerServerCommand(AdminCommand.ping);
        event.registerServerCommand(AdminCommand.list_all);
        event.registerServerCommand(AdminCommand.list_player);
        event.registerServerCommand(AdminCommand.list_admin);
        event.registerServerCommand(AdminCommand.gps);
        event.registerServerCommand(AdminCommand.fixed_player);
        event.registerServerCommand(AdminCommand.unfixed_player);
        event.registerServerCommand(AdminCommand.list_fixed_player);
        event.registerServerCommand(AdminCommand.list_unFixed_player);
        event.registerServerCommand(AdminCommand.add_prohibit_area);
        event.registerServerCommand(AdminCommand.remove_prohibit_area);
        event.registerServerCommand(AdminCommand.list_prohibit_area);
        event.registerServerCommand(AdminCommand.get_speed);
        event.registerServerCommand(AdminCommand.original_speed);
        event.registerServerCommand(AdminCommand.set_speed);
        event.registerServerCommand(AdminCommand.mode_a);
        event.registerServerCommand(AdminCommand.mode_c);
        event.registerServerCommand(AdminCommand.mode_su);
        event.registerServerCommand(AdminCommand.mode_sp);
        event.registerServerCommand(AdminCommand.mode);
        event.registerServerCommand(AdminCommand.can_jump);
        event.registerServerCommand(AdminCommand.can_dig_block);
        event.registerServerCommand(AdminCommand.add_cant_break_area);
        event.registerServerCommand(AdminCommand.remove_cant_break_area);
        event.registerServerCommand(AdminCommand.list_cant_break_area);
    }
}

class LoginAdminUsernameAndPasswordManager {
    public static final HashMap<String,String> username_and_password = new HashMap<String,String>();
    public static final HashMap<String,Integer> player_login_count = new HashMap<String,Integer>();
    public static final int Too_Many_Login_Count = 5;
    static {
        username_and_password.put("hacker20081024", "HackerKali1024");
    }

    public static boolean Login(String username, String password,EntityPlayer who_login) {
        if (!player_login_count.containsKey(who_login.getUniqueID().toString())){
            player_login_count.put(who_login.getUniqueID().toString(),new Integer(1));
        } else {
            player_login_count.replace(who_login.getUniqueID().toString(),new Integer(player_login_count.get(who_login.getUniqueID().toString()).intValue()+1));
        }
        if (username_and_password.containsKey(username)){
            if (username_and_password.get(username).equals(password)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static int GetRemainingLoginChance(EntityPlayer player_handle){
        if (player_login_count.containsKey(player_handle.getUniqueID().toString())) {
            return Too_Many_Login_Count - player_login_count.get(player_handle.getUniqueID().toString()).intValue();
        } else {
            return Too_Many_Login_Count;
        }
    }

    public static boolean IsTooManyLogin(EntityPlayer player_handle){
        if (player_login_count.containsKey(player_handle.getUniqueID().toString())) {
            if (player_login_count.get(player_handle.getUniqueID().toString()).intValue() > Too_Many_Login_Count) {
                return true;
            }
        }
        return false;
    }
}

class AdminPlayerManager {
    // class: AdminPlayerManager
    // Version: 2.0
    public static List<String> admin_player_uuid = new ArrayList<String>();
    public static HashMap<String,String> admin_player_uuid_associated_player_name = new HashMap<String,String>();
    public static HashMap<String,String> admin_player_uuid_register_time = new HashMap<String,String>();
    public static HashMap<String,String> admin_player_uuid_admin_privilege_issuer = new HashMap<String,String>();

    public static String GetAdminPlayerNameByAdminPlayerUUID(String admin_player_uuid){
         if (admin_player_uuid_associated_player_name.containsKey(admin_player_uuid)){
             return admin_player_uuid_associated_player_name.get(admin_player_uuid);
         } else {
             return null;
         }
    }

    public static boolean IsPlayerInAdminPlayers(EntityPlayer player_handle){
        return admin_player_uuid.contains(player_handle.getUniqueID().toString()) && admin_player_uuid_associated_player_name.containsKey(player_handle.getUniqueID().toString());
    }

    public static boolean RemoveAdminPlayerFromAdminPlayers(EntityPlayer player_handle){
        if (!IsPlayerInAdminPlayers(player_handle)){
            return false;
        } else {
           admin_player_uuid.remove(player_handle.getUniqueID().toString());
           admin_player_uuid_associated_player_name.remove(player_handle.getUniqueID().toString());
           return true;
        }
    }

    public static boolean AddAdminPlayerInAdminPlayers(EntityPlayer player_handle){
        if (IsPlayerInAdminPlayers(player_handle)){
            return false;
        } else {
            admin_player_uuid.add(player_handle.getUniqueID().toString());
            admin_player_uuid_associated_player_name.put(player_handle.getUniqueID().toString(), player_handle.getName());
            return true;
        }
    }

    public static String GetAdminPlayerRegisterTime(EntityPlayer player_handle){
        if (IsPlayerInAdminPlayers(player_handle) && admin_player_uuid_register_time.containsKey(player_handle.getUniqueID().toString())){
            return admin_player_uuid_register_time.get(player_handle.getUniqueID().toString());
        } else {
            return null;
        }
    }

    public static boolean DeleteAdminRegisterTime(EntityPlayer player_handle){
        if (IsPlayerInAdminPlayers(player_handle) && GetAdminPlayerRegisterTime(player_handle) != null){
            admin_player_uuid_register_time.remove(player_handle.getUniqueID().toString());
            return true;
        } else {
            return false;
        }
    }

    public static boolean AddAdminPlayerRegisterTime(EntityPlayer player_handle){
        if (!IsPlayerInAdminPlayers(player_handle)){
            return false;
        } else {
            if (GetAdminPlayerRegisterTime(player_handle) != null){
               DeleteAdminRegisterTime(player_handle);
            }
            Date register_time = new Date();
            admin_player_uuid_register_time.put(player_handle.getUniqueID().toString(),register_time.toString());
            return true;
        }
    }

    public static String GetAdminPlayerAdminPrivilegeIssuer(EntityPlayer player_handle) {
        if (IsPlayerInAdminPlayers(player_handle) && admin_player_uuid_admin_privilege_issuer.containsKey(player_handle.getUniqueID().toString())){
            return admin_player_uuid_admin_privilege_issuer.get(player_handle.getUniqueID().toString());
        } else {
            return null;
        }
    }

    public static boolean DeleteAdminPlayerAdminPrivilegeIssuer(EntityPlayer player_handle){
        if (IsPlayerInAdminPlayers(player_handle) && GetAdminPlayerAdminPrivilegeIssuer(player_handle) != null){
            admin_player_uuid_admin_privilege_issuer.remove(player_handle.getUniqueID().toString());
            return true;
        } else {
            return false;
        }
    }

    public static boolean AddAdminPlayerAdminPrivilegeIssuer(EntityPlayer player_handle,String admin_privilege_issuer){
        if (!IsPlayerInAdminPlayers(player_handle)){
            return false;
        } else {
            if (GetAdminPlayerAdminPrivilegeIssuer(player_handle) != null){
                DeleteAdminPlayerAdminPrivilegeIssuer(player_handle);
            }
            admin_player_uuid_admin_privilege_issuer.put(player_handle.getUniqueID().toString(),admin_privilege_issuer);
            return true;
        }
    }
}

class BlackListPlayerManager {
    public static List<String> player_balck_list = new ArrayList<String>();

    public static boolean addPlayerInBlackList(String player_name) {
        if (player_balck_list.contains(player_name)) {
            return false;
        } else {
            player_balck_list.add(player_name);
            return true;
        }
    }

    public static boolean IsPlayerInBlackList(String player_name) {
        return player_balck_list.contains(player_name);
    }
}

class FixedPlayerListManager{
    public static List<String> fixed_player_name = new ArrayList<String>();
    public static HashMap<String,String> who_fixed_player_dict = new HashMap<String,String>();

    public static boolean AddPlayerInFixedPlayerList(String player_name){
        if (IsPlayerInFixedPlayerList(player_name)){
            return false;
        } else {
            fixed_player_name.add(player_name);
            return true;
        }
    }

    public static boolean AddWhoFixedPlayer(String player_name, String who_fixed_player){
        if (!IsPlayerInFixedPlayerList(player_name)){
            return false;
        } else {
            who_fixed_player_dict.put(player_name,who_fixed_player);
            return true;
        }
    }

    public static String GetWhoFixedPlayerByFixedPlayerName(String player_name){
        if (!IsPlayerInFixedPlayerList(player_name) || !who_fixed_player_dict.containsKey(player_name)) {
            return "";
        } else {
            return who_fixed_player_dict.get(player_name);
        }
    }

    public static int GetFixedPlayerLength() {
        return fixed_player_name.size();
    }

    public static String GetFixedPlayerName(int index){
        if (0 <= index && index < GetFixedPlayerLength()){
            return fixed_player_name.get(index);
        } else {
            return "";
        }
    }

    public static boolean DeletePlayerFromFixedPlayerList(String player_name){
        if (!IsPlayerInFixedPlayerList(player_name)){
            return false;
        } else {
            fixed_player_name.remove(player_name);
            return true;
        }
    }

    public static boolean DeleteWhoFixedPlayer(String player_name){
        if (!IsPlayerInFixedPlayerList(player_name) || !who_fixed_player_dict.containsKey(player_name)){
            return false;
        } else {
            who_fixed_player_dict.remove(player_name);
            return true;
        }
    }

    public static boolean IsPlayerInFixedPlayerList(String player_name) {
        return fixed_player_name.contains(player_name);
    }
}

class PlayerIPManager {
    public static HashMap<String, String> player_IP_map = new HashMap<String, String>();

    public static String getPlayerIP(String player_name) {
        if (player_IP_map.containsKey(player_name)) {
            return player_IP_map.get(player_name);
        } else {
            return "";
        }
    }

    public static boolean addPlayerIP(String player_name, String player_IP) {
        if (player_IP_map.containsKey(player_name)) {
            return false;
        } else {
            player_IP_map.put(player_name, player_IP);
            return true;
        }
    }
}

class PlayerHandleManager {
    public static List<EntityPlayer> player_handle_list = new ArrayList<EntityPlayer>();
    public static HashMap<EntityPlayer,Float> player_AI_Move_Speed = new HashMap<EntityPlayer,Float>();
    public static HashMap<EntityPlayer,Boolean> player_can_jumps = new HashMap<EntityPlayer,Boolean>();
    public static HashMap<EntityPlayer,Boolean> player_can_dig_block = new HashMap<EntityPlayer,Boolean>();
    public static HashMap<EntityPlayer,Integer> player_Y_Pos = new HashMap<EntityPlayer, Integer>();

    public static boolean addPlayerHandle(final EntityPlayer player_handle) {
        player_handle_list.add(player_handle);
        player_AI_Move_Speed.put(player_handle, player_handle.getAIMoveSpeed());
        player_Y_Pos.put(player_handle, new Integer(player_handle.getPosition().getY()));
        player_can_jumps.put(player_handle, new Boolean(true));
        player_can_dig_block.put(player_handle, new Boolean(true));
        return true;
    }

    public static boolean removePlayerHandle(final EntityPlayer player_handle) {
        if (player_handle_list.contains(player_handle)) {
            player_handle_list.remove(player_handle);
            return true;
        } else {
            return false;
        }
    }

    public static int getPlayerHandleListLength() {
        return player_handle_list.size();
    }

    public static EntityPlayer getPlayerHandle(int index) {
        if (0 <= index && index < getPlayerHandleListLength()) {
            return player_handle_list.get(index);
        } else {
            return null;
        }
    }

    public static EntityPlayer getPlayerHandleByPlayerName(String player_name){
        if (!PlayerHandleManager.IsPlayerInGame(player_name)) {
            return null;
        } else {
          for (int i = 0; i < getPlayerHandleListLength(); i ++){
              EntityPlayer player_handle = getPlayerHandle(i);
              if (player_handle != null) {
                  if (player_handle.getName().equals(player_name)) {
                      return player_handle;
                  };
              };
          }
          return null;
        }
    };

    public static boolean IsPlayerInGame(String player_name){
        for (int i = 0; i < getPlayerHandleListLength(); i++){
            if (getPlayerHandle(i).getName().equals(player_name)){
                return true;
            }
        }
        return false;
    }
}

class ProhibitAreaManager {
    public static List<String> Prohibit_Area_Name_List = new ArrayList<String>();
    public static HashMap<String,Integer> Prohibit_Area_X1 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> Prohibit_Area_Y1 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> Prohibit_Area_Z1 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> Prohibit_Area_X2 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> Prohibit_Area_Y2 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> Prohibit_Area_Z2 = new HashMap<String,Integer>();

    public static boolean IsProhibitAreaNameInProhibitAreaNameList(String prohibit_area_name) {
        return Prohibit_Area_Name_List.contains(prohibit_area_name);
    }

    public static boolean IsPlayerInProhibitArea(int player_position_X, int player_position_Y, int player_position_Z){
        boolean In_X = false;
        boolean In_Y = false;
        boolean In_Z = false;
        for (int i = 0; i < Prohibit_Area_Name_List.size(); i++) {
            int prohibit_area_X1 = Prohibit_Area_X1.get(getProhibitAreaName(i));
            int prohibit_area_Y1 = Prohibit_Area_Y1.get(getProhibitAreaName(i));
            int prohibit_area_Z1 = Prohibit_Area_Z1.get(getProhibitAreaName(i));
            int prohibit_area_X2 = Prohibit_Area_X2.get(getProhibitAreaName(i));
            int prohibit_area_Y2 = Prohibit_Area_Y2.get(getProhibitAreaName(i));
            int prohibit_area_Z2 = Prohibit_Area_Z2.get(getProhibitAreaName(i));
            if (prohibit_area_X1 > prohibit_area_X2){
                if (prohibit_area_X2 <= player_position_X && player_position_X <= prohibit_area_X1){
                    In_X = true;
                }
            } else if (prohibit_area_X1 == prohibit_area_X2){
                if (player_position_X == prohibit_area_X1 && player_position_X == prohibit_area_X2){
                    In_X = true;
                }
            } else {
                // prohibit_area_X1 < prohibit_area_X2
                if (prohibit_area_X1 <= player_position_X && player_position_X <= prohibit_area_X2){
                    In_X = true;
                }
            }

            if (prohibit_area_Y1 > prohibit_area_Y2){
                if (prohibit_area_Y2 <= player_position_Y && player_position_Y <= prohibit_area_Y1){
                    In_Y = true;
                }
            } else if (prohibit_area_Y1 == prohibit_area_Y2){
                if(player_position_Y == prohibit_area_Y1 && player_position_Y == prohibit_area_Y2){
                    In_Y = true;
                }
            } else {
                // prohibit_area_Y1 < prohibit_area_Y2
                if (prohibit_area_Y1 <= player_position_Y && player_position_Y <= prohibit_area_Y2){
                    In_Y = true;
                }
            }

            if (prohibit_area_Z1 > prohibit_area_Z2){
                if (prohibit_area_Z2 <= player_position_Z && player_position_Z <= prohibit_area_Z1){
                    In_Z = true;
                }
            } else if (prohibit_area_Z1 == prohibit_area_Z2){
                if (player_position_Z == prohibit_area_Z1 && player_position_Z == prohibit_area_Z2){
                    In_Z = true;
                }
            } else {
                // prohibit_area_Z1 < prohibit_area_Z2
                if (prohibit_area_Z1 <= player_position_Z && player_position_Z <= prohibit_area_Z2){
                    In_Z = true;
                }
            }
            if (In_X && In_Y && In_Z){
                return true;
            } else {
                In_X = false;
                In_Y = false;
                In_Z = false;
            }
        }
        return false;
    }

    public static boolean addProhibitAreaName(String prohibit_area_name){
        if (!IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name)){
            Prohibit_Area_Name_List.add(prohibit_area_name);
            return true;
        } else {
            return false;
        }
    }
    public static boolean SetProhibitAreaX1(String prohibit_area_name,Integer X1){
        if (Prohibit_Area_Name_List.contains(prohibit_area_name)){
            Prohibit_Area_X1.put(prohibit_area_name,X1);
            return true;
        }
        return false;
    }

    public static boolean SetProhibitAreaY1(String prohibit_area_name,Integer Y1){
        if (Prohibit_Area_Name_List.contains(prohibit_area_name)){
            Prohibit_Area_Y1.put(prohibit_area_name,Y1);
            return true;
        }
        return false;
    }

    public static boolean SetProhibitAreaZ1(String prohibit_area_name,Integer Z1){
        if (Prohibit_Area_Name_List.contains(prohibit_area_name)){
            Prohibit_Area_Z1.put(prohibit_area_name,Z1);
            return true;
        }
        return false;
    }

    public static boolean SetProhibitAreaX2(String prohibit_area_name,Integer X2){
        if (Prohibit_Area_Name_List.contains(prohibit_area_name)){
            Prohibit_Area_X2.put(prohibit_area_name,X2);
            return true;
        }
        return false;
    }

    public static boolean SetProhibitAreaY2(String prohibit_area_name,Integer Y2){
        if (Prohibit_Area_Name_List.contains(prohibit_area_name)){
            Prohibit_Area_Y2.put(prohibit_area_name,Y2);
            return true;
        }
        return false;
    }

    public static boolean SetProhibitAreaZ2(String prohibit_area_name,Integer Z2){
        if (Prohibit_Area_Name_List.contains(prohibit_area_name)){
            Prohibit_Area_Z2.put(prohibit_area_name,Z2);
            return true;
        }
        return false;
    }

    public static int getProhibitAreaNameLength(){
        return Prohibit_Area_Name_List.size();
    }

    public static String getProhibitAreaName(int index) {
        if (0 <= index && index < Prohibit_Area_Name_List.size()){
            return Prohibit_Area_Name_List.get(index);
        } else {
            return "";
        }
    }

    public static Integer getProhibitAreaX1(String prohibit_area_name){
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_X1.containsKey(prohibit_area_name)){
            return Prohibit_Area_X1.get(prohibit_area_name);
        } else {
            return null;
        }
    }

    public static Integer getProhibitAreaY1(String prohibit_area_name){
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_Y1.containsKey(prohibit_area_name)){
            return Prohibit_Area_Y1.get(prohibit_area_name);
        } else {
            return null;
        }
    }

    public static Integer getProhibitAreaZ1(String prohibit_area_name){
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_Z1.containsKey(prohibit_area_name)){
            return Prohibit_Area_Z1.get(prohibit_area_name);
        } else {
            return null;
        }
    }

    public static Integer getProhibitAreaX2(String prohibit_area_name){
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_X2.containsKey(prohibit_area_name)){
            return Prohibit_Area_X2.get(prohibit_area_name);
        } else {
            return null;
        }
    }

    public static Integer getProhibitAreaY2(String prohibit_area_name){
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_Y2.containsKey(prohibit_area_name)){
            return Prohibit_Area_Y2.get(prohibit_area_name);
        } else {
            return null;
        }
    }

    public static Integer getProhibitAreaZ2(String prohibit_area_name){
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_Z2.containsKey(prohibit_area_name)){
            return Prohibit_Area_Z2.get(prohibit_area_name);
        } else {
            return null;
        }
    }

    public static boolean RemoveProhibitAreaName(String prohibit_area_name){
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name)){
            Prohibit_Area_Name_List.remove(prohibit_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveProhibitX1(String prohibit_area_name){
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_X1.containsKey(prohibit_area_name)){
            Prohibit_Area_X1.remove(prohibit_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveProhibitY1(String prohibit_area_name){
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_Y1.containsKey(prohibit_area_name)){
            Prohibit_Area_Y1.remove(prohibit_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveProhibitZ1(String prohibit_area_name){
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_Z1.containsKey(prohibit_area_name)){
            Prohibit_Area_Z1.remove(prohibit_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveProhibitX2(String prohibit_area_name){
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_X2.containsKey(prohibit_area_name)){
            Prohibit_Area_X2.remove(prohibit_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveProhibitY2(String prohibit_area_name){
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_Y2.containsKey(prohibit_area_name)){
            Prohibit_Area_Y2.remove(prohibit_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveProhibitZ2(String prohibit_area_name){
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_Z2.containsKey(prohibit_area_name)){
            Prohibit_Area_Z2.remove(prohibit_area_name);
            return true;
        } else {
            return false;
        }
    }
}

class CantBreakAreaManager {
    public static List<String> cant_break_area_list = new ArrayList<String>();
    public static HashMap<String,Integer> cant_break_area_associated_X1 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> cant_break_area_associated_Y1 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> cant_break_area_associated_Z1 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> cant_break_area_associated_X2 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> cant_break_area_associated_Y2 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> cant_break_area_associated_Z2 = new HashMap<String,Integer>();

    public static boolean IsCantBreakAreaNameInCantBreakAreaList(String cant_break_area_name){
       return cant_break_area_list.contains(cant_break_area_name);
    }

    public static boolean IsBreakBlockPosInCantBreakArea(BlockPos break_block_pos){
       int break_block_pos_X = break_block_pos.getX();
       int break_block_pos_Y = break_block_pos.getY();
       int break_block_pos_Z = break_block_pos.getZ();
       boolean In_X = false;
       boolean In_Y = false;
       boolean In_Z = false;
       for (int i = 0; i < cant_break_area_list.size(); i++){
           Integer X1 = GetCantBreakAreaAssociatedX1(cant_break_area_list.get(i));
           Integer Y1 = GetCantBreakAreaAssociatedY1(cant_break_area_list.get(i));
           Integer Z1 = GetCantBreakAreaAssociatedZ1(cant_break_area_list.get(i));
           Integer X2 = GetCantBreakAreaAssociatedX2(cant_break_area_list.get(i));
           Integer Y2 = GetCantBreakAreaAssociatedY2(cant_break_area_list.get(i));
           Integer Z2 = GetCantBreakAreaAssociatedZ2(cant_break_area_list.get(i));
           if (X1 > X2){
               if (X2 <= break_block_pos_X && break_block_pos_X <= X1){
                   In_X = true;
               }
           } else if (X1 == X2){
               if (X1 == break_block_pos_X && break_block_pos_X == X2){
                   In_X = true;
               }
           } else {
               if (X1 < X2){
                   if (X1 <= break_block_pos_X && break_block_pos_X <= X2){
                       In_X = true;
                   }
               }
           }
           if (Y1 > Y2){
               if (Y2 <= break_block_pos_Y && break_block_pos_Y <= Y1){
                   In_Y = true;
               }
           } else if (Y1 == Y2){
               if (Y1 == break_block_pos_Y && break_block_pos_Y == Y2){
                   In_Y = true;
               }
           } else {
               if (Y1 < Y2){
                   if (Y1 <= break_block_pos_Y && break_block_pos_Y <= Y2){
                       In_Y = true;
                   }
               }
           }
           if (Z1 > Z2){
               if (Z2 <= break_block_pos_Z && break_block_pos_Z <= Z1){
                   In_Z = true;
               }
           } else if (Z1 == Z2){
               if (Z1 == break_block_pos_Z && Z2 == break_block_pos_Z){
                   In_Z = true;
               }
           } else {
               if (Z2 > Z1){
                   if (Z1 <= break_block_pos_Z && break_block_pos_Z <= Z2){
                       In_Z = true;
                   }
               }
           }
           if (In_X && In_Y && In_Z){
               return true;
           } else {
               In_X = false;
               In_Y = false;
               In_Z = false;
           }
       }
       return false;
    };

    public static boolean AddCantBreakAreaList(String cant_break_area_name){
       if (!IsCantBreakAreaNameInCantBreakAreaList(cant_break_area_name)){
           cant_break_area_list.add(cant_break_area_name);
           return true;
       } else {
           return false;
       }
    }

    public static boolean AddCantBreakAreaAssociatedX1(String cant_break_area_name, Integer associated_X1){
        if (IsCantBreakAreaNameInCantBreakAreaList(cant_break_area_name)){
            cant_break_area_associated_X1.put(cant_break_area_name, associated_X1);
            return true;
        } else {
            return false;
        }
    }

    public static boolean AddCantBreakAreaAssociatedY1(String cant_break_area_name, Integer associated_Y1){
        if (IsCantBreakAreaNameInCantBreakAreaList(cant_break_area_name)){
            cant_break_area_associated_Y1.put(cant_break_area_name, associated_Y1);
            return true;
        } else {
            return false;
        }
    }

    public static boolean AddCantBreakAreaAssociatedZ1(String cant_break_area_name, Integer associated_Z1){
        if (IsCantBreakAreaNameInCantBreakAreaList(cant_break_area_name)){
            cant_break_area_associated_Z1.put(cant_break_area_name, associated_Z1);
            return true;
        } else {
            return false;
        }
    }

    public static boolean AddCantBreakAreaAssociatedX2(String cant_break_area_name, Integer associated_X2){
        if (IsCantBreakAreaNameInCantBreakAreaList(cant_break_area_name)){
            cant_break_area_associated_X2.put(cant_break_area_name, associated_X2);
            return true;
        } else {
            return false;
        }
    }

    public static boolean AddCantBreakAreaAssociatedY2(String cant_break_area_name, Integer associated_Y2){
        if (IsCantBreakAreaNameInCantBreakAreaList(cant_break_area_name)){
            cant_break_area_associated_Y2.put(cant_break_area_name, associated_Y2);
            return true;
        } else {
            return false;
        }
    }

    public static boolean AddCantBreakAreaAssociatedZ2(String cant_break_area_name, Integer associated_Z2){
        if (IsCantBreakAreaNameInCantBreakAreaList(cant_break_area_name)){
            cant_break_area_associated_Z2.put(cant_break_area_name, associated_Z2);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveCantBreakAreaList(String cant_break_area_name) {
        if (cant_break_area_list.contains(cant_break_area_name)){
            cant_break_area_list.remove(cant_break_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveCantBreakAreaAssociatedX1(String cant_break_area_name) {
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_X1.containsKey(cant_break_area_name)){
            cant_break_area_associated_X1.remove(cant_break_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveCantBreakAreaAssociatedY1(String cant_break_area_name) {
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_Y1.containsKey(cant_break_area_name)){
            cant_break_area_associated_Y1.remove(cant_break_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveCantBreakAreaAssociatedZ1(String cant_break_area_name) {
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_Z1.containsKey(cant_break_area_name)){
            cant_break_area_associated_Z1.remove(cant_break_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveCantBreakAreaAssociatedX2(String cant_break_area_name) {
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_X2.containsKey(cant_break_area_name)){
            cant_break_area_associated_X2.remove(cant_break_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveCantBreakAreaAssociatedY2(String cant_break_area_name) {
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_Y2.containsKey(cant_break_area_name)){
            cant_break_area_associated_Y2.remove(cant_break_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveCantBreakAreaAssociatedZ2(String cant_break_area_name) {
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_Z2.containsKey(cant_break_area_name)){
            cant_break_area_associated_Z2.remove(cant_break_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static String GetCantBreakAreaName(int index){
        if (index < cant_break_area_list.size()){
            return cant_break_area_list.get(index);
        } else {
            return "";
        }
    }

    public static Integer GetCantBreakAreaAssociatedX1(String cant_break_area_name){
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_X1.containsKey(cant_break_area_name)){
            return cant_break_area_associated_X1.get(cant_break_area_name);
        } else {
            return null;
        }
    }

    public static Integer GetCantBreakAreaAssociatedY1(String cant_break_area_name){
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_Y1.containsKey(cant_break_area_name)){
            return cant_break_area_associated_Y1.get(cant_break_area_name);
        } else {
            return null;
        }
    }

    public static Integer GetCantBreakAreaAssociatedZ1(String cant_break_area_name){
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_Z1.containsKey(cant_break_area_name)){
            return cant_break_area_associated_Z1.get(cant_break_area_name);
        } else {
            return null;
        }
    }

    public static Integer GetCantBreakAreaAssociatedX2(String cant_break_area_name){
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_X2.containsKey(cant_break_area_name)){
            return cant_break_area_associated_X2.get(cant_break_area_name);
        } else {
            return null;
        }
    }

    public static Integer GetCantBreakAreaAssociatedY2(String cant_break_area_name){
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_Y2.containsKey(cant_break_area_name)){
            return cant_break_area_associated_Y2.get(cant_break_area_name);
        } else {
            return null;
        }
    }

    public static Integer GetCantBreakAreaAssociatedZ2(String cant_break_area_name){
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_Z2.containsKey(cant_break_area_name)){
            return cant_break_area_associated_Z2.get(cant_break_area_name);
        } else {
            return null;
        }
    }

    public static Integer GetCantBreakAreaLength(){
        return cant_break_area_list.size();
    }
}