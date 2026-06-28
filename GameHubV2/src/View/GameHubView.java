package View;

import Controller.MainController;
import Model.Videojuego;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

//Eduardo Motta y Leandro Lagos
// Vista principal de la aplicacion GameHub usando Swing
public class GameHubView extends JFrame {

    private MainController mainController;
    private JTable tablaCatalogo;
    private DefaultTableModel modeloCatalogo;
    
    // Paleta de colores para el diseno claro
    private final Color BG_LIGHT = new Color(248, 249, 250);
    private final Color PANEL_LIGHT = new Color(255, 255, 255);
    private final Color SIDEBAR_LIGHT = new Color(233, 236, 239);
    private final Color ACCENT_COLOR = new Color(13, 110, 253);
    private final Color TEXT_DARK = new Color(33, 37, 41);
    private final Color TEXT_GRAY = new Color(108, 117, 125);

    private JPanel panelCentral;
    private CardLayout cardLayout;
    private JLabel lblStatusJugando;
    private JLabel lblStatusDescargando;

    // Componentes de busqueda y filtrado avanzado
    private JComboBox<String> comboTipoBusqueda;
    private JTextField txtBuscar;
    private JComboBox<String> comboGeneroFiltro;
    private JComboBox<String> comboPlataformaFiltro;
    private JTextField txtPrecioMin, txtPrecioMax;
    private JSpinner spinnerPuntajeMin;
    private JComboBox<String> comboMultijugadorFiltro;
    private JTextField txtDesarrolladorFiltro;
    private JTextField txtEditorFiltro;
    private JTextField txtTamanoMax;
    private JTextField txtAnioLanzamiento;

    public GameHubView(MainController mainController) {
        this.mainController = mainController;
        configurarVentana();
        inicializarComponentes();
        cargarCombosDinamicos();
        cargarDatosTabla(mainController.getCatalogoController().mostrarTodos());
        actualizarBarraEstado();
    }

    private void configurarVentana() {
        setTitle("GameHub - Plataforma de Gestion de Videojuegos");
        setSize(1250, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_LIGHT);
    }

