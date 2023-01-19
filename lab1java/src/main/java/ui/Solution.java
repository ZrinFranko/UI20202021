package ui;
import java.io.*;
import java.util.*;
import java.util.function.ToDoubleFunction;
import java.util.stream.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        String algorithm = "";
        boolean checkO = false;
        boolean checkC = false;
        BasicNode start = new BasicNode();
        List<String> end = new ArrayList<String>();
        File prostorStanja = new File("");
        File opisHeur = new File("");
        Map<String,Set<BasicNode>> map = new HashMap<>();
        Map<String,Integer> heuristics = new TreeMap<>();
        //ispitivanje potreba korisnika

        for(int i = 0; i < args.length ; i++){
            if(args[i].equals("--alg")) algorithm = args[i+1];
            else if(args[i].equals("--ss")) prostorStanja = new File((args[i+1]).toString());
            else if(args[i].equals("--h")) opisHeur = new File((args[i+1]).toString());
            else if(args[i].equals("--check-optimistic")) checkO = true;
            else if(args[i].equals("--check-consistent")) checkC = true;
        }
        FileInputStream fis = new FileInputStream(prostorStanja);
        Scanner sc = new Scanner(fis);
        String line;
        boolean startEndfound=false;

        //dobivanje informacija o stanjima
        while(sc.hasNextLine()){
            line = sc.nextLine();
            if(line.startsWith("#")){}
            else if(!startEndfound){
                start = new BasicNode(line,null,0);
                line = sc.nextLine();
                end = Arrays.stream(line.split(" ")).collect(Collectors.toList());
                startEndfound = true;
            }else{
                BasicNode curr = new BasicNode(line.split(": ")[0],0);
                String[] temp = line.split(": ");
                Set<BasicNode> dalje = new TreeSet<>();
                String[] tt = line.split(": ")[1].split(" ");
                for(String s : tt){
                    dalje.add(new BasicNode(s.split(",")[0],curr,Double.parseDouble(s.split(",")[1])));
                }
                map.put(curr.getName(),dalje);

            }
        }
        sc.close();
        // dobivanje informacija o heuristici
        if(algorithm.equals("astar") || checkO) {
            fis = new FileInputStream(opisHeur);
            sc = new Scanner(fis);
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                String[] temp = line.split(": ");
                heuristics.put(temp[0], Integer.parseInt(temp[1]));
            }
            sc.close();
        }

        List<String> finalEnd = end;
        Predicate<String> endTest = str -> finalEnd.contains(str);
        Function<String,Set<BasicNode>> prijelaz = str -> map.get(str);
        ToDoubleFunction<String> heuristic = str -> heuristics.get(str);
        //poziv zeljene funkcije pretrazivanja

        if(algorithm.equals("bfs")){
            BFS(start,prijelaz,endTest);
        }else if(algorithm.equals("ucs")){
            UCS(start,prijelaz,endTest);
        }else if(algorithm.equals("astar")){
            aStarSearch(start,prijelaz,endTest,heuristic,true);
        }

        //ispit optimisticnosti

        if(checkO){
            System.out.println("#HEURISTIC-OPTIMISTIC " + opisHeur);
            StringBuilder sb = new StringBuilder();
            boolean notGood = false;
            for (String s : heuristics.keySet()){
                Optional<HeuristicNode> t = aStarSearch(new BasicNode(s,null,0),prijelaz,endTest,heuristic,false);
                if(t.isPresent()){
                    double trueP = t.get().getDistanceFromStart();
                    if(trueP > heuristics.get(s))
                        sb.append("[CONDITION]: [OK] h(" + s + ") <= h*: " + heuristics.get(s) + " <= " + trueP + "\n");
                    else {
                        sb.append("[CONDITION]: [ERR] h(" + s + ") <= h*: " + heuristics.get(s) + " <= " + trueP + "\n");
                        notGood = true;
                    }
                }
            }
            sb.append("[CONCLUSION]: ");
            if(notGood) sb.append("Heuristic is not optimistic.");
            else sb.append("Heuristic is optimistic.");
            System.out.println(sb.toString());
        }

    }

    //algoritmi pretraživanja

    static  Optional<BasicNode> BFS(BasicNode poc, Function<String,Set<BasicNode>> putanja, Predicate<String> kraj){
        Deque<BasicNode> dq = new LinkedList<>();
        dq.add(poc);
        List<String> visited = new ArrayList<>();
        int i = 1;
        while(!dq.isEmpty()){
            BasicNode n = dq.removeFirst();
            visited.add(n.getName());
            if(kraj.test(n.getName())){
                solutionString(n,i,"BFS");
                return Optional.of(n);
            }
            for(BasicNode bN : putanja.apply(n.getName())){
                if(!(visited.contains(bN))){
                    dq.addLast(new BasicNode(bN.getName(), n, bN.getDistance()));
                }
            }
            i++;
        }
        return Optional.empty();
    }
    static Optional<BasicNode> UCS(BasicNode poc, Function<String,Set<BasicNode>> putanje,Predicate<String> kraj){
        Queue<BasicNode> pq = new PriorityQueue<BasicNode>();
        pq.add(poc);
        int i = 1;
        while(!pq.isEmpty()){
            BasicNode n = pq.remove();
            if(kraj.test(n.getName())){
                solutionString(n,i,"UCS");
                return Optional.of(n);
            }
            for(BasicNode bN : putanje.apply(n.getName())){
                pq.add(new BasicNode(bN.getName(),n,n.getDistance() + bN.getDistance()));
            }
            i++;
        }
        return Optional.empty();
    }
    public static  Optional<HeuristicNode> aStarSearch(BasicNode poc,Function<String, Set<BasicNode>> putanje, Predicate<String> kraj,ToDoubleFunction<String> heuristic,boolean printSolution) {
        Queue<HeuristicNode> pq = new PriorityQueue<>(HeuristicNode.COMPARE_BY_TOTAL);
        pq.add(new HeuristicNode (poc.getName(), null, 0.0, heuristic.applyAsDouble(poc.getName())));
        int i = 1;
        while(!pq.isEmpty()) {
            HeuristicNode n = pq.remove();
            if(kraj.test(n.getName())){
                if(printSolution) solutionString(n,i,"A-STAR");
                return Optional.of(n);
            }
            for(BasicNode bN : putanje.apply(n.getName())){
                double sumDistance = n.getDistance() + bN.getDistance();
                double totalDistance = sumDistance + heuristic.applyAsDouble(bN.getName());
                pq.add(new HeuristicNode(bN.getName(),n,sumDistance,totalDistance));
            }
            i++;
        }
        return Optional.empty();
    }

    //funkcija ispisa

    public static void solutionString(BasicNode n, int i,String algorithm){
        StringBuilder sb = new StringBuilder();
        sb.append("# " + algorithm + "\n[FOUND_SOLUTION]: yes\n[STATES_VISITED]: ").append(i)
                .append("\n[PATH_LENGHT]: ").append(Optional.of(n).get().countPassedStates())
                .append("\n[TOTAL_COST]: ").append(Optional.of(n).get().getDistance())
                .append("\n[PATH]: ").append(Optional.of(n).get());
        System.out.println(sb.toString());
    }

}
