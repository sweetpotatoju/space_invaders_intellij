package spaceinvaders;


import java.util.ArrayList;
import java.util.HashMap;

public class GlobalStorage {
    private static GlobalStorage globalStorage = null;

    private String userID = "";

    private String userBestScore = "";

    private String userProfileImage = "";

    private String userTheme = "";

    private GlobalStorage() {

    }

    public static GlobalStorage getInstance() {
        if (globalStorage == null) {
            globalStorage = new GlobalStorage();
        }

        return globalStorage;
    }




    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserBestScore() {
        return userBestScore;
    }

    public void setUserBestScore(String userBestScore) {
        this.userBestScore = userBestScore;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public String getUserTheme() {
        return userTheme;
    }

    public void setUserTheme(String userTheme) {
        this.userTheme = userTheme;
    }
}