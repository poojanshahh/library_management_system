public class User {
    private String fullName;
    private String userName;
    private String password;
    private String role;
    User(String fullName,String userName,String password,String role){
        this.fullName=fullName;
        this.userName=userName;
        this.password=password;
        this.role=role;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return userName+","+fullName+","+password+","+role;
    }
}
