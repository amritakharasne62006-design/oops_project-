package com.examscheduler.model;

/**
 * ============================================================================
 * CLASS: Person (Abstract Base Class)
 * ============================================================================
 * WHAT IT DOES:
 *   Represents a general human being in our college examination system.
 *   Acts as the parent class for both 'Student' and 'Faculty'.
 *
 * WHY IT EXISTS (OOP CONCEPTS):
 *   1. ABSTRACTION: We cannot create a plain 'Person' object directly (it is abstract).
 *      A person in an exam is always either a Student or a Faculty.
 *   2. ENCAPSULATION: All member variables are protected/private, accessed via getters/setters.
 *   3. CONSTRUCTOR OVERLOADING: Provides Default, Parameterized, and Copy constructors.
 *   4. 'this' KEYWORD: Used to resolve variable shadowing and for constructor chaining this(...).
 * ============================================================================
 */
public abstract class Person {

    // ------------------------------------------------------------------------
    // 1. ATTRIBUTES / MEMBER VARIABLES
    // 'protected' allows derived classes (Student, Faculty) to access them directly.
    // ------------------------------------------------------------------------
    protected String id;     // Unique identification (e.g., PRN for student, EmpID for faculty)
    protected String name;   // Full name of the person
    protected int age;       // Age in years

    // ------------------------------------------------------------------------
    // 2. CONSTRUCTORS (Demonstrates Constructor Overloading)
    // ------------------------------------------------------------------------

    /**
     * A) DEFAULT CONSTRUCTOR
     * Calls the parameterized constructor using this(...) constructor chaining.
     */
    public Person() {
        this("UNKNOWN", "Not Assigned", 18);
    }

    /**
     * B) PARAMETERIZED CONSTRUCTOR
     * Initializes all the member variables with specific values passed by caller.
     * Uses 'this.variable = variable' to resolve name shadowing.
     */
    public Person(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    /**
     * C) COPY CONSTRUCTOR
     * Creates a new Person object by copying data from an existing Person object.
     * Useful when we want to clone data safely without affecting the original.
     */
    public Person(Person other) {
        if (other != null) {
            this.id = other.id;
            this.name = other.name;
            this.age = other.age;
        }
    }

    // ------------------------------------------------------------------------
    // 3. ABSTRACT METHOD (Forces child classes to provide implementation)
    // ------------------------------------------------------------------------

    /**
     * Abstract method to print personal details.
     * Every child class (Student, Faculty) MUST override and provide its own implementation.
     */
    public abstract void displayDetails();

    // ------------------------------------------------------------------------
    // 4. GETTERS AND SETTERS (Encapsulation and Data Protection)
    // ------------------------------------------------------------------------

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    // Setter with basic validation to avoid empty names
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
    }

    public int getAge() {
        return this.age;
    }

    // Setter with validation to ensure age is positive
    public void setAge(int age) {
        if (age > 0) {
            this.age = age;
        }
    }

    // ------------------------------------------------------------------------
    // 5. TOSTRING METHOD (Method Overriding from Object class)
    // ------------------------------------------------------------------------
    @Override
    public String toString() {
        return "Person [ID=" + id + ", Name=" + name + ", Age=" + age + "]";
    }
}
