/*
ID: eddycyu
LANG: JAVA
TASK: milk
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class milk {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("milk.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("milk.out")))) {

            String line = f.readLine();
            String[] tokens = line.split(" ");
            int unitsToBuy = Integer.parseInt(tokens[0]);
            final int farmersCount = Integer.parseInt(tokens[1]);

            // get the farmers
            final List<Farmer> farmers = new ArrayList<>();
            for (int i = 0; i < farmersCount; i++) {
                line = f.readLine();
                tokens = line.split(" ");
                final int pricePerUnit = Integer.parseInt(tokens[0]);
                final int unitsToSell = Integer.parseInt(tokens[1]);
                farmers.add(new Farmer(pricePerUnit, unitsToSell));
            }

            // sort the farmers by price
            Collections.sort(farmers);

            // find the lowest cost for the desired quantity
            int totalCost = 0;
            for (Farmer farmer : farmers) {
                if (farmer.units <= unitsToBuy) {
                    // buy all available units and find more
                    totalCost += farmer.units * farmer.price;
                    unitsToBuy -= farmer.units;
                } else {
                    // buy some units and stop
                    totalCost += unitsToBuy * farmer.price;
                    break;
                }
            }

            // output cost
            out.println(totalCost);
        }
    }

    static class Farmer implements Comparable<Farmer> {
        int price = 0;
        int units = 0;

        Farmer(int price, int units) {
            super();
            this.price = price;
            this.units = units;
        }

        @Override
        public int compareTo(Farmer other) {
            return (this.price - other.price);
        }
    }
}