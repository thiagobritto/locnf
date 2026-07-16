package br.com.locnf.infra.database.config.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import br.com.locnf.infra.database.config.ConnectionProvider;
import br.com.locnf.infra.database.exceptions.DatabaseException;

public class SqliteConnectionProvider implements ConnectionProvider {

	@Override
	public Connection getConnection() {
		try {
			return DriverManager.getConnection("jdbc:sqlite:banco_locnfe.db");
		} catch (SQLException e) {
			throw new DatabaseException("Erro ao obter conexão com o banco", e);
		}
	}

}
