package com.examscheduler.model;

/**
 * Abstract base class representing a human actor in the system.
 * Demonstrates Abstraction, Encapsulation, and Constructor Overloading.
 */
public abstract class Person {

    // Protected fields accessible by derived classes (Student and Faculty)
    protected String id;
    protected String name;
    protected int age;

    /**
     * Default constructor.
     * Initializes default placeholder values using constructor chaining.
     */
    public Person() {
        this("UNKNOWN", "Not Assigned", 18);
    }

    /**
     * Parameterized constructor.
     * Initializes all fields with specific provided values.
     */
    public Person(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    /**
     * Copy constructor.
     * Creates a new Person object as an exact copy of an existing Person.
     */
    public Person(Person other) {
        if (other != null) {
            this.id = other.id;
            this.name = other.name;
            this.age = other.age;
        }
    }

    /**
     * Abstract method that derived classes MUST implement to show details.
     * Demonstrates abstraction and polymorphism.
     */
    public abstract void displayDetails();

    // Getter for ID
    public String getId() {
        return id;
    }

    // Setter for ID
    public void setId(String id) {
        this.id = id;
    }

    // Getter for Name
    public String getName() {
        return name;
    }

    // Setter for Name with simple validation
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
    }

    // Getter for Age
    public int getAge() {
        return age;
    }

    // Setter for Age
    public void setAge(int age) {
        if (age > 0) {
            this.age = age;
        }
    }

    // Overridden toString method
    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Age: " + age;
    }
}
