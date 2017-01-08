package am.instigatemobile.pokasapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import am.instigatemobile.pokasapp.R;
import am.instigatemobile.pokasapp.models.Blog;

public class BlogActivity extends AppCompatActivity {

    private RecyclerView blogList;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        database = FirebaseDatabase.getInstance().getReference().child("Blog");

        blogList = (RecyclerView) findViewById(R.id.blogList);
        blogList.setHasFixedSize(true);
        blogList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blog_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add) {
            startActivity(new Intent(BlogActivity.this,PostActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.blog_item,
                BlogViewHolder.class,
                database

        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setTitle(model.getDesc());
                viewHolder.setImage(getApplicationContext(), model.getImage());
            }
        };

        blogList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View view;


        public BlogViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setTitle(String title) {
            TextView blogItemTitle = (TextView) view.findViewById(R.id.blogItemTitle);
            blogItemTitle.setText(title);
        }

        public void setDesc(String desc) {
            TextView blogItemdesc = (TextView) view.findViewById(R.id.blogItemDesc);
            blogItemdesc.setText(desc);
        }

        public void setImage(Context context, String image) {
            ImageView postImage = (ImageView) view.findViewById(R.id.blogItemImage);
            Picasso.with(context).load(image).into(postImage);
        }

    }
}
