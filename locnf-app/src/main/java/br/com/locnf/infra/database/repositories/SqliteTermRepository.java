package br.com.locnf.infra.database.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.locnf.domain.entities.Term;
import br.com.locnf.domain.exceptions.RepositoryException;
import br.com.locnf.domain.repositories.TermRepository;
import br.com.locnf.infra.database.config.JdbcExecutor;
import br.com.locnf.infra.database.exceptions.DatabaseException;

public class SqliteTermRepository implements TermRepository {

	private final JdbcExecutor executor;
	private final String tableName = "terms";

	public SqliteTermRepository(JdbcExecutor executor) {
		this.executor = executor;
		createTable();
	}

	private void createTable() {
		String sql = "CREATE TABLE IF NOT EXISTS %s (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL)";

		try {

			executor.execute(conn -> {
				try (Statement st = conn.createStatement()) {
					// posso usar o String.format(). Aqui?
					st.execute(String.format(sql, tableName));
				}
			});

		} catch (DatabaseException e) {
			throw new RepositoryException("Erro ao inicializar a tabela: " + tableName, e);
		}
	}

	@Override
	public void save(Term term) {
		try {
			if (term.getId() == null) {
				insert(term);
			} else {
				update(term);
			}
		} catch (DatabaseException e) {
			throw new RepositoryException("Erro ao salvar o termo: " + term.getName(), e);
		}
	}

	private void insert(Term term) {
		String sql = "INSERT INTO %s (name) VALUES (?)";

		Long generatedId = executor.execute(conn -> {
			Long id = null;
			
			try (PreparedStatement ps = conn.prepareStatement(String.format(sql, tableName),
					Statement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, term.getName());
				ps.executeUpdate();
				
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						id = rs.getLong(1);
					}
				}
			}
			
			return id;
		});

		if (generatedId != null) {
			term.setId(generatedId);
		}
	}

	private void update(Term term) {
		String sql = "UPDATE %s SET name = ? WHERE id = ?";

		executor.execute(conn -> {
			try (PreparedStatement ps = conn.prepareStatement(String.format(sql, tableName))) {
				ps.setString(1, term.getName());
				ps.setLong(2, term.getId());
				ps.executeUpdate();
			}
		});
	}

	@Override
	public void delete(Long id) {
		String sql = "DELETE FROM %s WHERE id = ?";

		try {
			executor.execute(conn -> {
				try (PreparedStatement st = conn.prepareStatement(String.format(sql, tableName))) {
					st.setLong(1, id);
					st.executeUpdate();
				}
			});
		} catch (DatabaseException e) {
			throw new RepositoryException("Erro ao remover termo: " + id, e);
		}
	}

	@Override
	public Term findById(Long id) {
		String sql = "SELECT * FROM %s WHERE id = ?";

		try {
			return executor.execute(conn -> {
				try (PreparedStatement st = conn.prepareStatement(String.format(sql, tableName))) {
					st.setLong(1, id);
					try (ResultSet rs = st.executeQuery()) {
						Term term = null;
						if (rs.next()) {
							term = new Term(id, rs.getString("name"));
						}
						return term;
					}
				}
			});
		} catch (DatabaseException e) {
			throw new RepositoryException("Erro ao obter termo: " + id, e);
		}
	}

	@Override
	public Term findByName(String termName) {
		String sql = "SELECT * FROM %s WHERE name = ?";

		try {
			return executor.execute(conn -> {
				try (PreparedStatement st = conn.prepareStatement(String.format(sql, tableName))) {
					st.setString(1, termName);
					try (ResultSet rs = st.executeQuery()) {
						Term term = null;
						if (rs.next()) {
							term = new Term(rs.getLong("id"), termName);
						}
						return term;
					}
				}
			});
		} catch (DatabaseException e) {
			throw new RepositoryException("Erro ao obter termo: " + termName, e);
		}
	}

	@Override
	public List<Term> findAll() {
		String sql = "SELECT * FROM %s";

		try {
			return executor.execute(conn -> {
				try (Statement st = conn.createStatement();
						ResultSet rs = st.executeQuery(String.format(sql, tableName))) {
					List<Term> termList = new ArrayList<>();
					while (rs.next()) {
						termList.add(new Term(rs.getLong("id"), rs.getString("name")));
					}
					return termList;
				}
			});
		} catch (DatabaseException e) {
			throw new RepositoryException("Erro ao listar termos.", e);
		}
	}

}
