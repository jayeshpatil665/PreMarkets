package in.specialsoft.premarkets.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.specialsoft.premarkets.Interfaces.ItemClickListener;
import in.specialsoft.premarkets.R;

public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView tv_post_name,tv_post_description,tv_post_id;
    public ImageView img_post_img;

    public ItemClickListener listener;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);

        img_post_img = itemView.findViewById(R.id.img_post_img);
        tv_post_name = itemView.findViewById(R.id.tv_post_name);
        tv_post_description = itemView.findViewById(R.id.tv_post_description);
        tv_post_id = itemView.findViewById(R.id.tv_post_id);
    }


    public  void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

        listener.onClick(v,getAdapterPosition(),false);

    }
}
