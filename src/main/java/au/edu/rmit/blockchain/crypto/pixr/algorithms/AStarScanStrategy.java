package au.edu.rmit.blockchain.crypto.pixr.algorithms;

import au.edu.rmit.blockchain.crypto.common.utils.Util;
import au.edu.rmit.blockchain.crypto.pixr.algorithms.results.PIXRDistinctVectorFinderResult;
import com.google.common.collect.ImmutableList;

import java.util.*;

public class AStarScanStrategy implements PIXRDistinctVectorStrategy {

    @Override
    public PIXRDistinctVectorFinderResult run(List<String> txBinaryList, int hashLength) throws NotMatchException {
        List<State> open = createOpenStates(hashLength, txBinaryList);
        while (!open.isEmpty()) {
            State best = open.stream().min(Comparator.comparing(State::getCost)).get();
            System.out.println("+ Cost: " + best.cost);
            System.out.println("+ Bit indexes: " + best.accumulatedIdxs);
            System.out.println("+ Subgroups: ");
            for (String s : best.subGroups.keySet()) {
                System.out.println("\t\t Vector " + s + ": " + best.subGroups.get(s));
            }
            if (best.subGroups.size() == txBinaryList.size()) {
                return new PIXRDistinctVectorFinderResult(txBinaryList.size(), hashLength, best.accumulatedIdxs, best.subGroups.keySet().stream().collect(ImmutableList.toImmutableList()));
            }
            List<State> children = createChildStates(hashLength, best, txBinaryList);
            open.remove(best);
            open.addAll(children);
        }
        throw new NotMatchException("Cannot find distinct vectors");
    }

    /**
     * Init open state
     *
     * @param size         hash length
     * @param txBinaryList transactions
     * @return open states
     */
    private List<State> createOpenStates(int size, List<String> txBinaryList) {
        List<State> states = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            State s = new State();
            s.idx = i;
            s.accumulatedIdxs = List.of(i);
            s.subGroups = new HashMap<>();
            grouping(s, txBinaryList);
            s.cost = gCost(s) + hCost(s);
            states.add(s);
        }
        return states;
    }

    /**
     * Create descendant of a state
     *
     * @param size         hash length
     * @param parent       parent state
     * @param txBinaryList transaction list
     * @return descendant states
     */
    private List<State> createChildStates(int size, State parent, List<String> txBinaryList) {
        List<State> states = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (!parent.accumulatedIdxs.contains(i)) {
                State s = new State();
                s.idx = i;
                s.accumulatedIdxs = new ArrayList<>(parent.accumulatedIdxs);
                s.accumulatedIdxs.add(i);
                s.subGroups = new HashMap<>();
                grouping(s, txBinaryList);
                s.cost = gCost(s) + hCost(s);
                states.add(s);
            }
        }
        return states;
    }


    /**
     * Split all transaction into groups which has the same identified vector
     *
     * @param s   state
     * @param txs list of transaction
     */
    private void grouping(State s, List<String> txs) {
        for (String tx : txs) {
            String vector = extractBits(s.accumulatedIdxs, tx);
            int groups = s.subGroups.computeIfAbsent(vector, k -> 0);
            s.subGroups.put(vector, groups + 1);
        }
    }

    /**
     * Extract vector from hashcode according to list of index
     *
     * @param idx list of index
     * @param tx  transaction hash code
     * @return vector
     */
    private String extractBits(List<Integer> idx, String tx) {
        StringBuilder builder = new StringBuilder();
        for (int i : idx) {
            builder.append(tx.charAt(i));
        }
        return builder.toString();
    }

    /**
     * Cost from start state to current state
     *
     * @param s current state
     * @return cost
     */
    private double gCost(State s) {
        return s.accumulatedIdxs.size();
    }

    /**
     * Cost from current state to end state
     *
     * @param s current state
     * @return cost
     */
    private double hCost(State s) {
        long max = s.subGroups.values().stream()
                .mapToInt(v -> v)
                .max()
                .orElse(0);
        return Util.log2D(max);
    }

    /**
     * State class
     */
    static class State {
        int idx;
        Map<String, Integer> subGroups;
        double cost;
        List<Integer> accumulatedIdxs;

        public double getCost() {
            return cost;
        }
    }
}
