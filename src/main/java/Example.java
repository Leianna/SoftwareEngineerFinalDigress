public class Example {

    private String email;
    private double balance;

    public Example(String email, double startingBalance){
        if (isEmailValid(email)){
            this.email = email;
            this.balance = startingBalance;
        }
    }

    public double getBalance(){
        return balance;
    }
    public static boolean isEmailValid(String email){
        return email.matches("(\\w)+((_|\\.|-)+\\w+)?@(\\w)+\\.\\w{2,}$");
    }
}
