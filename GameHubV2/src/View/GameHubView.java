package View;

import Controller.MainController;
import Model.Videojuego;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
//Eduardo Motta, Leandro Lagos
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
    private JComboBox<String> comboMultijugadorFiltro;
    private JTextField txtTamanoMax;
    private JTextField txtAnioLanzamiento;

    // Componentes para la lista circular de recomendaciones
    private JLabel lblRecNombre;
    private JLabel lblRecGenero;
    private JLabel lblRecPlataforma;
    private JLabel lblRecPrecio;
    private JLabel lblRecPuntuacion;
    private JLabel lblRecDesarrollador;
    private JPanel cardRecomendacion;

    // Componentes para la seccion de favoritos
    private JTable tablaFavoritos;
    private DefaultTableModel modeloFavoritos;

    // Interfaz funcional para criterios de filtrado
    private interface CriterioFiltrado {
        boolean cumple(Videojuego v);
    }

    public GameHubView(MainController mainController) {
        this.mainController = mainController;
        configurarVentana();
        inicializarComponentes();
        cargarCombosDinamicos();
        cargarDatosTabla(mainController.getCatalogoController().mostrarTodos());
        actualizarBarraEstado();
    }

    private void configurarVentana() {
        setTitle("GameHub Dashboard - Administrador de Videojuegos");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_LIGHT);
    }

    private void inicializarComponentes() {
        // Sidebar lateral
        JPanel sidebar = crearSidebar();
        add(sidebar, BorderLayout.WEST);

        // Area central con CardLayout
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);
        panelCentral.setBackground(BG_LIGHT);
        panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));

        panelCentral.add(crearVistaBiblioteca(), "BIBLIOTECA");
        panelCentral.add(crearVistaRecomendaciones(), "DESCUBRIR");
        panelCentral.add(crearVistaRankings(), "RANKINGS");
        panelCentral.add(crearVistaGestor(), "GESTOR");
        panelCentral.add(crearVistaFavoritos(), "FAVORITOS");

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
        sidebar.add(crearBotonMenu("Mis Favoritos", "FAVORITOS"));
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
        
        pnlBusqueda.add(crearLabel("Explorar Catálogo", 22, TEXT_DARK));
        pnlBusqueda.add(Box.createHorizontalStrut(15));
        
        pnlBusqueda.add(new JLabel("Buscar por:"));
        comboTipoBusqueda = new JComboBox<>(new String[]{"ID", "Nombre", "Genero", "Desarrollador", "Editor"});
        pnlBusqueda.add(comboTipoBusqueda);

        txtBuscar = new JTextField(15);
        estilizarTextField(txtBuscar);
        pnlBusqueda.add(txtBuscar);

        JButton btnBuscar = crearBotonAccion("Buscar");
        btnBuscar.setToolTipText("Búsqueda indexada rápida usando AVL o Hash");
        pnlBusqueda.add(btnBuscar);

        JButton btnLimpiar = crearBotonAccion("Restaurar");
        pnlBusqueda.add(btnLimpiar);

        pnlNorte.add(pnlBusqueda);
        pnlNorte.add(Box.createRigidArea(new Dimension(0, 10)));

        // Panel de filtros avanzados
        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlFiltros.setOpaque(false);
        pnlFiltros.setBorder(BorderFactory.createTitledBorder("Filtrado Ponderado Avanzado"));

        pnlFiltros.add(new JLabel("Género:"));
        comboGeneroFiltro = new JComboBox<>();
        pnlFiltros.add(comboGeneroFiltro);

        pnlFiltros.add(new JLabel("Plataforma:"));
        comboPlataformaFiltro = new JComboBox<>();
        pnlFiltros.add(comboPlataformaFiltro);

        pnlFiltros.add(new JLabel("Precio Min:"));
        txtPrecioMin = new JTextField(3);
        estilizarTextField(txtPrecioMin);
        pnlFiltros.add(txtPrecioMin);

        pnlFiltros.add(new JLabel("Precio Max:"));
        txtPrecioMax = new JTextField(3);
        estilizarTextField(txtPrecioMax);
        pnlFiltros.add(txtPrecioMax);

        pnlFiltros.add(new JLabel("Multiplayer:"));
        comboMultijugadorFiltro = new JComboBox<>(new String[]{"Todos", "Si", "No"});
        pnlFiltros.add(comboMultijugadorFiltro);

        pnlFiltros.add(new JLabel("Tamaño Max (GB):"));
        txtTamanoMax = new JTextField(4);
        estilizarTextField(txtTamanoMax);
        pnlFiltros.add(txtTamanoMax);

        pnlFiltros.add(new JLabel("Año:"));
        txtAnioLanzamiento = new JTextField(4);
        estilizarTextField(txtAnioLanzamiento);
        pnlFiltros.add(txtAnioLanzamiento);

        JButton btnFiltrar = crearBotonAcento("Filtrar Catálogo");
        btnFiltrar.setToolTipText("Aplica filtros estables usando cola como buffer");
        pnlFiltros.add(btnFiltrar);

        pnlNorte.add(pnlFiltros);

        // Tabla de datos
        String[] columnas = {"ID", "Nombre", "Género", "Plataforma", "Precio", "Puntuación", "Desarrollador", "Editor", "Tamaño (GB)", "Año", "Multiplayer"};
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

        // Botones de simulación y favoritos
        JPanel pnlSimulacion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlSimulacion.setOpaque(false);
        JButton btnFavorito = crearBotonAccion("❤ Favorito");
        JButton btnJugar = crearBotonAcento("▶ Jugar");
        JButton btnDescargar = crearBotonAccion("📥 Encolar Descarga");
        pnlSimulacion.add(btnFavorito);
        pnlSimulacion.add(btnDescargar);
        pnlSimulacion.add(btnJugar);

        pnlSur.add(pnlCrud, BorderLayout.WEST);
        pnlSur.add(pnlSimulacion, BorderLayout.EAST);

        // Registro de eventos

        // Buscar
        btnBuscar.addActionListener(e -> {
            String criterio = comboTipoBusqueda.getSelectedItem().toString();
            String query = txtBuscar.getText().trim();
            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Escribe un término para buscar.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Videojuego[] encontrados = new Videojuego[0];
            if (criterio.equals("ID")) {
                Videojuego v = mainController.getCatalogoController().buscarPorID(query);
                if (v != null) encontrados = new Videojuego[]{v};
            } else if (criterio.equals("Nombre")) {
                Videojuego v = mainController.getCatalogoController().buscarPorNombre(query);
                if (v != null) encontrados = new Videojuego[]{v};
            } else if (criterio.equals("Género")) {
                encontrados = mainController.getCatalogoController().filtrarPorGenero(query);
            } else if (criterio.equals("Desarrollador")) {
                encontrados = mainController.getCatalogoController().filtrarPorDesarrollador(query);
            } else if (criterio.equals("Editor")) {
                encontrados = mainController.getCatalogoController().filtrarPorEditor(query);
            }
            cargarDatosTabla(encontrados);
        });

        // Limpiar
        btnLimpiar.addActionListener(e -> {
            txtBuscar.setText("");
            txtPrecioMin.setText("");
            txtPrecioMax.setText("");
            txtTamanoMax.setText("");
            txtAnioLanzamiento.setText("");
            comboGeneroFiltro.setSelectedIndex(0);
            comboPlataformaFiltro.setSelectedIndex(0);
            comboMultijugadorFiltro.setSelectedIndex(0);
            cargarDatosTabla(mainController.getCatalogoController().mostrarTodos());
        });

        // Filtrar
        btnFiltrar.addActionListener(e -> {
            Videojuego[] filtrados = mainController.getCatalogoController().mostrarTodos();

            // Filtrar por genero
            String genSel = comboGeneroFiltro.getSelectedItem().toString();
            if (!genSel.equals("Todos")) {
                filtrados = filtrarColeccionTemporal(filtrados, v -> v.getGenero() != null && v.getGenero().equalsIgnoreCase(genSel));
            }

            // Filtrar por plataforma
            String platSel = comboPlataformaFiltro.getSelectedItem().toString();
            if (!platSel.equals("Todas")) {
                filtrados = filtrarColeccionTemporal(filtrados, v -> v.getPlataforma() != null && v.getPlataforma().equalsIgnoreCase(platSel));
            }

            // Filtrar por rango de precios
            String pMinStr = txtPrecioMin.getText().trim();
            String pMaxStr = txtPrecioMax.getText().trim();
            if (!pMinStr.isEmpty() || !pMaxStr.isEmpty()) {
                double min = pMinStr.isEmpty() ? 0.0 : Double.parseDouble(pMinStr);
                double max = pMaxStr.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(pMaxStr);
                filtrados = filtrarColeccionTemporal(filtrados, v -> v.getPrecio() >= min && v.getPrecio() <= max);
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

        // Registrar juego
        btnRegistrar.addActionListener(e -> {
            GameDialog dialog = new GameDialog(this, "Registrar Nuevo Videojuego", null);
            dialog.setVisible(true);
            if (dialog.isConfirmed() && dialog.getJuegoResult() != null) {
                try {
                    mainController.getCatalogoController().registrarVideojuego(dialog.getJuegoResult());
                    cargarDatosTabla(mainController.getCatalogoController().mostrarTodos());
                    cargarCombosDinamicos();
                    JOptionPane.showMessageDialog(this, "Videojuego registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Modificar juego
        btnModificar.addActionListener(e -> {
            Videojuego v = obtenerJuegoSeleccionado();
            if (v == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un videojuego de la tabla para modificar.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            GameDialog dialog = new GameDialog(this, "Modificar Videojuego", v);
            dialog.setVisible(true);
            if (dialog.isConfirmed() && dialog.getJuegoResult() != null) {
                try {
                    mainController.getCatalogoController().modificarVideojuego(v.getId(), dialog.getJuegoResult());
                    cargarDatosTabla(mainController.getCatalogoController().mostrarTodos());
                    cargarCombosDinamicos();
                    JOptionPane.showMessageDialog(this, "Videojuego modificado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Eliminar juego
        btnEliminar.addActionListener(e -> {
            Videojuego v = obtenerJuegoSeleccionado();
            if (v == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un videojuego de la tabla para eliminar.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar a " + v.getNombre() + "?", "Eliminar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    mainController.getCatalogoController().eliminarVideojuego(v.getId());
                    cargarDatosTabla(mainController.getCatalogoController().mostrarTodos());
                    cargarCombosDinamicos();
                    JOptionPane.showMessageDialog(this, "Videojuego eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Jugar
        btnJugar.addActionListener(e -> {
            Videojuego v = obtenerJuegoSeleccionado();
            if (v != null) {
                mainController.getUserActionController().registrarJuegoJugado(v);
                actualizarBarraEstado();
                JOptionPane.showMessageDialog(this, "Iniciando juego: " + v.getNombre() + ". ¡Que te diviertas!", "Ejecutar", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un juego para jugar.", "Atención", JOptionPane.WARNING_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "Selecciona un juego para encolar descarga.", "Atención", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Favorito
        btnFavorito.addActionListener(e -> {
            Videojuego v = obtenerJuegoSeleccionado();
            if (v != null) {
                if (mainController.getUserActionController().esFavorito(v.getId())) {
                    mainController.getUserActionController().eliminarFavorito(v);
                    JOptionPane.showMessageDialog(this, "Removido de favoritos: " + v.getNombre(), "Favoritos", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    mainController.getUserActionController().agregarFavorito(v);
                    JOptionPane.showMessageDialog(this, "Agregado a favoritos con éxito: " + v.getNombre(), "Favoritos", JOptionPane.INFORMATION_MESSAGE);
                }
                cargarDatosFavoritos();
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un juego en la biblioteca para guardarlo en favoritos.", "Atención", JOptionPane.WARNING_MESSAGE);
            }
        });

        panel.add(pnlNorte, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(pnlSur, BorderLayout.SOUTH);

        return panel;
    }

    // Filtra la coleccion temporal usando DoubleLinkedList para evitar librerias estandar de Java
    private Videojuego[] filtrarColeccionTemporal(Videojuego[] catalogo, CriterioFiltrado crit) {
        Structures.DoubleLinkedList<Videojuego> list = new Structures.DoubleLinkedList<>();
        for (Videojuego v : catalogo) {
            if (v != null && crit.cumple(v)) {
                list.insertLast(v);
            }
        }
        Object[] arr = list.toArray();
        Videojuego[] res = new Videojuego[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = (Videojuego) arr[i];
        }
        return res;
    }

    // Carga los generos y plataformas de forma unica usando nuestra HashTable
    private void cargarCombosDinamicos() {
        Videojuego[] todos = mainController.getCatalogoController().mostrarTodos();
        Structures.HashTable<String, Boolean> generosMap = new Structures.HashTable<>(100);
        Structures.HashTable<String, Boolean> plataformasMap = new Structures.HashTable<>(100);
        Structures.DoubleLinkedList<String> generosList = new Structures.DoubleLinkedList<>();
        Structures.DoubleLinkedList<String> plataformasList = new Structures.DoubleLinkedList<>();
        
        for (Videojuego v : todos) {
            if (v != null) {
                String g = v.getGenero();
                if (g != null && !g.trim().isEmpty()) {
                    g = g.trim();
                    if (generosMap.get(g) == null) {
                        generosMap.put(g, true);
                        generosList.insertLast(g);
                    }
                }
                String p = v.getPlataforma();
                if (p != null && !p.trim().isEmpty()) {
                    p = p.trim();
                    if (plataformasMap.get(p) == null) {
                        plataformasMap.put(p, true);
                        plataformasList.insertLast(p);
                    }
                }
            }
        }

        // Cargar Generos
        comboGeneroFiltro.removeAllItems();
        comboGeneroFiltro.addItem("Todos");
        Object[] genArr = generosList.toArray();
        for (Object g : genArr) {
            comboGeneroFiltro.addItem((String) g);
        }

        // Cargar Plataformas
        comboPlataformaFiltro.removeAllItems();
        comboPlataformaFiltro.addItem("Todas");
        Object[] platArr = plataformasList.toArray();
        for (Object p : platArr) {
            comboPlataformaFiltro.addItem((String) p);
        }
    }

    // Vista de recomendaciones circulares (RF-RECOMENDACION)
    private JPanel crearVistaRecomendaciones() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setOpaque(false);
        
        panel.add(crearLabel("Motor de Descubrimiento Circular", 24, TEXT_DARK), BorderLayout.NORTH);

        cardRecomendacion = new JPanel();
        cardRecomendacion.setLayout(new BoxLayout(cardRecomendacion, BoxLayout.Y_AXIS));
        cardRecomendacion.setBackground(PANEL_LIGHT);
        cardRecomendacion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1, true),
            new EmptyBorder(30, 30, 30, 30)
        ));

        lblRecNombre = new JLabel("Ningún juego recomendado en este momento");
        lblRecNombre.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblRecNombre.setForeground(TEXT_DARK);
        lblRecNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblRecGenero = new JLabel("Género: N/A");
        lblRecGenero.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblRecGenero.setForeground(TEXT_GRAY);
        lblRecGenero.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblRecPlataforma = new JLabel("Plataforma: N/A");
        lblRecPlataforma.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblRecPlataforma.setForeground(TEXT_GRAY);
        lblRecPlataforma.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblRecPrecio = new JLabel("Precio: N/A");
        lblRecPrecio.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblRecPrecio.setForeground(new Color(25, 135, 84));
        lblRecPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblRecPuntuacion = new JLabel("Puntuación: ★ N/A");
        lblRecPuntuacion.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblRecPuntuacion.setForeground(new Color(255, 193, 7));
        lblRecPuntuacion.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblRecDesarrollador = new JLabel("Desarrolladora: N/A");
        lblRecDesarrollador.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblRecDesarrollador.setForeground(TEXT_GRAY);
        lblRecDesarrollador.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardRecomendacion.add(Box.createVerticalGlue());
        cardRecomendacion.add(lblRecNombre);
        cardRecomendacion.add(Box.createRigidArea(new Dimension(0, 15)));
        cardRecomendacion.add(lblRecGenero);
        cardRecomendacion.add(Box.createRigidArea(new Dimension(0, 10)));
        cardRecomendacion.add(lblRecPlataforma);
        cardRecomendacion.add(Box.createRigidArea(new Dimension(0, 10)));
        cardRecomendacion.add(lblRecDesarrollador);
        cardRecomendacion.add(Box.createRigidArea(new Dimension(0, 10)));
        cardRecomendacion.add(lblRecPrecio);
        cardRecomendacion.add(Box.createRigidArea(new Dimension(0, 10)));
        cardRecomendacion.add(lblRecPuntuacion);
        cardRecomendacion.add(Box.createVerticalGlue());

        JPanel pnlNavegacion = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        pnlNavegacion.setOpaque(false);

        JButton btnAnterior = crearBotonAccion("◀ Recomendación Anterior");
        btnAnterior.setToolTipText("Retroceder en la lista circular de recomendaciones");
        btnAnterior.setEnabled(false);

        JButton btnSiguiente = crearBotonAccion("Siguiente Recomendación ▶");
        btnSiguiente.setToolTipText("Avanzar en la lista circular de recomendaciones");
        btnSiguiente.setEnabled(false);

        pnlNavegacion.add(btnAnterior);
        pnlNavegacion.add(btnSiguiente);

        JPanel pnlSur = new JPanel(new BorderLayout(0, 10));
        pnlSur.setOpaque(false);

        JButton btnGenerar = crearBotonAcento("Generar Carrusel de Similitud");
        btnGenerar.setToolTipText("Genera sugerencias a partir del grafo de similitud y las carga en la lista circular");
        
        pnlSur.add(btnGenerar, BorderLayout.NORTH);
        pnlSur.add(pnlNavegacion, BorderLayout.SOUTH);

        panel.add(cardRecomendacion, BorderLayout.CENTER);
        panel.add(pnlSur, BorderLayout.SOUTH);

        // Acciones para navegacion circular
        btnGenerar.addActionListener(e -> {
            Videojuego v = obtenerJuegoSeleccionado();
            if (v != null) {
                mainController.getRecommendationController().generarCarruselParaJuego(v);
                try {
                    Videojuego rec = mainController.getRecommendationController().getSiguienteRecomendacion();
                    actualizarDetalleRecomendacion(rec);
                    btnAnterior.setEnabled(true);
                    btnSiguiente.setEnabled(true);
                } catch (Exception ex) {
                    lblRecNombre.setText("No hay suficientes conexiones en el grafo para " + v.getNombre());
                    lblRecGenero.setText("Género: N/A");
                    lblRecPlataforma.setText("Plataforma: N/A");
                    lblRecDesarrollador.setText("Desarrolladora: N/A");
                    lblRecPrecio.setText("Precio: N/A");
                    lblRecPuntuacion.setText("Puntuación: N/A");
                    btnAnterior.setEnabled(false);
                    btnSiguiente.setEnabled(false);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un juego en la Biblioteca primero para generar recomendaciones.", "Atención", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnSiguiente.addActionListener(e -> {
            try {
                Videojuego rec = mainController.getRecommendationController().getSiguienteRecomendacion();
                actualizarDetalleRecomendacion(rec);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "La lista circular está vacía.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnAnterior.addActionListener(e -> {
            try {
                Videojuego rec = mainController.getRecommendationController().getAnteriorRecomendacion();
                actualizarDetalleRecomendacion(rec);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "La lista circular está vacía.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return panel;
    }

    private void actualizarDetalleRecomendacion(Videojuego rec) {
        if (rec != null) {
            lblRecNombre.setText(rec.getNombre());
            lblRecGenero.setText("Género: " + rec.getGenero());
            lblRecPlataforma.setText("Plataforma: " + rec.getPlataforma());
            lblRecDesarrollador.setText("Desarrolladora: " + rec.getDesarrollador());
            lblRecPrecio.setText("Precio Especial: $" + rec.getPrecio());
            lblRecPuntuacion.setText("Puntuación Global: ★ " + rec.getPuntuacion());
            cardRecomendacion.repaint();
        }
    }

    // Rankings usando MaxHeap
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
        btnCargar.setToolTipText("Genera rankings mediante montículos binarios (MaxHeap)");
        
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

    // Vista de favoritos usando DoubleLinkedList
    private JPanel crearVistaFavoritos() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setOpaque(false);

        panel.add(crearLabel("Mis Videojuegos Favoritos (DoubleLinkedList)", 24, TEXT_DARK), BorderLayout.NORTH);

        String[] columnas = {"ID", "Nombre", "Género", "Plataforma", "Precio", "Puntuación", "Desarrollador"};
        modeloFavoritos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaFavoritos = new JTable(modeloFavoritos);
        estilizarTabla(tablaFavoritos);

        JScrollPane scroll = new JScrollPane(tablaFavoritos);
        scroll.getViewport().setBackground(PANEL_LIGHT);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230)));

        panel.add(scroll, BorderLayout.CENTER);

        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlSur.setOpaque(false);
        JButton btnQuitarFav = crearBotonAccion("💔 Quitar de Favoritos");
        pnlSur.add(btnQuitarFav);
        panel.add(pnlSur, BorderLayout.SOUTH);

        btnQuitarFav.addActionListener(e -> {
            int fila = tablaFavoritos.getSelectedRow();
            if (fila >= 0) {
                String id = modeloFavoritos.getValueAt(fila, 0).toString();
                Videojuego v = mainController.getCatalogoController().buscarPorID(id);
                if (v != null) {
                    mainController.getUserActionController().eliminarFavorito(v);
                    cargarDatosFavoritos();
                    JOptionPane.showMessageDialog(this, "Removido de favoritos correctamente.", "Favoritos", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un juego de la lista de favoritos para quitarlo.", "Atención", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panel;
    }

    private void cargarDatosFavoritos() {
        if (modeloFavoritos == null) return;
        modeloFavoritos.setRowCount(0);
        Videojuego[] favs = mainController.getUserActionController().obtenerFavoritos();
        for (Videojuego v : favs) {
            if (v != null) {
                modeloFavoritos.addRow(new Object[]{
                    v.getId(),
                    v.getNombre(),
                    v.getGenero(),
                    v.getPlataforma(),
                    "$" + v.getPrecio(),
                    v.getPuntuacion(),
                    v.getDesarrollador()
                });
            }
        }
    }

    // Gestor de Actividad (Pilas y Colas)
    private JPanel crearVistaGestor() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 20));
        panel.setOpaque(false);

        // Historial de pila (LIFO)
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

        // Historial de cola (FIFO)
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
        btn.addActionListener(e -> {
            cardLayout.show(panelCentral, tarjeta);
            if (tarjeta.equals("FAVORITOS")) {
                cargarDatosFavoritos();
            }
        });
        return btn;
    }

    private JButton crearBotonAccion(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(PANEL_LIGHT);
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
