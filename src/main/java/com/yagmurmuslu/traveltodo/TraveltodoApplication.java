package com.yagmurmuslu.traveltodo;

import com.yagmurmuslu.traveltodo.dao.JdbcUserDao;
import com.yagmurmuslu.traveltodo.dao.JdbcWishToSeeDao;
import com.yagmurmuslu.traveltodo.dao.UserDao;
import com.yagmurmuslu.traveltodo.dao.WishToSeeDao;
import com.yagmurmuslu.traveltodo.model.User;
import com.yagmurmuslu.traveltodo.model.WishToSee;
import com.yagmurmuslu.traveltodo.view.Menu;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TraveltodoApplication {

	private static final String MAIN_MENU_OPTION_USERS = "Users";
	private static final String MAIN_MENU_OPTION_WISHES = "Wishes";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTION_USERS,
																	 MAIN_MENU_OPTION_WISHES,
																	 MAIN_MENU_OPTION_EXIT };

	private static final String MENU_OPTION_RETURN_TO_MAIN = "Return to main menu";

	private static final String USERS_MENU_OPTION_ALL_USERS = "Show all users";
	private static final String USER_MENU_OPTION_SEARCH_BY_NAME = "Find user search by name";
	private static final String USER_MENU_OPTION_UPDATE_NAME = "Update user name";
	private static final String USER_MENU_OPTION_ADD_USER = "Add new user";
	private static final String USER_MENU_OPTION_DELETE_USER = "Delete user";
	private static final String[] USER_MENU_OPTION = new String[]{ USERS_MENU_OPTION_ALL_USERS,
																   USER_MENU_OPTION_SEARCH_BY_NAME,
																   USER_MENU_OPTION_UPDATE_NAME,
																   USER_MENU_OPTION_ADD_USER,
																   USER_MENU_OPTION_DELETE_USER };

	private static final String WISH_MENU_OPTION_ALL_WISH = "Show all wish to see";
	private static final String WISH_MENU_OPTION_SEARCH_BY_USER = "Show all wish to see for user";
	private static final String WISH_MENU_OPTION_SEARCH_BY_CITY = "Show all wish to see by city name";
	private static final String WISH_MENU_OPTION_UPDATE = "Update wish to see";
	private static final String WISH_MENU_OPTION_DELETE = "Delete wish to see";
	private static final String WISH_MENU_OPTION_CREATE = "Create new wish to see";
	private static final String[] WISH_MENU_OPTION = new String[] { WISH_MENU_OPTION_ALL_WISH,
																	WISH_MENU_OPTION_SEARCH_BY_USER,
																	WISH_MENU_OPTION_SEARCH_BY_CITY,
																	WISH_MENU_OPTION_UPDATE,
																	WISH_MENU_OPTION_DELETE };

	private final Menu menu;
	private final UserDao userDao;
	private final WishToSeeDao wishToSeeDao;


	public TraveltodoApplication(DataSource dataSource) {
		this.menu = new Menu(System.in, System.out);
		userDao = new JdbcUserDao(dataSource);
		wishToSeeDao = new JdbcWishToSeeDao(dataSource);
	}



	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		// Step One: Configure the database connection
		dataSource.setUrl("jdbc:postgresql://localhost:5432/toDoTravel");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		TraveltodoApplication application = new TraveltodoApplication(dataSource);
		application.run();
	}

	private void run(){
		boolean running = true;
		while (running) {
			menu.printHeadLine("Main Menu");
			String choice = (String)menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(choice.equals(MAIN_MENU_OPTION_USERS)){
				handleUsers();
			}else if(choice.equals(MAIN_MENU_OPTION_WISHES)){
				handleWishes();
			}else if(choice.equals(MAIN_MENU_OPTION_EXIT)){
				running = false;
			}
		}
	}

	private void handleUsers(){
		menu.printHeadLine("Users");
		String choice = (String)menu.getChoiceFromOptions(USER_MENU_OPTION);
		if(choice.equals(USER_MENU_OPTION_ADD_USER)){
			this.handleAddNewUser();
		}else if(choice.equals(USER_MENU_OPTION_DELETE_USER)){
			this.handleDeleteUser();
		}else if(choice.equals(USER_MENU_OPTION_SEARCH_BY_NAME)){
			this.handleUsersSearch();
		}else if(choice.equals(USER_MENU_OPTION_UPDATE_NAME)){
			this.handleUpdateUser();
		}else if(choice.equals(USERS_MENU_OPTION_ALL_USERS)){
			this.handleListAllUsers();
		}
	}

	private void handleAddNewUser(){
		String userName = this.getUserInput("Enter user name: ");
		String password = this.getUserInput("Enter password: ");

		User newUser = new User(0, userName, password);
		this.userDao.create(newUser);
	}

	private void handleDeleteUser(){

	}

	private void handleUsersSearch(){
		menu.printHeadLine("User search");
		String name = getUserInput("Enter name to search for");
		User user = userDao.findByUserName(name);
		ArrayList<User> users = new ArrayList<>();
		users.add(user);
		listUsers(users);
	}

	private void handleUpdateUser(){
		menu.printHeadLine("Update user name");
		List<User> allUser = userDao.getAllUser();
		if(allUser.size() > 0){
			System.out.println("\n*** Choose a User");
			User selectedUser = (User)menu.getChoiceFromOptions(allUser.toArray());
			User updatedUser = menu.updateUser(selectedUser);
			userDao.update(updatedUser);
		}else {
			System.out.println("\n*** No result");
		}
	}

	private void listUsers(List<User> users){
		System.out.println();
		if(users.size() > 0){
			for(User user : users){
				System.out.println(user.getName());
			}
		}else{
			System.out.println("\n*** No result ***");
		}
	}

	private void handleListAllUsers(){
		menu.printHeadLine("All Users");
		List<User> allUsers = userDao.getAllUser();
		listUsers(allUsers);
	}

	private void handleWishes(){
		menu.printHeadLine("Wish to see");
		String choice = (String)menu.getChoiceFromOptions(WISH_MENU_OPTION);
		if(choice.equals(WISH_MENU_OPTION_ALL_WISH)){
			this.handleAllWishlist();
		}else if(choice.equals(WISH_MENU_OPTION_DELETE)){
			this.handleDeleteWish();
		}else if(choice.equals(WISH_MENU_OPTION_SEARCH_BY_CITY)){
			this.handleSearchByCity();
		}else if(choice.equals(WISH_MENU_OPTION_SEARCH_BY_USER)){
			this.handleSearchByUserId();
		}else if(choice.equals(WISH_MENU_OPTION_UPDATE)){
			this.handleWishlistUpdate();
		}else if(choice.equals(WISH_MENU_OPTION_CREATE)){
			this.handleWishlistCreate();
		}
	}

	private void listWishes(List<WishToSee> wishes) {
		System.out.println();
		if(wishes.size() > 0){
			for(WishToSee wish: wishes){
				System.out.println(wish.getPalaceName());
			}
		} else {
			System.out.println("\n *** No result ***");
		}
	}

	private void handleAllWishlist(){
		menu.printHeadLine("All wishlist");
		List<WishToSee> allWish = wishToSeeDao.listAll();
		listWishes(allWish);
	}

	private void handleDeleteWish(){

	}

	private void handleSearchByCity(){
		menu.printHeadLine("City search");
		String cityName = getUserInput("Enter city name");
		List<WishToSee> wishToSeeByCityName = wishToSeeDao.listByCity(cityName);
		listWishes(wishToSeeByCityName);
	}

	private void handleSearchByUserId(){
		menu.printHeadLine("User search");
		String userName = getUserInput("Enter user name ");
		User user = this.userDao.findByUserName(userName);
		List<WishToSee> wishToSeeByUserId = this.wishToSeeDao.listByUserId(user.getId());
		this.listWishes(wishToSeeByUserId);
	}

	private void handleWishlistUpdate(){

	}

	private void handleWishlistCreate(){

	}

	private String getUserInput (String prompt){
		System.out.println(prompt + " >>> ");
		return new Scanner(System.in).nextLine();
	}

}
