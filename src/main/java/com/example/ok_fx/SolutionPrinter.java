package com.example.demo;

/**
 * @author Heavely inspired by many different sources,
 * that said, sources are found in the gitlab README.
 */
import java.util.ArrayList;
import java.util.List;

public interface SolutionPrinter {
    void handleSolution(List<DancingLinks.DancingNode> solution, long start1) throws InterruptedException;

    void handleSolution2(List<DancingLinks2.DancingNode> answer, long start1) throws InterruptedException;

    void handleCloseToFinish(List<DancingLinks2.DancingNode> answer, long start1) throws InterruptedException;
}

class DefaultPrinter implements SolutionPrinter {
    public void handleSolution(List<DancingLinks.DancingNode> answer, long start1) throws InterruptedException {
        long end1 = System.currentTimeMillis();
//        System.out.println("Elapsed Time in milli seconds: " + (end1 - start1));

        ArrayList<String> the_answers = new ArrayList<>();
        // Takes each node and gets the column name
        // i get this string of names of columns and add it to the answers
        for (DancingLinks.DancingNode n : answer) {
            StringBuilder ret = new StringBuilder();
            ret.append(n.columnNode.name).append(" ");
            DancingLinks.DancingNode tmp = n.Right;
            while (tmp != n) {
                ret.append(tmp.columnNode.name).append(" ");
                tmp = tmp.Right;
            }
            the_answers.add(ret.toString());
        }
        // push the answers here to digest them
        DLX3D.ReturnPentominoesUsed(the_answers);
    }

    @Override
    public void handleSolution2(List<DancingLinks2.DancingNode> answer, long start1) throws InterruptedException {
        long end1 = System.currentTimeMillis();
//        System.out.println("Elapsed Time in milli seconds: " + (end1 - start1));

        ArrayList<String> the_answers = new ArrayList<>();
        // Takes each node and gets the column name
        // i get this string of names of columns and add it to the answers
        for (DancingLinks2.DancingNode n : answer) {
            StringBuilder ret = new StringBuilder();
            ret.append(n.columnNode.name).append(" ");
            DancingLinks2.DancingNode tmp = n.Right;
            while (tmp != n) {
                ret.append(tmp.columnNode.name).append(" ");
                tmp = tmp.Right;
            }
            the_answers.add(ret.toString());
        }
        // push the answers here to digest them
        DLX3D.ReturnPentominoesUsed2(the_answers);
    }

    @Override
    public void handleCloseToFinish(List<DancingLinks2.DancingNode> answer, long start1) throws InterruptedException {
        long end1 = System.currentTimeMillis();
//        System.out.println("Elapsed Time in milli seconds: " + (end1 - start1));

        ArrayList<String> the_answers = new ArrayList<>();
        // Takes each node and gets the column name
        // i get this string of names of columns and add it to the answers
        for (DancingLinks2.DancingNode n : answer) {
            StringBuilder ret = new StringBuilder();
            ret.append(n.columnNode.name).append(" ");
            DancingLinks2.DancingNode tmp = n.Right;
            while (tmp != n) {
                ret.append(tmp.columnNode.name).append(" ");
                tmp = tmp.Right;
            }
            the_answers.add(ret.toString());
        }
        // push the answers here to digest them
        DLX3D.ReturnPentominoesUsed2(the_answers);
    }
}
