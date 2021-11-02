package io.github.fvarrui.contactmanager;

import java.util.Scanner;
import java.util.logging.Logger;

import com.google.cloud.firestore.ListenerRegistration;

import io.github.fvarrui.contactmanager.db.Firebase;
import io.github.fvarrui.contactmanager.db.dao.ContactDAO;
import io.github.fvarrui.contactmanager.db.entity.Contact;

public class Main {
	
	private static final String TAG = Main.class.getName();
	
	private static ListenerRegistration listener;
	
	public static void main(String[] args) throws Exception {
				
		Firebase.init();
		
		Logger.getLogger(TAG).info("thread: " + Thread.currentThread().getName());
		
		listener = ContactDAO.getInstance().listenContacts(
				(t, c) -> {
					Logger.getLogger("listenContacts").info("thread: " + Thread.currentThread().getName());					
					System.out.println(t + " contact " + c);
				},
				Throwable::printStackTrace
			);

		Contact contact = new Contact("Charles", "Bronson");
		contact.getPhones().put("Trabajo", "(12)3-456-789");
		contact.getPhones().put("Móvil", "(987)65-43-211");

//		ContactDAO.getInstance().addContact(
//				contact, 
//				result -> System.out.println("Contact successfully added! " + result),
//				Throwable::printStackTrace
//			);
		
//		ContactDAO.getInstance().removeContact(
//				"9W2znYGGREqaItlF6kSJ",
//				System.out::println,
//				Throwable::printStackTrace
//			);

//		ContactDAO.getInstance().getContact(
//				"P4CkX8uaSLD2QpXON0XJ",
//				System.out::println,
//				Throwable::printStackTrace
//			);

		inputText("Press Enter to quit...");

		listener.remove();
		Firebase.close();
		
		System.exit(0);		

	}

	private static String inputText(String message) {
		Scanner scanner = new Scanner(System.in);
		System.out.println(message);
		String input = scanner.nextLine();
		scanner.close();
		return input;
	}

}
