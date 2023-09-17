/* This class is for basic operation like showing menu,
Signin, Registration, Student Action, Admin action
 */

import java.util.*;

public class Operations {
    private String fullName;
    private String userName;
    private String password;
    private String role = null;
    Scanner sc = new Scanner(System.in);
    BorrowedBooks bb = new BorrowedBooks();
    AdminInterface aa=new Admin();

    //Making obj of fileio operation class
    FileIOOperations fop = new FileIOOperations(1);

    public void showMenu() {
        int choice;
        System.out.println();
        System.out.println("+----------------------------------------------------------------+");
        System.out.println("|             Welcome to Library management system               |");
        System.out.println("+----------------------------------------------------------------+");
//        System.out.println("\n\t");
        choice = 0;
        do {
            System.out.println();
            System.out.println("+-------------------------------------+");
            System.out.println("|             Main Menu               |");
            System.out.println("+-------------------------------------+");
            System.out.println("| 1. Sign in                          |");
            System.out.println("| 2. Register                         |");
            System.out.println("| 3. Exit                             |");
            System.out.println("+-------------------------------------+");
            System.out.print(" Enter your choice: ");
            try {
                choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1:
                        signIn();
                        break;
                    case 2:
                        register();
                        break;
                    case 3:
                        break;
                    default:
                        System.out.println("Enter correct option");

                }
            } catch (InputMismatchException e) {
                System.out.println("You entered some character !!!!");
                System.out.println("\t.. try again with digit !!!!");
                sc.nextLine(); // or again call same fn
            }
        } while (choice != 3);
    }

    private void signIn() {
        System.out.print("Enter username : ");
        String userName = sc.nextLine();
        /*System.out.print("Enter password : ");
        String password = sc.nextLine();*/
        String password=BorrowedBooks.readPass();


        byte flag = 0;
        List<String[]> allUser = fop.readUsers();
        for (String[] rowData : allUser) {
            if (rowData[0].equals(userName)) {
                flag = 1;
            }
        }
        if (flag == 0) {
            System.out.println("\n\n\tUserName doesn't exist..try again");
            return;
        }

        if (!userValid(userName, password)) {
            System.out.println("\n\t!!Password or userName not valid");
            System.out.print("Forgot password?(y/n)");
            char c = sc.nextLine().charAt(0);
            if (c == 'y' || c == 'Y') {
                forgotPassword(userName);
            }
        } else {
            System.out.println("\n...Valid user name....");
            if ("Admin".equals(role)) {
                System.out.println("\n\tWelcome Admin");
                adminActions(userName);
            } else {
                System.out.println("\tWelcome "+userName);
                studentActions(userName);
            }
        }
    }

    private void adminActions(String userName) {
        int choice=0;
        do {
            System.out.println();
            System.out.println("=========================================");
            System.out.println("|               Menu                    |");
            //System.out.println("=========================================");
            System.out.println("| 1. Add book info                      |");
            System.out.println("| 2. Show all books                     |");
            System.out.println("| 3. Back to Main Menu                  |");
            System.out.println("=========================================");
            System.out.print(" Enter your choice: ");
            try {
                choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1:
                        aa.addBookInfo();
                        break;
                    case 2: displayBooks();
                    break;
                    case 3:
                        System.out.println("Exited....");
                        break;
                    default:
                        System.out.println("Wrong input?????");

                }
            } catch (InputMismatchException e) {
                System.out.println("\n!!May you entered some character !!!!!");
                sc.nextLine();

            }
        } while (choice != 3);
    }

    private void studentActions(String userName) {
        int choice=0;
        displayBooks();

        do {
            displayBooks();
            System.out.println();
            System.out.println("|****************************|");
            System.out.println("|      Library Operation     |");
            System.out.println("|****************************|");
            System.out.println("| 1. Issue book              |");
            System.out.println("| 2. Return book             |");
            System.out.println("| 3. Display Profile         |");
            System.out.println("| 4. Back to main menu       |");
            System.out.println("|****************************|");
            System.out.print(" Enter your choice: ");
            String bookId;
            try {
                choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1:
                        System.out.print("Enter book id to issue: ");
                        bookId = sc.nextLine();
                        Book book = fop.getBook(bookId);
                        if (book != null) {
                            bb.issueBook(userName, book);
                        }
                        //fop.issueBook(userName,bookId);
                        break;
                    case 2:
                        System.out.print("Enter book id to return: ");
                        bookId = sc.nextLine();
                        book = fop.getBook(bookId);
                        if (bb.isBookBorrowed(userName, book)) {
                            bb.returnBook(userName, book);
                        } else {
                            System.out.println("\n\tYou don't own this book ;) .|.");
                        }
                        //fop.returnBook(userName,bookId);
                        break;
                    case 3:
                        fop.showProfile(userName);
                        break;
                    case 4:
                        System.out.println("\tvisit again....Thank you!!!");
                        break;
                    default:
                        System.out.println("You entered wrong operation");
                }
            } catch (InputMismatchException e) {
                System.out.println("You entered some character !!!");
                System.out.println("\t.. try again with digit");
                sc.nextLine();
            }
        } while (choice != 4);
    }

    private void displayBooks() {
        List<String[]> books = fop.showCatalog();
        System.out.println("\n============= Books ================");
        System.out.println(" Book id       Book Name        Copies");
        System.out.println("\n====================================");

        for (int i=1;i<books.size();i++) {
            String[] row= books.get(i);
            System.out.println("|"+row[0] + "\t\t\t" + row[1] + "\t\t\t\t\t" + row[2]+"|");
        }
        System.out.println("\n====================================");
    }

    private boolean userValid(String userName, String password) {
        List<String[]> allUser = fop.readUsers();
        for (String[] rowData : allUser) {
            if (rowData[0].equals(userName)) {
                if (rowData[2].equals(password)) {
                    role = rowData[3];
                    return true;
                }
            }
        }
        return false;
    }

    private void forgotPassword(String userName) {
        /*System.out.print("Enter new password : ");
        String password = sc.nextLine();*/
        String password=BorrowedBooks.readPass();

        //fetching old data
        List<String[]> allUser = fop.readUsers();
        for (String[] rowData : allUser) {
            if (rowData[0].equals(userName)) {
                rowData[2] = password;
            }
        }
        //String newData=String.join(",",allUser.get(index));
        fop.updatePassword(allUser);
        //System.out.println("newdata: "+newData);
    }
    private boolean isUserNameExist(String userName){
        List<String[]> allUser = fop.readUsers();
        for(String[] row: allUser){
            if(row[0].equals(userName)){
                return true;
            }
        }
        return false;
    }
    boolean isValidUserName(String username){
        if (isUserNameExist(username))
        {
            System.out.println("\n! Username already taken..\nPlease try for another username !");
            return false;
        }

        else if (username.contains(" "))
        {
            System.out.println("\n! No spaces allowed in username !");
            return false;
        }
        else if (!username.matches("^[a-z0-9_]+$"))
        {
            System.out.println("\n! No special characters allowed in username !");
            return false;
        }
        return true;
    }
    private void register() {
        System.out.println();
        System.out.println("|******************|");
        System.out.println("|    Register as?  |");
        System.out.println("|******************|");
        System.out.println("| 1. Student       |");
        System.out.println("| 2. Admin         |");
        System.out.println("|******************|");
        System.out.print("Enter Choice : ");
        int choiceForRole=0;
        try{

        choiceForRole = sc.nextInt();
        sc.nextLine();
        }catch (InputMismatchException e){
            System.out.println("\nMay you entered some character");
        }

        System.out.print("Enter your full name: ");
        fullName = sc.nextLine();
        if(!fullName.matches("^[a-zA-Z\\s]+$"))
        {
            System.out.println("\n! Only Characters Allowed in Full Name !");
            return;
        }
        System.out.print("Enter username : ");
        userName = sc.nextLine();
        if(!isValidUserName(userName))
            return;
        /*System.out.print("Enter password : ");
        password = sc.nextLine();*/
        String password=BorrowedBooks.readPass();


        String role = (choiceForRole == 1) ? "Student" : "Admin";

        User user = new User(fullName, userName, password, role);
        System.out.println("\n\tRegistered successfully");

        fop.addUser(user);
    }
}