    private void inicializarComponentes() {
        // Sidebar lateral
        JPanel sidebar = crearSidebar();
        add(sidebar, BorderLayout.WEST);

        // Area central
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);
        panelCentral.setBackground(BG_LIGHT);
        panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));

        panelCentral.add(crearVistaBiblioteca(), "BIBLIOTECA");
        panelCentral.add(crearVistaRecomendaciones(), "DESCUBRIR");
        panelCentral.add(crearVistaRankings(), "RANKINGS");
        panelCentral.add(crearVistaGestor(), "GESTOR");

        add(panelCentral, BorderLayout.CENTER);

        // Barra de estado inferior
        JPanel statusBar = crearStatusBar();
        add(statusBar, BorderLayout.SOUTH);
    }

    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_LIGHT);
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel logo = new JLabel("GAMEHUB");
        logo.setForeground(ACCENT_COLOR);
        logo.setFont(new Font("SansSerif", Font.BOLD, 26));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        sidebar.add(crearBotonMenu("Biblioteca Central", "BIBLIOTECA"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(crearBotonMenu("Descubrir Similares", "DESCUBRIR"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(crearBotonMenu("Top Charts Globales", "RANKINGS"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(crearBotonMenu("Gestor de Actividad", "GESTOR"));

        return sidebar;
    }

    private JPanel crearVistaBiblioteca() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setOpaque(false);

        // Panel de cabecera para busquedas y filtros
        JPanel pnlNorte = new JPanel();
        pnlNorte.setLayout(new BoxLayout(pnlNorte, BoxLayout.Y_AXIS));
        pnlNorte.setOpaque(false);

        // Fila de busqueda inteligente
        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlBusqueda.setOpaque(false);
        
        pnlBusqueda.add(crearLabel("Explorar Catalogo", 22, TEXT_DARK));
        pnlBusqueda.add(Box.createHorizontalStrut(15));
        
        pnlBusqueda.add(new JLabel("Buscar por:"));
        comboTipoBusqueda = new JComboBox<>(new String[]{"ID", "Nombre", "Genero", "Desarrollador", "Editor"});
        pnlBusqueda.add(comboTipoBusqueda);

        txtBuscar = new JTextField(15);
        estilizarTextField(txtBuscar);
        pnlBusqueda.add(txtBuscar);

        JButton btnBuscar = crearBotonAccion("Buscar");
        btnBuscar.setToolTipText("Busqueda indexada rapida usando AVL o Hash");
        pnlBusqueda.add(btnBuscar);

        JButton btnRestaurar = crearBotonAccion("Mostrar Todo");
        pnlBusqueda.add(btnRestaurar);

        pnlNorte.add(pnlBusqueda);

        // Fila de filtros avanzados
        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        pnlFiltros.setOpaque(false);
        pnlFiltros.setBorder(BorderFactory.createTitledBorder("Filtros Avanzados"));

        pnlFiltros.add(new JLabel("Genero:"));
        comboGeneroFiltro = new JComboBox<>();
        pnlFiltros.add(comboGeneroFiltro);

        pnlFiltros.add(new JLabel("Plataforma:"));
        comboPlataformaFiltro = new JComboBox<>();
        pnlFiltros.add(comboPlataformaFiltro);

        pnlFiltros.add(new JLabel("Precio Max ($):"));
        txtPrecioMax = new JTextField(4);
        estilizarTextField(txtPrecioMax);
        pnlFiltros.add(txtPrecioMax);

        pnlFiltros.add(new JLabel("Puntaje Min:"));
        spinnerPuntajeMin = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10.0, 0.5));
        pnlFiltros.add(spinnerPuntajeMin);

        pnlFiltros.add(new JLabel("Multiplayer:"));
        comboMultijugadorFiltro = new JComboBox<>(new String[]{"Todos", "Si", "No"});
        pnlFiltros.add(comboMultijugadorFiltro);

        pnlFiltros.add(new JLabel("Tamano Max (GB):"));
        txtTamanoMax = new JTextField(4);
        estilizarTextField(txtTamanoMax);
        pnlFiltros.add(txtTamanoMax);

        pnlFiltros.add(new JLabel("Anio:"));
        txtAnioLanzamiento = new JTextField(4);
        estilizarTextField(txtAnioLanzamiento);
        pnlFiltros.add(txtAnioLanzamiento);

        JButton btnFiltrar = crearBotonAcento("Filtrar Catalogo");
        btnFiltrar.setToolTipText("Aplica filtros estables usando cola como buffer");
        pnlFiltros.add(btnFiltrar);

        pnlNorte.add(pnlFiltros);

        // Tabla de datos
        String[] columnas = {"ID", "Nombre", "Genero", "Plataforma", "Precio", "Puntuacion", "Desarrollador", "Editor", "Tamano (GB)", "Anio", "Multiplayer"};
        modeloCatalogo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCatalogo = new JTable(modeloCatalogo);
        estilizarTabla(tablaCatalogo);
        
        JScrollPane scroll = new JScrollPane(tablaCatalogo);
        scroll.getViewport().setBackground(PANEL_LIGHT);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230)));

        // Botones CRUD y acciones de juego
        JPanel pnlSur = new JPanel(new BorderLayout());
        pnlSur.setOpaque(false);

        // Botones CRUD
        JPanel pnlCrud = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlCrud.setOpaque(false);
        JButton btnRegistrar = crearBotonAcento("+ Registrar Videojuego");
        JButton btnModificar = crearBotonAccion("✏ Modificar");
        JButton btnEliminar = crearBotonAccion("❌ Eliminar");
        pnlCrud.add(btnRegistrar);
        pnlCrud.add(btnModificar);
        pnlCrud.add(btnEliminar);

        // Botones de simulacion
        JPanel pnlSimulacion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlSimulacion.setOpaque(false);
        JButton btnJugar = crearBotonAcento("▶ Jugar");
        JButton btnDescargar = crearBotonAccion("📥 Encolar Descarga");
        pnlSimulacion.add(btnDescargar);
        pnlSimulacion.add(btnJugar);

        pnlSur.add(pnlCrud, BorderLayout.WEST);
        pnlSur.add(pnlSimulacion, BorderLayout.EAST);

        // Eventos de controles
        
        // Registrar juego
        btnRegistrar.addActionListener(e -> {
            GameDialog dialog = new GameDialog(this, "Registrar Nuevo Videojuego", null);
            dialog.setVisible(true);
            if (dialog.isConfirmed() && dialog.getJuegoResult() != null) {
                try {
                    mainController.getCatalogoController().registrarVideojuego(dialog.getJuegoResult());
                    cargarDatosTabla(mainController.getCatalogoController().mostrarTodos());
                    cargarCombosDinamicos();
                    JOptionPane.showMessageDialog(this, "Videojuego registrado correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Modificar juego
        btnModificar.addActionListener(e -> {
            Videojuego v = obtenerJuegoSeleccionado();
            if (v == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un videojuego de la tabla para modificar.", "Atencion", JOptionPane.WARNING_MESSAGE);
                return;
            }
            GameDialog dialog = new GameDialog(this, "Modificar Videojuego", v);
            dialog.setVisible(true);
            if (dialog.isConfirmed() && dialog.getJuegoResult() != null) {
                try {
                    mainController.getCatalogoController().modificarVideojuego(v.getId(), dialog.getJuegoResult());
                    cargarDatosTabla(mainController.getCatalogoController().mostrarTodos());
                    cargarCombosDinamicos();
                    JOptionPane.showMessageDialog(this, "Videojuego modificado correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Eliminar juego
        btnEliminar.addActionListener(e -> {
            Videojuego v = obtenerJuegoSeleccionado();
            if (v == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un videojuego de la tabla para eliminar.", "Atencion", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "uSeguro que deseas eliminar el videojuego: " + v.getNombre() + "?", "Confirmar Eliminacion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    mainController.getCatalogoController().eliminarVideojuego(v.getId());
                    cargarDatosTabla(mainController.getCatalogoController().mostrarTodos());
                    cargarCombosDinamicos();
                    JOptionPane.showMessageDialog(this, "Videojuego eliminado con exito.", "Exito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Busqueda inteligente
        btnBuscar.addActionListener(e -> {
            String query = txtBuscar.getText().trim();
            if (query.isEmpty()) {
                cargarDatosTabla(mainController.getCatalogoController().mostrarTodos());
                return;
            }
            String tipo = comboTipoBusqueda.getSelectedItem().toString();
            switch (tipo) {
                case "ID":
                    Videojuego vid = mainController.getCatalogoController().buscarPorID(query);
                    cargarDatosTabla(vid != null ? new Videojuego[]{vid} : new Videojuego[0]);
                    break;
                case "Nombre":
                    Videojuego vnm = mainController.getCatalogoController().buscarPorNombre(query);
                    cargarDatosTabla(vnm != null ? new Videojuego[]{vnm} : new Videojuego[0]);
                    break;
                case "Genero":
                    cargarDatosTabla(mainController.getCatalogoController().buscarPorGenero(query));
                    break;
                case "Desarrollador":
                    cargarDatosTabla(mainController.getCatalogoController().buscarPorDesarrollador(query));
                    break;
                case "Editor":
                    cargarDatosTabla(mainController.getCatalogoController().buscarPorEditor(query));
                    break;
            }
        });

        btnRestaurar.addActionListener(e -> {
            txtBuscar.setText("");
            cargarDatosTabla(mainController.getCatalogoController().mostrarTodos());
        });

        // Filtros avanzados acumulativos
        btnFiltrar.addActionListener(e -> {
            Videojuego[] filtrados = mainController.getCatalogoController().mostrarTodos();
            
            // Filtrar por genero
            String gSel = comboGeneroFiltro.getSelectedItem().toString();
            if (!gSel.equals("Todos")) {
                filtrados = mainController.getCatalogoController().filtrarPorGenero(gSel);
            }
            
            // Filtrar por plataforma
            String pSel = comboPlataformaFiltro.getSelectedItem().toString();
            if (!pSel.equals("Todas")) {
                filtrados = filtrarColeccionTemporal(filtrados, v -> v.getPlataforma() != null && v.getPlataforma().equalsIgnoreCase(pSel));
            }

            // Filtrar por precio
            String pMaxStr = txtPrecioMax.getText().trim();
            if (!pMaxStr.isEmpty()) {
                try {
                    double pMax = Double.parseDouble(pMaxStr);
                    filtrados = filtrarColeccionTemporal(filtrados, v -> v.getPrecio() <= pMax);
                } catch (NumberFormatException ex) {}
            }

            // Filtrar por puntuacion
            double scoreMin = (Double) spinnerPuntajeMin.getValue();
            if (scoreMin > 0.0) {
                filtrados = filtrarColeccionTemporal(filtrados, v -> v.getPuntuacion() >= scoreMin);
            }

            // Filtrar por multiplayer
            String mSel = comboMultijugadorFiltro.getSelectedItem().toString();
            if (!mSel.equals("Todos")) {
                boolean isMulti = mSel.equals("Si");
                filtrados = filtrarColeccionTemporal(filtrados, v -> v.isMultijugador() == isMulti);
            }

            // Filtrar por tamano
            String sizeStr = txtTamanoMax.getText().trim();
            if (!sizeStr.isEmpty()) {
                try {
                    double sMax = Double.parseDouble(sizeStr);
                    filtrados = filtrarColeccionTemporal(filtrados, v -> v.getTamanoGb() <= sMax);
                } catch (NumberFormatException ex) {}
            }

            // Filtrar por anio
            String anioStr = txtAnioLanzamiento.getText().trim();
            if (!anioStr.isEmpty()) {
                filtrados = filtrarColeccionTemporal(filtrados, v -> v.getFechaLanzamiento() != null && v.getFechaLanzamiento().contains(anioStr));
            }

            cargarDatosTabla(filtrados);
        });

        // Jugar
        btnJugar.addActionListener(e -> {
            Videojuego v = obtenerJuegoSeleccionado();
            if (v != null) {
                mainController.getUserActionController().registrarJuegoJugado(v);
                actualizarBarraEstado();
                JOptionPane.showMessageDialog(this, "Iniciando juego: " + v.getNombre() + ". uQue te diviertas!", "Ejecutar", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un juego para jugar.", "Atencion", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Descargar
        btnDescargar.addActionListener(e -> {
            Videojuego v = obtenerJuegoSeleccionado();
            if (v != null) {
                mainController.getUserActionController().encolarDescarga(v);
                actualizarBarraEstado();
                JOptionPane.showMessageDialog(this, "Encolado para descargar: " + v.getNombre(), "Descargas", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un juego para encolar descarga.", "Atencion", JOptionPane.WARNING_MESSAGE);
            }
        });

        panel.add(pnlNorte, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(pnlSur, BorderLayout.SOUTH);
        return panel;
    }

    private interface CriterioFiltrado {
        boolean cumple(Videojuego v);
    }

    private Videojuego[] filtrarColeccionTemporal(Videojuego[] catalogo, CriterioFiltrado crit) {
        java.util.List<Videojuego> resultList = new java.util.ArrayList<>();
        for (Videojuego v : catalogo) {
            if (crit.cumple(v)) {
                resultList.add(v);
            }
        }
        return resultList.toArray(new Videojuego[0]);
    }

    private void cargarCombosDinamicos() {
        Videojuego[] todos = mainController.getCatalogoController().mostrarTodos();
        Set<String> generos = new HashSet<>();
        Set<String> plataformas = new HashSet<>();
        
        for (Videojuego v : todos) {
            if (v.getGenero() != null && !v.getGenero().trim().isEmpty()) {
                generos.add(v.getGenero().trim());
            }
            if (v.getPlataforma() != null && !v.getPlataforma().trim().isEmpty()) {
                plataformas.add(v.getPlataforma().trim());
            }
        }

        // Generos
        comboGeneroFiltro.removeAllItems();
        comboGeneroFiltro.addItem("Todos");
        for (String g : generos) {
            comboGeneroFiltro.addItem(g);
        }

        // Plataformas
        comboPlataformaFiltro.removeAllItems();
        comboPlataformaFiltro.addItem("Todas");
        for (String p : plataformas) {
            comboPlataformaFiltro.addItem(p);
        }
    }

    private JPanel crearVistaRecomendaciones() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setOpaque(false);
        
        panel.add(crearLabel("Motor de Descubrimiento", 24, TEXT_DARK), BorderLayout.NORTH);

        JTextArea txtRecomendados = new JTextArea("\n Selecciona un juego en la biblioteca central y haz clic en 'Generar' para explorar similitudes.");
        estilizarTextArea(txtRecomendados);

        JButton btnGenerar = crearBotonAcento("Generar Carrusel de Similitud");
        btnGenerar.setToolTipText("Genera sugerencias a partir del grafo ponderado");
        
        btnGenerar.addActionListener(e -> {
            Videojuego v = obtenerJuegoSeleccionado();
            if (v != null) {
                mainController.getRecommendationController().generarCarruselParaJuego(v);
                txtRecomendados.setText("\n  Sugerencias conectadas a: " + v.getNombre() + "\n\n");
                try {
                    for (int i = 0; i < 5; i++) {
                        txtRecomendados.append("  ✦ " + mainController.getRecommendationController().getSiguienteRecomendacion().getNombre() + "\n");
                    }
                } catch (Exception ex) {
                    txtRecomendados.append("  No hay suficientes conexiones en el grafo.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un juego en la Biblioteca primero.");
            }
        });

        panel.add(btnGenerar, BorderLayout.SOUTH);
        panel.add(new JScrollPane(txtRecomendados), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearVistaRankings() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 15));
        panel.setOpaque(false);

        JTextPane txtTopPuntos = new JTextPane();
        txtTopPuntos.setContentType("text/html");
        txtTopPuntos.setEditable(false);
        txtTopPuntos.setBackground(PANEL_LIGHT);
        
        JTextPane txtTopDescargas = new JTextPane();
        txtTopDescargas.setContentType("text/html");
        txtTopDescargas.setEditable(false);
        txtTopDescargas.setBackground(PANEL_LIGHT);

        JTextPane txtTopPrecios = new JTextPane();
        txtTopPrecios.setContentType("text/html");
        txtTopPrecios.setEditable(false);
        txtTopPrecios.setBackground(PANEL_LIGHT);

        JButton btnCargar = crearBotonAcento("Actualizar Rankings");
        btnCargar.setToolTipText("Genera rankings mediante monticulos binarios (MaxHeap)");
        
        btnCargar.addActionListener(e -> {
            try {
                // Top valoracion
                StringBuilder htmlPuntos = new StringBuilder("<html><body style='font-family:sans-serif; margin:10px; color:#212529;'>");
                htmlPuntos.append("<h3 style='color:#0d6efd; border-bottom:1px solid #dee2e6; padding-bottom:5px;'>★ TOP VALORACIÓN</h3>");
                htmlPuntos.append("<table width='100%' cellpadding='4' style='font-size:11px;'>");
                htmlPuntos.append("<tr style='color:#6c757d;'><th>#</th><th>Juego / Dev</th><th>Puntos</th></tr>");
                int rank = 1;
                for (Videojuego v : mainController.topPuntuacion(10)) {
                    htmlPuntos.append("<tr>")
                        .append("<td><b>#").append(rank++).append("</b></td>")
                        .append("<td><b>").append(v.getNombre()).append("</b><br><span style='color:#6c757d;'>").append(v.getDesarrollador()).append("</span><br><span style='color:#6c757d; font-size:9px;'>Descargas: ").append(v.getDescargas()).append("</span></td>")
                        .append("<td align='right' style='color:#ffc107;'><b>★ ").append(v.getPuntuacion()).append("</b></td>")
                        .append("</tr>");
                }
                htmlPuntos.append("</table></body></html>");
                txtTopPuntos.setText(htmlPuntos.toString());
                
                // Top descargas
                StringBuilder htmlDescargas = new StringBuilder("<html><body style='font-family:sans-serif; margin:10px; color:#212529;'>");
                htmlDescargas.append("<h3 style='color:#0d6efd; border-bottom:1px solid #dee2e6; padding-bottom:5px;'>📥 TOP DESCARGAS</h3>");
                htmlDescargas.append("<table width='100%' cellpadding='4' style='font-size:11px;'>");
                htmlDescargas.append("<tr style='color:#6c757d;'><th>#</th><th>Juego / Dev</th><th>Descargas</th></tr>");
                rank = 1;
                for (Videojuego v : mainController.topDescargas(10)) {
                    htmlDescargas.append("<tr>")
                        .append("<td><b>#").append(rank++).append("</b></td>")
                        .append("<td><b>").append(v.getNombre()).append("</b><br><span style='color:#6c757d;'>").append(v.getDesarrollador()).append("</span><br><span style='color:#6c757d; font-size:9px;'>Valoración: ★ ").append(v.getPuntuacion()).append("</span></td>")
                        .append("<td align='right' style='color:#0d6efd;'><b>").append(v.getDescargas()).append("</b></td>")
                        .append("</tr>");
                }
                htmlDescargas.append("</table></body></html>");
                txtTopDescargas.setText(htmlDescargas.toString());

                // Top precios
                StringBuilder htmlPrecios = new StringBuilder("<html><body style='font-family:sans-serif; margin:10px; color:#212529;'>");
                htmlPrecios.append("<h3 style='color:#198754; border-bottom:1px solid #dee2e6; padding-bottom:5px;'>💰 TOP PRECIO BASE</h3>");
                htmlPrecios.append("<table width='100%' cellpadding='4' style='font-size:11px;'>");
                htmlPrecios.append("<tr style='color:#6c757d;'><th>#</th><th>Juego / Dev</th><th>Precio</th></tr>");
                rank = 1;
                for (Videojuego v : mainController.topPrecio(10)) {
                    htmlPrecios.append("<tr>")
                        .append("<td><b>#").append(rank++).append("</b></td>")
                        .append("<td><b>").append(v.getNombre()).append("</b><br><span style='color:#6c757d;'>").append(v.getDesarrollador()).append("</span><br><span style='color:#6c757d; font-size:9px;'>Valoración: ★ ").append(v.getPuntuacion()).append("</span></td>")
                        .append("<td align='right' style='color:#198754;'><b>$").append(v.getPrecio()).append("</b></td>")
                        .append("</tr>");
                }
                htmlPrecios.append("</table></body></html>");
                txtTopPrecios.setText(htmlPrecios.toString());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JPanel pnlPuntos = new JPanel(new BorderLayout()); pnlPuntos.setOpaque(false);
        pnlPuntos.add(new JScrollPane(txtTopPuntos), BorderLayout.CENTER);
        
        JPanel pnlDescargas = new JPanel(new BorderLayout()); pnlDescargas.setOpaque(false);
        pnlDescargas.add(new JScrollPane(txtTopDescargas), BorderLayout.CENTER);

        JPanel pnlPrecios = new JPanel(new BorderLayout()); pnlPrecios.setOpaque(false);
        pnlPrecios.add(new JScrollPane(txtTopPrecios), BorderLayout.CENTER);

        panel.add(pnlPuntos);
        panel.add(pnlDescargas);
        panel.add(pnlPrecios);
        
        JPanel wrapper = new JPanel(new BorderLayout(0,20));
        wrapper.setOpaque(false);
        wrapper.add(crearLabel("Top Charts Globales (MaxHeap)", 24, TEXT_DARK), BorderLayout.NORTH);
        wrapper.add(panel, BorderLayout.CENTER);
        wrapper.add(btnCargar, BorderLayout.SOUTH);

        return wrapper;
    }

    private JPanel crearVistaGestor() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 20));
        panel.setOpaque(false);

        // Historial de pila
        JPanel pnlPila = new JPanel(new BorderLayout(0,10)); pnlPila.setOpaque(false);
        JTextArea txtHistorial = new JTextArea(); estilizarTextArea(txtHistorial);
        JButton btnVerPila = crearBotonAccion("Refrescar Historial");
        btnVerPila.setToolTipText("Muestra la pila de historial de juegos de forma LIFO");
        
        btnVerPila.addActionListener(e -> {
            txtHistorial.setText("\n  HISTORIAL DE SESIÓN (LIFO)\n\n");
            for (Object obj : mainController.getUserActionController().obtenerHistorialAsArray()) {
                txtHistorial.append("  " + obj.toString() + "\n");
            }
        });
        pnlPila.add(crearLabel("Mi Actividad", 18, TEXT_DARK), BorderLayout.NORTH);
        pnlPila.add(new JScrollPane(txtHistorial), BorderLayout.CENTER);
        pnlPila.add(btnVerPila, BorderLayout.SOUTH);

        // Historial de cola
        JPanel pnlCola = new JPanel(new BorderLayout(0,10)); pnlCola.setOpaque(false);
        JTextArea txtCola = new JTextArea(); estilizarTextArea(txtCola);
        JButton btnPopCola = crearBotonAcento("Procesar Siguiente Descarga");
        btnPopCola.setToolTipText("Procesa y remueve el primer juego en cola (FIFO)");
        
        btnPopCola.addActionListener(e -> {
            try {
                Videojuego v = mainController.getUserActionController().procesarSiguienteDescarga();
                txtCola.append("\n ✔ Completado: " + v.getNombre());
                actualizarBarraEstado();
            } catch (Exception ex) {
                txtCola.append("\n  Cola vacía.");
            }
        });
        pnlCola.add(crearLabel("Gestor de Descargas", 18, TEXT_DARK), BorderLayout.NORTH);
        pnlCola.add(new JScrollPane(txtCola), BorderLayout.CENTER);
        pnlCola.add(btnPopCola, BorderLayout.SOUTH);

        panel.add(pnlPila);
        panel.add(pnlCola);
        return panel;
    }

    private JPanel crearStatusBar() {
        JPanel status = new JPanel(new GridLayout(1, 2));
        status.setBackground(PANEL_LIGHT);
        status.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
            new EmptyBorder(10, 25, 10, 25)
        ));

        lblStatusJugando = new JLabel("⏵ Reproduciendo: Nada");
        lblStatusJugando.setForeground(ACCENT_COLOR);
        lblStatusJugando.setFont(new Font("SansSerif", Font.BOLD, 13));

        lblStatusDescargando = new JLabel("📥 En Cola: 0 pendientes");
        lblStatusDescargando.setForeground(TEXT_GRAY);
        lblStatusDescargando.setHorizontalAlignment(SwingConstants.RIGHT);

        status.add(lblStatusJugando);
        status.add(lblStatusDescargando);
        return status;
    }

    private void actualizarBarraEstado() {
        try {
            Videojuego ultimo = mainController.getUserActionController().obtenerUltimoJugado();
            lblStatusJugando.setText("⏵ Reproduciendo: " + ultimo.getNombre());
        } catch (Exception e) { 
            lblStatusJugando.setText("⏵ Reproduciendo: Nada"); 
        }

        try {
            Videojuego prox = mainController.getUserActionController().verSiguienteDescarga();
            int total = mainController.getUserActionController().obtenerCantidadDescargas();
            lblStatusDescargando.setText("📥 Siguiente descarga: " + prox.getNombre() + " (" + total + " en cola)");
        } catch (Exception e) { 
            lblStatusDescargando.setText("📥 Cola de descargas vacía"); 
        }
    }

    private void cargarDatosTabla(Videojuego[] juegos) {
        modeloCatalogo.setRowCount(0);
        for (Videojuego v : juegos) {
            if (v != null) {
                modeloCatalogo.addRow(new Object[]{
                    v.getId(), 
                    v.getNombre(), 
                    v.getGenero(), 
                    v.getPlataforma(), 
                    "$" + v.getPrecio(), 
                    v.getPuntuacion(),
                    v.getDesarrollador(),
                    v.getEditor(),
                    v.getTamanoGb() + " GB",
                    v.getFechaLanzamiento(),
                    v.isMultijugador() ? "Sí" : "No"
                });
            }
        }
    }

    private Videojuego obtenerJuegoSeleccionado() {
        int fila = tablaCatalogo.getSelectedRow();
        if (fila >= 0) {
            String id = modeloCatalogo.getValueAt(fila, 0).toString();
            return mainController.getCatalogoController().buscarPorID(id);
        }
        return null;
    }

    private JButton crearBotonMenu(String texto, String tarjeta) {
        JButton btn = new JButton(texto);
        btn.setForeground(TEXT_DARK);
        btn.setBackground(SIDEBAR_LIGHT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(250, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> cardLayout.show(panelCentral, tarjeta));
        return btn;
    }

    private JButton crearBotonAccion(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(PANEL_LIGHT);
        btn.setForeground(TEXT_DARK);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        return btn;
    }

    private JButton crearBotonAcento(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(ACCENT_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        return btn;
    }

    private JLabel crearLabel(String texto, int size, Color color) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(color);
        lbl.setFont(new Font("SansSerif", Font.BOLD, size));
        return lbl;
    }

    private void estilizarTextField(JTextField txt) {
        txt.setBackground(PANEL_LIGHT);
        txt.setForeground(TEXT_DARK);
        txt.setCaretColor(ACCENT_COLOR);
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218)),
            new EmptyBorder(5, 8, 5, 8)
        ));
    }

    private void estilizarTextArea(JTextArea txt) {
        txt.setBackground(PANEL_LIGHT);
        txt.setForeground(TEXT_DARK);
        txt.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txt.setEditable(false);
        txt.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230)));
    }

    private void estilizarTabla(JTable tabla) {
        tabla.setBackground(PANEL_LIGHT);
        tabla.setForeground(TEXT_DARK);
        tabla.setGridColor(new Color(233, 236, 239));
        tabla.setSelectionBackground(new Color(200, 220, 255));
        tabla.setSelectionForeground(TEXT_DARK);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabla.setRowHeight(30);

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(SIDEBAR_LIGHT);
        header.setForeground(TEXT_DARK);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }
}