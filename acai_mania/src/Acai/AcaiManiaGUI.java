package Acai;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AcaiManiaGUI extends JFrame implements ActionListener {
    private JTextField textFieldCliente;
    private JComboBox<String> comboBoxTamanho;
    private JButton buttonAcrescimo;
    private JTextArea textAreaAcrescimos;
    private JButton buttonFinalizar;
    private JLabel labelValorFinal;
    private int numeroPedido;
    private double valorAcrescimos;
    private double valorFinal;

    public AcaiManiaGUI() {
        // Configurar a janela
        setTitle("Açaí Mania");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        // Configurar a conexão JDBC
        String url = "jdbc:postgresql://localhost/acai_Mania";
        String username = "postgres";
        String password = "123456";
        Connection connection;

        try {
            connection = DriverManager.getConnection(url, username, password);

            // Gerar número do pedido automaticamente
            numeroPedido = gerarNumeroPedido(connection);

            // Label para o nome do cliente
            JLabel labelCliente = new JLabel("Nome do cliente:");
            add(labelCliente);

            // Text field para o nome do cliente
            textFieldCliente = new JTextField();
            add(textFieldCliente);

            // Label para o tamanho do açaí
            JLabel labelTamanho = new JLabel("Tamanho do açaí:");
            add(labelTamanho);

            // Combo box para o tamanho do açaí
            String[] opcoesTamanho = {"Pequena", "Média", "Grande"};
            comboBoxTamanho = new JComboBox<>(opcoesTamanho);
            comboBoxTamanho.addActionListener(this); // Adiciona o ouvinte de eventos ao combobox
            add(comboBoxTamanho);

            // Botão para adicionar acréscimos
            buttonAcrescimo = new JButton("Adicionar Acréscimo");
            buttonAcrescimo.addActionListener(this);
            add(buttonAcrescimo);

            // Text area para exibir os acréscimos
            textAreaAcrescimos = new JTextArea();
            textAreaAcrescimos.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textAreaAcrescimos);
            add(scrollPane);

            // Botão para finalizar o pedido
            buttonFinalizar = new JButton("Finalizar Pedido");
            buttonFinalizar.addActionListener(this);
            add(buttonFinalizar);

            // Label para exibir o valor final
            labelValorFinal = new JLabel("Valor Final: R$ 0.00");
            add(labelValorFinal);

            // Fechar a conexão com o banco de dados
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao conectar ao banco de dados:\n" + e.getMessage());
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                AcaiManiaGUI gui = new AcaiManiaGUI();
                gui.setVisible(true);
            }
        });
    }

    private int gerarNumeroPedido(Connection connection) throws SQLException {
        String query = "INSERT INTO pedidos (nome_cliente, tamanho_acai, acrescimos) VALUES ('temp', 'temp', 'temp') RETURNING numero_pedido";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        int numeroPedido = -1;
        if (resultSet.next()) {
            numeroPedido = resultSet.getInt(1);
        }
        resultSet.close();
        statement.close();
        return numeroPedido;
    }

    private double calcularValorAcrescimos(String acrescimo) {
        double valorAcrescimo = 0.0;
        switch (acrescimo) {
            case "Banana":
            case "Granola":
                valorAcrescimo = 1.0;
                break;
            case "Leite Condensado":
                valorAcrescimo = 1.5;
                break;
            case "Leite em Pó":
                valorAcrescimo = 0.5;
                break;
            case "Nutella":
                valorAcrescimo = 2.0;
                break;
        }
        return valorAcrescimo;
    }

    private void atualizarValorFinal() {
        valorFinal = 5.0 + valorAcrescimos;
        if (comboBoxTamanho.getSelectedItem().equals("Média")) {
            valorFinal += 2.0;
        } else if (comboBoxTamanho.getSelectedItem().equals("Grande")) {
            valorFinal += 3.0;
        }
        labelValorFinal.setText("Valor Final: R$ " + valorFinal);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonAcrescimo) {
            String[] opcoesAcrescimo = {"Nenhum", "Banana", "Granola", "Leite Condensado", "Leite em Pó", "Nutella"};
            String acrescimo = (String) JOptionPane.showInputDialog(null, "Escolha um acréscimo:", "Acréscimo", JOptionPane.PLAIN_MESSAGE, null, opcoesAcrescimo, null);
            if (acrescimo != null && !acrescimo.isEmpty() && !acrescimo.equals("Nenhum")) {
                valorAcrescimos += calcularValorAcrescimos(acrescimo);
                textAreaAcrescimos.append(acrescimo + "\n");
                atualizarValorFinal();
            }
        } else if (e.getSource() == buttonFinalizar) {
            String nomeCliente = textFieldCliente.getText();
            String tamanhoAcai = (String) comboBoxTamanho.getSelectedItem();

            if (nomeCliente.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, digite o nome do cliente.");
                return;
            }

            try {
                // Configurar a conexão JDBC
                String url = "jdbc:postgresql://localhost/acai_Mania";
                String username = "postgres";
                String password = "123456";
                Connection connection = DriverManager.getConnection(url, username, password);

                // Inserir pedido no banco de dados
                inserirPedido(connection, numeroPedido, nomeCliente, tamanhoAcai, valorAcrescimos, valorFinal);

                // Fechar a conexão com o banco de dados
                connection.close();

                // Mostrar resultado
                String mensagem = "Pedido realizado com sucesso!\n" +
                        "Número do pedido: " + numeroPedido + "\n" +
                        "Cliente: " + nomeCliente + "\n" +
                        "Tamanho do açaí: " + tamanhoAcai + "\n" +
                        "Acréscimos: R$ " + valorAcrescimos + "\n" +
                        "Valor final: R$ " + valorFinal;
                JOptionPane.showMessageDialog(null, mensagem);
                System.exit(0);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao realizar o pedido:\n" + ex.getMessage());
            }
        } else if (e.getSource() == comboBoxTamanho) {
            atualizarValorFinal(); // Atualiza o valor final quando o tamanho do açaí é selecionado
        }
    }

    private void inserirPedido(Connection connection, int numeroPedido, String nomeCliente, String tamanhoAcai, double valorAcrescimos, double valorFinal) throws SQLException {
        String query = "UPDATE pedidos SET nome_cliente = ?, tamanho_acai = ?, acrescimos = ?, valor_final = ? WHERE numero_pedido = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, nomeCliente);
        statement.setString(2, tamanhoAcai);
        if (valorAcrescimos > 0) {
            statement.setString(3, "Valor total dos acréscimos: R$ " + valorAcrescimos);
        } else {
            statement.setString(3, "Nenhum acréscimo");
        }
        statement.setDouble(4, valorFinal);
        statement.setInt(5, numeroPedido);
        statement.executeUpdate();
        statement.close();
    }
}
