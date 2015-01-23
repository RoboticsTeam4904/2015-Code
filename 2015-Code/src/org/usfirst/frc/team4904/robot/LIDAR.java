package org.usfirst.frc.team4904.robot;

import java.io.Serializable;
import java.math.BigInteger;

public class LIDAR implements Serializable{
	public LIDAR(){
		
	}
    public int connect(){
        
    	// TODO add RS-232 connect
    	
        return 0;
    }
    
    private byte[] read(int bytes) throws Exception{
        
    	// TODO RS-232 read
    	byte b[] = new byte[bytes];
    	
        return b;
    }
    
    private int checksum(byte[] data){
        int[] data_list = new int[10];
        for(int t = 0; t < 10; t++) data_list[t] = ((int) data[2*t]) + (data[2*1] << 8);
        
        int chk32 = 0;
        for(byte d = 0; d < data_list.length; d++) chk32 = (chk32 << 1) + data_list[d];
        
        int checksum = (chk32 & 0x7FFF) + (chk32 >> 15);
        checksum = checksum & 0x7FFF;
        return checksum;
    }
    
    private int[] scanline_b(byte angle) throws Exception{
        boolean insync = false;
        while(!insync){
            byte header = read(1)[0];
            if(header == (byte) 0xFA){
                byte scan = read(1)[0];
                if(scan == angle) insync = true;
            }
        }
        if(insync){
            byte[] b_speed = read(2);
            byte[][] b_data = new byte[4][];
            for(int i = 0; i < 4; i++){
                b_data[i] = read(4);
            }
            
            byte[] all_data  = {(byte) 0xFA, angle, b_speed[0], b_speed[1], b_data[0][0], b_data[0][1], b_data[0][2], b_data[0][3], b_data[1][0], b_data[1][1], b_data[1][2], b_data[1][3], b_data[2][0], b_data[2][1], b_data[2][2], b_data[2][3], b_data[3][0], b_data[3][1], b_data[3][2], b_data[3][3]};
            
            int[] dist = new int[4];
            for(int i = 0; i < 4; i++){
                b_data[i][1] &= 0x3F;
                dist[i] = new BigInteger(new byte[]{0, b_data[i][1], b_data[i][0]}).intValue();
            }
            
            byte[] checksum = read(2);
            
            int ch = ((int) checksum[0]) + (((int) checksum[1]) << 8);
            byte chk0 = (byte) checksum(all_data);
            byte chk1 = (byte) (checksum(all_data) >> 8);
            
            return dist;
        }
        return null;
    }
    
    public int[] getDists(){
        
        byte scanhdr = (byte) 0xA0;
        int[] Dists = new int[360];
        
        try{        
            for(int i = 0; i < 90; i++){
                int[] scanrange = scanline_b(scanhdr);
                int degree = 4*((int) (scanhdr - ((byte) 0xA0)));
                
                if(scanrange != null){
                    for(int j = 0; j < 4; j++){
                        if(scanrange[j] != 53) Dists[degree+j] = scanrange[0];
                        else Dists[degree+j] = 0;
                    }
                }
                if(scanhdr == (byte) 0xF9) scanhdr = (byte) 0xA0;
                else scanhdr += 1;
            }
            
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }
        
        return Dists;
    }
    
    public int clean(){
        
    	// TODO add RS-232 cleanup port
    	
        return 0;
    }
	
}
