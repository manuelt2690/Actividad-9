import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ValidadorContraseñas {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Validador de Contraseñas Concurrente");
        System.out.println("Ingrese el número de contraseñas a validar:");

        int numContraseñas = scanner.nextInt();
        scanner.nextLine();

        Thread[] hilos = new Thread[numContraseñas];

        for (int i = 0; i < numContraseñas; i++) {
            System.out.println("Ingrese la contraseña #" + (i + 1) + ":");
            String contraseña = scanner.nextLine();

            hilos[i] = new Thread(new ValidadorContraseña(contraseña, i + 1));
            hilos[i].start();
        }

        for (Thread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                System.out.println("Hilo interrumpido: " + e.getMessage());
            }
        }

        System.out.println("Proceso de validación completado.");
        scanner.close();
    }

    static class ValidadorContraseña implements Runnable {
        private final String contraseña;
        private final int numero;

        public ValidadorContraseña(String contraseña, int numero) {
            this.contraseña = contraseña;
            this.numero = numero;
        }

        @Override
        public void run() {
            System.out.println("Hilo " + numero + " iniciando validación para contraseña: " + contraseña);

            boolean valida = validarContraseña(contraseña);

            if (valida) {
                System.out.println("Contraseña #" + numero + " (" + contraseña + "): VÁLIDA");
            } else {
                System.out.println("Contraseña #" + numero + " (" + contraseña + "): INVÁLIDA");
            }
        }

        private boolean validarContraseña(String contraseña) {
            String regexLongitud = ".{8,}";
            String regexEspeciales = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*";
            String regexMayusculas = "^(.*[A-Z].*[A-Z].*)";
            String regexMinusculas = "^(.*[a-z].*[a-z].*[a-z].*)";
            String regexNumeros = ".*\\d.*";

            boolean cumpleLongitud = Pattern.matches(regexLongitud, contraseña);
            boolean cumpleEspeciales = Pattern.matches(regexEspeciales, contraseña);
            boolean cumpleMayusculas = Pattern.matches(regexMayusculas, contraseña);
            boolean cumpleMinusculas = Pattern.matches(regexMinusculas, contraseña);
            boolean cumpleNumeros = Pattern.matches(regexNumeros, contraseña);

            if (!cumpleLongitud) {
                System.out.println("  - La contraseña #" + numero + " no cumple con la longitud mínima de 8 caracteres");
            }
            if (!cumpleEspeciales) {
                System.out.println("  - La contraseña #" + numero + " no contiene caracteres especiales");
            }
            if (!cumpleMayusculas) {
                System.out.println("  - La contraseña #" + numero + " no contiene al menos 2 letras mayúsculas");
            }
            if (!cumpleMinusculas) {
                System.out.println("  - La contraseña #" + numero + " no contiene al menos 3 letras minúsculas");
            }
            if (!cumpleNumeros) {
                System.out.println("  - La contraseña #" + numero + " no contiene al menos 1 número");
            }

            return cumpleLongitud && cumpleEspeciales && cumpleMayusculas &&
                    cumpleMinusculas && cumpleNumeros;
        }
    }
}
