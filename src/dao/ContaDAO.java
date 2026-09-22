package dao;

import exception.ContaNaoEncontradaException;
import model.ContaCorrente;

import java.sql.*;
import java.util.ArrayList;

public class ContaDAO {
    public static int inserir(String titular, double saldo) throws SQLException {
        String sql = "INSERT INTO contas (titular, saldo) VALUES (?, ?)";

        try (
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, titular);
            stmt.setDouble(2, saldo);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            throw new SQLException("Não foi possível obter o número gerado.");
        }
    }

    public static void depositar(int numero, double valor) throws SQLException, ContaNaoEncontradaException {
        String sql = "UPDATE contas SET saldo = saldo + ? WHERE numero = ?";

        try (
                Connection conn = Conexao.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setDouble(1, valor);
            stmt.setInt(2, numero);
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) { throw new ContaNaoEncontradaException("Conta não encontrada."); }
        }
    }

    public static void sacar(int numero, double valor) throws SQLException, ContaNaoEncontradaException {
        String sql = "UPDATE contas SET saldo = saldo - ? WHERE numero = ?";

        try (
                Connection conn = Conexao.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setDouble(1, valor);
            stmt.setInt(2, numero);
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) { throw new ContaNaoEncontradaException("Conta não encontrada."); }
        }
    }

    public static ArrayList<ContaCorrente> listar() throws SQLException {
        String sql = "SELECT numero, titular, saldo FROM contas";

        try (
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            ArrayList<ContaCorrente> contas = new ArrayList<>();

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
        String sql = "SELECT numero, titular, saldo FROM contas WHERE numero = ?";

        try (
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, numero);


            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ContaCorrente(
                        rs.getInt("numero"),
                        rs.getString("titular"),
                        rs.getDouble("saldo")
                    );
                }
            }

            return null;
        }
    }

    public static void atualizarSaldo(int numero, double novoSaldo) throws SQLException, ContaNaoEncontradaException {
        String sql = "UPDATE contas SET saldo = ? WHERE numero = ?";

        try (
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setDouble(1, novoSaldo);
            stmt.setInt(2, numero);
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) { throw new ContaNaoEncontradaException("Conta não encontrada."); }
        }
    }

    public static void remover(int numero) throws SQLException, ContaNaoEncontradaException {
        String sql = "DELETE FROM contas WHERE numero = ?";

        try (
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, numero);
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) { throw new ContaNaoEncontradaException("Conta não encontrada."); }
        }
    }

    public static void transferir(int numeroOrigem, int numeroDestino, double valor)
            throws SQLException, ContaNaoEncontradaException
    {
        String debitoSql = "UPDATE contas SET saldo = saldo - ? WHERE numero = ?";
        String creditoSql = "UPDATE contas SET saldo = saldo + ? WHERE numero = ?";

        try (
            Connection conn = Conexao.getConnection();
            PreparedStatement debitoStmt = conn.prepareStatement(debitoSql);
            PreparedStatement creditoStmt = conn.prepareStatement(creditoSql)
        ) {
            conn.setAutoCommit(false);

            try {
                debitoStmt.setDouble(1, valor);
                debitoStmt.setInt(2, numeroOrigem);
                int linhasDebito = debitoStmt.executeUpdate();

                if (linhasDebito == 0) {
                    throw new ContaNaoEncontradaException("Conta de origem não encontrada.");
                }

                creditoStmt.setDouble(1, valor);
                creditoStmt.setInt(2, numeroDestino);
                int linhasCredito = creditoStmt.executeUpdate();

                if (linhasCredito == 0) {
                    throw new ContaNaoEncontradaException("Conta de destino não encontrada.");
                }

                conn.commit();
            } catch (SQLException | ContaNaoEncontradaException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
