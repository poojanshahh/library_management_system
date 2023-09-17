public class Book {
    private String bookId;
    private String bookName;
    private String copies;
    private int index;

    Book(String bookId,String bookName,String copies,int index){
        this.bookId=bookId;
        this.bookName=bookName;
        this.copies=copies;
        this.index=index;
    }

    public String getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public String getCopies() {
        return copies;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return  bookId + ',' + bookName + ',' + copies;
    }
}
