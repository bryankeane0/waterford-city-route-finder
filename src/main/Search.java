package main;
import java.util.*;

public class Search {

    public static CostedPath dijkstra(Node<?> startNode, Node<?> destNode) {
        CostedPath cp = new CostedPath();
        List<Node<?>> encountered = new ArrayList<>(), unencountered = new ArrayList<>();
        startNode.setNodeValue(0);
        unencountered.add(startNode);
        Node<?> currentNode;
        do {
            currentNode = unencountered.remove(0);
            encountered.add(currentNode);
            if (currentNode==destNode) {
                cp.pathList.add(currentNode);
                cp.pathCost = currentNode.getNodeValue();
                while(currentNode!=startNode) {
                    boolean foundPrevPathNode = false;
                    for (Node<?> node : encountered)
                        for (Link e : node.getAdjListLink())
                            if (e.getDestNode() == currentNode && currentNode.getNodeValue()-e.getCost() == node.getNodeValue()) {
                                cp.pathList.add(0,node);
                                currentNode = node;
                                foundPrevPathNode = true;
                                break;
                            }
                    if (foundPrevPathNode) break;
                }
                for (Node<?> node : encountered) node.setNodeValue(Integer.MAX_VALUE);
                for (Node<?> node : unencountered) node.setNodeValue(Integer.MAX_VALUE);
                return cp;
            }
            for (Link link : currentNode.getAdjListLink())
                if (!encountered.contains(link.getDestNode())) {
                    link.getDestNode().setNodeValue(Integer.min(link.getDestNode().getNodeValue(), currentNode.getNodeValue() + link.getCost()));
                    unencountered.add(link.getDestNode());
                }
            unencountered.sort(Comparator.comparingInt(Node::getNodeValue));
        } while (!unencountered.isEmpty());
        return cp;
    }

    public static <T> List<Node<?>> bfs(Node<?> startNode, T lookingfor) {
        List<List<Node<?>>> agenda = new ArrayList<>();
        List<Node<?>> firstAgendaPath = new ArrayList<>(),resultPath;
        firstAgendaPath.add(startNode);
        agenda.add(firstAgendaPath);
        resultPath = findPathBreadthFirst(agenda,null,lookingfor);
        Collections.reverse(Objects.requireNonNull(resultPath));
        return resultPath;
    }

    public static <T> List<Node<?>> findPathBreadthFirst(List<List<Node<?>>> agenda, List<Node<?>> encountered, T lookingfor) {
        if (agenda.isEmpty())
            return null;
        List<Node<?>> nextPath = agenda.remove(0);
        Node<?> currentNode=nextPath.get(0);
        if (currentNode.getData().equals(lookingfor))
            return nextPath;
        if (encountered == null)
            encountered = new ArrayList<>();
        encountered.add(currentNode);
        for (Node<?> node : currentNode.getAdjList())
            if (!encountered.contains(node)) {
                List<Node<?>> newPath = new ArrayList<>(nextPath);
                newPath.add(0, node);
                agenda.add(newPath);
            }
        return findPathBreadthFirst(agenda, encountered, lookingfor);
    }

}
