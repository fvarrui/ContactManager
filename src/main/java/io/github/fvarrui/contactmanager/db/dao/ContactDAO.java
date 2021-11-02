package io.github.fvarrui.contactmanager.db.dao;

import java.util.logging.Logger;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

import io.github.fvarrui.contactmanager.db.OnChangeListener;
import io.github.fvarrui.contactmanager.db.OnFailureListener;
import io.github.fvarrui.contactmanager.db.OnSuccessListener;
import io.github.fvarrui.contactmanager.db.entity.Contact;

public class ContactDAO extends BaseDAO {
	
	private static final String TAG = ContactDAO.class.getName();
	
    public static final String COLLECTION_NAME = "contacts";

	private static ContactDAO singleton;
	
	private ContactDAO() {
		super();
	}
	
	public static ContactDAO getInstance() {
		if (singleton == null) {
			singleton = new ContactDAO();
		}
		return singleton;
	}
	
	public void addContact(Contact contact, OnSuccessListener<Contact> successListener, OnFailureListener failureListener) {
	
		ApiFuture<DocumentReference> future = getDb().collection(COLLECTION_NAME).add(contact);
		
		ApiFutures.addCallback(future, new ApiFutureCallback<DocumentReference>() {
			@Override
			public void onSuccess(DocumentReference result) {
				Logger.getLogger(TAG).info("Added contact with id " + result.getId());
				contact.setId(result.getId());
				successListener.onSuccess(contact);
			}
			@Override
			public void onFailure(Throwable t) {
				failureListener.onFailure(t);
			}
		}, getExecutor());
		
	}
	
	public void updateContact(Contact contact, OnSuccessListener<Contact> successListener, OnFailureListener failureListener) {
		
		ApiFuture<WriteResult> future = getDb().collection(COLLECTION_NAME).document(contact.getId()).set(contact);
		
		ApiFutures.addCallback(future, new ApiFutureCallback<WriteResult>() {
			@Override
			public void onSuccess(WriteResult result) {
				Logger.getLogger(TAG).info("Updated contact with id " + contact.getId() + " at " + result.getUpdateTime());
				successListener.onSuccess(contact);
			}
			@Override
			public void onFailure(Throwable t) {
				failureListener.onFailure(t);
			}
		}, getExecutor());
		
	}
	
	public void removeContact(String id, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
		
		ApiFuture<WriteResult> future = getDb().collection(COLLECTION_NAME).document(id).delete();
		
		ApiFutures.addCallback(future, new ApiFutureCallback<WriteResult>() {
			@Override
			public void onSuccess(WriteResult result) {
				Logger.getLogger(TAG).info("Removed contact with id " + id + " at " + result.getUpdateTime());
				successListener.onSuccess(null);	
			}
			@Override
			public void onFailure(Throwable t) {
				failureListener.onFailure(t);
			}
		}, getExecutor());
		
	}
	
	public void removeContact(Contact contact, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
		removeContact(contact.getId(), successListener, failureListener);
	}
	
	public void getContact(String id, OnSuccessListener<Contact> successListener, OnFailureListener failureListener) {
		
		ApiFuture<DocumentSnapshot> future = getDb().collection(COLLECTION_NAME).document(id).get();
		
		ApiFutures.addCallback(future, new ApiFutureCallback<DocumentSnapshot>() {
			@Override
			public void onSuccess(DocumentSnapshot result) {
				Logger.getLogger(TAG).info("Retrieved contact with id " + id);
				if (!result.exists()) {
					failureListener.onFailure(new Exception("Contact with id " + id + " doesn't exist"));
				} else {
					Contact contact = result.toObject(Contact.class);
					contact.setId(result.getId());
					successListener.onSuccess(contact);
				}
			}
			@Override
			public void onFailure(Throwable t) {
				failureListener.onFailure(t);
			}
		}, getExecutor());
		
	}
	
	public ListenerRegistration listenContacts(OnChangeListener<Contact> changeListener, OnFailureListener failureListener) {
		
		Logger.getLogger(TAG).info("Listening contacts!");

		return getDb().collection(COLLECTION_NAME).addSnapshotListener(new EventListener<QuerySnapshot>() {
			@Override
			public void onEvent(QuerySnapshot value, FirestoreException error) {
				
				if (error != null) {
					failureListener.onFailure(error);
				} else {
					value.getDocumentChanges().stream().forEach(c -> {
						Contact contact = c.getDocument().toObject(Contact.class);
						contact.setId(c.getDocument().getId());
						changeListener.onChange(c.getType(), contact);
					});
				}
				
			}
		});
		
	}

}
