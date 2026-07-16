package br.com.locnf.infra.database.config;

public interface JdbcExecutor {

	<T> T execute(JdbcExecutorCallback<T> callback);

	default void execute(JdbcConsumer callback) {
		execute(conn -> {
			callback.accept(conn);
			return null;
		});
	}

}
