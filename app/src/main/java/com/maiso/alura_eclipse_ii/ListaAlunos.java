package com.maiso.alura_eclipse_ii;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.maiso.alura_eclipse_ii.adapter.AlunoAdapter;
import com.maiso.alura_eclipse_ii.dao.AlunoDAO;
import com.maiso.alura_eclipse_ii.modelo.Aluno;

import java.util.List;

public class ListaAlunos extends AppCompatActivity {


    private ListView listagem;
    private Aluno aluno;
    public static final int CODIGO_SMS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listagem_alunos);

        if (ActivityCompat.checkSelfPermission(ListaAlunos.this, android.Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ListaAlunos.this, new String[]{android.Manifest.permission.RECEIVE_SMS}, CODIGO_SMS);
        }



        listagem = (ListView) findViewById(R.id.lista);
        registerForContextMenu(listagem);


        listagem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int posicao, long l) {
                Aluno alunoASerAlterado = (Aluno) adapter.getItemAtPosition(posicao);
                Intent vaiproFormulario = new Intent(ListaAlunos.this, Formulario.class);
                vaiproFormulario.putExtra("alunoSelecionado",alunoASerAlterado);
                startActivity(vaiproFormulario);
            }
        });
        
        listagem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int posicao, long l) {
                 aluno = (Aluno) adapter.getItemAtPosition(posicao);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista_alunos,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_novo:
                Intent vaiproFormulario = new Intent(this,Formulario.class);
                startActivity(vaiproFormulario);
                break;

            case R.id.mapa:
                Intent vaiproMapa = new Intent(Intent.ACTION_VIEW);
                Uri localizacaoDoAluno = Uri.parse("geo:0,0?z=14&q="+aluno.getEndereco());
                vaiproMapa.setData(localizacaoDoAluno);
                item.setIntent(vaiproMapa);

        }




        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    private void carregaLista() {
        DBHelper helper = new DBHelper(this);
        AlunoDAO dao = new AlunoDAO(helper);
        List<Aluno> alunos = dao.getLista();

        AlunoAdapter adapter = new AlunoAdapter(alunos,this);
        listagem.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem ligar = menu.add("Ligar");
        Intent intentLigar = new Intent(Intent.ACTION_CALL);
        Uri telefoneDoAluno = Uri.parse("tel:" +aluno.getTelefone());
        intentLigar.setData(telefoneDoAluno);
        ligar.setIntent(intentLigar);


        final MenuItem site = menu.add("Navegar no Site");
        site.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intentNavegar = new Intent(Intent.ACTION_VIEW);
                Uri siteDoAluno = Uri.parse("http://"+aluno.getSite());
                intentNavegar.setData(siteDoAluno);
                site.setIntent(intentNavegar);
                return false;
            }
        });


        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                DBHelper helper = new DBHelper(ListaAlunos.this);
                AlunoDAO dao = new AlunoDAO(helper);
                dao.deletar(aluno);
                helper.close();
                carregaLista();
                return false;
            }
        });

        MenuItem mensagem = menu.add("Enviar SMS");
        Intent enviarSms = new Intent(Intent.ACTION_VIEW);
        Uri telefoneDoAlunosms = Uri.parse("sms:" + aluno.getTelefone());
        enviarSms.setData(telefoneDoAlunosms);
        mensagem.setIntent(enviarSms);

        menu.add("Achar no Mapa");

        final MenuItem email = menu.add("Enviar E-mail");

        email.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intentEmail = new Intent(Intent.ACTION_SEND);
                intentEmail.setType("message/rfc822");
                intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[] {"email-do-aluno@yahoo.com.br"});
                intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Testando subject do email");
                intentEmail.putExtra(Intent.EXTRA_TEXT, "Testando corpo do email");
                email.setIntent(intentEmail);
                return false;
            }
        });


    }

}
