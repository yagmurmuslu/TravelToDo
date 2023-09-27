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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class TraveltodoApplication {

	// Menu options

	private static final String MAIN_MENU_OPTION_USERS = "Users";
	private static final String MAIN_MENU_OPTION_WISHES = "Wishes";

	private static final String MAIN_MENU_OPTION_LOGIN = "Login";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTION_USERS,
																	 MAIN_MENU_OPTION_WISHES,
																	 MAIN_MENU_OPTION_LOGIN,
																	 MAIN_MENU_OPTION_EXIT };

	//Return to menu

	private static final String MENU_OPTION_RETURN_TO_MAIN = "Return to main menu";

	//Login menu


	private static final String[] LOGIN_MENU_OPTION = new String[] {
	};


	//User menu options

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

	//Wish to see menu options

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
																	WISH_MENU_OPTION_DELETE,
																	WISH_MENU_OPTION_CREATE };

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
			} else if(choice.equals(MAIN_MENU_OPTION_LOGIN)){
				handleLogin();
			}else if(choice.equals(MAIN_MENU_OPTION_EXIT)){
				running = false;
			}
		}
	}

	//Login menu option creating

	private void handleLogin() {
		menu.printHeadLine("Login");
		String choice = (String)menu.getChoiceFromOptions(LOGIN_MENU_OPTION);

		String userName = this.getUserInput("username: ");
		String password = this.getUserInput("password: ");

		User user = userDao.findByUserName(userName);
		if(user.getPassword().equals(password)) {
			System.out.println("Login successful");
		} else {
			System.out.println("Login is not success");
		}
	}

	//User menu option creating

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
		menu.printHeadLine("User delete");
		String userName = getUserInput("Enter user name: ");
		User user = this.userDao.findByUserName(userName.toLowerCase(Locale.ROOT));
		if (user != null){
			this.userDao.deleteUser(user.getId());
		}
		else{
			System.out.println("\n*** User '" + userName + "' does not exist. Please try again.");
		}
	}

	private void handleUsersSearch(){
		menu.printHeadLine("User search");
		String name = getUserInput("Enter name to search for");
		User user = userDao.findByUserName(name.toLowerCase());
		ArrayList<User> users = new ArrayList<>();
		users.add(user);
		listUsers(users);
	}

	private void handleUpdateUser(){
		menu.printHeadLine("Update user name");
		List<User> allUser = userDao.getAllUser();
		if(allUser.size() > 0){
			System.out.println("\n Choose a User");
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

	//Wish to see option creating

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
			this.handleCreateNewPlace();
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
		menu.printHeadLine("Delete wish");
		String placeName = getUserInput("Enter place name ");
		WishToSee place = wishToSeeDao.listByPlace(placeName.toLowerCase());
		if (place != null){
			wishToSeeDao.delete(place.getWishId());
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
		newArea.setPalaceName(placeName);
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
