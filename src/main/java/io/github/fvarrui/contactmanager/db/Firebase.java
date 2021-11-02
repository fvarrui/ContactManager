package io.github.fvarrui.contactmanager.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class Firebase {
	
	private static final String TAG = Firebase.class.getName();
	
	private static final String serviceAccountKey = "serviceAccountKey.json";
	
	public static void init() throws IOException {
		
		System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "info");
		
		FileInputStream serviceAccount = new FileInputStream(serviceAccountKey);

		FirebaseOptions options = 
				FirebaseOptions
					.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

		FirebaseApp.initializeApp(options);
		
		Logger.getLogger(TAG).info("Firebase initialized!");
	}
	
	public static void close() throws Exception {
		FirestoreClient.getFirestore().close();
		Logger.getLogger(TAG).info("Firebase closed!");
	}

}
