package photon.app.mediscanner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>  {

    private final List<String> mFileList;
    private final Activity mActivity;

    public MyAdapter(List<String> mFileList, Activity mActivity) {
        this.mFileList = mFileList;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_image_frame,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide
                .with(mActivity)
                .load(mFileList.get(position))
                .override(200, 200)
                .centerCrop()
                .into(holder.imageResource);

        final int itemPosition = holder.getAdapterPosition();
        holder.imageResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity, mFileList.get(itemPosition), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageResource;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageResource = itemView.findViewById(R.id.sifImageView);

        }
    }

}
