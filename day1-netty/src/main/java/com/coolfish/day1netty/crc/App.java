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
    /**
     *
     * @param src	数据
     * @param len	数据长度
     * @return
     */
    public static String crc16(String src, int len) {
        int crc = 0x0000FFFF;
        short tc;
        char sbit;
        for (int i = 0; i < len; i++) {
            tc = (short) (crc >>> 8);
            crc = ((tc ^ src.charAt(i)) & 0x00FF);
            for (int r = 0; r < 8; r++) {
                sbit = (char) (crc & 0x01);
                crc >>>= 1;
                if (sbit != 0)
                    crc = (crc ^ 0xA001) & 0x0000FFFF;
            }
        }
        String str=Integer.toHexString(crc);
        if(str.length()==3){
            return "0"+str.toUpperCase();
        }else if(str.length()==2){
            return "00"+str.toUpperCase();
        }else if(str.length()==1){
            return "000"+str.toUpperCase();
        }
        return str.toUpperCase();
    }
    public static void main( String[] args )
    {
        String  stmp = "QN=20160801085857223;ST=32;CN=1062;PW=100000;MN=010000A8900016F000169DC0;Flag=5;CP=&&RtdInterval=30&&";

        System.out.println(crc16(stmp,stmp.length()));
        //  1C80
    }
}



