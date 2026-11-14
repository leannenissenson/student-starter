package edu.starter; 
import java.util.Scanner;
import java.util.HashMap; 
import java.util.Map; 
import java.util.Collection;
import java.util.List;
import java.util.Random; 
import java.util.ArrayList; 
import java.util.Arrays; 

public class Game {
    public Game() {} 
    
    public Collection<Player> players_get(int players_num) {
    
        for (robot_player rob : Main.robot_players) {
            Main.players_dict.put(rob.get_name(), rob); 
        }
        for (int i=0; i<=players_num-1; i++) {
            System.out.print(String.format("Name of player%s: ", i+1));  
            String name = Main.scanner.next(); 

            
            String type = null; 
            
            while (!Main.player_human_types.contains(type)) {
                System.out.println(String.format("%s", Main.player_human_types));
                type=Main.scanner.next();

            }

            
            Player player_i = (Main.player_types_dict.get(type)).clone();

            
            player_i = new player_human(player_i, name); 
            
            
            
            Main.players_dict.put(name, player_i); 

        }

        return Main.players_dict.values();
    }
    public Player get_winner(Collection<Player> players) {
        int defeated = 0;
        Player winner_player = null; 
        Player a = null; 

        for (Player player1 : players) {
            
            a = player1; 
            if (player1.get_data_dict().get("health")<=0) {
                defeated++;
            }
            else {
                winner_player = player1; 
            }
        }
        if (defeated >= Main.players_num-1&&!(winner_player==null)) {
            return winner_player;
        }
        else {
            if (defeated >= Main.players_num-1) {
                return a; 
            }
        }
        return null; 
        
    }

    public void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void turn(Player player1, Collection<Player> players) {
        System.out.println(String.format("Player %s", player1.get_name())); 

        String player2_name = "X"; 
        
        Player player2 = null; 
        String answer = "X"; 

        int player2_health = 0; 

        Collection<String> players_name = Main.players_dict.keySet(); 

        do {
            System.out.println(String.format("Pick an opponent players: %s", players)); 
        
            player2_name = Main.scanner.next(); 

            System.out.println("Are you sure? Y/N "); 

            answer = Main.scanner.next(); 

            if (answer.equals("Y")) {
                Main.game.clear();
                if (players_name.contains(player2_name)) {
                    player2 = Main.players_dict.get(player2_name); 
                    player2_health = player2.get_data_dict().get("health"); 
                    if (player2_health > 0) {
                        player1.fight(player2);
                    }
                }
                
            
            
            Main.game.clear(); 

            }
        } while ((!Main.players_dict.containsKey(player2_name))|(!answer.equals("Y"))|(!(player2_health>0))); 

    }

    public void win(Player player1) {
        Main.game.clear(); 
        if (!player1.equals(null)) {
            System.out.println(player1.get_name() + " won!"); 
        }
    }

    public void Game_loop(Collection<Player> players) {
        int turn = 0; 
        Player player_winner = null; 
        
        Main.game.clear();
        
        do {
            for (Player player1 : players) { 
                /* Robot players don't have a turn */ 
                if (player1.get_data_dict().get("health")>0&&!(player1 instanceof robot_player)) {
                    this.turn(player1, players);
                }
            }

            System.out.println(players);

            player_winner = Main.game.get_winner(players); 
                        
            turn++; 
        
    } while (player_winner==null); 
    
    Main.game.win(player_winner); 

    }
    } 