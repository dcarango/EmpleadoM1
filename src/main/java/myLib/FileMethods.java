package myLib;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileMethods {
    // - Comprueba si un archivo fué modificado -
    /**
     * Notas:
     * Se entiende que si un archivo no existe en un backup incremtal, la variable modified es true y esto
     * ayuda a que el servicio cree una copy del file.
     * **/
    public boolean isModified (Path pathOrigin, Path pathDestination) throws IOException {

        boolean modified = false;

        if (!Files.exists(pathDestination)) {
            modified = true;
        } else {
            FileTime pathOriginDate = Files.getLastModifiedTime(pathOrigin);
            FileTime pathDestinationDate = Files.getLastModifiedTime(pathDestination);

            modified = pathOriginDate.compareTo(pathDestinationDate) > 0;
        }
        return modified;
    }

    // - Recibe una ruta, formatea fecha y hora la agrega a la ruta - (sirve para los dos tipos backUps)
    public Path dateName (Path path) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formatter);

        String folderName = path.getFileName().toString() + "_" + timestamp;
        path = Path.of("target", folderName);

        return path;
    }
}
