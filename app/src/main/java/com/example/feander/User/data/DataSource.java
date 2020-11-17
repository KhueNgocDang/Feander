package com.example.feander.User.data;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.feander.User.data.model.LoggedInUser;
import com.example.feander.User.data.model.UncorrectException;
import com.example.feander.User.data.model.UserNameExistException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class DataSource {
    private FirebaseFirestore dataSource = FirebaseFirestore.getInstance();
    private MutableLiveData<Result> resultLive = new MutableLiveData<>();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public MutableLiveData<Result> login(String username, String password) {
        try {
//            if (checkUsers(username, password)) {
//                LoggedInUser loggedInUser =
//                        new LoggedInUser(
//                                java.util.UUID.randomUUID().toString(),
//                                username);
//                return new Result.Success<LoggedInUser>(loggedInUser);
//            } else {
//                return new Result.Error(new UncorrectPassWordException());
//            }
            return checkUsers(username, password);

        } catch (Exception e) {
            Log.d("login", e.toString());
            resultLive.setValue(new Result.Error(new IOException("Lỗi vào ra khi đăng nhập", e)));
            return resultLive;
        }
    }

    public MutableLiveData<Result> signUp(final String username, final String password, final String phoneNumber) {
        dataSource.collection("users").whereEqualTo("userNames", username)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() > 0) {
                            resultLive.setValue(new Result.Error(new UserNameExistException()));
                        } else {
                            writeData(username, password, phoneNumber);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultLive.setValue(new Result.Error(new Exception("Lỗi khi đăng ký", e)));
            }
        });
        return resultLive;
    }

    private void writeData(final String username, String password, String phoneNumber) {
        Map<String, Object> user = new HashMap<>();
        user.put("userNames", username);
        user.put("passWord", password);
        user.put("phoneNumbers", phoneNumber);
        dataSource.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        LoggedInUser loggedInUser =
                                new LoggedInUser(
                                        documentReference.getId(),
                                        username);
                        resultLive.setValue(new Result.Success<LoggedInUser>(loggedInUser));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        resultLive.setValue(new Result.Error(new Exception("Lỗi khi đăng ký", e)));
                    }
                });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void logout() {
    }

    private MutableLiveData<Result> checkUsers(final String userName, final String passWord) {
//        dataSource.collection("users").whereEqualTo("userNames", userName)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if ((task.isSuccessful()) && (task.getResult().size() > 0)) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("PassWord", document.getId() + " => " + document.getData());
//                                if (document.get("passWord").equals(passWord)) {
//                                    loginSuccess = true;
//                                } else loginSuccess = false;
//                            }
//                        } else {
//                            Log.w("PassWord", "Error getting documents.", task.getException());
//                            loginSuccess = false;
//                        }
//                    }
//                });
//        return loginSuccess;
//        dataSource.collection("users").whereEqualTo("userNames", userName)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if ((task.isSuccessful()) && (task.getResult().size() > 0)) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("PassWord", document.getId() + " => " + document.getData());
//                                if (document.get("passWord").equals(passWord)) {
//                                    LoggedInUser loggedInUser =
//                                            new LoggedInUser(
//                                                    java.util.UUID.randomUUID().toString(),
//                                                    userName);
//                                    resultLive.setValue(new Result.Success<LoggedInUser>(loggedInUser));
//                                } else {
//                                    resultLive.setValue(new Result.Error(new UncorrectException()));
//                                }
//                            }
//                        } else {
//                            Log.w("PassWord", "Lỗi khi đọc các bản ghi", task.getException());
//                            resultLive.setValue(new Result.Error(new Exception("Lỗi khi đăng nhập", task.getException())));
//                        }
//                    }
//                });

        final MutableLiveData<QuerySnapshot> querySnapshotMutableLiveData = getUserData(userName);
        querySnapshotMutableLiveData.observeForever(new Observer<QuerySnapshot>() {
            @Override
            public void onChanged(QuerySnapshot querySnapshot) {
                if (querySnapshot != null) {
                    querySnapshotMutableLiveData.removeObserver(this);
                    if (querySnapshot.size() > 0) {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Log.d("PassWord", document.getId() + " => " + document.getData());
                            if (document.get("passWord").equals(passWord)) {
                                LoggedInUser loggedInUser =
                                        new LoggedInUser(
                                                document.getId(),
                                                userName);
                                resultLive.setValue(new Result.Success<LoggedInUser>(loggedInUser));
                            } else {
                                resultLive.setValue(new Result.Error(new UncorrectException()));
                            }
                        }
                    } else {
                        resultLive.setValue(new Result.Error(new UncorrectException()));
                    }
                }
            }
        });

        return resultLive;
    }

    public MutableLiveData<Result> updateUserData(String userName, final String phoneNumber) {
        final MutableLiveData<QuerySnapshot> querySnapshotMutableLive = getUserData(userName);
        querySnapshotMutableLive.observeForever(new Observer<QuerySnapshot>() {
            @Override
            public void onChanged(QuerySnapshot querySnapshot) {
                if (querySnapshot != null) {
                    updateInFirreStore(phoneNumber, querySnapshot.getDocuments().get(0).getId(),"phoneNumbers");
                    querySnapshotMutableLive.removeObserver(this);
                }
            }
        });
        return resultLive;
    }


    public MutableLiveData<Result> changePassword(String userName, String oldPassword, final String newPassword) {
        final MutableLiveData<Result> resultMutableLiveData = new MutableLiveData<>();
        final MutableLiveData<Result> mutableLiveData = checkUsers(userName, oldPassword);
        mutableLiveData.observeForever(new Observer<Result>() {
            @Override
            public void onChanged(Result result) {
                if (result instanceof Result.Success) {
                    Void aVoid = null;
                    resultMutableLiveData.setValue(result);
                    updateInFirreStore(newPassword, ((Result.Success<LoggedInUser>) result).getData().getUserId(), "passWord" );
                    mutableLiveData.removeObserver(this);
                } else if (result instanceof Result.Error) {
                    resultMutableLiveData.setValue(result);
                    mutableLiveData.removeObserver(this);
                }
            }
        });
        return resultMutableLiveData;
    }


    private void updateInFirreStore(String info, String id, String field) {
        DocumentReference userRecord = dataSource.collection("users").document(id);

// Set the "isCapital" field of the city 'DC'
        userRecord
                .update(field, info)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("update", "DocumentSnapshot successfully updated!");
                        resultLive.setValue(new Result.Success<Void>(aVoid));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("update", "Loi khi cap nhat", e);
                        resultLive.setValue(new Result.Error(e));
                    }
                });
    }

    public MutableLiveData<String> getUserPhoneNumber(String username) {
        final MutableLiveData<QuerySnapshot> querySnapshotLive = getUserData(username);
        final MutableLiveData<String> phoneNumberLive = new MutableLiveData<>();
        querySnapshotLive.observeForever(new Observer<QuerySnapshot>() {
            @Override
            public void onChanged(QuerySnapshot querySnapshot) {
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        phoneNumberLive.setValue(document.get("phoneNumbers").toString());
                        querySnapshotLive.removeObserver(this);
                    }
                }
            }
        });
        return phoneNumberLive;
    }

    private MutableLiveData<QuerySnapshot> getUserData(String username) {
        final MutableLiveData<QuerySnapshot> querySnapshotMutableLiveData = new MutableLiveData<>();
        dataSource.collection("users").whereEqualTo("userNames", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if ((task.isSuccessful())) {
                            querySnapshotMutableLiveData.setValue(task.getResult());
                            Log.d("so ban ghi", Integer.toString(task.getResult().size()));
                        } else {
                            Log.w("PassWord", "Lỗi khi đọc các bản ghi", task.getException());
                            resultLive.setValue(new Result.Error(new Exception("Lỗi khi đăng nhập", task.getException())));
                        }
                    }
                });
        return querySnapshotMutableLiveData;
    }

}