import java.util.ArrayList;
import java.util.List;

public class ListExample {

    public static void main(String[] args) {
        List<String> myList = new ArrayList<String>();
        myList.add("one");
        myList.add("two");
        myList.add("three");
        myList.add("four");
        myList.add("five");

        System.out.println("Inserted in 'order': ");
        printList(myList);
        System.out.println("\n");
        System.out.println("Inserted out of 'order': ");

        // Clear the list
        myList.clear();

        myList.add("four");
        myList.add("five");
        myList.add("one");
        myList.add("two");
        myList.add("three");

        printList(myList);
    }

    private static void printList(List<String> myList) {
        for (String string : myList) {
            System.out.println(string);
        }
    }
}