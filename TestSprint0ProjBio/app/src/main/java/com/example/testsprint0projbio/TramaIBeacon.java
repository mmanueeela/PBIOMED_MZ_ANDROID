
package com.example.testsprint0projbio;
// -----------------------------------------------------------------------------------
import java.util.Arrays;

// -----------------------------------------------------------------------------------
// @author: Jordi Bataller i Mascarell
// -----------------------------------------------------------------------------------

//**
// @brief Clase TramaIBeacon: Representa la estructura de una trama iBeacon y
// proporciona métodos para acceder a sus campos.
// */
public class TramaIBeacon {
    private byte[] prefijo = null; // 9 bytes
    private byte[] uuid = null; // 16 bytes
    private byte[] major = null; // 2 bytes
    private byte[] minor = null; // 2 bytes
    private byte txPower = 0; // 1 byte

    private byte[] losBytes;

    private byte[] advFlags = null; // 3 bytes
    private byte[] advHeader = null; // 2 bytes
    private byte[] companyID = new byte[2]; // 2 bytes
    private byte iBeaconType = 0 ; // 1 byte
    private byte iBeaconLength = 0 ; // 1 byte

    // -------------------------------------------------------------------------------
    // @brief Obtiene el prefijo de la trama iBeacon.
    //     * @return byte[]: El prefijo de 9 bytes.
    // -------------------------------------------------------------------------------
    public byte[] getPrefijo() {
        return prefijo;
    }

    // -------------------------------------------------------------------------------
    // @brief Obtiene el UUID de la trama iBeacon.
    //     * @return byte[]: El UUID de 16 bytes.
    // -------------------------------------------------------------------------------
    public byte[] getUUID() {
        return uuid;
    }

    // -------------------------------------------------------------------------------
    // @brief Obtiene el valor "Major" de la trama iBeacon.
    //     * @return byte[]: El valor "Major" de 2 bytes.
    // -------------------------------------------------------------------------------
    public byte[] getMajor() {
        return major;
    }

    // -------------------------------------------------------------------------------
    // @brief Obtiene el valor "Minor" de la trama iBeacon.
    //     * @return byte[]: El valor "Minor" de 2 bytes.
    // -------------------------------------------------------------------------------
    public byte[] getMinor() {
        return minor;
    }

    // -------------------------------------------------------------------------------
    // @brief Obtiene el valor "TxPower" de la trama iBeacon.
    //     * @return byte: El valor "TxPower" de 1 byte.
    // -------------------------------------------------------------------------------
    public byte getTxPower() {
        return txPower;
    }

    // -------------------------------------------------------------------------------
    // @brief Obtiene todos los bytes de la trama iBeacon.
    //     * @return byte[]: Los bytes de la trama iBeacon.
    // -------------------------------------------------------------------------------
    public byte[] getLosBytes() {
        return losBytes;
    }

    // -------------------------------------------------------------------------------
    // @brief Obtiene los Adv Flags de la trama iBeacon.
    //     * @return byte[]: Los Adv Flags de 3 bytes.
    // -------------------------------------------------------------------------------
    public byte[] getAdvFlags() {
        return advFlags;
    }

    // -------------------------------------------------------------------------------
    // @brief Obtiene el Adv Header de la trama iBeacon.
    //     * @return byte[]: El Adv Header de 2 bytes.
    // -------------------------------------------------------------------------------
    public byte[] getAdvHeader() {
        return advHeader;
    }

    // -------------------------------------------------------------------------------
    // @brief Obtiene el Company ID de la trama iBeacon.
    //     * @return byte[]: El Company ID de 2 bytes.
    // -------------------------------------------------------------------------------
    public byte[] getCompanyID() {
        return companyID;
    }

    // -------------------------------------------------------------------------------
    // @brief Obtiene el tipo de iBeacon de la trama iBeacon.
    //     * @return byte: El tipo de iBeacon de 1 byte.
    // -------------------------------------------------------------------------------
    public byte getiBeaconType() {
        return iBeaconType;
    }

    // -------------------------------------------------------------------------------
    // @brief Obtiene la longitud de iBeacon de la trama iBeacon.
    //     * @return byte: La longitud de iBeacon de 1 byte.
    // -------------------------------------------------------------------------------
    public byte getiBeaconLength() {
        return iBeaconLength;
    }

    // -------------------------------------------------------------------------------
    // @brief Constructor de la clase TramaIBeacon.
    //     * @param bytes byte[]: Los bytes de la trama iBeacon.
    // -------------------------------------------------------------------------------
    public TramaIBeacon(byte[] bytes ) {
        this.losBytes = bytes;

        prefijo = Arrays.copyOfRange(losBytes, 0, 8+1 ); // 9 bytes
        uuid = Arrays.copyOfRange(losBytes, 9, 24+1 ); // 16 bytes
        major = Arrays.copyOfRange(losBytes, 25, 26+1 ); // 2 bytes
        minor = Arrays.copyOfRange(losBytes, 27, 28+1 ); // 2 bytes
        txPower = losBytes[ 29 ]; // 1 byte

        advFlags = Arrays.copyOfRange( prefijo, 0, 2+1 ); // 3 bytes
        advHeader = Arrays.copyOfRange( prefijo, 3, 4+1 ); // 2 bytes
        companyID = Arrays.copyOfRange( prefijo, 5, 6+1 ); // 2 bytes
        iBeaconType = prefijo[ 7 ]; // 1 byte
        iBeaconLength = prefijo[ 8 ]; // 1 byte

    } // ()
} // class
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------