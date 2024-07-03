package ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Util {

    private static String startNode;
    private static List<String> endNodes = new ArrayList<String>();

    public static Algorithm findTheAlgorithm(String name) {
        if(name.equalsIgnoreCase("bfs")) return Algorithm.BFS;
        if(name.equalsIgnoreCase("ucs")) return Algorithm.UCS;
        if(name.equalsIgnoreCase("astar")) return Algorithm.A_STAR;
        throw new InvalidEntryException("Invalid Algorithm name!\nAlgorithms we can use: BFS(bfs), UCS(ucs), A*(astar)");
    }

    public static List<Node> generateNodes(File stateFile){
        List<Node> temp = new ArrayList<>();
        int i = 0;
        try (Scanner reader = new Scanner(stateFile)){
            while(reader.hasNextLine()) {
                String line = reader.nextLine();
                if(!line.startsWith("#") && !line.isEmpty()) {
                    if(i < 2) {
                        if(i == 0)
                            startNode = line.trim();
                        else {
                            String[] ends = line.trim().split(" ");
                            Collections.addAll(endNodes, ends);
                        }
                        i++;
                    }else {
                        String[] nameChildren = line.split(":");
                        if(nameChildren.length == 1) {
                            temp.add(new Node(nameChildren[0].trim(),null));
                        }else {
                            Map<String,Double> nodeChildren = aquireChildren(nameChildren[1].trim());
                            temp.add(new Node(nameChildren[0].trim(),nodeChildren));
                        }
                    }
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return temp;
    }

    public static String getStartNode() {
        return startNode;
    }

    public static List<String> getEndNodes() {
        return endNodes;
    }

    private static Map<String,Double> aquireChildren(String line){
        Map<String,Double> temp = new HashMap<>();
        String[] lineEl = line.split(" ");
        for(String el : lineEl) {
            String[] elems = el.split(",");
            temp.put(elems[0],Double.parseDouble(elems[1]));
        }
        return temp;
    }

    public static Map<String, Double> aquireHeuristics(File heuristicFile) {
        Map<String,Double> temp = new HashMap<>();
        try(Scanner reader = new Scanner(heuristicFile)){
            while(reader.hasNextLine()) {
                String line = reader.nextLine();
                if(!line.startsWith("#") && !line.isEmpty()) {
                    String[] lineEl = line.split(":");
                    temp.put(lineEl[0].trim(), Double.parseDouble(lineEl[1].trim()));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return temp;
    }

}
