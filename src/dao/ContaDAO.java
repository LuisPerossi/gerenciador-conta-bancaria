package dao;

import model.ContaCorrente;

import java.sql.*;
import java.util.ArrayList;

public class ContaDAO {
    public static int inserir(ContaCorrente conta) throws SQLException {
        try (Connection conn = Conexao.getConnection()) {
            String sql = "INSERT INTO contas (titular, saldo) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, conta.getTitular());
            stmt.setDouble(2, conta.getSaldo());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            throw new SQLException("Não foi possível obter o número gerado.");
        }
    }

    public static ArrayList<ContaCorrente> listar() throws SQLException {
        try (Connection conn = Conexao.getConnection()) {
            ArrayList<ContaCorrente> contas = new ArrayList<>();

            String sql = "SELECT * FROM contas";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                contas.add(new ContaCorrente(
                        rs.getInt("numero"),
                        rs.getString("titular"),
                        rs.getDouble("saldo"))
                );
            }

            return contas;
        }
    }

    public static ContaCorrente buscarPorNumero(int numero) throws SQLException {
        try (Connection conn = Conexao.getConnection()) {
            String sql = "SELECT * FROM contas WHERE numero = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, numero);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new ContaCorrente(
                        rs.getInt("numero"),
                        rs.getString("titular"),
                        rs.getDouble("saldo")
                );
            }

            return null;
        }
    }

    public static void atualizarSaldo(int numero, double novoSaldo) throws SQLException {
        try (Connection conn = Conexao.getConnection()) {
            String sql = "UPDATE contas SET saldo = ? WHERE numero = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, novoSaldo);
            stmt.setInt(2, numero);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) { throw new SQLException("Conta não encontrada: " + numero); }
        }
    }

    public static void remover(int numero) throws SQLException {
        try (Connection conn = Conexao.getConnection()) {
            String sql = "DELETE FROM contas WHERE numero = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, numero);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) { throw new SQLException("Conta não encontrada: " + numero); }
        }
    }

    public static void transferir(int numeroOrigem, int numeroDestino, double valor) throws SQLException {
        String debitoSql = "UPDATE contas SET saldo = saldo - ? WHERE numero = ?";
        String creditoSql = "UPDATE contas SET saldo = saldo + ? WHERE numero = ?";

        try (
            Connection conn = Conexao.getConnection();
            PreparedStatement debitoStmt = conn.prepareStatement(debitoSql);
            PreparedStatement creditoStmt = conn.prepareStatement(creditoSql);
        ) {
            conn.setAutoCommit(false);

            try {
                debitoStmt.setDouble(1, valor);
                debitoStmt.setInt(2, numeroOrigem);
                int linhasDebito = debitoStmt.executeUpdate();

                if (linhasDebito == 0) {
                    throw new SQLException(String.format("Conta de origem não encontrada: %s.", numeroOrigem));
                }

                creditoStmt.setDouble(1, valor);
                creditoStmt.setInt(2, numeroDestino);
                int linhasCredito = creditoStmt.executeUpdate();

                if (linhasCredito == 0) {
                    throw new SQLException(String.format("Conta de destino não encontrada: %s.", numeroDestino));
                }

                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
