package fragments;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.spit.R;
import com.example.admin.spit.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutMeFragment extends Fragment {

    TextView username_textview;
    TextView gender_textview;
    TextView department_textview;
    TextView uid_textview;
    TextView email_textview;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    public AboutMeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        retreiveData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_me, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username_textview=(TextView)view.findViewById(R.id.username_textview);
        gender_textview=(TextView)view.findViewById(R.id.gender_textview);
        department_textview=(TextView)view.findViewById(R.id.department_textview);
        uid_textview=(TextView)view.findViewById(R.id.uid_textview);
        email_textview=(TextView)view.findViewById(R.id.email_textview);


    }

    public void retreiveData()
    {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username_textview.setText(user.Name);
                gender_textview.setText(user.Gender);
                department_textview.setText(user.Department);
                uid_textview.setText(user.UID);
                email_textview.setText(user.Email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}