/**************************************************************************************
 *Projeto: App Agencia Mercado Central
 *Descricao: classe responsavel pelo gerenciamento de resolucao de video e componentes
 *Autor: Silvano Malfatti
 * Local : Palmas-TO
 **************************************************************************************/

//Pacote de recursos
package com.jp.util;

import android.content.Context;

public class GerenciadorTela
{
    //Atributos da classe
    private static int largura = 0;
    private static int altura = 0;
    private static int orientacao = 0;

    public static final int PORTRAIT = 1;
    public static final int LANDSCAPE = 2;

    public static void inicializaDisplay(Context pContext)
    {
        largura = pContext.getResources().getDisplayMetrics().widthPixels;
        altura = pContext.getResources().getDisplayMetrics().heightPixels;
        orientacao = pContext.getResources().getConfiguration().orientation;

    }
    public static int larguraPercentagem(int percent)
    {
        return (percent * largura) / 100;
    }

    public static int alturaPercentagem(int percent)
    {
        return (percent * altura) / 100;
    }

    public static int getLarguraTela()
    {
        return largura;
    }

    public static int getAlturaTela()
    {
        return altura;
    }

    /**
     * Indefinido = 0;
     * Portrait = 1;
     * Landscape = 2;
     */
    public static int getOrientacao()
    {
        return orientacao;
    }
}