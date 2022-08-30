package com.brianbett.urembom.retrofit;

public interface UserInterface {
    void success (User currentUser);
    void failure (Throwable throwable);
    void errorExists(String message);
}
