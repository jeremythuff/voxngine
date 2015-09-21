package voxngine.threading;

import java.util.concurrent.Future;

public class NonBlockingFuture<R> {
	
	private FutureCallback<R> handler;
    private R result;
    private Throwable failure;
    private boolean isCompleted;
    
    public void setCallback(FutureCallback<R> handler) {
        this.handler = handler;
        if (isCompleted) {
            if (failure != null) handler.onFailure(failure);
            else handler.onSuccess(result); 
        }
    }
 
    void setResult(R result) {
        this.result = result;
        this.isCompleted = true;
        if (handler != null) {
            handler.onSuccess(result);
        }
    }
 
    void setFailure(Throwable failure) {
        this.failure = failure;
        this.isCompleted = true;
        if (handler != null) {
            handler.onFailure(failure);
        }
    }

}
