import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


//user defined exception
class InvalidBookID extends Exception {
    public InvalidBookID(String errorMessage) {
        // calling the constructor of parent Exception
        super(errorMessage);
    }
}

/*
* this class is for file input output operation which read, write and update data in file
* We have checked all possible condition for right input and output.
* i.e, IF file exist then dont create new one, check if book id valid,
* */
 class FileIOOperations {
    Scanner sc = new Scanner(System.in);
    private static final String USERDATA = "src/UserData.csv";
    protected static final String BOOKDATA = "src/BookData.csv";
    protected static final String BORROWEDDATA = "src/BorrowedData.csv";
    private final String userHeader = "User_name,Full_name,Password,role\n";

    private final String bookHeader = "Book_id,Book_name,Copies\n";

    //protected int index=0;
    FileIOOperations() {
    }

    FileIOOperations(int i) {
        BufferedWriter userWriter = null;
        BufferedWriter bookWriter = null;

        try {
            File userfile = new File(USERDATA);

            if(!(userfile.exists() && !userfile.isDirectory())) {
                // do something
                userWriter = new BufferedWriter(new FileWriter(USERDATA));
                bookWriter = new BufferedWriter(new FileWriter(BOOKDATA));

                userWriter.append(userHeader);
                bookWriter.append(bookHeader);
            }
        } catch (IOException e) {
            System.out.println("IO ex");
        } finally {
            try {
                if(userWriter!=null)
                    userWriter.close();
                if(bookWriter!=null)
                bookWriter.close();
            } catch (IOException e) {
                System.out.println("Erro whie clos");
            }
        }
    }

    void addUser(User user) {
        FileWriter userWriter = null;
        try {
            userWriter = new FileWriter(USERDATA, true);
            //userWriter.append(user.getUserName()+",");
            userWriter.append(user.toString()).append("\n");
        } catch (IOException e) {
            System.out.println("IO ex");
        } finally {
            try {
                if(userWriter!=null)
                userWriter.close();
            } catch (IOException e) {
                System.out.println("Erro while closing");
            }
        }
    }

    List<String[]> readUsers() {
        List<String[]> allUser = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(USERDATA));
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                allUser.add(row);
            }
            return allUser;
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (IOException e) {
            System.out.println("IO exception");
        } finally {
            try {
                if(br!=null)
                br.close();
            } catch (IOException e) {
                System.out.println("Error while closing");
            }
        }
        return null;
    }

    void updatePassword(List<String[]> allUser) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(USERDATA);
            for (String[] rowData : allUser) {
                writer.append(String.join(",", rowData));
                writer.append("\n");
            }
        } catch (IOException e) {
            System.out.println("IO");
        } finally {
            try {
                if(writer!=null)
                writer.close();
            } catch (IOException e) {
                System.out.println("Error while ..");
            }
        }

    }




    List<String[]> showCatalog() {
        List<String[]> bookList = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(BOOKDATA));
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                bookList.add(row);
            }
            return bookList;
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (IOException e) {
            System.out.println("IO exception");
        } finally {
            try {
                if(br!=null)
                br.close();
            } catch (IOException e) {
                System.out.println("Error while closing");
            }
        }
        return bookList;
    }

    Book getBook(String bookId) {
        int index = 0;
        Book book = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(BOOKDATA));
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if (row[0].equals(bookId)) {
                    book = new Book(row[0], row[1], row[2], index);
                    //System.out.println("book equal with index " + index + " " + book.toString());
                    break;
                }
                index++;
            }

        } catch (FileNotFoundException e) {
            System.out.println("File no");
        } catch (IOException e) {
            System.out.println("IO excepton");
        } finally {
            try {
                if(br!=null)
                br.close();
            } catch (IOException e) {
                System.out.println("Error while closing");
            }
        }
        try {
            if (book == null) {
                throw new InvalidBookID("You entered wrong Book_Id");
            } else {
                //valid book id
                //System.out.println("hi book got");
                return book;
            }
        } catch (InvalidBookID e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //this method we didn't use
    int getIndexOfBookId(int bookId) {
        List<String[]> allBooks = showCatalog();
        int index = -1;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(BORROWEDDATA));
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if (Integer.parseInt(row[2]) == bookId) {
                    return index;
                }
                index++;
            }
        } catch (IOException e) {
            System.out.println("IO excep");
        } finally {
            try {
                if(br!=null)
                br.close();
            } catch (IOException e) {
                System.out.println("Error while closing");
            }
        }
        return index;
    }

    private void showUserDetail(String userName) {
        List<String[]> users = readUsers();
        for (String[] userData : users) {
            if (userData[0].equals(userName)) {
                System.out.println("\nFull Name: " + userData[1]);
                System.out.println("User_Name: " + userData[0]);
            }
        }
    }

    private void displayBorrowedBooks(String[] row) {
        for (int i = 0; i < 6; i++) {
            if (i > row.length - i)
                System.out.print(" ");
            else
                System.out.print(row[i] + " ");
        }
        System.out.println();
    }

    private void showUserBooks(String userName) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(BORROWEDDATA));
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if (row[0].equals(userName)) {
                    if (row.length > 4) {
                        System.out.println(row[0] + "\t\t" + row[1] + "\t" + row[2] + "\t\t" + row[3] + "\t\t\t"+ row[4] + "\t\t " + row[5]);
                    } else {
                        System.out.println(row[0] + "\t\t" + row[1] + "\t\t\t" + row[2] + "\t\t" + row[3] + "\t\t   Not Returned" + "\t  Not Returned");
                    }
                    //displayBorrowedBooks(row);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("IO exception");
        } finally {
            try {
                if(br!=null)
                    br.close();
            } catch (IOException e) {
                System.out.println("Error while closing");
            }
        }
    }

    void showProfile(String userName) {
        //show dotted line
        System.out.println("\n\n=========================Your Profile==================================");
        showUserDetail(userName);
        //line
        System.out.println("\n\n=========================Borrowed Books==================================");
        System.out.println("User_Name\tBook_ID\t\tBook_name\t\tIssue Date\t\tReturn Date\t    Fine");
        showUserBooks(userName);
    }
    protected boolean bookAddOrNot(String bookId){
        List<String[]> allBooks = showCatalog();
        for(String[] row: allBooks){
            if(bookId.equals(row[0]))
                return true;
        }
        return false;
    }
}
