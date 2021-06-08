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


    public static String getCrc(byte[] data) {
        int high;
        int flag;

        // 16位寄存器，所有数位均为1
        int wcrc = 0xffff;
        for (int i = 0; i < data.length; i++) {
            // 16 位寄存器的高位字节
            high = wcrc >> 8;
            // 取被校验串的一个字节与 16 位寄存器的高位字节进行“异或”运算
            wcrc = high ^ data[i];

            for (int j = 0; j < 8; j++) {
                flag = wcrc & 0x0001;
                // 把这个 16 寄存器向右移一位
                wcrc = wcrc >> 1;
                // 若向右(标记位)移出的数位是 1,则生成多项式 1010 0000 0000 0001 和这个寄存器进行“异或”运算
                if (flag == 1)
                    wcrc ^= 0xa001;
            }
        }
        return Integer.toHexString(wcrc);
    }

    public static void main( String[] args )
    {
        String crc = getCrc("QN=20160801085857223;ST=32;CN=1062;PW=100000;MN=010000A8900016F000169DC0;Flag=5;CP=&&RtdInterval=30&&".getBytes());
        System.out.println(crc);
        //{PtVersion=LY-1.05,MainType=work,InstModel=ly3023y,InstID=2B12345678,Date=2021-04-07,Time=11:54:40,DevState=0,SwVer=V5.22-0511+V6.00-0511}
//        String  stmp = "{PtVersion=LY-1.05,MainType=work,InstModel=ly3023y,InstID=2B12345678,Date=2021-04-07,Time=11:54:40,DevState=0,SwVer=V5.22-0511+V6.00-0511}";

//        System.out.println(crc16(stmp,stmp.length()));
        //  1C80
    }
}



