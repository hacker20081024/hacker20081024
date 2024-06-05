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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.*;
import java.util.*;

// 如果模组仅在服务器上使用,请将Mod修饰器中的serverSideOnly属性设置为true
@Mod(modid = Main.MOD_ID, name = Main.MOD_NAME, version = Main.MOD_VERSION)
public class Main {
    // Class: Main
    // Version: 3.0

    public static final String MOD_ID = "github-hacker20081024-admintool";
    public static final String MOD_NAME = "Admin Tool";
    public static final String MOD_VERSION = "1.0";

    @EventHandler
    @SideOnly(Side.SERVER)
    public static void RegisterAdminCommand(FMLServerStartingEvent event) {
        // This code never run in minecraft client
        // Load Admin Info
        if (!LoginAdminUsernameAndPasswordManager.IsLoadAdminInfo){
            try {
                LoginAdminUsernameAndPasswordManager.LoadAdminUsernameInfoFile();
            } catch (IOException e){
                try {
                    ErrorLogManager.WriteLog("Not find admin_info.txt file,can't load admin info");
                } catch (IOException e1){
                    // ... System Error
                    System.out.println("System Error !!!");
                }
            }
            LoginAdminUsernameAndPasswordManager.IsLoadAdminInfo = true;
        }
        // Register Command
        event.registerServerCommand(AdminCommand.login_admin);
        event.registerServerCommand(AdminCommand.logout_admin);
        event.registerServerCommand(AdminCommand.list_admin_user_);
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
        event.registerServerCommand(AdminCommand.register_admin);
    }
}

class ErrorLogManager {
    // Class: ErrorLogManager
    // Version: 1.0

    public static final String LogFile = "admin_tool_error_log.txt";
    public static boolean WriteLog(String log) throws IOException {
        // This code never run in minecraft client
        File test_file = new File(LogFile);
        if (!test_file.exists()){
            test_file.createNewFile();
        }
        String all_log = "";
        FileInputStream    byte_input_stream  = new FileInputStream(test_file.getName());
        InputStreamReader  char_input_stream  = new InputStreamReader(byte_input_stream);
        BufferedReader     buffer_reader      = new BufferedReader(char_input_stream);
        char[] buffer = new char[2048];
        int length = 0;
        while ((length = buffer_reader.read(buffer)) != -1){
            for (int i = 0; i < length; i++){
                all_log += buffer[i];
            }
        }
        String now_time_str = new Date().toString();
        for (int i = 0; i < now_time_str.length(); i++){
            all_log += now_time_str.charAt(i);
        }
        all_log += ' ';
        for (int i = 0; i < log.length(); i++){
            all_log += log.charAt(i);
        }
        all_log += '\n';
        FileOutputStream   byte_output_stream = new FileOutputStream(test_file.getName());
        OutputStreamWriter char_output_stream = new OutputStreamWriter(byte_output_stream,"utf-8");
        BufferedWriter     buffer_writer      = new BufferedWriter(char_output_stream);
        for (int i = 0; i < all_log.length(); i++){
            buffer_writer.append(all_log.charAt(i));
        }
        buffer_writer.close();
        char_output_stream.close();
        byte_output_stream.close();
        return true;
    }
}

class LoginAdminUsernameAndPasswordManager {
    // Class: LoginAdminUsernameAndPasswordManager
    // Version: 2.0

    public static final HashMap<String,String> username_and_password = new HashMap<String,String>();
    public static final List<String> username_list = new ArrayList<String>();
    public static final HashMap<String,Integer> player_login_count = new HashMap<String,Integer>();
    public static final int Too_Many_Login_Count = 5;
    public static boolean IsLoadAdminInfo = false;
    public static final char[] Illegal_Char = {'~','!','@','#','$','%','^','&','*','(',')','-','_','+','=','{','}','[',']',':',';','\'','\"','|','\\','<','>',',','.','?','/'};
    public static final String admin_info_file  = new String("admin_info.txt");
    public static final String super_admin_file = new String("super_admin_info.txt");

