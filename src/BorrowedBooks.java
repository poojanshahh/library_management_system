import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/*
This class is only for issuing and returning book with validation like if book
available to borrow or not, after issue update copies in book database, return that books which
are not returned yet,
 */
public class BorrowedBooks extends FileIOOperations {

    //private final String borrowedHeader="User_name,Book_id,Issue_date,Return_date\n";
    private final String borrowedHeader = "User_name,Book_id,Book_name,Issue_date,Return_date,fine\n";

    BorrowedBooks() {
        BufferedWriter borrowedWriter = null;

        try {
            File userfile = new File(BORROWEDDATA);

            if (!(userfile.exists() && !userfile.isDirectory())) {
                borrowedWriter = new BufferedWriter(new FileWriter(BORROWEDDATA));
                borrowedWriter.write(borrowedHeader);
            }
        } catch (IOException e) {
            System.out.println("IO exception");
        } finally {
            try {
                if (borrowedWriter != null)
                    borrowedWriter.close();
            } catch (IOException e) {
                System.out.println("Error whie clos");
            }
        }
    }


    void issueBook(String username, Book book) {
        if (isBookAvailable(book)) {

            FileWriter writer = null;
            try {
                writer = new FileWriter(BORROWEDDATA, true);

                LocalDate today = LocalDate.now();
                String issue_date = today.toString();

                writer.append(username).append(",");
                writer.append(book.getBookId()).append(",");
                writer.append(book.getBookName()).append(",");
                writer.append(issue_date).append("\n");
                updateCopies(book, 0);
                System.out.println("\n\tBook issued successfully....");

            } catch (IOException e) {
                System.out.println("Exception");
            } finally {
                try {
                    if (writer != null)
                    writer.close();
                } catch (IOException e) {
                    System.out.println("error while");
                }
            }
        }else{
            System.out.println("\n\tNot available.. You can't issue");
        }
    }

    private boolean isBookAvailable(Book book) {
        //System.out.println("id: "+book.getBookId());
        List<String[]> books = showCatalog();
        byte flag=1;
        for(int i=1;i<books.size();i++){
            String[] row=books.get(i);
            if(row[0].equals(book.getBookId())){
                ///System.out.println("id eqal");
                if (Integer.parseInt(row[2])>0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void updateCopies(Book book, int addOrMinus) {
        int i = book.getIndex();
        List<String[]> bookList = showCatalog();

        String newData[] = bookList.get(i);
        int remainCopies = Integer.parseInt(newData[2]);
        if (addOrMinus == 0) {
            if (remainCopies - 1 < 0)
                System.out.println("\n\tNo book available");
            else
                newData[2] = Integer.toString(--remainCopies);
        } else {
            newData[2] = Integer.toString(++remainCopies);
        }
        bookList.set(i, newData);

        FileWriter bookWriter = null;
        try {
            bookWriter = new FileWriter("src/BookData.csv");
            for (String[] rowData : bookList) {
                bookWriter.append(String.join(",", rowData));
                bookWriter.append("\n");
            }
        } catch (IOException e) {
            System.out.println("IO exc");
        } finally {
            try {
                if (bookWriter != null)
                bookWriter.close();
            } catch (IOException e) {
                System.out.println("IO excep");
            }
        }
    }

    private List<String[]> readBorrower() {
        BufferedReader br = null;
        List<String[]> borrowerList = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(BORROWEDDATA));
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                borrowerList.add(row);
            }
            return borrowerList;
        } catch (FileNotFoundException e) {
            System.out.println("File not ");
        } catch (IOException e) {
            System.out.println("IO e");
        } finally {
            try {
                if (br != null)
                br.close();
            } catch (IOException e) {
                System.out.println("error while");
            }
        }
        return borrowerList;
    }

    boolean isBookBorrowed(String userName, Book book) {
        List<String[]> bList = readBorrower();
        for (String[] rowData : bList) {
            if (rowData.length == 5) continue;
            if (rowData[0].equals(userName) && rowData[1].equals(book.getBookId()) && rowData.length <= 4)
                return true;
        }
        return false;
    }

    void returnBook(String userName, Book book) {
        FileWriter fr = null;
        byte flag = 1;
        List<String[]> bList = readBorrower();
        try {
            fr = new FileWriter(BORROWEDDATA);
            for (String[] rowData : bList) {
                if (rowData[0].equals(userName) && rowData[1].equals(book.getBookId()) && rowData.length == 4 && flag == 1) {
                    //if(isBookBorrowed(userName,bookId)){
                    AdminInterface aa=new Admin();
                    LocalDate today = LocalDate.now();
                    LocalDate returnDate = today;
                    int fine=aa.calculateFine(rowData[3]);
                    String newData = rowData[0] + "," + rowData[1] + "," + rowData[2] + "," + rowData[3] + "," +
                            returnDate.toString() + "," + Integer.toString(fine);
                    fr.append(newData).append("\n");

                    flag = 0;
                } else {
                    fr.append(String.join(",", rowData));
                    fr.append("\n");
                }
            }
            updateCopies(book, 1);
        } catch (IOException e) {
            System.out.println("IO");
        } finally {
            try {
                if (fr != null)
                fr.close();
            } catch (IOException e) {
                System.out.println("Error while");
            }
        }
    }
    static String readPass(){
        String password;
        while (true)
        {
            System.out.print("\nEnter Your Password - ");
            password = new String(System.console().readPassword());
            for (int i = 0; i < password.length(); i++)
            {
                System.out.print("*");
            }

            System.out.println();

            if (password.length() >= 8)
            {
                break;
            }
            else
            {
                System.out.println("\nPassword Must Contain Atleast 8 Characters!");
            }
        }
        return password;
    }

}
