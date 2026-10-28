import java.io.*;
import java.nio.file.*;


/**
 * Compara la version base (estado-inicial) con la refactorizada ejecutando ambas con las mismas
 * entradas simuladas de teclado y comparando lo que imprimen. Solo Java, sin scripts.
 *
 * Uso (desde la raiz del repo, con ambas versiones compiladas):
 *   java pruebas/ComparadorVersiones.java <carpeta-out-base> <carpeta-out-refactorizada>
 */
public class ComparadorVersiones {

    static final String MAIN_BASE = "Main";
    static final String MAIN_REFAC = "tpi.solicitudes.Main";

    // {nombre, entrada base, entrada refactorizada, resultado esperado}
    static final String[][] CASOS = {
        {"CP-01 flujo completo valido",     "cp01_flujo_valido",   "cp01_flujo_valido",   "IDENTICO"},
        {"CP-02 errores de negocio",        "cp02_errores_negocio","cp02_errores_negocio","IDENTICO"},
        {"CP-03 sistema vacio",             "cp03_sistema_vacio",  "cp03_sistema_vacio",  "IDENTICO"},
        {"CP-04 entradas invalidas",        "cp04_invalidas_base", "cp04_invalidas_refac","DIFIERE"},
        {"CP-05 texto en minusculas",       "cp05_minusculas",     "cp05_minusculas",     "DIFIERE"},
    };

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Uso: java pruebas/ComparadorVersiones.java <out-base> <out-refactorizada>");
            return;
        }
        Files.createDirectories(Paths.get("pruebas/salidas"));
        int fallos = 0;
        for (String[] caso : CASOS) {
            String salidaBase = ejecutar(args[0], MAIN_BASE, "pruebas/entradas/" + caso[1] + ".txt");
            String salidaRefac = ejecutar(args[1], MAIN_REFAC, "pruebas/entradas/" + caso[2] + ".txt");
            String id = caso[0].substring(0, 5).toLowerCase().replace("-", "");
            Files.write(Paths.get("pruebas/salidas/" + id + ".base.txt"), salidaBase.getBytes());
            Files.write(Paths.get("pruebas/salidas/" + id + ".refac.txt"), salidaRefac.getBytes());

            String real = salidaBase.equals(salidaRefac) ? "IDENTICO" : "DIFIERE";
            boolean ok = real.equals(caso[3]);
            if (!ok) fallos++;
            System.out.printf("%-32s esperado=%-8s real=%-8s %s%n", caso[0], caso[3], real, ok ? "[OK]" : "[FALLO]");
            if (real.equals("DIFIERE")) mostrarDiferencias(salidaBase, salidaRefac);
        }
        System.out.println("\nResultado: " + (CASOS.length - fallos) + " de " + CASOS.length + " casos como se esperaba");
        System.exit(fallos == 0 ? 0 : 1);
    }

    static String ejecutar(String classpath, String clasePrincipal, String entrada) throws Exception {
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        ProcessBuilder pb = new ProcessBuilder(java, "-cp", classpath, clasePrincipal);
        pb.redirectInput(new File(entrada));
        pb.redirectErrorStream(true);
        Process proceso = pb.start();
        String salida = new String(proceso.getInputStream().readAllBytes());
        proceso.waitFor();
        return salida.replace("\r\n", "\n");
    }

    /** Muestra solo las lineas que cambian (hasta 6), para ver rapido que difiere. */
    static void mostrarDiferencias(String base, String refac) {
        String[] a = base.split("\n");
        String[] b = refac.split("\n");
        int mostradas = 0;
        for (int i = 0; i < Math.max(a.length, b.length) && mostradas < 6; i++) {
            String x = i < a.length ? a[i] : "(sin linea)";
            String y = i < b.length ? b[i] : "(sin linea)";
            if (!x.equals(y)) {
                System.out.println("    linea " + (i + 1));
                System.out.println("      base : " + x);
                System.out.println("      refac: " + y);
                mostradas++;
            }
        }
    }
}
