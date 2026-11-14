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

public class player_human implements Player {
        private Map<String, Integer> data_dict;
        private String type;
        private String name; 
        private int MAX_RESOURCE = 10; 
        private int RESOURCE_PER_FIGHT = 3; 
        private ArrayList<int[]> conditions = new ArrayList<>(Arrays.asList(new int[]{0, 100}, new int[]{0, -1}, new int[]{0, -1}, new int[]{0, MAX_RESOURCE})); 
        private Map<String, int[]> stat_condition_dict = new HashMap(); 

          { 
            for (int i = conditions.size(); i<Main.stats_names.length; i++) {
                int[] place_holder = {0, 1};  
                conditions.add(place_holder); 
            }

            int i = 0; 
            for (String stat : Main.stats_names) {
                stat_condition_dict.put(stat, conditions.get(i));
                i++; 

        }
    }
        public player_human(Map<String, Integer> data_dict, String type) {
            this.data_dict=data_dict; 
            this.type = type; 
            this.conditions.get(0)[1]=this.data_dict.get("health"); 
            
        }


        public player_human(Player player_type, String name) {
            
            this.data_dict = player_type.get_data_dict(); 
            this.type = player_type.get_type(); 
            this.name = name; 
            
        }

        public String toString() {
            return "Name: %s, \n %s".formatted(this.name, this.data_dict);
        }

        public boolean action(Player player2, String move) {
            if (Arrays.stream(Main.moves).anyMatch(m -> m.equals(move))) {
                Collection<String> effects = Main.moves_expenses_dict.get(move).keySet();
                boolean affect = true; 

                for (String effect : effects) { 
                    this.do_effect(effect, move, player2); 
                }
                if (Main.attacks.contains(move)) {
                    this.attack(player2, move); 
                }

                return affect; 

            }

            
            return false; 

        }

        
        public player_human clone() {
            Map<String, Integer> data_dict = new HashMap();

            

            for (String string : this.data_dict.keySet()) {
                data_dict.put(string, this.data_dict.get(string)); 
            }

            player_human player2 = new player_human(data_dict, this.type); 


            return player2; 

        }


        public void attack(Player opponent, String move) {
            int opponent_health = opponent.get_data_dict().get("health"); 
            Double crit = Main.attacks_crit_dict.get(move); 
            int attack = this.data_dict.get("attack_stat"); 

            opponent.get_data_dict().put("health", opponent_health-(int)(attack*crit));

        }

        public void do_effect(String effect, String move, Player player2) {
            Player[] players = {this, player2}; 


            List<Object> data = this.get_effect_value_change(effect, Main.moves_expenses_dict.get(move)); 

            int player_index = (int)data.get(1); 
            String effect_key = (String)data.get(0); 
            int val = players[player_index].get_data_dict().get(effect_key);  
            int value_change = (int)data.get(2); 

            players[player_index].get_data_dict().put(effect_key, this.cap(effect_key, val, value_change));
        
        }

        public boolean legal_effect(String effect, String move, Player player2) {
            Player[] players = {this, player2}; 


            List<Object> data = this.get_effect_value_change(effect, Main.moves_expenses_dict.get(move)); 


            String effect_name = (String)data.get(0); 
            int player_index = (int)data.get(1);
            int value_change = (int)data.get(2); 
            int val = players[player_index].get_data_dict().get(effect_name);

            

            return (cap(effect_name, val, value_change)==val+value_change); 


        }

        public List<String> get_avaliable_moves(Player player2) {
            List<String> avaliable_moves = new ArrayList(); 

            boolean legal_move = true; 
            for (String move : Main.moves) {
                for (String effect : Main.moves_expenses_dict.get(move).keySet()) {
                    if (!this.legal_effect(effect, move, player2)) {
                        legal_move = false; 
                    }
                }
                if (legal_move) {
                    avaliable_moves.add(move); 
                }

                legal_move = true; 
            }


            return avaliable_moves; 
            
        } 

        public String get_move(List<String> avaliable_moves) {
            String move = null; 
            if (this instanceof robot_player) {
                Random random = new Random(); 
                int index = random.nextInt(avaliable_moves.size()); 
                move  = Main.moves[index]; 
                System.out.println(this.name + " attacked with " + move); 
                return move; 
            }

            do{
            
                System.out.println(String.format("Player %s what is your move? options: %s", this.name, avaliable_moves));
                move = Main.scanner.next();
            
            } while (!avaliable_moves.contains(move)); 

            return move; 
        }

        public String get_name() {
            return this.name; 
        }

        public String get_type() {
            return this.type; 
        }
        
        public List<Object> get_effect_value_change(String effect, Map<String, Integer> effects_dict) {
            String effect_key = effect.substring(0, effect.length()-1); 
            int player_index = Integer.parseInt(effect.substring(effect.length()-1)); 
            
            int value_change = effects_dict.get(effect); 

            return List.of(effect_key, player_index, value_change); 

        }

        public Map<String, Integer> get_data_dict() {
            return this.data_dict; 
        }

        public int cap(String effect_name, int val, int value_change) {
            int[] condition = stat_condition_dict.get(effect_name); 

            if ((val+value_change) < condition[0]) {
                return condition[0]; 
            }

            if (((val+value_change) > condition[1]) && !(condition[1]==-1)) {
                return condition[1]; 
            }

            return val + value_change; 

        }

        private void add_stat(String stat, Map<String, Integer> effects_dict) {
            int value_change = effects_dict.get(stat); 

            int val = this.data_dict.get(stat); 

            this.data_dict.put(stat, this.cap(stat, val, value_change)); 

            
            
        }

        public void won(Player player2) {
            /* Add RESOURCE_PER_FIGHT to resources */

            int player1_resource = this.data_dict.get("resource"); 
            int player2_resource = player2.get_data_dict().get("resource"); 

            player1_resource = player1_resource + RESOURCE_PER_FIGHT; 
            player2_resource = player2_resource + RESOURCE_PER_FIGHT; 
            
            if (player1_resource > MAX_RESOURCE) {
                player1_resource = MAX_RESOURCE;
            }
            if (player2_resource > MAX_RESOURCE) {
                player2_resource = MAX_RESOURCE;
            }

            this.data_dict.put("resource", player1_resource); 
            player2.get_data_dict().put("resource", player2_resource); 

            
            if ((player2.getClass()).equals(robot_player.class)) {
                int stage = Integer.parseInt(player2.get_type().substring(5))-1; 
                for (String stat : Main.robots_rewards.get(stage).keySet()) {
                    this.add_stat(stat, Main.robots_rewards.get(stage)); 
                } 
            }
        }

        public void fight(Player player2) {
            List<String> avaliable_moves = null; 
            do {
                avaliable_moves = this.get_avaliable_moves(player2); 
                String move = this.get_move(avaliable_moves);
                this.action(player2, move); 

                avaliable_moves = player2.get_avaliable_moves(this);
                String move2 = player2.get_move(avaliable_moves);
                player2.action(this, move2);  
                System.out.println(Main.players_dict.values()); 


        } while (this.get_data_dict().get("health") >=0&&player2.get_data_dict().get("health") >= 0);

        if (player2.get_data_dict().get("health") > 0) {
            player2.won(this); 
        }
        
        if (this.data_dict.get("health") > 0) {
            this.won(player2); 
        }
    }

    }

