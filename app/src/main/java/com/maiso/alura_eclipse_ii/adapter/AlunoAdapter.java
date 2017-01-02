package com.maiso.alura_eclipse_ii.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maiso.alura_eclipse_ii.ListaAlunos;
import com.maiso.alura_eclipse_ii.R;
import com.maiso.alura_eclipse_ii.modelo.Aluno;

import java.util.List;

import static com.maiso.alura_eclipse_ii.R.drawable.ic_action_no_image;

/**
 * Created by maiso on 01/01/2017.
 */
public class AlunoAdapter extends BaseAdapter{
    private final Activity activity;
    private List<Aluno> alunos;

    public AlunoAdapter(List<Aluno> alunos, Activity activity) {
        this.alunos=alunos;
        this.activity=activity;
    }
    

    @Override
    public int getCount() {
        return alunos.size();
    }

    @Override
    public Object getItem(int i) {
        return alunos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return alunos.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View linha = inflater.inflate(R.layout.item, null);
        TextView nome = (TextView) linha.findViewById(R.id.nome_layout);
        nome.setText(alunos.get(i).getNome());
        if(i%2==0){
            linha.setBackgroundColor(activity.getResources().getColor(R.color.linha_par));
        }
        ImageView foto = (ImageView) linha.findViewById(R.id.foto_layout);
        if(alunos.get(i).getCaminhodaFoto()!=null){
            Bitmap imagem = BitmapFactory.decodeFile(alunos.get(i).getCaminhodaFoto());
            Bitmap imagemReduzida = Bitmap.createScaledBitmap(imagem,100,100,true);
            foto.setImageBitmap(imagemReduzida);
        }else {
            foto.setImageResource(R.drawable.ic_action_no_image);
        }


        return linha;
    }
}
