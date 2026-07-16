package br.com.locnf.infra.database.config.sqlite;

import java.sql.Connection;

import br.com.locnf.infra.database.config.ConnectionProvider;
import br.com.locnf.infra.database.config.JdbcExecutor;
import br.com.locnf.infra.database.config.JdbcExecutorCallback;
import br.com.locnf.infra.database.exceptions.DatabaseException;

public class SqliteJdbcExecutor implements JdbcExecutor {

	private final ConnectionProvider provider;

	public SqliteJdbcExecutor(ConnectionProvider provider) {
		this.provider = provider;
	}

	@Override
	public <T> T execute(JdbcExecutorCallback<T> callback) {
		try (Connection connection = provider.getConnection()) {
			return callback.execute(connection);
		} catch (Exception e) {
			throw new DatabaseException("Erro na operação JDBC", e);
		}
	}

}
