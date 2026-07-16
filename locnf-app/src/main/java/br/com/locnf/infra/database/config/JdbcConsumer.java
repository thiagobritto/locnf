package br.com.locnf.infra.database.config;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface JdbcConsumer {

	void accept(Connection conn) throws SQLException;
	
}
