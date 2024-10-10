package com.example.testsprint0projbio;


import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

// -----------------------------------------------------------------------------------
// @author: Jordi Bataller i Mascarell
// -----------------------------------------------------------------------------------

//@brief Clase Utilidades: Proporciona métodos estáticos para manipulación de bytes, UUIDs y conversiones.
public class Utilidades {

    // -------------------------------------------------------------------------------
    // @brief Convierte una cadena de texto en un arreglo de bytes.
    //     * @param texto String: La cadena de texto a convertir.
    //     * @return byte[]: El arreglo de bytes resultante.
    // -------------------------------------------------------------------------------
    public static byte[] stringToBytes ( String texto ) {
        return texto.getBytes();
        // byte[] b = string.getBytes(StandardCharsets.UTF_8); // Ja
    } // ()

    // -------------------------------------------------------------------------------
    // @brief Convierte una cadena de 16 caracteres en un UUID.
    //     * @param uuid String: La cadena de texto a convertir en UUID.
    //     * @return UUID: El UUID resultante.
    //     * @throws Error si la cadena no tiene 16 caracteres.
    // -------------------------------------------------------------------------------
    public static UUID stringToUUID(String uuid ) {
        if ( uuid.length() != 16 ) {
            throw new Error( "stringUUID: string no tiene 16 caracteres ");
        }
        byte[] comoBytes = uuid.getBytes();

        String masSignificativo = uuid.substring(0, 8);
        String menosSignificativo = uuid.substring(8, 16);
        UUID res = new UUID( Utilidades.bytesToLong( masSignificativo.getBytes() ), Utilidades.bytesToLong( menosSignificativo.getBytes() ) );

        // Log.d( MainActivity.ETIQUETA_LOG, " \n\n*** stringToUUID * " + uuid  + "=?=" + Utilidades.uuidToString( res ) );

        // UUID res = UUID.nameUUIDFromBytes( comoBytes ); no va como quiero

        return res;
    } // ()

    // -------------------------------------------------------------------------------
    // @brief Convierte un UUID en una cadena de texto.
    //     * @param uuid UUID: El UUID a convertir.
    //     * @return String: La cadena de texto resultante.
    // -------------------------------------------------------------------------------
    public static String uuidToString ( UUID uuid ) {
        return bytesToString( dosLongToBytes( uuid.getMostSignificantBits(), uuid.getLeastSignificantBits() ) );
    } // ()

    // -------------------------------------------------------------------------------
    // @brief Convierte un UUID en una cadena hexadecimal.
    //     * @param uuid UUID: El UUID a convertir.
    //     * @return String: La cadena hexadecimal resultante.
    // -------------------------------------------------------------------------------
    public static String uuidToHexString ( UUID uuid ) {
        return bytesToHexString( dosLongToBytes( uuid.getMostSignificantBits(), uuid.getLeastSignificantBits() ) );
    } // ()

    // -------------------------------------------------------------------------------
    // @brief Convierte un arreglo de bytes en una cadena de texto.
    //     * @param bytes byte[]: El arreglo de bytes a convertir.
    //     * @return String: La cadena de texto resultante.
    // -------------------------------------------------------------------------------
    public static String bytesToString( byte[] bytes ) {
        if (bytes == null ) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append( (char) b );
        }
        return sb.toString();
    }

    // -------------------------------------------------------------------------------
    // @brief Convierte dos valores long en un arreglo de bytes.
    //     * @param masSignificativos long: Los bits más significativos.
    //     * @param menosSignificativos long: Los bits menos significativos.
    //     * @return byte[]: El arreglo de bytes resultante.
    // -------------------------------------------------------------------------------
    public static byte[] dosLongToBytes( long masSignificativos, long menosSignificativos ) {
        ByteBuffer buffer = ByteBuffer.allocate( 2 * Long.BYTES );
        buffer.putLong( masSignificativos );
        buffer.putLong( menosSignificativos );
        return buffer.array();
    }

    // -------------------------------------------------------------------------------
    // @brief Convierte un arreglo de bytes en un entero.
    //     * @param bytes byte[]: El arreglo de bytes a convertir.
    //     * @return int: El entero resultante.
    // -------------------------------------------------------------------------------
    public static int bytesToInt( byte[] bytes ) {
        return new BigInteger(bytes).intValue();
    }

    // -------------------------------------------------------------------------------
    // @brief Convierte un arreglo de bytes en un valor long.
    //     * @param bytes byte[]: El arreglo de bytes a convertir.
    //     * @return long: El valor long resultante.
    // -------------------------------------------------------------------------------
    public static long bytesToLong( byte[] bytes ) {
        return new BigInteger(bytes).longValue();
    }

    // -------------------------------------------------------------------------------
    // @brief Convierte un arreglo de bytes en un entero, manejando posibles errores.
    //     * @param bytes byte[]: El arreglo de bytes a convertir.
    //     * @return int: El entero resultante.
    //     * @throws Error si el arreglo de bytes es mayor a 4 bytes.
    // -------------------------------------------------------------------------------
    public static int bytesToIntOK( byte[] bytes ) {
        if (bytes == null ) {
            return 0;
        }

        if ( bytes.length > 4 ) {
            throw new Error( "demasiados bytes para pasar a int ");
        }
        int res = 0;



        for( byte b : bytes ) {
           /*
           Log.d( MainActivity.ETIQUETA_LOG, "bytesToInt(): byte: hex=" + Integer.toHexString( b )
                   + " dec=" + b + " bin=" + Integer.toBinaryString( b ) +
                   " hex=" + Byte.toString( b )
           );
           */
            res =  (res << 8) // * 16
                    + (b & 0xFF); // para quedarse con 1 byte (2 cuartetos) de lo que haya en b
        } // for

        if ( (bytes[ 0 ] & 0x8) != 0 ) {
            // si tiene signo negativo (un 1 a la izquierda del primer byte
            res = -((byte)res)-1; // complemento a 2 () de res pero como byte, -1
        }
       /*
        Log.d( MainActivity.ETIQUETA_LOG, "bytesToInt(): res = " + res + " ~res=" + (res ^ 0xffff)
                + "~res=" + ~((byte) res)
        );
        */

        return res;
    } // ()

    // -------------------------------------------------------------------------------
    // @brief Convierte un arreglo de bytes en una cadena hexadecimal.
    //     * @param bytes byte[]: El arreglo de bytes a convertir.
    //     * @return String: La cadena hexadecimal resultante.
    // -------------------------------------------------------------------------------
    public static String bytesToHexString( byte[] bytes ) {

        if (bytes == null ) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
            sb.append(':');
        }
        return sb.toString();
    } // ()

} // class
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------