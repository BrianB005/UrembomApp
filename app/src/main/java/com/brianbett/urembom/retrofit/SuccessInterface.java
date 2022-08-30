package com.brianbett.urembom.retrofit;

public interface SuccessInterface {
    void success(String message);
    void errorExists(String errorMessage);
    void failure(Throwable t);
}
