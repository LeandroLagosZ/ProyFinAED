package View;

import Model.Videojuego;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
//Leandro Lagos
// Dialogo interactivo para registrar y modificar videojuegos con Swing
public class GameDialog extends JDialog {
    private JTextField txtId, txtNombre, txtGenero, txtPlataforma, txtPuntuacion, txtDescargas,
                       txtFecha, txtDesarrollador, txtEditor, txtPrecio, txtModo, txtTamaño;
    private JCheckBox chkMultijugador;
    private boolean isConfirmed = false;
    private Videojuego juegoResult = null;

    public GameDialog(Frame parent, String titulo, Videojuego juegoExistente) {
        super(parent, titulo, true);
        setSize(480, 580);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel pnlForm = new JPanel(new GridLayout(13, 2, 10, 10));
        pnlForm.setBorder(new EmptyBorder(25, 25, 25, 25));
        pnlForm.setBackground(Color.WHITE);

        pnlForm.add(new JLabel("ID del Videojuego:"));
        txtId = new JTextField();
        pnlForm.add(txtId);

        pnlForm.add(new JLabel("Nombre o Título:"));
        txtNombre = new JTextField();
        pnlForm.add(txtNombre);

        pnlForm.add(new JLabel("Género (Ej. RPG, Acción):"));
        txtGenero = new JTextField();
        pnlForm.add(txtGenero);

        pnlForm.add(new JLabel("Plataforma (Ej. PC, PS5, Xbox):"));
        txtPlataforma = new JTextField();
        pnlForm.add(txtPlataforma);

        pnlForm.add(new JLabel("Puntuación (0.0 a 10.0):"));
        txtPuntuacion = new JTextField();
        pnlForm.add(txtPuntuacion);

        pnlForm.add(new JLabel("Número de Descargas:"));
        txtDescargas = new JTextField();
        pnlForm.add(txtDescargas);

        pnlForm.add(new JLabel("Fecha Lanzamiento (DD/MM/AAAA):"));
        txtFecha = new JTextField();
        pnlForm.add(txtFecha);

        pnlForm.add(new JLabel("Desarrolladora (Studio):"));
        txtDesarrollador = new JTextField();
        pnlForm.add(txtDesarrollador);

        pnlForm.add(new JLabel("Distribuidora / Editor:"));
        txtEditor = new JTextField();
        pnlForm.add(txtEditor);

        pnlForm.add(new JLabel("Precio base (USD):"));
        txtPrecio = new JTextField();
        pnlForm.add(txtPrecio);

        pnlForm.add(new JLabel("Modo de Juego:"));
        txtModo = new JTextField();
        pnlForm.add(txtModo);

        pnlForm.add(new JLabel("Tamaño en Disco (GB):"));
        txtTamaño = new JTextField();
        pnlForm.add(txtTamaño);

        pnlForm.add(new JLabel("¿Cuenta con Multijugador?"));
        chkMultijugador = new JCheckBox("Habilitado");
        chkMultijugador.setBackground(Color.WHITE);
        pnlForm.add(chkMultijugador);

        // Cargar datos si es edicion de un juego existente
        if (juegoExistente != null) {
            txtId.setText(juegoExistente.getId());
            txtId.setEditable(false); // ID inmutable
            txtNombre.setText(juegoExistente.getNombre());
            txtGenero.setText(juegoExistente.getGenero());
            txtPlataforma.setText(juegoExistente.getPlataforma());
            txtPuntuacion.setText(String.valueOf(juegoExistente.getPuntuacion()));
            txtDescargas.setText(String.valueOf(juegoExistente.getDescargas()));
            txtFecha.setText(juegoExistente.getFechaLanzamiento());
            txtDesarrollador.setText(juegoExistente.getDesarrollador());
            txtEditor.setText(juegoExistente.getEditor());
            txtPrecio.setText(String.valueOf(juegoExistente.getPrecio()));
            txtModo.setText(juegoExistente.getModoJuego());
            txtTamaño.setText(String.valueOf(juegoExistente.getTamanoGb()));
            chkMultijugador.setSelected(juegoExistente.isMultijugador());
        }

        add(new JScrollPane(pnlForm), BorderLayout.CENTER);

        // Panel de botones inferior
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        pnlButtons.setBackground(new Color(245, 245, 245));
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnGuardar = new JButton("Guardar Datos");

        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> {
            try {
                // Validacion de campos obligatorios
                if (txtId.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El ID y el Nombre son campos estrictamente obligatorios.", "Campos Faltantes", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Crear objeto de tipo Videojuego con los datos parseados
                juegoResult = new Videojuego(
                    txtId.getText().trim(),
                    txtNombre.getText().trim(),
                    txtGenero.getText().trim(),
                    txtPlataforma.getText().trim(),
                    Double.parseDouble(txtPuntuacion.getText().trim().isEmpty() ? "0" : txtPuntuacion.getText().trim()),
                    Integer.parseInt(txtDescargas.getText().trim().isEmpty() ? "0" : txtDescargas.getText().trim()),
                    txtFecha.getText().trim(),
                    txtDesarrollador.getText().trim(),
                    txtEditor.getText().trim(),
                    Double.parseDouble(txtPrecio.getText().trim().isEmpty() ? "0" : txtPrecio.getText().trim()),
                    txtModo.getText().trim(),
                    chkMultijugador.isSelected(),
                    Double.parseDouble(txtTamaño.getText().trim().isEmpty() ? "0" : txtTamaño.getText().trim())
                );
                isConfirmed = true;
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error de formato: Asegurate de ingresar numeros validos.", "Error de Tipos", JOptionPane.ERROR_MESSAGE);
            }
        });

        pnlButtons.add(btnCancelar);
        pnlButtons.add(btnGuardar);
        add(pnlButtons, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public Videojuego getJuegoResult() {
        return juegoResult;
    }
}
