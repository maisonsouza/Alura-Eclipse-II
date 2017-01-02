package com.maiso.alura_eclipse_ii;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.maiso.alura_eclipse_ii.modelo.Aluno;

/**
 * Created by maiso on 10/12/2016.
 */

public class FormularioHelper {

    private final EditText campoNome;
    private final EditText campoTelefone;
    private final EditText campoEndereco;
    private final EditText campoSite;
    private final RatingBar campoNota;
    private Aluno aluno;
    private final ImageView campoFoto;

    public FormularioHelper(Activity activity) {
        aluno = new Aluno();
        campoNome = (EditText) activity.findViewById(R.id.editnome);
        campoTelefone = (EditText) activity.findViewById(R.id.edittelefone);
        campoEndereco = (EditText) activity.findViewById(R.id.editendereco);
        campoSite = (EditText) activity.findViewById(R.id.editsite);
        campoNota = (RatingBar) activity.findViewById(R.id.nota);
        campoFoto = (ImageView) activity.findViewById(R.id.imageFoto);
    }
    
    public Aluno pegaAlunoDoFormulario(){

        String nome = campoNome.getText().toString();
        String endereco = campoEndereco.getText().toString();
        String telefone = campoTelefone.getText().toString();
        String site = campoSite.getText().toString();
        int nota = campoNota.getProgress();


        aluno.setNome(nome);
        aluno.setEndereco(endereco);
        aluno.setTelefone("92"+telefone);
        aluno.setSite(site);
        aluno.setNota(Double.valueOf(nota));
        
       return aluno; 
    }

    public void colocaAlunoNoFormulario(Aluno alunoSelecionado) {
        this.aluno = alunoSelecionado;
        campoNome.setText(alunoSelecionado.getNome());
        campoEndereco.setText(alunoSelecionado.getEndereco());
        campoTelefone.setText(alunoSelecionado.getTelefone());
        campoSite.setText(alunoSelecionado.getSite());
        campoNota.setRating(alunoSelecionado.getNota().floatValue());
        if(getCampoFoto()!=null){
            carregaImagem(aluno.getCaminhodaFoto());
        }else{
            campoFoto.setImageResource(R.drawable.ic_action_no_image);
        }
    }

    public ImageView getCampoFoto() {
        return campoFoto;
    }

    public void carregaImagem(String caminhodoArquivo){
        aluno.setCaminhodaFoto(caminhodoArquivo);
        Bitmap imagem = BitmapFactory.decodeFile(caminhodoArquivo);
        Bitmap imagemreduzida = Bitmap.createScaledBitmap(imagem,100,100,true);
       campoFoto.setImageBitmap(imagemreduzida);
    }
}
