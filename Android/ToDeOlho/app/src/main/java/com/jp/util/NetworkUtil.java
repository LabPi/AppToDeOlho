package com.jp.util;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by jp on 22/03/16.
 */
public class NetworkUtil {

    /**
     * Retorna o IP da rede que estiver conectado
     * @return String IP
     */
    public static String getIpAddress()
    {
        String ipAddress = "0.0.0.0";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ipAddress = inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
            return "0.0.0.0";
        }
        return ipAddress;
    }

    /**
     * Retorna o IP da rede Wifi
     * @return String IP
     */
    public static String getIpAddr(Context context)
    {
        //Recupera o geerenciador de WIFI
        WifiManager wifiManager = (WifiManager) context.getSystemService(Activity.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        //Recupera o IP configurado pela wifi
        int ip = wifiInfo.getIpAddress();
        //Configura o formato do ip com quatro octetos
        String ipString = String.format(
                "%d.%d.%d.%d",
                (ip & 0xff),
                (ip >> 8 & 0xff),
                (ip >> 16 & 0xff),
                (ip >> 24 & 0xff));
        return ipString;
    }
}
