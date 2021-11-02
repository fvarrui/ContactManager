package io.github.fvarrui.contactmanager.db.dao;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

public class BaseDAO {
	
    private static Executor executor = Executors.newCachedThreadPool();
    private static Firestore db = FirestoreClient.getFirestore();

    protected Firestore getDb() {
		return db;
	}

    protected Executor getExecutor() {
		return executor;
	}

}
