import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.awt.Font;
import java.awt.BorderLayout;
import javax.swing.table.DefaultTableModel;

public class Bd {
    private JLabel Titulo;
    private JLabel User;
    private JLabel password;
    public JPanel mainPanel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton ingresarButton;

    private void cargarTabla(JTable tabla) {
        DefaultTableModel tabla1 = new DefaultTableModel(new String[]{"ID", "Nombre", "Descripción"}, 0);
        tabla.setModel(tabla1);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "Kevin100KWg_");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM datos")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                tabla1.addRow(new Object[]{id, nombre, descripcion});
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel, "Error al cargar los datos");
        }
    }

    public void MostrarDatos(String usuario) {
        JFrame datos = new JFrame("Datos");
        datos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        datos.setSize(800, 400);
        datos.setLayout(new BorderLayout());

        JLabel bienvenida = new JLabel("Bienvenido, " + usuario, SwingConstants.CENTER);
        bienvenida.setFont(new Font("Arial", Font.BOLD, 18));
        datos.add(bienvenida, BorderLayout.NORTH);

        JTable tabla = new JTable();
        cargarTabla(tabla);
        datos.add(new JScrollPane(tabla), BorderLayout.CENTER);

        datos.setVisible(true);
    }

    public boolean autenticacion(String usuario, String clave) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "Kevin100KWg_");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM usuarios WHERE username = ? AND password = ?")) {
            stmt.setString(1, usuario);
            stmt.setString(2, clave);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel, "Error al conectar con la base de datos");
            return false;
        }
    }

    public Bd() {
        ingresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = textField1.getText();
                String clave = new String(passwordField1.getPassword());

                if (autenticacion(usuario, clave)) {
                    JOptionPane.showMessageDialog(mainPanel, "Login exitoso");
                    MostrarDatos(usuario);
                }
                else {
                    JOptionPane.showMessageDialog(mainPanel, "Usuario o contraseña incorrectos");
                }
            }
        });
    }
}

