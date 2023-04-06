package spaceinvaders;

import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class store{

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();

    private final DatabaseReference userRef = db.getReference("users");

    public static Object score;

    public void storeScore(int score) {
        HashMap<String, Integer> users = new HashMap<>();
        users.put("high score", score);
        this.userRef.setValueAsync(users);
    }

    public void readData(){
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Logger.getLogger(store.class.getName()).log(Level.INFO, "data read");
                score = dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Logger.getLogger(store.class.getName()).log(Level.INFO, "data read");
            }
        });
    }

    public Object returnData(){
        return score;
    }

}