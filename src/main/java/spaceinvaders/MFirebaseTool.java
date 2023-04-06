package spaceinvaders;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

//public class MFirebaseTool {
//    private static MFirebaseTool mFirebaseTool = null;
//
//    public MFirebaseTool() {
//        try {
//            FileInputStream serviceAccount = new FileInputStream(this.getClass().getResource("/config/key.json").getPath());
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setDatabaseUrl("https://space-5d8e0-default-rtdb.firebaseio.com//")
//                    .build();
//
//            FirebaseApp.initializeApp(options);
//
//            System.out.println(FirebaseApp.getInstance().getName());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static MFirebaseTool getInstance() {
//        if (mFirebaseTool == null) {
//            mFirebaseTool = new MFirebaseTool();
//        }
//
//        return mFirebaseTool;
//    }
//}

public class MFirebaseTool {
    private FirebaseOptions options;

    private static FirebaseApp firebaseApp;

    public static FirebaseApp getFirebaseApp() {
        return firebaseApp;
    }

    public MFirebaseTool() {
        try {
            System.out.println(System.getProperty("user.dir") + "\\src\\main\\resources\\key.json");
            FileInputStream serviceAccount = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\resources\\key.json");
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://space-5d8e0-default-rtdb.firebaseio.com/")
                    .build();

            firebaseApp = FirebaseApp.initializeApp(options, "SPACE");



            if (firebaseApp != null) {
                System.out.println(firebaseApp.getName());
            }

        } catch (IOException e) {
            Logger.getLogger(MFirebaseTool.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
