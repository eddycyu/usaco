/*
ID: eddycyu
LANG: JAVA
TASK: gift1
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class gift1 {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("gift1.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("gift1.out")))) {

            // parse the number of people in the group
            final int NP = Integer.parseInt(f.readLine());

            // parse the people in the group
            final List<Person> group = new ArrayList<>(NP);
            final Map<String, Person> groupMap = new HashMap<>();
            for (int i = 0; i < NP; i++) {
                final String name = f.readLine();
                final Person person = new Person(name);
                group.add(person);
                groupMap.put(name, person);
            }

            // parse the details for each person in the group
            for (int i = 0; i < NP; i++) {
                final Person person = groupMap.get(f.readLine());
                final String line = f.readLine();
                final String[] tokens = line.split(" ");
                person.amountToGive = Integer.parseInt(tokens[0]);
                final int numberOfFriends = Integer.parseInt(tokens[1]);
                for (int j = 0; j < numberOfFriends; j++) {
                    person.addFriend(groupMap.get(f.readLine()));
                }
            }

            // exchange gifts
            for (Person person : group) {
                if (person.getNumberOfFriends() > 0) {
                    // distribute gifts to friends
                    final int giftAmount = person.amountToGive / person.getNumberOfFriends();
                    final int remainder = person.amountToGive % person.getNumberOfFriends();
                    person.distributeGifts(giftAmount);
                    person.addAccount(remainder);
                } else {
                    // no gifts to give; keep the money
                    person.addAccount(person.amountToGive);
                }
            }

            // output results
            for (Person person : group) {
                out.print(person.name);
                out.print(" ");
                out.println(person.account - person.amountToGive);
            }
        }
    }

    public static class Person {
        String name;
        int account;
        int amountToGive;
        final private List<Person> friends;

        private Person() {
            super();
            friends = new ArrayList<>();
        }

        Person(String name) {
            this();
            this.name = name;
        }

        void addAccount(int amount) {
            account += amount;
        }

        void addFriend(Person person) {
            friends.add(person);
        }

        int getNumberOfFriends() {
            return friends.size();
        }

        void distributeGifts(int giftAmount) {
            for (Person friend : friends) {
                friend.addAccount(giftAmount);
            }
        }
    }
}
