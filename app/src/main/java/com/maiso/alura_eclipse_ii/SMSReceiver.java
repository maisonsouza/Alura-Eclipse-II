package com.maiso.alura_eclipse_ii;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.maiso.alura_eclipse_ii.dao.AlunoDAO;

/**
 * Created by maiso on 02/01/2017.
 */

public class SMSReceiver extends BroadcastReceiver {
    private BroadcastReceiver bateria;
    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] mensagens = (Object[]) intent.getExtras().get("pdus");
        byte[] mensagem = (byte[]) mensagens[0];
        SmsMessage sms = SmsMessage.createFromPdu(mensagem);
        String telefone = sms.getOriginatingAddress();
        DBHelper helper = new DBHelper(context);
        boolean SMSEhDeAluno = new AlunoDAO(helper).ehAluno(telefone);
        if(SMSEhDeAluno){
            MediaPlayer musica = MediaPlayer.create(context, R.raw.msg);
            musica.start();
            Toast.makeText(context,"SMS de Aluno"+telefone,Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,"SMS NÃO é de Aluno"+telefone,Toast.LENGTH_LONG).show();
        }

        int valor = intent.getIntExtra("level", 0);
        Toast.makeText(context, valor + "%", Toast.LENGTH_SHORT).show() ;
        context.registerReceiver(bateria, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }



}

