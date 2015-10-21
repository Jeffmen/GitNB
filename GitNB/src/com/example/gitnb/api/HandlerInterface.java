package com.example.gitnb.api;

public interface HandlerInterface <E> {
    public void onSuccess(E data);

    public void onSuccess(E data, int totalPages, int currentPage);

    public void onFailure(String error);
    
}
