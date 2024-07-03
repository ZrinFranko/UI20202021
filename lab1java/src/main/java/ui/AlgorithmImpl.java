package ui;

import java.io.File;
import java.util.*;

public class AlgorithmImpl {

    private final Algorithm alg;
    private List<Node> nodes;
    private Map<String, Double> heuristics;
    private int startIndex;
    private Optional<Node> finish;
    private int iterationNumber;
    private String heuristicsUsed;
    private String checkingFlag;

    public AlgorithmImpl(String[] info) {
        alg = Util.findTheAlgorithm(info[3]);
        nodes = Util.generateNodes(new File(info[1]));
        findIndexes();
        if (info.length == 6) {
            heuristics = Util.aquireHeuristics(new File(info[5]));
            this.heuristicsUsed = info[5].substring(info[5].lastIndexOf("/") + 1);
        } else
            heuristics = null;
    }

    public AlgorithmImpl(String states, String heuristic, String flag) {
        this.alg = Algorithm.A_STAR;
        nodes = Util.generateNodes(new File(states.trim()));
        findIndexes();
        heuristics = Util.aquireHeuristics(new File(heuristic.trim()));
        this.heuristicsUsed = heuristic.substring(heuristic.lastIndexOf("/") + 1);
        this.checkingFlag = flag.trim();
    }

    /**
     * Function that solves the problem using a given algorithm
     *
     * @return The text defining the output of the algorithm
     */
    public String solve() {
        StringBuilder sb = new StringBuilder();
        sb.append("# ");
        this.iterationNumber = 0;
        switch (alg) {
            case BFS:
                doBFS();
                sb.append("BFS\n");
                break;
            case UCS:
                doUCS();
                sb.append("UCS\n");
                break;
            case A_STAR:
                doAStar();
                sb.append("A-STAR ").append(this.heuristicsUsed).append("\n");
                break;
            default:
                throw new IllegalStateException();
        }
        sb.append("[FOUND_SOLUTION]: ")
                .append(finish.isPresent() ? "yes" : "no")
                .append("\n").append("[STATES_VISITED]: ")
                .append(this.iterationNumber).append("\n")
                .append("[PATH_LENGTH]: ")
                .append(finish.get().sizeOfAncestry()).append("\n")
                .append("[TOTAL_COST]: ")
                .append(finish.get().distanceToMe()).append("\n")
                .append("[PATH]: ").append(finish.get().pathToMe());
        return sb.toString();
    }

    public String check() {
        StringBuilder sb = new StringBuilder();
        sb.append("# ");
        switch (checkingFlag) {
            case "--check-optimistic":
                sb.append(checkOptimistic());
                break;
            case "--check-consistent":
                sb.append(checkConsistent());
                break;
            default:
                throw new IllegalStateException();
        }
        return sb.toString();
    }

    private String checkConsistent() {
        StringBuilder sb = new StringBuilder();
        sb.append("HEURISTIC-CONSISTENT ").append(this.heuristicsUsed).append("\n");
        boolean passed = true;
        for (Node n : nodes) {
            if (n.getChildren() != null) {
                for (String child : n.getChildren().keySet()) {
                    sb.append("[CONDITION]: ");
                    if (this.heuristics.get(n.getName()) <= n.getChildren().get(child) + this.heuristics.get(child)) {
                        sb.append("[OK] ");
                    } else {
                        sb.append("[ERR] ");
                        passed = false;
                    }
                    sb.append("h(").append(n.getName()).append(") <= h(").append(child).append(") + c: ").append(this.heuristics.get(n.getName())).append(" <= ").append(heuristics.get(child)).append(" + ").append(n.getChildren().get(child)).append("\n");
                }
            }
        }
        sb.append("[CONCLUSION]: ").append(passed ? "Heuristic is consistent." : "Heuristic is not consistent.");
        return sb.toString();

    }

    private String checkOptimistic() {
        StringBuilder sb = new StringBuilder();
        sb.append("HEURISTIC-OPTIMISTIC ").append(this.heuristicsUsed).append("\n");
        boolean passed = true;
        for (int i = 0; i < nodes.size(); i++) {
            this.startIndex = i;
            doAStar();
            if (this.finish.isPresent()) {
                sb.append("[CONDITION]: ");
                if (finish.get().distanceToMe() >= heuristics.get(nodes.get(i).getName()))
                    sb.append("[OK]");
                else {
                    sb.append("[ERR]");
                    passed = false;
                }
                sb.append(" h(").append(nodes.get(i).getName()).append(") <= h*: ").append(this.heuristics.get(nodes.get(i).getName())).append(" <= ").append(this.finish.get().distanceToMe()).append("\n");
            }
        }
        sb.append("[CONCLUSION]: ").append(passed ? "Heuristic is optimistic." : "Heuristic is not optimistic.");
        return sb.toString();

    }

    /**
     * Private function that implements the BFS algorithm
     */
    private void doBFS() {
        Deque<Node> open = new LinkedList<>();
        open.add(nodes.get(startIndex));
        while (!open.isEmpty()) {
            this.iterationNumber++;
            Node s = open.removeFirst();
            if (Util.getEndNodes().contains(s.getName())) {
                this.finish = Optional.of(s);
                return;
            }
            for (String c : s.getChildren().keySet()) {
                open.addLast(new Node(c, findGranchildren(c), s));
            }
        }
        this.finish = Optional.empty();
    }

    /**
     * Private function that implements the UCS algorithm
     */
    private void doUCS() {
        Queue<CostNode> open = new PriorityQueue<>();
        List<CostNode> visited = new ArrayList<>();
        Node start = nodes.get(startIndex);
        open.add(new CostNode(start.getName(), start.getChildren(), null, 0.0));
        while (!open.isEmpty()) {
            this.iterationNumber++;
            CostNode s = open.remove();
            if (Util.getEndNodes().contains(s.getName())) {
                this.finish = Optional.of(s);
                return;
            }
            for (String c : s.getChildren().keySet()) {
                open.add(new CostNode(c, findGranchildren(c), s, s.getCost() + s.getChildren().get(c)));
            }
            visited.add(s);
            open.removeAll(visited);
        }
        this.finish = Optional.empty();
    }

    /**
     * Private function that implements the A* algorithm
     */
    private void doAStar() {
        Queue<HeuristicNode> open = new PriorityQueue<>(HeuristicNode.COMPARE_BY_TOTAL);
        List<HeuristicNode> visited = new ArrayList<>();
        Node start = nodes.get(startIndex);
        open.add(new HeuristicNode(start.getName(), start.getChildren(), null, 0.0, heuristics.get(start.getName())));
        while (!open.isEmpty()) {
            this.iterationNumber++;
            HeuristicNode s = open.remove();
            if (Util.getEndNodes().contains(s.getName())) {
                this.finish = Optional.of(s);
                return;
            }
            for (String c : s.getChildren().keySet()) {
                open.add(new HeuristicNode(c, findGranchildren(c), s, s.getCost(), heuristics.get(c)));
            }
            visited.add(s);
            open.removeAll(visited);
        }
        this.finish = Optional.empty();
    }

    /**
     * Helper method that finds the index of the starting point from the list of nodes
     */
    private void findIndexes() {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getName().equals(Util.getStartNode())) {
                startIndex = i;
                break;
            }
        }
    }

    /**
     * Function that finds the children of the child from the list of nodes
     *
     * @param childName the name of the child whose children we need to find
     * @return A map of children if the node exists or null if it doesn't
     */
    private Map<String, Double> findGranchildren(String childName) {
        for (Node n : nodes) {
            if (n.getName().equals(childName))
                return n.getChildren();
        }
        return null;
    }


}
