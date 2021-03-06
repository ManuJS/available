package com.aprendizagem.manu.boaviagemapp.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class Contract {

    private Contract() {
    }

    public static final String CONTENT_AUTHORITY = "com.aprendizagem.manu.boaviagemapp";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_VIAGENS = "viagens";
    public static final String PATH_GASTOS = "gastos";
    public static final String PATH_IMAGENS = "imagens";

    public static final class ViagemEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_VIAGENS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIAGENS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIAGENS;

        public final static String TABLE_NAME = "viagens";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DESTINO = "destino";
        public final static String COLUMN_RAZAO = "razao";
        public final static String COLUMN_LOCAL_ACOMODACAO = "local_acomodacao";
        public final static String COLUMN_DATA_CHEGADA = "data_chegada";
        public final static String COLUMN_DATA_PARTIDA = "data_saida";
        public final static String COLUMN_GASTO_TOTAL = "gasto_total";
        public final static String COLUMN_ID_USUARIO = "id_usuario";

        public static final int RAZAO_DESCONHECIDA= 0;
        private static final int RAZAO_LAZER = 1;
        private static final int RAZAO_TRABALHO = 2;

        public static boolean getRazaoDaViagem(int razao) {
            return razao == RAZAO_DESCONHECIDA || razao == RAZAO_TRABALHO || razao == RAZAO_LAZER;
        }
    }

    public static final class GastoEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_GASTOS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GASTOS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GASTOS;

        public final static String TABLE_NAME = "gastos";

        public final static String _ID = BaseColumns._ID;
        public static final String COLUMN_VIAGEM_ID = "viagem_id";
        public final static String COLUMN_DESCRICAO_GASTO = "descricao_gasto";
        public final static String COLUMN_VALOR_GASTO = "valor_gasto";
        public final static String COLUMN_METODO_PAGAMENTO = "metodo_pagamento";
        public final static String COLUMN_DATA_GASTO = "data_gasto";
        public final static String COLUMN_ID_USUARIO = "id_usuario";
    }

    public static final class ImagemGaleriaEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_IMAGENS);

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_IMAGENS;

        public final static String TABLE_NAME = "imagens";

        public final static String _ID = BaseColumns._ID;
        public static final String COLUMN_VIAGEM_ID = "viagem_id";
        public final static String COLUMN_CAMINHO_IMAGEM = "caminho_imagem";

    }
}

