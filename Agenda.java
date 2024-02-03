import java.io.*;
import java.util.*;

public class Agenda {
    private static final String FILE_NAME = "agenda.dat";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("Agenda Telefónica");
            System.out.println("1. Agregar contacto");
            System.out.println("2. Eliminar contacto");
            System.out.println("3. Mostrar agenda");
            System.out.println("4. Salir");
            System.out.print("Ingrese una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    agregarContacto();
                    break;
                case 2:
                    eliminarContacto();
                    break;
                case 3:
                    mostrarAgenda();
                    break;
                case 4:
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción inválida");
                    break;
            }
        } while (opcion != 4);
    }

    private static void agregarContacto() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME, true))) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Ingrese el nombre del contacto: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese el teléfono del contacto: ");
            String telefono = scanner.nextLine();

            Contacto contacto = new Contacto(nombre, telefono);
            outputStream.writeObject(contacto);

            System.out.println("Contacto agregado correctamente");
        } catch (IOException e) {
            System.out.println("Error al agregar el contacto: " + e.getMessage());
        }
    }

    private static void eliminarContacto() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILE_NAME));
             ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("temp.dat"))) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Ingrese el nombre del contacto a eliminar: ");
            String nombre = scanner.nextLine();

            Contacto contacto;
            boolean encontrado = false;

            while (true) {
                try {
                    contacto = (Contacto) inputStream.readObject();

                    if (!contacto.getNombre().equalsIgnoreCase(nombre)) {
                        outputStream.writeObject(contacto);
                    } else {
                        encontrado = true;
                    }
                } catch (EOFException e) {
                    break;
                }
            }

            if (encontrado) {
                File file = new File(FILE_NAME);
                file.delete();
                File tempFile = new File("temp.dat");
                tempFile.renameTo(file);
                System.out.println("Contacto eliminado correctamente");
            } else {
                System.out.println("No se encontró el contacto");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al eliminar el contacto: " + e.getMessage());
        }
    }

    private static void mostrarAgenda() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println("Agenda:");

            while (true) {
                try {
                    Contacto contacto = (Contacto) inputStream.readObject();
                    System.out.println(contacto);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al mostrar la agenda: " + e.getMessage());
        }
    }
}

class Contacto implements Serializable {
    private String nombre;
    private String telefono;

    public Contacto(String nombre, String telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + ", Teléfono: " + telefono;
    }
}