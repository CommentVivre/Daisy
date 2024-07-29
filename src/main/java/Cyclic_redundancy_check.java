/**
 * Author:
 * Time:
 */
public final class Cyclic_redundancy_check {
    /**
     * @param buffer 待计算校验数组
     * @return 计算结果
     */
    public static int CRC8(byte[] buffer) {
        int wCRCin = 0x00;
        int wCPoly = 0x07;
        for(byte b : buffer){
            for (int j = 0; j < 8; j++) {
                boolean bit = ((b >> (7 - j) & 1) == 1);
                boolean c07 = ((wCRCin >> 7 & 1) == 1);
                wCRCin <<= 1;
                if (c07 ^ bit)
                    wCRCin ^= wCPoly;
            }
        }
        wCRCin &= 0xFF;
        return wCRCin ^= 0x00;
    }
    /**
     * @param buffer 待计算校验数组
     * @return 计算结果
     */
    public static int CRC8_DARC(byte[] buffer) {
        int wCRCin = 0x00;
        int wCPoly = 0x9C;
        for(byte b : buffer){
            wCRCin ^= (b & 0xFF);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x01) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0x00;
    }
    /**
     * @param buffer 待计算校验数组
     * @return 计算结果
     */
    public static int CRC8_ITU(byte[] buffer) {
        int wCRCin = 0x00;
        int wCPoly = 0x07;
        for(byte b : buffer){
            for (int j = 0; j < 8; j++) {
                boolean bit = ((b >> (7 - j) & 1) == 1);
                boolean c07 = ((wCRCin >> 7 & 1) == 1);
                wCRCin <<= 1;
                if (c07 ^ bit)
                    wCRCin ^= wCPoly;
            }
        }
        wCRCin &= 0xFF;
        return wCRCin ^= 0x55;
    }
    /**
     * @param buffer 待计算校验数组
     * @return 计算结果
     */
    public static int CRC8_MAXIM(byte[] buffer) {
        int wCRCin = 0x00;
        int wCPoly = 0x8C;
        for(byte b : buffer){
            wCRCin ^= (b & 0xFF);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x01) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0x00;
    }
    /**
     * @param buffer 待计算校验数组
     * @return 计算结果
     */
    public static int CRC8_ROHC(byte[] buffer) {
        int wCRCin = 0xFF;
        // Integer.reverse(0x07) >>> 24
        int wCPoly = 0xE0;
        for(byte b : buffer){
            wCRCin ^= ((int)b & 0xFF);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x01) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0x00;
    }
    /**
     * @param buffer 待计算校验数组
     * @return 计算结果
     */
    public static int CRC16_CCITT(byte[] buffer) {
        int wCRCin = 0x0000;
        int wCPoly = 0x8408;
        for (byte b : buffer) {
            wCRCin ^= ((int) b & 0x00ff);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x0001) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0x0000;

    }

    /**
     * @param buffer 待计算校验数组
     * @return 计算结果
     */
    public static int CRC16_CCITT_FALSE(byte[] buffer) {
        int wCRCin = 0xffff;
        int wCPoly = 0x1021;
        for (byte b : buffer) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((wCRCin >> 15 & 1) == 1);
                wCRCin <<= 1;
                if (c15 ^ bit)
                    wCRCin ^= wCPoly;
            }
        }
        wCRCin &= 0xffff;
        return wCRCin ^= 0x0000;
    }

    /**
     * @param buffer 待计算校验数组
     * @return 计算结果
     */
    public static int CRC16_XMODEM(byte[] buffer) {
        int wCRCin = 0x0000;
        int wCPoly = 0x1021;
        for (byte b : buffer) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((wCRCin >> 15 & 1) == 1);
                wCRCin <<= 1;
                if (c15 ^ bit)
                    wCRCin ^= wCPoly;
            }
        }
        wCRCin &= 0xffff;
        return wCRCin ^= 0x0000;
    }


    /**
     * @param buffer 待计算校验数组
     * @return 计算结果
     */
    public static int CRC16_X25(byte[] buffer) {
        int wCRCin = 0xffff;
        int wCPoly = 0x8408;
        for (byte b : buffer) {
            wCRCin ^= ((int) b & 0x00ff);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x0001) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0xffff;
    }

    /**
     * @param buffer 待计算校验数组
     * @return 计算结果
     */
    public static int CRC16_MODBUS(byte[] buffer) {
        int wCRCin = 0xffff;
        int POLYNOMIAL = 0xa001;
        for (byte b : buffer) {
            wCRCin ^= ((int) b & 0x00ff);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x0001) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= POLYNOMIAL;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0x0000;
    }

    /**
     * @param buffer 待计算校验数组
     * @return 计算结果
     */
    public static int CRC16_IBM(byte[] buffer) {
        int wCRCin = 0x0000;
        int wCPoly = 0xa001;
        for (byte b : buffer) {
            wCRCin ^= ((int) b & 0x00ff);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x0001) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0x0000;
    }

    /**
     * @param buffer 待计算校验数组
     * @return 计算结果
     */
    public static int CRC16_MAXIM(byte[] buffer) {
        int wCRCin = 0x0000;
        int wCPoly = 0xa001;
        for (byte b : buffer) {
            wCRCin ^= ((int) b & 0x00ff);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x0001) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0xffff;
    }

    /**
     * @param buffer 待计算校验数组
     * @return 计算结果
     */
    public static int CRC16_USB(byte[] buffer) {
        int wCRCin = 0xFFFF;
        int wCPoly = 0xa001;
        for (byte b : buffer) {
            wCRCin ^= ((int) b & 0x00ff);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x0001) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0xffff;
    }

    /**
     * @param buffer 待计算校验数组
     * @return 计算结果
     */
    public static int CRC16_DNP(byte[] buffer) {
        int wCRCin = 0x0000;
        int wCPoly = 0xA6BC;
        for (byte b : buffer) {
            wCRCin ^= ((int) b & 0x00ff);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x0001) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0xffff;
    }
}