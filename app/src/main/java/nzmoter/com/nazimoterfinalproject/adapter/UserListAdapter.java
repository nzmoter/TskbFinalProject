package nzmoter.com.nazimoterfinalproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import nzmoter.com.nazimoterfinalproject.R;
import nzmoter.com.nazimoterfinalproject.model.User;

/**
 * Created by Bhct on 26.12.2017.
 */

public class UserListAdapter extends BaseAdapter {
    private ArrayList<User> userArrayList;
    private Activity activity;
    LayoutInflater layoutInflater;

    public UserListAdapter(ArrayList<User> userArrayList, Activity activity) {
        this.userArrayList = userArrayList;
        this.activity = activity;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return userArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.layout_userlist_row, null);
        TextView txtFullName = (TextView) view.findViewById(R.id.txtFullName);
        TextView txtDepName = (TextView) view.findViewById(R.id.txtDepName);
        ImageView imgProfil = (ImageView) view.findViewById(R.id.imgProfil);
        User user = userArrayList.get(position);
        txtFullName.setText(user.getFirstName() + " " + user.getLastName());
        txtDepName.setText("Departman Adı:"+user.getDepartmentName());
        Picasso.with(activity).load(user.getImageUrl()).centerCrop().fit().into(imgProfil);
        return view;
    }
}
