package voxngine.threading;

public interface FutureCallback<R> {
	public void onSuccess(R result);
	void onFailure(Throwable e);
}
