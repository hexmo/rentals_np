package np.com.ranjan.rental.Model;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String userId;
    private String fullName;
    private String phoneNumber;
    private String profilePicture;

    /**
     * Non-parameterized constructor
     */
    public UserModel() {
    }

    /**
     * Parameterized constructor
     * @param userId  id of user
     * @param fullName full name of user
     * @param phoneNumber phone no of user
     * @param profilePicture Uri of user profile picture
     */
    public UserModel(String userId, String fullName, String phoneNumber, String profilePicture) {
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
    }

    //Getters

    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    //Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}