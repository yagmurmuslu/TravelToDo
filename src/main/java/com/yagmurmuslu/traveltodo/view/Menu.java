package com.yagmurmuslu.traveltodo.view;

import com.yagmurmuslu.traveltodo.model.User;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Menu {

    private PrintStream out;
    private Scanner in;

    public Menu(InputStream input, PrintStream output) {
        this.out = output;
        this.in = new Scanner(input);
    }

    public Object getChoiceFromOptions(Object[] options) {
        Object choice = null;
        while(choice == null) {
            displayMenuOptions(options);
            choice = getChoiceFromUserInput(options);
        }
        return choice;
    }

    public void printHeadLine (String headingText) {
        System.out.println("\n" + headingText);
        for(int i = 0; i < headingText.length(); i++){
            System.out.print("-");
        }
        System.out.println();
    }

    public User updateUserName(User user){
        User updatedUser = new User();
        out.println("Enter new user name: ");
        updatedUser.setName(in.nextLine());
        updatedUser.setId(user.getId());
        return updatedUser;
    }

    public User updateUserPassword(User userPassword) {
        User updatedUserPassword = new User();
        out.println("Enter new password: ");
        String firstPassword = in.nextLine();
        out.println(("Enter new password again: "));
        String secondPassword = in.nextLine();
        if(firstPassword.equals(secondPassword)){
            updatedUserPassword.setPassword(secondPassword);
        } else {
            out.println("Passwords do not match");
        }
        updatedUserPassword.setId(userPassword.getId());
        return updatedUserPassword;
    }

    private Object getChoiceFromUserInput(Object[] options) {
        Object choice = null;
        String userInput = in.nextLine();
        try{
            int selectedOption = Integer.valueOf(userInput);
            if(selectedOption <= options.length){
                choice = options[selectedOption - 1];
            }
        } catch (NumberFormatException exception) {
            return null;
        }
        if(choice == null){
            out.println("\n*** " + userInput + " is not a valid option *** \n");
        }
        return choice;
    }

    private void displayMenuOptions(Object[] options){
        out.println();
        for(int i = 0; i < options.length; i++ ){
            int optionNum = i +1;
            out.println(optionNum+") " + options[i]);
        }
        out.println("\nPlease choose an option >>>");
        out.flush();
    }

}
