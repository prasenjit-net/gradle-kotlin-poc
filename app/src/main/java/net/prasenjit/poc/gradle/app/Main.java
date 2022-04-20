package net.prasenjit.poc.gradle.app;

import net.prasenjit.poc.gradle.model.Person;

public class Main {
    public static void main(String[] args) {
        Person person = new Person("Prasenjit", "Purohit");
        System.out.println(person);
        Other other = new Other();
        other.print();
        System.out.println("Hello World!");
    }
}