    public static boolean IsRightFormatForAdminUser(String username, String password) {
            // This code never run in minecraft client
            for (char illegal_char: Illegal_Char){
                if (username.indexOf(illegal_char) != -1 ||  password.indexOf(illegal_char) != -1){
                    return false;
                }
            }
            return true;
    }

    public static boolean IsInSuperAdminUser(String username) throws IOException{
        // This code never run in minecraft client;
        File test_file = new File(super_admin_file);
        if (!test_file.exists()){
            throw new IOException("not find "+super_admin_file);
        } else {
            FileInputStream   byte_input_stream = new FileInputStream(super_admin_file);
            InputStreamReader char_input_stream = new InputStreamReader(byte_input_stream,"utf-8");
            BufferedReader    buffer_reader     = new BufferedReader(char_input_stream);
            String all_super_admin_user = new String();
            char[] buffer = new char[1024];
            int length = 0;
            while ((length = buffer_reader.read(buffer)) != -1){
                for (int i = 0; i < length; i++){
                    all_super_admin_user += buffer[i];
                }
            }
            String[] all_super_admin_list = all_super_admin_user.split("\n");
            for (String super_admin: all_super_admin_list){
                if (super_admin.equals(username)){
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean RegisterAdminUser(String username, String password) throws IOException {
        // This code never run in minecraft client
        if (IsRightFormatForAdminUser(username,password) && !username_and_password.containsKey(username)) {
            File test_file = new File(admin_info_file);
            if (!test_file.exists()){
                throw new IOException("not find "+admin_info_file);
            } else {
                String append_info = new String();
                FileInputStream    byte_input_stream  = new FileInputStream(test_file.getName());
                InputStreamReader  char_input_stream  = new InputStreamReader(byte_input_stream,"utf-8");
                BufferedReader     char_buffer_reader = new BufferedReader(char_input_stream);
                char[] buffer = new char[1024];
                int length = 0;
                while ((length = char_buffer_reader.read(buffer)) != -1) {
                    for (int i = 0; i < length; i++) {
                        append_info += buffer[i];
                    }
                }
                char_buffer_reader.close();
                char_input_stream.close();
                byte_input_stream.close();
                append_info += '\n';
                for (int i = 0; i < username.length(); i++) {
                    append_info += username.charAt(i);
                }
                append_info += ',';
                for (int i = 0; i < password.length(); i++) {
                    append_info += password.charAt(i);
                }
                FileOutputStream byte_output_stream = new FileOutputStream(test_file.getName());
                OutputStreamWriter char_output_stream = new OutputStreamWriter(byte_output_stream, "utf-8");
                BufferedWriter char_buffer_writer = new BufferedWriter(char_output_stream);
                for (int i = 0; i < append_info.length(); i++) {
                    char_buffer_writer.append(append_info.charAt(i));
                }
                char_buffer_writer.close();
                char_output_stream.close();
                byte_output_stream.close();
                username_and_password.clear();
                username_list.clear();
                LoadAdminUsernameInfoFile();
                return true;
            }
        }
        return false;
    }

    public static void LoadAdminUsernameInfoFile() throws IOException {
        // This code never run in minecraft client
        File test_file = new File(admin_info_file);
        if (test_file.exists()){
           FileInputStream    byte_input_stream  = new FileInputStream(test_file.getName());
           InputStreamReader  char_input_stream  = new InputStreamReader(byte_input_stream,"utf-8");
           BufferedReader     char_buffer_reader = new BufferedReader(char_input_stream);
           String all_admin_info = new String();
           char[] buffer = new char[1024];
           int length = 0;
           while ((length = char_buffer_reader.read(buffer)) != -1) {
               for (int i = 0; i < length; i++) {
                   all_admin_info += buffer[i];
               }
           }
           String[] admin_info_list = all_admin_info.split("\n");
           for (int i = 0; i < admin_info_list.length; i++){
               String[] admin_info = admin_info_list[i].split(",");
               username_and_password.put(admin_info[0],admin_info[1]);
               username_list.add(admin_info[0]);
           }
           char_buffer_reader.close();
           char_input_stream.close();
           byte_input_stream.close();
        } else {
           throw new IOException("not find "+admin_info_file);
        }
    }

    public static boolean Login(String username, String password,EntityPlayer who_login) {
        // this code never run in minecraft client
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
        // this code never run in minecraft client
        if (player_login_count.containsKey(player_handle.getUniqueID().toString())) {
            return Too_Many_Login_Count - player_login_count.get(player_handle.getUniqueID().toString()).intValue();
        } else {
            return Too_Many_Login_Count;
        }
    }

    public static boolean IsTooManyLogin(EntityPlayer player_handle){
        // this code never run in minecraft client
        if (player_login_count.containsKey(player_handle.getUniqueID().toString())) {
            if (player_login_count.get(player_handle.getUniqueID().toString()).intValue() > Too_Many_Login_Count) {
                return true;
            }
        }
        return false;
    }
}

class AdminPlayerManager {
    // Class: AdminPlayerManager
    // Version: 2.0

    public static List<String> admin_player_uuid = new ArrayList<String>();
    public static HashMap<String,String> admin_player_uuid_associated_player_name = new HashMap<String,String>();
    public static HashMap<String,String> admin_player_uuid_register_time = new HashMap<String,String>();
    public static HashMap<String,String> admin_player_uuid_admin_privilege_issuer = new HashMap<String,String>();

    public static String GetAdminPlayerNameByAdminPlayerUUID(String admin_player_uuid){
        // This code never run in minecraft client
        if (admin_player_uuid_associated_player_name.containsKey(admin_player_uuid)){
            return admin_player_uuid_associated_player_name.get(admin_player_uuid);
        } else {
            return null;
        }
    }

    public static boolean IsPlayerInAdminPlayers(EntityPlayer player_handle){
        // This code never run in minecraft client
        return admin_player_uuid.contains(player_handle.getUniqueID().toString()) && admin_player_uuid_associated_player_name.containsKey(player_handle.getUniqueID().toString());
    }

    public static boolean RemoveAdminPlayerFromAdminPlayers(EntityPlayer player_handle){
        // This code never run in minecraft client
        if (!IsPlayerInAdminPlayers(player_handle)){
            return false;
        } else {
           admin_player_uuid.remove(player_handle.getUniqueID().toString());
           admin_player_uuid_associated_player_name.remove(player_handle.getUniqueID().toString());
           return true;
        }
    }

    public static boolean AddAdminPlayerInAdminPlayers(EntityPlayer player_handle){
        // This code never run in minecraft client
        if (IsPlayerInAdminPlayers(player_handle)){
            return false;
        } else {
            admin_player_uuid.add(player_handle.getUniqueID().toString());
            admin_player_uuid_associated_player_name.put(player_handle.getUniqueID().toString(), player_handle.getName());
            return true;
        }
    }

    public static String GetAdminPlayerRegisterTime(EntityPlayer player_handle){
        // This code never run in minecraft client
        if (IsPlayerInAdminPlayers(player_handle) && admin_player_uuid_register_time.containsKey(player_handle.getUniqueID().toString())){
            return admin_player_uuid_register_time.get(player_handle.getUniqueID().toString());
        } else {
            return null;
        }
    }

    public static boolean DeleteAdminRegisterTime(EntityPlayer player_handle){
        // This code never run in minecraft client
        if (IsPlayerInAdminPlayers(player_handle) && GetAdminPlayerRegisterTime(player_handle) != null){
            admin_player_uuid_register_time.remove(player_handle.getUniqueID().toString());
            return true;
        } else {
            return false;
        }
    }

    public static boolean AddAdminPlayerRegisterTime(EntityPlayer player_handle){
        // This code never run in minecraft client
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
        // This code never run in minecraft client
        if (IsPlayerInAdminPlayers(player_handle) && admin_player_uuid_admin_privilege_issuer.containsKey(player_handle.getUniqueID().toString())){
            return admin_player_uuid_admin_privilege_issuer.get(player_handle.getUniqueID().toString());
        } else {
            return null;
        }
    }

    public static boolean DeleteAdminPlayerAdminPrivilegeIssuer(EntityPlayer player_handle){
        // This code never run in minecraft client
        if (IsPlayerInAdminPlayers(player_handle) && GetAdminPlayerAdminPrivilegeIssuer(player_handle) != null){
            admin_player_uuid_admin_privilege_issuer.remove(player_handle.getUniqueID().toString());
            return true;
        } else {
            return false;
        }
    }

    public static boolean AddAdminPlayerAdminPrivilegeIssuer(EntityPlayer player_handle,String admin_privilege_issuer){
        // This code never run in minecraft client
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
    // Class: BlackListPlayerManager
    // Version: 1.0

    public static List<String> player_balck_list = new ArrayList<String>();

    public static boolean addPlayerInBlackList(String player_name) {
        // This code never run in minecraft client
        if (player_balck_list.contains(player_name)) {
            return false;
        } else {
            player_balck_list.add(player_name);
            return true;
        }
    }

    public static boolean IsPlayerInBlackList(String player_name) {
        // This code never run in minecraft client
        return player_balck_list.contains(player_name);
    }
}

class FixedPlayerListManager{
    // Class: FixedPlayerListManager
    // Version: 1.0

    public static List<String> fixed_player_name = new ArrayList<String>();
    public static HashMap<String,String> who_fixed_player_dict = new HashMap<String,String>();

    public static boolean AddPlayerInFixedPlayerList(String player_name){
        // This code never run in minecraft client
        if (IsPlayerInFixedPlayerList(player_name)){
            return false;
        } else {
            fixed_player_name.add(player_name);
            return true;
        }
    }

    public static boolean AddWhoFixedPlayer(String player_name, String who_fixed_player){
        // This code never run in minecraft client
        if (!IsPlayerInFixedPlayerList(player_name)){
            return false;
        } else {
            who_fixed_player_dict.put(player_name,who_fixed_player);
            return true;
        }
    }

    public static String GetWhoFixedPlayerByFixedPlayerName(String player_name){
        // This code never run in minecraft client
        if (!IsPlayerInFixedPlayerList(player_name) || !who_fixed_player_dict.containsKey(player_name)) {
            return "";
        } else {
            return who_fixed_player_dict.get(player_name);
        }
    }

    public static int GetFixedPlayerLength() {
        // This code never run in minecraft client
        return fixed_player_name.size();
    }

    public static String GetFixedPlayerName(int index){
        // This code never run in minecraft client
        if (0 <= index && index < GetFixedPlayerLength()){
            return fixed_player_name.get(index);
        } else {
            return "";
        }
    }

    public static boolean DeletePlayerFromFixedPlayerList(String player_name){
        // This code never run in minecraft client
        if (!IsPlayerInFixedPlayerList(player_name)){
            return false;
        } else {
            fixed_player_name.remove(player_name);
            return true;
        }
    }

    public static boolean DeleteWhoFixedPlayer(String player_name){
        // This code never run in minecraft client
        if (!IsPlayerInFixedPlayerList(player_name) || !who_fixed_player_dict.containsKey(player_name)){
            return false;
        } else {
            who_fixed_player_dict.remove(player_name);
            return true;
        }
    }

    public static boolean IsPlayerInFixedPlayerList(String player_name) {
        // This code never run in minecraft client
        return fixed_player_name.contains(player_name);
    }
}

class PlayerIPManager {
    // Class: PlayerIPManager
    // Version: 1.0

    public static HashMap<String, String> player_IP_map = new HashMap<String, String>();

    public static String getPlayerIP(String player_name) {
        // This code never run in minecraft client
        if (player_IP_map.containsKey(player_name)) {
            return player_IP_map.get(player_name);
        } else {
            return "";
        }
    }

    public static boolean addPlayerIP(String player_name, String player_IP) {
        // This code never run in minecraft client
        if (player_IP_map.containsKey(player_name)) {
            return false;
        } else {
            player_IP_map.put(player_name, player_IP);
            return true;
        }
    }
}

class PlayerHandleManager {
    // Class: PlayerHandleManager
    // Version: 1.0

    public static List<EntityPlayer> player_handle_list = new ArrayList<EntityPlayer>();
    public static HashMap<EntityPlayer,Float> player_AI_Move_Speed = new HashMap<EntityPlayer,Float>();
    public static HashMap<EntityPlayer,Boolean> player_can_jumps = new HashMap<EntityPlayer,Boolean>();
    public static HashMap<EntityPlayer,Boolean> player_can_dig_block = new HashMap<EntityPlayer,Boolean>();
    public static HashMap<EntityPlayer,Integer> player_Y_Pos = new HashMap<EntityPlayer, Integer>();

    public static boolean addPlayerHandle(final EntityPlayer player_handle) {
        // This code never run in minecraft client
        player_handle_list.add(player_handle);
        player_AI_Move_Speed.put(player_handle, player_handle.getAIMoveSpeed());
        player_Y_Pos.put(player_handle, new Integer(player_handle.getPosition().getY()));
        player_can_jumps.put(player_handle, new Boolean(true));
        player_can_dig_block.put(player_handle, new Boolean(true));
        return true;
    }

    public static boolean removePlayerHandle(final EntityPlayer player_handle) {
        // This code never run in minecraft client
        if (player_handle_list.contains(player_handle)) {
            player_handle_list.remove(player_handle);
            return true;
        } else {
            return false;
        }
    }

    public static int getPlayerHandleListLength() {
        // This code never run in minecraft client
        return player_handle_list.size();
    }

    public static EntityPlayer getPlayerHandle(int index) {
        // This code never run in minecraft client
        if (0 <= index && index < getPlayerHandleListLength()) {
            return player_handle_list.get(index);
        } else {
            return null;
        }
    }

    public static EntityPlayer getPlayerHandleByPlayerName(String player_name){
        // This code never run in minecraft client
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
        // This code never run in minecraft client
        for (int i = 0; i < getPlayerHandleListLength(); i++){
            if (getPlayerHandle(i).getName().equals(player_name)){
                return true;
            }
        }
        return false;
    }
}

class ProhibitAreaManager {
    // Class: ProhibitAreaManager
    // Version: 1.0

    public static List<String> Prohibit_Area_Name_List = new ArrayList<String>();
    public static HashMap<String,Integer> Prohibit_Area_X1 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> Prohibit_Area_Y1 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> Prohibit_Area_Z1 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> Prohibit_Area_X2 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> Prohibit_Area_Y2 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> Prohibit_Area_Z2 = new HashMap<String,Integer>();

    public static boolean IsProhibitAreaNameInProhibitAreaNameList(String prohibit_area_name) {
        // This code never run in minecraft client
        return Prohibit_Area_Name_List.contains(prohibit_area_name);
    }

    public static boolean IsPlayerInProhibitArea(int player_position_X, int player_position_Y, int player_position_Z){
        // This code never run in minecraft client
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
        // This code never run in minecraft client
        if (!IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name)){
            Prohibit_Area_Name_List.add(prohibit_area_name);
            return true;
        } else {
            return false;
        }
    }
    public static boolean SetProhibitAreaX1(String prohibit_area_name,Integer X1){
        // This code never run in minecraft client
        if (Prohibit_Area_Name_List.contains(prohibit_area_name)){
            Prohibit_Area_X1.put(prohibit_area_name,X1);
            return true;
        }
        return false;
    }

    public static boolean SetProhibitAreaY1(String prohibit_area_name,Integer Y1){
        // This code never run in minecraft client
        if (Prohibit_Area_Name_List.contains(prohibit_area_name)){
            Prohibit_Area_Y1.put(prohibit_area_name,Y1);
            return true;
        }
        return false;
    }

    public static boolean SetProhibitAreaZ1(String prohibit_area_name,Integer Z1){
        // This code never run in minecraft client
        if (Prohibit_Area_Name_List.contains(prohibit_area_name)){
            Prohibit_Area_Z1.put(prohibit_area_name,Z1);
            return true;
        }
        return false;
    }

    public static boolean SetProhibitAreaX2(String prohibit_area_name,Integer X2){
        // This code never run in minecraft client
        if (Prohibit_Area_Name_List.contains(prohibit_area_name)){
            Prohibit_Area_X2.put(prohibit_area_name,X2);
            return true;
        }
        return false;
    }

    public static boolean SetProhibitAreaY2(String prohibit_area_name,Integer Y2){
        // This code never run in minecraft client
        if (Prohibit_Area_Name_List.contains(prohibit_area_name)){
            Prohibit_Area_Y2.put(prohibit_area_name,Y2);
            return true;
        }
        return false;
    }

    public static boolean SetProhibitAreaZ2(String prohibit_area_name,Integer Z2){
        // This code never run in minecraft client
        if (Prohibit_Area_Name_List.contains(prohibit_area_name)){
            Prohibit_Area_Z2.put(prohibit_area_name,Z2);
            return true;
        }
        return false;
    }

    public static int getProhibitAreaNameLength(){
        // This code never run in minecraft client
        return Prohibit_Area_Name_List.size();
    }

    public static String getProhibitAreaName(int index) {
        // This code never run in minecraft client
        if (0 <= index && index < Prohibit_Area_Name_List.size()){
            return Prohibit_Area_Name_List.get(index);
        } else {
            return "";
        }
    }

    public static Integer getProhibitAreaX1(String prohibit_area_name){
        // This code never run in minecraft client
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_X1.containsKey(prohibit_area_name)){
            return Prohibit_Area_X1.get(prohibit_area_name);
        } else {
            return null;
        }
    }

    public static Integer getProhibitAreaY1(String prohibit_area_name){
        // This code never run in minecraft client
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_Y1.containsKey(prohibit_area_name)){
            return Prohibit_Area_Y1.get(prohibit_area_name);
        } else {
            return null;
        }
    }

    public static Integer getProhibitAreaZ1(String prohibit_area_name){
        // This code never run in minecraft client
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_Z1.containsKey(prohibit_area_name)){
            return Prohibit_Area_Z1.get(prohibit_area_name);
        } else {
            return null;
        }
    }

    public static Integer getProhibitAreaX2(String prohibit_area_name){
        // This code never run in minecraft client
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_X2.containsKey(prohibit_area_name)){
            return Prohibit_Area_X2.get(prohibit_area_name);
        } else {
            return null;
        }
    }

