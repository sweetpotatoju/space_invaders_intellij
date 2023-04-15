package spaceinvaders;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

//새로 추가 한것
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.*;

import javax.swing.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Consumer;

public class FirebaseTool {
    private static final String DOMAIN_NAME = "https://space-5d8e0-default-rtdb.firebaseio.com/";
    private static final String KEY_LOCATION = "\\src\\main\\resources\\key.json";
    private static FirebaseTool firebaseTool = null;
    private FirebaseApp firebaseApp;
    private DatabaseReference databaseReference;

    private GlobalStorage globalStorage;

    public static FirebaseTool getInstance() {
        if (firebaseTool == null) {
            firebaseTool = new FirebaseTool();
        }

        return firebaseTool;
    }

    public FirebaseTool() {
        initialize();
        globalStorage = GlobalStorage.getInstance();
    }

    public void initialize() {
        try {
            FileInputStream serviceAccount = new FileInputStream(System.getProperty("user.dir") + KEY_LOCATION);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(DOMAIN_NAME)
                    .build();

            firebaseApp = FirebaseApp.initializeApp(options, "space-invander-member-list");

            if (firebaseApp != null) {
                System.out.println(firebaseApp.getName());

                databaseReference = FirebaseDatabase.getInstance(firebaseApp).getReference();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean Login(String id, String password) {
        try {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp);

            UserRecord userRecord = firebaseAuth.getUserByEmail(id);

            if (userRecord != null) {
                if (userRecord.getEmail().equals(id)) {
                    globalStorage.setUserID(id);
                    getUserBestScore(id);
                    getUserProfileImage(id);
                    getUserTheme(id);
                    JOptionPane.showMessageDialog(null, "로그인이 정상적으로 처리되었습니다.");
                    return true;
                }
            }

        } catch (NullPointerException e) {
            //e.printStackTrace();
            JOptionPane.showMessageDialog(null, "아이디와 비밀번호를 확입하세요.");
            return false;
        } catch (FirebaseAuthException e) {
            //throw new RuntimeException(e);
            JOptionPane.showMessageDialog(null, "아이디와 비밀번호를 확인하세요.");
            return false;
        }

        return false;
    }

    public void Signup(String id, String password) {
        try {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
            firebaseAuth.createUser(new UserRecord.CreateRequest()
                    .setEmail(id)
                    .setEmailVerified(false)
                    .setPassword(password)
                    .setDisplayName(id));

            DatabaseReference initReference = FirebaseDatabase.getInstance(firebaseApp).getReference();

            initReference.child("user").child(id.split("@")[0]).child("profileimage").setValue("1", null);
            initReference.child("user").child(id.split("@")[0]).child("bestscore").setValue("0", null);
            initReference.child("user").child(id.split("@")[0]).child("theme").setValue("1", null);

            JOptionPane.showMessageDialog(null, "회원가입에 정상적으로 처리되었습니다.");

        } catch (NullPointerException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "회원가입에 문제가 생겼습니다.");
        } catch (FirebaseAuthException e) {
            JOptionPane.showMessageDialog(null, "회원가입에 문제가 생겼습니다.");
            throw new RuntimeException(e);
        }
    }

    public String getUserProfileImage(String id) {
        String userProfileSelect = "1";

        try {
            DatabaseReference userProfileImageDatabase = FirebaseDatabase.getInstance(firebaseApp).getReference();

            userProfileImageDatabase.child("user")
                    .child(id.split("@")[0])
                    .child("profileimage")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String select = dataSnapshot.getValue(String.class);
                            System.out.println("User Profile Data Receivced " + select);
                            globalStorage.setUserProfileImage(select);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("User Profile Data Error " + databaseError);
                        }
                    });

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return userProfileSelect;
    }

    public String getUserTheme(String id) {
        String userThemeSelect = "1";

        try {
            DatabaseReference userThemeDatabase = FirebaseDatabase.getInstance(firebaseApp).getReference();

            userThemeDatabase.child("user")
                    .child(id.split("@")[0])
                    .child("theme")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String select = dataSnapshot.getValue(String.class);
                            System.out.println("User Theme Data Receivced " + select);
                            globalStorage.setUserTheme(select);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("User Theme Data Error " + databaseError);

                        }
                    });

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return userThemeSelect;
    }

    public String getUserBestScore(String id) {
        String userBestScore = "1";

        try {
            DatabaseReference userScoreDatabase = FirebaseDatabase.getInstance(firebaseApp).getReference();

            userScoreDatabase.child("user")
                    .child(id.split("@")[0])
                    .child("bestscore")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String score = dataSnapshot.getValue(String.class);
                    System.out.println("User Score Data Receivced " + score);
                    globalStorage.setUserBestScore(score);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("User Score Data Error " + databaseError);

                }
            });

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return userBestScore;
    }

    public void getAllUserBestScore(Consumer<ArrayList<HashMap<String, String>>> callback) {
        DatabaseReference userAllScoreDatabase = FirebaseDatabase.getInstance(firebaseApp).getReference();

        userAllScoreDatabase.child("user")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<HashMap<String, String>> userAllBestScore = new ArrayList<HashMap<String, String>>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String id = ds.getKey();
                            String bestscore = ds.child("bestscore").getValue(String.class);

                            HashMap<String, String> userScore = new HashMap<String, String>();
                            userScore.put("id", id);
                            userScore.put("bestscore", bestscore);
                            userAllBestScore.add(userScore);
                        }
                        // 데이터 가져오기 완료 후 내림차순으로 정렬
                        Collections.sort(userAllBestScore, new Comparator<HashMap<String, String>>() {
                            @Override
                            public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                                return Integer.compare(Integer.parseInt(o2.get("bestscore")), Integer.parseInt(o1.get("bestscore")));
                            }
                        });
                        callback.accept(userAllBestScore);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("Data Error " + databaseError);
                    }
                });
    }

    public void setUserBestScore(String id, String bestscore) {
        try {
            DatabaseReference userScoreDatabase = FirebaseDatabase.getInstance(firebaseApp).getReference();

            userScoreDatabase.child("user").child(id.split("@")[0]).child("bestscore").setValue(bestscore, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    globalStorage.setUserBestScore(bestscore);
                }
            });

        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public void setUserProfileImage(String id, String select) {
        try {
            DatabaseReference userProfileImageDatabase = FirebaseDatabase.getInstance(firebaseApp).getReference();

            userProfileImageDatabase.child("user").child(id.split("@")[0]).child("profileimage").setValue(select, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    globalStorage.setUserProfileImage(select);
                }
            });

        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public void setUserTheme(String id, String select) {
        try {
            DatabaseReference userProfileImageDatabase = FirebaseDatabase.getInstance(firebaseApp).getReference();

            userProfileImageDatabase.child("user").child(id.split("@")[0]).child("theme").setValue(select, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    globalStorage.setUserTheme(select);
                }
            });

        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }



}
