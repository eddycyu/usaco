/*
ID: eddycyu
LANG: JAVA
TASK: milk2
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class milk2 {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("milk2.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("milk2.out")))) {

            // number of farmers/cow
            final int N = Integer.parseInt(f.readLine());

            // beginning and ending times
            final ArrayList<Node> cowList = new ArrayList<>();
            for (int i = 0; i < N; i++) {
                final String input = f.readLine();
                final String[] tokens = input.split(" ");
                cowList.add(new Node(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1])));
            }

            // sort in ascending order by beginning time
            Collections.sort(cowList);

            // find segments (combine overlapping beginning/ending times into same segment)
            final ArrayList<Node> segmentList = new ArrayList<>();
            if (cowList.size() == 1) {
                segmentList.add(cowList.get(0));
            } else {
                int earliest = cowList.get(0).begin;
                int latest = cowList.get(0).end;
                for (int i = 1; i < cowList.size(); i++) {
                    final Node current = cowList.get(i);
                    if (current.begin > latest) {
                        segmentList.add(new Node(earliest, latest));
                        earliest = current.begin;
                        latest = current.end;
                    }
                    if (current.end > latest) {
                        latest = current.end;
                    }
                }
                segmentList.add(new Node(earliest, latest));
            }

            // find longest milking time (e.g. longest segment)
            int longestMilkingTime = 0;
            for (Node current : segmentList) {
                final int length = current.end - current.begin;
                if (length > longestMilkingTime) {
                    longestMilkingTime = length;
                }
            }

            // find longest non-milking time
            // (e.g. longest distance between adjacent segments)
            int longestNonMilkingTime = 0;
            if (segmentList.size() > 0) {
                Node previous = segmentList.get(0);
                for (int i = 1; i < segmentList.size(); i++) {
                    final Node current = segmentList.get(i);
                    final int length = current.begin - previous.end;
                    if (length > longestNonMilkingTime) {
                        longestNonMilkingTime = length;
                    }
                    previous = current;
                }
            }

            // output the answer
            final String answer = longestMilkingTime + " " + longestNonMilkingTime;
            out.println(answer);
        }
    }

    public static class Node implements Comparable<Node> {
        int begin;
        int end;

        Node(int begin, int end) {
            super();
            this.begin = begin;
            this.end = end;
        }

        @Override
        public String toString() {
            return ("[" + this.begin + "," + this.end + "]");
        }

        @Override
        public int compareTo(Node o) {
            if (begin < o.begin) {
                return -1;
            } else if (begin > o.begin) {
                return 1;
            }
            return 0;
        }
    }
}
