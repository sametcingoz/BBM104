package Library;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LibrarySystem {
    // A map to store items using their ID
    private Map<String, Item> items;
    // A map to store users using their ID
    private Map<String, UserBase> users;
    // Name of the file that contains item data
    private String itemsFileName;
    // A list to keep the output log
    private List<String> transactionLog;

    public LibrarySystem() {
        items = new TreeMap<>();
        users = new TreeMap<>();
        transactionLog = new ArrayList<>();
    }

    // This method loads items (books, magazines, DVDs) from a file
    public void loadItems(String filename) {
        this.itemsFileName = filename;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", -1);
                String firstToken = parts[0].trim();

                // Load Book
                if (firstToken.equalsIgnoreCase("B") && parts.length >= 6) {
                    String id = parts[1].trim();
                    String title = parts[2].trim();
                    String author = parts[3].trim();
                    String genre = parts[4].trim();
                    String type = parts[5].trim();
                    items.put(id, new BookItem(id, title, author, genre, type));
                }
                // Load Magazine
                else if (firstToken.equalsIgnoreCase("M") && parts.length >= 6) {
                    String id = parts[1].trim();
                    String title = parts[2].trim();
                    String publisher = parts[3].trim();
                    String category = parts[4].trim();
                    String type = parts[5].trim();
                    items.put(id, new MagazineItem(id, title, publisher, category, type));
                }
                // Load DVD
                else if (firstToken.equalsIgnoreCase("D") && parts.length >= 7) {
                    String id = parts[1].trim();
                    String title = parts[2].trim();
                    String director = parts[3].trim();
                    String category = parts[4].trim();
                    String runtimeStr = parts[5].trim();
                    String type = parts[6].trim();
                    int runtime = Integer.parseInt(runtimeStr.replaceAll("[^0-9]", ""));
                    items.put(id, new DVDItem(id, title, director, category, runtime, type));
                }
                // Fallback for magazines with missing type
                else if (parts.length >= 5) {
                    String id = parts[0].trim();
                    String title = parts[1].trim();
                    String publisher = parts[2].trim();
                    String category = parts[3].trim();
                    String type = parts[4].trim();
                    items.put(id, new MagazineItem(id, title, publisher, category, type));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This method loads user data from a file
    public void loadUsers(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", -1);
                String userClass = parts[0].trim();

                // Load Student user
                if (userClass.equalsIgnoreCase("S")) {
                    String name = parts[1].trim();
                    String id = parts[2].trim();
                    String phone = parts[3].trim();
                    String department = parts[4].trim();
                    String faculty = parts[5].trim();
                    int grade = Integer.parseInt(parts[6].trim());
                    users.put(id, new StudentUser(id, name, phone, department, faculty, grade));
                }
                // Load Academic Staff user
                else if (userClass.equalsIgnoreCase("A")) {
                    String name = parts[1].trim();
                    String id = parts[2].trim();
                    String phone = parts[3].trim();
                    String department = parts[4].trim();
                    String faculty = parts[5].trim();
                    String title = parts[6].trim();
                    users.put(id, new AcademicStaff(id, name, phone, department, faculty, title));
                }
                // Load Guest user
                else if (userClass.equalsIgnoreCase("G")) {
                    String name = parts[1].trim();
                    String id = parts[2].trim();
                    String phone = parts[3].trim();
                    String occupation = parts[4].trim();
                    users.put(id, new Guest(id, name, phone, occupation));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This method reads commands from a file and performs actions
    public void processCommands(String filename) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate currentDate = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", -1);
                String command = parts[0].trim().toLowerCase();

                switch (command) {
                    // Borrow command
                    case "borrow":
                        if (parts.length >= 4) {
                            String userId = parts[1].trim();
                            String itemId = parts[2].trim();
                            String borrowDateStr = parts[3].trim();
                            LocalDate borrowDate = LocalDate.parse(borrowDateStr, formatter);
                            currentDate = borrowDate;
                            UserBase user = users.get(userId);
                            Item item = items.get(itemId);
                            if (user != null && item != null) {
                                user.checkOverdueItems(borrowDate);
                                if (user.penalty >= 6.0) {
                                    transactionLog.add(user.getName() + " cannot borrow " + item.getTitle() +
                                            ", you must first pay the penalty amount! " + (int) user.penalty + "$");
                                } else {
                                    // Check item restrictions for Guest users
                                    if (user instanceof Guest && item.getType().equalsIgnoreCase("rare")) {
                                        transactionLog.add(user.getName() + " cannot borrow rare item!");
                                    } else if (user instanceof Guest && item.getType().equalsIgnoreCase("limited")) {
                                        transactionLog.add(user.getName() + " cannot borrow limited item!");
                                    } else {
                                        // Try to borrow item
                                        boolean result = user.borrowItem(item, borrowDate);
                                        if (result) {
                                            transactionLog.add(user.getName() + " successfully borrowed! " + item.getTitle());
                                        } else {
                                            // Handle borrowing failure reasons
                                            if (item.isBorrowed()) {
                                                transactionLog.add(user.getName() + " cannot borrow " + item.getTitle() + ", it is not available!");
                                            } else if (user.borrowedItems.size() >= user.getMaxItems()) {
                                                transactionLog.add(user.getName() + " cannot borrow " + item.getTitle() +
                                                        ", since the borrow limit has been reached!");
                                            } else if (item.getType().equalsIgnoreCase("reference")) {
                                                transactionLog.add(user.getName() + " cannot borrow " + item.getTitle() +
                                                        ", since the borrow limit has been reached!");
                                            } else {
                                                transactionLog.add(user.getName() + " failed to borrow " + item.getTitle());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;

                    // Return command
                    case "return":
                        if (parts.length >= 3) {
                            String userId = parts[1].trim();
                            String itemId = parts[2].trim();
                            UserBase user = users.get(userId);
                            Item item = items.get(itemId);
                            if (user != null && item != null) {
                                LocalDate returnDate = (currentDate != null) ? currentDate : LocalDate.now();
                                boolean result = user.returnItem(item, returnDate);
                                if (result) {
                                    transactionLog.add(user.getName() + " successfully returned " + item.getTitle());
                                } else {
                                    transactionLog.add(user.getName() + " failed to return " + item.getTitle());
                                }
                            }
                        }
                        break;

                    // Pay penalty command
                    case "pay":
                        if (parts.length >= 2) {
                            String userId = parts[1].trim();
                            UserBase user = users.get(userId);
                            if (user != null) {
                                user.clearPenalty();
                                transactionLog.add(user.getName() + " has paid penalty");
                            }
                        }
                        break;

                    // Display users command
                    case "displayusers":
                        transactionLog.add("");
                        transactionLog.add("");
                        for (UserBase u : users.values()) {
                            transactionLog.add(u.toString());
                            transactionLog.add("");
                        }
                        transactionLog.add("");
                        break;

                    // Display items command
                    case "displayitems":
                        transactionLog.add("");
                        transactionLog.add("");
                        printFormattedItems();
                        break;

                    // If command is not known, do nothing
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Print final state of all items
        printFormattedItems();
    }

    // This method writes the output log to a file
    public void writeOutput(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (String log : transactionLog) {
                pw.println(log);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This method adds item display info to the log
    private void printFormattedItems() {
        for (Item item : items.values()) {
            transactionLog.add(item.display());
            transactionLog.add("");
        }
    }
}
