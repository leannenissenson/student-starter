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

public class robot_player extends player_human implements Player {
    private Map<String, Integer> rewards; 
    private int stage;

    public robot_player(Map<String, Integer> data_dict, String type) {
        assert type.substring(0,2).equals("ROB"); 
        super(data_dict, type); 
        this.stage = (int)type.charAt(type.length()-1); 

        
    }

    public robot_player(Player player, String name) {
        super(player, name); 
    }
    
}
