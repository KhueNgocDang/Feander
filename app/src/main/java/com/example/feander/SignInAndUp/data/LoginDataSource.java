package com.example.feander.SignInAndUp.data;

import androidx.annotation.NonNull;

import com.example.feander.SignInAndUp.data.model.LoggedInUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
//    FirebaseApp app = FirebaseApp.getInstance("Feander");
//    FirebaseFirestore dataSource = FirebaseFirestore.getInstance(app);
//    CollectionReference collectionReference = dataSource.collection("users");

    public Result<LoggedInUser> login(String username, String password) {
        try {
            if (true) {
                LoggedInUser loggedInUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username);

            return new Result.Success<>(loggedInUser);
            }else {
                return new Result.Error();
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

//    private boolean checkUsers(String userName, String passWord) {
//        Query query = collectionReference.whereEqualTo("userNames", userName);
//        List<DocumentSnapshot> documentSnapshot = query.get().getResult().getDocuments();
//        if ((documentSnapshot.size() > 0) && (passWord.equals(documentSnapshot.get(0).get("passWord")))) {
//            return true;
//        } else {
//            return false;
//        }
//    }
}