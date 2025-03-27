package dao;

import model.Pessoa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PessoaDAO {
    private Connection conexao;

    public PessoaDAO() {
        conectar();
    }

    public boolean conectar() {
        String driverName = "org.postgresql.Driver";
        String serverName = "localhost";
        String mydatabase = "teste";
        int porta = 8080;
        String url = "jdbc:postgresql://" + serverName + ":" + porta + "/" + mydatabase;
        String username = "ti2cc";
        String password = "ti@cc";
        boolean status = false;

        try {
            Class.forName(driverName);
            conexao = DriverManager.getConnection(url, username, password);
            status = (conexao != null);
            System.out.println("Conexão efetuada com o postgres!");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Conexão NÃO efetuada com o postgres -- " + e.getMessage());
        }
        return status;
    }

    public boolean close() {
        boolean status = false;
        try {
            if (conexao != null) {
                conexao.close();
                status = true;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return status;
    }

    public boolean insert(Pessoa pessoa) {
        boolean status = false;
        try {
            String sql = "INSERT INTO pessoa (nome, idade, cpf, email) VALUES (?, ?, ?, ?)";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, pessoa.getNome());
            st.setInt(2, pessoa.getIdade());
            st.setString(3, pessoa.getCpf());
            st.setString(4, pessoa.getEmail());
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public Pessoa get(int id) {
        Pessoa pessoa = null;
        try {
            String sql = "SELECT * FROM pessoa WHERE id=?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                pessoa = new Pessoa(rs.getInt("id"), rs.getString("nome"), rs.getInt("idade"),
                        rs.getString("cpf"), rs.getString("email"));
            }
            st.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return pessoa;
    }

    public List<Pessoa> getAll() {
        List<Pessoa> pessoas = new ArrayList<>();
        try {
            String sql = "SELECT * FROM pessoa";
            Statement st = conexao.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                pessoas.add(new Pessoa(rs.getInt("id"), rs.getString("nome"), rs.getInt("idade"),
                        rs.getString("cpf"), rs.getString("email")));
            }
            st.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return pessoas;
    }

    public boolean update(Pessoa pessoa) {
        boolean status = false;
        try {
            String sql = "UPDATE pessoa SET nome=?, idade=?, cpf=?, email=? WHERE id=?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, pessoa.getNome());
            st.setInt(2, pessoa.getIdade());
            st.setString(3, pessoa.getCpf());
            st.setString(4, pessoa.getEmail());
            st.setInt(5, pessoa.getId());
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean delete(int id) {
        boolean status = false;
        try {
            String sql = "DELETE FROM pessoa WHERE id=?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }
}
