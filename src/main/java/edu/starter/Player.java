package edu.starter; 
import java.util.Scanner;

import javax.net.ssl.SSLPermission;

import java.util.HashMap; 
import java.util.Map; 
import java.util.Collection;
import java.util.List;
import java.util.Random; 
import java.util.ArrayList; 
import java.util.Arrays; 

public interface Player {
        String toString(); 
        
        Player clone(); 

        String get_move(List<String> avaliable_moves); 
        
        String get_name(); 

        String get_type();

        static Class define_class(String type) {
                if (type.startsWith("ROB")) {
                        return robot_player.class; 
                }
                return player_human.class; 
        }

        List<String> get_avaliable_moves(Player player2); 

        void won(Player player); 

        Map<String, Integer> get_data_dict(); 

        void do_effect(String effect_name, String move, Player player);
        
        boolean action(Player player, String move); 

        int cap(String effect_name, int val, int value_change);  

        void fight(Player player2); 
}