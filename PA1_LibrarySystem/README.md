BBM104 – Programming Assignment 1 (University Library Management System)

In this assignment, a University Library Management System is implemented in Java 8, focusing on fundamental Object-Oriented Programming (OOP) principles such as inheritance and encapsulation. The system models three types of users (Student, Academic Staff, Guest) and three types of items (Book, Magazine, DVD), each with different attributes and borrowing constraints. Items are categorized as normal, reference, rare, or limited, and user types have different limits on how many items they can borrow and how long they can keep them.

The system processes operations through structured input files including users, items, and commands. Commands such as borrow, return, pay, displayUsers, and displayItems are executed sequentially. Overdue items are automatically returned and generate penalties, and users with penalties above a threshold are restricted from borrowing new items until payment is made. All operations must strictly follow the specified rules and produce outputs in an exact format.

This assignment emphasizes correct class design, proper file I/O handling, and precise output formatting verified by automated testing. The main learning objective is to design a clean and modular OOP system that correctly models real-world constraints and behaviors in a controlled simulation environment.
