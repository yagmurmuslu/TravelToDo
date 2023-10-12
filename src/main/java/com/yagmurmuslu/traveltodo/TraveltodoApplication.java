package com.yagmurmuslu.traveltodo;

import com.yagmurmuslu.traveltodo.dao.JdbcUserDao;
import com.yagmurmuslu.traveltodo.dao.JdbcWishToSeeDao;
import com.yagmurmuslu.traveltodo.dao.UserDao;
import com.yagmurmuslu.traveltodo.dao.WishToSeeDao;
import com.yagmurmuslu.traveltodo.model.User;
import com.yagmurmuslu.traveltodo.model.WishToSee;
import com.yagmurmuslu.traveltodo.view.Menu;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class TraveltodoApplication {

	// Static variables Login
	private static boolean isLogin = false;

	// Constants Menu options
	private static final String WELCOME_MENU_OPTION_LOGIN = "Login";
	private static final String WELCOME_MENU_OPTION_LOOK_ALL_WISHES = "Look all wishes";
	private static final String WELCOME_MENU_OPTION_CREATE_NEW_ACCOUNT = "Create new account";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { WELCOME_MENU_OPTION_LOGIN,
																	 WELCOME_MENU_OPTION_LOOK_ALL_WISHES,
																	 WELCOME_MENU_OPTION_CREATE_NEW_ACCOUNT };

	// Constants Return to menu

	private static final String MENU_OPTION_RETURN_TO_MAIN = "Return to main menu";

	//Login menu
	private static final String MAIN_MENU_OPTION_USERS = "Users";
	private static final String MAIN_MENU_OPTION_WISHES = "Wishes";
	private static final String MAIN_MENU_OPTION_LOGOUT = "Logout";
	private static final String[] LOGIN_MENU_OPTION = { MAIN_MENU_OPTION_USERS,
														MAIN_MENU_OPTION_WISHES,
														MAIN_MENU_OPTION_LOGOUT };


	// Constants Logout
	private static final String OPTION_YES = "Yes";
	private static final String OPTION_NO = "No";
	private static final String[] LOGOUT_OPTIONS = { OPTION_YES,
													 OPTION_NO };

	// Constants User menu options
	private static final String USERS_MENU_OPTION_ALL_USERS = "Show all users";
	private static final String USER_MENU_OPTION_UPDATE_NAME = "Update user name";
	private static final String USER_MENU_OPTION_UPDATE_PASSWORD = "Update user password";
	private static final String USER_MENU_OPTION_DELETE_USER = "Delete your account";
	private static final String[] USER_MENU_OPTION = new String[]{ USERS_MENU_OPTION_ALL_USERS,
																   USER_MENU_OPTION_UPDATE_NAME,
																   USER_MENU_OPTION_UPDATE_PASSWORD,
																   USER_MENU_OPTION_DELETE_USER,
																   MENU_OPTION_RETURN_TO_MAIN };

	// Constants Wish to see menu options

	private static final String WISH_MENU_OPTION_ALL_WISH = "Show all wishes";
	private static final String WISH_MENU_OPTION_SEARCH_BY_USER = "See user wishlist";
	private static final String WISH_MENU_OPTION_SEARCH_BY_CITY = "Search city and see all wish place";
	private static final String WISH_MENU_OPTION_UPDATE = "Update wish to see";
	private static final String WISH_MENU_OPTION_DELETE = "Delete wish to see";
	private static final String WISH_MENU_OPTION_CREATE = "Create new wish to see";
	private static final String[] WISH_MENU_OPTION = new String[] { WISH_MENU_OPTION_ALL_WISH,
																	WISH_MENU_OPTION_SEARCH_BY_USER,
																	WISH_MENU_OPTION_SEARCH_BY_CITY,
																	WISH_MENU_OPTION_UPDATE,
																	WISH_MENU_OPTION_DELETE,
																	WISH_MENU_OPTION_CREATE,
			  													    MENU_OPTION_RETURN_TO_MAIN };

	// Fields
	private final Menu menu;
	private final UserDao userDao;
	private final WishToSeeDao wishToSeeDao;
	private User user = null;


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
			if(isLogin) {
				menu.printHeadLine("Main Menu \n " + this.user.getName().toUpperCase() + " is logged in" );
				String choice = (String)menu.getChoiceFromOptions(LOGIN_MENU_OPTION);

				if(choice.equals(MAIN_MENU_OPTION_USERS)) {
					this.handleUsers();
				} else if (choice.equals(MAIN_MENU_OPTION_WISHES)) {
					this.handleWishes();
				} else if (choice.equals(MAIN_MENU_OPTION_LOGOUT)) {
					System.out.println("Are you sure to logout?");
					String logoutChoice = (String)menu.getChoiceFromOptions(LOGOUT_OPTIONS);
					if(logoutChoice.equals(OPTION_YES)) {
						isLogin = false;
					}
				}
			} else {
				menu.printHeadLine("Welcome travel ToDo application \n P.S. Before start adding new place ensure you are logged in");

				String choice = (String)menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
				if(choice.equals(WELCOME_MENU_OPTION_LOGIN)){
					handleLogin();
				}else if(choice.equals(WELCOME_MENU_OPTION_LOOK_ALL_WISHES)){
					handleAllWishlist();
				}else if(choice.equals(WELCOME_MENU_OPTION_CREATE_NEW_ACCOUNT)){
					handleAddNewUser();
				}else if(!isLogin) {
					System.out.println("You must be login!");
				}
			}

		}
	}

	//Login menu option creating
	private void handleLogin() {
		menu.printHeadLine("Login");

		String userName = this.getUserInput("username: ");
		String password = this.getUserInput("password: ");


		try {
			this.user = userDao.findByUserName(userName);
			if(user.getPassword().equals(password)) {
				System.out.println("Login successful");
				isLogin = true;
			} else {
				System.out.println("Login is not success. Please, make sure username or password are correct!");
			}
		} catch (Exception exception){
			System.out.println("User does not exist!");
		}

	}

	//User menu option creating
	private void handleUsers(){
		while (true) {
			menu.printHeadLine("Users menu");
			String choice = (String)menu.getChoiceFromOptions(USER_MENU_OPTION);
			if(choice.equals(USER_MENU_OPTION_DELETE_USER)){
				if(handleDeleteUser()) {
					break;
				}
			}else if(choice.equals(USER_MENU_OPTION_UPDATE_NAME)){
				this.handleUpdateUserName();
			}else if(choice.equals(USER_MENU_OPTION_UPDATE_PASSWORD)){
				this.handleUpdateUserPassword();
			}else if(choice.equals(USERS_MENU_OPTION_ALL_USERS)){
				this.handleListAllUsers();
			} else if(choice.equals(MENU_OPTION_RETURN_TO_MAIN)) {
				break;
			}
		}
	}

	private void handleAddNewUser(){
		String userName = this.getUserInput("Enter user name: ");
		String password = this.getUserInput("Enter password: ");
		try {
			User newUser = new User(0, userName, password);
			this.userDao.create(newUser);
			System.out.println("Account created successfully!");
		} catch (Exception exception) {
			System.out.println("This user has already created.");
		}
	}

	private boolean handleDeleteUser(){

		menu.printHeadLine("Delete your account");
		System.out.println("Are you sure you want to delete your account");
		String deleteYourAccountChoice = (String)menu.getChoiceFromOptions(LOGOUT_OPTIONS);
		if(deleteYourAccountChoice.equals(OPTION_YES)) {
			userDao.deleteUser(user.getId());
			isLogin = false;
			return true;
		}
		return false;
	}

	private void handleUpdateUserName(){
		menu.printHeadLine("Update user name");
		List<User> allUser = userDao.getAllUser();
		if(allUser.size() > 0){
			System.out.println("\n Choose a User");
			User selectedUser = (User)menu.getChoiceFromOptions(allUser.toArray());
			User updatedUser = menu.updateUserName(selectedUser);
			userDao.updateUserName(updatedUser);
		}else {
			System.out.println("\n*** No result");
		}
	}

	private void handleUpdateUserPassword(){
		menu.printHeadLine("Update user password");
		String name = getUserInput("Enter user name: ");
		try {
			User user = userDao.findByUserName(name.toLowerCase());
			User updatedUserPassword = menu.updateUserPassword(user);
			userDao.updateUserPassword(updatedUserPassword);
		} catch (Exception exception) {
			System.out.println("Be sure user name is correct!");
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

	//Wish to see option creating
	private void handleWishes(){
		while (true) {
			menu.printHeadLine("Wishes menu");
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
				this.handleCreateNewPlace();
			}else if(choice.equals(MENU_OPTION_RETURN_TO_MAIN)){
				break;
			}
		}

	}

	private void listWishes(List<WishToSee> wishes) {
		System.out.println();
		if(wishes.size() > 0){
			for(WishToSee wish: wishes){
				User user = userDao.getUserById(wish.getUserId());

				System.out.println(wish.getPlaceName() + " in " + wish.getCity() + " by " + user.getName());
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
		menu.printHeadLine("Delete wish");
		String placeName = getUserInput("Enter place name ");
		WishToSee place = wishToSeeDao.listByPlace(placeName.toLowerCase());

		if (place != null){
			if(place.getUserId() == this.user.getId()){
				wishToSeeDao.delete(place.getWishId());
			} else {
				System.out.println("The users can only delete their own wishes");
			}
		}
		else{
			System.out.println("\n*** User '" + place + "' does not exist. Please try again.");
		}
	}

	private void handleSearchByCity(){
		menu.printHeadLine("City search");
		String cityName = getUserInput("Enter city name");
		List<WishToSee> wishToSeeByCityName = wishToSeeDao.listByCity(cityName.toLowerCase());
		listWishes(wishToSeeByCityName);
	}

	private void handleSearchByUserId(){
		menu.printHeadLine("User search");
		String userName = getUserInput("Enter user name ");
		User user = this.userDao.findByUserName(userName.toLowerCase());
		List<WishToSee> wishToSeeByUserId = this.wishToSeeDao.listByUserId(user.getId());
		this.listWishes(wishToSeeByUserId);
	}

	private void handleWishlistUpdate(){
		menu.printHeadLine("Wish update");
		String placeName = getUserInput("Enter place name");
		WishToSee place = wishToSeeDao.listByPlace(placeName.toLowerCase());
		String cityName = getUserInput("Enter city name");
		place.setCity(cityName.toLowerCase());
		wishToSeeDao.update(place);
	}

	private void handleCreateNewPlace(){
		menu.printHeadLine("Wish create");
		String userName = getUserInput("Enter user name");
		User user = userDao.findByUserName(userName.toLowerCase());
		int userId = user.getId();

		String placeName = getUserInput("Enter place name");
		String cityName = getUserInput("Enter city name");
		String address = getUserInput("Enter address (optional)");
		String goodForKids = getUserInput("Is it kid friendly? [y|n]");
		boolean goodForKidsBoolean = false;

		if(goodForKids.equals("y")){
			goodForKidsBoolean = true;
		} else if(goodForKids.equals("n")){
			goodForKidsBoolean = false;
		}

		WishToSee newArea = new WishToSee();
		newArea.setPlaceName(placeName);
		newArea.setCity(cityName);
		newArea.setAddress(address);
		newArea.setForKids(goodForKidsBoolean);
		newArea.setUserId(userId);

		wishToSeeDao.createNewOne(newArea);

	}

	private String getUserInput (String prompt){
		System.out.println(prompt + " >>> ");
		return new Scanner(System.in).nextLine();
	}

}
