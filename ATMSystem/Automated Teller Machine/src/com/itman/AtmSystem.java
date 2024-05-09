package com.itman;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class AtmSystem {

    public static void main(String[] args) {
        ArrayList<AtmAccount> accounts = new ArrayList<>();
        Scanner sc = new Scanner(System.in);


        while (true) {
            System.out.println("-----ATM System-----");
            System.out.println("1. Account Login");
            System.out.println("2. Account Sign Up");

            System.out.println("Please press the number for the services");
            int command = sc.nextInt();

            switch (command) {
                case 1:
                    login(accounts, sc);
                    break;

                case 2:
                    register(accounts, sc);
                    break;

                default:
                    System.out.println("Not Valid. Please try again!");
            }
        }


    }

    private static void login(ArrayList<AtmAccount> accounts, Scanner sc) {
        System.out.println("-----------------Account Login-----------------");
        if (accounts.size() == 0) {
            System.out.println("No account exist");
            return;
        }

        while (true) {
            System.out.println("Please enter card number.");
            String cardId = sc.next();

            AtmAccount acc = getAccountByCardId(cardId, accounts);
            if (acc != null) {
                while (true) {
                    System.out.println("Please enter the password: ");
                    String passWord = sc.next();

                    if (acc.getPassWord().equals(passWord)) {
                        System.out.println("Login successfully");

                        showUserCommand(acc, sc, accounts);
                        return;
                    } else {
                        System.out.println("Sorry. The password is not valid!");
                    }
                }
            } else {
                System.out.println("Sorry. The account does not exist in the system.");
            }
        }
    }

    private static void showUserCommand(AtmAccount acc, Scanner sc, ArrayList<AtmAccount> accounts) {
        while (true) {
            System.out.println("-----------------User Function-----------------");
            System.out.println("1. Account Info");
            System.out.println("2. Deposit");
            System.out.println("3. Withdrawal");
            System.out.println("4. Transfer");
            System.out.println("5. Change Password");
            System.out.println("6. Logout");
            System.out.println("7. Account Cancellation");
            System.out.println("Please select the service number");
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    accountInfo(acc);
                    break;
                case 2:
                    deposit(acc, sc);
                    break;
                case 3:
                    withdrawal(acc, sc);
                    break;
                case 4:
                    transfer(acc, sc, accounts);
                    break;
                case 5:
                    updatePassword(acc, sc);
                    return;
                case 6:
                    System.out.println("Logout Successfully");
                    return;
                case 7:
                    cancelAccount(acc, sc, accounts);{
                    return;
                }
                default:
                    System.out.println("The order is not valid.");
            }
        }
    }

    private static boolean cancelAccount(AtmAccount acc, Scanner sc, ArrayList<AtmAccount> accounts) {
        System.out.println("Are you sure!? Please type yes or no! ");
        String cancel = sc.next();
        switch(cancel){
            case "yes":
                if(acc.getMoney() > 0){
                    System.out.println("Your account has balance. Please take it first.");

                }else{
                    accounts.remove(acc);
                    System.out.println("Your account has been cancelled!");
                    return true;
                }
                break;
            default:
                System.out.println("Your account has been reserved!");
        }
        return false;
    }


    private static void updatePassword(AtmAccount acc, Scanner sc) {
        while (true) {
            System.out.println("-----------------Welcome to EC Bank update password Interface-----------------");
            System.out.println("Please enter account password");
            String passWord = sc.next();

            if(acc.getPassWord().equals(passWord)){
                while (true) {
                    System.out.println("Please enter new password.");
                    String newPassWord = sc.next();

                    System.out.println("Please confirm new password.");
                    String okPassWord = sc.next();

                    if (newPassWord.equals(okPassWord)){
                        acc.setPassWord(newPassWord);
                        System.out.println("Password updated!");
                        return;
                    }else{
                        System.out.println("It is not the same password.Please try again.");
                    }
                }
            }else{
                System.out.println("Not Valid!");
            }
        }
    }

    private static void transfer(AtmAccount acc, Scanner sc, ArrayList<AtmAccount> accounts) {
        System.out.println("-----------------Welcome to EC Bank Transfer Interface-----------------");
        if(accounts.size() < 2) {
            System.out.println("Less than 2 Accounts. No transfer function.");
            return;
        }

        if(acc.getMoney() == 0) {
            System.out.println("You have no money! The system could not transfer money to other account!");
            return;
        }

        while (true) {
            System.out.println("Please enter the account ID");
            String cardId = sc.next();

            if(cardId.equals(acc.getCardId())){
                System.out.println("Sorry. You cannot transfer money to yourself!");
                continue;
            }

            AtmAccount account = getAccountByCardId(cardId, accounts);
            if(account == null) {
                System.out.println("Sorry. Account does not exists");
            }else{
                String userName = account.getUserName();
                String tip = "*" + userName.substring(1);
                System.out.println("Please enter your name with " + tip);
                String preName = sc.next();

                if (userName.startsWith(preName)){
                    while (true) {
                        System.out.println("Please enter transfer amount: ");
                        double money = sc.nextDouble();

                        if (money > acc.getMoney()){
                            System.out.println("The balance is not enough. The current transfer amount: " + acc.getMoney());
                        }else{
                            acc.setMoney(acc.getMoney() - money);
                            account.setMoney(account.getMoney() + money);
                            System.out.println("Transfer successfully! Current balance: " + acc.getMoney());
                            return;
                        }
                    }
                }else{
                    System.out.println("Not valid!");
                }

            }
        }
    }

    private static void withdrawal(AtmAccount acc, Scanner sc) {
        System.out.println("-----------------Welcome to EC Bank Withdrawal Interface-----------------");
        if (acc.getMoney() < 100) {
            System.out.println("Account balance is below 100. Please deposit first.");
            return;
        }

        while (true) {
            System.out.println("Please enter the withdrawal amount");
            double money = sc.nextDouble();

            if (money > acc.getQuotaMoney()){
                System.out.println("It is over withdrawal amount. The maximum withdrawal amount: " + acc.getQuotaMoney());

            }else{
                if(money > acc.getMoney()){
                    System.out.println("Not enough balance! Current balance: " + acc.getMoney());
                }else{
                    System.out.println("Withdrawal successfully!");
                    acc.setMoney(acc.getMoney() - money);
                    accountInfo(acc);
                    return;
                }
            }
        }
    }

    private static void deposit(AtmAccount acc, Scanner sc) {
        System.out.println("-----------------Deposit-----------------");
        System.out.println("Please enter the deposit amount: ");
        double money = sc.nextDouble();

        acc.setMoney(acc.getMoney() + money);
        System.out.println("Your bank account balance as follows: ");
        accountInfo(acc);
    }

    private static void accountInfo(AtmAccount acc) {
        System.out.println("-----------------Account Info-----------------");
        System.out.println("Card Number: " + acc.getCardId());
        System.out.println("Cardholder Name: " + acc.getUserName());
        System.out.println("Balance: " + acc.getMoney());
        System.out.println("Withdrawal Quota: " + acc.getQuotaMoney());
    }



    private static void register(ArrayList<AtmAccount> accounts, Scanner sc) {
        System.out.println("-----------------Account Opening-----------------");
        AtmAccount account = new AtmAccount();

        System.out.println("Please enter the account name:");
        String userName = sc.next();
        account.setUserName(userName);


        while (true) {
            System.out.println("Please enter the account password:");
            String passWord = sc.next();
            System.out.println("Please enter the account password again:");
            String okPassWord = sc.next();
            if (okPassWord.equals(passWord)) {
                account.setPassWord(okPassWord);
                break;

            } else {
                System.out.println("Sorry. It is not correct.");
            }
        }


        System.out.println("Please enter the account quota:");
        double quotaMoney = sc.nextDouble();
        account.setQuotaMoney(quotaMoney);

        String cardId = getRandomCardId(accounts);
        account.setCardId(cardId);
        accounts.add(account); //Account Info add to arraylist<AtmAccount> accounts
        System.out.println("Congratulations! Your bank account has been opened successfully. Here is your Card ID: " + cardId);
    }

    private static String getRandomCardId(ArrayList<AtmAccount> accounts) {
        Random r = new Random();
        while (true) {
            String cardId = "";
            for (int i = 0; i < 8; i++) {
                cardId += r.nextInt(10);
            }
            AtmAccount acc = getAccountByCardId(cardId, accounts);
            if (acc == null) {
                return cardId;
            }
        }
    }

    private static AtmAccount getAccountByCardId(String cardId, ArrayList<AtmAccount> accounts){
        for (int i = 0; i < accounts.size(); i++) {
            AtmAccount acc = accounts.get(i);
            if(acc.getCardId().equals(cardId)){
                return acc;
            }
        }
        return null;
    }
}
