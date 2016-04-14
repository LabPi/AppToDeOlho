package com.jp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jp.beans.Post;
import com.jp.todeolho.PostActivity;
import com.jp.todeolho.R;


import java.util.ArrayList;

/**
 * Created by jp on 26/11/15.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>
{
    //Variaveis
    private ArrayList<Post> posts;
    private int position = 0;
    private Context context;
    private PersonViewHolder holder;

    public RVAdapter(ArrayList<Post> posts, Context context)
    {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_conteudo, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        position = i;
        holder = personViewHolder;
        personViewHolder.usuario.setText(posts.get(i).getName());
        personViewHolder.descricao.setText(posts.get(i).getDescricao());
        personViewHolder.foto.setImageResource(posts.get(i).getPhotoId());
        personViewHolder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                context.startActivity(new Intent(context, PostActivity.class));
            }
        });
/*        personViewHolder.imgCurir.setImageResource((posts.get(i).isCurtir())?R.mipmap.ic_curtir:R.mipmap.ic_ncurtir);*/
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv = null;
        TextView usuario = null;
        TextView descricao = null;
        ImageView foto = null;
        ImageView imgCurir = null;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            usuario = (TextView)itemView.findViewById(R.id.txvUsuario);
            descricao = (TextView)itemView.findViewById(R.id.txvDescricao);
            foto = (ImageView)itemView.findViewById(R.id.person_photo);
            imgCurir = (ImageView)itemView.findViewById(R.id.imgCurtir);
        }
    }
}
