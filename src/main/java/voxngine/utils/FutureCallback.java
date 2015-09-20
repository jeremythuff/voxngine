package voxngine.utils;

public interface FutureCallback<R> {
	public void onSuccess(R result);
	void onFailure(Throwable e);
}
