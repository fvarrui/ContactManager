package io.github.fvarrui.contactmanager.db;

import com.google.cloud.firestore.DocumentChange.Type;

public interface OnChangeListener<T> {

	void onChange(Type changeType, T result);
	
}
