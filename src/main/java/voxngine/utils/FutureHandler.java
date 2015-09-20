package voxngine.utils;

public interface FutureHandler<R> {
	public void onSuccess(R result);
	void onFailure(Throwable e);
}
