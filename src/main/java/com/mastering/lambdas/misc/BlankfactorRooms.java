package com.mastering.lambdas.misc;

/*
You are with your friends in a castle, where there are multiple rooms named after flowers. Some of the rooms contain treasures - we call them the treasure rooms.

Each room contains a single instruction that tells you which room to go to next.

 *** instructions_1 ***

 lily ---------     daisy  sunflower
               |       |     |
               v       v     v
 jasmin ->  tulip      violet    -> rose --->
            ^    |      ^             ^     |
            |    |      |             |     |
            ------    iris            -------

This is given as a list of pairs of (source_room, destination_room)

instructions_1 = [
    ["jasmin", "tulip"],
    ["lily", "tulip"],
    ["tulip", "tulip"],
    ["rose", "rose"],
    ["violet", "rose"],
    ["sunflower", "violet"],
    ["daisy", "violet"],
    ["iris", "violet"]
]

Write a function that takes two parameters as input:
* a list containing the treasure rooms, and
* a list of instructions represented as pairs of (source_room, destination_room)

and returns a collection of all the rooms that satisfy the following two conditions:

* at least two *other* rooms have instructions pointing to this room
* this room's instruction immediately points to a treasure room

treasure_rooms_1 = ["lily", "tulip", "violet", "rose"]

filter_rooms(treasure_rooms_1, instructions_1) => ["tulip", "violet"]
* tulip can be accessed from rooms lily and jasmin. Tulip's instruction points to a treasure room (tulip itself)
* violet can be accessed from daisy, sunflower and iris. Violet's instruction points to a treasure room (rose)

Additional inputs

treasure_rooms_2 = ["lily", "jasmin", "violet"]

filter_rooms(treasure_rooms_2, instructions_1) => []
* none of the rooms reachable from tulip or violet are treasure rooms

 *** instructions_2 ***

 lily --------             ------
               |          |      |
               v          v      |
 jasmin ->  tulip -- > violet ---^

instructions_2 = [
    ["jasmin", "tulip"],
    ["lily", "tulip"],
    ["tulip", "violet"],
    ["violet", "violet"]
]

treasure_rooms_3 = ["violet"]

filter_rooms(treasure_rooms_3, instructions_2) => [tulip]
* tulip can be accessed from rooms lily and jasmin. Tulip's instruction points to a treasure room (violet)

All the test cases:
filter_rooms(treasure_rooms_1, instructions_1)    => ["tulip", "violet"]
filter_rooms(treasure_rooms_2, instructions_1)    => []
filter_rooms(treasure_rooms_3, instructions_2)    => [tulip]

Complexity Analysis variables:
T: number of treasure rooms
I: number of instructions given
*/

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class BlankfactorRooms {
    public static void main(String[] argv) {
        String[][] instructions_1 = {
                {"jasmin", "tulip"},
                {"lily", "tulip"},
                {"tulip", "tulip"},
                {"rose", "rose"},
                {"violet", "rose"},
                {"sunflower", "violet"},
                {"daisy", "violet"},
                {"iris", "violet"}
        };

        String[][] instructions_2 = {
                {"jasmin", "tulip"},
                {"lily", "tulip"},
                {"tulip", "violet"},
                {"violet", "violet"}
        };

        String[] treasure_rooms_1 = {"lily", "tulip", "violet", "rose"};
        String[] treasure_rooms_2 = {"lily", "jasmin", "violet"};
        String[] treasure_rooms_3 = {"violet"};

        getTreasureRooms(treasure_rooms_1, instructions_1);
        testLoop(instructions_1);
        getTreasureRoomsOptimized(treasure_rooms_1, instructions_1);
        getTreasureRoomsWithPairsClass(treasure_rooms_1, instructions_1);
        buildTwoSets(new int[]{1, 2, 3}, new int[]{3, 4});
    }

    private static List<String> getTreasureRoomsWithPairsClass(String[] treasureRooms1, String[][] instructions1) {
        List<String> treasureRooms = Arrays.stream(treasureRooms1).toList();
        List<Pair> allPairs = Arrays.stream(instructions1).map(i -> new Pair(i[0], i[1])).toList();
        List<Pair> resultPairs = new ArrayList<>();
        Map<String, Integer> pairsMap = new HashMap<>();

        allPairs.stream()
                .filter(p -> treasureRooms.contains(p.getDestination()))
                .forEach(p -> {
                    allPairs.stream()
                            .filter(pair -> pair.getDestination().equals(p.getSource()) && !Objects.equals(pair.getSource(), pair.getDestination()))
                            .forEach(resultPairs::add);
                });

        resultPairs.forEach(pair -> {
            pairsMap.merge(pair.getDestination(), 1, Integer::sum);
        });

        List<String> result = pairsMap.entrySet().stream()
                .filter(e -> e.getValue() >= 2)
                .map(Map.Entry::getKey)
                .sorted(String::compareTo)
                .toList();

        System.out.println("With Pairs: " + result);

        return result;
    }

    private static Set<String> getTreasureRooms(String[] treasureRooms, String[][] instructions) {
        Map<String, Integer> matches = new HashMap<>();
        Map<String, String> sourceDestinationPairs = new HashMap<>();
        List<String> destinations = new ArrayList<>(instructions.length);

        IntStream.range(0, instructions.length)
                .forEach(i -> {
                    sourceDestinationPairs.put(instructions[i][0], instructions[i][1]);
                    if (!Objects.equals(instructions[i][0], instructions[i][1])) {
                        destinations.add(instructions[i][1]);
                    }
                });

        Map<String, Integer> destinationsWithTwoOrMoreSources = destinations.stream()
                .collect(Collectors.groupingBy(k -> k, () -> matches,
                        Collectors.summingInt(e -> 1)))
                .entrySet()
                .stream()
                .filter(e -> e.getValue() >= 2)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Set<String> finalResult = new TreeSet<>();

        IntStream.range(0, treasureRooms.length).forEach(i -> {
            destinationsWithTwoOrMoreSources.forEach((k, v) -> {
                if (sourceDestinationPairs.containsKey(k) && sourceDestinationPairs.get(k).equals(treasureRooms[i])) {
                    finalResult.add(k);
                }
            });
        });

        System.out.println(finalResult);

        return finalResult;
    }

    private static Set<String> getTreasureRoomsOptimized(String[] treasureRooms, String[][] instructions) {
        List<String> treasures = new ArrayList<>(Arrays.stream(treasureRooms).toList());
        //treasures.removeIf(s -> s.equalsIgnoreCase("tulip"));
        List<String> sourceRoomsPointingToRoomsWithTreasure = new ArrayList<>();
        Map<String, Integer> resultMap = new HashMap<>();

        IntStream.range(0, instructions.length).forEach(i -> {
            String source = instructions[i][0];
            String destination = instructions[i][1];
            if (treasures.contains(destination)) {
                sourceRoomsPointingToRoomsWithTreasure.add(source);
            }
        });

        IntStream.range(0, instructions.length).forEach(i -> {
            String source = instructions[i][0];
            String destination = instructions[i][1];
            if (sourceRoomsPointingToRoomsWithTreasure.contains(destination) && !source.equals(destination)) {
                resultMap.merge(destination, 1, Integer::sum);
            }
        });

        Set<String> finalResult = resultMap
                .entrySet()
                .stream()
                .filter(e -> e.getValue() >= 2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(TreeSet::new));
/*        IntStream.range(0, instructions.length).forEach(i -> {
            //resultMap.computeIfPresent(instructions[i][1], (k, v) -> v + 1);
            //resultMap.computeIfAbsent(instructions[i][1], k -> 1);
            //resultMap.compute(instructions[i][1], (k, v) -> v == null ? 1 : v + 1);
            if (!Objects.equals(instructions[i][0], instructions[i][1])) {
                resultMap.merge(instructions[i][1], 1, Integer::sum);
            }
        });*/
        System.out.println("Optimized Result Set is: " + finalResult);

        return finalResult;
    }

/*    public static List<String> getTreasureRooms(String[] rooms, String[][] pairs) {
        List<String> result = new ArrayList();
        int counter = 0;
        String currentRoom = "";

        for(int i = 0; i < pairs.length; i++) {
            currentRoom = rooms[i];
            if(isRoomAtLeastTwiceIn(currentRoom, pairs)) {
                result.add(currentRoom);
            }
        }

        return result;
    }*/

/*    public static boolean isRoomAtLeastTwiceIn(String room, String[][] pairs) {
        int counter = 0;
        for(int i = 0; i < pairs.length; i++) {
            for(int j = i; j < pairs.length; j++) {
                if(room.equals(pairs[i][j])) {
                    counter++;
                }
            }
        }
        if(counter >= 2) {
            return true;
        } else {
            return false;
        }
    }*/

    //Iterate over 2-dimensional(two-dimensional) Array with Java 8 using Streams and forEach:
/*  Arrays.stream(names).forEach((i) -> {
        Arrays.stream(i).forEach((j) -> System.out.print(j + " "));
        System.out.println();
    });
           or

           for (String[] name : names) {
                for (String nameStr : name) {

                }
           }

           or the old way with for loop:
        for (String[] instruction : instructions) {
            sourceDestinationPairs.put(instruction[0], instruction[1]);
            if (!Objects.equals(instruction[0], instruction[1])) {
                destinations.add(instruction[1]);
            }
        }
    */

    public static void testLoop(String[][] instructions_1) {
        String collectedInstructions = Arrays.stream(instructions_1)
                .flatMap(Arrays::stream)
                .collect(Collectors.joining(", "));
        System.out.println(collectedInstructions);
    }

    public static List<List<Integer>> buildTwoSets(int[] arr1, int[] arr2) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.stream(arr1).forEach(e1 -> {
            Arrays.stream(arr2).forEach(e2 -> {
                result.add(Arrays.asList(e1, e2));
            });
        });

        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);
        List<int[]> pairs =
                numbers1.stream()
                        .flatMap(i -> numbers2.stream()
                                .map(j -> new int[]{i, j})
                        )
                        .collect(toList());

        pairs.forEach(arr -> {
            System.out.println("[" + arr[0] + ", " + arr[1] + "]");
        });

        System.out.println("---------------------------");
        result.forEach(System.out::println);
        return result;
    }
}

class Pair {
    private String source;
    private String destination;

    public Pair(String source, String destination) {
        this.source = source;
        this.destination = destination;
    }

    public String getSource() {
        return this.source;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}