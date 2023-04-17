package spaceinvaders;

import java.util.HashMap;

public class ImagePath {

    private static HashMap<String, String> themeImageMap = new HashMap<>();
    private static HashMap<String, String> profileImageMap = new HashMap<>();


    public static HashMap<String, String> getThemeImagePathMap() {
        for (int i=1; i<=5; i++) {
            themeImageMap.put(String.valueOf(i), "src/main/resources/sprites/Theme" + i + ".jpg");

        }
        return themeImageMap;
    }

    public static HashMap<String, String> getProfileImagePathMap() {
        for (int i=1; i<=5; i++) {
            profileImageMap.put(String.valueOf(i), "/sprites/Profile" + i + ".png");
        }
        return profileImageMap;
    }
}
