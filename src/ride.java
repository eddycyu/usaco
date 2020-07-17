/*
ID: eddycyu
LANG: JAVA
TASK: ride
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ride {

    private int convert(char letter) {
        return (letter - 'A') + 1;
    }

    private int translate(String name) {
        int finalNumber = 1;
        final char[] letters = name.toCharArray();
        for (char letter : letters) {
            finalNumber *= convert(letter);
        }
        return finalNumber;
    }

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("ride.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("ride.out")))) {

            final String cometName = f.readLine();
            final String groupName = f.readLine();

            // instantiate app
            final ride app = new ride();

            // convert names to numbers
            final int cometNumber = app.translate(cometName);
            final int groupNumber = app.translate(groupName);

            // GO or STAY?
            if ((cometNumber % 47) == (groupNumber % 47)) {
                out.println("GO");
            } else {
                out.println("STAY");
            }
        }
    }
}
