package com.mastering.lambdas.misc;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WorkingWithCollections {

    private static boolean isLongTweet(Function<String, Boolean> isLongTweet, String tweet) {
        return isLongTweet.apply(tweet);
    }

    public static void main(String[] args) {

        Function<String, Boolean> isLongTweet = (String s) -> s.length() > 60;
        String tweet = "A very short tweet more than 60 characters";
        System.out.println("It it long: " + isLongTweet(isLongTweet, tweet));

        List<Integer> numbers = Arrays.asList(2, 3, 4, 5);
        List<Integer> result =
                numbers.stream()
                        .peek(x -> System.out.println("from stream: " + x))
                        .map(x -> x + 17)
                        .peek(x -> System.out.println("after map: " + x))
                        .filter(x -> x % 2 == 0)
                        .peek(x -> System.out.println("after filter: " + x))
                        .limit(3)
                        .peek(x -> System.out.println("after limit: " + x))
                        .collect(Collectors.toList());

        Runnable r1 = () -> {
            int a = 2;
            System.out.println("The value is: " + a);
        };
        new Thread(r1).start();
        Map<String, Integer> movies = new HashMap<>();
        movies.put("JamesBond", 20);
        movies.put("Matrix", 15);
        movies.put("Harry Potter", 5);
  /*      Iterator<Map.Entry<String, Integer>> iterator =
                movies.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            if(entry.getValue() < 10) {
                iterator.remove();
            }
        }*/
        movies.entrySet().removeIf(entry -> entry.getValue() < 10);
        System.out.println(movies);

        Map<String, Long> moviesToCount = new HashMap<>();
        String movieName = "JamesBond";
        moviesToCount.put(movieName, 1L);
/*        Long count = moviesToCount.get(movieName);
        if(count == null) {
            moviesToCount.put(movieName, 1L);
        }
        else {
            moviesToCount.put(movieName, count + 1L);
        }*/

        moviesToCount.merge(movieName, 1L, (count, incr) -> count + incr);
        moviesToCount.merge(movieName, 1L, Long::sum);
        moviesToCount.merge(movieName, 1L, Long::sum);
        moviesToCount.merge(movieName, 1L, Long::sum);

        System.out.println("Movies to count: " + moviesToCount);

        workingWithLists();
        workingWithMaps();
        computingOnMaps();
        removingFromMaps();
        replacingInMaps();
        mergingMaps();
    }

    private static void workingWithLists() {
        System.out.println("------ Working with Lists ------");

        System.out.println("--> Transforming list items with a Stream");
        List<String> referenceCodes = Arrays.asList("a12", "C14", "b13");
        referenceCodes.stream()
                .map(code -> Character.toUpperCase(code.charAt(0)) + code.substring(1))
                .collect(Collectors.toList())
                .forEach(System.out::println);
        System.out.println("... but the original List remains unchanged: " + referenceCodes);

        System.out.println("--> Mutating a list with a ListIterator");
        for (ListIterator<String> iterator = referenceCodes.listIterator(); iterator.hasNext(); ) {
            String code = iterator.next();
            iterator.set(Character.toUpperCase(code.charAt(0)) + code.substring(1));
        }
        System.out.println("This time it's been changed: " + referenceCodes);

        System.out.println("--> Mutating a list with replaceAll()");
        referenceCodes = Arrays.asList("a12", "C14", "b13");
        System.out.println("Back to the original: " + referenceCodes);
        referenceCodes.replaceAll(code -> Character.toUpperCase(code.charAt(0)) + code.substring(1));
        System.out.println("Changed by replaceAll(): " + referenceCodes);
    }

    private static void workingWithMaps() {
        System.out.println("------ Working with Maps ------");

        System.out.println("--> Iterating a map with a for loop");
        Map<String, Integer> ageOfFriends = Stream.of(
                        new AbstractMap.SimpleEntry<>("Raphael", 30),
                        new AbstractMap.SimpleEntry<>("Olivia", 25),
                        new AbstractMap.SimpleEntry<>("Thibaut", 26))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        ;
        for (Entry<String, Integer> entry : ageOfFriends.entrySet()) {
            String friend = entry.getKey();
            Integer age = entry.getValue();
            System.out.println(friend + " is " + age + " years old");
        }

        System.out.println("--> Iterating a map with forEach()");
        ageOfFriends.forEach((friend, age) -> System.out.println(friend + " is " + age + " years old"));

        System.out.println("--> Iterating a map sorted by keys through a Stream");

        Map<String, String> favouriteMovies = Stream.of(
                        new AbstractMap.SimpleEntry<>("Raphael", "Star Wars"),
                        new AbstractMap.SimpleEntry<>("Cristina", "Matrix"),
                        new AbstractMap.SimpleEntry<>("Olivia", "James Bond"))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        favouriteMovies.entrySet().stream()
                .sorted(Entry.comparingByKey())
                .forEachOrdered(System.out::println);

        System.out.println("--> Using getOrDefault()");
        System.out.println(favouriteMovies.getOrDefault("Olivia", "Matrix"));
        System.out.println(favouriteMovies.getOrDefault("Thibaut", "Matrix"));
    }

    private static void computingOnMaps() {
        Map<String, List<String>> friendsToMovies = new HashMap<>();

        System.out.println("--> Adding a friend and movie in a verbose way");
        String friend = "Raphael";
        List<String> movies = friendsToMovies.get(friend);
        if (movies == null) {
            movies = new ArrayList<>();
            friendsToMovies.put(friend, movies);
        }
        movies.add("Star Wars");
        System.out.println(friendsToMovies);

        System.out.println("--> Adding a friend and movie using computeIfAbsent()");
        friendsToMovies.clear();
        friendsToMovies.computeIfAbsent("Raphael", name -> new ArrayList<>())
                .add("Star Wars");
        System.out.println(friendsToMovies);
    }

    private static void removingFromMaps() {
        // Mutable Map required here!
        Map<String, String> favouriteMovies = new HashMap<>();
        favouriteMovies.put("Raphael", "Jack Reacher 2");
        favouriteMovies.put("Cristina", "Matrix");
        favouriteMovies.put("Olivia", "James Bond");
        String key = "Raphael";
        String value = "Jack Reacher 2";

        System.out.println("--> Removing an unwanted entry the cumbersome way");
        boolean result = remove(favouriteMovies, key, value);
        System.out.printf("%s [%b]%n", favouriteMovies, result);

        // Put back the deleted entry for the second test
        favouriteMovies.put("Raphael", "Jack Reacher 2");

        System.out.println("--> Removing an unwanted the easy way");
        favouriteMovies.remove(key, value);
        System.out.printf("%s [%b]%n", favouriteMovies, result);
    }

    private static <K, V> boolean remove(Map<K, V> favouriteMovies, K key, V value) {
        if (favouriteMovies.containsKey(key) && Objects.equals(favouriteMovies.get(key), value)) {
            favouriteMovies.remove(key);
            return true;
        }
        return false;
    }

    private static void replacingInMaps() {
        Map<String, String> favouriteMovies = new HashMap<>();
        favouriteMovies.put("Raphael", "Star Wars");
        favouriteMovies.put("Olivia", "james bond");

        System.out.println("--> Replacing values in a map with replaceAll()");
        favouriteMovies.replaceAll((friend, movie) -> movie.toUpperCase());
        System.out.println(favouriteMovies);
    }

    private static void mergingMaps() {
        Map<String, String> family = Stream.of(
                        new AbstractMap.SimpleEntry<>("Teo", "Star Wars"),
                        new AbstractMap.SimpleEntry<>("Cristina", "James Bond"),
                        new AbstractMap.SimpleEntry<>("Olivia", "James Bond"))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        Map<String, String> friends = Stream.of(
                        new AbstractMap.SimpleEntry<>("Raphael", "Star Wars"),
                        new AbstractMap.SimpleEntry<>("Cristina", "James Bond"),
                        new AbstractMap.SimpleEntry<>("Olivia", "James Bond"))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        System.out.println("--> Merging the old way");
        Map<String, String> everyone = new HashMap<>(family);
        everyone.putAll(friends);
        System.out.println(everyone);

        Map<String, String> friends2 = Stream.of(
                        new AbstractMap.SimpleEntry<>("Raphael", "Star Wars"),
                        new AbstractMap.SimpleEntry<>("Cristina", "Matrix"))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        friends2.put("John", "Doe");

        System.out.println("--> Merging maps using merge()");
        Map<String, String> everyone2 = new HashMap<>(family);
        friends2.forEach((k, v) -> everyone2.merge(k, v, (movie1, movie2) -> movie1 + " & " + movie2));
        System.out.println(everyone2);
    }

}