    public static Integer getProhibitAreaY2(String prohibit_area_name){
        // This code never run in minecraft client
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_Y2.containsKey(prohibit_area_name)){
            return Prohibit_Area_Y2.get(prohibit_area_name);
        } else {
            return null;
        }
    }

    public static Integer getProhibitAreaZ2(String prohibit_area_name){
        // This code never run in minecraft client
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_Z2.containsKey(prohibit_area_name)){
            return Prohibit_Area_Z2.get(prohibit_area_name);
        } else {
            return null;
        }
    }

    public static boolean RemoveProhibitAreaName(String prohibit_area_name){
        // This code never run in minecraft client
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name)){
            Prohibit_Area_Name_List.remove(prohibit_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveProhibitX1(String prohibit_area_name){
        // This code never run in minecraft client
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_X1.containsKey(prohibit_area_name)){
            Prohibit_Area_X1.remove(prohibit_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveProhibitY1(String prohibit_area_name){
        // This code never run in minecraft client
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_Y1.containsKey(prohibit_area_name)){
            Prohibit_Area_Y1.remove(prohibit_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveProhibitZ1(String prohibit_area_name){
        // This code never run in minecraft client
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_Z1.containsKey(prohibit_area_name)){
            Prohibit_Area_Z1.remove(prohibit_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveProhibitX2(String prohibit_area_name){
        // This code never run in minecraft client
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_X2.containsKey(prohibit_area_name)){
            Prohibit_Area_X2.remove(prohibit_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveProhibitY2(String prohibit_area_name){
        // This code never run in minecraft client
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_Y2.containsKey(prohibit_area_name)){
            Prohibit_Area_Y2.remove(prohibit_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveProhibitZ2(String prohibit_area_name){
        // This code never run in minecraft client
        if (IsProhibitAreaNameInProhibitAreaNameList(prohibit_area_name) && Prohibit_Area_Z2.containsKey(prohibit_area_name)){
            Prohibit_Area_Z2.remove(prohibit_area_name);
            return true;
        } else {
            return false;
        }
    }
}

class CantBreakAreaManager {
    // Class: CantBreakAreaManager
    // Version: 1.0

    public static List<String> cant_break_area_list = new ArrayList<String>();
    public static HashMap<String,Integer> cant_break_area_associated_X1 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> cant_break_area_associated_Y1 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> cant_break_area_associated_Z1 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> cant_break_area_associated_X2 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> cant_break_area_associated_Y2 = new HashMap<String,Integer>();
    public static HashMap<String,Integer> cant_break_area_associated_Z2 = new HashMap<String,Integer>();

    public static boolean IsCantBreakAreaNameInCantBreakAreaList(String cant_break_area_name){
        // This code never run in minecraft client
       return cant_break_area_list.contains(cant_break_area_name);
    }

    public static boolean IsBreakBlockPosInCantBreakArea(BlockPos break_block_pos){
        // This code never run in minecraft client
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
        // This code never run in minecraft client
       if (!IsCantBreakAreaNameInCantBreakAreaList(cant_break_area_name)){
           cant_break_area_list.add(cant_break_area_name);
           return true;
       } else {
           return false;
       }
    }

    public static boolean AddCantBreakAreaAssociatedX1(String cant_break_area_name, Integer associated_X1){
        // This code never run in minecraft client
        if (IsCantBreakAreaNameInCantBreakAreaList(cant_break_area_name)){
            cant_break_area_associated_X1.put(cant_break_area_name, associated_X1);
            return true;
        } else {
            return false;
        }
    }

    public static boolean AddCantBreakAreaAssociatedY1(String cant_break_area_name, Integer associated_Y1){
        // This code never run in minecraft client
        if (IsCantBreakAreaNameInCantBreakAreaList(cant_break_area_name)){
            cant_break_area_associated_Y1.put(cant_break_area_name, associated_Y1);
            return true;
        } else {
            return false;
        }
    }

    public static boolean AddCantBreakAreaAssociatedZ1(String cant_break_area_name, Integer associated_Z1){
        // This code never run in minecraft client
        if (IsCantBreakAreaNameInCantBreakAreaList(cant_break_area_name)){
            cant_break_area_associated_Z1.put(cant_break_area_name, associated_Z1);
            return true;
        } else {
            return false;
        }
    }

    public static boolean AddCantBreakAreaAssociatedX2(String cant_break_area_name, Integer associated_X2){
        // This code never run in minecraft client
        if (IsCantBreakAreaNameInCantBreakAreaList(cant_break_area_name)){
            cant_break_area_associated_X2.put(cant_break_area_name, associated_X2);
            return true;
        } else {
            return false;
        }
    }

    public static boolean AddCantBreakAreaAssociatedY2(String cant_break_area_name, Integer associated_Y2){
        // This code never run in minecraft client
        if (IsCantBreakAreaNameInCantBreakAreaList(cant_break_area_name)){
            cant_break_area_associated_Y2.put(cant_break_area_name, associated_Y2);
            return true;
        } else {
            return false;
        }
    }

    public static boolean AddCantBreakAreaAssociatedZ2(String cant_break_area_name, Integer associated_Z2){
        // This code never run in minecraft client
        if (IsCantBreakAreaNameInCantBreakAreaList(cant_break_area_name)){
            cant_break_area_associated_Z2.put(cant_break_area_name, associated_Z2);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveCantBreakAreaList(String cant_break_area_name) {
        // This code never run in minecraft client
        if (cant_break_area_list.contains(cant_break_area_name)){
            cant_break_area_list.remove(cant_break_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveCantBreakAreaAssociatedX1(String cant_break_area_name) {
        // This code never run in minecraft client
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_X1.containsKey(cant_break_area_name)){
            cant_break_area_associated_X1.remove(cant_break_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveCantBreakAreaAssociatedY1(String cant_break_area_name) {
        // This code never run in minecraft client
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_Y1.containsKey(cant_break_area_name)){
            cant_break_area_associated_Y1.remove(cant_break_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveCantBreakAreaAssociatedZ1(String cant_break_area_name) {
        // This code never run in minecraft client
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_Z1.containsKey(cant_break_area_name)){
            cant_break_area_associated_Z1.remove(cant_break_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveCantBreakAreaAssociatedX2(String cant_break_area_name) {
        // This code never run in minecraft client
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_X2.containsKey(cant_break_area_name)){
            cant_break_area_associated_X2.remove(cant_break_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveCantBreakAreaAssociatedY2(String cant_break_area_name) {
        // This code never run in minecraft client
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_Y2.containsKey(cant_break_area_name)){
            cant_break_area_associated_Y2.remove(cant_break_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean RemoveCantBreakAreaAssociatedZ2(String cant_break_area_name) {
        // This code never run in minecraft client
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_Z2.containsKey(cant_break_area_name)){
            cant_break_area_associated_Z2.remove(cant_break_area_name);
            return true;
        } else {
            return false;
        }
    }

    public static String GetCantBreakAreaName(int index){
        // This code never run in minecraft client
        if (index < cant_break_area_list.size()){
            return cant_break_area_list.get(index);
        } else {
            return "";
        }
    }

    public static Integer GetCantBreakAreaAssociatedX1(String cant_break_area_name){
        // This code never run in minecraft client
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_X1.containsKey(cant_break_area_name)){
            return cant_break_area_associated_X1.get(cant_break_area_name);
        } else {
            return null;
        }
    }

    public static Integer GetCantBreakAreaAssociatedY1(String cant_break_area_name){
        // This code never run in minecraft client
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_Y1.containsKey(cant_break_area_name)){
            return cant_break_area_associated_Y1.get(cant_break_area_name);
        } else {
            return null;
        }
    }

    public static Integer GetCantBreakAreaAssociatedZ1(String cant_break_area_name){
        // This code never run in minecraft client
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_Z1.containsKey(cant_break_area_name)){
            return cant_break_area_associated_Z1.get(cant_break_area_name);
        } else {
            return null;
        }
    }

    public static Integer GetCantBreakAreaAssociatedX2(String cant_break_area_name){
        // This code never run in minecraft client
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_X2.containsKey(cant_break_area_name)){
            return cant_break_area_associated_X2.get(cant_break_area_name);
        } else {
            return null;
        }
    }

    public static Integer GetCantBreakAreaAssociatedY2(String cant_break_area_name){
        // This code never run in minecraft client
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_Y2.containsKey(cant_break_area_name)){
            return cant_break_area_associated_Y2.get(cant_break_area_name);
        } else {
            return null;
        }
    }

    public static Integer GetCantBreakAreaAssociatedZ2(String cant_break_area_name){
        // This code never run in minecraft client
        if (cant_break_area_list.contains(cant_break_area_name) && cant_break_area_associated_Z2.containsKey(cant_break_area_name)){
            return cant_break_area_associated_Z2.get(cant_break_area_name);
        } else {
            return null;
        }
    }

    public static Integer GetCantBreakAreaLength(){
        // This code never run in minecraft client
        return cant_break_area_list.size();
    }
}