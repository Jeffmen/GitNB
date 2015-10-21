package com.example.gitnb.api;



public class DefaulHandlerImp {
    public static <E> void onFailure (HandlerInterface<E> handler, String error){
        try{
            handler.onFailure(error);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static <E> void  onSuccess(HandlerInterface<E> handler, E data){
        try{
            handler.onSuccess(data);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static <E> void  onSuccess(HandlerInterface<E> handler, E data, int totalPages, int currentPage){
        try{
            handler.onSuccess(data, totalPages, currentPage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
