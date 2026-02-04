BBM104 – Programming Assignment 2 (Zoo Manager System)

In this assignment, a Zoo Manager System is developed in Java 8, emphasizing abstraction, polymorphism, and custom exception handling. The system manages different animal types (Lion, Elephant, Penguin, Chimpanzee), each with specific feeding rules and meal size calculations based on age. Animals can be fed and their habitats can be cleaned, and each species implements these behaviors differently.

The system also models two types of people: Personnel and Visitor. Personnel are authorized to feed animals and clean habitats, while visitors are only allowed to visit animals. Invalid actions, such as visitors attempting to feed animals or feeding when food stock is insufficient, trigger meaningful custom exceptions that must be handled without stopping program execution.

All data is read from external text files for animals, people, food stock, and commands. The program must process both valid and invalid commands, printing detailed logs and error messages in a strict format. The assignment focuses on effective use of OOP design, runtime exception management, and realistic simulation of a zoo environment.
