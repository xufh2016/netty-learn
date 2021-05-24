package com.coolfish.day1netty.crc;

/**
 * CRC16：CRC校验（参考HJ212）
 */
public class App
{
    public static String calcCrc16(String text) {
        byte[] data = text.getBytes();
        int crc = 0xffff;
        int dxs = 0xa001;
        int hibyte;
        int sbit;
        for (int i = 0; i < data.length; i++) {
            hibyte = crc >> 8;
            crc = hibyte ^ data[i];
            for (int j = 0; j < 8; j++) {
                sbit = crc & 0x0001;
                crc = crc >> 1;
                if (sbit == 1)
                    crc ^= dxs;
            }
        }
        String crcstr=Integer.toHexString(crc & 0xffff);
        if (crcstr.length()<4)
        {
            crcstr="0"+crcstr;
        }
        return crcstr;
    }
    public static void main( String[] args )
    {
        String  stmp = "QN=20160801085857223;ST=32;CN=1062;PW=100000;MN=010000A8900016F000169DC0;Flag=5;CP=&&RtdInterval=30&&";
        System.out.println(calcCrc16(stmp));
        //  1C80
    }
}



