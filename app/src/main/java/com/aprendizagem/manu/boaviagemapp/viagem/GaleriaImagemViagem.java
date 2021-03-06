package com.aprendizagem.manu.boaviagemapp.viagem;

import android.Manifest;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aprendizagem.manu.boaviagemapp.Constantes;
import com.aprendizagem.manu.boaviagemapp.R;
import com.aprendizagem.manu.boaviagemapp.adapter.GaleriaImagensAdapter;
import com.aprendizagem.manu.boaviagemapp.adapter.ItemClickListenerAdapter;
import com.aprendizagem.manu.boaviagemapp.database.Contract.ImagemGaleriaEntry;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.util.List;

public class GaleriaImagemViagem extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener {

    private RecyclerView mRecyclerView;
    private TextView mGaleriaVazia;
    private GaleriaImagensAdapter mGaleriaImagemAdapter;
    private String getIdViagem;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 102;

    private static final int REQUEST_CODE_CHOOSE = 23;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhes_viagem_linear);
        getIdViagem = String.valueOf(Constantes.getIdViagemSelecionada());
        if (!verificaPermissaoLeitura()) {
            solicitaPermissaoLeitura();
        }

        mGaleriaVazia = findViewById(R.id.text_view_galeria_vazia);

        Toolbar galeriaImagemToolbar = findViewById(R.id.toolbar_galeria_imagem);
        setSupportActionBar(galeriaImagemToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView tituloPaginaToolbar = findViewById(R.id.titulo_pagina);
        tituloPaginaToolbar.setText(getString(R.string.tela_galeria));

        ImageButton adiconarImagem = findViewById(R.id.image_button_adiciona_viagem);
        adiconarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adicionaImagem();
            }
        });

        mGaleriaImagemAdapter = new GaleriaImagensAdapter(new ItemClickListenerAdapter() {
            @Override
            public void itemFoiClicado(Cursor cursor) {
                int id = cursor.getInt(cursor.getColumnIndex(ImagemGaleriaEntry._ID));
                opcoesParaCliqueDaImagem(id);
            }
        }, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        mRecyclerView = findViewById(R.id.recycler_view_galeria);
        mGaleriaImagemAdapter.notifyDataSetChanged();
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mGaleriaImagemAdapter);

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean verificaPermissaoLeitura() {

        boolean acessoConcedido = false;

        int checaPermissaoDeLeitura = ContextCompat.checkSelfPermission(GaleriaImagemViagem.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        int checaPermissaoDeEscrita = ContextCompat.checkSelfPermission(GaleriaImagemViagem.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (checaPermissaoDeLeitura == PackageManager.PERMISSION_GRANTED &&
                checaPermissaoDeEscrita == PackageManager.PERMISSION_GRANTED) {
            acessoConcedido = true;
        }

        return acessoConcedido;
    }

    private void solicitaPermissaoLeitura() {

        ActivityCompat.requestPermissions(GaleriaImagemViagem.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

    }

    private void adicionaImagem() {

        Matisse.from(GaleriaImagemViagem.this)
                .choose(MimeType.allOf())
                .theme(R.style.Matisse_Dracula)
                .countable(true)
                .maxSelectable(1)
                .imageEngine(new PicassoEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> caminhoDaImagem = Matisse.obtainResult(data);

            ContentValues values = new ContentValues();
            values.put(ImagemGaleriaEntry.COLUMN_VIAGEM_ID, Constantes.getIdViagemSelecionada());
            values.put(ImagemGaleriaEntry.COLUMN_CAMINHO_IMAGEM, String.valueOf(caminhoDaImagem.get(0)));

            getContentResolver().insert(ImagemGaleriaEntry.CONTENT_URI, values);

        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ImagemGaleriaEntry._ID,
                ImagemGaleriaEntry.COLUMN_VIAGEM_ID,
                ImagemGaleriaEntry.COLUMN_CAMINHO_IMAGEM,

        };

        String selection = ImagemGaleriaEntry.COLUMN_VIAGEM_ID + " = '" + getIdViagem + "'";

        return new CursorLoader(this,
                ImagemGaleriaEntry.CONTENT_URI,
                projection,
                selection,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mGaleriaVazia.setVisibility(View.VISIBLE);
        } else {
            mGaleriaVazia.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mGaleriaImagemAdapter.setmCursor(cursor);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void opcoesParaCliqueDaImagem(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(GaleriaImagemViagem.this);
        builder.setItems(R.array.opcoes_item_imagem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        deletarImagem(position);
                        break;
                }
            }
        });
        AlertDialog dialog =
                builder.create();
        dialog.show();
    }

    private void deletarImagem(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.confirmar_exclusao_imagem);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                final int x = 1;
                Cursor cursor = mGaleriaImagemAdapter.getmCursor();
                cursor.moveToPosition(x);
                getContentResolver().delete(
                        Uri.withAppendedPath(ImagemGaleriaEntry.CONTENT_URI, String.valueOf(position)),
                        null, null);

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
