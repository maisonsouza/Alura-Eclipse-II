package com.maiso.alura_eclipse_ii;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.maiso.alura_eclipse_ii.dao.AlunoDAO;
import com.maiso.alura_eclipse_ii.modelo.Aluno;

import java.io.File;
import java.io.Serializable;

public class Formulario extends AppCompatActivity {

    private FormularioHelper helper;
    private String caminhodafoto;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        helper = new FormularioHelper(this);

        final Aluno alunoSelecionado = (Aluno) getIntent().getSerializableExtra("alunoSelecionado");
        Toast.makeText(this,"Aluno: "+alunoSelecionado,Toast.LENGTH_SHORT).show();
        if(alunoSelecionado!=null){
            helper.colocaAlunoNoFormulario(alunoSelecionado);
        }

        final Button botao = (Button) findViewById(R.id.botao);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Aluno aluno = helper.pegaAlunoDoFormulario();
                DBHelper helper = new DBHelper(Formulario.this);
                AlunoDAO dao = new AlunoDAO(helper);
                if(alunoSelecionado != null){
                    aluno.setId(alunoSelecionado.getId());
                    botao.setText("Alterar");
                    dao.atualizar(aluno);
                    Toast.makeText(Formulario.this,"Aluno alterado: "+aluno.getNome()+"salvo",Toast.LENGTH_LONG).show();
                }else{
                    dao.insere(aluno);
                }
                helper.close();
                Toast.makeText(Formulario.this,"Aluno: "+aluno.getNome()+"salvo",Toast.LENGTH_LONG).show();
                finish();
            }
        }
        );

        ImageView foto = helper.getCampoFoto();
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vaipraCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                caminhodafoto = getExternalFilesDir(null)+"/"+System.currentTimeMillis()+".jpg";
                File arquivoFoto = new File(caminhodafoto);
                Uri photoURI = FileProvider.getUriForFile(Formulario.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        arquivoFoto);
                vaipraCamera.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(vaipraCamera,REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK){
            if(resultCode==Activity.RESULT_OK){
                helper.carregaImagem(caminhodafoto);
            }else{
                caminhodafoto=null;
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
