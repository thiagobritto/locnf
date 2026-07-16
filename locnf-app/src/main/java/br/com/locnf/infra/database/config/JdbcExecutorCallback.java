package br.com.locnf.infra.database.config;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface JdbcExecutorCallback<T> {

	T execute(Connection connection) throws SQLException;
	
}

