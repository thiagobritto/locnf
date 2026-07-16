package br.com.locnf.infra.database.config;

import java.sql.Connection;

public interface ConnectionProvider {
	Connection getConnection();
}
