import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

/*
This class is implementation of admin interface
add book will not allow admin to enter duplicate book id
 */
public class Admin extends FileIOOperations implements AdminInterface {

    public void addBookInfo() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter book id: ");
        int bookId = sc.nextInt();
        sc.nextLine();
        if (!bookAddOrNot(Integer.toString(bookId))) {

            System.out.print("Enter name of book: ");
            String bookName = sc.nextLine();
            System.out.print("Enter number of copies: ");
            int totalCopies = sc.nextInt();
            String bookInfo = bookId + "," + bookName + "," + totalCopies;
            FileWriter bookWriter = null;
            try {
                bookWriter = new FileWriter(BOOKDATA, true);
                //userWriter.append(user.getUserName()+",");
                bookWriter.append(bookInfo).append("\n");
                System.out.println("\n\tBook added successfully");
            } catch (IOException e) {
                System.out.println("IO ex");
            } finally {
                try {
                    if (bookWriter != null)
                        bookWriter.close();
                } catch (IOException e) {
                    System.out.println("Erro while close");
                }
            }
        } else {
            System.out.println("\n\t Book id already registered...");
        }
    }


    @Override
    public int calculateFine(String issue_date) {
        //calculating fine
        LocalDate today = LocalDate.now();
        LocalDate issuDate = LocalDate.parse(issue_date); //rowData[3]

        // Define the second date
        LocalDate returnDate = today;

        // Calculate the difference between the two dates in days
        long daysDifference = ChronoUnit.DAYS.between(issuDate, returnDate);
//        System.out.println("day of diff": );
        int extraDays;
        int fine = 0;
        if (daysDifference > 15) {
            extraDays = (int) (daysDifference) - 15;
            fine = extraDays * 5;
        } else
            fine = 0;
        return fine;
    }


}
