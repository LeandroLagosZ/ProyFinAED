package App;

import Controller.MainController;
import View.GameHubView;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Iniciando GameHub...");
            
            // 1. Instanciamos el controlador principal (el orquestador)
            MainController mainController = new MainController();

            // 2. Cargamos los datos del dataset
            // Nota: Asegúrate de que games_200_extended.csv esté en la raíz de tu proyecto en Eclipse
            String rutaCSV = args.length > 0 ? args[0] : "games_200_extended.csv";
            mainController.inicializarSistema(rutaCSV);
            System.out.println("Datos cargados. Total en catálogo: " + mainController.getCatalogoController().cantidadJuegos());

            // 3. Levantamos la Interfaz Gráfica de Forma Segura
            SwingUtilities.invokeLater(() -> {
                GameHubView vista = new GameHubView(mainController);
                vista.setVisible(true);
            });

        } catch (Exception e) {
            System.err.println("Error crítico al iniciar el sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
