package edu.starter;
import java.util.Scanner;
import java.util.HashMap; 
import java.util.Map; 
import java.util.Collection;
import java.util.List;
import java.util.Random; 
import java.util.ArrayList; 
import java.util.Arrays; 
import java.lang.reflect.Constructor; 

public class Main {
    static int players_num = 1; 
    static Main main2 = new Main();
    static Game game = new Game(); 
    static Map<String, Double> attacks_crit_dict = new HashMap(Map.of("REG", 1.0, "SPECIAL", 1.5)); 
    static Collection<String> attacks = attacks_crit_dict.keySet(); 
    static Map<String, Map<String, Integer>> moves_expenses_dict = new HashMap(Map.of(
        "REG", Map.of(),
        "SPECIAL", Map.of("resource0", -5), 
        "MAGIC1", Map.of(
            "resource0", -3 , 
            "defense_stat1", -2), 
        "MAGIC2", Map.of(
            "resource0", -3 , 
            "defense_stat0", 2), 
        "Recover", Map.of(
            "resource0", 3)));
    
    static String[] moves = (moves_expenses_dict.keySet()).toArray(new String[0]);  
    
    static String[] stats_names = {"health", "defense_stat", "attack_stat", "resource",};
    
    static Map<String, int[]> type_data = Map.of(
        "KNIGHT", new int[] {100, 20, 10, 5}, 
        "MAGE", new int[] {100, 10, 8, 10},
        "ROBlv1", new int[] {30, 10, 10, 10}, 
        "ROBlv2", new int[] {45, 20, 15, 10}); 

    static Map<String, Player> players_dict = new HashMap(); 

    public static Map<String, Player> player_types_dict = new HashMap<>(); 
    
    static {
        for (String type_name : type_data.keySet()) {

            Map<String, Integer> data = new HashMap<>(); 

            
            int i = 0; 
            for (String stat : stats_names) {
                data.put(stat, type_data.get(type_name)[i]);
                i++;  
            }

            Player player = null; 

            Class player_class = Player.define_class(type_name); 

            try {
                Constructor<?> constructor = player_class.getConstructor(Map.class, String.class);

                player = (Player) constructor.newInstance(data, type_name); 

            } catch(ReflectiveOperationException error) {System.out.println(error); }

            player_types_dict.put(type_name, player); 

        }

    }

    static robot_player[] robot_players = {new robot_player(player_types_dict.get(
        "ROBlv1"), "Robotlv1"), new robot_player(player_types_dict.get(
            "ROBlv2"), "Robotlv2")}; 

    
    static List<String> player_human_types = new ArrayList<>(); 

    static Collection<String> player_types = player_types_dict.keySet(); 
    

    static {for (String type : player_types) {
        if (Player.define_class(type).equals(player_human.class)) {
            player_human_types.add(type); 
        }
    }}

    static List<Map<String, Integer>> robots_rewards = List.of(
        Map.of("attack_stat", 2), 
        Map.of("health", 25)); 

    static Scanner scanner = new Scanner(System.in);

        public static void main(String[] args) {

            System.out.println("Hello world");
            
            Collection<Player> players = game.players_get(players_num); 
            players_num = players_num + robot_players.length; 

            game.Game_loop(players);

        }
}

